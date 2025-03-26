CREATE TABLE `users` (
                        `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                        `username` varchar(255) NOT NULL ,
                        `password` varchar(255) NOT NULL ,
                        `full_name` varchar(255) NOT NULL ,
                        `profile_image_url` varchar(500),
                        `is_enabled` boolean NOT NULL
);

