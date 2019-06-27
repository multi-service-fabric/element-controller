#!/bin/bash
##
## SNMPTrap Functional Part linkup.sh Link Up Notification Script
##
## Shell script which is triggered by the LinkUp event notification in snmptrapd.conf
## and does REST interface notification to EC.
## (URI parameters are in JSON format)
##
## Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
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

    in=$1

    message="{"

    count=0
    for data in ${in} ; do

        if [ ${count} -eq 2 ]; then
            cutIp=`echo ${data} | cut -d"[" -f2 | cut -d"]" -f1`
            message=${message}"\"src_host_ip\":\""${cutIp}"\","
        elif [ ${count} -eq 3 ]; then
            message=${message}"\"varbind\": ["
        fi

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

        count=`expr ${count} + 1`
    done

    message=$(echo ${message/%?/})
    message=${message}"]}"

    echo ${message}
}

## POST Message Send Process
function postSend() {

    sendText=$1

    curl -sS --connect-timeout 5 -m 60 -w ' resultCode:%{http_code}' -H "Accept: application/json" -H "Content-type: application/json" -g -d "${sendText}" -X POST ${HOST}:${PORT}/v1/internal/snmp/linkup 2>&1

}

## POST Response Analysis Process
function chkResponse() {

    response=$1

    ret="0"

    err=`echo "${response}" | grep -c "resultCode\:000"`
    if [ "${err}" -eq 0 ]; then

        resultCode=`echo "${response}" | sed -e "s/.*resultCode://"`

        if [ "${resultCode}" != "200" ]; then
            resMsg=`echo "${response}" | cut -d"," -f2 | cut -d":" -f2 | cut -d"\"" -f2`
            logger -t EcMainModule "responCode=""${resultCode}"
            ret=1
        fi
    else 
        err=`echo "${response}" | egrep -c curl\|Failed`
        if [ "${err}" -ne 0 ]; then
            outLogMsg=`echo "${response}" | sed -e "s/ resultCode.*//"`
            logger -t EcMainModule "curl command failed.(""${outLogMsg}"")"
            ret=1
        else
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

