package com.example.Bookhub_EP3.repository;

import com.example.Bookhub_EP3.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByUsuarioEmailOrderByFechaDesc(String email);
    List<Pedido> findAllByOrderByFechaDesc();
}