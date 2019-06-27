#!/bin/bash
##
## resource_lock_release.sh 
## The schell scrpipt for unlocking the resource by the crm command 
##
## Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
##
TARGET=$1
echo -n `crm resource unmove $TARGET`
