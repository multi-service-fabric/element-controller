#!/bin/bash
##
##  MSF2017_MSF2018B.sh
##  The script for the execution of the SQL statement which updates the DB from MSF2017 to MSF2018B
##  when upadating the DB schema

##
## Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
##

## The current directory is moved.
cd `dirname "$0"`

## The file for setting the environmental information is read.
source ./db_env

## The path of the SQL statement file for updating the DB schema in each resource.
DB_SCHEMA_UPDATE_SCRIPT="./MSF2017_to_MSF2018B.sql"

# The SQL statement is sent in the DB for updating the file.
psql -U ${SBY_USER} -h ${SBY_SERVER} -p 5432 ${RESTORE_DB_NAME} < ${DB_SCHEMA_UPDATE_SCRIPT}

exit 0

