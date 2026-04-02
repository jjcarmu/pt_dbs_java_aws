package com.btg.fondos.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Fondo {
    private String id;
    private String nombre;
    private Double montoMinimo;
    private String categoria;
}