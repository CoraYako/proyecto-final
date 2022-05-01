package com.notlify.servicios;

import com.notlify.entidades.Tarea;
import com.notlify.entidades.Usuario;
import com.notlify.enums.Estado;
import com.notlify.exceptions.ElementoNoEncontradoException;
import com.notlify.exceptions.ErrorInputException;
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

    @Autowired
    private UsuarioService usuarioService;

    @Transactional(rollbackFor = Exception.class)
    public Tarea crearYPersistir(String titulo, String descripcion, String idUsuario) throws ErrorInputException, ElementoNoEncontradoException {
        validar(titulo, descripcion, idUsuario);

        Tarea tarea = new Tarea();

        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        tarea.getListaUsuarios().add(usuario);

        tarea.setTitulo(titulo);
        tarea.setDescripcion(descripcion);
        tarea.setEstado(Estado.TODO);
        tarea.setActivo(true);

        return tareaRepository.save(tarea);
    }

    @Transactional(readOnly = true)
    public List<Tarea> listarTodas() {
        return tareaRepository.findAll();
    }

    @Transactional(rollbackFor = {Exception.class})
    public Tarea modificar(String id, String titulo, String descripcion, Estado estado) throws ErrorInputException, ElementoNoEncontradoException {
        validar(titulo, descripcion, id);

        Tarea tarea = buscarPorId(id);
        tarea.setTitulo(titulo);
        tarea.setDescripcion(descripcion);
        tarea.setEstado(estado);

        return tareaRepository.save(tarea);
    }

    @Transactional(rollbackFor = {Exception.class})
    public Tarea moverDeEstado(String id, Estado estado) throws ElementoNoEncontradoException, ErrorInputException {
        if (estado == null) {
            throw new ErrorInputException("Debe declarar el estado al que moverá la tarea actual.");
        }
        Tarea tarea = buscarPorId(id);
        tarea.setEstado(estado);

        return tareaRepository.save(tarea);
    }

    @Transactional(rollbackFor = {Exception.class})
    public Tarea deshabilitar(String id) throws ElementoNoEncontradoException, ErrorInputException {
        Tarea tarea = buscarPorId(id);
        tarea.setActivo(false);
        return tareaRepository.save(tarea);
    }

    @Transactional(readOnly = true)
    public Tarea buscarPorId(String id) throws ElementoNoEncontradoException, ErrorInputException {
        if (id == null || id.trim().isEmpty()) {
            throw new ErrorInputException("El id de la tarea no es el correcto.");
        }
        Optional<Tarea> respuesta = tareaRepository.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ElementoNoEncontradoException("No existe la tarea solicitada.");
        }
    }

    public void validar(String titulo, String descripcion, String idUsuario) throws ErrorInputException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ErrorInputException("Debe proporcionar un título.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new ErrorInputException("Debe proporcionar una descripción.");
        }
        if (idUsuario == null || idUsuario.trim().isEmpty()) {
            throw new ErrorInputException("Debe proporcionar un id válido.");
        }
    }
}
