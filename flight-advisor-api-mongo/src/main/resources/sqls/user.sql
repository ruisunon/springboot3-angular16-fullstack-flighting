DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    username       VARCHAR(50) PRIMARY KEY,
    password       VARCHAR(250) NOT NULL,
    user_role      VARCHAR(50) NOT NULL,
    first_name     VARCHAR(100) NOT NULL,
    last_name      VARCHAR(100) NOT NULL,
    email          VARCHAR(100) NOT NULL UNIQUE,
    avatar         VARCHAR,
    secret         VARCHAR,
    account_enabled BOOLEAN NOT NULL DEFAULT true,
    account_expired BOOLEAN NOT NULL DEFAULT false,
    account_locked BOOLEAN NOT NULL DEFAULT false,
    credentials_expired BOOLEAN NOT NULL DEFAULT false,
    use_2fa BOOLEAN NOT NULL DEFAULT false
);

INSERT INTO users (username, password, user_role, first_name, last_name, email) VALUES ('bob', '$2a$10$esolmUvFZDqSIE744dU5V.5dPxBk0.xzjDXe7Gim4tou7DXYBLa4q', 'USER', 'Bob', 'Doe', 'bob@doe.com');
INSERT INTO users (username, password, user_role, first_name, last_name, email) VALUES ('alice', '$2a$10$esolmUvFZDqSIE744dU5V.5dPxBk0.xzjDXe7Gim4tou7DXYBLa4q', 'ADMIN', 'Alice', 'Doe', 'alice@doe.com');
COMMIT;