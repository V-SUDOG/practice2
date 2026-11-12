package telem;

import java.time.LocalDate;

/**
 * Ejemplo de uso del patrón Estrategia (ejercicio 2): calcula el precio de un alquiler con
 * cada una de las cuatro políticas para distintas duraciones y cambia la política de un
 * mismo Alquiler en tiempo de ejecución.
 */
public class DemoPoliticasPrecios {

    public static void main(String[] args) {
        //Prepara las políticas y los datos de prueba
        ICalculadorPrecio[] politicas = {
            new CalculadorPrecioNormal(),
            new CalculadorPrecioTemporadaAlta(),
            new CalculadorPrecioOferton(),
            new CalculadorPrecioClienteFrecuente()
        };
        //Guarda las duraciones y categorías a probar, y la fecha de inicio del alquiler
        int[] duraciones = {1, 3, 4, 7, 8};
        Categoria[] categorias = {Categoria.NORMAL, Categoria.ESPECIAL, Categoria.SUPERIOR};
        LocalDate inicio = LocalDate.of(2026, 11, 5);

        // Muestra los precios de alquiler para cada política, duración y categoría
        for (Categoria categoria : categorias) {
            Vehiculo turismo = new Turismo("ABC123", categoria, 2020, 5, 400.0);
            System.out.println("Turismo " + categoria + " (precio base diario: " + turismo.getPrecioBase() + ")");
            System.out.printf("%-34s", "Política \\ días");
            for (int dias : duraciones) {
                System.out.printf("%7d", dias);
            }
            System.out.println();
            // Calcula y muestra el precio de alquiler para cada política y duración
            for (ICalculadorPrecio politica : politicas) {
                System.out.printf("%-34s", politica.getClass().getSimpleName());
                for (int dias : duraciones) {
                    LocalDate fin = inicio.plusDays(dias - 1);
                    System.out.printf("%7d", politica.calcularPrecioAlquiler(inicio, fin, turismo));
                }
                System.out.println();
            }
            System.out.println();
        }

        // Un mismo alquiler cambia de política sin que Alquiler se modifique.
        Vehiculo turismo = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
        Cliente cliente = new Cliente("Juan Perez", "123456789");
        Alquiler alquiler = new Alquiler(turismo, cliente, inicio, inicio.plusDays(3)); // 4 días
        System.out.println("Alquiler de 4 días, política por defecto: "
                + alquiler.calcularPrecioAlquiler(alquiler.getFechaInicio(), alquiler.getFechaFin()));
        for (ICalculadorPrecio politica : politicas) {
            alquiler.setCalculadorPrecio(politica);
            System.out.println("Alquiler de 4 días, " + politica.getClass().getSimpleName() + ": "
                    + alquiler.calcularPrecioAlquiler(alquiler.getFechaInicio(), alquiler.getFechaFin()));
        }
    }
}
