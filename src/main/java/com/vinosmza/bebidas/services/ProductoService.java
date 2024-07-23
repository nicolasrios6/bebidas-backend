package com.vinosmza.bebidas.services;

import com.vinosmza.bebidas.model.Producto;
import com.vinosmza.bebidas.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
public class ProductoService {
    //private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";
    @Autowired
    private ProductoRepository productoRepo;

    @Autowired
    private CloudinaryService cloudinaryServ;

    public List<Producto> getProductos() {
        return productoRepo.findAll();
    }

    public Producto getProductoById(Long id) {
        return productoRepo.findById(id).orElse(null);
    }

    public Producto createProducto(
            Producto producto,
            MultipartFile imagen
    ) throws IOException {
        try {
            Map uploadResult = cloudinaryServ.upload(imagen);
            producto.setImagenUrl(uploadResult.get("url").toString());
            return productoRepo.save(producto);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*public Producto createProducto(Producto producto, MultipartFile imagen) {
        try {
            if (imagen != null && !imagen.isEmpty()) {
                // Genera un nombre de archivo único combinando el tiempo actual en milisegundos con el nombre original del archivo
                String fileName = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();

                // Define la ruta completa donde se guardará el archivo
                Path path = Paths.get(UPLOAD_DIR + fileName);

                // Escribe el contenido del archivo en la ruta especificada
                Files.write(path, imagen.getBytes());

                // Establece el nombre del archivo en el campo 'imagen' del producto
                producto.setImagen(fileName);
            }
            // Guarda el producto en el repositorio (base de datos)
            return productoRepo.save(producto);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/

/*public Producto updateProducto(Producto producto, MultipartFile imagen) {
    try {
        if (imagen != null && !imagen.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imagen.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.write(path, imagen.getBytes());
            producto.setImagen(fileName);
        }
        return productoRepo.save(producto);
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
} */

    public Producto updateProducto(Producto producto, MultipartFile imagen) {
        if(imagen != null) {
            try {
                if(producto.getImagenPublicId() != null) {
                    cloudinaryServ.delete(producto.getImagenPublicId());
                }
                Map uploadResult = cloudinaryServ.upload(imagen);
                producto.setImagenUrl(uploadResult.get("url").toString());
                producto.setImagenPublicId(uploadResult.get("public_id").toString());
            } catch(IOException e) {
                e.printStackTrace();

            }
        }
        return productoRepo.save(producto);
    }

    public void deleteProducto(Long id) {
        Producto producto = getProductoById(id);
        if(producto != null) {
            try {
                cloudinaryServ.delete(producto.getImagenPublicId());
            } catch(IOException e) {
                e.printStackTrace();
            }
            productoRepo.delete(producto);
        }
    }

    public List<Producto> getProductosByCategoria(String categoria) {
        return productoRepo.findByCategoria(categoria);
    }
}
