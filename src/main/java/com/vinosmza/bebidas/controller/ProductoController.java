package com.vinosmza.bebidas.controller;

import com.vinosmza.bebidas.model.Producto;
import com.vinosmza.bebidas.repository.ProductoRepository;
import com.vinosmza.bebidas.services.CloudinaryService;
import com.vinosmza.bebidas.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoServ;
    @Autowired
    private ProductoRepository productoRepo;
    @Autowired
    private CloudinaryService cloudinaryServ;

    @GetMapping
    public List<Producto> traerProducto() {
        return productoServ.getProductos();
    }

    @GetMapping("/{id}")
    public Producto traerProductoPorId(@PathVariable Long id) {
        return productoServ.getProductoById(id);
    }

    @PostMapping
    public Producto crearProducto(
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") double precio,
            @RequestParam("bodega") String bodega,
            @RequestParam("varietal") String varietal,
            @RequestParam("categoria") String categoria,
            @RequestParam("imagen") MultipartFile imagen
    )throws IOException {
        Map uploadResult = cloudinaryServ.upload(imagen);
        String imagenUrl = (String) uploadResult.get("url");
        String imagenPublicId = (String) uploadResult.get("public_id");
        Producto producto = new Producto(nombre, precio, bodega, varietal, categoria, imagenUrl, imagenPublicId);
        return productoRepo.save(producto);
    }

    @PutMapping("/{id}")
    public Producto actualizarProducto(
            @PathVariable Long id,
            @RequestParam("nombre") String nombre,
            @RequestParam("precio") double precio,
            @RequestParam("bodega") String bodega,
            @RequestParam("varietal") String varietal,
            @RequestParam("categoria") String categoria,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen
    ) {
        Producto productoExistente = productoServ.getProductoById(id);
        productoExistente.setNombre(nombre);
        productoExistente.setPrecio(precio);
        productoExistente.setBodega(bodega);
        productoExistente.setVarietal(varietal);
        productoExistente.setCategoria(categoria);
        return productoServ.updateProducto(productoExistente, imagen);
    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        Producto producto = productoServ.getProductoById(id);
        if(producto == null) {
            throw new RuntimeException("Producto no encontrado.");
        }

        //Eliminar la imagen de Cloudinary si el publicId está presente
        if(producto.getImagenPublicId() != null) {
            try {
                cloudinaryServ.delete(producto.getImagenPublicId());
            } catch(Exception e) {
                throw new RuntimeException("Error al eliminar la imagen de cloudinary", e);
            }
        }
        productoServ.deleteProducto(id);
    }

    @GetMapping("/categoria/{categoria}")
    public List<Producto> traerProductosPorCategoria(@PathVariable String categoria) {
        return productoServ.getProductosByCategoria(categoria);
    }
}
