##
## Copyright(c) 2016 Nippon Telegraph and Telephone Corporation
##

## Environment Definition
HOST="192.168.53.132"
PORT="18080"
RETRYNUM=10

EVENT=""

JARFILE="msf.ecmm.ope.execute.ecstate.ECMainStarter"
CONFFILE="/usr/ec_main/conf/ec_main.conf"

CLASSPATH="/usr/ec_main/conf/"
for name in `ls /usr/ec_main/lib/*.jar`; do
  CLASSPATH="${CLASSPATH}:$name"
done

DEFINE="-Dlog4j.configurationFile=file:///usr/ec_main/conf/log4j2.xml"

function start() {

    cnt=`ps -ef | grep ${JARFILE} | grep -c -v grep`
    if [ "${cnt}" != "0" ]; then
        logger -t EcMainModule "command execute failed.(${JARFILE} is already running)"
        echo "Error : ${JARFILE} is already running"
        ret=1
    else
        java -cp ${CLASSPATH} ${DEFINE} ${JARFILE} ${CONFFILE} > /dev/null 2>&1 &
        ret=0
    fi
    return ${ret}
}

function stop() {
    parm="chgover"
    res=`postSend POST "${parm}"`

    chkStopResponse "${res}"
    ret=$?

    if [ "${ret}" != "0" ]; then
        echo "Error : EcMainModule stop failed."
    fi
    echo ${res} | sed -e "s/resultCode.*//"

    return ${ret}
}

function forcestop() {

    pid=`ps -ef | grep ${JARFILE} | grep -v grep`
    if [ -z "${pid}" ]; then
        logger -t EcMainModule "command execute failed.(${JARFILE} is not running)"
        echo "Error : ${JARFILE} is not running"
        ret=7
    else
        pid=`echo ${pid} | cut -d" " -f2`

        kill -9 ${pid}
        ret=0
    fi
    return ${ret}
}

function status() {
    pid=`ps -ef | grep ${JARFILE} | grep -v grep`
    if [ -z "${pid}" ]; then
        logger -t EcMainModule "command execute failed.(${JARFILE} is not running)"
        echo "Error : ${JARFILE} is not running"
        ret=7
        return ${ret}
    fi
    parm=""
    res=`postSend GET "${parm}"`

    chkStatusResponse "${res}"
    ret=$?

    if [ "${ret}" != "0" -a "${ret}" != "10" ]; then
        echo "Error : EcMainModule get status failed."
    fi
    if [ "${ret}" != "10" ]; then
        echo ${res} | sed -e "s/resultCode.*//"
    fi

    return ${ret}
}

function postSend() {

    method=$1
    if [ -z $2 ]; then
        in=""
    else
        in="/$2"
    fi

    curl -sS --connect-timeout 5 -m 60 -w ' resultCode:%{http_code}' -H "Accept: application/json" -X ${method} ${HOST}:${PORT}/v1/internal/ec_ctrl/"${EVENT}${in}" 2>&1

}

function chkStopResponse() {

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
            curlErrCode=`echo ${response} | cut -d"(" -f2 | cut -d")" -f1`

            if [ "${curlErrCode}" != "52" -a "${curlErrCode}" != "56" ]; then
                outLogMsg=`echo ${response} | sed -e "s/ resultCode.*//"`
                logger -t EcMainModule "curl command failed.(""${outLogMsg}"")"
                ret=7
            fi
        else
            logger -t EcMainModule "curl command failed.("detail error reason is unknown")"
            ret=7
        fi
    fi
    return ${ret}
}


function chkStatusResponse() {

    response=$1

    ret="0"

    err=`echo "${response}" | grep -c "resultCode\:000"`
    if [ "${err}" -eq 0 ]; then
        resultCode=`echo ${response} | sed -e "s/.*resultCode://"`

        if [ "${resultCode}" != "200" ]; then
            logger -t EcMainModule "responCode=""${resultCode}"
            ret=1
        else
            bodyStatus=`echo "${response}" | grep -c "stop"`
            if [ "${bodyStatus}" -ne 0 ]; then
                ret=7
            fi
        fi
    else
        ret=7
        err=`echo "${response}" | egrep -c curl\|Failed`
        if [ "${err}" -ne 0 ]; then
            curlErrCode=`echo ${response} | cut -d"(" -f2 | cut -d")" -f1`

            if [ "${curlErrCode}" = "7" ]; then
                ret=10
            fi
        else
            logger -t EcMainModule "curl command failed.("detail error reason is unknown")"
        fi
    fi
    return ${ret}
}


if [ $# -ne 1 ]; then
    logger -t EcMainModule "command execute failed.(Number of input parameter is incorrect)"
    echo "Error : Number of input parameter is incorrect"
    echo "Usage : ./ec_ctl.sh <start|stop|forcestop|status>"
    ret=1
else
    if [ "$1" = "start" ]; then
        start
        ret=$?
    elif [ "$1" = "stop" ]; then
        EVENT="stop"
        stop
        ret=$?
    elif [ "$1" = "forcestop" ]; then
        EVENT="forcestop"
        forcestop
        ret=$?
    elif [ "$1" = "status" ]; then
        EVENT="statusget"
        cnt=0
        while [ "$cnt" != "${RETRYNUM}" ]
        do
            status
            ret=$?
            if [ "${ret}" != "10" ]; then
                break;
            fi
            cnt=$(( cnt+1 ))
            sleep 1
        done
        if [ "${ret}" = "10" ]; then
            echo "Error : EcMainModule get status retry timeout."
            ret=7;
        fi
    else
        logger -t EcMainModule "command execute failed.(String of input parameter is incorrect)"
        echo "Error : String of input parameter is incorrect"
        echo "Usage : ./ec_ctl.sh <start|stop|forcestop|status>"
        ret=1
    fi
fi

if [ "${ret}" != "0" ]; then
    logger -t EcMainModule "<error> ec_ctl.sh Failed."
fi

exit ${ret}
