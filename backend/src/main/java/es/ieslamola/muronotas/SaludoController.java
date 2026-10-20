package es.ieslamola.muronotas;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/** Saludo de prueba para practicar el ciclo de actualización del despliegue. */
@RestController
public class SaludoController {

    // La ruta empieza por /api/ para que Nginx la reenvíe al backend
    @GetMapping("/api/saludo")
    public Map<String, String> saludo() {
        Map<String, String> saludo = new LinkedHashMap<>();
        saludo.put("mensaje", "Hola desde el contenedor");
        saludo.put("contenedor", nombreDelEquipo());
        saludo.put("hora", LocalDateTime.now().toString()); // formato ISO
        return saludo;
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
