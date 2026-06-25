-- =============================================
-- scripts422.sql
-- Проектирование таблиц: Человек и Машина
-- =============================================

-- 1. Таблица "Человек" (Person)
CREATE TABLE persons (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT CHECK (age >= 18),  -- минимальный возраст для вождения
    has_driver_license BOOLEAN DEFAULT FALSE
);

-- 2. Таблица "Машина" (Car)
CREATE TABLE cars (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    price DECIMAL(12, 2) NOT NULL CHECK (price >= 0)
);

-- 3. Связующая таблица "Владение машиной" (Person_Car)
-- Несколько человек могут пользоваться одной машиной
CREATE TABLE person_car (
    person_id INT REFERENCES persons(id) ON DELETE CASCADE,
    car_id INT REFERENCES cars(id) ON DELETE CASCADE,
    PRIMARY KEY (person_id, car_id)
);


-- =============================================
-- ТЕСТОВЫЕ ДАННЫЕ
-- =============================================

-- Добавляем людей
INSERT INTO persons (name, age, has_driver_license) VALUES
('Гарри Поттер', 17, FALSE),
('Гермиона Грейнджер', 17, TRUE),
('Рон Уизли', 17, FALSE),
('Драко Малфой', 16, TRUE),
('Невилл Долгопупс', 17, FALSE);

-- Добавляем машины
INSERT INTO cars (brand, model, price) VALUES
('Ford', 'Focus', 1500000),
('Toyota', 'Camry', 2500000),
('BMW', 'X5', 4500000),
('Audi', 'A4', 3500000);

-- Связываем людей с машинами
INSERT INTO person_car (person_id, car_id) VALUES
(1, 1),  -- Гарри водит Ford Focus
(1, 2),  -- Гарри также водит Toyota Camry
(2, 2),  -- Гермиона водит Toyota Camry
(3, 3),  -- Рон водит BMW X5
(4, 4);  -- Драко водит Audi A4


-- =============================================
-- ПРОВЕРОЧНЫЕ ЗАПРОСЫ
-- =============================================

-- 1. Все люди и их машины
SELECT p.name AS person_name, c.brand, c.model, c.price
FROM persons p
JOIN person_car pc ON p.id = pc.person_id
JOIN cars c ON pc.car_id = c.id;

-- 2. Все люди, у которых есть права
SELECT * FROM persons WHERE has_driver_license = TRUE;

-- 3. Все машины стоимостью больше 2 000 000
SELECT * FROM cars WHERE price > 2000000;