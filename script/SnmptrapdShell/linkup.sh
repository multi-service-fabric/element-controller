#!/bin/bash
##
## SNMPTrap Functional Part linkup.sh Link Up Notification Script
##
## Shell script which is triggered by the LinkUp event notification in snmptrapd.conf
## and does REST interface notification to EC.
## (URI parameters are in JSON format)
##
## Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
##

## Environment Definition
HOST="10.34.131.64"
PORT="18080"

## Standard Input Acquisition (Join)
function getStdin() {
    if [ -p /dev/stdin ] ; then
        sendText=$(cat -)
    else
        sendText=$@
    fi
    echo ${sendText}
}

## POST Message Data Creation
function creSendMsg() {

    # snmptrapd からの受信文字列
    # ex) msf01
    #     UDP: 
    #     [10.34.131.64]:42274->[10.34.131.64]:162 
    #     .1.3.6.1.2.1.1.3.0 0:0:00:00.10 
    #     .1.3.6.1.6.3.1.1.4.1.0 .1.3.6.1.6.3.1.1.5.3
    #     .1.3.6.1.6.3.18.1.3.0 192.168.0.80
    #     .1.3.6.1.6.3.18.1.4.0 "public"

    # 入力パラメータを取得
    in=$1

    # 出力パラメータに初期値設定
    message="{"

    count=0
    # 入力パラメータはデータ単位に分割してループ
    for data in ${in} ; do

        # データ名を設定
        if [ ${count} -eq 2 ]; then
            cutIp=`echo ${data} | cut -d"[" -f2 | cut -d"]" -f1`
            message=${message}"\"src_host_ip\":\""${cutIp}"\","
        elif [ ${count} -eq 3 ]; then
            message=${message}"\"varbind\": ["
        fi

        # Oid & Value値を設定
        if [ ${count} -ge 3 ]; then
            if [ `expr ${count} % 2` -ne 0 ]; then
                message=${message}"{\"oid\":\""${data}"\","
            else
                if [ $(echo ${data} | cut -c 1) = "\"" ]; then
                    message=${message}"\"value\":"${data}"},"
                else
                    message=${message}"\"value\":\""${data}"\"},"
                fi
            fi
        fi

        # ループカウンタをインクリメント
        count=`expr ${count} + 1`
    done

    # 最後の文字(,)を削除
    message=$(echo ${message/%?/})
    # 出力パラメータの閉じ設定
    message=${message}"]}"

    echo ${message}
}

## POST Message Send Process
function postSend() {

    sendText=$1

    # RESTメッセージ送信
    curl -sS --connect-timeout 5 -m 60 -w ' resultCode:%{http_code}' -H "Accept: application/json" -H "Content-type: application/json" -g -d "${sendText}" -X POST ${HOST}:${PORT}/v1/internal/snmp/linkup 2>&1

}

## POST Response Analysis Process
function chkResponse() {

    # curlコマンド失敗(ex.curl: (7) Failed connect to 10.34.131.79:1234; 接続がタイムアウトしました)
    # 応答メッセージ(ex.{"resultCode":10,"message":"OK!!"})
    response=$1

    # 解析結果
    ret="0"

    # コマンド実行結果の解析
    err=`echo "${response}" | grep -c "resultCode\:000"`
    if [ "${err}" -eq 0 ]; then
        # 応答レスポンスあり

        # resultCodeの抜き出し
        resultCode=`echo "${response}" | sed -e "s/.*resultCode://"`

        # 応答結果チェック
        if [ "${resultCode}" != "200" ]; then
            # エラーログ出力
            resMsg=`echo "${response}" | cut -d"," -f2 | cut -d":" -f2 | cut -d"\"" -f2`
            logger -t EcMainModule "responCode=""${resultCode}"
            ret=1
        fi
    else 
        err=`echo "${response}" | egrep -c curl\|Failed`
        if [ "${err}" -ne 0 ]; then
            # curlコマンド失敗
            outLogMsg=`echo "${response}" | sed -e "s/ resultCode.*//"`
            logger -t EcMainModule "curl command failed.(""${outLogMsg}"")"
            ret=1
        else
            # 解析不可能応答
            logger -t EcMainModule "curl command failed.("detail error reason is unknown")"
            ret=1
        fi
    fi
    return ${ret}
}

## Receiving Standard Input
input=`getStdin $@`

## REST Message Data Creation
msg=`creSendMsg "${input}"`

## REST Message Send
res=`postSend "${msg}"`
echo ${res}

## Response Analysis
chkResponse "${res}"
ret=$?

# Ending Log
if [ "${ret}" != "0" ]; then
    logger -t EcMainModule "<error> linkup.sh Failed."
fi

