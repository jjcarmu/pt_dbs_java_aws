package com.btg.fondos.application.service;

import com.btg.fondos.domain.model.Cliente;
import com.btg.fondos.domain.model.Fondo;
import com.btg.fondos.domain.port.ClienteRepository;
import com.btg.fondos.domain.port.FondoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AdministracionFondosServiceConcurrencyTest {

    @Autowired
    private AdministracionFondosService service;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FondoRepository fondoRepository;

    private final String CLIENTE_ID = "cliente-concurrencia-001";
    private final String FONDO_ID = "fondo-concurrencia-1";

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de ejecutar la prueba
        if (clienteRepository.findById(CLIENTE_ID).isPresent()) {
            clienteRepository.deleteById(CLIENTE_ID);
        }
        if (fondoRepository.findById(FONDO_ID).isPresent()) {
            fondoRepository.deleteById(FONDO_ID);
        }

        // 1. Preparar un cliente con el saldo base
        Cliente cliente = new Cliente();
        cliente.setId(CLIENTE_ID);
        cliente.setNombre("Usuario Concurrente");
        cliente.setSaldoDisponible(500000.0);
        clienteRepository.save(cliente);

        // 2. Preparar un fondo con valor de 100.000
        Fondo fondo = new Fondo();
        fondo.setId(FONDO_ID);
        fondo.setNombre("Fondo Prueba Concurrencia");
        fondo.setMontoMinimo(100000.0);
        fondoRepository.save(fondo);
    }

    @AfterEach
    void tearDown() {
        // Limpiar la base de datos después de la prueba
        clienteRepository.deleteById(CLIENTE_ID);
        fondoRepository.deleteById(FONDO_ID);
    }

    @Test
    void suscribirFondo_PeticionesConcurrentes_DebeBloquearDobleGasto() {

        // 1. Hilo A lee el cliente
        Cliente clienteLeidoPorHiloA = clienteRepository.findById(CLIENTE_ID).orElseThrow();
        
        // 2. Hilo B lee el cliente (tiene exactamente la misma versión que A)
        Cliente clienteLeidoPorHiloB = clienteRepository.findById(CLIENTE_ID).orElseThrow();

        // 3. Hilo A procesa su suscripción y guarda (Esto funciona)
        clienteLeidoPorHiloA.setSaldoDisponible(clienteLeidoPorHiloA.getSaldoDisponible() - 100000.0);
        clienteRepository.save(clienteLeidoPorHiloA);

        // 4. Hilo B intenta hacer lo mismo con los datos viejos que leyó.
        // Aquí es donde el Bloqueo Optimista (@Version) DEBE intervenir y lanzar la excepción.
        clienteLeidoPorHiloB.setSaldoDisponible(clienteLeidoPorHiloB.getSaldoDisponible() - 100000.0);
        
        assertThrows(OptimisticLockingFailureException.class, () -> {
            clienteRepository.save(clienteLeidoPorHiloB);
        }, "Se esperaba que fallara al intentar guardar una versión desactualizada del cliente");

        // 5. Verificamos que el saldo final sea 400.000 y no 300.000
        Cliente clienteFinal = clienteRepository.findById(CLIENTE_ID).orElseThrow();
        assertEquals(400000.0, clienteFinal.getSaldoDisponible(), "El saldo solo debió descontarse una vez");
        assertEquals(1, clienteFinal.getVersion(), "La versión del documento solo debió incrementar en 1");
    }
}
