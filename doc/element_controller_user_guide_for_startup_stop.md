# Element Controller User Guide for Startup/Stop

** Version 2.0 **
** April 12, 2018 **
** Copyright(c) 2018 Nippon Telegraph and Telephone Corporation**

This text describes how to Startup/Stop Element Controller.

## 1. Startup
---

When starting up from RA by Pacemaker, or in the case of non-redundancy configuration, execute the following command to start up EC.
~~~console
# ${EC_HOME}/ec_main/bin/ec_ctl.sh start [Enter]
~~~
\* When using this command, assume that virtual IP is assigned to the target server.
In a redundant configuration, virtual IP is automatically assigned by Pacemaker.

Virtual IP confirmation
~~~console
# ip addr show dev <device name> [Enter]
~~~

Virtual IP configuration
~~~console
# ip addr add <virtual IP address>/<netmask> dev <device name> [Enter]
~~~

## 2. Stop
---

In the case of non-redundancy configuration, execute the following command to stop EC.
~~~console
# ${EC_HOME}/ec_main/bin/ec_ctl.sh stop [Enter]
~~~

When stopping from RA by Pacemaker, execute the following command to stop EC.
~~~console
# ${EC_HOME}/ec_main/bin/ec_ctl.sh stop changeover [Enter]
~~~
\* In the case of non-redundancy configuration, if the standby-server is online, execute system switchover.

Executed by REST request from FC.
~~~
Normal stop URI: POST http://{active-server}:{port}/v1/internal/ec_ctrl/stop/normal
System switchover stop URI: POST http://{active-server}:{port}/v1/internal/ec_ctrl/stop/chgover
~~~

## 3. Forced stop
---

Forced stop of EC can be done to active-server. Execute the following command to forced stop EC.
~~~console
# ${EC_HOME}/ec_main/bin/ec_ctl.sh forcestop [Enter]
~~~
\* In the case of non-redundancy configuration, if the standby-server is online, execute system switchover.

## 4. Status confirmation
---

When confirming from RA by Pacemaker, or in the case of non-redundancy configuration, execute the following command to confirm EC.
~~~console
# ${EC_HOME}/ec_main/bin/ec_ctl.sh status [Enter]
~~~

Executed by REST request from FC.
~~~
Status confirmation URI	：POST http://{active-server}:{port}/v1/internal/ec_ctrl/statusget
~~~

|Result example   |
|:----------------|
|{"ec_status":{"status":"inservice","busy":"inservice"},"informations":[{"controller_type":"ec","host_name":"HOST1","management_ip_address":"192.168.0.0"}]}|

|Tag name         |Description   |
|:----------------|:-------|
|status |Stop: stop<br>Inservice: inservice<br>Inservice is in progress: startready<br>Stop is in progress: stopready<br>System switching is in progress: changeover|
|busy  |Under blockade: busy<br>No blockade: inservice|
