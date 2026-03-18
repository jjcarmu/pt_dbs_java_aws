package com.btg.fondos.application.service;

import com.btg.fondos.application.factory.NotificacionFactory;
import com.btg.fondos.domain.model.Cliente;
import com.btg.fondos.domain.model.Fondo;
import com.btg.fondos.domain.model.Transaccion;
import com.btg.fondos.domain.port.ClienteRepository;
import com.btg.fondos.domain.port.FondoRepository;
import com.btg.fondos.domain.port.NotificacionPort;
import com.btg.fondos.domain.port.TransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdministracionFondosServiceTest {

    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private FondoRepository fondoRepository;
    @Mock
    private TransaccionRepository transaccionRepository;
    @Mock
    private NotificacionFactory notificacionFactory;
    @Mock
    private NotificacionPort notificacionPort;

    @InjectMocks
    private AdministracionFondosService service;

    private Cliente cliente;
    private Fondo fondo;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId("cliente-1");
        cliente.setNombre("John");
        cliente.setSaldoDisponible(500000.0); // [cite: 14]
        cliente.setPreferenciaNotificacion("EMAIL"); // [cite: 12]

        fondo = new Fondo();
        fondo.setId("1");
        fondo.setMontoMinimo(75000.0); // [cite: 21]
        fondo.setNombre("FPV_BTG_PACTUAL_RECAUDADORA"); // [cite: 21]
    }

    @Test
    void suscribirFondo_FlujoExitoso_GuardaCambiosYEnviaNotificacion() {

        when(clienteRepository.findById("cliente-1")).thenReturn(Optional.of(cliente));
        when(fondoRepository.findById("1")).thenReturn(Optional.of(fondo));
        when(notificacionFactory.obtenerEstrategia("EMAIL")).thenReturn(notificacionPort);

        service.suscribirFondo("cliente-1", "1");

        // Assert: Verificamos que se guardó el cliente con el saldo restado
        verify(clienteRepository, times(1)).save(cliente);
        assertEquals(425000.0, cliente.getSaldoDisponible());

        // Assert: Capturamos la transacción para verificar que se guardó como APERTURA [cite: 11]
        ArgumentCaptor<Transaccion> transaccionCaptor = ArgumentCaptor.forClass(Transaccion.class);
        verify(transaccionRepository, times(1)).save(transaccionCaptor.capture());
        assertEquals(Transaccion.TipoTransaccion.APERTURA, transaccionCaptor.getValue().getTipo()); // [cite: 11]

        // Assert: Verificamos que se llamó al envío de notificación [cite: 12]
        verify(notificacionPort, times(1)).enviar(eq("cliente-1"), anyString());
    }
}