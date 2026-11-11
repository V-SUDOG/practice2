package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Pruebas de VehiculoIndustrial. El enunciado no fija sus precios base, por lo que aquí solo se exige
 * que cada categoría tenga un precio base positivo.
 */
public class VehiculoIndustrialTest {

    @Test
    public void elConstructorGuardaCapacidadDeCargaYNumeroDeEjes() {
        VehiculoIndustrial v = new VehiculoIndustrial("CNA2019", Categoria.NORMAL, 2019, 1400, 2);

        assertEquals(1400, v.getCapacidadCarga());
        assertEquals(2, v.getNumeroEjes());
    }

    @Test
    public void esUnVehiculoDeTipoVehiculoIndustrial() {
        Vehiculo v = new VehiculoIndustrial("CNA2019", Categoria.NORMAL, 2019, 1400, 2);

        assertEquals("VehiculoIndustrial", v.getTipo());
    }

    @Test
    public void todasLasCategoriasTienenPrecioBasePositivo() {
        for (Categoria categoria : Categoria.values()) {
            VehiculoIndustrial v = new VehiculoIndustrial("CNA2019", categoria, 2019, 1400, 2);
            assertTrue("Categoría " + categoria, v.getPrecioBase() > 0);
        }
    }
}
