CREATE TABLE user_favorit_meals (
                                    id BIGSERIAL PRIMARY KEY,
                                    user_id BIGINT NOT NULL,
                                    meal_id BIGINT NOT NULL,
                                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                    FOREIGN KEY (meal_id) REFERENCES meals(id) ON DELETE CASCADE
);