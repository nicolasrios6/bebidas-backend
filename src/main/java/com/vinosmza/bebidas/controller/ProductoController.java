package com.vinosmza.bebidas.controller;

import com.vinosmza.bebidas.model.Producto;
import com.vinosmza.bebidas.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoServ;

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
    ) {
        Producto producto = new Producto(nombre, precio, bodega, varietal, categoria);
        return productoServ.createProducto(producto, imagen);
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
//        Producto productoExistente = productoServ.getProductoById(id);
//        if(productoExistente != null && (imagen == null || imagen.isEmpty())) {
//            producto.setImagen(productoExistente.getImagen());
//        }


    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id) {
        productoServ.deleteProducto(id);
    }

    @GetMapping("/categoria/{categoria}")
    public List<Producto> traerProductosPorCategoria(@PathVariable String categoria) {
        return productoServ.getProductosByCategoria(categoria);
    }
}
