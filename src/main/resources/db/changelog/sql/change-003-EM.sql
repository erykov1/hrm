CREATE TABLE assignment (
    assignment_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    object_id UUID NOT NULL,
    assigned_at TIMESTAMP WITH TIME ZONE,
    done_at TIMESTAMP WITH TIME ZONE,
    assignment_created_by BIGINT NOT NULL,
    assignment_status VARCHAR(30) NOT NULL,
    CONSTRAINT unique_user_object UNIQUE (user_id, object_id)
);