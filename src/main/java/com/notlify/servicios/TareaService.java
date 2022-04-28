package com.notlify.servicios;

import com.notlify.entidades.Tarea;
import com.notlify.entidades.Usuario;
import com.notlify.enums.Estado;
import com.notlify.repositorios.TareaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    @Transactional(rollbackFor = Exception.class)
    public void save(String titulo, String descripcion, Estado estado, List<Usuario> usuarios) {

        Tarea tarea = new Tarea();

        tarea.setTitulo(titulo);
        tarea.setDescripcion(descripcion);
        tarea.setEstado(estado);
        tarea.setUsuarios(usuarios);
        tarea.setActivo(Boolean.TRUE);

        tareaRepository.save(tarea);

    }

    public List<Tarea> getTareas() {

        List<Tarea> tareas = tareaRepository.findAll();

        return tareas;

    }

    public Tarea findById(String id) throws Exception {

        Optional<Tarea> respuesta = tareaRepository.findById(id);

        if (respuesta.isPresent()) {
            Tarea tarea = respuesta.get();
            return tarea;
        } else {
            throw new Exception("No existe esta tarea");
        }

    }

    public void update(String id, String titulo, String descripcion, Estado estado) throws Exception {

        Tarea tarea = findById(id);

        tarea.setTitulo(titulo);
        tarea.setDescripcion(descripcion);
        tarea.setEstado(estado);

        tareaRepository.save(tarea);

    }

    public void delete(String id) throws Exception {

        Tarea tarea = findById(id);

        tarea.setActivo(Boolean.FALSE);

        tareaRepository.save(tarea);

    }

}
