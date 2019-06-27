CREATE TABLE update_lag_ifs (
    fc_lag_if_id                         text         NOT NULL,
	new_lag_if_id                         text         NOT NULL,
    PRIMARY KEY(fc_lag_if_id)
);

COPY update_lag_ifs (fc_lag_if_id,new_lag_if_id) FROM :DATAPATH WITH CSV;

update lag_ifs set fc_lag_if_id=new_lag_if_id from update_lag_ifs where lag_ifs.fc_lag_if_id = update_lag_ifs.fc_lag_if_id;

drop table update_lag_ifs;