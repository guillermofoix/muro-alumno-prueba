package es.ieslamola.muronotas;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/** Un post-it del muro. Cada objeto es una fila de la tabla "notas". */
@Entity
@Table(name = "notas")
public class Nota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 280)
    private String texto;

    @Column(nullable = false, length = 10)
    private String color;

    @Column(nullable = false)
    private int likes = 0;

    @Column(nullable = false)
    private Instant creada = Instant.now();

    // JPA necesita un constructor vacío
    protected Nota() {
    }

    public Nota(String texto, String color) {
        this.texto = texto;
        this.color = color;
    }

    public void sumarLike() {
        likes++;
    }

    public Long getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public String getColor() {
        return color;
    }

    public int getLikes() {
        return likes;
    }

    public Instant getCreada() {
        return creada;
    }
}
