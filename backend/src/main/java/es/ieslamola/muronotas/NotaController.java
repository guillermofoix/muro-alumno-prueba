package es.ieslamola.muronotas;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    private final NotaRepository repositorio;

    public NotaController(NotaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @GetMapping
    public List<Nota> listar() {
        return repositorio.findAllByOrderByCreadaDescIdDesc();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Nota crear(@Valid @RequestBody NuevaNota nueva) {
        return repositorio.save(new Nota(nueva.texto().trim(), nueva.color()));
    }

    @PostMapping("/{id}/like")
    public Nota darLike(@PathVariable Long id) {
        Nota nota = repositorio.findById(id).orElseThrow(() -> new NotaNoEncontradaException(id));
        nota.sumarLike();
        return repositorio.save(nota);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borrar(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            throw new NotaNoEncontradaException(id);
        }
        repositorio.deleteById(id);
    }
}
