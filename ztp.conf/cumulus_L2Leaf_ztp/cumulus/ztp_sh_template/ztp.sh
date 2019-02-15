#!/bin/bash

function error() {
  echo -e "\e[0;33mERROR: The Zero Touch Provisioning script failed while running the command $BASH_COMMAND at line $BASH_LINENO.\e[0m" >&2
  exit 1
}

DOWNLOAD_DIR='/home/cumulus'
Server_DIR='tftp://$$ECMANAGEMENTADDRESS$$/cumulus/EL3'
export DOWNLOAD_DIR='/home/cumulus'
export Server_DIR='tftp://$$ECMANAGEMENTADDRESS$$/cumulus/EL3'


# Log all output from this script
exec >> /var/log/autoprovision 2>&1
date "+%FT%T ztp starting script $0"

trap error ERR

# Install a License from usb and restart switchd
curl ${Server_DIR}/licenses/license.txt -o ${DOWNLOAD_DIR}/license.txt
/usr/cumulus/bin/cl-license -i ${DOWNLOAD_DIR}/license.txt && sudo systemctl restart switchd.service

# Update NCLU
curl ${Server_DIR}/nclu_1.0-cl3u21_all.deb -o ${DOWNLOAD_DIR}/nclu_1.0-cl3u21_all.deb
sudo dpkg -i ${DOWNLOAD_DIR}/nclu_1.0-cl3u21_all.deb

sleep 60

net del all
net commit


# Load switchd config from tftp server
curl ${Server_DIR}/snmp/snmpd.conf -o ${DOWNLOAD_DIR}/snmpd.conf
cp -f ${DOWNLOAD_DIR}/snmpd.conf /etc/snmp/snmpd.conf

# Load system configuration file from tftp server
curl ${Server_DIR}/config/config.txt -o ${DOWNLOAD_DIR}/config.txt
source ${DOWNLOAD_DIR}/config.txt

# Load snmpd config from tftp server
curl ${Server_DIR}/switchd/switchd.conf -o ${DOWNLOAD_DIR}/switchd.conf
cp -f ${DOWNLOAD_DIR}/switchd.conf /etc/cumulus/switchd.conf

# Load traffic.conf from tftp server
curl ${Server_DIR}/traffic/traffic.conf -o ${DOWNLOAD_DIR}/traffic.conf
cp -f ${DOWNLOAD_DIR}/traffic.conf /etc/cumulus/datapath/traffic.conf

# Load interface config from tftp server
#curl ${Server_DIR}/interfaces/interfaces.txt -o ${DOWNLOAD_DIR}/interfaces.txt
#cp -f ${DOWNLOAD_DIR}/interfaces.txt /etc/network/interfaces

# Load port config from tftp server
#   (if breakout cables are used for certain interfaces)
#curl ${Server_DIR}/ports/ports.conf -o ${DOWNLOAD_DIR}/ports.conf
#cp -f ${DOWNLOAD_DIR}/ports.conf /etc/cumulus/ports.conf

# Reload interfaces to apply loaded config
ifreload -a

# Output state of interfaces
#net show interface

# Load SSH config from tftp server
curl ${Server_DIR}/ssh/sshd.conf -o ${DOWNLOAD_DIR}/sshd.conf
cp -f ${DOWNLOAD_DIR}/sshd.conf /etc/ssh/sshd_config

# Reload sshd to apply loaded config
sudo systemctl restart ssh.service

# Reload switchd to apply loaded config
sudo systemctl restart switchd.service

printf "finished switchd.service restart\n"

# Remove download files
rm -f ${DOWNLOAD_DIR}/*.txt
rm -f ${DOWNLOAD_DIR}/*.conf
rm -f ${DOWNLOAD_DIR}/nclu_1.0-cl3u21_all.deb


#Stop service
sudo systemctl stop dhcpd
sudo systemctl stop ptp4l.service
sudo systemctl stop phc2sys.service

sudo systemctl disable dhcpd
sudo systemctl disable ptp4l.service
sudo systemctl disable phc2sys.service

#Reload service
sudo systemctl restart rsyslog




date
printf "clear chach\n"
net abort
printf "finish chach clear\n"


date
printf "ztp.sh finish\n"
printf "///////////////////////////////////////////////////////////////////////\n"


# CUMULUS-AUTOPROVISIONING
exit 0

