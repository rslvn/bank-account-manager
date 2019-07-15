CREATE TABLE customer (
	id bigserial NOT NULL PRIMARY KEY,
	first_name text NOT NULL,
	last_name text NOT NULL,
	initial text NOT NULL,
    phone_number text NULL,
	email text NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL
);

CREATE TABLE bank_account (
	id bigserial NOT NULL PRIMARY KEY,
	iban text NULL,
	current_balance decimal NOT NULL,
	customer_id bigint,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL
);

CREATE TABLE card (
	id bigserial NOT NULL PRIMARY KEY,
	card_type text NOT NULL,
	number text NOT NULL,
	holder_name text NOT NULL,
    expiry_date text NOT NULL,
	cvv text NULL,
	bank_account_id bigint,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL
);

CREATE TABLE transaction_history (
	id bigserial NOT NULL PRIMARY KEY,
	type text NOT NULL,
	statement_type text NOT NULL,
	customer_id bigint NOT NULL,
    bank_account_id bigint NOT NULL,
	card_id bigint NOT NULL,
	amount decimal,
	fee decimal,
	total_amount decimal,
	before_balance decimal,
	after_balance decimal,
	correlation_id bigint NULL,
	status text NOT NULL,
	failing_reason text NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL
);

ALTER TABLE bank_account
    ADD CONSTRAINT fk_bank_account_customer_id FOREIGN KEY (customer_id) REFERENCES customer(id);

ALTER TABLE card
    ADD CONSTRAINT fk_card_bank_account_id FOREIGN KEY (bank_account_id) REFERENCES bank_account(id);
