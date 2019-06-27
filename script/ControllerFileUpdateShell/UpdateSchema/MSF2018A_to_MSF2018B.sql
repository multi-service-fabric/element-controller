ALTER TABLE equipments ADD COLUMN q_in_q_selectable_by_vlan_if_capability boolean;
ALTER TABLE nodes ADD COLUMN q_in_q_type text;
ALTER TABLE vlan_ifs ADD COLUMN q_in_q boolean;
