CREATE TABLE usr (
                         usr_id varchar NOT NULL,
                         name varchar,
                         number varchar,
                         email varchar,
                         role varchar,
                         position varchar,
                         order_position varchar,
                         online boolean,
                         last_action varchar,
                         CONSTRAINT pk_user PRIMARY KEY (usr_id)
);