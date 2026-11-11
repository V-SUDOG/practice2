package telem;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

/**
 * Utilidades compartidas por las pruebas de precios (no es una clase de pruebas).
 * Los días se cuentan de forma inclusiva, así que un alquiler de n días termina n-1 días después del inicio.
 */
final class AyudaPruebasPrecios {

    static final LocalDate INICIO = LocalDate.of(2026, 11, 9);

    /** Duraciones límite que interesa comprobar en cada política. */
    static final int[] DIAS_LIMITE = {1, 3, 4, 7, 8};

    private AyudaPruebasPrecios() {
    }

    static Vehiculo turismo(Categoria categoria) {
        return new Turismo("ABC123", categoria, 2020, 5, 400.0);
    }

    /** Fecha de fin de un alquiler que empieza en INICIO y dura el número de días indicado. */
    static LocalDate fin(int dias) {
        return INICIO.plusDays(dias - 1);
    }

    /** Precio de alquilar un turismo de la categoría dada durante el número de días indicado. */
    static int precio(ICalculadorPrecio calculador, Categoria categoria, int dias) {
        return calculador.calcularPrecioAlquiler(INICIO, fin(dias), turismo(categoria));
    }

    /** Comprueba el precio esperado de un turismo de la categoría dada para cada duración indicada. */
    static void comprobar(ICalculadorPrecio calculador, Categoria categoria, int[] dias, int[] esperados) {
        for (int i = 0; i < dias.length; i++) {
            assertEquals(categoria + ", " + dias[i] + " días", esperados[i], precio(calculador, categoria, dias[i]));
        }
    }
}
