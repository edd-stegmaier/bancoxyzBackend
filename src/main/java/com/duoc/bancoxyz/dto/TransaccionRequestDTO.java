package com.duoc.bancoxyz.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.duoc.bancoxyz.model.TipoTransaccion;

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
public class TransaccionRequestDTO {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El tipo de transaccion es obligatorio")
    private TipoTransaccion tipo;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 255, message = "La descripcion no puede exceder los 255 caracteres")
    private String descripcion;

    @NotNull(message = "El id de la cuenta es obligatorio")
    private Long cuentaId;

}
