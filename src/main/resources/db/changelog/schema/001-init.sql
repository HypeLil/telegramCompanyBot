CREATE TABLE usr (
                         usr_id varchar NOT NULL,
                         name varchar,
                         number varchar,
                         email varchar,
                         role varchar,
                         position varchar,
                         order_position varchar,
                         CONSTRAINT pk_user PRIMARY KEY (usr_id)
);