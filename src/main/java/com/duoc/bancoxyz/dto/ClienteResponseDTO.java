package com.duoc.bancoxyz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponseDTO {

    private Long id;
    private String nombre;
    private Integer edad;
    private String email;
    private String rut;

}
