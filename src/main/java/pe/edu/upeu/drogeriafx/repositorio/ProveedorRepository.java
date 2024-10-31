package pe.edu.upeu.drogeriafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.drogeriafx.modelo.Proveedor;

@Repository
public interface ProveedorRepository  extends JpaRepository<Proveedor, Long> {
}
