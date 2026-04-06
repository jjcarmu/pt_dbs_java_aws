package com.btg.fondos.domain.model;

import com.btg.fondos.domain.exception.SaldoInsuficienteException;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad principal de Dominio que representa a un Cliente.
 * <p>
 * Contiene el estado financiero del cliente y la lista de fondos a los que está suscrito.
 * Alberga las reglas de negocio críticas para suscribirse (vincularse) o cancelar (desvincularse)
 * un fondo, garantizando la integridad de su saldo disponible.
 * </p>
 * 
 * @author Jhon Javier Cardona Muñoz <jhonjcm2@gmail.com>
 * @version 1.0
 */
@Data
@Document(collection = "cliente")
public class Cliente {
    
    @Id
    private String id;
    
    private String nombre;
    
    private Double saldoDisponible;
    
    private String preferenciaNotificacion;
    
    private List<String> fondosSuscritos = new ArrayList<>();

    @Version
    private Long version;

    /**
     * Lógica de negocio encapsulada: Vincula (suscribe) al cliente a un fondo de inversión.
     * <p>
     * Verifica que el cliente posea un saldo disponible mayor o igual al monto mínimo 
     * requerido por el fondo. Si cumple la condición, resta el monto del saldo disponible
     * y añade el ID del fondo a la lista de fondos suscritos.
     * </p>
     * 
     * @param fondo El fondo de inversión al que el cliente desea suscribirse.
     * @throws SaldoInsuficienteException Si el saldo actual es menor al monto mínimo del fondo.
     */
    public void vincular(Fondo fondo) {
        if (this.saldoDisponible < fondo.getMontoMinimo()) {
            throw new SaldoInsuficienteException("No tiene saldo disponible para vincularse al fondo " + fondo.getNombre());
        }
        this.saldoDisponible -= fondo.getMontoMinimo();
        this.fondosSuscritos.add(fondo.getId());
    }

    /**
     * Lógica de negocio encapsulada: Desvincula (cancela) a un cliente de un fondo de inversión.
     * <p>
     * Reembolsa el monto mínimo del fondo al saldo disponible del cliente y remueve
     * el ID del fondo de su lista de suscripciones activas.
     * </p>
     * 
     * @param fondo El fondo de inversión que el cliente desea cancelar.
     */
    public void desvincular(Fondo fondo) {
        this.saldoDisponible += fondo.getMontoMinimo(); // El valor retorna al cliente
        this.fondosSuscritos.remove(fondo.getId());
    }
}
