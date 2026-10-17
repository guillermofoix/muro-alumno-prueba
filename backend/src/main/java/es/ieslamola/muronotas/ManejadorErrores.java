package es.ieslamola.muronotas;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/** Convierte los errores en respuestas JSON sencillas: {"error": "mensaje claro"}. */
@RestControllerAdvice
public class ManejadorErrores {

    // Falla la validación de NuevaNota -> 400 con todos los motivos
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> datosNoValidos(MethodArgumentNotValidException e) {
        String motivos = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining("; "));
        return Map.of("error", motivos);
    }

    // El cuerpo no es un JSON válido -> 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> jsonNoValido() {
        return Map.of("error", "el cuerpo de la petición no es un JSON válido");
    }

    // La nota no existe -> 404
    @ExceptionHandler(NotaNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notaNoEncontrada(NotaNoEncontradaException e) {
        return Map.of("error", e.getMessage());
    }
}
