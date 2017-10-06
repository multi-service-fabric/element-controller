CREATE TABLE equipments (
    equipment_type_id    text       primary key not null,
    lag_prefix           text                   not null,
    unit_connector       varchar(2)             not null,
    if_name_oid          text                   not null,
    snmptrap_if_name_oid text,
    max_repetitions      int                    not null
);

CREATE TABLE equipment_ifs (
    equipment_type_id  text  not null,
    physical_if_id     text  not null,
    if_slot            text,
    FOREIGN KEY (equipment_type_id)
        REFERENCES equipments(equipment_type_id),
    PRIMARY KEY(equipment_type_id,physical_if_id)
);

CREATE TABLE if_name_rules (
    equipment_type_id  text  not null,
    speed              text  not null,
    port_prefix        text  not null,
    FOREIGN KEY (equipment_type_id)
        REFERENCES equipments(equipment_type_id),
    PRIMARY KEY(equipment_type_id,speed)
);

CREATE TABLE nodes (
	node_type              int   not null,
	node_id                text  not null,
	equipment_type_id      text  not null,
	node_name              text  not null,
	management_if_address  text  not null UNIQUE,
	snmp_community         text  not null,
    FOREIGN KEY (equipment_type_id)
        REFERENCES equipments(equipment_type_id),
    PRIMARY KEY(node_type,node_id)
);

CREATE TABLE lag_ifs (
	node_type  int   not null,
	node_id    text  not null,
	lag_if_id  text  not null,
	if_name    text,
	FOREIGN KEY (node_type,node_id)
        REFERENCES nodes(node_type,node_id),
    PRIMARY KEY(node_type,node_id,lag_if_id)
);

CREATE TABLE internal_link_ifs (
    internal_link_if_id  text  not null,
    node_type            int   not null,
    node_id              text  not null,
    lag_if_id            text  not null UNIQUE,
    FOREIGN KEY (node_type,node_id,lag_if_id)
        REFERENCES lag_ifs(node_type,node_id,lag_if_id),
    PRIMARY KEY(internal_link_if_id)
);

CREATE TABLE l2_cps (
	slice_id             text  not null,
	cp_id                text  not null,
	node_type            int   not null,
	node_id              text  not null,
	base_physical_if_id  text,
	base_lag_if_id       text,
	if_name              text  not null,
	vlan_id              int   not null,
	FOREIGN KEY (node_type,node_id)
        REFERENCES nodes(node_type,node_id),
    PRIMARY KEY(slice_id,cp_id)
);

CREATE TABLE l3_cps (
	slice_id             text  not null,
	cp_id                text  not null,
	node_type            int   not null,
	node_id              text  not null,
	base_physical_if_id  text,
	base_lag_if_id       text,
	if_name              text  not null,
	vlan_id              int   not null,
	FOREIGN KEY (node_type,node_id)
        REFERENCES nodes(node_type,node_id),
    PRIMARY KEY(slice_id,cp_id)
);

CREATE TABLE physical_ifs (
	node_type       int   not null,
	node_id         text  not null,
	physical_if_id  text  not null,
	if_name         text,
	FOREIGN KEY (node_type,node_id)
        REFERENCES nodes(node_type,node_id),
    PRIMARY KEY(node_type,node_id,physical_if_id)
);

CREATE TABLE nodes_startup_notification (
	node_type       int   not null,
	node_id         text   not null,
	notification_reception_status  int  not null,
	FOREIGN KEY (node_type,node_id)
		REFERENCES nodes(node_type,node_id),
	PRIMARY KEY(node_type,node_id)
);

CREATE TABLE system_status (
	service_status   int  not null,
	blockade_status  int  not null
);

