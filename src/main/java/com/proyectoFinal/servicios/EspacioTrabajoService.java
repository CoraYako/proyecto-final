package com.proyectoFinal.servicios;

import com.proyectoFinal.entidades.EspacioTrabajo;
import com.proyectoFinal.repositorios.EspacioTrabajoRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EspacioTrabajoService {

    @Autowired
    private EspacioTrabajoRepository espacioTrabajoRepository;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public EspacioTrabajo cargar(String nombre,/*String idTarea, String idImagen, String idUsuarios, */Boolean activo) throws Exception{

        validar(nombre);
        
        EspacioTrabajo espacioTrabajo = new EspacioTrabajo();

        espacioTrabajo.setNombre(nombre);
        espacioTrabajo.setActivo(true);
        espacioTrabajo.setFechaCreacion(new Date());

        //ESTAS TRES ENTIDADES DEBO ESPERAR A QUE REALICEN SUS RESPECTIVOS SERVICIO PARA PODER
        //IMPORTARLAS Y CARGARLAS EN LA CREACION DEL ESPACIO DE TRABAJO
        //Tarea tarea = 
        //Imagen imagen
        //Usuario usuario
        return espacioTrabajoRepository.save(espacioTrabajo);

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public EspacioTrabajo modificar(String id, String nombre, String idTarea, String idImagen,
            String idUsuarios, Boolean activo) throws Exception {

       
        Optional<EspacioTrabajo> respuesta = espacioTrabajoRepository.findById(id);

        if (respuesta.isPresent()) {

            EspacioTrabajo espacioTrabajo = respuesta.get();

            espacioTrabajo.setNombre(nombre);
            espacioTrabajo.setActivo(true);
            espacioTrabajo.setFechaCreacion(new Date());

            //ESTAS TRES ENTIDADES DEBO ESPERAR A QUE REALICEN SUS RESPECTIVOS SERVICIO PARA PODER
            //IMPORTARLAS Y CARGARLAS EN LA CREACION DEL ESPACIO DE TRABAJO
            //Tarea tarea = 
            //Imagen imagen =
            //Usuario usuario =
            return espacioTrabajoRepository.save(espacioTrabajo);
        } else {
            throw new Exception("No existe la tarea solicitada");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public EspacioTrabajo buscarPorId(String id) throws Exception {
        
        Optional<EspacioTrabajo> respuesta = espacioTrabajoRepository.findById(id);
        
        if(respuesta.isPresent()){
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
    public EspacioTrabajo baja(String id) {
        
         EspacioTrabajo espacioTrabajo = espacioTrabajoRepository.getOne(id);
        
        espacioTrabajo.setActivo(false);
        espacioTrabajo.setFechaFinalizacion(new Date());
        return espacioTrabajoRepository.save(espacioTrabajo);
        
    }
    
    @Transactional(readOnly  = true)
    public  List<EspacioTrabajo> listarTodos() {
        return espacioTrabajoRepository.findAll();
    }
    
    @Transactional(readOnly  = true)
    public  List<EspacioTrabajo> listarActivos() {
        return espacioTrabajoRepository.buscarActivos();
    }
    
    public void validar(String nombre) throws Exception{
        if (nombre == null || nombre.isEmpty() || nombre.contains("  ")){
            throw new Exception("EL nombre no puede estar nulo o vacío");
        }
    }
    
}
