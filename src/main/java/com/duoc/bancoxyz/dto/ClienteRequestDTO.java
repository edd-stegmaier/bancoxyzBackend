package com.duoc.bancoxyz.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRequestDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(max = 120, message = "El nombre del cliente no puede exceder los 120 caracteres")
    private String nombre;

    @NotNull(message = "La edad del cliente es obligatoria")
    @Min(value = 18, message = "La edad minima es 18")
    @Max(value = 99, message = "La edad maxima es 99")
    private Integer edad;

    @NotBlank(message = "El email del cliente es obligatorio")
    @Email(message = "El email no tiene un formato valido")
    @Size(max = 150, message = "El email no puede exceder los 150 caracteres")
    private String email;

    @Size(max = 20, message = "El RUT no puede exceder los 20 caracteres")
    private String rut;

}
