#!/bin/bash
##
## Standby state acquisition script
##
## Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
##

## Input Argument
## Login information
LOGIN_IP=$1
USER=$2
PASS=$3
## Execution script path
ScriptPath=$4

## top Command Execution Yes/No
topExecution=$5
## nproc Command Execution Yes/No
nprocExecution=$6
## df Command Execution Yes/No
dfExecution=$7
## sar Command Execution Yes/No
sarExecution=$8
## hostname Command Execution Yes/No
hosutnameExecution=$9

Prompt="\[#$%>\]"
REQ_PASS="\[Pp\]assword:"

SSH_ERROR=255

logFile="sby_status"
ssh-keygen -R ${LOGIN_IP} > /dev/null 2>&1

function get_status(){
expect -c "
set timeout 20
#exp_internal 1

spawn ssh -tt -o StrictHostKeyChecking=no ${USER}@${LOGIN_IP}
expect {
    \"${REQ_PASS}\" {
      send \"${PASS}\n\"
      exp_continue
    }
    \"denied\" {
      exit 1
    }
    \"${Prompt}\" {
      send \"\n\"
    }
    default { exit 2 }
}

expect {
         -glob \"${Prompt}\" {
         log_file -noappend $logFile
         log_user 1
            send \"bash ${ScriptPath} ${topExecution} ${nprocExecution} ${dfExecution} ${sarExecution} ${hosutnameExecution} 0\n\"
        }
     }
   

expect {
        -regexp \"\n.*\r\" {
            log_user 0
            send \"exit\n\"
        }
    }
"
ret=$?
if [ $ret -eq 1 ];then
 return 1
fi
if [ $ret -eq 2 ];then
 return 2
fi
return 0
}

RESULT=`get_status`
ret=$?
if [ $ret -eq 1 ];then
 echo "SSH connection error"
 exit 255
fi

if [ $ret -eq 2 ];then
 echo "TimeOut Error"
 exit 255
fi

rmCommand="rm -f ${logFile}"
error=`cat ${logFile} | grep ${ScriptPath}:`
if [ ${#error} = 0 ];then
  echo `cat ${logFile} | grep top`
  eval $rmCommand
  exit 0
else
  eval $rmCommand
  exit 1
fi
