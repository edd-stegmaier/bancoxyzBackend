package com.duoc.bancoxyz.controller;

import java.util.List;

import com.duoc.bancoxyz.dto.CuentaDetalleResponseDTO;
import com.duoc.bancoxyz.dto.CuentaRequestDTO;
import com.duoc.bancoxyz.dto.CuentaResponseDTO;
import com.duoc.bancoxyz.dto.TransaccionResponseDTO;
import com.duoc.bancoxyz.model.TipoCuenta;
import com.duoc.bancoxyz.service.CuentaService;
import com.duoc.bancoxyz.service.TransaccionService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;
    private final TransaccionService transaccionService;

    public CuentaController(CuentaService cuentaService, TransaccionService transaccionService) {
        this.cuentaService = cuentaService;
        this.transaccionService = transaccionService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("El servicio de Cuentas se encuentra funcionando correctamente");
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponseDTO>> listarCuentas() {
        return ResponseEntity.ok(cuentaService.listarCuentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> obtenerCuentaId(@PathVariable Long id) {
        CuentaResponseDTO cuenta = cuentaService.obtenerCuentaId(id);
        if (cuenta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/numero/{numeroCuenta}")
    public ResponseEntity<CuentaResponseDTO> obtenerPorNumero(@PathVariable String numeroCuenta) {
        CuentaResponseDTO cuenta = cuentaService.obtenerPorNumero(numeroCuenta);
        if (cuenta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/tipo/{tipoCuenta}")
    public ResponseEntity<List<CuentaResponseDTO>> listarPorTipo(@PathVariable TipoCuenta tipoCuenta) {
        return ResponseEntity.ok(cuentaService.listarPorTipo(tipoCuenta));
    }

    @GetMapping("/{id}/detalle")
    public ResponseEntity<CuentaDetalleResponseDTO> obtenerCuentaDetalle(@PathVariable Long id) {
        CuentaDetalleResponseDTO cuenta = cuentaService.obtenerCuentaDetalle(id);
        if (cuenta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/{id}/transacciones")
    public ResponseEntity<List<TransaccionResponseDTO>> listarTransaccionesCuenta(@PathVariable Long id) {
        if (cuentaService.obtenerCuentaId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transaccionService.listarPorCuenta(id));
    }

    @PostMapping
    public ResponseEntity<CuentaResponseDTO> crearCuenta(@Valid @RequestBody CuentaRequestDTO cuentaRequestDTO) {
        CuentaResponseDTO nuevaCuenta = cuentaService.crearCuenta(cuentaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCuenta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> actualizarCuenta(@PathVariable Long id,
                                                              @Valid @RequestBody CuentaRequestDTO cuentaRequestDTO) {
        CuentaResponseDTO actualizada = cuentaService.actualizarCuenta(id, cuentaRequestDTO);
        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCuenta(@PathVariable Long id) {
        boolean eliminada = cuentaService.eliminarCuenta(id);
        if (eliminada) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
