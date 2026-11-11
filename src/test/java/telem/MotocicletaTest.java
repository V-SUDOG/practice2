package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Pruebas de Motocicleta. El enunciado no fija sus precios base, por lo que aquí solo se exige
 * que cada categoría tenga un precio base positivo.
 */
public class MotocicletaTest {

    @Test
    public void elConstructorGuardaLaCilindrada() {
        Motocicleta m = new Motocicleta("MNA2021", Categoria.NORMAL, 2021, 200);

        assertEquals(200, m.getCilindrada());
    }

    @Test
    public void esUnVehiculoDeTipoMotocicleta() {
        Vehiculo v = new Motocicleta("MNA2021", Categoria.NORMAL, 2021, 200);

        assertEquals("Motocicleta", v.getTipo());
    }

    @Test
    public void todasLasCategoriasTienenPrecioBasePositivo() {
        for (Categoria categoria : Categoria.values()) {
            Motocicleta m = new Motocicleta("MNA2021", categoria, 2021, 200);
            assertTrue("Categoría " + categoria, m.getPrecioBase() > 0);
        }
    }
}
