package com.duoc.recetas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Test de carga del contexto de Spring Boot")
class RecetasApplicationTests {

    @Test
    @DisplayName("El contexto de Spring Boot carga correctamente con H2")
    void contextLoads() {
        // Verifica que todos los beans se inicializan sin errores
    }
}
