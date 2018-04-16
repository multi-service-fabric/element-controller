# Element Controller Automate Installation Manual

**Version 1.0**
**March 28, 2018**
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
However, downloading java installation package is necessary regardless of network connection.

Table 1-1 Included
Accessories

| \#      | Folder Structure | | | File    | Description | Remarks |
|---------|---------|---------|---------|---------|---------|---------|
| 1.      | ec_main | \-      | \-      | \-      | \-        | \-        |
| 2.      |         | bin     |         | boot.sh | Inter-devices I/F Control Functinal Part <br> Device Start-up Notification Script | In-Advance DL from GitHub  |
| 3.      |         |         |         | ec\_ctl.sh | EC Start-up Script | In-Advance DL from GitHub  |
| 4.      |         |         |         | linkdown.sh | SNMPTrap Functional Part Link-down <br> Notification Script | In-Advance DL from GitHub  |
| 5.      |         |         |         | linkup.sh | SNMPTrap Functional Part Link-up <br> Notification Script | In-Advance DL from GitHub  |
| 6.      |         |         |         | controller\_status.sh | Controler Status Information Acquisition Script | In-Advance DL from GitHub  |
| 7.      |         | lib     |         | EcMainModule.jar | EC Main Module | In-Advance DL and compile (Only if you perform installation in no internet connection. <br> Compile details is listed "building_guide". )  |
| 8.      |         |         |         | NetConf.jar | NetConf Library | In-Advance DL from GitHub  |
| 9.      |         |         |         | antlr-2.7.7.jar | Using Library   | In-Advance DL  |
| 10.     |       |         |         | c3p0-0.9.2.1.jar | Using Library   | In-Advance DL  |
| 11.     |       |         |         | commons-io-2.5.jar | Using Library   | In-Advance DL  |
| 12.     |       |         |         | dom4j-1.6.1.jar | Using Library   | In-Advance DL  |
| 13.     |       |         |         | ganymed-ssh2-build210.jar | Using Library   | In-Advance DL  |
| 14.     |       |         |         | geronimo-jta_1.1_spec-1.1.1.jar | Using Library   | In-Advance DL  |
| 15.     |       |         |         | gson-2.7.jar | Using Library   | In-Advance DL  |
| 16.     |       |         |         | hibernate-c3p0-5.0.10.Final.jar | Using Library   | In-Advance DL  |
| 17.     |       |         |         | hibernate-commons-annotations-5.0.1.Final.jar | Using Library   | In-Advance DL  |
| 18.     |       |         |         | hibernate-core-5.0.10.Final.jar | Using Library   | In-Advance DL  |
| 19.     |       |         |         | hibernate-jpa-2.1-api-1.0.0.Final.jar | Using Library   | In-Advance DL  |
| 20.     |       |         |         | hk2-api-2.5.0-b05.jar | Using Library   | In-Advance DL  |
| 21.     |       |         |         | hk2-locator-2.5.0-b05.jar | Using Library   | In-Advance DL  |
| 22.     |       |         |         | hk2-utils-2.5.0-b05.jar | Using Library   | In-Advance DL  |
| 23.     |       |         |         | javassist-3.18.1-GA.jar | Using Library   | In-Advance DL  |
| 24.     |       |         |         | javax.annotation-api-1.2.jar | Using Library   | In-Advance DL  |
| 25.     |       |         |         | javax.inject-2.5.0-b05.jar | Using Library   | In-Advance DL  |
| 26.     |       |         |         | javax.ws.rs-api-2.0.1.jar | Using Library   | In-Advance DL  |
| 27.     |       |         |         | jboss-logging-3.3.0.Final.jar | Using Library   | In-Advance DL  |
| 28.     |       |         |         | jersey-client.jar | Using Library   | In-Advance DL  |
| 29.     |       |         |         | jersey-common.jar | Using Library   | In-Advance DL  |
| 30.     |       |         |         | jersey-container-servlet-core.jar | Using Library   | In-Advance DL  |
| 31.     |       |         |         | jersey-guava-2.23.2.jar | Using Library   | In-Advance DL  |
| 32.     |       |         |         | jersey-server.jar | Using Library   | In-Advance DL  |
| 33.     |       |         |         | jetty-http-9.3.11.v20160721.jar | Using Library   | In-Advance DL  |
| 34.     |       |         |         | jetty-io-9.3.11.v20160721.jar | Using Library   | In-Advance DL  |
| 35.     |       |         |         | jetty-security-9.3.11.v20160721.jar | Using Library   | In-Advance DL  |
| 36.     |       |         |         | jetty-server-9.3.11.v20160721.jar | Using Library   | In-Advance DL  |
| 37.     |       |         |         | jetty-servlet-9.3.11.v20160721.jar | Using Library   | In-Advance DL  |
| 38.     |       |         |         | jetty-util-9.3.11.v20160721.jar | Using Library   | In-Advance DL  |
| 39.     |       |         |         | jsch-0.1.53.jar | Using Library   | In-Advance DL  |
| 40.     |       |         |         | log4j-api-2.6.2.jar | Using Library   | In-Advance DL  |
| 41.     |       |         |         | log4j-core-2.6.2.jar | Using Library   | In-Advance DL  |
| 42.     |       |         |         | log4j-slf4j-impl-2.6.2.jar | Using Library   | In-Advance DL  |
| 43.     |       |         |         | mchange-commons-java-0.2.3.4.jar | Using Library   | In-Advance DL  |
| 44.     |       |         |         | org.eclipse.persistence.core.jar | Using Library   | In-Advance DL  |
| 45.     |       |         |         | postgresql-9.4.1209.jre7.jar | Using Library   | In-Advance DL  |
| 46.     |       |         |         | quartz-2.2.3.jar | Using Library   | In-Advance DL  |
| 47.     |       |         |         | servlet-api-3.1.jar | Using Library   | In-Advance DL  |
| 48.     |       |         |         | slf4j-api-1.7.21.jar | Using Library   | In-Advance DL  |
| 49.     |       |         |         | slf4j-simple-1.7.21.jar | Using Library   | In-Advance DL  |
| 50.     |       |         |         | snmp4j-2.5.0.jar | Using Library   | In-Advance DL  |
| 51.     |       |         |         | validation-api-1.1.0.Final.jar | Using Library   | In-Advance DL  |
| 52.     |       | conf    |         | ec_main.conf | EC Main Module Configuration File | In-Advance DL from GitHub  |
| 53.     |       |         |         | hibernate.cfg.xml | Hibernate Configuration File | In-Advance DL from GitHub  |
| 54.     |       |         |         | log4j2.xml | log4j2 Configuration File  | In-Advance DL from GitHub  |
| 55.     |       | installer |         | \-      | \-      | \-      |
| 56.     |       |         | dhcp.v4.2.5 | dhcp-4.2.5-42.el7.centos.x86_64.rpm | DHCP Installation Package    | In-Advance DL  |
| 57.     |       |         | ntp.v4.2 | autogen-libopts-5.18-5.el7.x86_64.rpm | NTP Installation Package     | In-Advance DL  |
| 58.     |       |         |         | ntpdate-4.2.6p5-22.el7.centos.x86_64.rpm | NTP Installation Package     | In-Advance DL  |
| 59.     |       |         |         | ntp-4.2.6p5-22.el7.centos.x86_64.rpm | NTP Installation Package     | In-Advance DL  |
| 60.     |       |         |postgresql.v9.3.13 | postgresql93-9.3.13-1PGDG.rhel7.x86_64.rpm | PostgreSQL Installation Package | In-Advance DL  |
| 61.     |       |         |         | postgresql93-contrib-9.3.13-1PGDG.rhel7.x86_64.rpm | PostgreSQL Installation Package | In-Advance DL  |
| 62.     |       |         |         | postgresql93-devel-9.3.13-1PGDG.rhel7.x86_64.rpm | PostgreSQL Installation Package | In-Advance DL  |
| 63.     |       |         |         | postgresql93-libs-9.3.13-1PGDG.rhel7.x86_64.rpm | PostgreSQL Installation Package | In-Advance DL  |
| 64.     |       |         |         | postgresql93-server-9.3.13-1PGDG.rhel7.x86_64.rpm | PostgreSQL Installation Package | In-Advance DL  |
| 65.     |       |         |         | uuid-1.6.2-26.el7.x86_64.rpm | PostgreSQL Installation Package | In-Advance DL  |
| 66.     |       |         |         | libxslt-1.1.28-5.el7.x86_64.rpm | PostgreSQL Installation Package | In-Advance DL  |
| 67.     |       |         | snmptrapd.v5.7.2 | perl-HTTP-Tiny-0.033-3.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 68.     |       |         |         | perl-Pod-Perldoc-3.20-4.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 69.     |       |         |         | perl-podlators-2.5.1-3.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 70.     |       |         |         | perl-Encode-2.51-7.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 71.     |       |         |         | perl-Text-ParseWords-3.29-4.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 72.     |       |         |         | perl-Pod-Usage-1.63-3.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 73.     |       |         |         | perl-constant-1.27-2.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 74.     |       |         |         | perl-Time-Local-1.2300-2.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 75.     |       |         |         | perl-Storable-2.45-3.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 76.     |       |         |         | perl-Socket-2.010-3.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 77.     |       |         |         | perl-Scalar-List-Utils-1.27-248.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 78.     |       |         |         | perl-File-Temp-0.23.01-3.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 79.     |       |         |         | perl-Getopt-Long-2.40-2.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 80.     |       |         |         | perl-File-Path-2.09-2.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 81.     |       |         |         | perl-Exporter-5.68-3.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 82.     |       |         |         | perl-Carp-1.26-244.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 83.     |       |         |         | perl-PathTools-3.40-5.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 84.     |       |         |         | perl-Pod-Escapes-1.04-286.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 85.     |       |         |         | perl-macros-5.16.3-286.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 86.     |       |         |         | perl-threads-shared-1.43-6.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 87.     |       |         |         | perl-threads-1.87-4.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 88.     |       |         |         | perl-Time-HiRes-1.9725-3.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 89.     |       |         |         | perl-Pod-Simple-3.28-4.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 90.     |       |         |         | perl-Filter-1.49-3.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 91.     |       |         |         | perl-parent-0.225-244.el7.noarch.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 92.     |       |         |         | net-snmp-agent-libs-5.7.2-24.el7_2.1.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 93.     |       |         |         | net-snmp-libs-5.7.2-24.el7_2.1.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 94.     |       |         |         | perl-5.16.3-286.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 95.     |       |         |         | perl-libs-5.16.3-286.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 96.     |       |         |         | perl-Data-Dumper-2.145-3.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 97.     |       |         |         | net-snmp-5.7.2-24.el7_2.1.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 98.     |       |         |         | openssl098e-0.9.8e-29.el7.centos.3.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 99.     |       |         |         | glibc-2.17-105.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 100.    |       |         |         | glibc-common-2.17-105.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 101.    |       |         |         | lm_sensors-libs-3.3.4-11.el7.x86_64.rpm | Net-SNMP Installation Package | In-Advance DL  |
| 102.    |       |         | java.v8u92 | jdk-8u92-linux-x64.rpm | Java Installation Package    | In-Advance DL  |
| 103.    |       |         | pacemaker.v1.1.14-1.1 | pacemaker-1.1.14-1.el7.x86_64.rpm | Pacemaker Installation Package | In-Advance DL  |
| 104.    |       |         |         | corosync-2.3.5-1.el7.x86_64.rpm | Corosync  Installation Package | In-Advance DL  |
| 105.    |       |         |         | crmsh-2.1.5-1.el7.x86_64.rpm | Crm Command Installation Package     | In-Advance DL  |
| 106.    |       |         |         | cluster-glue-1.0.12-2.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 107.    |       |         |         | cluster-glue-libs-1.0.12-2.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 108.    |       |         |         | corosynclib-2.3.5-1.el7.x86_64.rpm | Corosync Dependent Package | In-Advance DL  |
| 109.    |       |         |         | ipmitool-1.8.13-9.el7_2.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 110.    |       |         |         | libqb-1.0-1.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 111.    |       |         |         | libtool-ltdl-2.4.2-21.el7_2.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 112.    |       |         |         | libxslt-1.1.28-5.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 113.    |       |         |         | libyaml-0.1.4-11.el7_0.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 114.    |       |         |         | lm_sensors-libs-3.3.4-11.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 115.    |       |         |         | nano-2.3.1-10.el7.x86_64.rpm | crm Dependent Package | In-Advance DL  |
| 116.    |       |         |         | net-snmp-agent-libs-5.7.2-24.el7_2.1.x86_64.rpm | Corosync Dependent Package | In-Advance DL  |
| 117.    |       |         |         | net-snmp-libs-5.7.2-24.el7_2.1.x86_64.rpm | Corosync Dependent Package | In-Advance DL  |
| 118.    |       |         |         | openhpi-libs-3.4.0-2.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 119.    |       |         |         | OpenIPMI-libs-2.0.19-11.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 120.    |       |         |         | OpenIPMI-modalias-2.0.19-11.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 121.    |       |         |         | pacemaker-cli-1.1.14-1.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 122.    |       |         |         | pacemaker-cluster-libs-1.1.14-1.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 123.    |       |         |         | pacemaker-libs-1.1.14-1.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 124.    |       |         |         | pacemaker-all-1.1.14-1.1.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 125.    |       |         |         | perl-5.16.3-286.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 126.    |       |         |         | perl-Carp-1.26-244.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 127.    |       |         |         | perl-constant-1.27-2.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 128.    |       |         |         | perl-Encode-2.51-7.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 129.    |       |         |         | perl-Exporter-5.68-3.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 130.    |       |         |         | perl-File-Path-2.09-2.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 131.    |       |         |         | perl-File-Temp-0.23.01-3.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 132.    |       |         |         | perl-Filter-1.49-3.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 133.    |       |         |         | perl-Getopt-Long-2.40-2.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 134.    |       |         |         | perl-HTTP-Tiny-0.033-3.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 135.    |       |         |         | perl-libs-5.16.3-286.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 136.    |       |         |         | perl-macros-5.16.3-286.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 137.    |       |         |         | perl-parent-0.225-244.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 138.    |       |         |         | perl-PathTools-3.40-5.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 139.    |       |         |         | perl-Pod-Escapes-1.04-286.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 140.    |       |         |         | perl-podlators-2.5.1-3.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 141.    |       |         |         | perl-Pod-Perldoc-3.20-4.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 142.    |       |         |         | perl-Pod-Simple-3.28-4.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 143.    |       |         |         | perl-Pod-Usage-1.63-3.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 144.    |       |         |         | perl-Scalar-List-Utils-1.27-248.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 145.    |       |         |         | perl-Socket-2.010-3.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 146.    |       |         |         | perl-Storable-2.45-3.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 147.    |       |         |         | perl-Text-ParseWords-3.29-4.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 148.    |       |         |         | perl-threads-1.87-4.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 149.    |       |         |         | perl-threads-shared-1.43-6.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 150.    |       |         |         | perl-TimeDate-2.30-2.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 151.    |       |         |         | perl-Time-HiRes-1.9725-3.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 152.    |       |         |         | perl-Time-Local-1.2300-2.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 153.    |       |         |         | pm_crmgen-2.1-1.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 154.    |       |         |         | pm_diskd-2.2-1.el7.x86_64.rpm | Diskd RA Package   | In-Advance DL  |
| 155.    |       |         |         | pm_extras-2.2-1.el7.x86_64.rpm | VIPCheck RA Packge | In-Advance DL  |
| 156.    |       |         |         | pm_logconv-cs-2.2-1.el7.noarch.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 157.    |       |         |         | psmisc-22.20-9.el7.x86_64.rpm | Pacemaker Dependent Package | In-Advance DL  |
| 158.    |       |         |         | pssh-2.3.1-5.el7.noarch.rpm | crm Dependent Package     | In-Advance DL  |
| 159.    |       |         |         | resource-agents-3.9.7-1.2.6f56.el7.x86_64.rpm | Standard RA Package Including Virtual IPRA | In-Advance DL  |
| 160.    |       |         |         | python-clufter-0.50.4-1.el7.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 161.    |       |         |         | python-dateutil-1.5-7.el7.noarch.rpm | pcs Dependent Package | In-Advance DL  |
| 162.    |       |         |         | python-lxml-3.2.1-4.el7.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 163.    |       |         |         | resource-agents-3.9.7-1.2.6f56.el7.x86_64.rpm | Standard RA Package Including Virtual IPRA | In-Advance DL  |
| 164.    |       |         |         | ruby-irb-2.0.0.598-25.el7_1.noarch.rpm | pcs Dependent Package | In-Advance DL  |
| 165.    |       |         |         | ruby-libs-2.0.0.598-25.el7_1.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 166.    |       |         |         | rubygem-bigdecimal-1.2.0-25.el7_1.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 167.    |       |         |         | rubygem-io-console-0.4.2-25.el7_1.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 168.    |       |         |         | rubygem-json-1.7.7-25.el7_1.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 169.    |       |         |         | rubygem-psych-2.0.0-25.el7_1.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 170.    |       |         |         | rubygem-rdoc-4.0.0-25.el7_1.noarch.rpm | pcs Dependent Package | In-Advance DL  |
| 171.    |       |         |         | rubygems-2.0.14-25.el7_1.noarch.rpm | pcs Dependent Package | In-Advance DL  |
| 172.    |       |         | httpd.v2.4.6 | apr-1.4.8-3.el7.x86_64.rpm | httpd Installation Package | In-Advance DL  |
| 173.    |       |         |         | apr-util-1.5.2-6.el7.x86_64.rpm | httpd Installation Package | In-Advance DL  |
| 174.    |       |         |         | mailcap-2.1.41-2.el7.noarch.rpm | httpd Installation Package | In-Advance DL  |
| 175.    |       |         |         | httpd-tools-2.4.6-40.el7.centos.4.x86_64.rpm | httpd Installation Package | In-Advance DL  |
| 176.    |       |         |         | httpd-2.4.6-40.el7.centos.4.x86_64.rpm | httpd Installation Package | In-Advance DL  |
| 177.    |       |         |         | httpd-manual-2.4.6-40.el7.centos.4.noarch.rpm | httpd Installation Package | In-Advance DL  |
| 178.    |       |         |         | mod_ssl-2.4.6-40.el7.centos.4.x86_64.rpm | httpd Installation Package | In-Advance DL  |
| 179.    |       |         | sysstat-11.6.0-1 | sysstat-11.6.0-1.x86_64.rpm | Sysstat Installation Package | In-Advance DL  |
| 180.    |       |         | tftp.v5.2 | xinetd-2.3.15-12.el7.x86_64.rpm | tftpd Installation Package   | In-Advance DL  |
| 181.    |       |         |         | tftp-5.2-12.el7.x86_64.rpm. | tftpd Installation Package   | In-Advance DL  |
| 182.    |       |         |         | tftp-server-5.2-12.el7.x86_64.rpm | tftpd Installation Package   | In-Advance DL  |
| 183.    |       | database |         | \-      | \-      | \-      |
| 184.    |       |         |         | create_table.sql | Table Creation Script   | In-Advance DL from GitHub  |
| 185.    |       | RA      |         | \-      | \-      | \-      |
| 186.    |       |         |         | pm_crmgen_env.xls | Resource Agent Configuration File | In-Advance DL  |
| 187.    |       |         |         | ec      | Resource Agent | In-Advance DL from GitHub  |
| 188.    |       |         |         | snmptrapd | Resource Agent | In-Advance DL from GitHub  |
| 189.    | Ansible |         |         |           |               |         |
| 190.    |       |         |         | ansible-2.4.2.0-2.el7.noarch.rpm | Ansible Installation Package   | In-Advance DL  |
| 191.    |       |         |         | PyYAML-3.10-11.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 192.    |       |         |         | libyaml-0.1.4-11.el7_0.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 193.    |       |         |         | openssl-1.0.2k-8.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 194.    |       |         |         | openssl-libs-1.0.2k-8.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 195.    |       |         |         | python-babel-0.9.6-8.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 196.    |       |         |         | python-backports-1.0-8.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 197.    |       |         |         | python-backports-ssl_match_hostname-3.4.0.2-4.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 198.    |       |         |         | python-cffi-1.6.0-5.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 199.    |       |         |         | python-enum34-1.0.4-1.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 200.    |       |         |         | python-httplib2-0.9.2-1.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 201.    |       |         |         | python-idna-2.4-1.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 202.    |       |         |         | python-ipaddress-1.0.16-2.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 203.    |       |         |         | python-jinja2-2.7.2-2.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 204.    |       |         |         | python-markupsafe-0.11-10.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 205.    |       |         |         | python-paramiko-2.1.1-2.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 206.    |       |         |         | python-passlib-1.6.5-2.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 207.    |       |         |         | python-ply-3.4-11.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 208.    |       |         |         | python-python-pycparser-2.14-1.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 209.    |       |         |         | python-setuptools-0.9.8-7.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 210.    |       |         |         | python-six-1.9.0-2.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 211.    |       |         |         | python2-cryptography-1.7.2-1.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |
| 212.    |       |         |         | python2-jmespath-0.9.0-3.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 213.    |       |         |         | python2-pyasn1-0.1.9-7.el7.noarch.rpm | Ansible Dependent Package   | In-Advance DL  |
| 214.    |       |         |         | sshpass-1.06-2.el7.x86_64.rpm | Ansible Dependent Package   | In-Advance DL  |

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
If you install this application by the internet using a proxy server,
you need to confirm that target servers are able to accsess the internet by https and http protcol.
Also assume that wget has already been installed as a package.


## 3. Installation of Controller Server

The instructions described in this section must be performed by the root
user unless any specific user is specified.

**&lt;Execution Host: ACT/SBY/DB&gt;**

Create a working folder where the files generated in the process of
installation are located.

(It will be deleted when the installation of Controller Server is
completed.)

**\[mkdir \~/setup\] \[Enter\]**

> Locate the ec\_main folder which is configured as described in \"1.5
> Configuration of the Included Accessories \" above in the working
> folder.

3.1. Ansible Installation
--------------------------

**&lt;Execution Host: Ansible&gt;**
Before Installation, place the rpm files in Ansible installation destination server.
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

($REMOTE_IP: IP of the install target server)

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command on each server.

**\[cd /root/.ssh/\] \[Enter\]**

**\[touch authorized_keys\] \[Enter\]**

**\[chmod 600 authorized_keys\] \[Enter\]**

**\[cat ~/id_rsa.pub >> authorized_keys\] \[Enter\]**

**\[rm ~/id_rsa.pub\] \[Enter\]**


### 3.2.1.2. Deploy Instllation Files

**&lt;Execution Host: Ansible&gt;**

Before Installation, place the playbook file in Ansible installation destination server.
(Locations: /root/setup/playbook/)

If you do not use the Internet connection, place rpm files(Table 1-1 #56-182) for install.
(Locations: same as "rpm_path" (Table 3-1))

However, you need to manually DL java rpm file and place it, even if you have an Internet connection.


#### 3.2.1.3. Edit playbook
Write the IP information of EC and DB to the host file which use by playbook.
Also, put the yml files which has environmental information for each server to the vars folder.


##### 3.2.1.3.1. Edit host file
Write the DB server name in \[DB:children\].

Write the EC server name in \[EC:children\].

And linking IP for above server name.

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
The meaning of each parameter is described below.

Table 3-1 vars Parameter

| name                | Description                          | EC | DB |
|---------------------|--------------------------------------|----|----|
| rpm_path            | In the case of using network connection, the path of rpm file location. <br> In the case of no network connection, it is rpm files path in the Ansible server. (default:/root/setup/ec_main)| ○ | ○ |
| ec_path             | In the case of using network connection, the path of deploying the EC installation file location. <br> In the case of no network connection, it is EC installation files path (Including DB scripts). (default:/usr/ec_main)| ○ | ○ |
| download_flag       | Parameter for determining whether to acquire a file from the Internet or place it on an Ansible server. <br> If True, use the Internet connection. | ○ | ○ |
| log_path            | Relative path of EC log file output destination. <br> (default:logs/ec/log) | ○ | × |
|installing_ec_path|The path of EC installing files(RA, msf, lib, database, conf, bin, jar_create.sh) (default:/root/setup/ec_main)|||
| db_address          | DB server IP address.                | ○ | ○ |
| db_name             | DB name.                             | ○ | ○ |
| ec_physical_address | EC physical IP address.              | ○ | × |
| ec_rest_address     | EC REST IP address.                  | ○ | × |
| ec_rest_port        | EC REST port.                        | ○ | × |
| em_netconf_address  | EM netconf IP address.               | ○ | × |
| em_netconf_port     | EM netconf port.                     | ○ | × |
| em_rest_address     | EM REST IP address.                  | ○ | × |
| em_rest_port        | EM REST port.                        | ○ | × |
| fc_rest_address     | FC REST IP address.                  | ○ | × |
| fc_rest_port        | FC REST port.                        | ○ | × |
| device_cidr         | The network address of the device that allows reception by Rsyslog. (CIDR) | ○ | × |
| dhcp_if_name        | The IF name (device name) of EC to be used when starting Dhcp server. | ○ | × |
| controller_cidr     | The network name of the server that the DB allows for connections. (CIDR) | ○ | ○ |
| ntp_server_address  | NTP server address.                  | ○ | × |
| ha_flag             | Flag indicating whether to implement redundancy. In the case of truth it is implemented. | ○ | ○ |
| act_address         | IP address for act server. (When ha_flag is False, it is set to none) | ○ | ○ |
| act_node_name       | Name for act server. (When ha_flag is False, it is set to none) | ○ | ○ |
| sby_address         | IP address for stand-by server. (When ha_flag is False, it is set to none) | ○ | ○ |
| sby_node_name       | Name for stand-by server. (When ha_flag is False, it is set to none) | ○ | ○ |
| cluster_name        | Cluster name (When ha_flag is False, it is set to none) | ○ | ○ |
| install_flag        | When setting the DB, it decides whether to install PostgreSQL. | × | ○ |


### 3.2.2. Execute Installation
**&lt;Execution Host: Ansible&gt;**

Execute the following command to install this application.

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

Add the following lines to SNMPTrap service file
(/usr/lib/systemd/system/snmptrapd.service).

**\[vi /usr/lib/systemd/system/snmptrapd.service\] \[Enter\]** (Add the
highlighted section.)

...

\[Service\]

Type=notify

Environment=OPTIONS=\"-Lsd\"

EnvironmentFile=-/etc/sysconfig/snmptrapd

ExecStart=/usr/sbin/snmptrapd `-On` \$OPTIONS -f

ExecReload=/bin/kill -HUP \$MAINPID

...


#### 3.2.3.2. Making of the systemctl Unit File

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to make the systemctl unit file.

**\[cp -p /usr/lib/systemd/system/dhcpd.service /etc/systemd/system/\]
\[Enter\]**

**\[vi /etc/systemd/system/dhcpd.service\] \[Enter\]** (Add the
highlighted section.)


>Type=notify
>
> ExecStart=/usr/sbin/dhcpd -f -cf /etc/dhcp/dhcpd.conf -user dhcpd
> -group dhcpd \--no-pid `"Interface Name"`


Add the following lines to the DHCP configuration file.

**\[vi /etc/sysconfig/dhcpd\] \[Enter\]** (Add the highlighted section.)


> `DHCPDARGS="Interface Name"`


#### 3.2.3.3. Edit of EC configure file
The settings required for the operation (IP,port,file path etc...) are automatically replaced with the parameters at the time of installation.
If you want to change other setting, you need to fix it by hand.

Change the EC Main Module configuration file by use of the following command.

**\[vi $EC_HOME/ec_main/conf/ec_main.conf\] \[Enter\]**

Please refer to "element\_controller\_configuration\_specifications.md" for the details of the change.


#### 3.2.3.4. Making of crm File

**&lt;Execution Host: ACT&gt;**

Edit the pm\_crmgen\_env.xls file, which has the configuration of resource
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

#### 3.2.3.5. Injection of crm File

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

Execute the following command to reboot the NTP.

**\[systemctl restart ntpd.service\] \[Enter\]**

**\[systemctl enable ntpd.service\] \[Enter\]**

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

> Execute the following command to confirm the status of inter-node
> communication by use of \"corosync-cfgtool -s\" command.
>
> This task must be performed both at the active and the stand-by nodes.
>
> **\[corosync-cfgtool -s\] \[Enter\]**
>
> If the cluster is started successfully, the following message will be
> displayed in the screen.
>
> When the \"status\" is \"active\" and \"no faults\", the communication
> is working properly.
>
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
