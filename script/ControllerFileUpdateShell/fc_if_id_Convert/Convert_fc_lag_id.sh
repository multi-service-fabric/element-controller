#!/bin/bash
##
##  Convet_fc_lag_id.sh : The script for converting lag_if_id 
##
## Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
##

## The current directoty is moved.
cd `dirname "$0"`
echo $(pwd)
 
## The file for setting the environmental information is read.
source ./db_env

## The file path for updating the DB schema in each resource
DB_SCHEMA_UPDATE_SCRIPT="./fc_lag_if_id_update.sql"

CNVERT_DATA="'$(pwd)/data.csv'"

# The SQL statement is input in the DB for updating the file.
psql -f ${DB_SCHEMA_UPDATE_SCRIPT} -U ${SBY_USER} -p 5432 -d ${RESTORE_DB_NAME} -h ${SBY_SERVER} -v DATAPATH=$CNVERT_DATA
psql -f ./node_id_convert.sql -U ${SBY_USER} -p 5432 -d ${RESTORE_DB_NAME} -h ${SBY_SERVER}

exit 0

