CREATE TABLE task (
    task_id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE,
    created_by BIGINT,
    task_name CHARACTER VARYING(60),
    description CHARACTER VARYING(255)
);