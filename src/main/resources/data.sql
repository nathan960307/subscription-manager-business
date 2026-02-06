
INSERT INTO subscription (user_id, service_id, price, billing_cycle, next_billing_date, status, auto_renew, created_at, updated_at) VALUES
(1, 1001,  9900.00, 'MONTHLY', '2026-02-10 00:00:00', 'ACTIVE',  true,  NOW(), NOW()),
(1, 1002, 12000.00, 'MONTHLY', '2026-02-15 00:00:00', 'ACTIVE',  true,  NOW(), NOW()),
(2, 2001, 79000.00, 'YEARLY',  '2027-01-01 00:00:00', 'PAUSED',  false, NOW(), NOW());

INSERT INTO subscription_change_history (user_id, subscription_id, change_type, old_value, new_value, changed_at, changed_by, created_at) VALUES
(1, 1, 'STATUS',        'TRIAL',   'ACTIVE',  '2026-02-01 09:00:00', 'SYSTEM', NOW()),
(1, 1, 'PRICE',         '9900',    '12000',   '2026-02-05 10:30:00', 'ADMIN',  NOW()),
(1, 1, 'BILLING_CYCLE', 'MONTHLY', 'YEARLY',  '2026-02-06 14:00:00', 'USER',   NOW()),
(1, 2, 'STATUS',        'ACTIVE',  'PAUSED',  '2026-02-07 18:20:00', 'USER',   NOW()),
(2, 3, 'AUTO_RENEW',    'true',    'false',   '2026-01-15 11:10:00', 'SYSTEM', NOW());

INSERT INTO subscription_billing_history (user_id,subscription_id,billing_period_start,billing_period_end,billing_date,amount,status,created_at)VALUES
(1, 1, '2026-01-01 00:00:00', '2026-01-31 23:59:59', '2026-01-01 10:00:00', 9900.00, 'SUCCESS', NOW()),
(1, 1, '2026-02-01 00:00:00', '2026-02-28 23:59:59', '2026-02-01 10:00:00', 9900.00, 'SUCCESS', NOW()),
(1, 1, '2026-03-01 00:00:00', '2026-03-31 23:59:59', '2026-03-01 10:00:00', 9900.00, 'FAILED',  NOW());
