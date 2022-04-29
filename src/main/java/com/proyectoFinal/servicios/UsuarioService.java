package com.proyectoFinal.servicios;

import com.proyectoFinal.entidades.Tarea;
import com.proyectoFinal.entidades.Usuario;
import com.proyectoFinal.enums.Estado;
import com.proyectoFinal.enums.Rol;
import com.proyectoFinal.excepciones.ErrorInputException;
import com.proyectoFinal.repositorios.UsuarioRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    
    
    //Metodo Create usuario

    @Transactional(rollbackFor = Exception.class)
    public void save(String email, String password, String confirmarPassword, Rol rol, String nombre, String apellido, Date fechaNacimiento,/*Imagen fotoPerfil,*/ Date fechaAlta, Date fechaBaja, Boolean activo) throws ErrorInputException {

       validacion(nombre,apellido,email,password,confirmarPassword,fechaAlta,fechaBaja,activo);
        
        
        //Validador de rol nulidad
        if (rol == null) {

            throw new ErrorInputException("El rol no debe ser nulo."); 

        } else {
         

                Usuario u = new Usuario();
                u.setEmail(email);
                String passwordEncriptado = new BCryptPasswordEncoder().encode(password);
                u.setPassword(passwordEncriptado);
                u.setRol(rol);
                u.setFechaNacimiento(fechaNacimiento);
                u.setNombre(nombre);
                u.setApellido(apellido);
                u.setFechaNacimiento(fechaAlta);
                u.setFechaAlta(fechaAlta);
                u.setFechaBaja(fechaBaja);
                //u.setImagen(imgPerfil);
                u.setActivo(activo);

                usuarioRepository.save(u);
           
        }
    }
    
    
    // Metodos Read Usuario
    // METODO LISTAR TODOS
    
    @Transactional(readOnly=true)
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }
    
    // METODO LISTAR POR NOMBRE
    
    @Transactional(readOnly=true)
    public Usuario buscarPorNombre(String nombre){
    return usuarioRepository.buscarPorNombre(nombre);
        
    }
      // METODO LISTAR POR APELLIDO
    
    
    @Transactional(readOnly=true)
    public Usuario buscarPorApellido(String apellido){
    return usuarioRepository.buscarPorApellido(apellido);
   
    // METODO LISTAR POR E-mail
    }
    @Transactional(readOnly=true)
    public Usuario buscarPorEmail(String nombre){
    return usuarioRepository.buscarPorEmail(nombre);
      }
    
    //METODO LISTAR TODOS
    
     @Transactional(readOnly  = true)
    public  List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    

    //Buscar por ID
    @Transactional(readOnly=true)
    public Usuario buscarPorId(String id) throws Exception {

        Optional<Usuario> respuesta = usuarioRepository.findById(id);

        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            return usuario;
        } else {
            throw new Exception("No existe el usuario");
        }

    }

    // Metodo update usuario 
    @Transactional(rollbackFor = Exception.class)
    public void update(String id, String email, String password, String confirmarPassword, Rol rol, String nombre, String apellido, Date fechaNacimiento,/*Imagen fotoPerfil,*/ Date fechaAlta, Date fechaBaja, Boolean activo) throws Exception {

        
        validacion(nombre,apellido,email,password,confirmarPassword,fechaAlta,fechaBaja,activo);
        
        // Condicion de nulidad
        if (rol == null) {

            System.out.println("El rol no puede ser nulo");

        } else {

                Usuario u = buscarPorId(id);
                u.setEmail(email);
                String passwordEncriptado = new BCryptPasswordEncoder().encode(password);
                u.setPassword(passwordEncriptado);
                u.setRol(rol);
                u.setFechaNacimiento(fechaNacimiento);
                u.setNombre(nombre);
                u.setApellido(apellido);
                u.setFechaNacimiento(fechaAlta);
                u.setFechaAlta(fechaAlta);
                u.setFechaBaja(fechaBaja);
                //u.setImagen(imgPerfil);
                u.setActivo(activo);
               
                usuarioRepository.save(u);
         }
    
    }

    //Metodo DELETE usuario
    
     @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws Exception {

        Usuario u = buscarPorId(id);

        u.setActivo(Boolean.FALSE);
 
        usuarioRepository.save(u);

    }

    
    //Metodos ALTA y BAJA usuario.
        
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Usuario altaUsuario(String id) {
        
        Usuario u  = usuarioRepository.getOne(id);
        
       u.setActivo(true);
        return usuarioRepository.save(u);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Usuario baja(String id) {
        
         Usuario u = usuarioRepository.getOne(id);
        
        u.setActivo(false);
        u.setFechaBaja(new Date());
        return usuarioRepository.save(u);
        
    }
    
   
    // METODO LOGIN POR EMAIL
    
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.buscarPorEmail(email);
        if (u == null) {
            return null;
        }
        List<GrantedAuthority> permisos = new ArrayList();
        GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + u.getRol().toString());
        permisos.add(p1);
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("usuariosession", u);
        return new User(u.getEmail(), u.getPassword(), permisos);
    }
    
    // Metodos de validacion
    
     public void validacion(String nombre, String apellido, String email, String password, String confirmarPassword, Date fechaAlta, Date fechaBaja, Boolean activo) throws ErrorInputException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorInputException("El nombre del usuario no puede ser nulo.");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new ErrorInputException("El apellido del usuario no puede ser nulo.");
        }
       
        if (email == null || email.trim().isEmpty()) {
            throw new ErrorInputException("El mail del usuario no puede ser nulo.");
        }
        if (password == null || password.trim().isEmpty() || password.length() < 6) {
            throw new ErrorInputException("La contraseña no puede ser nula y debe contener al menos 6 dígitos.");
        }
        if (!confirmarPassword.equals(confirmarPassword)) {
            throw new ErrorInputException("Las contraseñas deben ser iguales.");
        }
        
        if (fechaAlta==null ){
        throw new ErrorInputException("Debe ingresar alguna fecha de alta.");
        }
        
        if (fechaBaja==null ){
        throw new ErrorInputException("Debe ingresar alguna fecha de baja.");
        }
        
        if (activo==null ){
        throw new ErrorInputException("Debe seleccionar si está activo o no.");
        }
        
    }
    
}
