package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Pruebas del comportamiento común de Vehiculo, comprobado a través de sus tres subclases
 * (Turismo, Motocicleta y VehiculoIndustrial) y usando referencias polimórficas.
 */
public class VehiculoTest {

    /** Crea un vehículo de cada subclase con los mismos datos comunes. */
    private Vehiculo[] unoDeCadaTipo(String matricula, Categoria categoria, int annio) {
        return new Vehiculo[] {
            new Turismo(matricula, categoria, annio, 5, 450.0),
            new Motocicleta(matricula, categoria, annio, 200),
            new VehiculoIndustrial(matricula, categoria, annio, 1400, 2)
        };
    }

    @Test
    public void losDatosComunesSeConservanEnTodasLasSubclases() {
        for (Vehiculo v : unoDeCadaTipo("1234ABC", Categoria.SUPERIOR, 2024)) {
            assertEquals(v.getTipo(), "1234ABC", v.getMatricula());
            assertEquals(v.getTipo(), Categoria.SUPERIOR, v.getCategoria());
            assertEquals(v.getTipo(), 2024, v.getAnnio());
        }
    }

    @Test
    public void laCategoriaSeConservaParaCadaValorDelEnumerado() {
        for (Categoria categoria : Categoria.values()) {
            Vehiculo v = new Turismo("T-001", categoria, 2020, 5, 450.0);
            assertEquals(categoria, v.getCategoria());
        }
    }

    @Test
    public void setDisponibleCambiaLaDisponibilidad() {
        Vehiculo v = new Motocicleta("M-001", Categoria.NORMAL, 2021, 200);

        v.setDisponible(true);
        assertTrue(v.isDisponible());

        v.setDisponible(false);
        assertFalse(v.isDisponible());
    }

    /**
     * FALLO CONOCIDO DEL CÓDIGO ORIGINAL (prueba ignorada a propósito):
     * el atributo disponible nunca se inicializa a true, así que todo vehículo nuevo nace "no disponible".
     * El JSON de ejemplo del enunciado (apartado 5.4) muestra "disponible":true para un vehículo recién
     * creado, y GestorAlquileres.menuPrincipal solo deja alquilar vehículos disponibles, por lo que con
     * el código actual no se puede alquilar ningún vehículo de ejemplo.
     * Se deja sin corregir porque no es objeto de los ejercicios 1, 2 y 4 (se comunica a la pareja).
     */
    @Ignore("Fallo conocido: un vehículo nuevo debería estar disponible (ver enunciado 5.4)")
    @Test
    public void unVehiculoNuevoDeberiaEstarDisponible() {
        for (Vehiculo v : unoDeCadaTipo("1234ABC", Categoria.NORMAL, 2020)) {
            assertTrue(v.getTipo(), v.isDisponible());
        }
    }

    /** Prueba de caracterización del comportamiento actual (ver prueba anterior). */
    @Test
    public void comportamientoActual_unVehiculoNuevoNaceNoDisponible() {
        for (Vehiculo v : unoDeCadaTipo("1234ABC", Categoria.NORMAL, 2020)) {
            assertFalse(v.getTipo(), v.isDisponible());
        }
    }

    @Test
    public void getTipoDevuelveElNombreDeLaSubclase() {
        Vehiculo[] vehiculos = unoDeCadaTipo("1234ABC", Categoria.NORMAL, 2020);

        assertEquals("Turismo", vehiculos[0].getTipo());
        assertEquals("Motocicleta", vehiculos[1].getTipo());
        assertEquals("VehiculoIndustrial", vehiculos[2].getTipo());
    }

    @Test
    public void getPrecioBaseSeResuelveEnTiempoDeEjecucionSegunLaSubclase() {
        Vehiculo turismo = new Turismo("T-001", Categoria.NORMAL, 2020, 5, 450.0);

        assertEquals(50, turismo.getPrecioBase());
        for (Vehiculo v : unoDeCadaTipo("1234ABC", Categoria.ESPECIAL, 2020)) {
            assertTrue(v.getTipo(), v.getPrecioBase() > 0);
        }
    }

    @Test
    public void toStringDevuelveJsonConTipoYMatricula() {
        Vehiculo v = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);

        String json = v.toString();

        assertTrue(json, json.contains("\"tipo\":\"Turismo\""));
        assertTrue(json, json.contains("\"matricula\":\"ABC123\""));
        assertTrue(json, json.contains("\"categoria\":\"NORMAL\""));
    }

    /** Prueba de caracterización: el constructor actual no valida la matrícula ni el año. */
    @Test
    public void elConstructorActualNoValidaSusArgumentos() {
        Vehiculo v = new Turismo(null, Categoria.NORMAL, -1, 5, 450.0);

        assertNull(v.getMatricula());
        assertEquals(-1, v.getAnnio());
    }

    /** Prueba de caracterización: con categoría nula, getPrecioBase falla en el switch. */
    @Test
    public void getPrecioBaseConCategoriaNulaLanzaNullPointerException() {
        Vehiculo v = new Turismo("T-001", null, 2020, 5, 450.0);

        AyudaPruebas.assertLanza(NullPointerException.class, () -> v.getPrecioBase());
    }
}
