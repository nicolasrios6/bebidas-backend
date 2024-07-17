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

@Service
public class ProductoService {
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";
    @Autowired
    private ProductoRepository productoRepo;

    public List<Producto> getProductos() {
        return productoRepo.findAll();
    }

    public Producto getProductoById(Long id) {
        return productoRepo.findById(id).orElse(null);
    }

//    public Producto createProducto(Producto producto) {
//        return productoRepo.save(producto);
//    }
    public Producto createProducto(Producto producto, MultipartFile imagen) {
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
    }

//    public Producto updateProducto(Long id, Producto productoEdit) {
//        Producto producto = productoRepo.findById(id).orElse(null);
//        if(producto != null) {
//            producto.setNombre(productoEdit.getNombre());
//            producto.setCategoria(productoEdit.getCategoria());
//            producto.setVarietal(productoEdit.getVarietal());
//            producto.setBodega(productoEdit.getBodega());
//            producto.setImagen(productoEdit.getImagen());
//            producto.setPrecio(productoEdit.getPrecio());
//            return productoRepo.save(producto);
//        }
//        return null;
//    }
public Producto updateProducto(Producto producto, MultipartFile imagen) {
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
}

    public void deleteProducto(Long id) {
        productoRepo.deleteById(id);
    }

    public List<Producto> getProductosByCategoria(String categoria) {
        return productoRepo.findByCategoria(categoria);
    }
}
