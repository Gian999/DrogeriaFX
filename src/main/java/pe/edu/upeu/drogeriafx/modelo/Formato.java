package pe.edu.upeu.drogeriafx.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_formato")
public class Formato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_formato")
    private Long idFormato;

    @NotNull(message = "El nombre del formato no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @NotNull(message = "La descripción no puede estar vacía")
    @Size(max = 150, message = "La descripción no debe exceder los 150 caracteres")
    @Column(name = "descripcion", length = 150)
    private String descripcion;
}
