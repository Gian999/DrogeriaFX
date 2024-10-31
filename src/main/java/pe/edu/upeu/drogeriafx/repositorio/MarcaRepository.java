package pe.edu.upeu.drogeriafx.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upeu.drogeriafx.modelo.Marca;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {

}
