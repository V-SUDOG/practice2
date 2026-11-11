package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas de Alquiler: construcción, consulta, registro de alquileres y cálculo de fechas y precio.
 * Los precios se comprueban con un Turismo de categoría NORMAL (precio base 50) y la política normal
 * (días de alquiler x precio base), que es el comportamiento por defecto de Alquiler.
 * Los días se cuentan de forma inclusiva: inicio y fin cuentan, de modo que inicio == fin es 1 día.
 */
public class AlquilerTest {

    private static final LocalDate INICIO = LocalDate.of(2026, 11, 9);

    private Vehiculo turismo;
    private Cliente cliente;

    @Before
    public void preparar() {
        turismo = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
        cliente = new Cliente("Juan Perez", "123456789");
    }

    private Alquiler alquiler(LocalDate inicio, LocalDate fin) {
        return new Alquiler(turismo, cliente, inicio, fin);
    }

    @Test
    public void elConstructorGuardaVehiculoClienteYFechas() {
        Alquiler a = alquiler(INICIO, INICIO.plusDays(3));

        assertSame(turismo, a.getVehiculo());
        assertSame(cliente, a.getCliente());
        assertEquals(INICIO, a.getFechaInicio());
        assertEquals(INICIO.plusDays(3), a.getFechaFin());
    }

    /** Prueba de caracterización: el constructor actual no valida que fin sea posterior a inicio. */
    @Test
    public void elConstructorActualNoValidaElOrdenDeLasFechas() {
        Alquiler a = alquiler(INICIO, INICIO.minusDays(5));

        assertEquals(INICIO.minusDays(5), a.getFechaFin());
    }

    @Test
    public void elPrecioEsDiasInclusivosPorElPrecioBase() {
        // días entre las fechas -> precio esperado (inicio == fin es 1 día)
        int[] diasEntreFechas = {0, 1, 2, 3, 6, 7, 29};
        int[] esperados = {50, 100, 150, 200, 350, 400, 1500};

        for (int i = 0; i < diasEntreFechas.length; i++) {
            LocalDate fin = INICIO.plusDays(diasEntreFechas[i]);
            Alquiler a = alquiler(INICIO, fin);

            assertEquals(diasEntreFechas[i] + " días entre inicio y fin",
                    esperados[i], a.calcularPrecioAlquiler(INICIO, fin));
        }
    }

    @Test
    public void elCalculoDeFechasRespetaElAnnioBisiesto() {
        Alquiler a = alquiler(INICIO, INICIO.plusDays(1));

        // 28-feb, 29-feb y 1-mar de 2028 (bisiesto): 3 días
        assertEquals(150, a.calcularPrecioAlquiler(LocalDate.of(2028, 2, 28), LocalDate.of(2028, 3, 1)));
        // 28-feb y 1-mar de 2027 (no bisiesto): 2 días
        assertEquals(100, a.calcularPrecioAlquiler(LocalDate.of(2027, 2, 28), LocalDate.of(2027, 3, 1)));
    }

    @Test
    public void elCalculoDeFechasCruzaElCambioDeAnnio() {
        Alquiler a = alquiler(INICIO, INICIO.plusDays(1));

        // 30-dic, 31-dic, 1-ene y 2-ene: 4 días
        assertEquals(200, a.calcularPrecioAlquiler(LocalDate.of(2026, 12, 30), LocalDate.of(2027, 1, 2)));
    }

    @Test
    public void elPrecioSeCalculaConLasFechasRecibidasNoConLasDelAlquiler() {
        Alquiler a = alquiler(INICIO, INICIO); // alquiler de 1 día

        assertEquals(250, a.calcularPrecioAlquiler(INICIO, INICIO.plusDays(4))); // 5 días
    }

    @Test
    public void agregarAlquilerLoRegistraEnElArrayEstatico() {
        Alquiler a = alquiler(INICIO, INICIO.plusDays(2));

        Alquiler.agregarAlquiler(a);

        boolean registrado = false;
        for (Alquiler registro : Alquiler.obtenerAlquileres()) {
            if (registro == a) {
                registrado = true;
            }
        }
        assertTrue("El alquiler agregado debe aparecer en obtenerAlquileres()", registrado);
    }

    @Test
    public void obtenerAlquileresDevuelveSiempreUnArray() {
        assertNotNull(Alquiler.obtenerAlquileres());
    }

    @Test
    public void toStringDevuelveJsonConClienteVehiculoYFechas() {
        Alquiler a = alquiler(INICIO, INICIO.plusDays(3));

        String json = a.toString();

        assertTrue(json, json.contains("\"nombre\":\"Juan Perez\""));
        assertTrue(json, json.contains("\"matricula\":\"ABC123\""));
        assertTrue(json, json.contains("\"fechaInicio\":\"2026-11-09\""));
        assertTrue(json, json.contains("\"fechaFin\":\"2026-11-12\""));
    }
}
