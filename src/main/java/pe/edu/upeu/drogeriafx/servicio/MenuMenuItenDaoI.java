package pe.edu.upeu.drogeriafx.servicio;

import pe.edu.upeu.drogeriafx.dto.MenuMenuItenTO;

import java.util.List;
import java.util.Properties;

public interface MenuMenuItenDaoI {

    public List<MenuMenuItenTO> listaAccesos(String perfil, Properties idioma);

}
