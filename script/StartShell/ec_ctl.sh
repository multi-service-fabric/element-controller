#!/bin/bash
##
## EC Start-up Script ec_ctl.sh
## Parameter (mandatory) <start|stop|forcestop|status>
##
## Shell script which is launched by RA or a maintenance operator
## and does REST interface notification to EC.
## (URI parameters are in JSON format)
##
## Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
##

## Environment Definition
HOST="192.168.51.52"
PORT="18080"
RETRYNUM=10
FC_HOST="192.168.51.54"
FC_PORT="18081"

## Input Argument (only stop/status are used)
EVENT=""

## Environment Variable (only start/stop is used)
JARFILE="msf.ecmm.ope.execute.ecstate.ECMainStarter"
CONFFILE="/usr/ec_main/conf/ec_main.conf"
EXNTED_CONFFILE="/usr/ec_main/lib/extend_operation.conf"

## For Start-up Confirmation
CHECKFILE="/usr/ec_main/lib/EcMainModule.jar:"

## Getting REST timeout time and the number of retries from the configuration
RESTTIMEOUT=`grep rest_timeout ${CONFFILE} | sed s/rest_timeout=//`
RESTRETRYNUM=`grep rest_retry_num ${CONFFILE} | sed s/rest_retry_num=//`


CLASSPATH="/usr/ec_main/conf/"
for name in `ls /usr/ec_main/lib/*.jar`; do
  CLASSPATH="${CLASSPATH}:$name"
done

DEFINE="-Dlog4j.configurationFile=file:///usr/ec_main/conf/log4j2.xml"

## Constant Definition
#EC_NORMAL_STOP="NORMAL_STOP"
EC_CHANGEOVER="changeover"

## EC Main Module Start-up Process
function start() {

    if [ ! -f ${EXNTED_CONFFILE} ]; then
        EXNTED_CONFFILE=""
    fi
    
    cnt=`ps -ef | grep ${CHECKFILE} | grep -c -v grep`
    if [ "${cnt}" != "0" ]; then
        logger -t EcMainModule "command execute failed.(${CHECKFILE} is already running)"
        echo "Error : ${CHECKFILE} is already running"
        ret=1
    else
        #echo java -cp ${CLASSPATH} ${DEFINE} ${JARFILE} ${CONFFILE} ${EXNTED_CONFFILE}
        java -cp ${CLASSPATH} ${DEFINE} ${JARFILE} ${CONFFILE} ${EXNTED_CONFFILE} > /dev/null 2>&1 &
        ret=0
        
    fi
    return ${ret}
}

## EC Main Module Termination Process
function stop() {

    if [ "$1" != ${EC_CHANGEOVER} ]; then
        parm="/normal"
    else
        parm="/chgover"
    fi

    res=`postSend POST "${parm}"`

    chkStopResponse "${res}"
    ret=$?

    if [ "${ret}" != "0" ]; then
        echo "Error : EcMainModule stop failed."
    fi
    echo ${res} | sed -e "s/resultCode.*//"

    return ${ret}
}

## EC Main Module Forceful Process
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

## EC Main Module Status Confirmation
function status() {
    pid=`ps -ef | grep ${CHECKFILE} | grep -v grep`
    if [ -z "${pid}" ]; then
        logger -t EcMainModule "command execute failed.(${CHECKFILE} is not running)"
        echo "Error : ${CHECKFILE} is not running"
        ret=7
        return ${ret}
    fi
    parm="?controller=ec&get_info=ctr-state"
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

## POST Message Send Process
function postSend() {

    method=$1
    if [ -z $2 ]; then
        in=""
    else
        in="$2"
    fi

    curl -sS --connect-timeout 5 -m 60 -w ' resultCode:%{http_code}' -H "Accept: application/json" -X ${method} ${HOST}:${PORT}/v1/internal/ec_ctrl/"${EVENT}${in}" 2>&1

}

## POST Response (stop) Analysis Process
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


## POST Response (status) Analysis Process
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


## The Number of Input Parameters Check
if [ $# -eq 0 -o $# -gt 2 ]; then
    logger -t EcMainModule "command execute failed.(Number of input parameter is incorrect)"
    echo "Error : Number of input parameter is incorrect"
    echo "Usage : ./ec_ctl.sh <start|stop[NORMAL_STOP]|forcestop|status>"
    ret=1
else
    if [ "$1" = "start" ]; then
        start
        ret=$?
    elif [ "$1" = "stop" ]; then
        EVENT="stop"
        stop $2
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

## Ending Log
if [ "${ret}" != "0" ]; then
    logger -t EcMainModule "<error> ec_ctl.sh Failed."
fi

## Ending Code Return
exit ${ret}

