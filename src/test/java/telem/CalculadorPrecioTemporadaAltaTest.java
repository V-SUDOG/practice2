package telem;

import static org.junit.Assert.assertEquals;
import static telem.AyudaPruebasPrecios.DIAS_LIMITE;
import static telem.AyudaPruebasPrecios.comprobar;
import static telem.AyudaPruebasPrecios.precio;

import org.junit.Test;

/**
 * Política de temporada alta: se duplica el precio base diario (100, 160 y 200 al día).
 * Límites: 1, 3, 4, 7 y 8 días.
 */
public class CalculadorPrecioTemporadaAltaTest {

    private final ICalculadorPrecio calculador = new CalculadorPrecioTemporadaAlta();

    @Test
    public void categoriaNormal_1_3_4_7_8_dias() {
        comprobar(calculador, Categoria.NORMAL, DIAS_LIMITE, new int[] {100, 300, 400, 700, 800});
    }

    @Test
    public void categoriaEspecial_1_3_4_7_8_dias() {
        comprobar(calculador, Categoria.ESPECIAL, DIAS_LIMITE, new int[] {160, 480, 640, 1120, 1280});
    }

    @Test
    public void categoriaSuperior_1_3_4_7_8_dias() {
        comprobar(calculador, Categoria.SUPERIOR, DIAS_LIMITE, new int[] {200, 600, 800, 1400, 1600});
    }

    @Test
    public void unAlquilerLargoDeTreintaDias() {
        assertEquals(3000, precio(calculador, Categoria.NORMAL, 30));
    }

    @Test
    public void esSiempreElDobleDeLaPoliticaNormal() {
        ICalculadorPrecio normal = new CalculadorPrecioNormal();
        for (Categoria categoria : Categoria.values()) {
            for (int dias = 1; dias <= 10; dias++) {
                assertEquals(categoria + ", " + dias + " días",
                        2 * precio(normal, categoria, dias), precio(calculador, categoria, dias));
            }
        }
    }
}
