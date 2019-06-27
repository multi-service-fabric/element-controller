#!/bin/bash
##
## Ping execute script (cisco)
##
## Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
##

LANG=C

# The parameter processing
LOGIN_IP=$1
PING_INFO_LIST=$2
USER=$3
PASS=$4
RETRY=$5

# The common paramters are set.
COUNT=1
WAIT=2

# The individual parameters are set.
REQ_PASS="\[Pp\]assword:"
PROMPT="\[#$%>\]"
PING_OK="Success rate is 100 percent"

# The error code is defined.
SSH_ERROR=255

# The key is generated.
ssh-keygen -R ${LOGIN_IP} > /dev/null 2>&1

declare -a PING_COMMAND_LIST
PING_NUM=0
PING_WAIT=`expr ${WAIT} + 1`

# The syntax for the ping command is generated.(The strings separated by "T" are generated.)
F_T_LIST=$(echo ${PING_INFO_LIST} | tr '+' ' ');
for F_T in ${F_T_LIST[@]};
do
  FROM=`echo ${F_T} | cut -d_ -f1`
  TO=`echo ${F_T} | cut -d_ -f2`
  PING_COMMAND="ping ${TO} source ${FROM} count ${COUNT} timeout ${WAIT}"
  PING_COMMAND_LIST+=${PING_COMMAND}
  PING_COMMAND_LIST+="+"
  PING_NUM=`expr ${PING_NUM} + 1`
done
PING_COMMAND_LIST=`echo ${PING_COMMAND_LIST[@]} | sed -e 's/+$//g'`

## The ping execution
function execute_ping(){
  expect -c "
  set timeout 20
  #exp_internal 1
  log_user 0
  spawn ssh -tt -o StrictHostKeyChecking=no ${USER}@${LOGIN_IP}
  expect {
    \"${REQ_PASS}\" {
      send \"${PASS}\n\"
      exp_continue
    }
    \"denied\" {
      exit 1
    }
    \"${PROMPT}\" {
      send \"\n\"
    }
    default { exit 2 }
  }  

  set idx 0
  set retry 0
  set ping_info_list [split \"${PING_INFO_LIST}\" \"+\"]
  set ping_command_list [split \"${PING_COMMAND_LIST}\" \"+\"]
  set ret_json \"{\"
  set timeout ${PING_WAIT}
  while {\$idx < ${PING_NUM}} {
    set ping_info [lindex \$ping_info_list \$idx]
    set ping_command [lindex \$ping_command_list \$idx]
    expect {
      \"${PROMPT}\" {
        send \"\$ping_command\n\"
      }
    }
    expect {
      \"${PING_OK}\" {
        append ret_json \"\\\"\$ping_info\\\":\\\"success\\\",\"
        incr idx 1
      }
      default {
        if {\$retry < ${RETRY}} {
          incr retry 1
        } else {
          append ret_json \"\\\"\$ping_info\\\":\\\"failed\\\",\"
          set retry 0
          incr idx 1
        }
      }
    }
  }

  expect {
    \"${PROMPT}\" {
      send \"exit\n\"
    }
  }

  append ret_json \"}\t\"
  puts \$ret_json
  "
  RET=$?
  return ${RET}
}

RESULT=`execute_ping`
EXEC_RET=$?
if [ ${EXEC_RET} -eq 1 ];then
  echo "SSH connection error"
  exit ${SSH_ERROR}
fi

if [ ${EXEC_RET} -eq 2 ];then
  echo "TimeOut Error"
  exit ${SSH_ERROR}
fi

OUT_JSON=`echo "${RESULT}" | sed -e "s/,}/}/g"`
echo ${OUT_JSON}

exit 0


