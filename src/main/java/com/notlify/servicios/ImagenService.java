package com.notlify.servicios;

import com.notlify.entidades.Imagen;
import com.notlify.exceptions.ElementoNoEncontradoException;
import com.notlify.exceptions.ErrorInputException;
import com.notlify.repositorios.ImagenRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

    @Transactional(rollbackFor = Exception.class)
    public Imagen guardar(MultipartFile archivo) throws ErrorInputException {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Imagen imagen = new Imagen();

                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());

                return imagenRepository.save(imagen);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public Imagen actualizar(String id, MultipartFile archivo) throws ErrorInputException, Exception {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Imagen imagen = new Imagen();

                // reutilizamos el código y la validación del método buscarPorId(id)
                imagen = buscarPorId(id);

//                if (id != null) {
//                    Optional<Imagen> respuesta = imagenRepository.findById(id);
//                    if (respuesta.isPresent()) {
//                        imagen = respuesta.get();
//                    }
//                }
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());

                return imagenRepository.save(imagen);
            } catch (Exception e) {
                throw e;
            }
        }

        return null;
    }

    @Transactional(readOnly = true)
    public Imagen buscarPorId(String id) throws ElementoNoEncontradoException, Exception {
        if (id == null || id.trim().isEmpty()) {
            throw new Exception("El id del archivo no puede ser nulo.");
        }
        Optional<Imagen> respuesta = imagenRepository.findById(id);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ElementoNoEncontradoException("No se encontró el archivo solicitado.");
        }
    }

    @Transactional(readOnly = true)
    public List<Imagen> listarTodos() {
        return imagenRepository.findAll();
    }

}
