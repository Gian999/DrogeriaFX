package pe.edu.upeu.drogeriafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.drogeriafx.modelo.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
}
