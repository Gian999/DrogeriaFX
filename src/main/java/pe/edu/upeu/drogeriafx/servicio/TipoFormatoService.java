package pe.edu.upeu.drogeriafx.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.drogeriafx.dto.ComboBoxOption;
import pe.edu.upeu.drogeriafx.modelo.Formato;
import pe.edu.upeu.drogeriafx.modelo.Marca;
import pe.edu.upeu.drogeriafx.modelo.TipoFormato;
import pe.edu.upeu.drogeriafx.repositorio.FormatoRepository;
import pe.edu.upeu.drogeriafx.repositorio.TipoFormatoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TipoFormatoService {
    @Autowired
    private TipoFormatoRepository repo;
    Logger logger= LoggerFactory.getLogger(ProductoService.class);


    /**
     * List all available Formato entries.
     */
    public List<TipoFormato> list() {
        return repo.findAll();
    }

    /**
     * Save a new or existing Formato entry.
     */
    public TipoFormato save(TipoFormato tipoformato) {
        return repo.save(tipoformato);
    }

    public TipoFormato update(TipoFormato to){
        return repo.save(to);
    }


    /**
     * Search for a Formato entry by its ID.
     */
    public TipoFormato searchById(Long id){
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
        for (TipoFormato cate : repo.findAll()) {
            cb = new ComboBoxOption();
            cb.setKey(String.valueOf(cate.getIdFormato()));
            cb.setValue(cate.getNombre());
            listar.add(cb);
        }
        return listar;
    }
}
