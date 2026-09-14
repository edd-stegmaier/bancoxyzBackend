package com.duoc.bancoxyz.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDetalleResponseDTO {

    private Long id;
    private String nombre;
    private Integer edad;
    private String email;
    private String rut;
    private List<CuentaDetalleResponseDTO> cuentas;

}
