INSERT INTO public.customer(id, first_name, last_name,initial,created_at, updated_at)
VALUES
(1,'NoBankAccount', 'Surname1','N.',current_timestamp,current_timestamp),
(2,'HasBankAccount', 'Debit','H.',current_timestamp,current_timestamp),
(3,'HasBankAccount', 'Credit','H.',current_timestamp,current_timestamp),
(4,'HasNoLimit', 'Debit','H.',current_timestamp,current_timestamp),
(5,'HasNoLimit', 'Credit','H.',current_timestamp,current_timestamp);

INSERT INTO public.bank_account(id, customer_id, iban, current_balance,created_at, updated_at)
VALUES
(1,2, 'IBAN1', 100.0 , current_timestamp,current_timestamp),
(2,3, null, 100.0 , current_timestamp,current_timestamp),
(3,4, 'IBAN1', 5.0 , current_timestamp,current_timestamp),
(4,5, null, 5.0 , current_timestamp,current_timestamp);

INSERT INTO public.card(id, bank_account_id,card_type, number, holder_name, expiry_date,cvv,created_at, updated_at)
VALUES
(1,1, 'DEBIT_CARD','1111222233334444', 'H. HasBankAccount','12/23','123' , current_timestamp,current_timestamp),
(2,2, 'CREDIT_CARD','5555222233334444', 'H. HasBankAccount','12/23','321' , current_timestamp,current_timestamp),
(3,3, 'DEBIT_CARD','1111222255554444', 'H. HasBankAccount','12/23','123' , current_timestamp,current_timestamp),
(4,4, 'CREDIT_CARD','5555555533334444', 'H. HasBankAccount','12/23','321' , current_timestamp,current_timestamp);
