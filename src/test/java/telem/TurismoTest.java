package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Pruebas de Turismo: atributos propios, tipo y precio base (valores indicados en el enunciado, apartado 4).
 */
public class TurismoTest {

    @Test
    public void elConstructorGuardaPlazasYCapacidadDelMaletero() {
        Turismo t = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);

        assertEquals(5, t.getPlazas());
        assertEquals(400.0, t.getCapacidadMaletero(), 0.0001);
    }

    @Test
    public void esUnVehiculoDeTipoTurismo() {
        Turismo t = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);

        Vehiculo v = t; // un Turismo es un Vehiculo
        assertEquals("Turismo", v.getTipo());
    }

    @Test
    public void elPrecioBaseDeLaCategoriaNormalEs50() {
        assertEquals(50, new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0).getPrecioBase());
    }

    @Test
    public void elPrecioBaseDeLaCategoriaEspecialEs80() {
        assertEquals(80, new Turismo("ABC123", Categoria.ESPECIAL, 2020, 5, 400.0).getPrecioBase());
    }

    @Test
    public void elPrecioBaseDeLaCategoriaSuperiorEs100() {
        assertEquals(100, new Turismo("ABC123", Categoria.SUPERIOR, 2020, 5, 400.0).getPrecioBase());
    }

    @Test
    public void laCategoriaSuperiorEsLaMasCara() {
        int normal = new Turismo("A", Categoria.NORMAL, 2020, 5, 400.0).getPrecioBase();
        int especial = new Turismo("A", Categoria.ESPECIAL, 2020, 5, 400.0).getPrecioBase();
        int superior = new Turismo("A", Categoria.SUPERIOR, 2020, 5, 400.0).getPrecioBase();

        assertTrue(normal < especial && especial < superior);
    }
}
