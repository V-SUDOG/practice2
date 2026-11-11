package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static telem.AyudaPruebasPrecios.DIAS_LIMITE;
import static telem.AyudaPruebasPrecios.comprobar;
import static telem.AyudaPruebasPrecios.precio;

import org.junit.Test;

/**
 * Política ofertón: 30 % de descuento sobre el precio base (35, 56 y 70 al día) y, si el alquiler dura
 * más de 7 días, un 10 % adicional sobre el total.
 * Límites: 1, 3, 4, 7 (sin descuento adicional) y 8 días (con él).
 */
public class CalculadorPrecioOfertonTest {

    private final ICalculadorPrecio calculador = new CalculadorPrecioOferton();

    @Test
    public void categoriaNormal_1_3_4_7_8_dias() {
        comprobar(calculador, Categoria.NORMAL, DIAS_LIMITE, new int[] {35, 105, 140, 245, 252});
    }

    @Test
    public void categoriaEspecial_1_3_4_7_8_dias() {
        comprobar(calculador, Categoria.ESPECIAL, DIAS_LIMITE, new int[] {56, 168, 224, 392, 403});
    }

    @Test
    public void categoriaSuperior_1_3_4_7_8_dias() {
        comprobar(calculador, Categoria.SUPERIOR, DIAS_LIMITE, new int[] {70, 210, 280, 490, 504});
    }

    @Test
    public void unAlquilerLargoDeTreintaDias() {
        assertEquals(945, precio(calculador, Categoria.NORMAL, 30));
    }

    @Test
    public void conExactamenteSieteDiasNoSeAplicaElDescuentoAdicional() {
        // 7 días x (50 - 30 %) = 7 x 35
        assertEquals(7 * 35, precio(calculador, Categoria.NORMAL, 7));
    }

    @Test
    public void conOchoDiasElDescuentoAdicionalSeAplicaSobreElTotal() {
        // Total con el 30 %: 8 x 35 = 280; con el 10 % adicional sobre el total: 280 x 0,9 = 252
        assertEquals(252, precio(calculador, Categoria.NORMAL, 8));
    }

    @Test
    public void ochoDiasCuestanMasQueSieteAunqueSeAplicaElDescuentoAdicional() {
        assertTrue(precio(calculador, Categoria.NORMAL, 8) > precio(calculador, Categoria.NORMAL, 7));
    }

    /**
     * El enunciado no especifica el redondeo. Se redondea una sola vez, al final, al entero más próximo
     * y los .5 hacia arriba.
     */
    @Test
    public void redondeaAlEnteroMasProximoYLosMediosHaciaArriba() {
        // 8 días, ESPECIAL: 8 x 56 x 0,9 = 403,2 -> baja
        assertEquals(403, precio(calculador, Categoria.ESPECIAL, 8));
        // 9 días, NORMAL: 9 x 35 x 0,9 = 283,5 -> medio: sube
        assertEquals(284, precio(calculador, Categoria.NORMAL, 9));
        // 9 días, ESPECIAL: 9 x 56 x 0,9 = 453,6 -> sube
        assertEquals(454, precio(calculador, Categoria.ESPECIAL, 9));
        // 11 días, NORMAL: 11 x 35 x 0,9 = 346,5 -> medio: sube
        assertEquals(347, precio(calculador, Categoria.NORMAL, 11));
    }
}
