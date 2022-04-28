package com.proyectoFinal.servicios;

import com.proyectoFinal.entidades.Usuario;
import com.proyectoFinal.enums.Rol;
import com.proyectoFinal.repositorios.UsuarioRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioService implements UserDetailsService {

    
    @Autowired
    private UsuarioRepository usuarioRepository;

    
    @Transactional(rollbackOn = { Exception.class })
    public void save(String email, String password, String confirmarPassword,Rol rol, String nombre, String apellido, String fechaNacimiento,/*Imagen fotoPerfil,*/ Date fechaAlta, Date fechaBaja, Boolean activo  ) {
        
       //metodo para validar que el rol no sea nulo
        if(password.equals(confirmarPassword)){
        
           Usuario u = new Usuario();
        u.setEmail(email);
        String passwordEncriptado = new BCryptPasswordEncoder().encode(password);
        u.setPassword(passwordEncriptado);
        u.setFechaNacimiento(fechaAlta);
        u.getApellido();
        u.setFechaAlta(fechaAlta);
        u.setFechaBaja(fechaBaja);
        u.setRol(rol);
        //u.setImagen(imgPerfil);
        u.setActivo(activo);
     

        //usuario.setRol(Rol.ADMIN);
        usuarioRepository.save(u);
        
        }else{
        
            System.out.println(" Las contraseñas no coinciden");
        
        }
        
    }

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

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
}
