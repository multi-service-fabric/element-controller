# Element Controller Automate Installation Manual

**Version 1.0**
**December 7, 2018**
**Copyright(c) 2018 Nippon Telegraph and Telephone Corporation**

## 1.  Introduction

1.1. Objective
---------

This document is the installation manual for the EC Main Module included
in the Element Controller (hereafter referred to as \"EC\").
Please read this manual carefully before using the software.

1.2. Scope of Application
--------------------

The scope of this document is for the operation of the components of EC
Main Module of Controller.

The subjects other than that are not covered in this document.

1.3. Expressional Conventions
------------------------

There are certain expressions and text styles conventionally used in
this document. Please make yourself clear about the items below before
going on through the document.

**&lt;Execution Server: XXX&gt;** - bold letters surrounded by angle
brackets

This means the server which commands are executed on.

**\[XX XX\]** - bold letters surrounded by square brackets

This means the command to be entered in Linux.

**X \[Enter\]** - bold letter and \"\[Enter\]\"

> In this case, you need to enter the letters within brackets and press
> the Enter key in the console screen.

1.4. Trademark Notice
----------------

All company names and product names mentioned in this document are
registered trademarks or trademarks of their respective companies.

**LinuxR:**

The registered trademark or the trademark of Linus Torvalds in the U.S.
and other countries

**PostgreSQL@:**

The trademark of PostgreSQL in the U.S. and other countries

1.5. Configuration of the Included Accessories
-----------------------------------------

The table below illustrates the required items to follow the
installation instructions in this document.

For the items described as \"in-advance DL\", you must download and
prepare them prior to implementing the installation in this document
at no internet connection.
Then, place the acquired files under the ec_main folder of the target server (created in Chapter 3)
so that they are the same as the following file structure.

However, downloading java installation package is necessary regardless of network connection.
Then, place the acquired files under the ec_main folder of the target server (created in Chapter 3)
so that they are the same as the following file structure.

Table 1-1 Included
Accessories

| \#      | Folder Structure | | | File    | Description | Remarks |
|---------|---------|---------|---------|---------|---------|---------|
| 1.      | ec_main | \-      | \-      | \-      | \-        | \-        |
| 2.      |         |         |         | jar_create.sh | Compile Script | In-Advance DL from GitHub  |
| 3.      |         | msf     |         | \-      | EC source Files | In-Advance DL from GitHub <br> Omit details of files. <br> Please refer to element_controller_building_guide. |
| 4.      |         | bin     |         | boot.sh | Inter-devices I/F Control Functinal Part <br> Device Start-up Notification Script | In-Advance DL from GitHub  |
| 5.      |         |         |         | ec\_ctl.sh | EC Start-up Script | In-Advance DL from GitHub  |
| 6.      |         |         |         | linkdown.sh | SNMPTrap Functional Part Link-down <br> Notification Script | In-Advance DL from GitHub  |
| 7.      |         |         |         | linkup.sh | SNMPTrap Functional Part Link-up <br> Notification Script | In-Advance DL from GitHub  |
| 8.      |         |         |         | controller\_status.sh | Controler Status Information Acquisition Script | In-Advance DL from GitHub  |
| 9.      |         |         |         | CumulusVlanTraffic.sh | User Traffic Acquisition Script (CLI) | In-Advance DL from GitHub  |
| 10.      |         | lib     |         | NetConf.jar | NetConf Library | In-Advance DL from GitHub  |
| 11.     |         |         |         | antlr-2.7.7.jar | Using Library   | In-Advance DL  |
| 12.     |       |         |         | c3p0-0.9.2.1.jar | Using Library   | In-Advance DL  |
| 13.     |       |         |         | commons-io-2.5.jar | Using Library   | In-Advance DL  |
| 14.     |       |         |         | dom4j-1.6.1.jar | Using Library   | In-Advance DL  |
| 15.     |       |         |         | ganymed-ssh2-build210.jar | Using Library   | In-Advance DL  |
| 16.     |       |         |         | geronimo-jta_1.1_spec-1.1.1.jar | Using Library   | In-Advance DL  |
| 17.     |       |         |         | gson-2.7.jar | Using Library   | In-Advance DL  |
| 18.     |       |         |         | hibernate-c3p0-5.0.10.Final.jar | Using Library   | In-Advance DL  |
| 19.     |       |         |         | hibernate-commons-annotations-5.0.1.Final.jar | Using Library   | In-Advance DL  |
| 20.     |       |         |         | hibernate-core-5.0.10.Final.jar | Using Library   | In-Advance DL  |
| 21.     |       |         |         | hibernate-jpa-2.1-api-1.0.0.Final.jar | Using Library   | In-Advance DL  |
| 22.     |       |         |         | hk2-api-2.5.0-b05.jar | Using Library   | In-Advance DL  |
| 23.     |       |         |         | hk2-locator-2.5.0-b05.jar | Using Library   | In-Advance DL  |
| 24.     |       |         |         | hk2-utils-2.5.0-b05.jar | Using Library   | In-Advance DL  |
| 25.     |       |         |         | javassist-3.18.1-GA.jar | Using Library   | In-Advance DL  |
| 26.     |       |         |         | javax.annotation-api-1.2.jar | Using Library   | In-Advance DL  |
| 27.     |       |         |         | javax.inject-2.5.0-b05.jar | Using Library   | In-Advance DL  |
| 28.     |       |         |         | javax.ws.rs-api-2.0.1.jar | Using Library   | In-Advance DL  |
| 29.     |       |         |         | jboss-logging-3.3.0.Final.jar | Using Library   | In-Advance DL  |
| 30.     |       |         |         | jersey-client.jar | Using Library   | In-Advance DL  |
| 31.     |       |         |         | jersey-common.jar | Using Library   | In-Advance DL  |
| 32.     |       |         |         | jersey-container-servlet-core.jar | Using Library   | In-Advance DL  |
| 33.     |       |         |         | jersey-guava-2.23.2.jar | Using Library   | In-Advance DL  |
| 34.     |       |         |         | jersey-server.jar | Using Library   | In-Advance DL  |
| 35.     |       |         |         | jetty-http-9.3.11.v20160721.jar | Using Library   | In-Advance DL  |
| 36.     |       |         |         | jetty-io-9.3.11.v20160721.jar | Using Library   | In-Advance DL  |
| 37.     |       |         |         | jetty-security-9.3.11.v20160721.jar | Using Library   | In-Advance DL  |
| 38.     |       |         |         | jetty-server-9.3.11.v20160721.jar | Using Library   | In-Advance DL  |
| 39.     |       |         |         | jetty-servlet-9.3.11.v20160721.jar | Using Library   | In-Advance DL  |
| 40.     |       |         |         | jetty-util-9.3.11.v20160721.jar | Using Library   | In-Advance DL  |
| 41.     |       |         |         | jsch-0.1.53.jar | Using Library   | In-Advance DL  |
| 42.     |       |         |         | log4j-api-2.6.2.jar | Using Library   | In-Advance DL  |
| 43.     |       |         |         | log4j-core-2.6.2.jar | Using Library   | In-Advance DL  |
| 44.     |       |         |         | log4j-slf4j-impl-2.6.2.jar | Using Library   | In-Advance DL  |
| 45.     |       |         |         | mchange-commons-java-0.2.3.4.jar | Using Library   | In-Advance DL  |
| 46.     |       |         |         | org.eclipse.persistence.core.jar | Using Library   | In-Advance DL  |
| 47.     |       |         |         | postgresql-9.4.1209.jre7.jar | Using Library   | In-Advance DL  |
| 48.     |       |         |         | quartz-2.2.3.jar | Using Library   | In-Advance DL  |
| 49.     |       |         |         | servlet-api-3.1.jar | Using Library   | In-Advance DL  |
| 50.     |       |         |         | slf4j-api-1.7.21.jar | Using Library   | In-Advance DL  |
| 51.     |       |         |         | slf4j-simple-1.7.21.jar | Using Library   | In-Advance DL  |
| 52.     |       |         |         | snmp4j-2.5.0.jar | Using Library   | In-Advance DL  |
| 53.     |       |         |         | validation-api-1.1.0.Final.jar | Using Library   | In-Advance DL  |
| 54.     |       | conf    |         | ec_main.conf | EC Main Module Configuration File | In-Advance DL from GitHub  |
| 55.     |       |         |         | hibernate.cfg.xml | Hibernate Configuration File | In-Advance DL from GitHub  |
| 56.     |       |         |         | log4j2.xml | log4j2 Configuration File  | In-Advance DL from GitHub  |
| 57.     |       |         |         | extend_operation.conf | Extended Function Configuration File | In-Advance DL from GitHub  |
| 58.     |       | dhcp.v4.2.5 |     | dhcp-4.2.5-42.el7.centos.x86_64.rpm | DHCP Installation Package    | In-Advance DL  |
| 59.     |       | ntp.v4.2 |        | autogen-libopts-5.18-5.el7.x86_64.rpm | NTP Installation Package     | In-Advance DL  |
| 60.     |       |         |         | ntpdate-4.2.6p5-22.el7.centos.x86_64.rpm | NTP Installation Package     | In-Advance DL  |
| 61.     |       |         |         | ntp-4.2.6p5-22.el7.centos.x86_64.rpm | NTP Installation Package     | In-Advance DL  |
| 62.     |       | postgresql.v9.3.13 |  | postgresql93-9.3.13-1PGDG.rhel7.x86_64.rpm | PostgreSQL Installation Package | In-Advance DL  |
| 63.     |       |         |         | postgresql93-contrib-9.3.13-1PGDG.rhel7.x86_64.rpm | PostgreSQL Installation Package | In-Advance DL  |
| 64.     |       |         |         | postgresql93-devel-9.3.13-1PGDG.rhel7.x86_64.rpm | PostgreSQL Installation Package | In-Advance DL  |
| 65.     |       |         |         | postgresql93-libs-9.3.13-1PGDG.rhel7.x86_64.rpm | PostgreSQL Installation Package | In-Advance DL  |
| 66.     |       |         |         | postgresql93-server-9.3.13-1PGDG.rhel7.x86_64.rpm | PostgreSQL Installation Package | In-Advance DL  |
| 67.     |       |         |         | uuid-1.6.2-26.el7.x86_64.rpm | PostgreSQL Installation Package | In-Advance DL  |
| 68.     |       |         |         | libxslt-1.1.28-5.el7.x86_64.rpm | PostgreSQL Installation Package | In-Advance DL  |
| 69.     |       | snmptrapd.v5.7.2 |  | perl-HTTP-Tiny-0.033-3.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 70.     |       |         |         | perl-Pod-Perldoc-3.20-4.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 71.     |       |         |         | perl-podlators-2.5.1-3.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 72.     |       |         |         | perl-Encode-2.51-7.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 73.     |       |         |         | perl-Text-ParseWords-3.29-4.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 74.     |       |         |         | perl-Pod-Usage-1.63-3.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 75.     |       |         |         | perl-constant-1.27-2.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 76.    |       |         |         | perl-Time-Local-1.2300-2.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 77.     |       |         |         | perl-Storable-2.45-3.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 78.     |       |         |         | perl-Socket-2.010-3.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 79.     |       |         |         | perl-Scalar-List-Utils-1.27-248.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 80.     |       |         |         | perl-File-Temp-0.23.01-3.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 81.     |       |         |         | perl-Getopt-Long-2.40-2.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 82.     |       |         |         | perl-File-Path-2.09-2.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 83.     |       |         |         | perl-Exporter-5.68-3.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 84.     |       |         |         | perl-Carp-1.26-244.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 85.     |       |         |         | perl-PathTools-3.40-5.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 86.     |       |         |         | perl-Pod-Escapes-1.04-286.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 87.     |       |         |         | perl-macros-5.16.3-286.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 88.     |       |         |         | perl-threads-shared-1.43-6.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 89.     |       |         |         | perl-threads-1.87-4.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 90.     |       |         |         | perl-Time-HiRes-1.9725-3.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 91.     |       |         |         | perl-Pod-Simple-3.28-4.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 92.     |       |         |         | perl-Filter-1.49-3.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 93.     |       |         |         | perl-parent-0.225-244.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 94.     |       |         |         | net-snmp-agent-libs-5.7.2-24.el7_2.1.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 95.     |       |         |         | net-snmp-libs-5.7.2-24.el7_2.1.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 96.     |       |         |         | perl-5.16.3-286.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 97.     |       |         |         | perl-libs-5.16.3-286.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 98.     |       |         |         | perl-Data-Dumper-2.145-3.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 99.     |       |         |         | net-snmp-5.7.2-24.el7_2.1.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 100.     |       |         |         | openssl098e-0.9.8e-29.el7.centos.3.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 101.     |       |         |         | glibc-2.17-105.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 102.    |       |         |         | glibc-common-2.17-105.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 103.    |       |         |         | lm_sensors-libs-3.3.4-11.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 104.    |       | java.v8u92 |      | jdk-8u92-linux-x64.rpm | Java Installation Package    | In-Advance DL  |
| 105.    |       | pacemaker.v1.1.14-1.1 |  | pacemaker-1.1.14-1.el7.x86_64.rpm | Pacemaker Installation Package | In-Advance DL  |
| 106.    |       |         |         | corosync-2.3.5-1.el7.x86_64.rpm | Corosync  Installation Package | In-Advance DL  |
| 107.    |       |         |         | crmsh-2.1.5-1.el7.x86_64.rpm | Crm Command Installation Package     | In-Advance DL  |
| 108.    |       |         |         | cluster-glue-1.0.12-2.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 109.    |       |         |         | cluster-glue-libs-1.0.12-2.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 110.    |       |         |         | corosynclib-2.3.5-1.el7.x86_64.rpm | Corosync Dependent Package | In-Advance DL  |
| 111.    |       |         |         | ipmitool-1.8.13-9.el7_2.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 112.    |       |         |         | libqb-1.0-1.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 113.    |       |         |         | libtool-ltdl-2.4.2-21.el7_2.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 114.    |       |         |         | libxslt-1.1.28-5.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 115.    |       |         |         | libyaml-0.1.4-11.el7_0.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 116.    |       |         |         | lm_sensors-libs-3.3.4-11.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 117.    |       |         |         | nano-2.3.1-10.el7.x86_64.rpm | crm Dependent Package | In-Advance DL  |
| 118.    |       |         |         | net-snmp-agent-libs-5.7.2-24.el7_2.1.x86_64.rpm | Corosync Dependent Package | In-Advance DL  |
| 119.    |       |         |         | net-snmp-libs-5.7.2-24.el7_2.1.x86_64.rpm | Corosync Dependent Package | In-Advance DL  |
| 120.    |       |         |         | openhpi-libs-3.4.0-2.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 121.    |       |         |         | OpenIPMI-libs-2.0.19-11.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 122.    |       |         |         | OpenIPMI-modalias-2.0.19-11.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 123.    |       |         |         | pacemaker-cli-1.1.14-1.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 124.    |       |         |         | pacemaker-cluster-libs-1.1.14-1.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 125.    |       |         |         | pacemaker-libs-1.1.14-1.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 126.    |       |         |         | pacemaker-all-1.1.14-1.1.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 127.    |       |         |         | perl-5.16.3-286.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 128.    |       |         |         | perl-Carp-1.26-244.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 129.    |       |         |         | perl-constant-1.27-2.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 130.    |       |         |         | perl-Encode-2.51-7.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 131.    |       |         |         | perl-Exporter-5.68-3.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 132.    |       |         |         | perl-File-Path-2.09-2.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 133.    |       |         |         | perl-File-Temp-0.23.01-3.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 134.    |       |         |         | perl-Filter-1.49-3.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 135.    |       |         |         | perl-Getopt-Long-2.40-2.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 136.    |       |         |         | perl-HTTP-Tiny-0.033-3.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 137.    |       |         |         | perl-libs-5.16.3-286.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 138.    |       |         |         | perl-macros-5.16.3-286.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 139.    |       |         |         | perl-parent-0.225-244.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 140.    |       |         |         | perl-PathTools-3.40-5.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 141.    |       |         |         | perl-Pod-Escapes-1.04-286.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 142.    |       |         |         | perl-podlators-2.5.1-3.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 143.    |       |         |         | perl-Pod-Perldoc-3.20-4.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 144.    |       |         |         | perl-Pod-Simple-3.28-4.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 145.    |       |         |         | perl-Pod-Usage-1.63-3.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 146.    |       |         |         | perl-Scalar-List-Utils-1.27-248.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 147.    |       |         |         | perl-Socket-2.010-3.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 148.    |       |         |         | perl-Storable-2.45-3.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 149.    |       |         |         | perl-Text-ParseWords-3.29-4.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 150.    |       |         |         | perl-threads-1.87-4.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 151.    |       |         |         | perl-threads-shared-1.43-6.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 152.    |       |         |         | perl-TimeDate-2.30-2.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 153.    |       |         |         | perl-Time-HiRes-1.9725-3.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 154.    |       |         |         | perl-Time-Local-1.2300-2.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 155.    |       |         |         | pm_crmgen-2.1-1.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 156.    |       |         |         | pm_diskd-2.2-1.el7.x86_64.rpm | Diskd RA Package   | In-Advance DL  |
| 157.    |       |         |         | pm_extras-2.2-1.el7.x86_64.rpm | VIPCheck RA Packge | In-Advance DL  |
| 158.    |       |         |         | pm_logconv-cs-2.2-1.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 159.    |       |         |         | psmisc-22.20-9.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 160.    |       |         |         | pssh-2.3.1-5.el7.noarch.rpm | crm Dependent Package     | In-Advance DL  |
| 161.    |       |         |         | resource-agents-3.9.7-1.2.6f56.el7.x86_64.rpm | Standard RA Package Including Virtual IPRA | In-Advance DL  |
| 162.    |       |         |         | python-clufter-0.50.4-1.el7.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 163.    |       |         |         | python-dateutil-1.5-7.el7.noarch.rpm | pcs Dependent Package | In-Advance DL  |
| 164.    |       |         |         | python-lxml-3.2.1-4.el7.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 165.    |       |         |         | resource-agents-3.9.7-1.2.6f56.el7.x86_64.rpm | Standard RA Package Including Virtual IPRA | In-Advance DL  |
| 166.    |       |         |         | ruby-irb-2.0.0.598-25.el7_1.noarch.rpm | pcs Dependent Package | In-Advance DL  |
| 167.    |       |         |         | ruby-libs-2.0.0.598-25.el7_1.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 168.    |       |         |         | rubygem-bigdecimal-1.2.0-25.el7_1.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 169.    |       |         |         | rubygem-io-console-0.4.2-25.el7_1.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 170.    |       |         |         | rubygem-json-1.7.7-25.el7_1.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 171.    |       |         |         | rubygem-psych-2.0.0-25.el7_1.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 172.    |       |         |         | rubygem-rdoc-4.0.0-25.el7_1.noarch.rpm | pcs Dependent Package | In-Advance DL  |
| 173.    |       |         |         | rubygems-2.0.14-25.el7_1.noarch.rpm | pcs Dependent Package | In-Advance DL  |
| 174.    |       | httpd.v2.4.6 |    | apr-1.4.8-3.el7.x86_64.rpm | httpd Installation Package | In-Advance DL  |
| 175.    |       |         |         | apr-util-1.5.2-6.el7.x86_64.rpm | httpd Installation Package | In-Advance DL  |
| 176.    |       |         |         | mailcap-2.1.41-2.el7.noarch.rpm | httpd Installation Package | In-Advance DL  |
| 177.    |       |         |         | httpd-tools-2.4.6-40.el7.centos.4.x86_64.rpm | httpd Installation Package | In-Advance DL  |
| 178.    |       |         |         | httpd-2.4.6-40.el7.centos.4.x86_64.rpm | httpd Installation Package | In-Advance DL  |
| 179.    |       |         |         | httpd-manual-2.4.6-40.el7.centos.4.noarch.rpm | httpd Installation Package | In-Advance DL  |
| 180.    |       |         |         | mod_ssl-2.4.6-40.el7.centos.4.x86_64.rpm | httpd Installation Package | In-Advance DL  |
| 181.    |       | sysstat-11.6.0-1 |  | sysstat-11.6.0-1.x86_64.rpm | Sysstat Installation Package | In-Advance DL  |
| 182.    |       | tftp.v5.2 |       | xinetd-2.3.15-12.el7.x86_64.rpm | tftpd Installation Package   | In-Advance DL  |
| 183.    |       |         |         | tftp-5.2-12.el7.x86_64.rpm | tftpd Installation Package   | In-Advance DL  |
| 184.    |       |         |         | tftp-server-5.2-12.el7.x86_64.rpm | tftpd Installation Package   | In-Advance DL  |
| 185.    |       | expect-5.45-14 |       | expect-5.45-14.el7_1.x86_64.rpm | expect Installation Package   | In-Advance DL  |
| 186.    |       |         |         | tcl-8.5.13-8.el7.x86_64.rpm | tcl Installation Package   | In-Advance DL  |
| 187.    |       | bc-1.06.95-13 |       | bc-1.06.95-13.el7.x86_64.rpm | bc Installation Package   | In-Advance DL  |
| 188.    |       | database |         | \-      | \-      | \-      |
| 189.    |       |         |         | create_table.sql | Table Creation Script   | In-Advance DL from GitHub  |
| 190.    |       | RA      |         | \-      | \-      | \-      |
| 191.    |       |         |         | pm_crmgen_env.xls | Resource Agent Configuration File | In-Advance DL  |
| 192.    |       |         |         | ec      | Resource Agent | In-Advance DL from GitHub  |
| 193.    |       |         |         | snmptrapd | Resource Agent | In-Advance DL from GitHub  |
| 194.    | Ansible |         |         |           |               |         |
| 195.    |       |         |         | ansible-2.4.2.0-2.el7.noarch.rpm | Ansible Installation Package   | In-Advance DL  |
| 196.    |       |         |         | PyYAML-3.10-11.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 197.    |       |         |         | libyaml-0.1.4-11.el7_0.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 198.    |       |         |         | openssl-1.0.2k-8.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 199.    |       |         |         | openssl-libs-1.0.2k-8.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 200.    |       |         |         | python-babel-0.9.6-8.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 201.    |       |         |         | python-backports-1.0-8.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 202.    |       |         |         | python-backports-ssl_match_hostname-3.4.0.2-4.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 203.    |       |         |         | python-cffi-1.6.0-5.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 204.    |       |         |         | python-enum34-1.0.4-1.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 205.    |       |         |         | python-httplib2-0.9.2-1.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 206.    |       |         |         | python-idna-2.4-1.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 207.    |       |         |         | python-ipaddress-1.0.16-2.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 208.    |       |         |         | python-jinja2-2.7.2-2.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 209.    |       |         |         | python-markupsafe-0.11-10.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 210.    |       |         |         | python-paramiko-2.1.1-2.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 211.    |       |         |         | python-passlib-1.6.5-2.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 212.    |       |         |         | python-ply-3.4-11.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 213.    |       |         |         | python-python-pycparser-2.14-1.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 214.    |       |         |         | python-setuptools-0.9.8-7.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 215.    |       |         |         | python-six-1.9.0-2.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 216.    |       |         |         | python2-cryptography-1.7.2-1.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 217.    |       |         |         | python2-jmespath-0.9.0-3.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 218.    |       |         |         | python2-pyasn1-0.1.9-7.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 219.    |       |         |         | sshpass-1.06-2.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |

## 2. Operational Environment

2.1. EC Main Module Start-up Server of Controller
------------------------------------------------

### 2.1.1. Hardware Operating environment

It is recommended to operate the software on the following Linux
computer environment.

Table 2-1 Recommended Hardware Configuration

| No.  | Computer      | Minimum Configuration                |
|------|---------------|--------------------------------------|
| 1.   | OS            | CentOS7.2 x86\_64                    |
| 2.   | CPU           | IntelR XeonR CPU E5-2420 v2 @2.20GHz <br> 6 Core/12 Thread or greater |
| 3.   | Memory        | 32GB or larger                       |
| 4.   | HD Free Space | 500G or larger                       |
| 5.   | NIC           | More than 1 port                     |

### 2.1.2. Software Operating environment
Assume that firewalld has already been installed as a package.
If you install this application by the internet using a proxy server, 
you need to confirm that target servers are able to accsess the internet by https and http protcol.
Also assume that wget has already been installed as a package.


## 3. Installation of Controller Server

The instructions described in this section must be performed by the root
user unless any specific user is specified.

**&lt;Execution Host: Ansible&gt;**

Create a working folder where the files generated in the process of
installation are located.

(It will be deleted when the installation of Controller Server is
completed.)

**\[mkdir \~/setup\] \[Enter\]**

In case of non-connected Internet environment,
locate the ec\_main folder which is configured as described in \"1.5
Configuration of the Included Accessories \" above in the working
folder. (The Ansible folder is unnecessary.)<br>
Even in case of connected Internet environment,
you need to download the necessary files in installing java.
And then, place it under the work folder along the path shown in
\"1.5 Configuration of the Included Accessories \".<br>
Specifically, place the following file manually.<br>
<br>
/root/setup/ec_main/java.v8u92/jdk-8u92-linux-x64.rpm

3.1. Ansible Installation
--------------------------

**&lt;Execution Host: Ansible&gt;**

Before Installation, place the rpm files in Ansible installation destination server.
The placement targets are the files under the Anslbile folder described in \"1.5 Configuration of the Included Accessories \".<br>
(Locations: /root/setup/ec/installer/ansible)

Execute the following command to install Ansible.

**\[cd /root/setup/ec/installer/ansible\] \[Enter\]**

**\[rpm -Uvh \*rpm\] \[Enter\]**


3.2. Controller Server Installation
--------------------------

### 3.2.1. Prepare Installation

### 3.2.1.1. Deploy SSH key

**&lt;Execution Host: Ansible&gt;**

Generate the SSH authentication key with the following command.

**\[ssh -keygen -t rsa\] \[Enter\]**

After that, copy the generated key to the target servers.

**\[scp ~/.ssh/id_rsa.pub root@$REMOTE_IP:~\] \[Enter\]**

($REMOTE_IP: IP of the install target server (EC Main Module or DB))

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command on each server.

**\[cd /root/.ssh/\] \[Enter\]**

**\[touch authorized_keys\] \[Enter\]**

**\[chmod 600 authorized_keys\] \[Enter\]**

**\[cat ~/id_rsa.pub >> authorized_keys\] \[Enter\]**

**\[rm ~/id_rsa.pub\] \[Enter\]**


### 3.2.1.2. Deploy Instllation Files

**&lt;Execution Host: Ansible&gt;**

Before Installation, place the playbook file in Ansible installation destination server.<br>
A set of Playbooks can be download at the URL below.

> Download URL : https://github.com/multi-service-fabric/element-controller/tree/master/playbook <br>

(Locations: /root/setup/playbook/)<br>
If you do not use the Internet connection, place rpm files for install.
However, you need to manually DL java rpm file and place it, even if you have an Internet connection.


#### 3.2.1.3. Edit playbook
Write the IP information of EC and DB to the host file which use by playbook.
Also, put the yml files which has environmental information for each server to the vars folder.


##### 3.2.1.3.1. Edit host file
Write the DB server name in \[DB:children\].

Write the EC server name in \[EC:children\].

Also, write the IP address for each of the above server names.

example)
> [DB:children]
>
> db1
>
> db2
> 	
> [EC:children]
>
> ec1
>
> ec2
>
> [db1]
>
> 192.168.0.73
>
> [db2]
>
> 192.168.0.74
>
> [ec1]
>
> 192.168.0.75
>
> [ec2]
>
> 192.168.0.76


##### 3.2.1.3.2. Deploy vars file
Place tha information files defined by the host.
The meaning of each parameter is described below.<br>
Regarding variables in which the recommended value is described in the Description column,
it is possible to operate by setting the recommended value especially when there is no need to perform arbitrary setting.

Table 3-1 vars Parameter

| name                | Description                          | EC | DB |
|---------------------|--------------------------------------|----|----|
| rpm_path            | In the case of using network connection, the path of installing file location. <br> In the case of no network connection, it is installing files path in the Ansible server. <br> (Recommendation: /root/setup/ec_main) | ○ | ○ |
| ec_path             | The path of EC location. <br> (Recommendation: /usr/ec_main). | ○ | × |
| download_flag       | Parameter for determining whether to acquire a file from the Internet or place it on an Ansible server. <br> If True, use the Internet connection. | ○ | ○ |
| installing_ec_path  | The path of EC source folder location in Ansible Server. <br> (Recommendation: /root/setup/ec_main) | ○ | × |
| log_path            | Relative path of EC log file output destination. <br> (Recommendation: logs/ec/log) | ○ | × |
| ec_jar_name         | The name of EC jar file. <br> (Recommendation: EcMainModule.jar) | ○ | × |
| db_address          | DB server IP address.                | ○ | ○ |
| db_name             | DB name. (Recommendation: msf_ec_1)  | ○ | ○ |
| ec_physical_address | EC physical IP address.              | ○ | × |
| ec_rest_address     | EC REST IP address.                  | ○ | × |
| ec_rest_port        | EC REST port. (Recommendation: 18080)  | ○ | × |
| em_netconf_address  | EM netconf IP address.               | ○ | × |
| em_netconf_port     | EM netconf port. (Recommendation: 831)  | ○ | × |
| em_rest_address     | EM REST IP address.                  | ○ | × |
| em_rest_port        | EM REST port. (Recommendation: 8080) | ○ | × |
| fc_rest_address     | FC REST IP address.                  | ○ | × |
| fc_rest_port        | FC REST port.                        | ○ | × |
| device_cidr         | The network address of the device that allows reception by Rsyslog. (CIDR) | ○ | × |
| dhcp_if_name        | The IF name (device name) of EC to be used when starting Dhcp server. | ○ | × |
| controller_cidr     | The network name of the server that the DB allows for connections. (CIDR) | ○ | ○ |
| ntp_server_address  | NTP server address.                  | ○ | × |
| ha_flag             | Flag indicating whether to implement redundancy. In the case of truth it is implemented. | ○ | ○ |
| act_address         | Inter connect IP address for act server. (When ha_flag is False, it is set to none) | ○ | ○ |
| act_node_name       | Name for act server. (When ha_flag is False, it is set to none) | ○ | ○ |
| sby_address         | Inter connect IP address for stand-by server. (When ha_flag is False, it is set to none) | ○ | ○ |
| sby_node_name       | Name for stand-by server. (When ha_flag is False, it is set to none) | ○ | ○ |
| cluster_name        | Cluster name (When ha_flag is False, it is set to none) <br> (Recommendation: ec_cluster) | ○ | ○ |
| install_flag        | When setting the DB, it decides whether to install PostgreSQL. <br> In case of True, execute installation. | × | ○ |

#### 3.2.1.4. Delete existing DB
If the same name DB (db_name specified in the vars file) already exists in the installation DB server, it is necessary to delete it beforehand.

**&lt;Execution Host: DB&gt;**

Execute the following command and delete DB with postgres user.

**\[su - postgres\] \[Enter\]**

**\[dropdb \[DB name\]\] \[Enter\]**

### 3.2.2. Execute Installation
**&lt;Execution Host: Ansible&gt;**

Execute the following command to install this application.

**\[cd /root/setup/playbook/EC] \[Enter\]**

**\[ansible-playbook ec.yml -i hosts\] \[Enter\]**


### 3.2.3. Edit Configuration

#### 3.2.3.1. Change of SNMPTrap Configuration File

**&lt;Execution Host: ACT/SBY&gt;**

Add the following lines to the SNMPTrap configuration file
(/etc/snmp/snmptrapd.conf) (append the highlighted section).

If there are multiple community names, list them in multiple lines.

**(The "\$EC\_HOME/" below must be replaced with the same path as
"3.2.3Locating the Script".)**

**\[vi /etc/snmp/snmptrapd.conf\] \[Enter\]**

```

authCommunity log,execute "Community Name A"

authCommunity log,execute "Community Name B"

#trap reception of Linkdown

traphandle .1.3.6.1.6.3.1.1.5.3 $EC_HOME/bin/linkdown.sh

#trap reception of Linkup

traphandle .1.3.6.1.6.3.1.1.5.4 $EC_HOME/bin/linkup.sh

```

In the case of installing some EC controller in one server,
write traphandle of the number of EC contollers.

ex.) in the case of installing two EC controllers in one server 
(Path of EC instolled\: /usr/ec_main1, /usr/ec_main2)

```

authCommunity log,execute "Community Name A"

authCommunity log,execute "Community Name B"

#first EC

#trap reception of Linkdown

traphandle .1.3.6.1.6.3.1.1.5.3 /usr/ec_main1/bin/linkdown.sh

#trap reception of Linkup

traphandle .1.3.6.1.6.3.1.1.5.4 /usr/ec_main1/bin/linkup.sh

#second EC

#trap reception of Linkdown

traphandle .1.3.6.1.6.3.1.1.5.3 /usr/ec_main2/bin/linkdown.sh

#trap reception of Linkup

traphandle .1.3.6.1.6.3.1.1.5.4 /usr/ec_main2/bin/linkup.sh

```

#### 3.2.3.2. Edit of EC configure file
The settings required for the operation (IP,port,file path etc...) are automatically replaced with the parameters at the time of installation.
If you want to change other setting, you need to fix it by hand.

Change the EC Main Module configuration file by use of the following command.

**\[vi $EC_HOME/ec_main/conf/ec_main.conf\] \[Enter\]**

Please refer to "element\_controller\_configuration\_specifications.md" for the details of the change.

extend\_operation.conf is necessary in system operation, however there is no need to edit it.

#### 3.2.3.3. Making of crm File

**&lt;Execution Host: ACT&gt;**

Edit the pm\_crmgen\_env.xls file(*1), which has the configuration of resource
agent in the included accessories, for updating the necessary
information, then convert it to a csv file and locate in the home folder
of the active system.

Execute the following command at the folder where you locate the csv
file to convert it into a crm file that is used for registering it to
the resource agent.

**\[pm\_crmgen -o \$EC\_HOME/conf/crm\_conf.crm (located csv file name).csv\] \[Enter\]**

If the conversion completes successfully, nothing will be displayed in
the screen but in case anything went wrong with the csv file, the
location to be amended would be displayed.

*1. Make sure to acquire the file “pm_crmgen_env.xls” from the following URL.<br>
URL : https://github.com/linux-ha-japan/pm_crmgen-1.0/blob/master/pm_crmgen_env.xls <br>
In addition, please follow the procedures shown below when editing the file “pm_crmgen_env.xls”.<br>
Moreover, add new lines to the original file when required.<br>

Since the following values in the figure are examples, you need to change the value.

EC_CONTROL_SHELL (Figure 3-4) : Path of ec_ctl.sh (absolute path)<br>
device (Figure 3-5) : Target physical volumes of disk check<br>
target_ip (Figure 3-6) : IP Address (EC, virtual IP address)<br>
ip (Figure 3-7) : IP Address (EC, virtual IP address)<br>
nic (Figure 3-7) : Name of the NIC to which the virtual IP address is assigned.<br>
cidr_netmask (Figure 3-7) : Network prefix of Virtual IP address<br>


![Figure 3-1 Cluster property](img/Fig_3_1.png "Fig3-1")<br>
Figure 3-1 Cluster property

![Figure 3-2 Resource default](img/Fig_3_2.png "Fig3-2")<br>
Figure 3-2 Resource default

![Figure 3-3 Resource structure](img/Fig_3_3.png "Fig3-3")<br>
Figure 3-3 Resource structure

![Figure 3-4 Primitive resource (id=prmEC)](img/Fig_3_4.png "Fig3-4")<br>
Figure 3-4 Primitive resource (id=prmEC)

![Figure 3-5 Primitive resource (id=prmDiskd)](img/Fig_3_5.png "Fig3-5")<br>
Figure 3-5 Primitive resource (id=prmDiskd)

![Figure 3-6 Primitive resource (id=vipCheck)](img/Fig_3_6.png "Fig3-6")<br>
Figure 3-6 Primitive resource (id=vipCheck)

![Figure 3-7 Primitive resource (id=prmIP)](img/Fig_3_7.png "Fig3-7")<br>
Figure 3-7 Primitive resource (id=prmIP)

![Figure 3-8 Primitive resource (id=prmSNMPTrapd)](img/Fig_3_8.png "Fig3-8")<br>
Figure 3-8 Primitive resource (id=prmSNMPTrapd)

![Figure 3-9 Resource location restriction](img/Fig_3_9.png "Fig3-9")<br>
Figure 3-9 Resource location restriction

![Figure 3-10 Resource colocation restriction](img/Fig_3_10.png "Fig3-10")<br>
Figure 3-10 Resource colocation restriction

![Figure 3-11 Resource activation order restriction](img/Fig_3_11.png "Fig3-11")<br>
Figure 3-11 Resource activation order restriction

#### 3.2.3.4. Injection of crm File

**&lt;Execution Host: ACT&gt;**

With the following commend, register the resource agent.

**\[crm configure load update \$EC\_HOME/conf/crm\_conf.crm\] \[Enter\]**

If the injection completes successfully, nothing will be displayed in
the screen. So you need to check the result by following the next
instruction. (\*Although a message which says the configured number of
seconds for VIPcheck is shorter than the default, you can ignore it and
keep on going.)

If there is any critical error in the configuration, a warning with the
location of the error will be displayed and you will be prompted to
answer with Y/N whether you want to keep the injection going or not.
When this warning is displayed, there must be errors in the
configuration, and you should answer it by entering \[N\] \[Enter\].

3.3. Confirm Setting
------------------

### 3.3.1. Confirmation of firewall configuration

Confirm the current configuration by executing the following command
(especially for the highlighted section).

**\[firewall-cmd \--list-all\] \[Enter\]**

**&lt;Execution Host: ACT/SBY&gt;**

public (default, active)

interfaces:

sources:

services: dhcpv6-client `high-availability` `http` ssh `tftp`

ports: `514/tcp` `162/udp` `514/udp` `18080/tcp`

masquerade: no

forward-ports:

icmp-blocks:

rich rules:

**&lt;Execution Host: DB&gt;**

public (default, active)

interfaces:

sources:

services: ssh

ports: `5432/tcp`

masquerade: no

forward-ports:

icmp-blocks:

rich rules:


### 3.3.2. Confirmation of ntp operation

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to confirm the synchronization with the
NTP server.

**\[ntpq -p\] \[Enter\]**

&lt;The output example of successful synchronization&gt;

| remote refid st t when poll reach delay offset jitter               |
| ------------------------------------------------------------------- |
| \*xxx.xxx.xxx.xxx LOCAL(0) 11 u 55 64 377 0.130 -0.017 0.017        |


### 3.3.3. Confirmation of pacemaker operation

#### 3.3.3.1. Confirmation of Installation of Pacemaker

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to confirm the version of Corosync.

**\[corosync -version\] \[Enter\]**

If the installation has been completed successfully, the following
message will be displayed.

> Corosync Cluster Engine, version \'2.3.5\'
>
> Copyright (c) 2006-2009 Red Hat, Inc.

Execute the following command to confirm the version of Pacemaker.

**\[crmadmin \--version\] \[Enter\]**

If the installation has been completed successfully, the following
message will be displayed.

> Pacemaker 1.1.14-1.el7
>
> Written by Andrew Beekhof

Execute the following command to confirm the version of crm.

**\[crm \--version\] \[Enter\]**

If the installation has been completed successfully, the following
message will be displayed.

> 2.1.5-1.el7 (Build unknown)

Execute the following command to confirm the resource agent which is
going to be used is actually installed.

**\[ls /lib/ocf/resource.d/pacemaker/\] \[Enter\]**

If the installation has been completed successfully, the following
message will be displayed.

> diskd

Execute the following command to confirm the resource agent which is
going to be used is actually installed.

**\[ls /lib/ocf/resource.d/heartbeat/\] \[Enter\]**

If the installation has been completed successfully, the following
message will be displayed.

> VIPcheck、IPaddr2

Confirm the configuration of the hosts

**\[ping "Stand-by Host Name"\] \[Enter\]**

> PING "Stand-by Host Name" (IP address for the stand-by
> interconnection) 56(84) bytes of data.
>
> 64 bytes from "Stand-by Host Name" (IP address for the stand-by
> interconnection): icmp\_seq=1 ttl=64 time=0.166 ms

**\[ping "Active Host Name"\] \[Enter\]**

> PING "Active Host Name" (IP address for the active interconnection)
> 56(84) bytes of data.
>
> 64 bytes from "Active Host Name" (IP address for the active
> interconnection): icmp\_seq=1 ttl=64 time=0.166 ms

In case the IP address and the Host Name are not displayed like this,
review the configuration at /etc/hosts.

#### 3.3.3.2. Confirmation of the Inter-node Communication Status

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to confirm the status of inter-node
communication by use of \"corosync-cfgtool -s\" command.

This task must be performed both at the active and the stand-by nodes.

**\[corosync-cfgtool -s\] \[Enter\]**

If the cluster is started successfully, the following message will be
displayed in the screen.

When the \"status\" is \"active\" and \"no faults\", the communication
is working properly.

> Printing ring status.
>
> Local node ID (1 or 2)
>
> RING ID 0
>
> id = (IP address of the Active or Stand-by system)
>
> status = ring 0 active with no faults

#### 3.3.3.3. Confirmation of the Result of Injection

**&lt;Execution Host\: ACT or SBY&gt;**

Confirm the operational status of resource agent with the following
command.


**\[crm\_mon -fA -1\] \[Enter\]**

If it injected successfully, a message will be displayed as follows.

> Last updated: WDW MMM DD HH:MM:SS YYYY Last change: WDW MMM DD
> HH:MM:SS YYYY by root via cibadmin on (Active Node Name or Stand-by
> Node Name)
>
> Stack: corosync
>
> Current DC: (Active Node Name or Stand-by Node Name) (version
> 1.1.14-1.el7-70404b0) - partition with quorum
>
> 2 nodes and 5 resources configured
>
> Online: \[(Active Node Name) (Stand-by Node Name)\]
>
> Resource Group: grpEC
>
> vipCheck (VIPcheck): Started (Active Node Name))
>
> prmIP (IPaddr2): Started (Active Node Name)
>
> prmEC (ec): Started (Active Node Name)
>
> prmSNMPTrapd (snmptrapd): Started (Active Node Name)
>
> Clone Set: clnDiskd \[prmDiskd\]
>
> Started: \[(Active Node Name) (Stand-by Node Name)\]
>
> Node Attribute:
>
> \* Node (Active Node Name)
>
> \+ diskcheck\_status\_internal : normal
>
> \* Node (Stand-by Node Name)
>
> \+ diskcheck\_status\_internal : normal
>
> Migration Summary:
>
> \* Node (Active Node Name)
>
> \* Node (Stand-by Node Name)
> Summary:
>
> \* Node (Active Node Name)
>
> \* Node (Stand-by Node Name)
