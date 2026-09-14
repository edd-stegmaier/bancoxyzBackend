package com.duoc.bancoxyz.controller;

import java.util.List;

import com.duoc.bancoxyz.dto.ClienteDetalleResponseDTO;
import com.duoc.bancoxyz.dto.ClienteRequestDTO;
import com.duoc.bancoxyz.dto.ClienteResponseDTO;
import com.duoc.bancoxyz.dto.CuentaResponseDTO;
import com.duoc.bancoxyz.service.ClienteService;
import com.duoc.bancoxyz.service.CuentaService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final CuentaService cuentaService;

    public ClienteController(ClienteService clienteService, CuentaService cuentaService) {
        this.clienteService = clienteService;
        this.cuentaService = cuentaService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("El servicio de Clientes se encuentra funcionando correctamente");
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerClienteId(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.obtenerClienteId(id);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/{id}/detalle")
    public ResponseEntity<ClienteDetalleResponseDTO> obtenerClienteDetalle(@PathVariable Long id) {
        ClienteDetalleResponseDTO cliente = clienteService.obtenerClienteDetalle(id);
        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/{id}/cuentas")
    public ResponseEntity<List<CuentaResponseDTO>> listarCuentasCliente(@PathVariable Long id) {
        if (clienteService.obtenerClienteId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cuentaService.listarPorCliente(id));
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crearCliente(@Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO nuevoCliente = clienteService.crearCliente(clienteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> actualizarCliente(@PathVariable Long id,
                                                                @Valid @RequestBody ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO actualizado = clienteService.actualizarCliente(id, clienteRequestDTO);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        boolean eliminado = clienteService.eliminarCliente(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
