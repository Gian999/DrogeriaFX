package pe.edu.upeu.drogeriafx.modelo;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "tipo_formato")
public class TipoFormato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFormato;

    private String nombre;

    // Getters y setters

//    public Long getIdFormato() {
//        return idFormato;
//    }
//
//    public void setIdFormato(Long idFormato) {
//        this.idFormato = idFormato;
//    }
//
//    public String getNombre() {
//        return nombre;
//    }
//
//    public void setNombre(String nombre) {
//        this.nombre = nombre;
//    }
}
