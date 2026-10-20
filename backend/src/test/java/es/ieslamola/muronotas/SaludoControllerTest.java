package es.ieslamola.muronotas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SaludoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void saludaDesdeElContenedor() throws Exception {
        mvc.perform(get("/api/saludo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Hola desde el contenedor"))
                .andExpect(jsonPath("$.contenedor").isNotEmpty())
                .andExpect(jsonPath("$.hora").exists());
    }
}
