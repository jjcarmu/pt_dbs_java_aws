package com.btg.fondos.domain.model;

import lombok.Data;

@Data
public class Fondo {
    private String id;
    private String nombre;
    private Double montoMinimo;
    private String categoria;
}