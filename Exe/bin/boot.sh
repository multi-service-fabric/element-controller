##
## Copyright(c) 2016 Nippon Telegraph and Telephone Corporation
##

## Environment Definition
HOST="10.34.131.64"
PORT="18080"

IF_ADDRESS=""
RESULT=""

function creSendMsg() {

    in=$1

    message="{\"management_if_address\":\""${in}"\"}"

    echo ${message}
}

function postSend() {

    result=$1
    sendText=$2

    curl -sS --connect-timeout 5 -m 60 -w ' resultCode:%{http_code}' -H "Accept: application/json" -H "Content-type: application/json" -g -d "${sendText}" -X POST ${HOST}:${PORT}/v1/internal/node_boot/"${result}" 2>&1

}

function chkResponse() {

    response=$1

    ret="0"

    err=`echo "${response}" | grep -c "resultCode\:000"`
    if [ "${err}" -eq 0 ]; then
        resultCode=`echo ${response} | sed -e "s/.*resultCode://"`

        if [ "${resultCode}" != "200" ]; then
            logger -t EcMainModule "responCode=""${resultCode}"
            ret=1
        fi
    else
        err=`echo "${response}" | egrep -c curl\|Failed`
        if [ "${err}" -ne 0 ]; then
            outLogMsg=`echo ${response} | sed -e "s/ resultCode.*//"`
            logger -t EcMainModule "curl command failed.(""${outLogMsg}"")"
            ret=1
        else
            logger -t EcMainModule "curl command failed.("detail error reason is unknown")"
            ret=1
        fi
    fi
    return ${ret}
}


if [ $# = 1 ]; then
    IF_ADDRESS=$1

    array=( `echo $0 | tr -s '/' ' '`)
    last_index=`expr ${#array[@]} - 1`
    name=${array[${last_index}]}

    if [ "${name}" = "boot_fail.sh" ]; then
        RESULT="failed"
    elif [ "${name}" = "boot_success.sh" ]; then
        RESULT="success"
    else
        echo "<error> call script illegal ("$0")."
        logger -t EcMainModule "<error> call script illegal ("$0")."
        exit 1
    fi
else
    echo "<error> boot.sh input parameter illegal(paramNum="$#")."
    logger -t EcMainModule "<error> boot.sh input parameter illegal(paramNum="$#")."
    exit 1
fi

msg=`creSendMsg "${IF_ADDRESS}"`

res=`postSend "${RESULT}" "${msg}"`
echo ${res}

chkResponse "${res}"
ret=$?

if [ "${ret}" != "0" ]; then
    echo "<error> boot.sh Failed."
    logger -t EcMainModule "<error> boot.sh Failed."
fi
