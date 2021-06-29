CREATE TABLE usrs (
                         user_id varchar(20),
                         name varchar(50),
                         number varchar(15),
                         email varchar(25),
                         role varchar(2),
                         position varchar(50),
                         order_position varchar(25),
                         online boolean,
                         last_action TIMESTAMP,
                         CONSTRAINT pk_user PRIMARY KEY (user_id)
);

CREATE TABLE chat (
    chat_id serial,
    user_id varchar(20),
    manager_id varchar(20),
    answered boolean,
    last_message TIMESTAMP,
    CONSTRAINT pk_chat PRIMARY KEY (chat_id)
);

CREATE TABLE message (
    message_id serial,
    chat_id int,
    user_id varchar(20),
    text text,
    message_time TIMESTAMP,
    CONSTRAINT pk_message PRIMARY KEY (message_id)
);