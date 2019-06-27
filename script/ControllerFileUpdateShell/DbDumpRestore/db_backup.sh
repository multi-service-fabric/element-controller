#!/bin/bash
##
## DB backup script
##
## Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
##

source ./db_env

Prompt="\[#$%>\]"

PGDUMP="/usr/bin/pg_dump "
AT="@"
ARRW=" > "
SEP="/"
BK=".bk"
SPACE=" "
# The command is generated.
COMMAND=$PGDUMP$DB_NAME$SPACE$ARRW$BACKUP_PATH$SEP$DB_NAME$BK
SbyRemoteHost=$SBY_USER$AT$SBY_SERVER
ActPw=$ACT_PASS
SbyPw=$SBY_PASS

function db_dump(){
RemoteHost=$1
echo $RemoteHost
PW=$2
echo $PW

logFile=$(cut -d'@' -f 2 <<<${RemoteHost})
ssh-keygen -R ${logFile} > /dev/null

expect -c "
exp_internal 1
set timeout 60
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
    \"denied\" {
       exit 1 
    }
    default { exit 2 }
}

expect {
         \"${Prompt}\" {
         log_user 0
            send \"$COMMAND\n\"
        }
     }
   

expect {
        \"${Prompt}\" {
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
