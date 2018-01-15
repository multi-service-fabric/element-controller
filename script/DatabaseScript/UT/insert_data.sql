
/* Model Information */
insert into equipments values ('1001', 'ae', '.', '1.3.6.1.2.1.31.1.1.1.1', '1.3.6.1.2.1.31.1.1.1.1', 10, 'platformJ', 'Junos', 'farmversionJ', 0, null, '<PORTPREFIX>-<IFSOLTNAME>:<BREAKOUT
IFSUFFIX>', '0:1:2:3' , '/home/msf/IT1/testData/dhcp_template', '/home/msf/IT1/testData/config_template', '/home/msf/IT1/testData/initial_config', 'boot_success', false, false, false);
insert into equipments values ('1002', 'ae', '.', '1.3.6.1.2.1.2.2.1.2', '1.3.6.1.2.1.2.2.1.2', 20, 'platformJ', 'Junos', 'farmversionJ', 0, null, '<PORTPREFIX>=<IFSOLTNAME>:<BREAKOUTIFSUFF
IX>', '4:5:6:7' , '/home/msf/IT1/testData/dhcp_template', '/home/msf/IT1/testData/config_template', '/home/msf/IT1/testData/initial_config', 'boot_success', true, true, false);
insert into equipments values ('1003', 'ae', '.', '1.3.6.1.2.1.31.1.1.1.1', '1.3.6.1.2.1.31.1.1.1.1', 30, 'platformJ', 'Junos', 'farmversionJ', 0, null, '<PORTPREFIX>==<IFSOLTNAME>:<BREAKOU
TIFSUFFIX>', '8:9:10:11' , '/home/msf/IT1/testData/dhcp_template', '/home/msf/IT1/testData/config_template', '/home/msf/IT1/testData/initial_config', 'boot_success', false, false, true);
insert into equipments values ('1004', 'ae', '.', '1.3.6.1.2.1.2.2.1.2', '1.3.6.1.2.1.2.2.1.2', 40, 'platformJ', 'Junos', 'farmversionJ', 0, null, '<PORTPREFIX>--<IFSOLTNAME>:<BREAKOUTIFSUFFIX>', '12:13:14:15' , '/home/msf/IT1/testData/dhcp_template', '/home/msf/IT1/testData/config_template', '/home/msf/IT1/testData/initial_config', 'boot_success', false, false, true);
insert into equipments values ('1005', 'ae', '.', '1.3.6.1.2.1.31.1.1.1.1 ', null, 50, 'platformJ', 'Junos', 'farmversionJ', 0, null, '<PORTPREFIX>-<IFSOLTNAME>+<BREAKOUTIFSUFFIX>', 'A:B:C:D', '/home/msf/IT1/testData/dhcp_template', '/home/msf/IT1/testData/config_template', '/home/msf/IT1/testData/initial_config', 'boot_success', false, false, false);
insert into equipments values ('1006', 'ae', '.', '1.3.6.1.2.1.2.2.1.2', null, 60, 'platformJ', 'Junos', 'farmversionJ', 0, null, '<PORTPREFIX>-<IFSOLTNAME>++<BREAKOUTIFSUFFIX>', 'E:F:G:H' , '/home/msf/IT1/testData/dhcp_template', '/home/msf/IT1/testData/config_template', '/home/msf/IT1/testData/initial_config', 'boot_success', true, true, false);
insert into equipments values ('1007', 'ae', '.', '1.3.6.1.2.1.31.1.1.1.1', null, 70, 'platformJ', 'Junos', 'farmversionJ', 0, null, '<PORTPREFIX>-<IFSOLTNAME>*<BREAKOUTIFSUFFIX>', 'I:J:K:L' , '/home/msf/IT1/testData/dhcp_template', '/home/msf/IT1/testData/config_template', '/home/msf/IT1/testData/initial_config', 'boot_success', false, false, false);
insert into equipments values ('1008', 'ae', '.', '1.3.6.1.2.1.2.2.1.2', null, 80, 'platformJ', 'Junos', 'farmversionJ', 0, null, '<PORTPREFIX>-<IFSOLTNAME>**<BREAKOUTIFSUFFIX>', 'M:N:O:P' , '/home/msf/IT1/testData/dhcp_template', '/home/msf/IT1/testData/config_template', '/home/msf/IT1/testData/initial_config', 'boot_success', false, false, true);



/* Model IF Information */
insert into equipment_ifs values ('1001', '3001', '0/0/1', '100g');
insert into equipment_ifs values ('1001', '3002', '0/0/2', '100g');
insert into equipment_ifs values ('1001', '3003', '0/0/3', '100g');
insert into equipment_ifs values ('1001', '3004', '0/0/4', '100g');
insert into equipment_ifs values ('1001', '3003', '0/0/3', '40g');
insert into equipment_ifs values ('1001', '3004', '0/0/4', '40g');
insert into equipment_ifs values ('1002', '3005', '0/0/1', '100g');
insert into equipment_ifs values ('1002', '3006', '0/0/2', '100g');
insert into equipment_ifs values ('1002', '3007', '0/0/3', '100g');
insert into equipment_ifs values ('1002', '3008', '0/0/4', '100g');
insert into equipment_ifs values ('1002', '3007', '0/0/3', '40g');
insert into equipment_ifs values ('1002', '3008', '0/0/4', '40g');
insert into equipment_ifs values ('1003', '3009', '0/0/1', '100g');
insert into equipment_ifs values ('1003', '3010', '0/0/2', '100g');
insert into equipment_ifs values ('1003', '3011', '0/0/3', '100g');
insert into equipment_ifs values ('1003', '3012', '0/0/4', '100g');
insert into equipment_ifs values ('1003', '3011', '0/0/3', '40g');
insert into equipment_ifs values ('1003', '3012', '0/0/4', '40g');
insert into equipment_ifs values ('1004', '3013', '0/0/1', '100g');
insert into equipment_ifs values ('1004', '3014', '0/0/2', '100g');
insert into equipment_ifs values ('1004', '3015', '0/0/3', '100g');
insert into equipment_ifs values ('1004', '3016', '0/0/4', '100g');
insert into equipment_ifs values ('1004', '3015', '0/0/3', '40g');
insert into equipment_ifs values ('1004', '3016', '0/0/4', '40g');
insert into equipment_ifs values ('1005', '3017', '0/0/1', '100g');
insert into equipment_ifs values ('1005', '3018', '0/0/2', '100g');
insert into equipment_ifs values ('1005', '3019', '0/0/3', '100g');
insert into equipment_ifs values ('1005', '3020', '0/0/4', '100g');
insert into equipment_ifs values ('1005', '3019', '0/0/3', '40g');
insert into equipment_ifs values ('1005', '3020', '0/0/4', '40g');
insert into equipment_ifs values ('1006', '3021', '0/0/1', '100g');
insert into equipment_ifs values ('1006', '3022', '0/0/2', '100g');
insert into equipment_ifs values ('1006', '3023', '0/0/3', '100g');
insert into equipment_ifs values ('1006', '3024', '0/0/4', '100g');
insert into equipment_ifs values ('1006', '3023', '0/0/3', '40g');
insert into equipment_ifs values ('1006', '3024', '0/0/4', '40g');
insert into equipment_ifs values ('1007', '3025', '0/0/1', '100g');
insert into equipment_ifs values ('1007', '3026', '0/0/2', '100g');
insert into equipment_ifs values ('1007', '3027', '0/0/3', '100g');
insert into equipment_ifs values ('1007', '3028', '0/0/4', '100g');
insert into equipment_ifs values ('1007', '3027', '0/0/3', '40g');
insert into equipment_ifs values ('1007', '3028', '0/0/4', '40g');
insert into equipment_ifs values ('1008', '3029', '0/0/1', '100g');
insert into equipment_ifs values ('1008', '3030', '0/0/2', '100g');
insert into equipment_ifs values ('1008', '3031', '0/0/3', '100g');
insert into equipment_ifs values ('1008', '3032', '0/0/4', '100g');
insert into equipment_ifs values ('1008', '3031', '0/0/3', '40g');
insert into equipment_ifs values ('1008', '3032', '0/0/4', '40g');



/* Physical IF Naming Convention Information */
insert into if_name_rules values ('1001', '40g', 'ge');
insert into if_name_rules values ('1001', '100g', 'xe-');
insert into if_name_rules values ('1002', '40g', 'ge');
insert into if_name_rules values ('1002', '100g', 'xe-');
insert into if_name_rules values ('1003', '40g', 'ge');
insert into if_name_rules values ('1003', '100g', 'xe-');
insert into if_name_rules values ('1004', '40g', 'ge');
insert into if_name_rules values ('1004', '100g', 'xe-');
insert into if_name_rules values ('1005', '40g', 'ge');
insert into if_name_rules values ('1005', '100g', 'xe-');
insert into if_name_rules values ('1006', '40g', 'ge');
insert into if_name_rules values ('1006', '100g', 'xe-');
insert into if_name_rules values ('1007', '40g', 'ge');
insert into if_name_rules values ('1007', '100g', 'xe-');
insert into if_name_rules values ('1008', '40g', 'ge');
insert into if_name_rules values ('1008', '100g', 'xe-');

/* Start-up Failure Determination Message*/
insert into boot_error_msgs values ('1001', 'boot_error1');
insert into boot_error_msgs values ('1001', 'boot_error2');
insert into boot_error_msgs values ('1002', 'boot_error1');
insert into boot_error_msgs values ('1002', 'boot_error2');
insert into boot_error_msgs values ('1003', 'boot_error1');
insert into boot_error_msgs values ('1003', 'boot_error2');
insert into boot_error_msgs values ('1004', 'boot_error1');
insert into boot_error_msgs values ('1004', 'boot_error2');
insert into boot_error_msgs values ('1005', 'boot_error1');
insert into boot_error_msgs values ('1005', 'boot_error2');
insert into boot_error_msgs values ('1006', 'boot_error1');
insert into boot_error_msgs values ('1006', 'boot_error2');
insert into boot_error_msgs values ('1007', 'boot_error1');
insert into boot_error_msgs values ('1007', 'boot_error2');
insert into boot_error_msgs values ('1007', 'boot_error3');
insert into boot_error_msgs values ('1008', 'boot_error1');


/* Device Information */
insert into nodes values ('2001', 'Spine_2001', '1001', '192.168.0.123', 'public', 0, true, 1, null, '1.1.1.1', 'user1', 'pass1', '3.1.1.1', 'Spine2001', '8-1-1-1-1-1-1');
insert into nodes values ('2002', 'Spine_2002', '1001', '192.168.0.123', 'public', 1, false, 1, null, '1.1.1.2', 'user2', 'pass2', '3.1.1.2', 'Spine2002','8-1-1-1-1-1-2');
insert into nodes values ('2003', 'LeafL2_2001', '1002', '192.168.0.123', 'public', 2, true, 1, 'l2', '1.1.1.3', 'user3', 'pass3', '3.1.1.3', 'LeafL2_2001','8-1-1-1-1-1-3');
insert into nodes values ('2004', 'LeafL2_2002', '1002', '192.168.0.123', 'public', 3, false, 2, 'l2', '1.1.1.4', 'user4', 'pass4', '3.1.1.4', 'LeafL2_2002','8-1-1-1-1-1-4');
insert into nodes values ('2005', 'LeafL3_2001', '1003', '192.168.0.123', 'public', 4, true, 1, 'l3', '1.1.1.5', 'user5', 'pass5', '3.1.1.5', 'LeafL3_2001','8-1-1-1-1-1-5');
insert into nodes values ('2006', 'LeafL3_2002', '1003', '192.168.0.123', 'public', 5, false, 2, 'l3', '1.1.1.6', 'user6', 'pass6', '3.1.1.6', 'LeafL3_2002','8-1-1-1-1-1-6');
insert into nodes values ('2007', 'B-Leaf_2001', '1004', '192.168.0.123',  'public', 6, true, 1, null, '1.1.1.7', 'user7', 'pass7', '3.1.1.7', 'B-Leaf2001','8-1-1-1-1-1-7');
insert into nodes values ('2008', 'B-Leaf_2002', '1004', '192.168.0.123','public', 7, false, 1, null, '1.1.1.8', 'user8', 'pass8', '3.1.1.8', 'B-Leaf2002','8-1-1-1-1-1-8');
insert into nodes values ('2009', 'Spine_2003', '1005', '192.168.0.123', 'public', 0, true, null, null, '1.1.1.9', 'user9', 'pass9', '3.1.1.9', 'Spine_2003','8-1-1-1-1-1-9');
insert into nodes values ('2010', 'Spine_2004', '1005', '192.168.0.123', 'public', 1, false, null, null, '1.1.1.10', 'user10', 'pass10', '3.1.1.10', 'Spine_2004','8-1-1-1-1-1-10');
insert into nodes values ('2011', 'LeafL2_2003', '1006', '192.168.0.123', 'public', 2, true, 2, 'l2', '1.1.1.11', 'user11', 'pass11', '3.1.1.11', 'LeafL2_2003','8-1-1-1-1-1-11');
insert into nodes values ('2012', 'LeafL2_2004', '1006', '192.168.0.123', 'public', 3, false, 1, 'l2', '1.1.1.12', 'user12', 'pass12', '3.1.1.12', 'LeafL2_2004','8-1-1-1-1-1-12');
insert into nodes values ('2013', 'LeafL3_2003', '1007', '192.168.0.123', 'public', 4, true, 2, 'l3', '1.1.1.13', 'user13', 'pass13', '3.1.1.13', 'LeafL3_2003','8-1-1-1-1-1-13');
insert into nodes values ('2014', 'LeafL3_2004', '1007', '192.168.0.123', 'public', 5, false, 1, 'l3', '1.1.1.14', 'user14', 'pass14', '3.1.1.14', 'LeafL3_2004','8-1-1-1-1-1-14');
insert into nodes values ('2015', 'B-Leaf_2003', '1008', '192.168.0.123', 'public', 6, true, null, null, '1.1.1.15', 'user15', 'pass15', '3.1.1.15', 'B-Leaf_2003','8-1-1-1-1-1-15');
insert into nodes values ('2016', 'B-Leaf_2004', '1008', '192.168.0.123', 'public', 7, false, null, null, '1.1.1.16', 'user16', 'pass16', '3.1.1.16', 'B-Leaf_2004','8-1-1-1-1-1-16');

/* Device Start-up Notification Information */
insert into nodes_startup_notification values ('2001', 1);
insert into nodes_startup_notification values ('2002', 1);
insert into nodes_startup_notification values ('2003', 1);
insert into nodes_startup_notification values ('2004', 1);
insert into nodes_startup_notification values ('2005', 1);
insert into nodes_startup_notification values ('2006', 1);
insert into nodes_startup_notification values ('2007', 1);
insert into nodes_startup_notification values ('2008', 1);
insert into nodes_startup_notification values ('2009', 2);
insert into nodes_startup_notification values ('2010', 2);
insert into nodes_startup_notification values ('2011', 2);
insert into nodes_startup_notification values ('2012', 2);
insert into nodes_startup_notification values ('2013', 2);
insert into nodes_startup_notification values ('2014', 2);
insert into nodes_startup_notification values ('2015', 2);
insert into nodes_startup_notification values ('2016', 0);



/* Physical IF Information */
insert into physical_ifs values ('2001', '3001', 'ge0/0/1', '100g', 1, '100.0.0.1', 24);
insert into physical_ifs values ('2001', '3002', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2001', '3003', 'xe-0/0/1', '40g', null, null, null);
insert into physical_ifs values ('2001', '3004', null, null, null, null, null);
insert into physical_ifs values ('2002', '3001', 'ge0/0/1', '100g', 1, '100.0.0.5', 24);
insert into physical_ifs values ('2002', '3002', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2002', '3003', 'xe-0/0/1', '40g', null, null, null);
insert into physical_ifs values ('2002', '3004', null, null, null, null, null);
insert into physical_ifs values ('2003', '3005', 'ge0/0/1', '100g', 1, '100.0.0.9', 24);
insert into physical_ifs values ('2003', '3006', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2003', '3007', 'xe-0/0/1', '40g', null, null, null);
insert into physical_ifs values ('2003', '3008', null, null, null, null, null);
insert into physical_ifs values ('2004', '3005', 'ge0/0/1', '100g', 1, '100.0.0.13', 24);
insert into physical_ifs values ('2004', '3006', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2004', '3007', 'xe-0/0/1', '40g', null, null, null);
insert into physical_ifs values ('2004', '3008', null, null, null, null, null);
insert into physical_ifs values ('2005', '3009', 'ge0/0/1', '100g', 1, '100.0.0.17', 24);
insert into physical_ifs values ('2005', '3010', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2005', '3011', 'xe-0/0/1', '40g', null, null, null);
insert into physical_ifs values ('2005', '3012', null, null, null, null, null);
insert into physical_ifs values ('2006', '3009', 'ge0/0/1', '100g', 1, '100.0.0.21', 24);
insert into physical_ifs values ('2006', '3010', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2006', '3011', 'xe-0/0/1', '40g', null, null, null);
insert into physical_ifs values ('2006', '3012', null, null, null, null, null);
insert into physical_ifs values ('2007', '3013', 'ge0/0/1', '100g', 1, '100.0.0.25', 24);
insert into physical_ifs values ('2007', '3014', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2007', '3015', 'xe-0/0/1', '40g', null, null, null);
insert into physical_ifs values ('2007', '3016', null, null, null, null, null);
insert into physical_ifs values ('2008', '3013', 'ge0/0/1', '100g', 1, '100.0.0.29', 24);
insert into physical_ifs values ('2008', '3014', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2008', '3015', 'xe-0/0/1', '40g', null, null, null);
insert into physical_ifs values ('2008', '3016', null, null, null, null, null);
insert into physical_ifs values ('2009', '3017', 'ge0/0/1', '100g', 1, '100.0.0.33', 24);
insert into physical_ifs values ('2009', '3018', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2009', '3019', 'xe-0/0/1', '40g', null, null, null);
insert into physical_ifs values ('2009', '3020', null, null, null, null, null);
insert into physical_ifs values ('2010', '3017', 'ge0/0/1', '100g', 1, '100.0.0.37', 24);
insert into physical_ifs values ('2010', '3018', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2010', '3019', 'xe-0/0/1', '40g', null, null, null);
insert into physical_ifs values ('2010', '3020', null, null, null, null, null);
insert into physical_ifs values ('2011', '3021', 'ge0/0/1', '100g', 1, '100.0.0.41', 24);
insert into physical_ifs values ('2011', '3022', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2011', '3023', 'xe-0/0/1', '40g', null, null, null);
insert into physical_ifs values ('2011', '3024', null, null, null, null, null);
insert into physical_ifs values ('2012', '3021', 'ge0/0/1', '100g', 1, '100.0.0.45', 24);
insert into physical_ifs values ('2012', '3022', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2012', '3023', 'xe-0/0/1', '40g', null, null, null);
insert into physical_ifs values ('2012', '3024', null, null, null, null, null);
insert into physical_ifs values ('2013', '3025', 'ge0/0/1', '100g', 1, '100.0.0.49', 24);
insert into physical_ifs values ('2013', '3026', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2013', '3027', 'xe-0/0/1', '40g', null, null, null);
insert into physical_ifs values ('2013', '3028', null, null, null, null, null);
insert into physical_ifs values ('2014', '3025', 'ge0/0/1', '100g', 1, '100.0.0.53', 24);
insert into physical_ifs values ('2014', '3026', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2014', '3027', 'xe-0/0/1', '40g', null, null, null);
insert into physical_ifs values ('2014', '3028', null, null, null, null, null);
insert into physical_ifs values ('2015', '3029', 'ge0/0/1', '100g', 1, '100.0.0.57', 24);
insert into physical_ifs values ('2015', '3030', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2015', '3031', 'xe-0/0/1', '40g', null, null, null);
insert into physical_ifs values ('2015', '3032', null, null, null, null, null);
insert into physical_ifs values ('2016', '3029', 'ge0/0/1', '100g', 1, '100.0.0.61', 24);
insert into physical_ifs values ('2016', '3030', 'ge0/0/2', '100g', null, null, null);
insert into physical_ifs values ('2016', '3031', 'ge0/0/3', '100g', null, null, null);
insert into physical_ifs values ('2016', '3032', null, null, null, null, null);




/* LAG Information */
insert into lag_ifs values ('2001', '4001', 'ae4001', 2, '101g', 1, '10.0.0.1', 24);
insert into lag_ifs values ('2002', '4002', 'ae4002', 2, '102g', 1, null, null);
insert into lag_ifs values ('2003', '4003', 'ae4003', 2, '103g', 1, '10.0.0.3', 24);
insert into lag_ifs values ('2004', '4004', 'ae4004', 2, '104g', 1,  null, null);
insert into lag_ifs values ('2005', '4005', 'ae4005', 2, '105g', 1, '10.0.0.5', 24);
insert into lag_ifs values ('2006', '4006', 'ae4006', 2, '106g', 1,  null, null);
insert into lag_ifs values ('2007', '4007', 'ae4007', 2, '107g', 1, '10.0.0.7', 24);
insert into lag_ifs values ('2008', '4008', 'ae4008', 2, '108g', 1,  null, null);
insert into lag_ifs values ('2009', '4009', 'ae4009', 2, '109g', 0, '10.0.0.9', 24);
insert into lag_ifs values ('2010', '4010', 'ae4010', 2, '110g', 2, '10.0.0.10', 24);
insert into lag_ifs values ('2011', '4011', 'ae4011', 2, '111g', 0, '10.0.0.11', 24);
insert into lag_ifs values ('2012', '4012', 'ae4012', 2, '112g', 2, '10.0.0.12', 24);
insert into lag_ifs values ('2013', '4013', 'ae4013', 2, '113g', 0, '10.0.0.13', 24);
insert into lag_ifs values ('2014', '4014', 'ae4014', 2, '114g', 2, '10.0.0.14', 24);
insert into lag_ifs values ('2015', '4015', 'ae4015', 2, '115g', 0, '10.0.0.15', 24);
insert into lag_ifs values ('2016', '4016', 'ae4016', 2, '116g', 2, '10.0.0.16', 24);


/* LAG Member Information */
insert into lag_ifs_member values ('4401', '2001', '4001', '3002', null);
insert into lag_ifs_member values ('4402', '2001', '4001', null, '5001');
insert into lag_ifs_member values ('4403', '2002', '4002', '3002', null);
insert into lag_ifs_member values ('4404', '2002', '4002', null, '5005');
insert into lag_ifs_member values ('4405', '2003', '4003', '3006', null);
insert into lag_ifs_member values ('4406', '2003', '4003', null, '5009');
insert into lag_ifs_member values ('4407', '2004', '4004', '3006', null);
insert into lag_ifs_member values ('4408', '2004', '4004', null, '50013');
insert into lag_ifs_member values ('4409', '2005', '4005', '3010', null);
insert into lag_ifs_member values ('4410', '2005', '4005', null, '50017');
insert into lag_ifs_member values ('4411', '2006', '4006', '3010', null);
insert into lag_ifs_member values ('4412', '2006', '4006', null, '5021');
insert into lag_ifs_member values ('4413', '2007', '4007', '3014', null);
insert into lag_ifs_member values ('4414', '2007', '4007', null, '5025');
insert into lag_ifs_member values ('4415', '2008', '4008', '3014', null);
insert into lag_ifs_member values ('4416', '2008', '4008', null, '5029');
insert into lag_ifs_member values ('4417', '2009', '4009', '3018', null);
insert into lag_ifs_member values ('4418', '2009', '4009', null, '5033');
insert into lag_ifs_member values ('4419', '2010', '4010', '3018', null);
insert into lag_ifs_member values ('4420', '2010', '4010', null, '5037');
insert into lag_ifs_member values ('4421', '2011', '4011', '3022', null);
insert into lag_ifs_member values ('4422', '2011', '4011', null, '5041');
insert into lag_ifs_member values ('4423', '2012', '4012', '3022', null);
insert into lag_ifs_member values ('4424', '2012', '4012', null, '5045');
insert into lag_ifs_member values ('4425', '2013', '4013', '3026', null);
insert into lag_ifs_member values ('4426', '2013', '4013', null, '5049');
insert into lag_ifs_member values ('4427', '2014', '4014', '3026', null);
insert into lag_ifs_member values ('4428', '2014', '4014', null, '5053');
insert into lag_ifs_member values ('4429', '2015', '4015', '3030', null);
insert into lag_ifs_member values ('4430', '2015', '4015', null, '5057');
insert into lag_ifs_member values ('4431', '2016', '4016', '3030', null);
insert into lag_ifs_member values ('4432', '2016', '4016', 3031, null);




/* BreakoutIF Information */
insert into breakout_ifs values ('2001', '5001', '3003', '10g', 'xe-0/0/1:1', 1, '100.2.0.1', 24);
insert into breakout_ifs values ('2001', '5002', '3003', '10g', 'xe-0/0/1:2', 1, '100.2.0.2', 24);
insert into breakout_ifs values ('2001', '5003', '3003', '10g', 'xe-0/0/1:3', null, null, null);
insert into breakout_ifs values ('2001', '5004', '3003', '10g', 'xe-0/0/1:4', null, null, null);
insert into breakout_ifs values ('2002', '5005', '3003', '10g', 'xe-0/0/1:1', 1, '100.2.0.5', 24);
insert into breakout_ifs values ('2002', '5006', '3003', '10g', 'xe-0/0/1:2', 1, '100.2.0.6', 24);
insert into breakout_ifs values ('2002', '5007', '3003', '10g', 'xe-0/0/1:3', null, null, null);
insert into breakout_ifs values ('2002', '5008', '3003', '10g', 'xe-0/0/1:4', null, null, null);
insert into breakout_ifs values ('2003', '5009', '3007', '10g', 'xe-0/0/1:1', 1, '100.2.0.9', 24);
insert into breakout_ifs values ('2003', '5010', '3007', '10g', 'xe-0/0/1:2', 1, '100.2.0.10', 24);
insert into breakout_ifs values ('2003', '5011', '3007', '10g', 'xe-0/0/1:3', null, null, null);
insert into breakout_ifs values ('2003', '5012', '3007', '10g', 'xe-0/0/1:4', null, null, null);
insert into breakout_ifs values ('2004', '5013', '3007', '10g', 'xe-0/0/1:1', 1, '100.2.0.13', 24);
insert into breakout_ifs values ('2004', '5014', '3007', '10g', 'xe-0/0/1:2', 1, '100.2.0.14', 24);
insert into breakout_ifs values ('2004', '5015', '3007', '10g', 'xe-0/0/1:3', null, null, null);
insert into breakout_ifs values ('2004', '5016', '3007', '10g', 'xe-0/0/1:4', null, null, null);
insert into breakout_ifs values ('2005', '5017', '3011', '10g', 'xe-0/0/1:1', 1, '100.2.0.17', 24);
insert into breakout_ifs values ('2005', '5018', '3011', '10g', 'xe-0/0/1:2', 1, '100.2.0.18', 24);
insert into breakout_ifs values ('2005', '5019', '3011', '10g', 'xe-0/0/1:3', null, null, null);
insert into breakout_ifs values ('2005', '5020', '3011', '10g', 'xe-0/0/1:4', null, null, null);
insert into breakout_ifs values ('2006', '5021', '3011', '10g', 'xe-0/0/1:1', 1, '100.2.0.21', 24);
insert into breakout_ifs values ('2006', '5022', '3011', '10g', 'xe-0/0/1:2', 1, '100.2.0.22', 24);
insert into breakout_ifs values ('2006', '5023', '3011', '10g', 'xe-0/0/1:3', null, null, null);
insert into breakout_ifs values ('2006', '5024', '3011', '10g', 'xe-0/0/1:4', null, null, null);
insert into breakout_ifs values ('2007', '5025', '3015', '10g', 'xe-0/0/1:1', 1, '100.2.0.25', 24);
insert into breakout_ifs values ('2007', '5026', '3015', '10g', 'xe-0/0/1:2', 1, '100.2.0.26', 24);
insert into breakout_ifs values ('2007', '5027', '3015', '10g', 'xe-0/0/1:3', null, null, null);
insert into breakout_ifs values ('2007', '5028', '3015', '10g', 'xe-0/0/1:4', null, null, null);
insert into breakout_ifs values ('2008', '5029', '3015', '10g', 'xe-0/0/1:1', 1, '100.2.0.29', 24);
insert into breakout_ifs values ('2008', '5030', '3015', '10g', 'xe-0/0/1:2', 1, '100.2.0.30', 24);
insert into breakout_ifs values ('2008', '5031', '3015', '10g', 'xe-0/0/1:3', null, null, null);
insert into breakout_ifs values ('2008', '5032', '3015', '10g', 'xe-0/0/1:4', null, null, null);
insert into breakout_ifs values ('2009', '5033', '3019', '10g', 'xe-0/0/1:1', 1, '100.2.0.33', 24);
insert into breakout_ifs values ('2009', '5034', '3019', '10g', 'xe-0/0/1:2', 1, '100.2.0.34', 24);
insert into breakout_ifs values ('2009', '5035', '3019', '10g', 'xe-0/0/1:3', null, null, null);
insert into breakout_ifs values ('2009', '5036', '3019', '10g', 'xe-0/0/1:4', null, null, null);
insert into breakout_ifs values ('2010', '5037', '3019', '10g', 'xe-0/0/1:1', 1, '100.2.0.37', 24);
insert into breakout_ifs values ('2010', '5038', '3019', '10g', 'xe-0/0/1:2', 1, '100.2.0.38', 24);
insert into breakout_ifs values ('2010', '5039', '3019', '10g', 'xe-0/0/1:3',  null, null, null);
insert into breakout_ifs values ('2010', '5040', '3019', '10g', 'xe-0/0/1:4',  null, null, null);
insert into breakout_ifs values ('2011', '5041', '3023', '10g', 'xe-0/0/1:1', 1, '100.2.0.41', 24);
insert into breakout_ifs values ('2011', '5042', '3023', '10g', 'xe-0/0/1:2', 1, '100.2.0.42', 24);
insert into breakout_ifs values ('2011', '5043', '3023', '10g', 'xe-0/0/1:3', null, null, null);
insert into breakout_ifs values ('2011', '5044', '3023', '10g', 'xe-0/0/1:4',  null, null, null);
insert into breakout_ifs values ('2012', '5045', '3023', '10g', 'xe-0/0/1:1', 1, '100.2.0.45', 24);
insert into breakout_ifs values ('2012', '5046', '3023', '10g', 'xe-0/0/1:2', 1, '100.2.0.46', 24);
insert into breakout_ifs values ('2012', '5047', '3023', '10g', 'xe-0/0/1:3', null, null, null);
insert into breakout_ifs values ('2012', '5048', '3023', '10g', 'xe-0/0/1:4', null, null, null);
insert into breakout_ifs values ('2013', '5049', '3027', '10g', 'xe-0/0/1:1', 1, '100.2.0.49', 24);
insert into breakout_ifs values ('2013', '5050', '3027', '10g', 'xe-0/0/1:2', 1, '100.2.0.50', 24);
insert into breakout_ifs values ('2013', '5051', '3027', '10g', 'xe-0/0/1:3', null, null, null);
insert into breakout_ifs values ('2013', '5052', '3027', '10g', 'xe-0/0/1:4', null, null, null);
insert into breakout_ifs values ('2014', '5053', '3027', '10g', 'xe-0/0/1:1', 1, '100.2.0.53', 24);
insert into breakout_ifs values ('2014', '5054', '3027', '10g', 'xe-0/0/1:2', 1, '100.2.0.54', 24);
insert into breakout_ifs values ('2014', '5055', '3027', '10g', 'xe-0/0/1:3', null, null, null);
insert into breakout_ifs values ('2014', '5056', '3027', '10g', 'xe-0/0/1:4', null, null, null);
insert into breakout_ifs values ('2015', '5057', '3031', '10g', 'xe-0/0/1:1', 1, '100.2.0.57', 24);
insert into breakout_ifs values ('2015', '5058', '3031', '10g', 'xe-0/0/1:2', 1, '100.2.0.58', 24);
insert into breakout_ifs values ('2015', '5059', '3031', '10g', 'xe-0/0/1:3', null, null, null);
insert into breakout_ifs values ('2015', '5060', '3031', '10g', 'xe-0/0/1:4', null, null, null);
insert into breakout_ifs values ('2016', '5061', '3031', '10g', 'xe-0/0/1:1', 1, '100.2.0.61', 24);
insert into breakout_ifs values ('2016', '5062', '3031', '10g', 'xe-0/0/1:2', 1, '100.2.0.62', 24);
insert into breakout_ifs values ('2016', '5063', '3031', '10g', 'xe-0/0/1:3', null, null, null);
insert into breakout_ifs values ('2016', '5064', '3031', '10g', 'xe-0/0/1:4', null, null, null);




/* VLANIF Information */
insert into vlan_ifs values ('2001', '6001', null, '4001', null, '16001', 'ae6001', 1, '200.0.0.1', 24, null, null, 26001, null, null, null);
insert into vlan_ifs values ('2002', '6002', null, '4002', null, '16002', 'ae6002', 1, null, null, '403:::2', 32, 26002, null, null, null);
insert into vlan_ifs values ('2003', '6003', null, null, '5009', '16003', 'ae6003', 1, '200.0.0.3', 24, null, null, 26003, 1, null, null);
insert into vlan_ifs values ('2004', '6004', '3005', null, null, '16004', 'ae6004', 1, null, null, '403:::4', 32, 26004, 2, null, null);
insert into vlan_ifs values ('2005', '6005', null, '4005', null, '16005', 'ae6005', 1, '200.0.0.5', 24, null, null, 26005, null, 8001, 9001);
insert into vlan_ifs values ('2006', '6006', null, null, '5022', '16006', 'ae6006', 1, null, null, '403:::6', 32, 26006, null, 8002, 9002);
insert into vlan_ifs values ('2007', '6007', '3014', null, null, '16007', 'ae6007', 1, '200.0.0.7', 24, null, null, 26007, null, null, null);
insert into vlan_ifs values ('2008', '6008', null, '4008', null, '16008', 'ae6008', 1, null, null, '403:::8', 32, 26008, 2, null, null);
insert into vlan_ifs values ('2009', '6009', null, null, '5033', '16009', 'ae6009', 1, '200.0.0.9', 24, null, null, 26009, null, null, null);
insert into vlan_ifs values ('2010', '6010', '3017', null, null, '16010', 'ae6010', 1, '200.0.0.10', 24, '403:::10', 32, 26010, null, null, null);
insert into vlan_ifs values ('2011', '6011', null, '4011', null, '16011', 'ae6011', 1, '200.0.0.11', 24, null, null, 26011, 2, null, null);
insert into vlan_ifs values ('2012', '6012', null, null, '5045', '16012', 'ae6012', 1, '200.0.0.12', 24, '403:::12', 32, 26012, 1, null, null);
insert into vlan_ifs values ('2013', '6013', '3025', null, null, '16013', 'ae6013', 1, '200.0.0.13', 24, null, null, 26013, null, 8013, 9013);
insert into vlan_ifs values ('2014', '6014', null, '4014', null, '16014', 'ae6014', 0, '200.0.0.14', 24, '403:::14', 32, 26014, null, null, null);
insert into vlan_ifs values ('2015', '6015', null, null, '5057', '16015', 'ae6015', 1, 'null', 24, null, null, 26015, null, null, null);
insert into vlan_ifs values ('2016', '6016', '3029', null, null, '16016', 'ae6016', 2, '200.0.0.16', 24, '403:::16', 32, 26016, null, null, null);


/* BGP Optional Information */
insert into bgp_option values ('8001', 1, 101, '20.0.0.1', null, '2001', '6001');
insert into bgp_option values ('8002', 2, 102, null, '200:::2', '2002', '6002');
insert into bgp_option values ('8003', 1, 103, '20.0.0.3', null, '2003', '6003');
insert into bgp_option values ('8004', 2, 104, null, '200:::4', '2004', '6004');
insert into bgp_option values ('8005', 1, 105, '20.0.0.5', null, '2005', '6005');
insert into bgp_option values ('8006', 2, 106, null, '200:::6', '2006', '6006');
insert into bgp_option values ('8007', 1, 107, '20.0.0.7', null, '2007', '6007');
insert into bgp_option values ('8008', 2, 108, null, '200:::8', '2008', '6008');
insert into bgp_option values ('8009', 1, 109, '20.0.0.9', null, '2009', '6009');
insert into bgp_option values ('8010', 1, 110, null, '200:::10', '2010', '6010');
insert into bgp_option values ('8011', 2, 111, '20.0.0.11', null, '2011', '6011');
insert into bgp_option values ('8012', 2, 112, null, '200:::12', '2012', '6012');
insert into bgp_option values ('8013', 1, 113, '20.0.0.13', null, '2013', '6013');
insert into bgp_option values ('8014', 1, 114, null, '200:::14', '2014', '6014');
insert into bgp_option values ('8015', 2, 115, '20.0.0.15', null, '2015', '6015');
insert into bgp_option values ('8016', 2, 116, null, null, '2016', '6016');


/* Static Route Optional Information */
insert into static_route_option values ('2001', '6001', 4, '180.0.0.1', 24, '20.0.0.1');
insert into static_route_option values ('2001', '6001', 6, '9001:::1', 32, '20:::1');
insert into static_route_option values ('2002', '6002', 4, '180.0.0.2', 32, '20.0.0.2');
insert into static_route_option values ('2002', '6002', 6, '9001:::2', 32, '20:::2');
insert into static_route_option values ('2003', '6003', 4, '180.0.0.3', 24, '20.0.0.3');
insert into static_route_option values ('2003', '6003', 6, '9001:::3', 32, '20:::3');
insert into static_route_option values ('2004', '6004', 4, '180.0.0.4', 32, '20.0.0.4');
insert into static_route_option values ('2004', '6004', 6, '9001:::4', 32, '20:::4');
insert into static_route_option values ('2005', '6005', 4, '180.0.0.5', 24, '20.0.0.5');
insert into static_route_option values ('2005', '6005', 6, '9001:::5', 32, '20:::5');
insert into static_route_option values ('2006', '6006', 4, '180.0.0.6', 32, '20.0.0.6');
insert into static_route_option values ('2006', '6006', 6, '9001:::6', 32, '20:::6');
insert into static_route_option values ('2007', '6007', 4, '180.0.0.7', 24, '20.0.0.7');
insert into static_route_option values ('2007', '6007', 6, '9001:::7', 32, '20:::7');
insert into static_route_option values ('2008', '6008', 4, '180.0.0.8', 32, '20.0.0.8');
insert into static_route_option values ('2008', '6008', 6, '9001:::8', 32, '20:::8');
insert into static_route_option values ('2009', '6009', 4, '180.0.0.9', 24, '20.0.0.9');
insert into static_route_option values ('2009', '6009', 6, '9001:::9', 32, '20:::9');
insert into static_route_option values ('2010', '6010', 4, '180.0.0.10', 24, '20.0.0.10');
insert into static_route_option values ('2010', '6010', 6, '9001:::10', 32, '20:::10');
insert into static_route_option values ('2011', '6011', 4, '180.0.0.11', 24, '20.0.0.11');
insert into static_route_option values ('2011', '6011', 6, '9001:::11', 32, '20:::11');
insert into static_route_option values ('2012', '6012', 4, '180.0.0.12', 24, '20.0.0.12');
insert into static_route_option values ('2012', '6012', 6, '9000:::12', 32, '20:::12');
insert into static_route_option values ('2013', '6013', 4, '180.0.0.13', 24, '20.0.0.13');
insert into static_route_option values ('2013', '6013', 6, '9001:::13', 32, '20:::13');
insert into static_route_option values ('2014', '6014', 4, '180.0.0.14', 24, '20.0.0.14');
insert into static_route_option values ('2014', '6014', 6, '9001:::14', 32, '20:::14');
insert into static_route_option values ('2015', '6015', 4, '180.0.0.15', 24, '20.0.0.15');
insert into static_route_option values ('2015', '6015', 6, '9001:::15', 32, '20:::15');
insert into static_route_option values ('2016', '6016', 4, '180.0.0.16', 24, '20.0.0.16');
insert into static_route_option values ('2016', '6016', 6, '9001:::16', 32, '20:::16');




/* VRRP Optional Information */
insert into vrrp_option values ('7001', 9001, 1, '10.9.0.1', '9001:::1', '2001', '6001');
insert into vrrp_option values ('7002', 9002, 2, '10.9.0.2', '9001:::2', '2002', '6002');
insert into vrrp_option values ('7003', 9003, 1, '10.9.0.3', '9001:::3', '2003', '6003');
insert into vrrp_option values ('7004', 9004, 2, '10.9.0.4', '9001:::4', '2004', '6004');
insert into vrrp_option values ('7005', 9005, 1, '10.9.0.5', '9001:::5', '2005', '6005');
insert into vrrp_option values ('7006', 9006, 2, '10.9.0.6', '9001:::6', '2006', '6006');
insert into vrrp_option values ('7007', 9007, 1, '10.9.0.7', '9001:::7', '2007', '6007');
insert into vrrp_option values ('7008', 9008, 2, '10.9.0.8', '9001:::8', '2008', '6008');
insert into vrrp_option values ('7009', 9009, 2, '10.9.0.9', '9001:::9', '2009', '6009');
insert into vrrp_option values ('7010', 9010, 1, '10.9.0.10', '9001:::10', '2010', '6010');
insert into vrrp_option values ('7011', 9011, 2, '10.9.0.11', '9001:::11', '2011', '6011');
insert into vrrp_option values ('7012', 9012, 1, '10.9.0.12', '9001:::12', '2012', '6012');
insert into vrrp_option values ('7013', 9013, 2, '10.9.0.13', '9001:::13', '2013', '6013');
insert into vrrp_option values ('7014', 9014, 1, '10.9.0.14', '9001:::14', '2014', '6014');
insert into vrrp_option values ('7015', 9015, 2, '10.9.0.15', '9001:::15', '2015', '6015');
insert into vrrp_option values ('7016', 9016, 1, '10.9.0.16', '9001:::16', '2016', '6016');