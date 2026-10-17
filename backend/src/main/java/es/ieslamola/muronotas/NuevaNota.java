package es.ieslamola.muronotas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Datos que envía la web para crear una nota (el cuerpo JSON del POST). */
public record NuevaNota(

        @NotBlank(message = "el texto no puede estar vacío")
        @Size(max = 280, message = "el texto no puede pasar de 280 caracteres")
        String texto,

        @NotNull(message = "hay que elegir un color")
        @Pattern(regexp = "amarillo|rosa|verde|azul", message = "el color debe ser amarillo, rosa, verde o azul")
        String color
) {
}
