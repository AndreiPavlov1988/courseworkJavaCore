package org.skypro.hogwarts;  // ← ДОЛЖЕН СОВПАДАТЬ С ПУТЕМ!

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HogwartsApplication {  // ← ИМЯ ДОЛЖНО СОВПАДАТЬ

    public static void main(String[] args) {
        SpringApplication.run(HogwartsApplication.class, args);  // ← ТО ЖЕ ИМЯ
    }
}