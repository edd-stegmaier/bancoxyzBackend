package com.duoc.bancoxyz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.bancoxyz.model.ClienteEntity;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    boolean existsByEmail(String email);

    Optional<ClienteEntity> findByEmail(String email);

    Optional<ClienteEntity> findByRut(String rut);

}
