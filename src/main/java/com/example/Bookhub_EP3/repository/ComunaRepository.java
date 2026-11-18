package com.example.Bookhub_EP3.repository;

import com.example.Bookhub_EP3.model.Comuna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Integer> {
    List<Comuna> findByRegionIdRegion(Integer idRegion);
}