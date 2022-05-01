package com.notlify.servicios;

import com.notlify.entidades.EspacioTrabajo;
import com.notlify.entidades.Imagen;
import com.notlify.entidades.Tarea;
import com.notlify.entidades.Usuario;
import com.notlify.exceptions.ElementoNoEncontradoException;
import com.notlify.exceptions.ErrorInputException;
import com.notlify.repositorios.EspacioTrabajoRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EspacioTrabajoService {

    @Autowired
    private EspacioTrabajoRepository espacioTrabajoRepository;

    @Autowired
    private TareaService tareaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ImagenService imagenService;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public EspacioTrabajo crearYPersistir(MultipartFile archivo, String nombre, String idUsuario) throws ErrorInputException, ElementoNoEncontradoException {
        validar(nombre, idUsuario);

        EspacioTrabajo espacioTrabajo = new EspacioTrabajo();
        Imagen fondo = imagenService.guardar(archivo);

        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        espacioTrabajo.getListaUsuarios().add(usuario);

        espacioTrabajo.setNombre(nombre);
        espacioTrabajo.setFondo(fondo);
        espacioTrabajo.setFechaCreacion(new Date());
        espacioTrabajo.setActivo(true);

        return espacioTrabajoRepository.save(espacioTrabajo);
    }

    @Transactional(rollbackFor = {Exception.class})
    public EspacioTrabajo agregarTarea(String id, String idTarea) throws ElementoNoEncontradoException, ErrorInputException {
        EspacioTrabajo espacioTrabajo = buscarPorId(id);
        Tarea tarea = tareaService.buscarPorId(idTarea);

        espacioTrabajo.getListaTareas().add(tarea);
        return espacioTrabajoRepository.save(espacioTrabajo);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public EspacioTrabajo modificar(String id, MultipartFile archivo, String nombre) throws ElementoNoEncontradoException, ErrorInputException {
        EspacioTrabajo espacioTrabajo = buscarPorId(id);

        String idImagen = null;
        if (espacioTrabajo.getFondo().getId() != null) {
            idImagen = espacioTrabajo.getFondo().getId();
        }
        Imagen imagen = imagenService.actualizar(idImagen, archivo);

        espacioTrabajo.setFondo(imagen);
        espacioTrabajo.setNombre(nombre);
        return espacioTrabajoRepository.save(espacioTrabajo);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public EspacioTrabajo alta(String id) throws ElementoNoEncontradoException, ErrorInputException {
        EspacioTrabajo espacioTrabajo = buscarPorId(id);

        espacioTrabajo.setActivo(true);
        return espacioTrabajoRepository.save(espacioTrabajo);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public EspacioTrabajo deshabilitar(String id) throws ErrorInputException, ElementoNoEncontradoException {
        EspacioTrabajo espacioTrabajo = buscarPorId(id);

        espacioTrabajo.setActivo(false);
        return espacioTrabajoRepository.save(espacioTrabajo);
    }

    @Transactional(readOnly = true)
    public EspacioTrabajo buscarPorId(String id) throws ElementoNoEncontradoException, ErrorInputException {
        if (id == null || id.trim().isEmpty()) {
            throw new ErrorInputException("El id del espacio de trabajo no es correcto.");
        }
        Optional<EspacioTrabajo> respuesta = espacioTrabajoRepository.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ElementoNoEncontradoException("El espacio de trabajo solicitado no existe.");
        }
    }

    @Transactional(readOnly = true)
    public List<EspacioTrabajo> listarTodos() {
        return espacioTrabajoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<EspacioTrabajo> listarActivos() {
        return espacioTrabajoRepository.buscarActivos();
    }

    public void validar(String nombre, String idUsuario) throws ErrorInputException {
        if (nombre == null || nombre.isEmpty() || nombre.contains("  ")) {
            throw new ErrorInputException("Debe proporcionar un nombre a su espacio de trabajo.");
        }
        if (idUsuario == null || idUsuario.isEmpty() || idUsuario.contains("  ")) {
            throw new ErrorInputException("EL id no puede estar nulo o vacío.");
        }
    }
}
