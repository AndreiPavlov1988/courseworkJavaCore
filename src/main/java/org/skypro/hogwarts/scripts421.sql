-- =============================================
-- scripts421.sql
-- Ограничения для таблиц Student и Faculty
-- =============================================

-- 1. Возраст студента не может быть меньше 16 лет
ALTER TABLE students
ADD CONSTRAINT age_check CHECK (age >= 16);

-- 2. Имена студентов уникальны и не равны NULL
ALTER TABLE students
ADD CONSTRAINT name_not_null CHECK (name IS NOT NULL);

ALTER TABLE students
ADD CONSTRAINT name_unique UNIQUE (name);

-- 3. Пара "название" - "цвет факультета" должна быть уникальной
ALTER TABLE faculties
ADD CONSTRAINT faculty_name_color_unique UNIQUE (name, color);

-- 4. При создании студента без возраста автоматически присваивается 20 лет
ALTER TABLE students
ALTER COLUMN age SET DEFAULT 20;


-- =============================================
-- ДОПОЛНИТЕЛЬНО: проверка существующих данных
-- =============================================

-- Проверка, что все студенты старше 16 лет
SELECT * FROM students WHERE age < 16;

-- Проверка, что нет студентов с NULL именем
SELECT * FROM students WHERE name IS NULL;

-- Проверка, что нет дубликатов имен
SELECT name, COUNT(*) FROM students GROUP BY name HAVING COUNT(*) > 1;

-- Проверка, что нет дубликатов (name, color) у факультетов
SELECT name, color, COUNT(*) FROM faculties
GROUP BY name, color HAVING COUNT(*) > 1;