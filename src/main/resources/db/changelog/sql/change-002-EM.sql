CREATE TABLE task (
    task_id UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE,
    created_by BIGINT,
    task_name CHARACTER VARYING(60),
    description CHARACTER VARYING(255),
    category_id BIGINT,
    CONSTRAINT category_id_fk FOREIGN KEY (category_id)
    REFERENCES category (category_id)
);