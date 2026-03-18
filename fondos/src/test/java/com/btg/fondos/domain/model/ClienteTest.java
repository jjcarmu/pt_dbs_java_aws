package com.btg.fondos.domain.model;

import com.btg.fondos.domain.exception.SaldoInsuficienteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    private Cliente cliente;
    private Fondo fondo;

    @BeforeEach
    void setUp() {
        // Configuramos un cliente con el monto inicial exigido por la regla de negocio
        cliente = new Cliente();
        cliente.setId("cliente-1");
        cliente.setNombre("John Doe");
        cliente.setSaldoDisponible(500000.0); // Monto inicial: COP $500.000 [cite: 14]

        // Configuramos un fondo de prueba [cite: 21]
        fondo = new Fondo();
        fondo.setId("1");
        fondo.setNombre("FPV_BTG_PACTUAL_RECAUDADORA"); // [cite: 21]
        fondo.setMontoMinimo(75000.0); // [cite: 21]
    }

    @Test
    void vincular_ConSaldoSuficiente_DebeRestarSaldoYAgregarFondo() {
        // Act (Ejecutar)
        cliente.vincular(fondo);

        // Assert (Verificar)
        assertEquals(425000.0, cliente.getSaldoDisponible());
        assertTrue(cliente.getFondosSuscritos().contains("1"));
    }

    @Test
    void vincular_SinSaldoSuficiente_DebeLanzarExcepcionConMensajeExacto() {
        // Arrange: Dejamos al cliente sin saldo suficiente [cite: 18]
        cliente.setSaldoDisponible(50000.0);

        // Act & Assert
        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class, () -> {
            cliente.vincular(fondo);
        });

        // Verificamos que el mensaje sea exactamente el solicitado por BTG Pactual [cite: 19]
        assertEquals("No tiene saldo disponible para vincularse al fondo FPV_BTG_PACTUAL_RECAUDADORA", exception.getMessage());
    }

    @Test
    void desvincular_DebeRetornarValorAlCliente() {
        // Arrange: Primero lo vinculamos [cite: 17]
        cliente.vincular(fondo);
        assertEquals(425000.0, cliente.getSaldoDisponible());

        // Act: Lo cancelamos [cite: 17]
        cliente.desvincular(fondo);

        // Assert: El saldo debe volver a 500.000 y el fondo desaparecer de la lista [cite: 14, 17]
        assertEquals(500000.0, cliente.getSaldoDisponible());
        assertFalse(cliente.getFondosSuscritos().contains("1"));
    }
}