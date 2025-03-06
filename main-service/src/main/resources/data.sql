DELETE FROM categories;
DELETE FROM users;
DELETE FROM events;
DELETE FROM participations;
DELETE FROM compilations;
DELETE FROM compilations_events;
DELETE FROM comments;


INSERT INTO categories (name) VALUES ('category1');
INSERT INTO categories (name) VALUES ('category2');
INSERT INTO categories (name) VALUES ('category3');

INSERT INTO users (name, email) VALUES ('user1', 'user1@yandex.ru');
INSERT INTO users (name, email) VALUES ('user2', 'user2@yandex.ru');
INSERT INTO users (name, email) VALUES ('user3', 'user3@yandex.ru');
INSERT INTO users (name, email) VALUES ('user4', 'user4@yandex.ru');

INSERT INTO events (title, annotation, category_id, initiator_id, description, event_date, location_lat, location_lon, paid, participant_limit, request_moderation, state, created_on, published_on) VALUES
('Music Festival',
 'Join us for a day of music and fun with various artists performing live.',
 1,  -- Предполагается, что категория с id = 1 существует
 1,  -- Предполагается, что инициатор с id = 1 существует
 'A day filled with music, food, and fun activities for everyone.',
 '2023-09-15 12:00:00',
 40.7128,
 -74.0060,
 false,
 100,
 true,
 'PENDING',
 '2023-08-01 10:00:00',
 NULL),

('Art Exhibition',
 'Explore the latest art from local artists at this exciting exhibition.',
 2,  -- Предполагается, что категория с id = 2 существует
 2,  -- Предполагается, что инициатор с id = 2 существует
 'An exhibition showcasing local talent and creativity.',
 '2023-09-20 18:00:00',
 34.0522,
 -118.2437,
 false,
 50,
 true,
 'PUBLISHED',
 '2023-08-05 10:00:00',
 '2023-08-10 10:00:00'),

('Tech Conference',
 'A conference about the latest in technology and innovations.',
 3,  -- Предполагается, что категория с id = 3 существует
 3,  -- Предполагается, что инициатор с id = 3 существует
 'Join us for discussions on tech innovations and networking opportunities.',
 '2023-10-01 09:00:00',
 37.7749,
 -122.4194,
 true,
 200,
 true,
 'PENDING',
 '2023-08-10 10:00:00',
 NULL);

INSERT INTO participations (event_id, requester_id, status, created_on) VALUES
(1, 1, 'PENDING', '2023-08-15 10:00:00'),  -- Участник 1 запрашивает участие в событии 1
(1, 2, 'CONFIRMED', '2023-08-16 10:00:00'), -- Участник 2 подтверждает участие в событии 1
(2, 3, 'REJECTED', '2023-08-17 10:00:00'),  -- Участник 3 отклоняет участие в событии 2
(2, 1, 'CANCELED', '2023-08-18 10:00:00'),  -- Участник 1 отменяет участие в событии 2
(3, 2, 'CONFIRMED', '2023-08-19 10:00:00'), -- Участник 2 подтверждает участие в событии 3
(3, 3, 'PENDING', '2023-08-20 10:00:00'),   -- Участник 3 запрашивает участие в событии 3
(1, 4, 'CONFIRMED', '2023-08-21 10:00:00'), -- Участник 4 подтверждает участие в событии 1
(2, 4, 'PENDING', '2023-08-22 10:00:00');   -- Участник 4 запрашивает участие в событии 2

-- Генерация тестовых данных для таблицы compilations
INSERT INTO compilations (title, pinned) VALUES
('Top Events', true),
('Upcoming Events', false),
('Popular Music Festivals', true),
('Art Exhibitions', false),
('Tech Conferences', true),
('Food and Drink Events', false),
('Sports Competitions', true),
('Workshops and Classes', false);

-- Генерация тестовых данных для таблицы compilations_events
INSERT INTO compilations_events (compilation_id, events_id) VALUES
(1, 1),  -- Компиляция 1 связана с событием 1
(1, 2),  -- Компиляция 1 связана с событием 2
(2, 3),  -- Компиляция 2 связана с событием 3
(3, 1),  -- Компиляция 3 связана с событием 1
(3, 2),  -- Компиляция 3 связана с событием 2
(4, 3),  -- Компиляция 4 связана с событием 3
(1, 3);  -- Компиляция 1 также связана с событием 3

-- UPDATE events SET state = 'PUBLISHED' WHERE id = 1;