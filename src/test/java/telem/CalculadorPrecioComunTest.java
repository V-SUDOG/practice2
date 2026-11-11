package telem;

import static org.junit.Assert.assertTrue;
import static telem.AyudaPruebas.assertLanza;
import static telem.AyudaPruebasPrecios.INICIO;
import static telem.AyudaPruebasPrecios.precio;
import static telem.AyudaPruebasPrecios.turismo;

import java.time.LocalDate;

import org.junit.Test;

/**
 * Comportamiento común a las cuatro políticas: validación de entradas y propiedades generales.
 * Se recorren como ICalculadorPrecio, sin depender de la clase concreta (polimorfismo).
 */
public class CalculadorPrecioComunTest {

    private final ICalculadorPrecio[] politicas = {
        new CalculadorPrecioNormal(),
        new CalculadorPrecioTemporadaAlta(),
        new CalculadorPrecioOferton(),
        new CalculadorPrecioClienteFrecuente()
    };

    @Test
    public void unAlquilerDeUnDiaTienePrecioPositivo() {
        for (ICalculadorPrecio p : politicas) {
            int precio = p.calcularPrecioAlquiler(INICIO, INICIO, turismo(Categoria.NORMAL));
            assertTrue(p.getClass().getSimpleName(), precio > 0);
        }
    }

    @Test
    public void laFechaDeInicioNulaEsRechazada() {
        for (ICalculadorPrecio p : politicas) {
            assertLanza(IllegalArgumentException.class,
                    () -> p.calcularPrecioAlquiler(null, INICIO, turismo(Categoria.NORMAL)));
        }
    }

    @Test
    public void laFechaDeFinNulaEsRechazada() {
        for (ICalculadorPrecio p : politicas) {
            assertLanza(IllegalArgumentException.class,
                    () -> p.calcularPrecioAlquiler(INICIO, null, turismo(Categoria.NORMAL)));
        }
    }

    @Test
    public void elVehiculoNuloEsRechazado() {
        for (ICalculadorPrecio p : politicas) {
            assertLanza(IllegalArgumentException.class,
                    () -> p.calcularPrecioAlquiler(INICIO, INICIO.plusDays(2), null));
        }
    }

    @Test
    public void unaFechaDeFinAnteriorALaDeInicioEsRechazada() {
        LocalDate finAnterior = INICIO.minusDays(1);
        for (ICalculadorPrecio p : politicas) {
            assertLanza(IllegalArgumentException.class,
                    () -> p.calcularPrecioAlquiler(INICIO, finAnterior, turismo(Categoria.NORMAL)));
        }
    }

    @Test
    public void elPrecioNuncaBajaAlAumentarLosDiasDeAlquiler() {
        for (ICalculadorPrecio p : politicas) {
            for (Categoria categoria : Categoria.values()) {
                for (int dias = 2; dias <= 40; dias++) {
                    assertTrue(p.getClass().getSimpleName() + ", " + categoria + ", " + dias + " días",
                            precio(p, categoria, dias) >= precio(p, categoria, dias - 1));
                }
            }
        }
    }
}
