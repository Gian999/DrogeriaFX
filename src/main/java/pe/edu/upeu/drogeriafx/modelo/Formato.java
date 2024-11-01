package pe.edu.upeu.drogeriafx.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "formato")
public class Formato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFormato;

    private String nombre;
    private String descripcion;

    @ManyToOne // Relación de muchos-a-uno con TipoFormato
    @JoinColumn(name = "tipo_formato_id", nullable = false) // Ajusta el nombre de la columna según tu BD
    private TipoFormato tipoFormato;

    // Getters y setters

    public Long getIdFormato() {
        return idFormato;
    }

    public void setIdFormato(Long idFormato) {
        this.idFormato = idFormato;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoFormato getTipoFormato() {
        return tipoFormato;
    }

    public void setTipoFormato(TipoFormato tipoFormato) {
        this.tipoFormato = tipoFormato;
    }
}
