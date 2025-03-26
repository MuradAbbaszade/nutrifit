CREATE TABLE `user_meal` (
                             `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                             `user_id` BIGINT NOT NULL,
                             `meal_id` BIGINT NOT NULL,
                             `date` DATE NOT NULL,
                             FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
                             FOREIGN KEY (`meal_id`) REFERENCES `meals`(`id`) ON DELETE CASCADE
);
