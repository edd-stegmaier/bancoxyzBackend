package com.duoc.bancoxyz.dto;

import java.math.BigDecimal;
import java.util.List;

import com.duoc.bancoxyz.model.TipoCuenta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuentaDetalleResponseDTO {

    private Long id;
    private String numeroCuenta;
    private TipoCuenta tipoCuenta;
    private BigDecimal saldo;
    private Boolean activa;
    private ClienteResponseDTO cliente;
    private List<TransaccionResponseDTO> transacciones;

}
