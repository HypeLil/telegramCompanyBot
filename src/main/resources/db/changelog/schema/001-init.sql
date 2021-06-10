CREATE TABLE user (
                         user_id varchar NOT NULL,
                         role varchar NOT NULL DEFAULT 'user',
                         CONSTRAINT pk_user PRIMARY KEY (user_id)
);