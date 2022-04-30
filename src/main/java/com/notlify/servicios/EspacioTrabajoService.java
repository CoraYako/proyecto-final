package com.notlify.servicios;

import com.notlify.entidades.EspacioTrabajo;
import com.notlify.entidades.Imagen;
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
    public EspacioTrabajo cargar(MultipartFile archivo, String nombre, String idUsuario) throws Exception {

        Imagen imagen = imagenService.guardar(archivo);

        validar(nombre, idUsuario);

        EspacioTrabajo espacioTrabajo = new EspacioTrabajo();

        espacioTrabajo.setNombre(nombre);
        espacioTrabajo.setFechaCreacion(new Date());
        espacioTrabajo.setActivo(true);
/*
        List<Usuario> lista;
        Usuario usuario = usuarioService.buscarPorId(idUsuario);
        lista.add(usuario);
        espacioTrabajo.setListaUsuarios(lista);*/
        return espacioTrabajoRepository.save(espacioTrabajo);

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public EspacioTrabajo modificar(String id, String nombre,
            String idUsuarios, Boolean activo) throws Exception {

        try {
            EspacioTrabajo espacioTrabajo = buscarPorId(id);
            espacioTrabajo.setNombre(nombre);
            espacioTrabajo.setFechaCreacion(new Date());
            espacioTrabajo.setActivo(true);
/*
            List<Usuario> lista;
            Usuario usuario = usuarioService.buscarPorId(idUsuario);
            lista.add(usuario);
            espacioTrabajo.setListaUsuarios(lista);*/
            return espacioTrabajoRepository.save(espacioTrabajo);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public EspacioTrabajo buscarPorId(String id) throws Exception {

        Optional<EspacioTrabajo> respuesta = espacioTrabajoRepository.findById(id);

        if (respuesta.isPresent()) {
            EspacioTrabajo espacioTrabajo = respuesta.get();
            return espacioTrabajo;
        } else {
            throw new Exception("No existe este espacio de trabajo");
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public EspacioTrabajo alta(String id) {

        EspacioTrabajo espacioTrabajo = espacioTrabajoRepository.getOne(id);

        espacioTrabajo.setActivo(true);
        return espacioTrabajoRepository.save(espacioTrabajo);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public EspacioTrabajo baja(String id) throws ErrorInputException, ElementoNoEncontradoException {

        if(id == null || id.trim().isEmpty()){
            throw new ErrorInputException("El Id no puede estar vacío");
        }
        try {
            EspacioTrabajo espacioTrabajo = buscarPorId(id);
            espacioTrabajo.setActivo(false);
            
            return espacioTrabajoRepository.save(espacioTrabajo);
        } catch (Exception e) {
            throw new ElementoNoEncontradoException(e.getMessage());
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

    public void validar(String nombre, String idUsuario) throws Exception {
        if (nombre == null || nombre.isEmpty() || nombre.contains("  ")) {
            throw new Exception("EL nombre no puede estar nulo o vacío");
        }
        if (idUsuario == null || idUsuario.isEmpty() || idUsuario.contains("  ")) {
            throw new Exception("EL nombre no puede estar nulo o vacío");
        }
    }

}
