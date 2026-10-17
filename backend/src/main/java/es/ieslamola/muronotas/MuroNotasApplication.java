package es.ieslamola.muronotas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MuroNotasApplication {

    // Variables sin valor por defecto: sin ellas no tiene sentido arrancar
    private static final String[] VARIABLES_OBLIGATORIAS = {"DB_HOST", "DB_PASSWORD"};

    public static void main(String[] args) {
        // Con el perfil "local" se usa H2 en memoria y no hacen falta las variables de la BD
        if (!perfilLocalActivo(args)) {
            comprobarVariablesObligatorias();
        }
        SpringApplication.run(MuroNotasApplication.class, args);
    }

    private static void comprobarVariablesObligatorias() {
        for (String variable : VARIABLES_OBLIGATORIAS) {
            String valor = System.getenv(variable);
            if (valor == null || valor.isBlank()) {
                System.err.println("ERROR: falta la variable de entorno " + variable + ".");
                System.err.println("Ponla en el fichero .env (copia .env.example) y vuelve a arrancar.");
                System.exit(1);
            }
        }
    }

    /** Detecta si el perfil activo es "local" (argumento, propiedad -D o variable de entorno). */
    private static boolean perfilLocalActivo(String[] args) {
        String perfiles = System.getProperty("spring.profiles.active", System.getenv("SPRING_PROFILES_ACTIVE"));
        for (String arg : args) {
            if (arg.startsWith("--spring.profiles.active=")) {
                perfiles = arg.substring("--spring.profiles.active=".length());
            }
        }
        if (perfiles == null) {
            return false;
        }
        for (String perfil : perfiles.split(",")) {
            if (perfil.trim().equals("local")) {
                return true;
            }
        }
        return false;
    }
}
