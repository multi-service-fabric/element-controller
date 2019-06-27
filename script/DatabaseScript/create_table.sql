CREATE TABLE equipments (
    equipment_type_id                            text         NOT NULL,
    lag_prefix                                   text         NOT NULL,
    unit_connector                               varchar(2)   NOT NULL,
    if_name_oid                                  text         NOT NULL,
    snmptrap_if_name_oid                         text,
    max_repetitions                              integer      NOT NULL,
    platform_name                                text         NOT NULL,
    os_name                                      text         NOT NULL,
    firmware_version                             text         NOT NULL,
    router_type                                  integer      NOT NULL,
    core_router_physical_if_name_format          text,
    breakout_if_name_syntax                      text,
    breakout_if_name_suffix_list                 text,
    dhcp_template                                text         NOT NULL,
    config_template                              text         NOT NULL,
    initial_config                               text         NOT NULL,
    boot_complete_msg                            text         NOT NULL,
    evpn_capability                              boolean      NOT NULL,
    l2vpn_capability                             boolean      NOT NULL,
    l3vpn_capability                             boolean      NOT NULL,
    qos_shaping_flg                              boolean      NOT NULL,
    qos_remark_flg                               boolean      NOT NULL,
    default_remark_menu                          text,
    default_egress_queue_menu                    text,
    same_vlan_number_traffic_total_value_flag    text,
    vlan_traffic_capability                      text,
    vlan_traffic_counter_name_mib_oid            text,
    vlan_traffic_counter_value_mib_oid           text,
    cli_exec_path                                text,
    irb_asymmetric_capability                    boolean,
    irb_symmetric_capability                     boolean,
    q_in_q_selectable_by_node_capability         boolean,
    q_in_q_selectable_by_vlan_if_capability      boolean,
    PRIMARY KEY(equipment_type_id)
);

CREATE TABLE equipment_ifs (
    equipment_type_id                    text         NOT NULL,
    physical_if_id                       text         NOT NULL,
    if_slot                              text         NOT NULL,
    port_speed                           text         NOT NULL,
    PRIMARY KEY(equipment_type_id, physical_if_id, port_speed),
    FOREIGN KEY(equipment_type_id) REFERENCES equipments(equipment_type_id)
);

CREATE TABLE if_name_rules (
    equipment_type_id                    text         NOT NULL,
    speed                                text         NOT NULL,
    port_prefix                          text         NOT NULL,
    PRIMARY KEY(equipment_type_id, speed),
    FOREIGN KEY(equipment_type_id) REFERENCES equipments(equipment_type_id)
);

CREATE TABLE remark_menu (
   equipment_type_id                    text         NOT NULL,
   remark_menu                          text         NOT NULL,
   FOREIGN KEY(equipment_type_id) REFERENCES equipments(equipment_type_id)
);

CREATE TABLE egress_queue_menu (
  equipment_type_id                    text         NOT NULL,
  egress_queue_menu                    text         NOT NULL,
  FOREIGN KEY(equipment_type_id) REFERENCES equipments(equipment_type_id)
);

CREATE TABLE boot_error_msgs (
    equipment_type_id                    text         NOT NULL,
    boot_error_msgs                      text,
    FOREIGN KEY(equipment_type_id) REFERENCES equipments(equipment_type_id)
);

CREATE TABLE nodes (
    node_id                              text         NOT NULL,
    node_name                            text         NOT NULL,
    equipment_type_id                    text         NOT NULL,
    management_if_address                text         NOT NULL,
    mng_if_addr_prefix                   integer      NOT NULL,
    snmp_community                       text         NOT NULL,
    node_state                           integer      NOT NULL,
    provisioning                         boolean      NOT NULL,
    plane                                integer,
    vpn_type                             varchar(2),
    as_number                            text,
    loopback_if_address                  text         NOT NULL,
    username                             text         NOT NULL,
    password                             text         NOT NULL,
    ntp_server_address                   text         NOT NULL,
    host_name                            text         NOT NULL,
    mac_addr                             varchar(17)  NOT NULL,
    irb_type                             text,
    q_in_q_type                          text,
    PRIMARY KEY(node_id),
    UNIQUE (mac_addr),
    FOREIGN KEY(equipment_type_id) REFERENCES equipments(equipment_type_id)
);

CREATE TABLE lag_ifs (
    node_id                              text         NOT NULL,
    lag_if_id                            text         NOT NULL,
    fc_lag_if_id                         text         NOT NULL,
    if_name                              text         NOT NULL,
    minimum_link_num                     integer      NOT NULL,
    if_speed                             varchar(8),
    if_status                            integer      NOT NULL,
    ipv4_address                         varchar(15),
    ipv4_prefix                          integer,
    PRIMARY KEY(node_id, lag_if_id),
    FOREIGN KEY(node_id) REFERENCES nodes(node_id)
);

CREATE TABLE physical_ifs (
    node_id                              text         NOT NULL,
    physical_if_id                       text         NOT NULL,
    if_name                              text,
    if_speed                             varchar(8),
    if_status                            integer,
    ipv4_address                         varchar(15),
    ipv4_prefix                          integer,
    PRIMARY KEY(node_id, physical_if_id),
    FOREIGN KEY(node_id) REFERENCES nodes(node_id)
);

CREATE TABLE nodes_startup_notification (
    node_id                              text         NOT NULL,
    notification_reception_status        integer      NOT NULL,
    PRIMARY KEY(node_id),
    FOREIGN KEY(node_id) REFERENCES nodes(node_id)
);

CREATE TABLE lag_ifs_member (
    num                                  serial       NOT NULL,
    node_id                              text,
    lag_if_id                            text,
    physical_if_id                       text,
    breakout_if_id                       text,
    FOREIGN KEY(node_id, lag_if_id) REFERENCES lag_ifs(node_id, lag_if_id)
);

CREATE TABLE breakout_ifs (
    node_id                              text         NOT NULL,
    breakout_if_id                       text         NOT NULL,
    physical_if_id                       text         NOT NULL,
    speed                                text         NOT NULL,
    if_name                              text         NOT NULL,
    breakout_if_index                    text         NOT NULL,
    if_status                            integer,
    ipv4_address                         varchar(15),
    ipv4_prefix                          integer,
    PRIMARY KEY(node_id, breakout_if_id),
    FOREIGN KEY(node_id, physical_if_id) REFERENCES physical_ifs(node_id, physical_if_id)
);

CREATE TABLE vlan_ifs (
    node_id                              text NOT NULL,
    vlan_if_id                           text NOT NULL,
    physical_if_id                       text,
    lag_if_id                            text,
    breakout_if_id                       text,
    vlan_id                              text,
    if_name                              text NOT NULL,
    if_status                            integer,
    ipv4_address                         varchar(15),
    ipv4_prefix                          integer,
    ipv6_address                         varchar(39),
    ipv6_prefix                          integer,
    mtu                                  integer,
    port_mode                            integer,
    bgp_id                               text,
    vrrp_id                              text,
    inflow_shaping_rate                  float,
    outflow_shaping_rate                 float,
    remark_menu                          text,
    egress_queue_menu                    text,
    irb_instance_id                      text,
    q_in_q                               boolean,
    PRIMARY KEY(node_id, vlan_if_id),
    FOREIGN KEY(node_id, physical_if_id) REFERENCES physical_ifs(node_id, physical_if_id),
    FOREIGN KEY(node_id, lag_if_id)      REFERENCES lag_ifs(node_id, lag_if_id),
    FOREIGN KEY(node_id, breakout_if_id) REFERENCES breakout_ifs(node_id, breakout_if_id)
);

CREATE TABLE bgp_option (
    bgp_id                               text         NOT NULL,
    bgp_role                             integer,
    bgp_neighbor_as                      integer,
    bgp_neighbor_ipv4_address            varchar(15),
    bgp_neighbor_ipv6_address            varchar(39),
    node_id                              text        NOT NULL,
    vlan_if_id                           text        NOT NULL,
    PRIMARY KEY(bgp_id),
    FOREIGN KEY(node_id, vlan_if_id) REFERENCES vlan_ifs(node_id, vlan_if_id)
);

CREATE TABLE static_route_option (
    node_id                              text         NOT NULL,
    vlan_if_id                           text         NOT NULL,
    static_route_address_type            integer,
    static_route_destination_address     varchar(39),
    static_route_prefix                  integer,
    static_route_nexthop_address         varchar(39),
    FOREIGN KEY(node_id, vlan_if_id) REFERENCES vlan_ifs(node_id, vlan_if_id)
);

CREATE TABLE vrrp_option (
    vrrp_id                              text         NOT NULL,
    vrrp_group_id                        integer,
    vrrp_role                            integer,
    vrrp_virtual_ipv4_address            varchar(15),
    vrrp_virtual_ipv6_address            varchar(39),
    node_id                              text         NOT NULL,
    vlan_if_id                           text         NOT NULL,
    PRIMARY KEY(vrrp_id),
    FOREIGN KEY(node_id, vlan_if_id) REFERENCES vlan_ifs(node_id, vlan_if_id)
);

CREATE TABLE system_status (
    blockade_status                      integer      NOT NULL,
    service_status                       integer      NOT NULL
);

--CREATE TABLE operation_phase (
--    node_name                            text         NOT NULL,
--    phase                                Integer      NOT NULL
--);

CREATE TABLE dummy_vlan_ifs_info (
    node_id                              text         NOT NULL,
    vlan_if_id                           text         NOT NULL,
    vlan_id                              text         NOT NULL,
    irb_instance_id                      text         NOT NULL,
    PRIMARY KEY(node_id,vlan_if_id),
    FOREIGN KEY(node_id) REFERENCES nodes(node_id)
);

CREATE TABLE irb_instance_info (
    node_id                              text         NOT NULL,
    vlan_id                              text         NOT NULL,
    irb_instance_id                      text         NOT NULL,
    irb_vni                              text         NOT NULL,
    irb_ipv4_address                     text         NOT NULL,
    irb_ipv4_prefix                      integer      NOT NULL,
    virtual_gateway_address              text,
    PRIMARY KEY(node_id,vlan_id,irb_instance_id),
    FOREIGN KEY(node_id) REFERENCES nodes(node_id)
);

CREATE TABLE acl_info (
    node_id                              text,
    acl_id                               text         NOT NULL,
    physical_if_id                       text,
    lag_if_id                            text,
    breakout_if_id                       text,
    vlan_if_id                           text,
    PRIMARY KEY(node_id,acl_id),
    FOREIGN KEY(node_id,physical_if_id) REFERENCES physical_ifs(node_id,physical_if_id),
    FOREIGN KEY(node_id,lag_if_id) REFERENCES lag_ifs(node_id,lag_if_id),
    FOREIGN KEY(node_id,breakout_if_id) REFERENCES breakout_ifs(node_id,breakout_if_id),
    FOREIGN KEY(node_id,vlan_if_id) REFERENCES vlan_ifs(node_id,vlan_if_id)

);

CREATE TABLE acl_detail_info (
    node_id                              text,
    acl_id                               text,
    term_name                            text         NOT NULL,
    action                               text         NOT NULL,
    direction                            text         NOT NULL,
    source_mac_address                   text,
    destination_mac_address              text,
    source_ip_address                    text,
    destination_ip_address               text,
    source_port                          integer,
    destination_port                     integer,
    protocol                             text,
    acl_priority                         integer,
    PRIMARY KEY(node_id,acl_id,term_name),
    FOREIGN KEY(node_id,acl_id) REFERENCES acl_info(node_id,acl_id)
);
