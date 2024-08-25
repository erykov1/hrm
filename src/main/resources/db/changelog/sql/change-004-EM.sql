CREATE TABLE assignment_note (
    note_id UUID PRIMARY KEY,
    note_content VARCHAR(255) NOT NULL,
    assignment_id BIGINT,
    CONSTRAINT assignment_id_fk FOREIGN KEY (assignment_id)
    REFERENCES assignment (assignment_id)
);