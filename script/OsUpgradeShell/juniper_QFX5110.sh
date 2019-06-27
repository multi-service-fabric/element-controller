#!/bin/bash
##
## Os upgrade script (juniper QFX5110)
##
## Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
##

# The parameter processing
LOGIN_IP=$1
USER=$2
PASS=$3

# The individual parameters are set.
REQ_PASS="\[Pp\]assword:"
PROMPT="\[#%>\]"

# The error code is defined.
SSH_ERROR=255

# The key is generated.
ssh-keygen -R ${LOGIN_IP} > /dev/null 2>&1

## OS is upgraded.
function execute_os_upgrade(){

# Password
# In the case the password includes the string included in the PROMPT valiable,
# an abnormal behavior may be detected.
NSLAB_PASS='\$1\$zCVxLjUU\$5QWFBU/IIoT7G/M0ooEld.'
SYSTEM_ROOT_PASS='\$1\$osUf/onb\$HbE0.Do5tmDp3ywLVnRdM/'

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
      sleep 1
    }
    default { exit 2 }
  }

  expect {
    \"${PROMPT}\" {
      send \"configure\n\"
      sleep 1
    }
  }

  expect {
    \"${PROMPT}\" {
      send \"load factory-default\n\"
      sleep 1
    }
  }
  expect {
    \"${PROMPT}\" {
      send \"set system services ssh\n\"
      sleep 1
    }
  }
  expect {
    \"${PROMPT}\" {
      send \"set system services netconf ssh\n\"
      sleep 1
    }
  }
  expect {
    \"${PROMPT}\" {
      send \"set system root-authentication encrypted-password $SYSTEM_ROOT_PASS\n\"
      sleep 1
    }
  }
  expect {
    \"${PROMPT}\" {
      send \"set system login user nslab uid 2000\n\"
      sleep 1
    }
  }
  expect {
    \"${PROMPT}\" {
      send \"set system login user nslab class super-user\n\"
      sleep 1
    }
  }
  expect {
    \"${PROMPT}\" {
      send \"set system login user nslab authentication encrypted-password $NSLAB_PASS\n\"
      sleep 1
    }
  }
  expect {
    \"${PROMPT}\" {
      send \"set interfaces vme unit 0 family inet dhcp vendor-id Juniper-qfx5110-48s-4c\n\"
      sleep 1
    }
  }

  expect {
    \"${PROMPT}\" {
      send \"commit\n\"
      sleep 1
    }
  }

  expect {
    \"${PROMPT}\" {
      send \"exit\n\"
      sleep 1
    }
  }
  
  expect {
    \"${PROMPT}\" {
      send \"request system reboot at now\n\"
      sleep 1
    }
  }

  expect {
    \"yes,no\" {
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

