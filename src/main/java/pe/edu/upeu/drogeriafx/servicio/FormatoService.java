package pe.edu.upeu.drogeriafx.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.drogeriafx.dto.ComboBoxOption;
import pe.edu.upeu.drogeriafx.modelo.Formato;
import pe.edu.upeu.drogeriafx.modelo.Marca;
import pe.edu.upeu.drogeriafx.modelo.UnidadMedida;
import pe.edu.upeu.drogeriafx.repositorio.FormatoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FormatoService {

    @Autowired
    private FormatoRepository repo;
    Logger logger= LoggerFactory.getLogger(ProductoService.class);


    /**
     * List all available Formato entries.
     */
    public List<Formato> list() {
        return repo.findAll();
    }

    /**
     * Save a new or existing Formato entry.
     */
    public Formato save(Formato formato) {
        return repo.save(formato);
    }

    public Formato update(Formato to){
        return repo.save(to);
    }


    /**
     * Search for a Formato entry by its ID.
     */
    public Formato searchById(Long id){
        return repo.findById(id).orElse(null);
    }

    /**
     * Delete a Formato entry by its ID.
     */
    public void delete(Long id) {
        repo.deleteById(id);
    }

    /**
     * Retrieve all Formato entries as ComboBox options.
     */
    public List<ComboBoxOption> listarCombobox() {
        List<ComboBoxOption> listar = new ArrayList<>();
        ComboBoxOption cb;
        for (Formato cate : repo.findAll()) {
            cb = new ComboBoxOption();
            cb.setKey(String.valueOf(cate.getIdFormato()));
            cb.setValue(cate.getNombre());
            listar.add(cb);
        }
        return listar;
    }
}
