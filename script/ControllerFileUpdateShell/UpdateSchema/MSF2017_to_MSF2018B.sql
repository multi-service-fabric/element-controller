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

ALTER TABLE equipments ADD COLUMN same_vlan_number_traffic_total_value_flag boolean;
ALTER TABLE equipments ADD COLUMN vlan_traffic_capability text;
ALTER TABLE equipments ADD COLUMN vlan_traffic_counter_name_mib_oid text;
ALTER TABLE equipments ADD COLUMN vlan_traffic_counter_value_mib_oid text;
ALTER TABLE equipments ADD COLUMN cli_exec_path text;
ALTER TABLE equipments ADD COLUMN irb_asymmetric_capability boolean;
ALTER TABLE equipments ADD COLUMN irb_symmetric_capability boolean;
ALTER TABLE equipments ADD COLUMN q_in_q_selectable_by_vlan_if_capability boolean;

ALTER TABLE nodes ADD COLUMN irb_type text;
ALTER TABLE nodes ADD COLUMN q_in_q_type text;

ALTER TABLE vlan_ifs ADD COLUMN irb_instance_id text;
ALTER TABLE vlan_ifs ADD COLUMN q_in_q boolean;
