package com.duoc.bancoxyz.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.duoc.bancoxyz.dto.TransaccionRequestDTO;
import com.duoc.bancoxyz.dto.TransaccionResponseDTO;
import com.duoc.bancoxyz.model.CuentaEntity;
import com.duoc.bancoxyz.model.TipoTransaccion;
import com.duoc.bancoxyz.model.TransaccionEntity;
import com.duoc.bancoxyz.repository.CuentaRepository;
import com.duoc.bancoxyz.repository.TransaccionRepository;

@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CuentaRepository cuentaRepository;

    public TransaccionService(TransaccionRepository transaccionRepository, CuentaRepository cuentaRepository) {
        this.transaccionRepository = transaccionRepository;
        this.cuentaRepository = cuentaRepository;
    }

    public List<TransaccionResponseDTO> listarTransacciones() {
        return transaccionRepository.findAll().stream().map(this::toDTO).toList();
    }

    public TransaccionResponseDTO obtenerTransaccionId(Long id) {
        return transaccionRepository.findById(id).map(this::toDTO).orElse(null);
    }

    public List<TransaccionResponseDTO> listarPorCuenta(Long cuentaId) {
        return transaccionRepository.findByCuenta_IdOrderByFechaDesc(cuentaId).stream().map(this::toDTO).toList();
    }

    public List<TransaccionResponseDTO> listarPorTipo(TipoTransaccion tipo) {
        return transaccionRepository.findByTipo(tipo).stream().map(this::toDTO).toList();
    }

    public List<TransaccionResponseDTO> listarPorFecha(LocalDate fecha) {
        return transaccionRepository.findByFecha(fecha).stream().map(this::toDTO).toList();
    }

    @Transactional
    public TransaccionResponseDTO crearTransaccion(TransaccionRequestDTO transaccionRequestDTO) {
        TransaccionEntity transaccion = toEntity(transaccionRequestDTO);
        aplicarMovimiento(transaccion.getCuenta(), transaccion.getTipo(), transaccion.getMonto());
        cuentaRepository.save(transaccion.getCuenta());
        return toDTO(transaccionRepository.save(transaccion));
    }

    @Transactional
    public TransaccionResponseDTO actualizarTransaccion(Long id, TransaccionRequestDTO transaccionRequestDTO) {
        return transaccionRepository.findById(id).map(transaccion -> {
            revertirMovimiento(transaccion.getCuenta(), transaccion.getTipo(), transaccion.getMonto());
            CuentaEntity cuenta = cuentaRepository.findById(transaccionRequestDTO.getCuentaId())
                    .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con id: " + transaccionRequestDTO.getCuentaId()));
            transaccion.setFecha(transaccionRequestDTO.getFecha());
            transaccion.setTipo(transaccionRequestDTO.getTipo());
            transaccion.setMonto(transaccionRequestDTO.getMonto());
            transaccion.setDescripcion(transaccionRequestDTO.getDescripcion());
            transaccion.setCuenta(cuenta);
            aplicarMovimiento(cuenta, transaccion.getTipo(), transaccion.getMonto());
            cuentaRepository.save(cuenta);
            return toDTO(transaccionRepository.save(transaccion));
        }).orElse(null);
    }

    @Transactional
    public boolean eliminarTransaccion(Long id) {
        return transaccionRepository.findById(id).map(transaccion -> {
            revertirMovimiento(transaccion.getCuenta(), transaccion.getTipo(), transaccion.getMonto());
            cuentaRepository.save(transaccion.getCuenta());
            transaccionRepository.deleteById(id);
            return true;
        }).orElse(false);
    }

    private void aplicarMovimiento(CuentaEntity cuenta, TipoTransaccion tipo, BigDecimal monto) {
        if (tipo == TipoTransaccion.DEPOSITO) {
            cuenta.setSaldo(cuenta.getSaldo().add(monto));
            return;
        }
        if (cuenta.getSaldo().compareTo(monto) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente en la cuenta " + cuenta.getNumeroCuenta());
        }
        cuenta.setSaldo(cuenta.getSaldo().subtract(monto));
    }

    private void revertirMovimiento(CuentaEntity cuenta, TipoTransaccion tipo, BigDecimal monto) {
        if (tipo == TipoTransaccion.DEPOSITO) {
            if (cuenta.getSaldo().compareTo(monto) < 0) {
                throw new IllegalArgumentException("No es posible revertir el deposito: saldo insuficiente");
            }
            cuenta.setSaldo(cuenta.getSaldo().subtract(monto));
            return;
        }
        cuenta.setSaldo(cuenta.getSaldo().add(monto));
    }

    private TransaccionResponseDTO toDTO(TransaccionEntity transaccion) {
        return new TransaccionResponseDTO(
                transaccion.getId(),
                transaccion.getFecha(),
                transaccion.getTipo(),
                transaccion.getMonto(),
                transaccion.getDescripcion(),
                transaccion.getCuenta().getId(),
                transaccion.getCuenta().getNumeroCuenta()
        );
    }

    private TransaccionEntity toEntity(TransaccionRequestDTO transaccionDTO) {
        CuentaEntity cuenta = cuentaRepository.findById(transaccionDTO.getCuentaId())
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con id: " + transaccionDTO.getCuentaId()));
        TransaccionEntity transaccion = new TransaccionEntity();
        transaccion.setFecha(transaccionDTO.getFecha());
        transaccion.setTipo(transaccionDTO.getTipo());
        transaccion.setMonto(transaccionDTO.getMonto());
        transaccion.setDescripcion(transaccionDTO.getDescripcion());
        transaccion.setCuenta(cuenta);
        return transaccion;
    }

}
