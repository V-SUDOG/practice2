package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static telem.AyudaPruebasPrecios.DIAS_LIMITE;
import static telem.AyudaPruebasPrecios.comprobar;
import static telem.AyudaPruebasPrecios.precio;

import org.junit.Test;

/**
 * Política de cliente frecuente: 20 % de descuento los tres primeros días y 40 % a partir del cuarto,
 * aplicado por tramos. Primeros 3 días: 40, 64 y 80 al día; a partir del cuarto: 30, 48 y 60.
 * Límites: 1, 3 (último del primer tramo), 4 (primero del segundo), 7 y 8 días.
 */
public class CalculadorPrecioClienteFrecuenteTest {

    private final ICalculadorPrecio calculador = new CalculadorPrecioClienteFrecuente();

    @Test
    public void categoriaNormal_1_3_4_7_8_dias() {
        comprobar(calculador, Categoria.NORMAL, DIAS_LIMITE, new int[] {40, 120, 150, 240, 270});
    }

    @Test
    public void categoriaEspecial_1_3_4_7_8_dias() {
        comprobar(calculador, Categoria.ESPECIAL, DIAS_LIMITE, new int[] {64, 192, 240, 384, 432});
    }

    @Test
    public void categoriaSuperior_1_3_4_7_8_dias() {
        comprobar(calculador, Categoria.SUPERIOR, DIAS_LIMITE, new int[] {80, 240, 300, 480, 540});
    }

    @Test
    public void unAlquilerLargoDeTreintaDias() {
        // 3 x 40 + 27 x 30
        assertEquals(930, precio(calculador, Categoria.NORMAL, 30));
    }

    @Test
    public void cuatroDiasNoSonUnDescuentoUnicoParaTodosLosDias() {
        int precio = precio(calculador, Categoria.NORMAL, 4);

        assertEquals(3 * 40 + 1 * 30, precio);
        // Un descuento único habría dado 4 x 40 = 160 (20 %) o 4 x 30 = 120 (40 %)
        assertFalse(precio == 4 * 40 || precio == 4 * 30);
    }

    @Test
    public void cadaDiaAdicionalCuestaLoQueLeCorrespondeASuTramo() {
        // Precio base 50: los días 1 a 3 suman 40 cada uno; del día 4 en adelante, 30 cada uno
        assertEquals(40, precio(calculador, Categoria.NORMAL, 1));
        assertEquals(40, precio(calculador, Categoria.NORMAL, 2) - precio(calculador, Categoria.NORMAL, 1));
        assertEquals(40, precio(calculador, Categoria.NORMAL, 3) - precio(calculador, Categoria.NORMAL, 2));
        for (int dias = 4; dias <= 12; dias++) {
            assertEquals("día " + dias, 30,
                    precio(calculador, Categoria.NORMAL, dias) - precio(calculador, Categoria.NORMAL, dias - 1));
        }
    }
}
