CREATE TABLE users (
                         user_id varchar(20),
                         name varchar(50),
                         number varchar(15),
                         email varchar(25),
                         role varchar(2),
                         position varchar(50),
                         order_position varchar(25),
                         online boolean,
                         last_action varchar(50),
                         CONSTRAINT pk_user PRIMARY KEY (user_id)
);