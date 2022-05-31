ALTER TABLE bank_accounts ALTER COLUMN account_number TYPE BIGINT USING account_number::bigint;

CREATE UNIQUE INDEX account_number_index ON bank_accounts (account_number);