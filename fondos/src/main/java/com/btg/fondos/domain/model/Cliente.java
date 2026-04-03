package com.btg.fondos.domain.model;

import com.btg.fondos.domain.exception.SaldoInsuficienteException;
//import lombok.Builder;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
//@Builder
public class Cliente {
    private String id;
    private String nombre;
    private Double saldoDisponible;
    private String preferenciaNotificacion;
    private List<String> fondosSuscritos = new ArrayList<>();

    // Lógica de negocio encapsulada: Apertura
    public void vincular(Fondo fondo) {
        if (this.saldoDisponible < fondo.getMontoMinimo()) {
            // Cumpliendo exactamente con el mensaje requerido por negocio
            throw new SaldoInsuficienteException("No tiene saldo disponible para vincularse al fondo " + fondo.getNombre());
        }
        this.saldoDisponible -= fondo.getMontoMinimo();
        this.fondosSuscritos.add(fondo.getId());
    }

    // Lógica de negocio encapsulada: Cancelación
    public void desvincular(Fondo fondo) {
        this.saldoDisponible += fondo.getMontoMinimo(); // El valor retorna al cliente
        this.fondosSuscritos.remove(fondo.getId());
    }
}