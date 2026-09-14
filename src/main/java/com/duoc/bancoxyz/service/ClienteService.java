package com.duoc.bancoxyz.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.duoc.bancoxyz.dto.ClienteDetalleResponseDTO;
import com.duoc.bancoxyz.dto.ClienteRequestDTO;
import com.duoc.bancoxyz.dto.ClienteResponseDTO;
import com.duoc.bancoxyz.dto.CuentaDetalleResponseDTO;
import com.duoc.bancoxyz.dto.TransaccionResponseDTO;
import com.duoc.bancoxyz.model.ClienteEntity;
import com.duoc.bancoxyz.model.CuentaEntity;
import com.duoc.bancoxyz.repository.ClienteRepository;
import com.duoc.bancoxyz.repository.CuentaRepository;
import com.duoc.bancoxyz.repository.TransaccionRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;

    public ClienteService(ClienteRepository clienteRepository,
                          CuentaRepository cuentaRepository,
                          TransaccionRepository transaccionRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    public List<ClienteResponseDTO> listarClientes() {
        return clienteRepository.findAll().stream().map(this::toDTO).toList();
    }

    public ClienteResponseDTO obtenerClienteId(Long id) {
        return clienteRepository.findById(id).map(this::toDTO).orElse(null);
    }

    @Transactional(readOnly = true)
    public ClienteDetalleResponseDTO obtenerClienteDetalle(Long id) {
        return clienteRepository.findById(id).map(this::toDetalleDTO).orElse(null);
    }

    public ClienteResponseDTO crearCliente(ClienteRequestDTO clienteRequestDTO) {
        if (clienteRepository.existsByEmail(clienteRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Ya existe un cliente con el email: " + clienteRequestDTO.getEmail());
        }
        ClienteEntity cliente = toEntity(clienteRequestDTO);
        return toDTO(clienteRepository.save(cliente));
    }

    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO clienteRequestDTO) {
        return clienteRepository.findById(id).map(cliente -> {
            clienteRepository.findByEmail(clienteRequestDTO.getEmail())
                    .filter(existente -> !existente.getId().equals(id))
                    .ifPresent(existente -> {
                        throw new IllegalArgumentException("Ya existe un cliente con el email: " + clienteRequestDTO.getEmail());
                    });
            cliente.setNombre(clienteRequestDTO.getNombre());
            cliente.setEdad(clienteRequestDTO.getEdad());
            cliente.setEmail(clienteRequestDTO.getEmail());
            cliente.setRut(clienteRequestDTO.getRut());
            return toDTO(clienteRepository.save(cliente));
        }).orElse(null);
    }

    public boolean eliminarCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ClienteResponseDTO toDTO(ClienteEntity cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getEdad(),
                cliente.getEmail(),
                cliente.getRut()
        );
    }

    private ClienteDetalleResponseDTO toDetalleDTO(ClienteEntity cliente) {
        List<CuentaEntity> cuentas = cuentaRepository.findByCliente_Id(cliente.getId());
        List<CuentaDetalleResponseDTO> cuentasDTO = cuentas.stream().map(cuenta -> {
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
                    toDTO(cliente),
                    txs
            );
        }).toList();

        return new ClienteDetalleResponseDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getEdad(),
                cliente.getEmail(),
                cliente.getRut(),
                cuentasDTO
        );
    }

    private ClienteEntity toEntity(ClienteRequestDTO clienteDTO) {
        ClienteEntity cliente = new ClienteEntity();
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setEdad(clienteDTO.getEdad());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setRut(clienteDTO.getRut());
        return cliente;
    }

}
