CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       full_name VARCHAR(255) NOT NULL,
                       profile_image_url VARCHAR(500),
                       is_enabled BOOLEAN NOT NULL,
                       gender VARCHAR(10),
                       age INT,
                       height FLOAT,
                       weight FLOAT,
                       goal VARCHAR(20),
                       activity_level VARCHAR(20)
);