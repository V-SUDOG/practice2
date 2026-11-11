package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Pruebas de la clase Cliente: construcción, consulta de atributos y representación JSON.
 */
public class ClienteTest {

    @Test
    public void elConstructorGuardaNombreYDni() {
        Cliente cliente = new Cliente("Juan Perez", "12345678A");

        assertEquals("Juan Perez", cliente.getNombre());
        assertEquals("12345678A", cliente.getDni());
    }

    @Test
    public void dosClientesConDatosDistintosConservanSusPropiosDatos() {
        Cliente c1 = new Cliente("Ana Litica", "005");
        Cliente c2 = new Cliente("Armando Guerra", "004");

        assertEquals("Ana Litica", c1.getNombre());
        assertEquals("004", c2.getDni());
    }

    @Test
    public void toStringDevuelveJsonConNombreYDni() {
        Cliente cliente = new Cliente("Juan Perez", "12345678A");

        String json = cliente.toString();

        assertTrue("Debe ser un objeto JSON: " + json, json.startsWith("{") && json.endsWith("}"));
        assertTrue(json, json.contains("\"nombre\":\"Juan Perez\""));
        assertTrue(json, json.contains("\"dni\":\"12345678A\""));
    }

    /**
     * Prueba de caracterización: el constructor actual no valida sus argumentos.
     * Si en el futuro se añade validación, esta prueba deberá actualizarse.
     */
    @Test
    public void elConstructorActualNoValidaSusArgumentos() {
        Cliente cliente = new Cliente(null, null);

        assertNull(cliente.getNombre());
        assertNull(cliente.getDni());
    }
}
