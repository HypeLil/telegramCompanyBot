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

CREATE TABLE chat (
    chat_id varchar(10),
    user_id varchar(20),
    manager_id varchar(20),
    last_message varchar,
    CONSTRAINT pk_chat PRIMARY KEY (chat_id)
);

CREATE TABLE message (
    message_id int,
    chat_id varchar(10),
    text text,
    message_time varchar(50),
    CONSTRAINT pk_message PRIMARY KEY (message_id)
);