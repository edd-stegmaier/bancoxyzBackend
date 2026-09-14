package com.duoc.bancoxyz.dto;

import java.math.BigDecimal;

import com.duoc.bancoxyz.model.TipoCuenta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuentaResponseDTO {

    private Long id;
    private String numeroCuenta;
    private TipoCuenta tipoCuenta;
    private BigDecimal saldo;
    private Boolean activa;
    private Long clienteId;
    private String clienteNombre;

}
