package com.duoc.bancoxyz.dto;

import java.math.BigDecimal;

import com.duoc.bancoxyz.model.TipoCuenta;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaRequestDTO {

    @NotBlank(message = "El numero de cuenta es obligatorio")
    @Size(max = 20, message = "El numero de cuenta no puede exceder los 20 caracteres")
    private String numeroCuenta;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private TipoCuenta tipoCuenta;

    @NotNull(message = "El saldo es obligatorio")
    @DecimalMin(value = "0.00", message = "El saldo no puede ser negativo")
    private BigDecimal saldo;

    private Boolean activa = true;

    @NotNull(message = "El id del cliente es obligatorio")
    private Long clienteId;

}
