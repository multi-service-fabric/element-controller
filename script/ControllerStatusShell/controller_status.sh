#!/bin/bash
##
##  Common Functional Part controller_status.sh Controller Status Acquisition Script
##
## Shell script which acquires the controller status information of whom calling and returns it
##
## Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
##

#D=`date +"%Y-%m-%d %H:%M:%S.%3N"`

## Character Code Configuration
LANG=C

## Checking the number of input arguments
if [ $# -ne 6 ] ; then
    echo "param num error"
    exit 1
fi

## Input Argument
## top Command Execution Yes/No
topExecution=$1
## nproc Command Execution Yes/No
nprocExecution=$2
## df Command Execution Yes/No
dfExecution=$3
## sar Command Execution Yes/No
sarExecution=$4
## hostname Command Execution Yes/No
hosutnameExecution=$5
## Controller PID
controllerPid=$6

## top Command Execution
function topCommand() {
    top_result=".top_result_$$"
    if [ $1 = "0" ]; then
      top -b -n 1 | sed -e "s/,/, /g" | sed -e "s/:/: /g" > ${top_result}
    else
      top -b -n 1 -p $1 | sed -e "s/,/, /g" | sed -e "s/:/: /g" > ${top_result}
    fi

    id=`cat ${top_result}|grep %Cpu|awk '{print $8}'`
    if [ "${id}" = "" ] ; then
        id=0
    fi

    free=`cat ${top_result}|grep buff/cache|awk '{print $6}'`
    if [ "${free}" = "" ] ; then
        free=0
    fi
    free=`checkMem $free`

    used=`cat ${top_result}|grep buff/cache|awk '{print $8}'`
    if [ "${used}" = "" ] ; then
        used=0
    fi
    used=`checkMem $used`

    buffers=`cat ${top_result}|grep buff/cache|awk '{print $10}'`
    if [ "${buffers}" = "" ] ; then
        buffers=0
    fi
    buffers=`checkMem $buffers`

    swapused=`cat ${top_result}|grep Swap|awk '{print $7}'`
    if [ "${swapused}" = "" ] ; then
        swapused=0
    fi

    res=`cat ${top_result}|tail -n 1 |awk '{print $6}'`
    if [ "${res}" = "" ] ; then
        res=0
    elif [ "${res: -1}" = "g" ] ; then
        res=${res/%?/}
        res=`echo "scale=4; ${res} * 1024.0 * 1024.0 + 0.5" | bc | cut -d '.' -f 1`
    elif [ "${res: -1}" = "m" ] ; then
        res=${res/%?/}
        res=`echo "scale=4; ${res} * 1024.0 + 0.5" | bc | cut -d '.' -f 1`
    fi

    cpu=`cat ${top_result}|tail -n 1 |awk '{print $9}'`
    if [ "${cpu}" = "" ] ; then
        cpu=0
    fi
    \rm -rf ${top_result}
}

## nproc Command Execution
function nprocCommand() {

    nprocdata=`nproc`
}

## df Command Execution
function dfCommand() {

    dfdata=(`df -k|grep -v 1K-blocks`)

    dfalllist=()
    dfcount=1
    dfret=""
    for df in "${dfdata[@]}"
    do
        dflist+=("${df}")
        dfcount=`expr $dfcount + 1`
        if [ "${dfcount}" -eq "7" ]; then
            dfliststr=${dflist[@]}
            dfret=`echo "${dfret}\"$dfliststr"\",`
            dfcount=1
            dfalllist+=("${dfliststr}")
            dflist=()
        fi    
    done
    dfret=`echo $dfret|sed -e "s/.\$//"`
}

## sar Command Execution
function sarCommand() {
    saralllist=()
    sarcount=0

    sardata=(`sar 1 1 -n DEV|grep -v -a Average|grep -v -a IFACE|awk '{$10 = ""; print}'`)
    sarret=""
    for sar in "${sardata[@]}"
    do
        if [ "${sarcount}" -ge 7 ]; then
            sarlist+=("${sar}")
            if [ "${sarcount}" -eq "15" ]; then
                sarliststr=${sarlist[@]}
                sarret=`echo "${sarret}\"$sarliststr"\",`
                sarcount=6
                saralllist+=("${sarliststr}")
                sarlist=()
            fi    
        fi
        sarcount=`expr $sarcount + 1`
    done
    sarret=`echo $sarret|sed -e "s/.\$//"`
}

## hostname Command Execution
function hostnameCommand() {

    hostnamedata=`hostname`
}

function checkMem() {
  memsize=$1
  if [ `echo $memsize | grep 'm'` ] ; then
    echo `expr ${memsize:0:-1} \* 1024`
  elif [ `echo $memsize | grep 'g'` ] ; then
    echo `expr ${memsize:0:-1} \* 1024 \* 1024`
  else
    echo $memsize
  fi
}
## Main

if [ "${topExecution}" -ne "0" ]; then
    topCommand ${controllerPid}
else
    id=0
    free=0
    used=0
    buffers=0
    swapused=0
    res=0
    cpu=0
fi

if [ "${nprocExecution}" -ne "0" ]; then
    nprocCommand
else
    nprocdata=0
fi

if [ "${dfExecution}" -ne "0" ]; then
    dfCommand
fi

if [ "${sarExecution}" -ne "0" ]; then
    sarCommand
fi

if [ "${hosutnameExecution}" -ne "0" ]; then
    hostnameCommand
fi

free=`checkMem $free`
used=`checkMem $used`
buffers=`checkMem $buffers`
swapused=`checkMem $swapused`

## Aggregating Command Execution Results in json Format
responsejson=` echo '{"top":{"id":'"${id}"',"free":'"${free}"',"used":'"${used}"',"buffers":'"${buffers}"',"swapused":'"${swapused}"',"res":'"${res}"',"cpu":'"${cpu}"'},"nproc":'"${nprocdata}"',
"hostname":"'${hostnamedata}'","df":['${dfret}'],"sar":['${sarret}'] }' `

echo ${responsejson}


