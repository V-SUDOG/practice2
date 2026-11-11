package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static telem.AyudaPruebas.assertLanza;
import static telem.AyudaPruebasPrecios.INICIO;
import static telem.AyudaPruebasPrecios.fin;

import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas de Alquiler como contexto del patrón Estrategia: política por defecto, cambio de política
 * en tiempo de ejecución y aceptación de cualquier implementación de ICalculadorPrecio.
 */
public class AlquilerEstrategiaTest {

    private Cliente cliente;

    @Before
    public void preparar() {
        cliente = new Cliente("Juan Perez", "123456789");
    }

    /** Alquiler de 4 días de un turismo NORMAL (precio base 50). */
    private Alquiler alquilerDeCuatroDias() {
        return new Alquiler(AyudaPruebasPrecios.turismo(Categoria.NORMAL), cliente, INICIO, fin(4));
    }

    private int precio(Alquiler a) {
        return a.calcularPrecioAlquiler(a.getFechaInicio(), a.getFechaFin());
    }

    @Test
    public void porDefectoSeAplicaLaPoliticaNormal() {
        assertEquals(200, precio(alquilerDeCuatroDias()));
    }

    @Test
    public void laPoliticaNormalUsaElPrecioBaseDelVehiculoDelAlquiler() {
        Alquiler a = new Alquiler(AyudaPruebasPrecios.turismo(Categoria.ESPECIAL), cliente, INICIO, fin(4));

        assertEquals(4 * 80, precio(a));
    }

    @Test
    public void elPrecioCambiaAlCambiarLaPoliticaSinModificarAlquiler() {
        Alquiler a = alquilerDeCuatroDias();

        a.setCalculadorPrecio(new CalculadorPrecioTemporadaAlta());
        assertEquals(400, precio(a));

        a.setCalculadorPrecio(new CalculadorPrecioOferton());
        assertEquals(140, precio(a));

        a.setCalculadorPrecio(new CalculadorPrecioClienteFrecuente());
        assertEquals(150, precio(a));

        a.setCalculadorPrecio(new CalculadorPrecioNormal());
        assertEquals(200, precio(a));
    }

    @Test
    public void elConstructorPermiteIndicarLaPoliticaDesdeElPrincipio() {
        Alquiler a = new Alquiler(AyudaPruebasPrecios.turismo(Categoria.NORMAL), cliente, INICIO, fin(4),
                new CalculadorPrecioOferton());

        assertEquals(140, precio(a));
    }

    @Test
    public void cadaAlquilerTieneSuPropiaPolitica() {
        Alquiler a1 = alquilerDeCuatroDias();
        Alquiler a2 = alquilerDeCuatroDias();

        a1.setCalculadorPrecio(new CalculadorPrecioTemporadaAlta());

        assertEquals(400, precio(a1));
        assertEquals(200, precio(a2));
    }

    @Test
    public void unaPoliticaNulaEsRechazadaEnElSetterYEnElConstructor() {
        Alquiler a = alquilerDeCuatroDias();

        assertLanza(IllegalArgumentException.class, () -> a.setCalculadorPrecio(null));
        assertLanza(IllegalArgumentException.class, () -> new Alquiler(
                AyudaPruebasPrecios.turismo(Categoria.NORMAL), cliente, INICIO, fin(4), null));
        assertEquals(200, precio(a)); // la política anterior no se pierde
    }

    @Test
    public void sePuedeUsarCualquierImplementacionDeLaInterfaz() {
        Alquiler a = alquilerDeCuatroDias();

        // ICalculadorPrecio tiene un solo método abstracto: vale una expresión lambda
        a.setCalculadorPrecio((inicio, fin, vehiculo) -> 42);

        assertEquals(42, precio(a));
    }

    @Test
    public void conCuatroDiasLasPoliticasSeOrdenanDeMasCaraAMasBarata() {
        Alquiler a = alquilerDeCuatroDias();
        a.setCalculadorPrecio(new CalculadorPrecioTemporadaAlta());
        int temporadaAlta = precio(a);
        a.setCalculadorPrecio(new CalculadorPrecioNormal());
        int normal = precio(a);
        a.setCalculadorPrecio(new CalculadorPrecioClienteFrecuente());
        int clienteFrecuente = precio(a);
        a.setCalculadorPrecio(new CalculadorPrecioOferton());
        int oferton = precio(a);

        assertTrue(temporadaAlta + " > " + normal + " > " + clienteFrecuente + " > " + oferton,
                temporadaAlta > normal && normal > clienteFrecuente && clienteFrecuente > oferton);
    }

    @Test
    public void laPoliticaNoFormaParteDeLaRepresentacionJsonDelAlquiler() {
        String json = alquilerDeCuatroDias().toString();

        assertFalse(json, json.toLowerCase().contains("calculador"));
    }
}
