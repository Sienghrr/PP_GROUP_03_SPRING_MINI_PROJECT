-- extension for using uuid and bcrypt
CREATE EXTENSION IF NOT EXISTS "pgcrypto";


DROP TABLE IF EXISTS habit_logs CASCADE;
DROP TABLE IF EXISTS habits CASCADE;
DROP TABLE IF EXISTS app_user_achievements CASCADE;
DROP TABLE IF EXISTS achievements CASCADE;
DROP TABLE IF EXISTS app_users CASCADE;

CREATE DATABASE habit_tracker;

-- create table all parent first and then child
CREATE TABLE app_users (
                           app_user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           username VARCHAR(100) NOT NULL,
                           email VARCHAR(150) NOT NULL UNIQUE,
                           password VARCHAR(255) NOT NULL,
                           level INT NOT NULL DEFAULT 1,
                           xp INT NOT NULL DEFAULT 0,
                           profile_image TEXT,
                           is_verified BOOLEAN NOT NULL DEFAULT FALSE,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE achievements (
                              achievement_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              title VARCHAR(150) NOT NULL,
                              description TEXT,
                              badge VARCHAR(255),
                              xp_required INT NOT NULL
);

CREATE TABLE app_user_achievements (
                                       app_user_achievement_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                       app_user_id UUID NOT NULL,
                                       achievement_id UUID NOT NULL,
                                       CONSTRAINT fk_app_user_achievements_app_user
                                           FOREIGN KEY (app_user_id)
                                               REFERENCES app_users(app_user_id)
                                               ON DELETE CASCADE
                                               ON UPDATE CASCADE,
                                       CONSTRAINT fk_app_user_achievements_achievement
                                           FOREIGN KEY (achievement_id)
                                               REFERENCES achievements(achievement_id)
                                               ON DELETE CASCADE
                                               ON UPDATE CASCADE,
                                       CONSTRAINT uq_app_user_achievement UNIQUE (app_user_id, achievement_id)
);

CREATE TABLE habits (
                        habit_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        title VARCHAR(150) NOT NULL,
                        description TEXT,
                        frequency VARCHAR(50) NOT NULL,
                        is_active BOOLEAN NOT NULL DEFAULT TRUE,
                        app_user_id UUID NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT fk_habits_app_users
                            FOREIGN KEY (app_user_id)
                                REFERENCES app_users(app_user_id)
                                ON DELETE CASCADE
                                ON UPDATE CASCADE
);

CREATE TABLE habit_logs (
                            habit_log_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            log_date DATE NOT NULL,
                            status VARCHAR(50) NOT NULL,
                            xp_earned INT NOT NULL DEFAULT 0,
                            habit_id UUID NOT NULL,
                            CONSTRAINT fk_habit_logs_habits
                                FOREIGN KEY (habit_id)
                                    REFERENCES habits(habit_id)
                                    ON DELETE CASCADE
                                    ON UPDATE CASCADE
);

-- seed data to app_users 100 records
INSERT INTO app_users (username, email, password, level, xp, profile_image, is_verified)
SELECT
    'user_' || i,
    'user_' || i || '@example.com',
    'hashed_password_' || i,
    (RANDOM() * 10)::INT + 1,
    (RANDOM() * 1000)::INT,
    'https://picsum.photos/200?random=' || i,
    (RANDOM() > 0.5)
FROM generate_series(1, 100) AS s(i);

-- seed data to achievements 100 records
INSERT INTO achievements (title, description, badge, xp_required)
SELECT
    'Achievement ' || i,
    'Description for achievement ' || i,
    'badge_' || i || '.png',
    (i * 10)
FROM generate_series(1, 100) AS s(i);

-- seed data to habits 100 records
INSERT INTO habits (title, description, frequency, app_user_id)
SELECT
    'Habit ' || i,
    'Description for habit ' || i,
    CASE
        WHEN i % 3 = 0 THEN 'daily'
        WHEN i % 3 = 1 THEN 'weekly'
        ELSE 'monthly'
        END,
    (SELECT app_user_id FROM app_users ORDER BY RANDOM() LIMIT 1)
FROM generate_series(1, 100) AS s(i);
-- seed data to habit_logs 100 records
INSERT INTO habit_logs (log_date, status, xp_earned, habit_id)
SELECT
            CURRENT_DATE - (RANDOM() * 30)::INT,
            CASE
                WHEN RANDOM() > 0.5 THEN 'completed'
                ELSE 'missed'
                END,
            (RANDOM() * 50)::INT,
            (SELECT habit_id FROM habits ORDER BY RANDOM() LIMIT 1)
FROM generate_series(1, 100) AS s(i);

-- seed data to app_user_achievements 100 records
INSERT INTO app_user_achievements (app_user_id, achievement_id)
SELECT
    (SELECT app_user_id FROM app_users ORDER BY RANDOM() LIMIT 1),
    (SELECT achievement_id FROM achievements ORDER BY RANDOM() LIMIT 1)
FROM generate_series(1, 100)
ON CONFLICT DO NOTHING;