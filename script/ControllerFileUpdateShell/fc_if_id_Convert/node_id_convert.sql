-- The Privileges "ON UPDATE CASCADE" are granted  to the child table.
-- Firstly, the contraints on the foreign  keys are dropped.

ALTER TABLE lag_ifs DROP CONSTRAINT "lag_ifs_node_id_fkey";
ALTER TABLE lag_ifs DROP CONSTRAINT "fk9u73sgom9j7ls5lmhi92bv0qo";
ALTER TABLE physical_ifs DROP CONSTRAINT "physical_ifs_node_id_fkey";
ALTER TABLE physical_ifs DROP CONSTRAINT "fkjbh2nsajivgencu1wn6fenmll";
ALTER TABLE vlan_ifs DROP CONSTRAINT "vlan_ifs_node_id_fkey";
ALTER TABLE vlan_ifs DROP CONSTRAINT "vlan_ifs_node_id_fkey1";
ALTER TABLE vlan_ifs DROP CONSTRAINT "vlan_ifs_node_id_fkey2";
ALTER TABLE acl_info DROP CONSTRAINT "acl_info_node_id_fkey";
ALTER TABLE acl_info DROP CONSTRAINT "acl_info_node_id_fkey1";
ALTER TABLE acl_info DROP CONSTRAINT "acl_info_node_id_fkey2";
ALTER TABLE acl_info DROP CONSTRAINT "acl_info_node_id_fkey3";
ALTER TABLE breakout_ifs DROP CONSTRAINT "breakout_ifs_node_id_fkey";
ALTER TABLE breakout_ifs DROP CONSTRAINT "fk11nhmtxq9pe186fvs3t8iwrsw";
ALTER TABLE acl_detail_info DROP CONSTRAINT "acl_detail_info_node_id_fkey";
ALTER TABLE acl_detail_info DROP CONSTRAINT "fk6laj22qep9gy2kmrlip1j1oci";
ALTER TABLE nodes_startup_notification DROP CONSTRAINT "nodes_startup_notification_node_id_fkey";
ALTER TABLE nodes_startup_notification DROP CONSTRAINT "fk278rhef1rmtpaomt2b9d7dtsa";
ALTER TABLE lag_ifs_member DROP CONSTRAINT "lag_ifs_member_node_id_fkey";
ALTER TABLE lag_ifs_member DROP CONSTRAINT "fk51vm8pdf8dpv1rqdoelqrk3ub";
ALTER TABLE bgp_option DROP CONSTRAINT "bgp_option_node_id_fkey";
ALTER TABLE bgp_option DROP CONSTRAINT "fk6cw0wwlwmkup83rd3csn2xa26";
ALTER TABLE static_route_option DROP CONSTRAINT "static_route_option_node_id_fkey";
ALTER TABLE static_route_option DROP CONSTRAINT "fk57dltf3pstcwo9pisvq48vmjf";
ALTER TABLE vrrp_option DROP CONSTRAINT "vrrp_option_node_id_fkey";
ALTER TABLE vrrp_option DROP CONSTRAINT "fk4ch929hrwkse47on2p5j5iqsi";
ALTER TABLE dummy_vlan_ifs_info DROP CONSTRAINT "dummy_vlan_ifs_info_node_id_fkey";
ALTER TABLE irb_instance_info DROP CONSTRAINT "irb_instance_info_node_id_fkey";
ALTER TABLE bgp_option DROP CONSTRAINT "bgp_option_node_id_fkey";
ALTER TABLE bgp_option DROP CONSTRAINT "fk6cw0wwlwmkup83rd3csn2xa26";


-- PrivilegesGranting Privileges "ON UPDATE CASCADE" is granted in the table again.

ALTER TABLE lag_ifs ADD FOREIGN KEY(node_id) REFERENCES nodes(node_id) ON UPDATE CASCADE;
ALTER TABLE physical_ifs ADD FOREIGN KEY(node_id) REFERENCES nodes(node_id) ON UPDATE CASCADE;
ALTER TABLE vlan_ifs ADD FOREIGN KEY(node_id, physical_if_id) REFERENCES physical_ifs(node_id, physical_if_id) ON UPDATE CASCADE;
ALTER TABLE vlan_ifs ADD FOREIGN KEY(node_id, lag_if_id)      REFERENCES lag_ifs(node_id, lag_if_id) ON UPDATE CASCADE;
ALTER TABLE vlan_ifs ADD FOREIGN KEY(node_id, breakout_if_id) REFERENCES breakout_ifs(node_id, breakout_if_id) ON UPDATE CASCADE;
ALTER TABLE acl_info ADD FOREIGN KEY(node_id,physical_if_id) REFERENCES physical_ifs(node_id,physical_if_id) ON UPDATE CASCADE;
ALTER TABLE acl_info ADD FOREIGN KEY(node_id,lag_if_id) REFERENCES lag_ifs(node_id,lag_if_id) ON UPDATE CASCADE;
ALTER TABLE acl_info ADD FOREIGN KEY(node_id,breakout_if_id) REFERENCES breakout_ifs(node_id,breakout_if_id) ON UPDATE CASCADE;
ALTER TABLE acl_info ADD FOREIGN KEY(node_id,vlan_if_id) REFERENCES vlan_ifs(node_id,vlan_if_id) ON UPDATE CASCADE;
ALTER TABLE breakout_ifs ADD FOREIGN KEY(node_id, physical_if_id) REFERENCES physical_ifs(node_id, physical_if_id) ON UPDATE CASCADE;
ALTER TABLE acl_detail_info ADD FOREIGN KEY(node_id,acl_id) REFERENCES acl_info(node_id,acl_id) ON UPDATE CASCADE;
ALTER TABLE nodes_startup_notification ADD FOREIGN KEY(node_id) REFERENCES nodes(node_id) ON UPDATE CASCADE;
ALTER TABLE lag_ifs_member ADD FOREIGN KEY(node_id, lag_if_id) REFERENCES lag_ifs(node_id, lag_if_id) ON UPDATE CASCADE;
ALTER TABLE bgp_option ADD FOREIGN KEY(node_id, vlan_if_id) REFERENCES vlan_ifs(node_id, vlan_if_id) ON UPDATE CASCADE;
ALTER TABLE static_route_option ADD FOREIGN KEY(node_id, vlan_if_id) REFERENCES vlan_ifs(node_id, vlan_if_id) ON UPDATE CASCADE;
ALTER TABLE vrrp_option ADD FOREIGN KEY(node_id, vlan_if_id) REFERENCES vlan_ifs(node_id, vlan_if_id) ON UPDATE CASCADE;
ALTER TABLE dummy_vlan_ifs_info ADD FOREIGN KEY(node_id) REFERENCES nodes(node_id) ON UPDATE CASCADE;
ALTER TABLE irb_instance_info ADD FOREIGN KEY(node_id) REFERENCES nodes(node_id) ON UPDATE CASCADE;
ALTER TABLE bgp_option ADD FOREIGN KEY(node_id, vlan_if_id) REFERENCES vlan_ifs(node_id, vlan_if_id) ON UPDATE CASCADE;

-- The node_id is upadated.
UPDATE nodes SET node_id = to_char((to_number(node_id, '9999') + 900), 'FM9999') WHERE plane IS NULL AND vpn_type IS NULL AND to_number(node_id, '9999') > 100;

-- The following keys are dropped again. 

ALTER TABLE lag_ifs DROP CONSTRAINT "lag_ifs_node_id_fkey";
ALTER TABLE physical_ifs DROP CONSTRAINT "physical_ifs_node_id_fkey";
ALTER TABLE vlan_ifs DROP CONSTRAINT "vlan_ifs_node_id_fkey";
ALTER TABLE vlan_ifs DROP CONSTRAINT "vlan_ifs_node_id_fkey1";
ALTER TABLE vlan_ifs DROP CONSTRAINT "vlan_ifs_node_id_fkey2";
ALTER TABLE acl_info DROP CONSTRAINT "acl_info_node_id_fkey";
ALTER TABLE acl_info DROP CONSTRAINT "acl_info_node_id_fkey1";
ALTER TABLE acl_info DROP CONSTRAINT "acl_info_node_id_fkey2";
ALTER TABLE acl_info DROP CONSTRAINT "acl_info_node_id_fkey3";
ALTER TABLE breakout_ifs DROP CONSTRAINT "breakout_ifs_node_id_fkey";
ALTER TABLE acl_detail_info DROP CONSTRAINT "acl_detail_info_node_id_fkey";
ALTER TABLE nodes_startup_notification DROP CONSTRAINT "nodes_startup_notification_node_id_fkey";
ALTER TABLE lag_ifs_member DROP CONSTRAINT "lag_ifs_member_node_id_fkey";
ALTER TABLE bgp_option DROP CONSTRAINT "bgp_option_node_id_fkey";
ALTER TABLE static_route_option DROP CONSTRAINT "static_route_option_node_id_fkey";
ALTER TABLE vrrp_option DROP CONSTRAINT "vrrp_option_node_id_fkey";
ALTER TABLE dummy_vlan_ifs_info DROP CONSTRAINT "dummy_vlan_ifs_info_node_id_fkey";
ALTER TABLE irb_instance_info DROP CONSTRAINT "irb_instance_info_node_id_fkey";
ALTER TABLE bgp_option DROP CONSTRAINT "bgp_option_node_id_fkey";

-- The foreign keys are added  in the table again(The Privileges "ON UPDATE CASCADE" are dropped)

ALTER TABLE lag_ifs ADD FOREIGN KEY(node_id) REFERENCES nodes(node_id);
ALTER TABLE physical_ifs ADD FOREIGN KEY(node_id) REFERENCES nodes(node_id);
ALTER TABLE vlan_ifs ADD FOREIGN KEY(node_id, physical_if_id) REFERENCES physical_ifs(node_id, physical_if_id);
ALTER TABLE vlan_ifs ADD FOREIGN KEY(node_id, lag_if_id)      REFERENCES lag_ifs(node_id, lag_if_id);
ALTER TABLE vlan_ifs ADD FOREIGN KEY(node_id, breakout_if_id) REFERENCES breakout_ifs(node_id, breakout_if_id);
ALTER TABLE acl_info ADD FOREIGN KEY(node_id,physical_if_id) REFERENCES physical_ifs(node_id,physical_if_id);
ALTER TABLE acl_info ADD FOREIGN KEY(node_id,lag_if_id) REFERENCES lag_ifs(node_id,lag_if_id);
ALTER TABLE acl_info ADD FOREIGN KEY(node_id,breakout_if_id) REFERENCES breakout_ifs(node_id,breakout_if_id);
ALTER TABLE acl_info ADD FOREIGN KEY(node_id,vlan_if_id) REFERENCES vlan_ifs(node_id,vlan_if_id);
ALTER TABLE breakout_ifs ADD FOREIGN KEY(node_id, physical_if_id) REFERENCES physical_ifs(node_id, physical_if_id);
ALTER TABLE acl_detail_info ADD FOREIGN KEY(node_id,acl_id) REFERENCES acl_info(node_id,acl_id);
ALTER TABLE nodes_startup_notification ADD FOREIGN KEY(node_id) REFERENCES nodes(node_id);
ALTER TABLE lag_ifs_member ADD FOREIGN KEY(node_id, lag_if_id) REFERENCES lag_ifs(node_id, lag_if_id);
ALTER TABLE bgp_option ADD FOREIGN KEY(node_id, vlan_if_id) REFERENCES vlan_ifs(node_id, vlan_if_id);
ALTER TABLE static_route_option ADD FOREIGN KEY(node_id, vlan_if_id) REFERENCES vlan_ifs(node_id, vlan_if_id);
ALTER TABLE vrrp_option ADD FOREIGN KEY(node_id, vlan_if_id) REFERENCES vlan_ifs(node_id, vlan_if_id);
ALTER TABLE dummy_vlan_ifs_info ADD FOREIGN KEY(node_id) REFERENCES nodes(node_id);
ALTER TABLE irb_instance_info ADD FOREIGN KEY(node_id) REFERENCES nodes(node_id);
ALTER TABLE bgp_option ADD FOREIGN KEY(node_id, vlan_if_id) REFERENCES vlan_ifs(node_id, vlan_if_id);
