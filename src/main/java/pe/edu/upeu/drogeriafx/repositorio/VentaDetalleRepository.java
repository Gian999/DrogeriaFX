package pe.edu.upeu.drogeriafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.drogeriafx.modelo.VentaDetalle;

@Repository
public interface VentaDetalleRepository  extends JpaRepository<VentaDetalle, Long> {
}
