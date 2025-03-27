CREATE TABLE `meals` (
                         `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                         `name` VARCHAR(255) NOT NULL,
                         `protein` DECIMAL(10,2) NOT NULL,
                         `fat` DECIMAL(10,2) NOT NULL,
                         `sugar` DECIMAL(10,2) NOT NULL,
                         `carbs` DECIMAL(10,2) NOT NULL,
                         `description` TEXT,
                         `type` VARCHAR(20),
                         `image` VARCHAR(500)
);