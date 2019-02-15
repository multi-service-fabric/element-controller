# Element Controller User Guide for ZTP

**Version 1.0**
**December 7, 2018**
**Copyright(c) 2018 Nippon Telegraph and Telephone Corporation**

## 1.  Introduction

This document describes how to operate ZTP in device installation or
addition, which is conducted with Element Controller (EC).

ZTP (Zero Touch Provisioning) is a general term for the functions which
will be triggered by resetting or powering-on of a device to
automatically perform the collection/set-up of the configuration or OS
installation of the device itself.

In this document, it is preconditioned that EC had been already
installed in accordance with the instructions described in `element_controller_installation_manual`
(hereafter referred to as `Installation Manual`).

Besides, the command operations should be performed as the root user
unless otherwise specified.

1.1. Expressional Conventions
------------------------

The legends of command input are described below.

\# COMMAND parameter \<parameter\> \[parameter\] Enter

\# ：the prompt sign

COMMAND ：a command name

parameter ：a regular parameter

\<parameter\> ：a parameter to be replaced (Change the content as
required.)

\[parameter\] ：a parameter that can be omitted

Enter ：pressing the Enter key

## 2. ZTP Operational Flow


The Figure 2-1 below illustrates the ZTP operational flow at the EC
server.

![Figure 2-1 ZTP Operational Flow](img/Fig2_1.png "Fig2-1")

The detail of each operation follows from the next section.

## 3. Confirmation of Process Startup


Sequences and file acquisition processes are different each time
depending on the used device, and tftpd and httpd are used.
(It is presumed that tftpd and httpd have been already installed and
launched in accordance with the Installation Manual.)<br>
In addition, it is not required to check if both of tftpd and httpd are activated; however,
it is only required to confirm activation of the process which is applicable for the type of device to perform ZTP.

3.1. tftpd
-----

### 3.1.1. Confirmation of tftpd Running

Confirm if tftpd is funning by use of the following command.

It should be noted that tftpd is started via xinetd.

\# systemctl status xinetd.serviceEnter

In case the output result shows \"active (running)", the process is
running.

| \[Output Result Process IS Running\]     |
|------------------------------------------|
| \--<br> Active: active (running) <br> \--                                      |

| \[Output Result Process is NOT Running\]<br> |
|------------------------------------------|
| \--<br> Active: inactive (dead)<br> \--<br>                                      |

In case the process is not running, launch it with the following command. Execute the command and confirm that no error message is to be shown.

\# systemctl start xinetd.serviceEnter

### 3.1.2. Confirmation of Port Release for tftpd

Confirm with the following command that the port \#69 of UDP is released.

\# ss -lnu | grep :69Enter

Showing result reveals that the port is released.

In case the result is not shown, confirm the configuration of xinetd.

|  \[Output Result\]|
|------------------------|
|  UNCONN 0 0 \*:69 \*:\*|

3.2. httpd
-----

### 3.2.1. Confirmation of httpd Running

Confirm if httpd is funning by use of the following command.

\# systemctl status httpd.serviceEnter

In case the output result shows \"active (running)", the process is
running.

| \[Output Result Process IS Running\]     |
|------------------------------------------|
| \--<br> Active: active (running) <br> \--                                      |

| \[Output Result Process is NOT Running\]<br> |
|------------------------------------------|
| \--<br> Active: inactive (dead)<br> \--<br>                                      |

In case the process is not running, launch it with the following command. Confirm that no error message is to be shown.

\# systemctl start httpd.serviceEnter

### 3.2.2. Confirmation of Port Release for httpd

Confirm with the following command that the port \#80 of TCP is
released.

\# ss -lnt | grep :80Enter

Showing result reveals that the port is released.

In case the result is not shown, confirm the configuration of httpd.

|  \[Output Result\] |
|--------------------------|
|  LISTEN 0 128 \*:80 \*:\*|

## 4. Locate the Configuration File


4.1. Locate the dhcpd.conf Template
------------------------------
### 4.1.1. List of reserved words in file

The following table illustrates the list of reserved words to be converted in the dhcpd. conf template file.
(The reserved words are variables to replace different parameters depending on the environment. The MSF controller automatically replaces these words. Due to this, the user does not need to replace them.)

  | No.|reserved words                       |converted value
  |----|-------------------------------------|-----------------------|
  | 1  |\$\$HOSTNAME\$\$                     |Device Host Name|
  | 2  |\$\$MACADDRESS\$\$                   |Device MAC Address|
  | 3  |\$\$TFTPHOSTNAME\$\$                 |EC Management IF|
  | 4  |\$\$NTPSERVER\$\$                    |NTP Server Address|
  | 5  |\$\$INITIALCONFIG\$\$                |Device Initial Injection Configuration File Path|
  | 6  |\$\$MANAGEMENTADDRESS\$\$            |Device Management IF|
  | 7  |\$\$LOGSERVERADDRESS\$\$             |EC Management IF|
  | 8  |\$\$MANAGEMENTSUBNETMASK\$\$         |Subnet Mask of the network where the Device Management IF belongs to|
  | 9  |\$\$MANAGEMENTNETWORKADDRESS\$\$     |Address of the network where the Device Management IF belongs to|
  | 10 |\$\$MANAGEMENTRANGESTART\$\$         |The starting value of the address range which is available for the network where the Device Management IF belongs to|
  | 11 |\$\$MANAGEMENTRANGEEND\$\$           |The ending value of the address range which is available for the network where the Device Management IF belongs to|

### 4.1.2. Locate the dhcpd.conf Template

The dhcpd.conf template files are different each time depending on the used device.<br>
Locate the dhcpd.conf template file corresponding to the type of device to be added into the file path configured at the time of registering the device information to MSF controller\*\.
Please refer to the attached document for an actual example of the template file.
The following table illustrates the list of attached document.<br>
*) Please make sure to be consistent with the parameters shown in the manual below.<br>
Manual: https://github.com/multi-service-fabric/fabric-controller/blob/master/API/controller_api.xlsx <br>
Parameter: 010101 sheet [dhcp_template]


  |\[dhcp.conf Template Files\] List of Attached Documents  ||
  |---------------------------------------------------------|-------------------------------|
  |File Name                                                | Overview|
  |dhcpd.conf.qfx5100                                       | Juniper QFX5100 Template File|
  |dhcpd.conf.qfx5110                                       | Juniper QFX5110 Template File|
  |dhcpd.conf.qfx5200                                       | Juniper QFX5200 Template File|
  |dhcpd.conf.ncs5001                                       | Cisco NCS5001 Template File|
  |dhcpd.conf.ncs5011                                       | Cisco NCS5011 Template File|
  |dhcpd.conf.ncs5501                                       | Cisco NCS5501 Template File|
  |dhcpd.conf.cumulus                                       | CUMULUS Template File|


4.2. Initial Configuration Template
--------------------------------

### 4.2.1. List of reserved words in file

The following table illustrates the list of reserved words to be converted in the initial configuration template file.
(The reserved words are variables to replace different parameters depending on the environment. The MSF controller automatically replaces these words. Due to this, the user does not need to replace them.)

  | No.|reserved words                       |converted value
  |----|-------------------------------------|-----------------------|
  | 1  |\$\$DEVICEMANAGEMENTADDRESS\$\$      |Device Management Address|
  | 2  |\$\$NTPSERVER\$\$                    |NTP Server Address|
  | 3  |\$\$SUBNETMASK\$\$                   |Device Subnet Mask|
  | 4  |\$\$DEVICEMANAGEMENT_CIDRADDRESS\$\$ |Maximum Number of Prefixes of the Device|
  | 5  |\$\$ECMANAGEMENTADDRESS\$\$          |Management IF of EC <br>(element_controller_configuration_specifications.md: **REST Waiting Interface Address**)|
  | 6  |\$\$COMMUNITYMEMBERS\$\$             |BGP Community Value|
  | 7  |\$\$BELONGINGSIDEMEMBERS\$\$         |BGP Community Value both priority and not priority|

### 4.2.2. Locate the Initial Configuration

The initial configuration templates are different each time depending on the usage and type of the used device.<br>
Locate the initial configuration template files corresponding to the type and usage of device to be added into the file path configured at the time of registering the device information to MSF controller\*.<br>
Please refer to the attached document for an actual example of the template file. The following table illustrates the list of attached document and each used reserved words.<br>
In addition, device of some models requires a specific directory structure (hereinafter, referred to as ‘ZTP directory’) including files such as multiple initial configuration templates, etc.
In the applicable models, ZTP directory is required to be located in such a way that the initial configuration templates (bold letters shown in the table below) in the ZTP directory will be located directly under path configured at the time of registering the model.<br>
Please specify the directory to the applicable parameter\* when registering the device information to MSF controller.
Taking No.10 as an example, please link each file location using a separated value “:”, and then specify as follows:

・・・【Any path】・・・/cumulus_L2Leaf_ztp /cumulus/ztp_sh_template:・・・【Any
path】・・・/cumulus_L2Leaf_ztp/cumulus/EL3/config/config_template」）

In addition, you need to put the ZTP directory as shown in the table below.

*) Please make sure to be consistent with the parameters shown in the manual below.<br>
Manual: https://github.com/multi-service-fabric/fabric-controller/blob/master/API/controller_api.xlsx <br>
Parameter: 010101 sheet [config_template]

![Figure 4-2-2 configuration templates](img/Fig_4_2_2.png "Fig4-2-2")

### 4.2.3. Example of Sections in the File To Be Modified

The following table illustrates the example of the correction part of the file
that needs modification in the initial configuration template.

|   |\[Initial Configuration Template\] (For Juniper, Spine)     |File Number of target |
|---|-------------------------------------------------------------------------|-----------------|
| 1 |policy-options {<br>    prefix-list EL_Prefix {<br>        **\[First Opposite L2Leaf Management Address\]/\[Prefix\],**<br>        **\[Second Opposite L2Leaf Management Address\]/\[Prefix\]**<br>    }<br> (\*)Make the list of the number of opposite L2Leafs. | 3,6 |


4.3. Locate the OS Image
-------------------

This operation is only required if the type of adding device is
QFX5200 or Cumulus.

Locate the OS image in the file path specified in the dhcpd.conf
template. Please refer to the following figure.

| \[QFX5200 dhcpd.conf Template\]                                       |
|-----------------------------------------------------------------------|
| \--<br> \#\#\#\#\#\#\#\#\#\#<br> \# host<br> \#\#\#\#\#\#\#\#\#\#     |
| \#\# QFX5200 \#\#<br> host QFX5200-1 {|
|   hardware ethernet \$\$MACADDRESS\$\$;<br>   fixed-address \$\$MANAGEMENTADDRESS\$\$;<br>   next-server \$\$TFTPHOSTNAME\$\$;<br>  option tftp-server-name \"\$\$TFTPHOSTNAME\$\$\";<br>                     |
| option QFX.alt-image-file-name<br> **\"/junos-conf/jinstall-qfx-5e-flex-15.1X53-D30.5-domestic-signed.tgz\"**; |
| option QFX.transfer-mode \"http\";<br> option QFX.config-file-name \"\$\$INITIALCONFIG\$\$\";<br> }                                                                     |
| dhcpd.conf.qfx5200 (END)<br> \--                                                                   |

(\*) **The file path (relative path) of the OS image.**

> Since the QFX5200's acquisition process is httpd, the absolute path
> here should be:
>
> /var/www/html/junos-conf/jinstall-qfx-5e-flex-15.1X53-D30.5-domestic-signed.tgz

| \[Cumulus dhcpd.conf Template\]|
|-----------------------------------------------------------------------|
| option cumulus-provision-url code 239 = text; <br> subnet $$MANAGEMENTNETWORKADDRESS$$ netmask $$MANAGEMENTSUBNETMASK$$ {<br> &nbsp;&nbsp;&nbsp;&nbsp;default-lease-time 600; <br> &nbsp;&nbsp;&nbsp;&nbsp;deny unknown-clients; <br> &nbsp;&nbsp;&nbsp;&nbsp;max-lease-time 7200; <br> &nbsp;&nbsp;&nbsp;&nbsp;range $$MANAGEMENTRANGESTART$$ $$MANAGEMENTRANGEEND$$; <br> &nbsp;&nbsp;&nbsp;&nbsp;option default-url = "tftp://**\$$TFTPHOSTNAME$$/cumulus/OS/cumulus-linux-3.6.0-bcm-amd64.bin\"**; <br> &nbsp;&nbsp;&nbsp;&nbsp;option cumulus-provision-url "tftp://$$TFTPHOSTNAME$$/cumulus/ztp.sh"; <br> &nbsp;&nbsp;&nbsp;&nbsp;host $$HOSTNAME$$ { <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;hardware ethernet $$MACADDRESS$$; <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;fixed-address $$MANAGEMENTADDRESS$$; <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;option host-name "$$HOSTNAME$$"; <br> &nbsp;&nbsp;&nbsp;&nbsp;} <br> }

(\*) **The file path (relative path) of the OS image.**

> Since the Cumulus's acquisition process is tftpd, the absolute path here should be:
> /var/lib/tftpd/cumulus/OS/cumulus-linux-3.6.0-bcm-amd64.bin

## 5. Operation of Device

Method for starting-up of ZTP is different depending upon the vendor of device. Operation method is described below.
Once this procedure has been done, all configurations, etc, will be deleted automatically.
Therefore, please make sure to create a backup of them in advance, and save them in another server.

---
### 5.1. Operational Procedures for JUNIPER’s Device

If remote access via ssh is possible, use the following command to activte ZTP.

\# request system zeroize Enter

Enter Yes, when asked a Yes/No question.<br>
Confirm if ssh disconnects naturally.

When remote access is unavaiable, peform the above procedure from a console.

### 5.2. Operational Procedures for CISCO’s Device

Before a reset, it is required to complete the following procedure by performing remote accss via ssh.

\# ztp clean Enter<br>
\# configure Enter

Enter the following command, when it changes to the Configuration Edit Mode.

\# commit replace Enter

Enter Yes, when asked a Yes/No question.<br>
Ensure that no response will be returned from the command prompt.

Power cable needs to be detached/inserted (a power switch is not provided) because CISCO’s device cannot be reset remotely.

### 5.3. Operational Procedures for Cumulus

Initialize the device by using the following command.

\# sudo onie-select -k Enter

Enter Yes, when asked a Yes/No question.<br>
After initialization has been finished, restart the device by using the following command.

\# sudo shutdown -r now Enter
