package com.notlify.servicios;

import com.notlify.entidades.Imagen;
import com.notlify.exceptions.ElementoNoEncontradoException;
import com.notlify.exceptions.ErrorInputException;
import com.notlify.repositorios.ImagenRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImagenService {

    @Autowired
    private ImagenRepository imagenRepository;

    /**
     *
     * Crea y persiste un archivo en la base de datos, seteando el mime, el
     * nombre y el contenido en un arreblo de byte[]. Dentro se verifica que el
     * archivo no venga nulo o esté vacío; esto es para evitar persistir un
     * archivo sin contenido en la DDBB. Lanza a la consola un IOException si se
     * ha producido un error en la entrada/salida.
     *
     * @param archivo
     * @return un objeto de tipo Imagen si y solo si el archivo pasa las
     * validaciones, de otro modo retorna null.
     */
    @Transactional(rollbackFor = Exception.class)
    public Imagen guardar(MultipartFile archivo) {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                Imagen imagen = new Imagen();

                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());

                return imagenRepository.save(imagen);
            } catch (IOException ex) {
                Logger.getLogger(ImagenService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public Imagen actualizar(String id, MultipartFile archivo) throws ErrorInputException, ElementoNoEncontradoException, IOException {
        if (archivo != null && !archivo.isEmpty()) {
            try {
                // reutilizamos el código y la validación del método buscarPorId(id)
                Imagen imagen = buscarPorId(id);

                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());

                return imagenRepository.save(imagen);
            } catch (ErrorInputException | ElementoNoEncontradoException e) {
                throw e;
            } catch (IOException ex) {
                Logger.getLogger(ImagenService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    @Transactional(readOnly = true)
    public Imagen buscarPorId(String id) throws ElementoNoEncontradoException, ErrorInputException {
        if (id == null || id.trim().isEmpty()) {
            throw new ErrorInputException("El id del archivo no puede ser nulo.");
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
