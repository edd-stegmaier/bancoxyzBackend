package com.duoc.bancoxyz.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.duoc.bancoxyz.model.TipoTransaccion;
import com.duoc.bancoxyz.model.TransaccionEntity;

@Repository
public interface TransaccionRepository extends JpaRepository<TransaccionEntity, Long> {

    List<TransaccionEntity> findByCuenta_Id(Long cuentaId);

    List<TransaccionEntity> findByCuenta_IdOrderByFechaDesc(Long cuentaId);

    List<TransaccionEntity> findByTipo(TipoTransaccion tipo);

    List<TransaccionEntity> findByFecha(LocalDate fecha);

}
