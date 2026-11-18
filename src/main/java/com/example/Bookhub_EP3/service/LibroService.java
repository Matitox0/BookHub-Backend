package com.example.Bookhub_EP3.service;

import com.example.Bookhub_EP3.DTO.LibroRequestDTO;
import com.example.Bookhub_EP3.model.Categoria;
import com.example.Bookhub_EP3.model.Editorial;
import com.example.Bookhub_EP3.model.Imagen;
import com.example.Bookhub_EP3.model.Libro;
import com.example.Bookhub_EP3.repository.CategoriaRepository;
import com.example.Bookhub_EP3.repository.EditorialRepository;
import com.example.Bookhub_EP3.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LibroService {

    private final LibroRepository libroRepository;
    private final EditorialRepository editorialRepository;
    private final CategoriaRepository categoriaRepository;

    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }

    public Libro obtenerPorId(Integer id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con ID: " + id));
    }

    @Transactional
    public Libro crear(LibroRequestDTO request) {
        Editorial editorial = editorialRepository.findById(request.getEditorialId())
                .orElseThrow(() -> new RuntimeException("Editorial no encontrada con ID: " + request.getEditorialId()));

        List<Categoria> categorias = request.getCategoriaIds() != null
                ? categoriaRepository.findAllById(request.getCategoriaIds())
                : new ArrayList<>();

        Libro libro = Libro.builder()
                .titulo(request.getTitulo())
                .autor(request.getAutor())
                .precio(request.getPrecio())
                .stock(request.getStock() != null ? request.getStock() : 10)
                .detalle(request.getDetalle())
                .editorial(editorial)
                .categorias(categorias)
                .build();

        if (request.getImagenesUrls() != null) {
            List<Imagen> imagenes = request.getImagenesUrls().stream()
                    .map(url -> Imagen.builder().url(url).libro(libro).build())
                    .toList();
            libro.setImagenes(new ArrayList<>(imagenes));
        }

        return libroRepository.save(libro);
    }

    @Transactional
    public Libro actualizar(Integer id, LibroRequestDTO request) {
        Libro libro = obtenerPorId(id);

        Editorial editorial = editorialRepository.findById(request.getEditorialId())
                .orElseThrow(() -> new RuntimeException("Editorial no encontrada con ID: " + request.getEditorialId()));

        List<Categoria> categorias = request.getCategoriaIds() != null
                ? categoriaRepository.findAllById(request.getCategoriaIds())
                : new ArrayList<>();

        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setPrecio(request.getPrecio());
        libro.setStock(request.getStock());
        libro.setDetalle(request.getDetalle());
        libro.setEditorial(editorial);
        libro.setCategorias(categorias);

        if (request.getImagenesUrls() != null) {
            if (libro.getImagenes() == null) {
                libro.setImagenes(new ArrayList<>());
            } else {
                libro.getImagenes().clear();
            }
            for (String url : request.getImagenesUrls()) {
                libro.getImagenes().add(Imagen.builder().url(url).libro(libro).build());
            }
        }

        return libroRepository.save(libro);
    }

    public void eliminar(Integer id) {
        if (!libroRepository.existsById(id)) {
            throw new RuntimeException("Libro no encontrado con ID: " + id);
        }
        libroRepository.deleteById(id);
    }
}