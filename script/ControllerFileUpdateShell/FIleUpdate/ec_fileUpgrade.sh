#!/bin/bash
##
## Controller file update script
##
## Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
##


# The directory path for preparing the new binary file
WORK_MAIN=$1
# The path for EC installing EC
EC_INSTALL_PATH=$2

# Individual parameters
URL_TAG="connection.url"
USERNAME_TAG="hibernate.connection.username"
PASSWORD_TAG="hibernate.connection.password"

OLD_DBCONF="${EC_INSTALL_PATH}/conf/hibernate.cfg.xml"
NEW_DBCONF="${WORK_MAIN}/conf/hibernate.cfg.xml"

# The Current time is acquired.
DATETIME=`date +%Y%m%d%H%M%S`

## The current directory is moved.
cd `dirname "$0"`

## The file path for setting the environmental information
SET_OLD_VALUE=./file_oldparam_env

## The value is acquired from the old configuration file and the binary file is updated.
function takeOverConfFile(){
  echo "start takeOverConfFile"
  for dir_name in conf bin
  do
      for file in `ls ${EC_INSTALL_PATH}/${dir_name}`
      do
          env_file=`grep ^${file}: ${SET_OLD_VALUE} | cut -d ":" -f 2`
          echo "${file} update start"
          
          if [ -z "${env_file}" ]; then
              echo "next file."
              continue
          fi

          declare -A update_param_list
          if [[ $file == *conf* ]]; then
             change_file=`cat ${EC_INSTALL_PATH}/${dir_name}/${file} | grep -v '^#' | sed '/^$/d'`
          else
             change_file=`cat ${EC_INSTALL_PATH}/${dir_name}/${file} | grep -v '^#' | sed '/^$/d' | grep '=' | sed '/^[^A-Z]/d'`
          fi
          for line in `echo ${change_file}`
          do
              key=`echo ${line} | cut -d "=" -f 1`
              if `echo ${env_file} | grep -q "${key}"` ; then
                  echo "    ${line}    "
                    key=`echo ${line} | cut -d "=" -f 1`
                    update_param_list["${key}"]="${line}"
              fi
          done
          if [[ $file == *conf* ]]; then
             change_file=`cat ${EC_INSTALL_PATH}/${dir_name}/${file} | grep -v '^#' | sed '/^$/d'`
          else
             change_file=`cat ${EC_INSTALL_PATH}/${dir_name}/${file} | grep -v '^#' | sed '/^$/d' | grep '=' | sed '/^[^A-Z]/d'`
          fi
          for line in `echo ${change_file}`
          do
              key=`echo ${line} | cut -d "=" -f 1`
              key_equal="${key}="
              if `echo ${update_param_list[@]} | grep -wq "${key}"` ; then
                  echo -n "${file} Overwrite. "
                  echo ${update_param_list["${key}"]}
                  sed -i -e "s#^${key_equal}.*#${update_param_list["${key}"]}#g" ${WORK_MAIN}/${dir_name}/${file}
              fi
          done
      done
  done
}

## The value is acquired from the old configuration file and the new binary file is updated.
function takeOverDBconf(){
# The IP address of the new file is acquired.
NEW_CONF=`sed -n '/connection.url/p' $NEW_DBCONF`
arr=( `echo $NEW_CONF | tr -s '/' '\n'`)
NEW_IP=`echo ${arr[2]}`
echo new ip $NEW_IP

oldFile=$OLD_DBCONF
while read line
do
  if [[ $line = *$URL_TAG* ]]; then
    DBURL="\        ${line}"
    arr=( `echo $line | tr -s '/' '\n'`)
    OLD_IP=`echo ${arr[2]}`
    echo old ip $OLD_IP
    sed -i "s/$NEW_IP/$OLD_IP/g" $NEW_DBCONF
  fi
  if [[ $line = *$USERNAME_TAG* ]]; then
    DBUSER="\        ${line}"
    sed -i "/${USERNAME_TAG}/c ${DBUSER}" $NEW_DBCONF
  fi
  if [[ $line = *$PASSWORD_TAG* ]]; then
    DBPASS="\        ${line}"
    sed -i "/${PASSWORD_TAG}/c ${DBPASS}" $NEW_DBCONF
    break
  fi
done < $oldFile
}

## MAIN

# The conf is taken over to the new binary file.
takeOverConfFile
takeOverDBconf


# The privilege for the execution is granted to the new schell script.
find $WORK_MAIN -name \*.sh -print | xargs chmod 777

# The privilege for the execution is granted to the RA.
chmod 777 $WORK_MAIN/RA/ec
chmod 777 $WORK_MAIN/RA/snmptrapd

# The old binary file is saved.
mv ${EC_INSTALL_PATH} ${EC_INSTALL_PATH}_${DATETIME}
mv /lib/ocf/resource.d/heartbeat/ec /lib/ocf/resource.d/heartbeat/ec_${DATETIME}
mv /lib/ocf/resource.d/heartbeat/snmptrapd /lib/ocf/resource.d/heartbeat/snmptrapd_${DATETIME}

# The new binary file is deployed..
cp -r ${WORK_MAIN} ${EC_INSTALL_PATH}
cp ${EC_INSTALL_PATH}/RA/ec /lib/ocf/resource.d/heartbeat/
cp ${EC_INSTALL_PATH}/RA/snmptrapd /lib/ocf/resource.d/heartbeat/

exit 0
