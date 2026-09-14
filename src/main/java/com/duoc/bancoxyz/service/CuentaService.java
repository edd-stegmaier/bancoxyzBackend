package com.duoc.bancoxyz.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.duoc.bancoxyz.dto.ClienteResponseDTO;
import com.duoc.bancoxyz.dto.CuentaDetalleResponseDTO;
import com.duoc.bancoxyz.dto.CuentaRequestDTO;
import com.duoc.bancoxyz.dto.CuentaResponseDTO;
import com.duoc.bancoxyz.dto.TransaccionResponseDTO;
import com.duoc.bancoxyz.model.ClienteEntity;
import com.duoc.bancoxyz.model.CuentaEntity;
import com.duoc.bancoxyz.model.TipoCuenta;
import com.duoc.bancoxyz.repository.ClienteRepository;
import com.duoc.bancoxyz.repository.CuentaRepository;
import com.duoc.bancoxyz.repository.TransaccionRepository;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final TransaccionRepository transaccionRepository;

    public CuentaService(CuentaRepository cuentaRepository,
                         ClienteRepository clienteRepository,
                         TransaccionRepository transaccionRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
        this.transaccionRepository = transaccionRepository;
    }

    public List<CuentaResponseDTO> listarCuentas() {
        return cuentaRepository.findAll().stream().map(this::toDTO).toList();
    }

    public CuentaResponseDTO obtenerCuentaId(Long id) {
        return cuentaRepository.findById(id).map(this::toDTO).orElse(null);
    }

    public CuentaResponseDTO obtenerPorNumero(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta).map(this::toDTO).orElse(null);
    }

    public List<CuentaResponseDTO> listarPorCliente(Long clienteId) {
        return cuentaRepository.findByCliente_Id(clienteId).stream().map(this::toDTO).toList();
    }

    public List<CuentaResponseDTO> listarPorTipo(TipoCuenta tipoCuenta) {
        return cuentaRepository.findByTipoCuenta(tipoCuenta).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public CuentaDetalleResponseDTO obtenerCuentaDetalle(Long id) {
        return cuentaRepository.findById(id).map(this::toDetalleDTO).orElse(null);
    }

    public CuentaResponseDTO crearCuenta(CuentaRequestDTO cuentaRequestDTO) {
        if (cuentaRepository.existsByNumeroCuenta(cuentaRequestDTO.getNumeroCuenta())) {
            throw new IllegalArgumentException("Ya existe una cuenta con el numero: " + cuentaRequestDTO.getNumeroCuenta());
        }
        CuentaEntity cuenta = toEntity(cuentaRequestDTO);
        return toDTO(cuentaRepository.save(cuenta));
    }

    public CuentaResponseDTO actualizarCuenta(Long id, CuentaRequestDTO cuentaRequestDTO) {
        return cuentaRepository.findById(id).map(cuenta -> {
            cuentaRepository.findByNumeroCuenta(cuentaRequestDTO.getNumeroCuenta())
                    .filter(existente -> !existente.getId().equals(id))
                    .ifPresent(existente -> {
                        throw new IllegalArgumentException("Ya existe una cuenta con el numero: " + cuentaRequestDTO.getNumeroCuenta());
                    });
            ClienteEntity cliente = clienteRepository.findById(cuentaRequestDTO.getClienteId())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id: " + cuentaRequestDTO.getClienteId()));
            cuenta.setNumeroCuenta(cuentaRequestDTO.getNumeroCuenta());
            cuenta.setTipoCuenta(cuentaRequestDTO.getTipoCuenta());
            cuenta.setSaldo(cuentaRequestDTO.getSaldo());
            cuenta.setActiva(cuentaRequestDTO.getActiva() != null ? cuentaRequestDTO.getActiva() : Boolean.TRUE);
            cuenta.setCliente(cliente);
            return toDTO(cuentaRepository.save(cuenta));
        }).orElse(null);
    }

    public boolean eliminarCuenta(Long id) {
        if (cuentaRepository.existsById(id)) {
            cuentaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private CuentaResponseDTO toDTO(CuentaEntity cuenta) {
        return new CuentaResponseDTO(
                cuenta.getId(),
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldo(),
                cuenta.getActiva(),
                cuenta.getCliente().getId(),
                cuenta.getCliente().getNombre()
        );
    }

    private CuentaDetalleResponseDTO toDetalleDTO(CuentaEntity cuenta) {
        ClienteEntity cliente = cuenta.getCliente();
        ClienteResponseDTO clienteDTO = new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getEdad(),
                cliente.getEmail(),
                cliente.getRut()
        );
        List<TransaccionResponseDTO> txs = transaccionRepository.findByCuenta_IdOrderByFechaDesc(cuenta.getId())
                .stream()
                .map(tx -> new TransaccionResponseDTO(
                        tx.getId(),
                        tx.getFecha(),
                        tx.getTipo(),
                        tx.getMonto(),
                        tx.getDescripcion(),
                        cuenta.getId(),
                        cuenta.getNumeroCuenta()
                ))
                .toList();
        return new CuentaDetalleResponseDTO(
                cuenta.getId(),
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldo(),
                cuenta.getActiva(),
                clienteDTO,
                txs
        );
    }

    private CuentaEntity toEntity(CuentaRequestDTO cuentaDTO) {
        ClienteEntity cliente = clienteRepository.findById(cuentaDTO.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con id: " + cuentaDTO.getClienteId()));
        CuentaEntity cuenta = new CuentaEntity();
        cuenta.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
        cuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
        cuenta.setSaldo(cuentaDTO.getSaldo());
        cuenta.setActiva(cuentaDTO.getActiva() != null ? cuentaDTO.getActiva() : Boolean.TRUE);
        cuenta.setCliente(cliente);
        return cuenta;
    }

}
