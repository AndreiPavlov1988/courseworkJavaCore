-- =============================================
-- scripts423.sql
-- JOIN-запросы для школы Хогвартс
-- =============================================

-- =============================================
-- ЗАПРОС 1: Студенты с факультетами
-- =============================================

-- Получить имена и возраст студентов вместе с названиями факультетов
SELECT
    s.name AS student_name,
    s.age AS student_age,
    f.name AS faculty_name
FROM students s
LEFT JOIN faculties f ON s.faculty_id = f.id
ORDER BY s.name;


-- =============================================
-- ЗАПРОС 2: Студенты с аватарками
-- =============================================

-- Получить студентов, у которых есть аватарки
SELECT
    s.id,
    s.name AS student_name,
    s.age AS student_age,
    a.file_path,
    a.media_type,
    a.file_size
FROM students s
JOIN avatars a ON s.id = a.student_id
ORDER BY s.name;


-- =============================================
-- ДОПОЛНИТЕЛЬНЫЕ JOIN-ЗАПРОСЫ
-- =============================================

-- 3. Студенты без факультета
SELECT * FROM students WHERE faculty_id IS NULL;

-- 4. Факультеты без студентов
SELECT f.* FROM faculties f
LEFT JOIN students s ON f.id = s.faculty_id
WHERE s.id IS NULL;

-- 5. Студенты с факультетами и аватарками
SELECT
    s.name AS student_name,
    s.age AS student_age,
    f.name AS faculty_name,
    a.file_path AS avatar_path
FROM students s
LEFT JOIN faculties f ON s.faculty_id = f.id
LEFT JOIN avatars a ON s.id = a.student_id
ORDER BY s.name;

-- 6. Количество студентов на каждом факультете
SELECT
    f.name AS faculty_name,
    COUNT(s.id) AS student_count
FROM faculties f
LEFT JOIN students s ON f.id = s.faculty_id
GROUP BY f.name
ORDER BY student_count DESC;