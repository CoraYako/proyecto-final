package com.notlify.servicios;

import com.notlify.entidades.Imagen;
import com.notlify.entidades.Usuario;
import com.notlify.enums.Rol;
import com.notlify.exceptions.ErrorInputException;
import com.notlify.repositorios.UsuarioRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ImagenService imagenService;

    @Transactional(rollbackFor = {Exception.class})
    public Usuario save(String email, String password, String confirmarPassword,
            Rol rol, String nombre, String apellido, Date fechaNacimiento,
            MultipartFile archivo) throws ErrorInputException {
        Usuario usuario = new Usuario();
        Imagen imagen = imagenService.guardar(archivo);

        String passwordEncriptado = new BCryptPasswordEncoder().encode(password);
        usuario.setPassword(passwordEncriptado);

        usuario.setEmail(email);
        usuario.setFechaNacimiento(fechaNacimiento);
        usuario.setApellido(apellido);
        usuario.setNombre(nombre);
        usuario.setFechaAlta(new Date());
        usuario.setRol(rol);
        usuario.setFotoPerfil(imagen);
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);

    }
    
//    public 

    @Transactional(readOnly = true)
    public Usuario buscarPorCorreo(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("El correo no puede ser nulo.");
        }
        return usuarioRepository.buscarPorEmail(email);
    }

    @Transactional(readOnly = true)
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
