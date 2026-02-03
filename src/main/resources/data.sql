
INSERT INTO subscription (user_id, service_id, price, billing_cycle, next_billing_date, status, auto_renew, created_at, updated_at) VALUES
(1, 1001,  9900.00, 'MONTHLY', '2026-02-10 00:00:00', 'ACTIVE',  true,  NOW(), NOW()),
(1, 1002, 12000.00, 'MONTHLY', '2026-02-15 00:00:00', 'ACTIVE',  true,  NOW(), NOW()),
(2, 2001, 79000.00, 'YEARLY',  '2027-01-01 00:00:00', 'PAUSED',  false, NOW(), NOW());
