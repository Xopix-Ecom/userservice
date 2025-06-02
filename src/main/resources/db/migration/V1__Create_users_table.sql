-- V1__Create_users_table.sql
CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY NOT NULL, -- UUIDs for primary keys
    auth0_user_id VARCHAR(255) UNIQUE NULL, -- External ID from Auth0
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- Hashed password
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
);