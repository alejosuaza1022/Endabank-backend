alter table type_identifier rename to identifier_types;

ALTER TABLE users
    ADD identifier_type_id INTEGER;

ALTER TABLE users
    ALTER COLUMN identifier_type_id SET NOT NULL;

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_IDENTIFIER_TYPE FOREIGN KEY (identifier_type_id) REFERENCES identifier_types (id);

ALTER TABLE users
    DROP CONSTRAINT fk_users_on_type_identifier;

ALTER TABLE users
    DROP COLUMN type_identifier_id;