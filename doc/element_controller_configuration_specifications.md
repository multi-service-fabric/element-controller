## Element Controller Configuration Specifications

**Version 1.0**
**March 28, 2018**
**Copyright(c) 2018 Nippon Telegraph and Telephone Corporation**

### Configuration Definitions

  Configurations are classified and defined by their usage. Also, they are distributed into separate files according to the usage.The table below is a list of them.

  |Namae   |Description            |
  |:---------|:---------------|
  |ec_main.conf      |The file which describes the configurations to be imported during the start up of EC Main Module.|


### Rules of Description for Configurations

There are some rules to describe definitions in configuration files with KEY_VALUE method as follows.

|No.  |Rule of Description            |
|:---------|:---------------|
|1|Character Code: UTF-8|
|2|Line Feed Code: LF|
|3|Delimiter between Key and Value: =|
|4|A line feed must be inserted just after each pair of key and value.|
|5|If a line is started with "#", it is a comment line (and will not be imported).|
|6|Half-width space and TAB characters are prohibited to be used (even in front or behind of "=" sign) except in comments.|
|7|Full-width characters (incl. all Japanese characters) are prohibited to be used except in comments.|
|8|Characters "=" and "#" are prohibited to be used in keys and values.|

Description Example (case of `ec_main.conf`)

~~~console
#REST Waiting I/F Address
rest_server_address=0.0.0.0
#REST Waiting Port
Port_number=8080
~~~

### 1. ec_main.conf

The table below shows the detail of items which are managed in the EC configuration file.

|No.  |Item Name|Key|Description|Required?|default Value|Type|Unit|Functional Block|Remarks|
|:----|:-----|:----|:-----|:----|:----|:----|:----|:----|:----|
|1|REST Waiting Interface Address|rest_server_address|REST waiting interface address|No|0.0.0.0|TEXT|-|Operation Request Reception Functional Part|-|
|2|REST Waiting Port|rest_server_port|REST waiting port; equal or greater than 0, equal or smaller than 65535|No|18080|INT|-|Operation Request Reception Functional Part|-|
|3|No. of Retries When REST Request Failed|rest_retry_num|the number of retry when REST request was failed; equal or greater than 0|No|0|INT|times|Operation Execution Functional Part|If 0, retry will not performed. This is used when EC sends request to FC as a client.|
|4|REST Request Timeout Time|rest_timeout|REST request timeout time; equal or greater than 1|Yes|-|INT|sec|Operation Execution Functional Part|This is used when EC sends request to FC as a client. Connection timeout values are dependent on the OS configuration.|
|5|FC Connection Address (for REST)|fc_address|FC connection address (for REST)|Yes|-|TEXT|-|Operation Execution Functional Part|-|
|6|FC Connection Port Number (for REST)|fc_port|FC connection port number (for REST); equal or greater than 0, equal or smaller than 65535|Yes|-|INT|-|Operation Execution Functional Part|-|
|7|Service Running Status|service_status|service star-up status; Specify either "in ready for start (startready)" or "in the process of changing over (changeover)".|No|startready|TEXT|-|Operation Management Functional Part|Fixed Value (Set the default value.)|
|8|Maintenance Blockage Status|blockade_status|maintenance blockage status; Specify either "blocked (busy)" or "opened (inservice)".|No|inservice|TEXT|-|Between EC-EM I/F Control Functional Part|Fixed Value (Set the default value.)|
|9|EM Connection Address (for NETCONF over ssh)|em_address|EM connection address (for NETCONF over ssh)|Yes|-|TEXT|-|Between EC-EM I/F Control Functional Part|-|
|10|EM Connection Port Number (for NETCONF over ssh)|em_port|EM connection port number (for NETCONF over ssh); equal or greater than 0, equal or smaller than 65535|Yes|-|INT|-|Between EC-EM I/F Control Functional Part|-|
|11|EM Connection User Name (for NETCONF over ssh)|em_user|EM connection user name (for NETCONF over ssh)|Yes|-|TEXT|-|Between EC-EM I/F Control Functional Part|-|
|12|EM Connection Password (for NETCONF over ssh)|em_password|EM connection password (for NETCONF over ssh)|Yes|-|TEXT|-|Between EC-EM I/F Control Functional Part|-|
|13|EM Connection Timeout Value (for NETCONF over ssh)|em_timeout|EM connection timeout value (for NETCONF over ssh); equal or greater than 1|Yes|-|INT|sec|Between EC-EM I/F Control Functional Part|-|
|14|SNMP Request Timeout Time in Configuration Management Function|device_snmp_timeout|SNMP request timeout time in the configuration management function; equal or greater than 1|Yes|-|INT|sec|EC Inter-Device I/F Control Functional Part|-|
|15|I/F Status Adjustment Process Execution Cycle|failure_mib_interval|I/F status adjustment process execution cycle; equal or greater than 1|Yes|-|INT|sec|EC Inter-Device I/F Control Functional Part|-|
|16|SNMP Request Timeout Time in Failure Management Function|failure_snmp_timeout|SNMP request timeout time in the failure management function; equal or greater than 1|Yes|-|INT|sec|EC Inter-Device I/F Control Functional Part|-|
|17|Execution Cycle of MIB Collection from the Managed Device in Traffic Management Function|traffic_mib_interval|execution cycle of MIB collection from the managed device in traffic management function; equal or greater than 0|No|1|INT|min|EC Inter-Device I/F Control Functional Part|If 0, do not operate the traffic management function. Make it the same value as the "traffic_tm_interval" of FC.|
|18|Timeout Time for MIB Collection|traffic_snmp_timeout|timeout time for MIB collection; equal or greater than 1 (exceptionally 0 when traffic_mib_interval is 0)|No|(traffic_mib_intervaｌ x 60)/2|INT|sec|EC Inter-Device I/F Control Functional Part|-|
|19|Timeout Value for Waiting EM Queue|em_queue_timeout|timeout value for waiting EM queue; equal or greater than 1|Yes|-|INT|sec|Between EC-EM I/F Control Functional Part|-|
|20|DHCP Configuration File|device_dhcp_config|DHCP configuration file (/etc/dhcp/dhcpd.conf)|Yes|-|TEXT|-|EC Inter-Device I/F Control Functional Part|-|
|21|Periodic Termination Process Timeout Time|gather_mib_stop_timeout|timeout value for waiting for the completion of the running I/F status adjustment process in the periodic termination process of the device information collection functional part; equal or greater than 1|Yes|-|INT|sec|EC Inter-Device I/F Control Functional Part|-|
|22|Periodic Termination Process Status Confirmation Cycle|gather_mib_stop_interval|confirmation cycle for the status of I/F status adjustment process in the periodic termination process of the device information collection functional part; equal or greater than 1|Yes|-|INT|msec|EC Inter-Device I/F Control Functional Part|-|
|23|RSYSLOG Configuration File Name|device_rsyslog_config|RSYSLOG configuration file (/etc/rsyslog.conf)|Yes|-|TEXT|-|EC Inter-Device I/F Control Functional Part|-|
|24|Timeout Value for Waiting Operation Queue|operation_queue_timeout|timeout value for waiting operation queue; equal or greater than 1|Yes|-|INT|sec|Operation Management Functional Part|-|
|25|EC Management I/F Address|device_ec_management_if|management I/F address of EC (for DHCP configuration)|Yes|-|TEXT|-|EC Inter-Device I/F Control Functional Part|-|
|26|Max. No. of REST Process Threads|rest_thread_max|the upper limit of the number of REST process threads (the number of running threads for the process received from FC); equal or greater than 1|Yes|-|INT|-|Operation Request Reception Functional Part|In case changing the value, it is needed to be matched with the number of DB connection pools.|
|27|Script Installation Path|script_path|the path where the script called for by rsyslogd (boot.sh) is located|Yes|-|TEXT|-|EC Inter-Device I/F Control Functional Part|-|
|28|Request Log Output Path|request_log_path|destination path of log for responding FC in jetty|Yes|-|TEXT|-|Operation Request Reception Functional Part|-|
|29|Request Log Retention Period (Days)|request_keep_period|retention period of the destination path of log for responding FC in jetty; equal or greater than 1|Yes|-|INT|days|Operation Request Reception Functional Part|-|
|30|No. of Retries for REST Waiting I/F bind Failure|rest_server_bind_ng_retry_num|the number of retries for REST waiting interface bind failure; equal or greater than 0|Yes|-|INT|times|Operation Request Reception Functional Part|-|
|31|ACT Server Physical IP Address|server_physical_address|own server physical IP address|Yes|-|TEXT|-|Operation Execution Functional Part|-|
|32|EM Connection Address (for REST)|em_rest_address|EM connection address (for REST)|Yes|-|INT|-|Operation Execution Functional Part|-|
|33|EM Connection Port Number (for REST)|em_rest_port|EM connection port number (for REST); equal or greater than 0, equal or smaller thatn 65535|Yes|-|INT|-|Operation Execution Functional Part|-|
|34|No. of REST Requests Average Time|rest_request_average|the unit time (sec) used in calculating the number of REST requests|Yes|3600|INT|sec|Operation Execution Functional Part|-|
|35|Log File Path|log_file_path|the path where the Processing log file (application.log) is located|Yes|-|TEXT|-|Operation Execution Functional Part|-|
