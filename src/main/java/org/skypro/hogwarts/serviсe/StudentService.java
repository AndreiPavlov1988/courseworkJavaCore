package org.skypro.hogwarts.service;

import org.skypro.hogwarts.exception.StudentNotFoundException;
import org.skypro.hogwarts.model.Faculty;
import org.skypro.hogwarts.model.Student;
import org.skypro.hogwarts.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        logger.info("Was invoked method for create student: {}", student.getName());
        logger.debug("Student details: name={}, age={}", student.getName(), student.getAge());
        Student savedStudent = studentRepository.save(student);
        logger.info("Student created with id: {}", savedStudent.getId());
        return savedStudent;
    }

    public Student getStudent(Long id) {
        logger.info("Was invoked method for get student by id: {}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is no student with id = {}", id);
                    return new StudentNotFoundException(id);
                });
    }

    public List<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        List<Student> students = studentRepository.findAll();
        logger.debug("Found {} students", students.size());
        return students;
    }

    public List<Student> getStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for get students by age between {} and {}", minAge, maxAge);
        if (minAge > maxAge) {
            logger.warn("Invalid age range: minAge={} > maxAge={}", minAge, maxAge);
            throw new IllegalArgumentException("minAge должен быть меньше maxAge");
        }
        List<Student> students = studentRepository.findByAgeBetween(minAge, maxAge);
        logger.debug("Found {} students in age range", students.size());
        return students;
    }

    public Student updateStudent(Student student) {
        logger.info("Was invoked method for update student with id: {}", student.getId());
        Student updatedStudent = studentRepository.save(student);
        logger.info("Student updated with id: {}", updatedStudent.getId());
        return updatedStudent;
    }

    public void deleteStudent(Long id) {
        logger.info("Was invoked method for delete student with id: {}", id);
        if (!studentRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existent student with id: {}", id);
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
        logger.info("Student with id {} deleted successfully", id);
    }

    public Faculty getStudentFaculty(Long studentId) {
        logger.info("Was invoked method for get faculty by student id: {}", studentId);
        Student student = getStudent(studentId);
        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            logger.warn("Student with id {} has no faculty", studentId);
        } else {
            logger.debug("Student {} belongs to faculty {}", student.getName(), faculty.getName());
        }
        return faculty;
    }

    public long getStudentsCount() {
        logger.info("Was invoked method for get students count");
        long count = studentRepository.countAllStudents();
        logger.debug("Total students count: {}", count);
        return count;
    }

    public double getAverageAge() {
        logger.info("Was invoked method for get average age of students");
        double avg = studentRepository.getAverageAge();
        logger.debug("Average age of students: {}", avg);
        return avg;
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        List<Student> students = studentRepository.findLastFiveStudents();
        logger.debug("Retrieved {} last students", students.size());
        return students;
    }
    public List<String> getStudentNamesStartingWithA() {
        logger.info("Was invoked method for get student names starting with 'A'");

        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name != null && !name.isEmpty())
                .filter(name -> name.toUpperCase().startsWith("А"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAverageAgeStream() {
        logger.info("Was invoked method for get average age using Stream API");

        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }
    public void printStudentsParallel() {
        logger.info("Was invoked method for print students in parallel mode");

        List<Student> students = studentRepository.findAll();

        // Проверяем, что студентов достаточно (минимум 6)
        if (students.size() < 6) {
            logger.warn("Not enough students for parallel printing. Found: {}", students.size());
            System.out.println("Недостаточно студентов для демонстрации. Нужно минимум 6.");
            return;
        }

        // Получаем первых 6 студентов
        List<Student> firstSix = students.stream().limit(6).collect(Collectors.toList());

        // Поток 1: первые два имени в основном потоке
        System.out.println("=== Основной поток (первые 2 студента) ===");
        printStudentName(firstSix.get(0), "Основной");
        printStudentName(firstSix.get(1), "Основной");

        // Поток 2: имена третьего и четвертого студента в параллельном потоке
        Thread thread1 = new Thread(() -> {
            System.out.println("=== Параллельный поток 1 (3-й и 4-й студенты) ===");
            printStudentName(firstSix.get(2), "Поток-1");
            printStudentName(firstSix.get(3), "Поток-1");
        });

        // Поток 3: имена пятого и шестого студента в еще одном параллельном потоке
        Thread thread2 = new Thread(() -> {
            System.out.println("=== Параллельный поток 2 (5-й и 6-й студенты) ===");
            printStudentName(firstSix.get(4), "Поток-2");
            printStudentName(firstSix.get(5), "Поток-2");
        });

        // Запускаем потоки
        thread1.start();
        thread2.start();

        // Ждем завершения потоков
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            logger.error("Thread interrupted", e);
            Thread.currentThread().interrupt();
        }

        System.out.println("=== Все потоки завершены ===");
    }

    private void printStudentName(Student student, String threadName) {
        System.out.println(threadName + ": " + student.getName() + " (ID: " + student.getId() + ")");
    }

    private final Object printLock = new Object();  // Объект для синхронизации

    // Синхронизированный метод для вывода имени
    private void synchronizedPrintStudentName(Student student, String threadName) {
        synchronized (printLock) {
            System.out.println(threadName + ": " + student.getName() + " (ID: " + student.getId() + ")");
            // Небольшая задержка для наглядности (опционально)
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // НОВЫЙ МЕТОД ДЛЯ ШАГА 2
    public void printStudentsSynchronized() {
        logger.info("Was invoked method for print students in synchronized mode");

        List<Student> students = studentRepository.findAll();

        // Проверяем, что студентов достаточно (минимум 6)
        if (students.size() < 6) {
            logger.warn("Not enough students for synchronized printing. Found: {}", students.size());
            System.out.println("Недостаточно студентов для демонстрации. Нужно минимум 6.");
            return;
        }

        // Получаем первых 6 студентов
        List<Student> firstSix = students.stream().limit(6).collect(Collectors.toList());

        // Создаем объект для синхронизации
        Object lock = new Object();

        // Поток 1: первые два имени в основном потоке
        System.out.println("=== Основной поток (синхронизированный) ===");
        synchronizedPrintStudentName(firstSix.get(0), "Основной");
        synchronizedPrintStudentName(firstSix.get(1), "Основной");

        // Поток 2: имена третьего и четвертого студента в параллельном потоке
        Thread thread1 = new Thread(() -> {
            System.out.println("=== Параллельный поток 1 (синхронизированный) ===");
            synchronizedPrintStudentName(firstSix.get(2), "Поток-1");
            synchronizedPrintStudentName(firstSix.get(3), "Поток-1");
        });

        // Поток 3: имена пятого и шестого студента в еще одном параллельном потоке
        Thread thread2 = new Thread(() -> {
            System.out.println("=== Параллельный поток 2 (синхронизированный) ===");
            synchronizedPrintStudentName(firstSix.get(4), "Поток-2");
            synchronizedPrintStudentName(firstSix.get(5), "Поток-2");
        });

        // Запускаем потоки
        thread1.start();
        thread2.start();

        // Ждем завершения потоков
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            logger.error("Thread interrupted", e);
            Thread.currentThread().interrupt();
        }

        System.out.println("=== Все синхронизированные потоки завершены ===");
    }
}

