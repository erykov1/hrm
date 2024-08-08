CREATE TABLE task (
    task_id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE,
    assigned_to BIGINT,
    created_by BIGINT,
    task_status CHARACTER VARYING(30),
    done_at TIMESTAMP WITH TIME ZONE,
    task_name CHARACTER VARYING(60),
    description CHARACTER VARYING(255)
);