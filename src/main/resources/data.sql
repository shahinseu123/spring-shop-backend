INSERT INTO roles (name)
VALUES ('ADMIN'),

       ('USER') ON CONFLICT (name) DO NOTHING;
INSERT INTO users (name, email, role_id, delete_status)
VALUES ('ADMIN', 'admin@test.com', 1) ON CONFLICT
(email) DO UPDATE SET name = EXCLUDED.name, email = EXCLUDED.email, role_id = EXCLUDED.role_id, delete_status = 'NO';

INSERT INTO user_credential(username, password, client_secret, user_id)
VALUES ('admin@test.com', '$2a$10$aEzMMya0wg9qUO0rf0o0tuHHjuzue/qnHfPXLV0ksXpqz5tCDndOa',
        'Z7ibJeEZsHIKWEW0L9cDJEFXy1FWAyVp51FVEHKEqgQ5fpbZa7vrSqTGWibxu2dA', 1) ON CONFLICT (username)
        DO NOTHING;