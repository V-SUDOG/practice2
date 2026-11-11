package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static telem.AyudaPruebas.assertLanza;
import static telem.AyudaPruebasPrecios.precio;

import org.junit.Test;

/**
 * Pruebas de FactoriaPoliticas: que devuelva la estrategia correcta para cada política, que gestione
 * denominaciones desconocidas y entradas nulas, y que los objetos creados sean utilizables.
 * Los cálculos de precios ya se prueban en los tests de cada calculador.
 */
public class FactoriaPoliticasTest {

    @Test
    public void crearPoliticaNormalDevuelveCalculadorPrecioNormal() {
        assertEquals(CalculadorPrecioNormal.class,
                FactoriaPoliticas.crearPolitica(PoliticasDePrecios.Normal).getClass());
    }

    @Test
    public void crearPoliticaTemporadaAltaDevuelveCalculadorPrecioTemporadaAlta() {
        assertEquals(CalculadorPrecioTemporadaAlta.class,
                FactoriaPoliticas.crearPolitica(PoliticasDePrecios.TemporadaAlta).getClass());
    }

    @Test
    public void crearPoliticaOfertonDevuelveCalculadorPrecioOferton() {
        assertEquals(CalculadorPrecioOferton.class,
                FactoriaPoliticas.crearPolitica(PoliticasDePrecios.Oferton).getClass());
    }

    @Test
    public void crearPoliticaClienteFrecuenteDevuelveCalculadorPrecioClienteFrecuente() {
        assertEquals(CalculadorPrecioClienteFrecuente.class,
                FactoriaPoliticas.crearPolitica(PoliticasDePrecios.ClienteFrecuente).getClass());
    }

    /** Si se añade un valor nuevo al enumerado sin darle calculador, esta prueba falla. */
    @Test
    public void todosLosValoresDelEnumeradoTienenCalculador() {
        for (PoliticasDePrecios politica : PoliticasDePrecios.values()) {
            ICalculadorPrecio calculador = FactoriaPoliticas.crearPolitica(politica);
            assertNotNull(politica.name(), calculador);
        }
    }

    @Test
    public void crearPoliticaPorDenominacionDevuelveLaEstrategiaCorrecta() {
        String[] denominaciones = {"Normal", "TemporadaAlta", "Oferton", "ClienteFrecuente"};
        Class<?>[] esperadas = {
            CalculadorPrecioNormal.class,
            CalculadorPrecioTemporadaAlta.class,
            CalculadorPrecioOferton.class,
            CalculadorPrecioClienteFrecuente.class
        };

        for (int i = 0; i < denominaciones.length; i++) {
            assertEquals(denominaciones[i], esperadas[i],
                    FactoriaPoliticas.crearPolitica(denominaciones[i]).getClass());
        }
    }

    @Test
    public void laDenominacionIgnoraMayusculasYEspaciosExtremos() {
        assertEquals(CalculadorPrecioOferton.class, FactoriaPoliticas.crearPolitica("OFERTON").getClass());
        assertEquals(CalculadorPrecioTemporadaAlta.class,
                FactoriaPoliticas.crearPolitica("temporadaalta").getClass());
        assertEquals(CalculadorPrecioNormal.class, FactoriaPoliticas.crearPolitica("  Normal ").getClass());
    }

    @Test
    public void unaDenominacionDesconocidaLanzaIllegalArgumentExceptionIndicandoElNombre() {
        IllegalArgumentException e = assertLanza(IllegalArgumentException.class,
                () -> FactoriaPoliticas.crearPolitica("Invierno"));

        assertTrue(e.getMessage(), e.getMessage().contains("Invierno"));
    }

    @Test
    public void unaDenominacionVaciaOSoloConEspaciosEsDesconocida() {
        assertLanza(IllegalArgumentException.class, () -> FactoriaPoliticas.crearPolitica(""));
        assertLanza(IllegalArgumentException.class, () -> FactoriaPoliticas.crearPolitica("   "));
    }

    /** El enumerado se llama "Oferton" (sin tilde); la denominación con tilde no está reconocida. */
    @Test
    public void unaDenominacionConTildeDistintaDelEnumeradoEsDesconocida() {
        assertLanza(IllegalArgumentException.class, () -> FactoriaPoliticas.crearPolitica("Ofertón"));
    }

    @Test
    public void unaDenominacionNulaLanzaIllegalArgumentException() {
        assertLanza(IllegalArgumentException.class, () -> FactoriaPoliticas.crearPolitica((String) null));
    }

    @Test
    public void unaPoliticaNulaLanzaIllegalArgumentException() {
        assertLanza(IllegalArgumentException.class,
                () -> FactoriaPoliticas.crearPolitica((PoliticasDePrecios) null));
    }

    @Test
    public void laEstrategiaCreadaCalculaIgualQueLaCreadaDirectamente() {
        // Comprobación de integración mínima; el detalle de los cálculos se prueba en cada calculador.
        assertEquals(precio(new CalculadorPrecioClienteFrecuente(), Categoria.NORMAL, 4),
                precio(FactoriaPoliticas.crearPolitica("ClienteFrecuente"), Categoria.NORMAL, 4));
        assertEquals(252, precio(FactoriaPoliticas.crearPolitica(PoliticasDePrecios.Oferton), Categoria.NORMAL, 8));
    }

    @Test
    public void laEstrategiaCreadaPuedeAsignarseAUnAlquiler() {
        Alquiler alquiler = new Alquiler(AyudaPruebasPrecios.turismo(Categoria.NORMAL),
                new Cliente("Juan Perez", "123456789"),
                AyudaPruebasPrecios.INICIO, AyudaPruebasPrecios.fin(4));

        alquiler.setCalculadorPrecio(FactoriaPoliticas.crearPolitica(PoliticasDePrecios.TemporadaAlta));

        assertEquals(400, alquiler.calcularPrecioAlquiler(alquiler.getFechaInicio(), alquiler.getFechaFin()));
    }
}
