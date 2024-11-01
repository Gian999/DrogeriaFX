package pe.edu.upeu.drogeriafx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.drogeriafx.modelo.Formato;
import pe.edu.upeu.drogeriafx.repositorio.FormatoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FormatoService {

    @Autowired
    private FormatoRepository repo;

    public List<Formato> obtenerTodos() {
        return repo.findAll();
    }

    public Formato guardar(Formato formato) {
        return repo.save(formato);
    }

    public Optional<Formato> obtenerPorId(Long id) {
        return repo.findById(id);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}
