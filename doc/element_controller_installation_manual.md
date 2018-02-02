# Element Controller Installation Manual

**Version 1.0**
**December 26, 2017**
**Copyright(c) 2017 Nippon Telegraph and Telephone Corporation**

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
prepare them prior to implementing the installation in this document.

Table 1-1 Included
Accessories

| \#      | Folder Structure | | | File    | Description | Remarks |
|---------|---------|---------|---------|---------|---------|---------|
| 1.      | ec_main | \-      | \-      | \-      | \-        | \-        |
| 2.      |         | bin     |         | boot.sh | Inter-devices I/F Control Functinal Part <br> Device Start-up Notification Script |         |
| 3.      |         |         |         | ec\_ctl.sh | EC Start-up Script |         |
| 4.      |         |         |         | linkdown.sh | SNMPTrap Functional Part Link-down <br> Notification Script |         |
| 5.      |         |         |         | linkup.sh | SNMPTrap Functional Part Link-up <br> Notification Script |         |
| 6.      |         |         |         | controller\_status.sh | Controler Status Information Acquisition Script |         |
| 7.      |         | lib     |         | EcMainModule.jar | EC Main Module |         |
| 8.      |         |         |         | NetConf.jar | NetConf Library |         |
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
| 52.     |       | conf    |         | ec_main.conf | EC Main Module Configuration File |         |
| 53.     |       |         |         | hibernate.cfg.xml | Hibernate Configuration File |         |
| 54.     |       |         |         | log4j2.xml | log4j2 Configuration File  |         |
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
| 160.    |       |         |         | pacemaker_install.sh | Package Installer |         |
| 161.    |       |         |         | python-clufter-0.50.4-1.el7.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 162.    |       |         |         | python-dateutil-1.5-7.el7.noarch.rpm | pcs Dependent Package | In-Advance DL  |
| 163.    |       |         |         | python-lxml-3.2.1-4.el7.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 164.    |       |         |         | resource-agents-3.9.7-1.2.6f56.el7.x86_64.rpm | Standard RA Package Including Virtual IPRA | In-Advance DL  |
| 165.    |       |         |         | ruby-irb-2.0.0.598-25.el7_1.noarch.rpm | pcs Dependent Package | In-Advance DL  |
| 166.    |       |         |         | ruby-libs-2.0.0.598-25.el7_1.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 167.    |       |         |         | rubygem-bigdecimal-1.2.0-25.el7_1.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 168.    |       |         |         | rubygem-io-console-0.4.2-25.el7_1.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 169.    |       |         |         | rubygem-json-1.7.7-25.el7_1.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 170.    |       |         |         | rubygem-psych-2.0.0-25.el7_1.x86_64.rpm | pcs Dependent Package | In-Advance DL  |
| 171.    |       |         |         | rubygem-rdoc-4.0.0-25.el7_1.noarch.rpm | pcs Dependent Package | In-Advance DL  |
| 172.    |       |         |         | rubygems-2.0.14-25.el7_1.noarch.rpm | pcs Dependent Package | In-Advance DL  |
| 173.    |       |         | httpd.v2.4.6 | apr-1.4.8-3.el7.x86_64.rpm | httpd Installation Package | In-Advance DL  |
| 174.    |       |         |         | apr-util-1.5.2-6.el7.x86_64.rpm | httpd Installation Package | In-Advance DL  |
| 175.    |       |         |         | mailcap-2.1.41-2.el7.noarch.rpm | httpd Installation Package | In-Advance DL  |
| 176.    |       |         |         | httpd-tools-2.4.6-40.el7.centos.4.x86_64.rpm | httpd Installation Package | In-Advance DL  |
| 177.    |       |         |         | httpd-2.4.6-40.el7.centos.4.x86_64.rpm | httpd Installation Package | In-Advance DL  |
| 178.    |       |         |         | httpd-manual-2.4.6-40.el7.centos.4.noarch.rpm | httpd Installation Package | In-Advance DL  |
| 179.    |       |         |         | mod_ssl-2.4.6-40.el7.centos.4.x86_64.rpm | httpd Installation Package | In-Advance DL  |
| 180.    |       |         | sysstat-11.6.0-1 | sysstat-11.6.0-1.x86_64.rpm | Sysstat Installation Package | In-Advance DL  |
| 181.    |       |         | tftp.v5.2 | xinetd-2.3.15-12.el7.x86_64.rpm | tftpd Installation Package   | In-Advance DL  |
| 182.    |       |         |         | tftp-5.2-12.el7.x86_64.rpm. | tftpd Installation Package   | In-Advance DL  |
| 183.    |       |         |         | tftp-server-5.2-12.el7.x86_64.rpm | tftpd Installation Package   | In-Advance DL  |
| 184.    |       | database |         | \-      | \-      | \-      |
| 185.    |       |         |         | create_table.sql | Table Creation Script   |         |
| 186.    |       | RA      |         | \-      | \-      | \-      |
| 187.    |       |         |         | ra_config.csv | Resource Agent Configuration File |         |
| 188.    |       |         |         | ra_config.xlsx | Resource Agent Configuration File |         |
| 189.    |       |         |         | ec      | Resource Agent |         |
| 190.    |       |         |         | snmptrapd | Resource Agent |         |

## 2. Operational Environment

2.1. EC Main Module Start-up Server of Controller
------------------------------------------------

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

3.1. Environmental Installation
--------------------------

### 3.1.1. Firewall Configuration

#### 3.1.1.1. Firewall Confirmation

**&lt;Execution Host: ACT/SBY/DB&gt;**

> Check if the firewall has been already configured.
>
> **\[firewall-cmd \--state\] \[Enter\]**
>
> If the firewall has already been configured, the following message
> will be displayed in the screen.
>
> running
>
> In this case, keep following the instructions from 3.1.1.2 to 3.1.1.8.
>
> If the firewall is not installed or is not started, the following
> message will be displayed in the screen.
>
> (In case the firewall is not started) not running
>
> (In case the firewall is not installed) bash: firewall-cmd: command
> not found
>
> In these cases, the instructions from 3.1.1.2 to 3.1.1.8 can be
> ignored.

#### 3.1.1.2. Permit the Connection of REST Request (FC, Start-up Notification, Trap Notification)

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to permit the connection from the port
used in REST Request.

In case installing two or more ECs in a same server, set a different
port for every entity in the configuration file and execute the
following command for every port. (Replace the highlighted section with
the appropriate port number.)

(The specified port numbers in this document are default values. You can
change the numbers in accordance with your configuration.)

**\[firewall-cmd \--permanent \--add-port=18080/tcp\] \[Enter\]**

#### 3.1.1.3. Permit the Connection of PostgreSQL

**&lt;Execution Host: DB&gt;**

Execute the following command to permit the connection from the port
used in PostgreSQL.

**\[firewall-cmd \--permanent \--add-port=5432/tcp\] \[Enter\]**

#### 3.1.1.4. Permit the Connection of SNMPTrap

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to permit the connection from the port
used in SNMPTrap.

**\[firewall-cmd \--permanent \--add-port=162/udp\] \[Enter\]**

#### 3.1.1.5. Permit syslog Transmission Reception

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to permit the connection from the port
used in syslog Transmission Reception.

**\[firewall-cmd \--permanent \--add-port=514/tcp\] \[Enter\]**

**\[firewall-cmd \--permanent \--add-port=514/udp\] \[Enter\]**

#### 3.1.1.6. Permit the Connection of Pacemaker/Corosync

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to permit the connection from the port
used in Pacemaker/Corosync.

**\[firewall-cmd \--permanent \--add-service=high-availability\]
\[Enter\]**

**\[cp -p /usr/lib/firewalld/services/high-availability.xml
/etc/firewalld/services/\] \[Enter\]**

#### 3.1.1.7. Permit the Connection of tftpd

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to permit the connection from the port
used in tftpd.

**\[firewall-cmd \--permanent \--add-service=tftp\] \[Enter\]**

**\[cp -p /usr/lib/firewalld/services/tftp.xml
/etc/firewalld/services/\] \[Enter\]**

#### 3.1.1.8. Permit the Connection of httpd

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to permit the connection from the port
used in httpd.

**\[firewall-cmd \--permanent \--add-service=http\] \[Enter\]**

**\[cp -p /usr/lib/firewalld/services/http.xml
/etc/firewalld/services/\] \[Enter\]**

> Once having completed the 3.1.1 Firewall Configuration above, execute
> the following command to reflect the configuration.

**&lt;Execution Host: ACT/SBY/DB&gt;**

**\[firewall-cmd \--reload\] \[Enter\]**

**\[systemctl restart firewalld\] \[Enter\]**

Confirm the current configuration by executing the following command
(especially for the highlighted section).

**\[firewall-cmd \--list-all\] \[Enter\]**

**&lt;Execution Host: ACT/SBY&gt;**

public (default, active)

interfaces:

sources:

services: dhcpv6-client high-availability http ssh tftp

ports: 514/tcp 162/udp 514/udp 18080/tcp

masquerade: no

forward-ports:

icmp-blocks:

rich rules:

**&lt;Execution Host: DB&gt;**

public (default, active)

interfaces:

sources:

services: dhcpv6-client ssh

ports: 5432/tcp

masquerade: no

forward-ports:

icmp-blocks:

rich rules:

#### 3.1.1.9. Configuration of SELinux

**&lt;Execution Host: ACT/SBY&gt;**

Confirm the current configuration with the following command.

**\[getenforce\] \[Enter\]**

If the result of execution of above command is "Enfoing", execute the
following command to change the SELinux configuration.

**\[setenforce 0\] \[Enter\]**

**\[vi /etc/selinux/config\] \[Enter\]** (set the highlighted value as
shown below）

...

\# This file controls the state of SELinux on the system.

\# SELINUX= can take one of these three values:

\# enforcing - SELinux security policy is enforced.

\# permissive - SELinux prints warnings instead of enforcing.

\# disabled - No SELinux policy is loaded.

SELINUX=disabled

...

### 3.1.2. Installation and Configuration of Java

#### 3.1.2.1. Installation

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to install jdk.

**\[cd \~/setup/ec\_main/installer/java.v8u92\] \[Enter\]**

**\[rpm -ivh jdk-8u92-linux-x64.rpm\] \[Enter\]**

#### 3.1.2.2. Confirmation of the version of Java

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to confirm the version.

**\[java -version\] \[Enter\]**

If it has been installed successfully, the following message will be
displayed.

java version \"1.8.0\_92\"

Java(TM) SE Runtime Environment (build 1.8.0\_92-b14)

Java HotSpot(TM) 64-Bit Server VM (build 25.92-b14, mixed mode)

#### 3.1.2.3. Change of the profile

**&lt;Execution Host: ACT/SBY&gt;**

Add JRE\_HOME environment variable to the following file (append the
highlighted section).

**\[vi /etc/profile\] \[Enter\]**

...

JRE\_HOME=/usr/java/jdk1.8.0\_92/

export JRE\_HOME

### 3.1.3. Installation and Configuration of Net-SNMP

#### 3.1.3.1. Installation

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to install Net-SNMP.

**\[cd \~/setup/ec\_main/installer/snmptrapd.v5.7.2\] \[Enter\]**

**\[rpm -ivh glibc-common-2.17-105.el7.x86\_64.rpm\] \[Enter\]**

**\[rpm -ivh glibc-2.17-105.el7.x86\_64.rpm\] \[Enter\]**

**\[rpm -ivh perl-\*\] \[Enter\]**

**\[rpm -ivh openssl098e-0.9.8e-29.el7.centos.3.x86\_64.rpm\]
\[Enter\]**

**\[rpm -ivh net-snmp-libs-5.7.2-24.el7\_2.1.x86\_64.rpm\] \[Enter\]**

**\[rpm -ivh lm\_sensors-libs-3.3.4-11.el7.x86\_64.rpm\] \[Enter\]**

**\[rpm -ivh net-snmp-agent-libs-5.7.2-24.el7\_2.1.x86\_64.rpm\]
\[Enter\]**

**\[rpm -ivh net-snmp-5.7.2-24.el7\_2.1.x86\_64.rpm\] \[Enter\]**

\*The output result saying that it has already been installed should be
taken as no problem

#### 3.1.3.2. Change of SNMPTrap Configuration File

**&lt;Execution Host: ACT/SBY&gt;**

> Add the following lines to the SNMPTrap configuration file
> (/etc/snmp/snmptrapd.conf) (append the highlighted section).

**(The "\$EC\_HOME/" below must be replaced with the same path as
"3.2.3Locating the Script".)**

**\[vi /etc/snmp/snmptrapd.conf\] \[Enter\]**

...

authCommunity log,execute "Community Name"

\#trap reception of Linkdown

traphandle .1.3.6.1.6.3.1.1.5.3 *\$EC\_HOME*/ec\_main/bin/linkdown.sh

\#trap reception of Linkup

traphandle .1.3.6.1.6.3.1.1.5.4 *\$EC\_HOME*/ec\_main/bin/linkup.sh

Add the following lines to SNMPTrap service file
(/usr/lib/systemd/system/snmptrapd.service).

**\[vi /usr/lib/systemd/system/snmptrapd.service\] \[Enter\]** (Add the
highlighted section.)

...

\[Service\]

Type=notify

Environment=OPTIONS=\"-Lsd\"

EnvironmentFile=-/etc/sysconfig/snmptrapd

ExecStart=/usr/sbin/snmptrapd -On \$OPTIONS -f

ExecReload=/bin/kill -HUP \$MAINPID

...

### 3.1.4. Configuration of rsyslog

#### 3.1.4.1. Change of rsyslog Configuration File

**&lt;Execution Host: ACT/SBY&gt;**

Change the contents of rsyslog configuration file (/etc/rsyslog.conf) as
below (change the highlighted section as specified below).

The "Device CIDR" below will be replaced with the CIDR expression of the
device's segment.

**\[vi /etc/rsyslog.conf\] \[Enter\]**

Before Change

...

\# Provides UDP syslog reception

\#\$ModLoad imudp

\#\$UDPServerRun 514

\# Provides TCP syslog reception

\#\$ModLoad imtcp

\#\$InputTCPServerRun 514

After Change

...

\# Provides UDP syslog reception

\$ModLoad imudp

\$UDPServerRun 514

\$AllowedSender UDP, 127.0.0.1, *Device CIDR*

\# Provides TCP syslog reception

\$ModLoad imtcp

\$InputTCPServerRun 514

\$AllowedSender TCP, 127.0.0.1, *Device CIDR*

...

Append the following line to the end of the file.

\$template hostip, \"%fromhost-ip%\"

### 3.1.5. Installation and Configuration of NTP

#### 3.1.5.1. Installation

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to install ntp.

**\[cd \~/setup/ec\_main/installer/ntp.v4.2/\] \[Enter\]**

**\[rpm -ivh autogen-libopts-5.18-5.el7.x86\_64.rpm\] \[Enter\]**

**\[rpm -ivh ntpdate-4.2.6p5-22.el7.centos.x86\_64.rpm\] \[Enter\]**

**\[rpm -ivh ntp-4.2.6p5-22.el7.centos.x86\_64.rpm\] \[Enter\]**

#### 3.1.5.2. Making of drift File

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to make a blank drift file.

**\[touch /var/lib/ntp/drift\] \[Enter\]**

#### 3.1.5.3. Change of NTP Configuration File

**&lt;Execution Host: ACT/SBY&gt;**

Add the following lines (the highlighted section) to the NTP
configuration file (/etc/ntp.conf).

**\[vi /etc/ntp.conf\] \[Enter\]**

...

restrict default nomodify notrap nopeer noquery

restrict default ignore

...

\#restrict 192.168.1.0 mask 255.255.255.0 nomodify notrap

restrict xxx.xxx.xxx.xxx noquery nomodify

server xxx.xxx.xxx.xxx iburst

...

#### 3.1.5.4. Synchronization with the NTP Server

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to confirm that NTP is not running.

**\[systemctl status ntpd.service\] \[Enter\]**

&lt;The output when NTP is not running &gt;

...

Active: inactive (dead)

...

&lt;The output when NTP is running &gt;

...

Active: active (running)

...

In case NTP is running, execute the following command to stop NTP.

**\[systemctl stop ntpd.service\] \[Enter\]**

Then synchronize the time with NTP server (IP address: xxx.xxx.xxx.xxx).

**\[ntpdate xxx.xxx.xxx.xxx\] \[Enter\]**

#### 3.1.5.5. Rebooting the NTP

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

### 3.1.6. Installation and Configuration of PostgreSQL

#### 3.1.6.1. Installation

**&lt;Execution Host: DB&gt;**

Execute the following command to install postgresql.

**\[cd \~/setup/ec\_main/installer/postgresql.v9.3.13\] \[Enter\]**

**\[rpm -ivh libxslt-1.1.28-5.el7.x86\_64.rpm\] \[Enter\] **

**\[rpm -ivh uuid-1.6.2-26.el7.x86\_64.rpm\] \[Enter\]**

**\[rpm -ivh postgresql93-libs-9.3.13-1PGDG.rhel7.x86\_64.rpm\]
\[Enter\]**

**\[rpm -ivh postgresql93-9.3.13-1PGDG.rhel7.x86\_64.rpm\] \[Enter\]**

**\[rpm -ivh postgresql93-server-9.3.13-1PGDG.rhel7.x86\_64.rpm\]
\[Enter\]**

**\[rpm -ivh postgresql93-devel-9.3.13-1PGDG.rhel7.x86\_64.rpm\]
\[Enter\]**

**\[rpm -ivh postgresql93-contrib-9.3.13-1PGDG.rhel7.x86\_64.rpm\]
\[Enter\]**

#### 3.1.6.2. Change of the PostgreSQL Configuration

**&lt;Execution Host: DB&gt;**

Change the configuration as follows.

**\[vi /var/lib/pgsql/.bash\_profile\] \[Enter\]** (Change or add the
highlighted section.)

PGDATA=/usr/local/pgsql/9.3/data

export PGDATA

export PATH=\$PATH:/usr/pgsql-9.3/bin

**\[source /var/lib/pgsql/.bash\_profile\] \[Enter\]**

#### 3.1.6.3. Making of Data Base and Granting Permissions

**&lt;Execution Host: DB&gt;**

Execute the following command to make the folder where the data base
will be installed.

**\[cd /usr/local/\] \[Enter\]**

**\[mkdir -pm 777 /usr/local/pgsql/9.3\] \[Enter\]**

**\[chown -R postgres:postgres pgsql\] \[Enter\]**

Execute the following command as a postgres user to make the data base.

**\[su - postgres\] \[Enter\]**

**\[cd /usr/local/pgsql/9.3/\] \[Enter\]**

**\[mkdir -m 700 data\] \[Enter\]**

**\[initdb \--encoding=UTF8 \--no-locale
\--pgdata=/usr/local/pgsql/9.3/data \--auth=ident\] \[Enter\]**

**\[pg\_ctl -D /usr/local/pgsql/9.3/data -l logfile start\] \[Enter\]**

**\[psql -c \"alter user postgres with password \'\'\"\] \[Enter\]**

**\[psql\] \[Enter\]**

**\[create role root login createdb password \'\'; \] \[Enter\]**

**\[\\q\] \[Enter\]**

**\[pg\_ctl -D /usr/local/pgsql/9.3/data -l logfile stop\] \[Enter\]**

**\[exit\] \[Enter\]**

Execute the following command as the root user.

**\[systemctl enable postgresql-9.3\] \[Enter\]**

**\[systemctl daemon-reload\] \[Enter\]**

#### 3.1.6.4. Change of the Data Base Configuration

**&lt;Execution Host: DB&gt;**

Change the configuration as follows.

**\[vi /usr/local/pgsql/9.3/data/postgresql.conf\] \[Enter\]**

Before Change

...

\#listen\_addresses = \'localhost\'

\#port = 5432

...

After Change

...

listen\_addresses = \'\*\'

port = 5432

...

Change the configuration as follows (the highlighted section should be
replaced with the segment of the server).

**\[vi /usr/local/pgsql/9.3/data/pg\_hba.conf\] \[Enter\]**

Before Change

\...

\# TYPE DATABASE USER ADDRESS METHOD

\# \"local\" is for Unix domain socket connections only

local all all peer

\# IPv4 local connections:

host all all 127.0.0.1/32 ident

\# IPv6 local connections:

host all all ::1/128 ident

\# Allow replication connections from localhost, by a user with the

\# replication privilege.

\#local replication postgres peer

\#host replication postgres 127.0.0.1/32 ident

\#host replication postgres ::1/128 ident

After Change

...

\# TYPE DATABASE USER ADDRESS METHOD

\# \"local\" is for Unix domain socket connections only

\#local all all peer

\# IPv4 local connections:

\#host all all 127.0.0.1/32 ident

\# IPv6 local connections:

\#host all all ::1/128 ident

\# Allow replication connections from localhost, by a user with the

\# replication privilege.

\#local replication postgres peer

\#host replication postgres 127.0.0.1/32 ident

\#host replication postgres ::1/128 ident

local all postgres peer

local all all trust

host all all 192.168.53.0/24 trust

host all all 127.0.0.1/32 trust

Change the configuration as follows (modify the highlighted section).

**\[vi /usr/lib/systemd/system/postgresql-9.3.service\] \[Enter\]**

...

\# Location of database directory

Environment=PGDATA=/usr/local/pgsql/9.3/data/

...

#### 3.1.6.5. Rebooting the Data Base

**&lt;Execution Host: DB&gt;**

Execute the following command as a postgres user.

**\[systemctl daemon-reload\] \[Enter\]**

**\[systemctl start postgresql-9.3\] \[Enter\]**

### 3.1.7. Installation and Configuration of DHCP

#### 3.1.7.1. Installation

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to install DHCP.

**\[cd \~/setup/ec\_main/installer/dhcp.v4.2.5\] \[Enter\]**

**\[rpm -ihv dhcp-4.2.5-42.el7.centos.x86\_64.rpm\] \[Enter\]**

#### 3.1.7.2. Making of the systemctl Unit File

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to make the systemctl unit file.

**\[cp -p /usr/lib/systemd/system/dhcpd.service /etc/systemd/system/\]
\[Enter\]**

**\[vi /etc/systemd/system/dhcpd.service\] \[Enter\]** (Add the
highlighted section.)

...

Type=notify

> ExecStart=/usr/sbin/dhcpd -f -cf /etc/dhcp/dhcpd.conf -user dhcpd
> -group dhcpd \--no-pid "Interface Name"

...

Add the following lines to the DHCP configuration file.

**\[vi /etc/sysconfig/dhcpd\] \[Enter\]** (Add the highlighted section.)

...

DHCPDARGS="Interface Name"

**\[vi /etc/dhcp/dhcpd.conf.template.NCS5001\] \[Enter\]** (Add the
highlighted section.)

\# DHCP server general settings

subnet \$\$MANAGEMENTNETWORKADDRESS\$\$ netmask
\$\$MANAGEMENTSUBNETMASK\$\$ {

range \$\$MANAGEMENTRANGESTART\$\$ \$\$MANAGEMENTRANGEEND\$\$;

option subnet-mask \$\$MANAGEMENTSUBNETMASK\$\$;

default-lease-time 600;

deny unknown-clients;

max-lease-time 7200;

}

\#\#\#\#\#\#\#\#\#\#

\# host

\#\#\#\#\#\#\#\#\#\#

\# NCS5001

group {

filename \"\$\$INITIALCONFIG\$\$\";

host ncs5k {

hardware ethernet \$\$MACADDRESS\$\$;

fixed-address \$\$MANAGEMENTADDRESS\$\$;

}

}

**\[vi /etc/dhcp/dhcpd.conf.template.QFX5100\] \[Enter\]** (Add the
highlighted section.)

\# For QFX zero touch provisioning

option space QFX;

option QFX.config-file-name code 1 = text;

option QFX.image-file-type code 2 = text;

option QFX.transfer-mode code 3 = text;

option QFX.alt-image-file-name code 4= text;

option QFX-encapsulation code 43 = encapsulate QFX;

\# DHCP server general settings

subnet \$\$MANAGEMENTNETWORKADDRESS\$\$ netmask
\$\$MANAGEMENTSUBNETMASK\$\$ {

range \$\$MANAGEMENTRANGESTART\$\$ \$\$MANAGEMENTRANGEEND\$\$;

option subnet-mask \$\$MANAGEMENTSUBNETMASK\$\$;

default-lease-time 600;

deny unknown-clients;

max-lease-time 7200;

}

\#\#\#\#\#\#\#\#\#\#

\# host

\#\#\#\#\#\#\#\#\#\#

\# QFX5100

host QFX5100 {

hardware ethernet \$\$MACADDRESS\$\$;

fixed-address \$\$MANAGEMENTADDRESS\$\$;

option tftp-server-name \"\$\$TFTPHOSTNAME\$\$\";

option host-name \"\$\$HOSTNAME\$\$\";

option log-servers \$\$LOGSERVERADDRESS\$\$;

option QFX.transfer-mode \"tftp\";

option QFX.config-file-name \"\$\$INITIALCONFIG\$\$\";

}

**\[vi /etc/dhcp/dhcpd.conf.template.QFX5200\] \[Enter\]** (Add the
highlighted section.)

\# For QFX zero touch provisioning

option space QFX;

option QFX.config-file-name code 1 = text;

option QFX.image-file-type code 2 = text;

option QFX.transfer-mode code 3 = text;

option QFX.alt-image-file-name code 4= text;

option QFX-encapsulation code 43 = encapsulate QFX;

\# DHCP server general settings

subnet \$\$MANAGEMENTNETWORKADDRESS\$\$ netmask
\$\$MANAGEMENTSUBNETMASK\$\$ {

range \$\$MANAGEMENTRANGESTART\$\$ \$\$MANAGEMENTRANGEEND\$\$;

option subnet-mask \$\$MANAGEMENTSUBNETMASK\$\$;

default-lease-time 600;

deny unknown-clients;

max-lease-time 7200;

}

\#\#\#\#\#\#\#\#\#\#

\# host

\#\#\#\#\#\#\#\#\#\#

\# QFX5200

host QFX5200-3 {

hardware ethernet \$\$MACADDRESS\$\$;

fixed-address \$\$MANAGEMENTADDRESS\$\$;

option tftp-server-name \"\$\$TFTPHOSTNAME\$\$\";

option host-name \"\$\$HOSTNAME\$\$\";

option log-servers \$\$LOGSERVERADDRESS\$\$;

option QFX.transfer-mode \"http\";

option QFX.config-file-name \"\$\$INITIALCONFIG\$\$\";

}

### 3.1.8. Installation and Configuration of tftpd

#### 3.1.8.1. Installation of tftpd

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to install tftpd.

**\[cd \~/setup/ec\_main/installer/tftp.v5.2\] \[Enter\]**

**\[rpm -ihv xinetd-2.3.15-12.el7.x86\_64.rpm\] \[Enter\]**

**\[rpm -ihv tftp-5.2-12.el7.x86\_64.rpm\] \[Enter\]**

**\[rpm -ihv tftp-server-5.2-12.el7.x86\_64.rpm\] \[Enter\]**

#### 3.1.8.2. Configuration of tftpd

**&lt;Execution Host: ACT/SBY&gt;**

Change the tftp configuration file (/etc/ xinetd.d/tftp) as follows (the
highlighted section).

**\[vi /etc/xinetd.d/tftp\] \[Enter\]**

Before Change

\...

disabled = yes

\...

After Change

\...

disabled = no

\...

Launch xinetd.

**\[systemctl start xinetd\] \[Enter\]**

### 3.1.9. Installation of httpd

#### 3.1.9.1. Installation of httpd

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to install httpd.

**\[cd \~/setup/ec\_main/installer/httpd.v2.4.6\] \[Enter\]**

**\[rpm -ihv apr-1.4.8-3.el7.x86\_64.rpm\] \[Enter\]**

**\[rpm -ihv apr-util-1.5.2-6.el7.x86\_64.rpm\] \[Enter\]**

**\[rpm -ihv mailcap-2.1.41-2.el7.noarch.rpm\] \[Enter\]**

**\[rpm -ihv httpd-tools-2.4.6-40.el7.centos.4.x86\_64.rpm\] \[Enter\]**

**\[rpm -ihv httpd-2.4.6-40.el7.centos.4.x86\_64.rpm\] \[Enter\]**

**\[rpm -ihv httpd-manual-2.4.6-40.el7.centos.4.noarch.rpm\] \[Enter\]**

**\[rpm -ihv mod\_ssl-2.4.6-40.el7.centos.4.x86\_64.rpm\] \[Enter\]**

Launch httpd.

**\[systemctl start httpd\] \[Enter\]**

**\[systemctl enable httpd\] \[Enter\]**

### 3.1.10. Installation and Configuration of Pacemaker

#### 3.1.10.1 Installation of Pacemaker

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to launch the installer and install.

**\[cd \~/setup/ec\_main/installer/pacemaker.v1.1.14-1.1/\] \[Enter\]**

**\[sh pacemaker\_install.sh\] \[Enter\]**

> Note: Although you will be warned that there is no key with the
> message \"Warning: pssh-2.3.1-5.el7.noarch.rpm: header V3 RSA/SHA256
> Signature, Key ID 352c64e5: NOKEY\" displayed in the screen during the
> installation, the installation process keeps going on and you can
> ignore it.

#### 3.1.10.2. Confirmation of Installation of Pacemaker

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

#### 3.1.10.3. Configuration of the Host

**&lt;Execution Host: ACT/SBY&gt;**

> Register the node's hosts used in the active system and the stand-by
> system.
>
> Perform this task both at the active node and the stand-by node.
>
> Execute the following command to open the hosts file.

**\[vi /etc/hosts\] \[Enter\]**

> Add the following lines to the end of the file.
>
> **(IP Address for the Stand-by Interconnection) (Stand-by Host Name)**
>
> **(IP Address for the Active Interconnecction) (Active Host Name)**
>
> Then save it as:
>
> **\[esc\]\[:wq\] \[Enter\]**
>
> Confirm the configuration of the hosts
>
> **\[ping "Stand-by Host Name"\] \[Enter\]**
>
> PING "Stand-by Host Name" (IP address for the stand-by
> interconnection) 56(84) bytes of data.
>
> 64 bytes from "Stand-by Host Name" (IP address for the stand-by
> interconnection): icmp\_seq=1 ttl=64 time=0.166 ms
>
> **\[ping "Active Host Name"\] \[Enter\]**
>
> PING "Active Host Name" (IP address for the active interconnection)
> 56(84) bytes of data.
>
> 64 bytes from "Active Host Name" (IP address for the active
> interconnection): icmp\_seq=1 ttl=64 time=0.166 ms
>
> In case the IP address and the Host Name are not displayed like this,
> review the configuration at /etc/hosts.

#### 3.1.10.4. Configuration of the password

**&lt;Execution Host: ACT/SBY&gt;**

> Execute the following command to create corosync.conf for executing
> pacemaker.

**【vi /etc/corosync/corosync.conf】\[Enter\]**

> The content of the file should be as follows.

totem {

version: 2

secauth: off

cluster\_name: (Cluster Name to be configured)

transport: udpu

}

nodelist {

node {

ring0\_addr: (Active Node Name)

nodeid: 1

}

node {

ring0\_addr: (Stand-by Node Name)

nodeid: 2

}

}

quorum {

provider: corosync\_votequorum

two\_node: 1

}

logging {

to\_logfile: yes

logfile: /var/log/cluster/corosync.log

to\_syslog: yes

}

#### 3.1.10.5. Launch Pacemaker

**&lt;Execution Host: ACT/SBY&gt;**

> Execute the following command to launch pacemaker.
>
> **\[systemctl start pacemaker\]**

#### 3.1.10.6. Confirmation of the Inter-node Communication Status

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

#### 3.1.10.7. Confirmation of Cluster Status

**&lt;Execution Host: ACT or SBY&gt;**

> Execute the following command to confirm the status of the cluster.
>
> This task can be performed at either the Active or the Stand-by
> system.
>
> **\[crm\_mon --fA -1\] \[Enter\]**
>
> After entering the command, the cluster status will be displayed in
> the screen. A warning message about STONITH, which looks like below
> will be also displayed on top of the screen.
>
> Cluster name: (Cluster Name having been set)
>
> WARNING: no stonith devices and stonith-enabled is not false
>
> Last updated: Thu Oct 13 02:06:57 2016 Last change: Thu Oct 13
> 02:06:49 2016
>
> Since STONITH is not available in the environment this time, set
> "false" for the enablement of STONITH.
>
> **\[crm configure property stonith-enabled="false"\] \[Enter\]**
>
> Confirm the cluster status for one last time.
>
> **\[crm\_mon -~-~fA -1\] \[Enter\]**
>
> After entering the command, the cluster status will be displayed in
> the screen. If it is configured properly, the screen message will be
> looked like below.

Last updated: WDW MMM DD HH:MM:DD YYYY Last change: WDW MMM DD HH:MM:DD
YYYY by root via cibadmin on (Active Node Name)

Stack: corosync

Current DC: (Stand-by Node Name) (version 1.1.14-1.el7-70404b0) -
partition with quorum

2 nodes and 0 resources configured

Online: \[(Active Node Name) (Stand-by Node Name)\]

Node Attribute:

\* Node (Active Node Name)

\* Node (Stand-by Node Name)

Migration Summary:

\* Node (Active Node Name)

\* Node (Stand-by Node Name)

#### 3.1.10.8. Enablement of pacemaker/corosync Services

**&lt;Execution Host: ACT/SBY&gt;**

> Execute the following command to enable corosync and pacemaker
> services for enabling the auto start of both services after the
> reboot.
>
> This task must be performed both at the active and the stand-by nodes.
>
> **\[systemctl enable pacemaker\] \[Enter\]**
>
> **\[systemctl enable corosync\] \[Enter\]**

### 3.1.11. Installation of sysstat(sar)

#### 3.1.11.1. Inatallation

**&lt;Execution Host: ACT/SBY&gt;**

> Execute the following command to install sysstat.
>
> **\[cd \~/setup/ec\_main/installer/sysstat-11.6.0-1\] \[Enter\]**
>
> **\[rpm -ihv sysstat-11.6.0-1.x86\_64.rpm\] \[Enter\]**

3.2. Installation of EC Main Module
------------------------------

Hereafter, the written expression \"\$EC\_HOME\" represents any location
path specified by the user.

In the following example, \"/usr\" is specified for the installation
directory.

**\[export EC\_HOME=/usr\] \[Enter\]**

### Locating the Library

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to locate the library file which is in the
included accessories.

**\[mkdir -p \$EC\_HOME/ec\_main/lib\] \[Enter\]**

**\[cp \~/setup/ec\_main/lib/\* \$EC\_HOME/ec\_main/lib/\] \[Enter\]**

### Locating the Configuration File

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to locate the configuration file which is
in the included accessories.

**\[mkdir -p \$EC\_HOME/ec\_main/conf\] \[Enter\]**

**\[cp \~/setup/ec\_main/conf/\* \$EC\_HOME/ec\_main/conf/\] \[Enter\]**

Change the EC Main Module configuration file by use of the following
command.

**\[vi \$EC\_HOME/ec\_main/conf/ec\_main.conf\] \[Enter\]**

\*Please modify configuration values based on your installed server.

Please refer to "EC\_Configuration Specifications.xlsx" for the details
of the change.

Change the Hibernate configuration file by use of the following command.

**\[vi \$EC\_HOME/ec\_main/conf/hibernate.cfg.xml\] \[Enter\]** (Change
the highlighted section.)

\...

&lt;property name=\"connection.url\"&gt;jdbc:postgresql:// "Destination URL
(IP address xxx.xxx.xxx.xxx):(Port Number XXXX)"/msf\_ec&lt;/property&gt;

\...

Change the log4j configuration file by use of the following command.

**\[mkdir -p "Log Output Location"\] \[Enter\]**

**\[vi \$EC\_HOME/ec\_main/conf/log4j2.xml\] \[Enter\]** (Change the
highlighted section.)

\...

&lt;Properties&gt;

&lt;Property name=\"log-path\"&gt;"Log Output Location"&lt;/Property&gt;

&lt;/Properties&gt;

\...

### Locating the Script

**&lt;Execution Host: ACT/SBY&gt;**

Execute the following command to locate the script file in the included
accessories.

**\[mkdir -p \$EC\_HOME/ec\_main/bin\] \[Enter\]**

**\[cp \~/setup/ec\_main/bin/\* \$EC\_HOME/ec\_main/bin/\] \[Enter\]**

**\[chmod 755 \$EC\_HOME/ec\_main/bin/\*\] \[Enter\]**

**\[cd \$EC\_HOME/ec\_main/bin/\] \[Enter\]**

**\[ln -s boot.sh boot\_fail.sh\] \[Enter\]**

**\[ln -s boot.sh boot\_success.sh\] \[Enter\]**

Add the PATH environment variable to the following file.

**\[vi /etc/profile\] \[Enter\]**

\...

export PATH=\$PATH:\$EC\_HOME/ec\_main/bin

Make the environment variable read by use of the following command.

**\[source /etc/profile\] \[Enter\]**

Change the environment definition by use of the following command.

**\[vi \$EC\_HOME/ec\_main/bin/ec\_ctl.sh\] \[Enter\]** (Change the
highlighted section.)

\...

\#\# Environment Definition

HOST=\"xxx.xxx.xxx.xxx\"

PORT=\"yyyyy\"

\...

**\[vi \$EC\_HOME/ec\_main/bin/boot.sh\] \[Enter\]** (Change the
highlighted section.)

\...

\#\# Environment Definition

HOST=\"xxx.xxx.xxx.xxx\"

PORT=\"yyyyy\"

\...

**\[vi \$EC\_HOME/ec\_main/bin/linkup.sh\] \[Enter\]** (Change the
highlighted section.)

\...

\#\# Environment Definition

HOST=\"xxx.xxx.xxx.xxx\"

PORT=\"yyyyy\"

\...

**\[vi \$EC\_HOME/ec\_main/bin/linkdown.sh\] \[Enter\]** (Change the
highlighted section.)

\...

\#\# Environment Definition

HOST=\"xxx.xxx.xxx.xxx\"

PORT=\"yyyyy\"

\...

> The "xxx.xxx.xxx.xxx" above must be replaced with the VIP address of
> the destination server for the installation.
>
> The "yyyyy" above must be replaced with the same value as the
> rest\_server\_port of \$EC\_HOME/ec\_main/conf/ec\_main.conf.

### Making of Schema

**&lt;Execution Host: DB&gt;**

Make the schema by use of the following command.

**\[createdb msf\_ec\] \[Enter\]**

Make a table in the schema by use of the following command.

**\[psql msf\_ec &lt; \~/setup/ec\_main/database/create\_table.sql\]
\[Enter\]**

Registration and Configuration of the Resource Agent
----------------------------------------------------

### Locating the Resource Agent

**&lt;Execution Host: ACT/SBY&gt;**

Copy the resource agent of EC to the default resource agent folder.

> **\[mkdir -p \$EC\_HOME/ec\_main/RA\] \[Enter\]**

**\[cp \~/setup/ec\_main/RA/\* \$EC\_HOME/ec\_main/RA/\] \[Enter\]**

> **\[cp \$EC\_HOME/ec\_main/RA/ec /lib/ocf/resource.d/heartbeat/\]
> \[Enter\]**
>
> **\[cp \$EC\_HOME/ec\_main/RA/snmptrapd
> /lib/ocf/resource.d/heartbeat/\] \[Enter\]**

Then grant executional privilege to the resource agent of EC copied
above.

> **\[cd /lib/ocf/resource.d/heartbeat/\] \[Enter\]**
>
> **\[chmod 755 ec\] \[Enter\]**
>
> **\[chmod 755 snmptrapd\] \[Enter\]**

### Making of crm File

**&lt;Execution Host: ACT&gt;**

Edit the ra\_config.xlsx file, which has the configuration of resource
agent in the included accessories, for updating the necessary
information, then convert it to a csv file and locate in the home folder
of the active system.

Execute the following command at the folder where you locate the csv
file to convert it into a crm file that is used for registering it to
the resource agent.

> **\[pm\_crmgen -o \$EC\_HOME/ec\_main/conf/crm\_conf.crm (located csv
> file name).csv\] \[Enter\]**

If the conversion completes successfully, nothing will be displayed in
the screen but in case anything went wrong with the csv file, the
location to be amended would be displayed.

### Injection of crm File

**&lt;Execution Host: ACT&gt;**

> With the following commend, register the resource agent.
>
> **\[crm configure load update \$EC\_HOME/ec\_main/conf/crm\_conf.crm\]
> \[Enter\]**

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

### Confirmation of the Result of Injection

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
> vipCheck (ocf::heartbeat:VIPcheck): Started (Active Node Name))
>
> prmIP (ocf::heartbeat:IPaddr2): Started (Active Node Name)
>
> prmEC (ocf::heartbeat:ec): Started (Active Node Name)
>
> prmSNMPTrapd (ocf::heartbeat:snmptrapd): Started (Active Node Name)
>
> Clone Set: clnDiskd \[prmDiskd\]
>
> Started: \[(Active Node Name) (Stand-by Node Name)\]

Node Attribute:

\* Node (Active Node Name)

\+ diskcheck\_status\_internal : normal

\* Node (Stand-by Node Name)

\+ diskcheck\_status\_internal : normal

Migration Summary:

\* Node (Active Node Name)

\* Node (Stand-by Node Name)
