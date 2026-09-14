package com.duoc.bancoxyz.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.duoc.bancoxyz.model.TipoTransaccion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionResponseDTO {

    private Long id;
    private LocalDate fecha;
    private TipoTransaccion tipo;
    private BigDecimal monto;
    private String descripcion;
    private Long cuentaId;
    private String numeroCuenta;

}
