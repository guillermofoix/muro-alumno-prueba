package es.ieslamola.muronotas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NotaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private NotaRepository repositorio;

    @BeforeEach
    void vaciarMuro() {
        repositorio.deleteAll();
    }

    @Test
    void creaUnaNotaValida() throws Exception {
        mvc.perform(post("/api/notas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"texto\":\"Hola, muro\",\"color\":\"amarillo\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.texto").value("Hola, muro"))
                .andExpect(jsonPath("$.color").value("amarillo"))
                .andExpect(jsonPath("$.likes").value(0))
                .andExpect(jsonPath("$.creada").exists());
    }

    @Test
    void rechazaUnaNotaConTextoVacio() throws Exception {
        mvc.perform(post("/api/notas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"texto\":\"   \",\"color\":\"rosa\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(containsString("el texto no puede estar vacío")));

        assertThat(repositorio.count()).isZero();
    }

    @Test
    void listaLasNotasMasRecientesPrimero() throws Exception {
        repositorio.save(new Nota("primera", "verde"));
        repositorio.save(new Nota("segunda", "azul"));

        mvc.perform(get("/api/notas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].texto").value("segunda"))
                .andExpect(jsonPath("$[1].texto").value("primera"));
    }

    @Test
    void sumaUnLike() throws Exception {
        Nota nota = repositorio.save(new Nota("dame like", "rosa"));

        mvc.perform(post("/api/notas/{id}/like", nota.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likes").value(1));
    }

    @Test
    void borraUnaNota() throws Exception {
        Nota nota = repositorio.save(new Nota("bórrame", "azul"));

        mvc.perform(delete("/api/notas/{id}", nota.getId()))
                .andExpect(status().isNoContent());

        assertThat(repositorio.existsById(nota.getId())).isFalse();
    }

    @Test
    void likeEnNotaInexistenteDevuelve404() throws Exception {
        mvc.perform(post("/api/notas/{id}/like", 999999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("no existe ninguna nota con id 999999"));
    }
}
