#!/bin/bash
##
## Os upgrade script (cisco NCS5011)
##
## Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
##

# The parameter processing
LOGIN_IP=$1
USER=$2
PASS=$3

# The individual parameter are set.
REQ_PASS="\[Pp\]assword:"
PROMPT="\[#$%>\]"

# The error code is defined.
SSH_ERROR=255

# The key is generated.
ssh-keygen -R ${LOGIN_IP} > /dev/null 2>&1

## OS is upgraded.
function execute_os_upgrade(){

  expect -c "
  set timeout 60

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

  expect {
    \"${PROMPT}\" {
      send \"ztp clean\n\"
      exp_continue
    }
    \"y/n\" {
      send \"yes\n\"
    }
  }

  expect {
    \"${PROMPT}\" {
     send \"admin hw-module location all bootmedia network reload\n\"
    }
  }

  expect {
    \"no,yes\" {
      send \"yes\n\"
      sleep 1
    }
  }
  "
  RET=$?
  return ${RET}
}

RESULT=`execute_os_upgrade`
EXEC_RET=$?
logger -p 3 "${RESULT}"

if [ ${EXEC_RET} -eq 1 ];then
  echo "SSH connection error"
  exit ${SSH_ERROR}
fi

if [ ${EXEC_RET} -eq 2 ];then
  echo "TimeOut Error"
  exit ${SSH_ERROR}
fi

exit 0

