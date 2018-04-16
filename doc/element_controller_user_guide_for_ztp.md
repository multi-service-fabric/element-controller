# Element Controller User Guide for ZTP

**Version 1.0**
**March 28, 2018**
**Copyright(c) 2018 Nippon Telegraph and Telephone Corporation**

## 1.  Introduction

This document describes how to operate ZTP in device installation or
enhancement, which is conducted with Element Controller (EC).

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
launched in accordance with the Installation Manual.)

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

The dhcpd.conf template files are different each time depending on the
used device.

Locate the dhcpd.conf template file corresponding to the type of device
to be enhanced into the file path configured at the time of registering
the device information. Please refer to the attached document for an
actual example of the template file. The following table illustrates the
list of attached document.

  |\[dhcp.conf Template Files\] List of Attached Documents  ||
  |---------------------------------------------------------|-------------------------------
  |File Name                                                | Overview
  |dhcpd.conf.qfx5100                                       | Juniper QFX5100 Template File
  |dhcpd.conf.qfx5200                                       | Juniper QFX5200 Template File
  |dhcpd.conf.ncs5001                                       | Cisco NCS5001 Template File
  |dhcpd.conf.ncs5011                                       | Cisco NCS5011 Template File
  |dhcpd.conf.ncs5501                                       | Cisco NCS5501 Template File

4.2. Initial Configuration Template
--------------------------------

### 4.2.1. List of reserved words in file

The following table illustrates the list of reserved words to be converted
in the initial configuration template.

  | No.|reserved words                       |converted value        |
  |----|-------------------------------------|-----------------------|
  | 1  |\$\$DEVICEMANAGEMENTADDRESS\$\$      |Device Management Address
  | 2  |\$\$NTPSERVER\$\$                    |NTP Server Address
  | 3  |\$\$SUBNETMASK\$\$                   |Device Subnet Mask
  | 4  |\$\$DEVICEMANAGEMENT_CIDRADDRESS\$\$ |Maximum Number of Prefixes of the Device
  | 5  |\$\$ECMANAGEMENTADDRESS\$\$          |Management IF of EC <br> (element_controller_configuration_specifications.md: **REST Waiting Interface Address**)
  | 6  |\$\$COMMUNITYMEMBERS\$\$             |BGP Community Value
  | 7  |\$\$BELONGINGSIDEMEMBERS\$\$         |BGP Community Value both priority and not priority

### 4.2.2. Locate the Initial Configuration

The initial configuration templates are different each time depending on the
usage and type of the used device.

Locate the initial configuration template files corresponding to the type and usage of
device to be enhanced into the file path configured at the time of
registering the device information. Please refer to the attached document for an
actual example of the template file. The following table illustrates the
list of attached document and each used reserved words.

  |\[Initial Configuration Template File\] List of Attached Documents   ||||
  |----|-----------------------------------------------|-------------------------------------------------------|-----------------------------|
  | No.|File Name                                      |Overview                                               |Number of used reserved word |
  | 1  |qfx5100\_L2Leaf\_1\_0\_ztp\_init.conf.template |Juniper QFX5100 Configuration Template File for L2Leaf |1,2,4,5,6,7
  | 2  |qfx5100\_L3Leaf\_1\_0\_ztp\_init.conf.template |Juniper QFX5100 Configuration Template File for L3Leaf |1,2,4,5,6,7
  | 3  |qfx5100\_Spine\_1\_0\_ztp\_init.conf.template  |Juniper QFX5100 Configuration Template File for Spine  |1,2,4,5
  | 4  |qfx5200\_L3Leaf\_0\_8\_ztp\_init.conf.template |Juniper QFX5200 Configuration Template File for L3Leaf |1,2,4,5,6,7
  | 5  |qfx5200\_Spine\_0\_8\_ztp\_init.conf.template  |Juniper QFX5200 Configuration Template File for Spine  |1,2,4,5
  | 6  |ncs5001\_L3Leaf\_0.8\_ztp\_init.sh.template    |Cisco NCS5001 Configuration Template File for L3Leaf   |1,2,3,5,6,7
  | 7  |ncs5011\_Spine\_0.8\_ztp\_init.sh.template     |Cisco NCS5011 Configuration Template File for Spine    |1,2,3,5
  | 8  |ncs5501\_L3Leaf\_0.8\_ztp\_init.sh.template    |Cisco NCS5011 Configuration Template File for L3Leaf   |1,2,3,5,6,7

### 4.2.3. Example of Sections in the File To Be Modified

The following table illustrates the example of the correction part of the file
that needs modification in the initial configuration template.

| \[Initial Configuration Template\] (For Juniper, Spine)     ||File Number of target |
|---|-------------------------------------------------------------------------|-----------------|
| 1 |policy-options {<br>    prefix-list EL_Prefix {<br>        **\[First Opposite L2Leaf Management Address\]/\[Prefix\],**<br>        **\[Second Opposite L2Leaf Management Address\]/\[Prefix\]**<br>    }<br> (\*)Make the list of the number of opposite L2Leafs. | 3,5 |


4.3. Locate the OS Image
-------------------

This operation is only required if the type of enhanced device is
QFX5200.

Locate the OS image in the file path specified in the dhcpd.conf
template. Please refer to the following figure.

| \[dhcpd.conf Template\]                                               |
|-----------------------------------------------------------------------|
| \--<br> \#\#\#\#\#\#\#\#\#\#<br> \# host<br> \#\#\#\#\#\#\#\#\#\#     |
| \#\# QFX5200 \#\#<br> host QFX5200-1 {                                                      |
|   hardware ethernet \$\$MACADDRESS\$\$;<br>   fixed-address \$\$MANAGEMENTADDRESS\$\$;<br>   next-server \$\$TFTPHOSTNAME\$\$;<br>   option tftp-server-name \"\$\$TFTPHOSTNAME\$\$\";<br>                     |
| option QFX.alt-image-file-name<br> **\"/junos-conf/jinstall-qfx-5e-flex-15.1X53-D30.5-domestic-signed.tgz\"**; |
| option QFX.transfer-mode \"http\";<br> option QFX.config-file-name \"\$\$INITIALCONFIG\$\$\";<br> }                                                                     |
| dhcpd.conf.qfx5200 (END)<br> \--                                                                   |

(\*) **The file path (relative path) of the OS image.**

> Since the QFX5200's acquisition process is httpd, the absolute path
> here should be:
>
> /var/www/html/junos-conf/jinstall-qfx-5e-flex-15.1X53-D30.5-domestic-signed.tgz
