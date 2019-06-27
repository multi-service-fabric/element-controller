#!/bin/bash
##
##  db_schema_update.sh  
##
## The shell script for updating the DB schema
##
## Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
##

## The current direcoty is moved.
cd `dirname "$0"`

## The file for setting the environmental infomation is read.
source ./db_env

## The directory of the DB data
DB_DATA="/usr/local/pgsql/9.3/data"
## The Locked file
DB_LOCK="/var/lib/pgsql/tmp/PGSQL.lock"

PROMPT="\[#$%>\]"

## The DB is defined as the Master DB
## The first argument : User Name for the DB server as the Master
## The second argument: IP address for the DB seerver
## The third argument : Login Password for the DB server
## The fourth argument: Host name of the MASTER server
function toMaster() {
expect -c "
exp_internal 1
set timeout 15
spawn ssh ${1}@${2}
    expect {
        \"password:\" {
            send \"${3}\n\"
        }
        \"denied\" {
           exit 1 
        }
        default { exit 2 }
    }
    expect {
        \"${PROMPT}\" {
            send \"crm node standby ${4}\n\"
        }
    }
    sleep 5
    expect {
        \"${PROMPT}\" {
            send \"crm node online ${4}\n\"
        }
    }
    expect {
        \"${PROMPT}\" {
            send \"exit\n\"
        }
    }
   "
}


## The DB is joined with the replication as the Slave server
## The first argument : User Name for the Slave DB server
## The second argument: IP address for the Slave DB server
## The third argument : Login Password for the Slave DB server
function joinSlave() {
expect -c "
exp_internal 1
set timeout 15
spawn ssh ${1}@${2}
    expect {
        \"password:\" {
            send \"${3}\n\"
        }
        \"denied\" {
           exit 1 
        }
        default { exit 2 }
    }
    expect {
        \"${PROMPT}\" {
             send \"pg_basebackup -h ${REP_MASTER_IP} -U postgres -D ${DB_DATA} --xlog --progress\n\"
        }
    }
    
    expect {
        \"${PROMPT}\" {
            send \"chown -R postgres:postgres ${DB_DATA}\n\"
        }
    }
    
    expect {
        \"${PROMPT}\" {
            send \"rm -rf ${DB_LOCK}\n\"
        }
    }
    
    expect {
        \"${PROMPT}\" {
            send \"crm resource cleanup pgsql\n\"
        }
    }
    expect {
        \"${PROMPT}\" {
            send \"exit\n\"
        }
    }
   "
}

## The DB schema is generated.
## The first argument : User Name for the DB server
## The second argument: IP address for the  DB server
function createDbSchema() {
    createdb -U ${1} -h ${2} -p 5432 ${RESTORE_DB_NAME}
}


## MAIN
# The DB is  backed up.
sh ./db_backup.sh

# If the DB is redundant(in the DB replication)
if [ "${REP_STATE}" -eq 1 ]; then
    echo "DB is replicated."

    toMaster ${SBY_USER} ${SBY_SERVER} ${SBY_PASS} ${ACT_SERVER_NAME}

    sleep 1m

    joinSlave ${ACT_USER} ${ACT_SERVER} ${ACT_PASS}
else
    echo "DB is not replicated."
fi

# The DB schema for updating is generted.
createDbSchema ${SBY_USER} ${SBY_SERVER}

# The DB is restored.
sh ./db_restore.sh

# The DB schema is updated.
sh ${DB_SCHEMA_UPDATE_SCRIPT}

exit 0

