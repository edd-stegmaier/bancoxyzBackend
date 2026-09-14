package com.duoc.bancoxyz.controller;

import java.time.LocalDate;
import java.util.List;

import com.duoc.bancoxyz.dto.TransaccionRequestDTO;
import com.duoc.bancoxyz.dto.TransaccionResponseDTO;
import com.duoc.bancoxyz.model.TipoTransaccion;
import com.duoc.bancoxyz.service.TransaccionService;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("El servicio de Transacciones se encuentra funcionando correctamente");
    }

    @GetMapping
    public ResponseEntity<List<TransaccionResponseDTO>> listarTransacciones() {
        return ResponseEntity.ok(transaccionService.listarTransacciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransaccionResponseDTO> obtenerTransaccionId(@PathVariable Long id) {
        TransaccionResponseDTO transaccion = transaccionService.obtenerTransaccionId(id);
        if (transaccion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transaccion);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<TransaccionResponseDTO>> listarPorTipo(@PathVariable TipoTransaccion tipo) {
        return ResponseEntity.ok(transaccionService.listarPorTipo(tipo));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<TransaccionResponseDTO>> listarPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(transaccionService.listarPorFecha(fecha));
    }

    @PostMapping
    public ResponseEntity<TransaccionResponseDTO> crearTransaccion(
            @Valid @RequestBody TransaccionRequestDTO transaccionRequestDTO) {
        TransaccionResponseDTO nueva = transaccionService.crearTransaccion(transaccionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransaccionResponseDTO> actualizarTransaccion(
            @PathVariable Long id,
            @Valid @RequestBody TransaccionRequestDTO transaccionRequestDTO) {
        TransaccionResponseDTO actualizada = transaccionService.actualizarTransaccion(id, transaccionRequestDTO);
        if (actualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTransaccion(@PathVariable Long id) {
        boolean eliminada = transaccionService.eliminarTransaccion(id);
        if (eliminada) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
