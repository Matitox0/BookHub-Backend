package com.example.Bookhub_EP3.repository;

import com.example.Bookhub_EP3.model.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, Integer> {
    Optional<Editorial> findByNombreIgnoreCase(String nombre);
}