package es.ieslamola.muronotas;

/** Se lanza cuando piden una nota que no existe. ManejadorErrores la convierte en un 404. */
public class NotaNoEncontradaException extends RuntimeException {

    public NotaNoEncontradaException(Long id) {
        super("no existe ninguna nota con id " + id);
    }
}
