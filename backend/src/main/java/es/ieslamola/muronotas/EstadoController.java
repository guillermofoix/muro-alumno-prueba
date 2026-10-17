package es.ieslamola.muronotas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.util.LinkedHashMap;
import java.util.Map;

/** Estado del sistema para la franja superior de la web. Siempre responde 200. */
@RestController
public class EstadoController {

    private final JdbcTemplate jdbc;
    private final String version;

    public EstadoController(JdbcTemplate jdbc, @Value("${app.version}") String version) {
        this.jdbc = jdbc;
        this.version = version;
    }

    @GetMapping("/api/estado")
    public Map<String, String> estado() {
        Map<String, String> estado = new LinkedHashMap<>();
        estado.put("api", "ok");
        estado.put("bd", comprobarBaseDeDatos());
        estado.put("contenedor", nombreDelEquipo());
        estado.put("version", version);
        return estado;
    }

    // Hace una consulta real: si la BD no contesta, devolvemos "error" en vez de fallar
    private String comprobarBaseDeDatos() {
        try {
            jdbc.queryForObject("SELECT 1", Integer.class);
            return "ok";
        } catch (Exception e) {
            return "error";
        }
    }

    // Dentro de Docker, el hostname es el id corto del contenedor
    private String nombreDelEquipo() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "desconocido";
        }
    }
}
