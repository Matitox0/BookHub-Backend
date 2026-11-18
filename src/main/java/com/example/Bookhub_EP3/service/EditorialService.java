package com.example.Bookhub_EP3.service;

import com.example.Bookhub_EP3.model.Editorial;
import com.example.Bookhub_EP3.repository.EditorialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EditorialService {

    private final EditorialRepository editorialRepository;

    public List<Editorial> listarTodas() {
        return editorialRepository.findAll();
    }

    public Editorial crear(Editorial editorial) {
        if (editorialRepository.findByNombreIgnoreCase(editorial.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una editorial con ese nombre");
        }
        return editorialRepository.save(editorial);
    }

    public Editorial actualizar(Integer id, Editorial datos) {
        Editorial existente = editorialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Editorial no encontrada con ID: " + id));
        existente.setNombre(datos.getNombre());
        return editorialRepository.save(existente);
    }

    public void eliminar(Integer id) {
        if (!editorialRepository.existsById(id)) {
            throw new RuntimeException("Editorial no encontrada con ID: " + id);
        }
        editorialRepository.deleteById(id);
    }
}