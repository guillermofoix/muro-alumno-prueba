package es.ieslamola.muronotas;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaRepository extends JpaRepository<Nota, Long> {

    // Spring genera la consulta a partir del nombre: más recientes primero
    // (el id desempata si dos notas se crean en el mismo instante)
    List<Nota> findAllByOrderByCreadaDescIdDesc();
}
