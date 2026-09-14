package com.duoc.bancoxyz.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.bancoxyz.model.CuentaEntity;
import com.duoc.bancoxyz.model.TipoCuenta;

@Repository
public interface CuentaRepository extends JpaRepository<CuentaEntity, Long> {

    boolean existsByNumeroCuenta(String numeroCuenta);

    Optional<CuentaEntity> findByNumeroCuenta(String numeroCuenta);

    List<CuentaEntity> findByCliente_Id(Long clienteId);

    List<CuentaEntity> findByTipoCuenta(TipoCuenta tipoCuenta);

}
