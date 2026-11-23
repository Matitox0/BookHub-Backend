package com.example.Bookhub_EP3.controller;

import com.example.Bookhub_EP3.model.Comuna;
import com.example.Bookhub_EP3.model.Estado;
import com.example.Bookhub_EP3.model.MetodoPago;
import com.example.Bookhub_EP3.model.Region;
import com.example.Bookhub_EP3.repository.ComunaRepository;
import com.example.Bookhub_EP3.repository.EstadoRepository;
import com.example.Bookhub_EP3.repository.MetodoPagoRepository;
import com.example.Bookhub_EP3.repository.RegionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogos")
@RequiredArgsConstructor
@Tag(name = "Catálogos Auxiliares", description = "Listados base para formularios de compra, despacho y estados")
public class CatalogoController {

    private final MetodoPagoRepository metodoPagoRepository;
    private final EstadoRepository estadoRepository;
    private final RegionRepository regionRepository;
    private final ComunaRepository comunaRepository;

    @GetMapping("/metodos-pago")
    @Operation(summary = "Listar métodos de pago disponibles")
    public ResponseEntity<List<MetodoPago>> listarMetodosPago() {
        return ResponseEntity.ok(metodoPagoRepository.findAll());
    }

    @GetMapping("/estados")
    @Operation(summary = "Listar estados de pedido")
    public ResponseEntity<List<Estado>> listarEstados() {
        return ResponseEntity.ok(estadoRepository.findAll());
    }

    @GetMapping("/regiones")
    @Operation(summary = "Listar regiones")
    public ResponseEntity<List<Region>> listarRegiones() {
        return ResponseEntity.ok(regionRepository.findAll());
    }

    @GetMapping("/regiones/{idRegion}/comunas")
    @Operation(summary = "Listar comunas de una región")
    public ResponseEntity<List<Comuna>> listarComunasPorRegion(@PathVariable Integer idRegion) {
        return ResponseEntity.ok(comunaRepository.findByRegionIdRegion(idRegion));
    }
}