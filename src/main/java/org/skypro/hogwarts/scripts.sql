-- 1. Получить всех студентов, возраст которых между 10 и 20
SELECT * FROM students
WHERE age BETWEEN 10 AND 20;

-- 2. Получить всех студентов, но отобразить только их имена
SELECT name FROM students;

-- 3. Получить всех студентов, у которых в имени присутствует буква 'О'
SELECT * FROM students
WHERE LOWER(name) LIKE '%о%';

-- 4. Получить всех студентов, у которых возраст меньше идентификатора
SELECT * FROM students
WHERE age < id;

-- 5. Получить всех студентов, упорядоченных по возрасту
SELECT * FROM students
ORDER BY age;

-- Дополнительно: по убыванию
SELECT * FROM students
ORDER BY age DESC;

-- 1. Количество всех студентов в школе
SELECT COUNT(*) FROM students;

-- 2. Средний возраст студентов
SELECT AVG(age) FROM students;

-- 3. Получить последних 5 студентов (по максимальному ID)
SELECT * FROM students ORDER BY id DESC LIMIT 5;

-- =============================================
-- Создание таблицы для аватарок (если её нет)
-- =============================================

CREATE TABLE IF NOT EXISTS avatars (
    id SERIAL PRIMARY KEY,
    file_path VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    media_type VARCHAR(100) NOT NULL,
    data BYTEA,
    student_id INTEGER UNIQUE,
    CONSTRAINT fk_avatar_student FOREIGN KEY (student_id) REFERENCES students(id)
);