ALTER TABLE transactions
    ADD create_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE transactions
    ADD state_description VARCHAR(255);

ALTER TABLE transactions
    ADD state_type_id INTEGER;

ALTER TABLE transactions
    ALTER COLUMN create_at SET NOT NULL;

ALTER TABLE transactions
    ALTER COLUMN state_type_id SET NOT NULL;

ALTER TABLE transactions
    ADD CONSTRAINT FK_TRANSACTIONS_ON_STATE_TYPE FOREIGN KEY (state_type_id) REFERENCES state_types (id);

ALTER TABLE transaction_states
    DROP CONSTRAINT fk_transaction_states_on_state_type;

ALTER TABLE transaction_states
    DROP CONSTRAINT fk_transaction_states_on_transaction;

DROP TABLE transaction_states CASCADE;