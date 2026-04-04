package com.btg.fondos.domain.model;

//import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "fondo")
public class Fondo {
    @Id
    private String id;
    private String nombre;
    private Double montoMinimo;
    private String categoria;
}
