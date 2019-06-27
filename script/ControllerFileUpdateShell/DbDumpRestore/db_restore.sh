#!/bin/bash
##
## DB restore script
##
## Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
##
Prompt="\[#$%>\]"
source ./db_env

PSQL="psql -h "
PSQL_USER="-U "
AT="@"
ARRW=" < "
SEP="/"
BK=".bk"
SPACE=" "
# The sby login parameters
SbyRemoteHost=$SBY_USER$AT$SBY_SERVER
SbyPw=$SBY_PASS
#The restore command
COMMAND=$PSQL${SBY_SERVER}$SPACE$RESTORE_DB_NAME$SPACE$PSQL_USER$SBY_USER$SPACE$ARRW$BACKUP_PATH$SEP$DB_NAME$BK

function db_dump(){
RemoteHost=$1
echo $RemoteHost
PW=$2
echo $PW

logFile=$(cut -d'@' -f 2 <<<${RemoteHost})
ssh-keygen -R ${logFile} > /dev/null

expect -c "
set timeout 5
spawn ssh -tt -o StrictHostKeyChecking=no ${RemoteHost}
expect {
    \"(yes/no)?\" {
    log_user 0
    send \"yes\n\"
    }
    \"password:\" {
        log_user 0
        send \"${PW}\n\"
    }
    timeout { exit 2 }
}

expect {
      \"denied\" {
       exit 1 
       }
    }
expect {
         -glob \"${Prompt}\" {
         log_user 0
            send \"$COMMAND\n\"
            expect eof 
        }
     }
   

expect {
        -regexp \"\n.*\r\" {
            log_user 0
            sleep 2
            send \"exit\n\"
            exit 0
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

RESULT=`db_dump $SbyRemoteHost $SbyPw`
ret=$?
if [ $ret -eq 1 ];then
 echo "SSH connection error"
 exit 1
fi

if [ $ret -eq 2 ];then
 echo "TimeOut Error"
 exit 2
fi
exit 0
