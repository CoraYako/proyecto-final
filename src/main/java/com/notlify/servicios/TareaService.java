package com.notlify.servicios;

import com.notlify.entidades.Tarea;
import com.notlify.entidades.Usuario;
import com.notlify.enums.Estado;
import com.notlify.repositorios.TareaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional(rollbackFor = Exception.class)
    public void save(String titulo, String descripcion, Estado estado, String idUsuario) throws Exception {

        try {

            validar(titulo, descripcion, estado, idUsuario);

            Tarea tarea = new Tarea();

            //List<Usuario> lista = new ArrayList();
            //Usuario usuario = usuarioService.buscarPorId(idUsuario);
            //lista.add(usuario);
            tarea.setTitulo(titulo);
            tarea.setDescripcion(descripcion);
            tarea.setEstado(estado);
            //tarea.setUsuarios(lista);
            tarea.setActivo(Boolean.TRUE);

            tareaRepository.save(tarea);

        } catch (Exception e) {

            e.getMessage();

        }

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

    public void update(String titulo, String descripcion, Estado estado, String id) throws Exception {

        try {

            Tarea tarea = findById(id);

            validar(titulo, descripcion, estado, id);

            tarea.setTitulo(titulo);
            tarea.setDescripcion(descripcion);
            tarea.setEstado(estado);

            tareaRepository.save(tarea);

        } catch (Exception e) {

            e.getMessage();

        }

    }

    public void delete(String id) throws Exception {

        try {

            Tarea tarea = findById(id);

            tarea.setActivo(Boolean.FALSE);

            tareaRepository.save(tarea);

        } catch (Exception e) {

            e.getMessage();

        }

    }

    public void validar(String titulo, String descripcion, Estado estado, String id) throws Exception {

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new Exception("El título de la tarea no puede estar vacío");
        }

        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new Exception("La descripción de la tarea no puede estar vacía");
        }

        if (estado == null) {
            throw new Exception("Seleccione un estado para la tarea");
        }

        if (id == null) {
            throw new Exception("No se encuentra el ID");
        }

    }

}
