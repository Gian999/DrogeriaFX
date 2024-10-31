package pe.edu.upeu.drogeriafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.drogeriafx.modelo.Venta;

@Repository
public interface VentaRepository  extends JpaRepository<Venta, Long> {
}
