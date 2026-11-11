package telem;

import static org.junit.Assert.assertEquals;
import static telem.AyudaPruebasPrecios.DIAS_LIMITE;
import static telem.AyudaPruebasPrecios.INICIO;
import static telem.AyudaPruebasPrecios.comprobar;
import static telem.AyudaPruebasPrecios.fin;
import static telem.AyudaPruebasPrecios.precio;

import org.junit.Test;

/**
 * Política normal: precio base diario x número de días. Límites: 1, 3, 4, 7 y 8 días.
 * Precio base de un turismo: NORMAL 50, ESPECIAL 80, SUPERIOR 100.
 */
public class CalculadorPrecioNormalTest {

    private final ICalculadorPrecio calculador = new CalculadorPrecioNormal();

    @Test
    public void categoriaNormal_1_3_4_7_8_dias() {
        comprobar(calculador, Categoria.NORMAL, DIAS_LIMITE, new int[] {50, 150, 200, 350, 400});
    }

    @Test
    public void categoriaEspecial_1_3_4_7_8_dias() {
        comprobar(calculador, Categoria.ESPECIAL, DIAS_LIMITE, new int[] {80, 240, 320, 560, 640});
    }

    @Test
    public void categoriaSuperior_1_3_4_7_8_dias() {
        comprobar(calculador, Categoria.SUPERIOR, DIAS_LIMITE, new int[] {100, 300, 400, 700, 800});
    }

    @Test
    public void unAlquilerLargoDeTreintaDias() {
        assertEquals(1500, precio(calculador, Categoria.NORMAL, 30));
    }

    @Test
    public void usaElPrecioBaseDelVehiculoDeCadaTipo() {
        Vehiculo[] vehiculos = {
            new Turismo("T-001", Categoria.SUPERIOR, 2024, 5, 450.0),
            new Motocicleta("M-001", Categoria.ESPECIAL, 2026, 1000),
            new VehiculoIndustrial("I-001", Categoria.NORMAL, 2019, 1400, 2)
        };
        for (Vehiculo v : vehiculos) {
            int esperado = v.getPrecioBase() * 5;
            assertEquals(v.getTipo(), esperado, calculador.calcularPrecioAlquiler(INICIO, fin(5), v));
        }
    }
}
