CREATE TABLE user_info (
    user_id BIGSERIAL PRIMARY KEY,
    username CHARACTER VARYING(30),
    name CHARACTER VARYING(30),
    surname CHARACTER VARYING(30),
    email CHARACTER VARYING(50),
    password CHARACTER VARYING(30),
    user_role CHARACTER VARYING(20)
);