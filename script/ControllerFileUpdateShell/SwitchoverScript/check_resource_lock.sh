#!/bin/bash
##
## check_resource_lock.sh  
##
## The schell scrpipt for confirming whether the Pacemaker resource is locked or not by the crm command 
##
## Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
##
TARGET=$1
CHECK_STRING="location cli-prefer-$TARGET"
echo -n `crm configure show | grep "$CHECK_STRING"`
