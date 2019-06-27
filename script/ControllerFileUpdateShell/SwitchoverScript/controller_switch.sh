#!/bin/bash
##
##  controller_switch.sh
##
##  The script for switching the controller
##
## Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
##

# The name of the resource group 
GRP_NAME=$1
# The name of the monitored resource 
PRM_NAME=$2
# The timeout value for the confirmation that the switching process has been finished
TIMEOUT=100


# The ACT node is exracted.
self=`crm_node -n`
# The SBY node is exracted.
other=`crm_node -l | grep member | grep -v ${self} | cut -f 2 -d " "`

# The resource is moved to the SBY node(In addition the SBY node is switched to the ACT node) 
`crm resource move ${GRP_NAME} ${other} force`

# The completion of the swiching process is confirmed
for i in {1..10} ; do
  check=`crm_mon -fA -1 | grep ${PRM_NAME} | awk '{print substr($0, index($0, "Started"))}' | cut -f 2 -d " "`
  echo ${check}
  if [ $? = 0 -a -n "${check}" -a "${other}" = "${check}" ] ; then
    break;
  else
    sleep $((${TIMEOUT}/10))
  fi
done

# The moved resource  is unlocked. 
`crm resource unmove ${GRP_NAME}`

exit 0
