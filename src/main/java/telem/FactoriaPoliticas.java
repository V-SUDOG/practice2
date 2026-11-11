package telem;

import java.time.LocalDate;

/**
 * Método Factoría para crear políticas de precios (estrategias) a partir de su denominación.
 * Los clientes de la factoría, como Alquiler o GestorAlquileres, trabajan solo con la interfaz
 * {@link ICalculadorPrecio} y no necesitan conocer las clases concretas de cada política.
 *
 * La factoría únicamente decide qué clase instanciar; no contiene ninguna lógica de cálculo de precios,
 * que está en cada calculador.
 */
public final class FactoriaPoliticas {

    private FactoriaPoliticas() {
        // Clase de utilidad: no se instancia.
    }

    /**
     * Crea la política de precios que corresponde a un valor del enumerado.
     *
     * @param politica política deseada
     * @return una nueva instancia del calculador de precios de esa política
     * @throws IllegalArgumentException si la política es nula o no tiene calculador asociado
     */
    public static ICalculadorPrecio crearPolitica(PoliticasDePrecios politica) {
        if (politica == null) {
            throw new IllegalArgumentException("La política de precios no puede ser nula");
        }
        switch (politica) {
            case Normal:
                return new CalculadorPrecioNormal();
            case TemporadaAlta:
                return new CalculadorPrecioTemporadaAlta();
            case Oferton:
                return new CalculadorPrecioOferton();
            case ClienteFrecuente:
                return new CalculadorPrecioClienteFrecuente();
            default:
                throw new IllegalArgumentException("Política de precios sin calculador asociado: " + politica);
        }
    }

    /**
     * Crea la política de precios a partir de su denominación, que es el nombre del valor del enumerado
     * {@link PoliticasDePrecios} ("Normal", "TemporadaAlta", "Oferton" o "ClienteFrecuente").
     * Se ignoran las mayúsculas/minúsculas y los espacios al principio y al final.
     *
     * @param denominacion nombre de la política
     * @return una nueva instancia del calculador de precios de esa política
     * @throws IllegalArgumentException si la denominación es nula o no corresponde a ninguna política
     */
    public static ICalculadorPrecio crearPolitica(String denominacion) {
        if (denominacion == null) {
            throw new IllegalArgumentException("La denominación de la política no puede ser nula");
        }
        String buscada = denominacion.trim();
        for (PoliticasDePrecios politica : PoliticasDePrecios.values()) {
            if (politica.name().equalsIgnoreCase(buscada)) {
                return crearPolitica(politica);
            }
        }
        throw new IllegalArgumentException("Política de precios desconocida: \"" + denominacion + "\"");
    }

    /** Ejemplo de uso de la factoría. */
    public static void main(String[] args) {
        Vehiculo turismo = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
        LocalDate inicio = LocalDate.of(2026, 11, 9);
        LocalDate fin = inicio.plusDays(7); // 8 días

        System.out.println("Precio de alquilar 8 días un turismo NORMAL, según la política:");
        for (PoliticasDePrecios politica : PoliticasDePrecios.values()) {
            ICalculadorPrecio calculador = FactoriaPoliticas.crearPolitica(politica);
            System.out.println("  " + politica + " -> " + calculador.getClass().getSimpleName()
                    + ": " + calculador.calcularPrecioAlquiler(inicio, fin, turismo));
        }

        // A partir de una denominación (por ejemplo, escrita por el usuario)
        ICalculadorPrecio porNombre = FactoriaPoliticas.crearPolitica("clientefrecuente");
        System.out.println("Por nombre \"clientefrecuente\": " + porNombre.getClass().getSimpleName());

        // Integración con Alquiler (contexto): la factoría crea la estrategia y Alquiler la usa
        Alquiler alquiler = new Alquiler(turismo, new Cliente("Juan Perez", "123456789"), inicio, fin);
        alquiler.setCalculadorPrecio(FactoriaPoliticas.crearPolitica(PoliticasDePrecios.Oferton));
        System.out.println("Alquiler con política Oferton: "
                + alquiler.calcularPrecioAlquiler(alquiler.getFechaInicio(), alquiler.getFechaFin()));

        // Denominaciones desconocidas o nulas: se informa con una excepción controlada
        String[] incorrectas = {"Invierno", "", null};
        for (String denominacion : incorrectas) {
            try {
                FactoriaPoliticas.crearPolitica(denominacion);
            } catch (IllegalArgumentException e) {
                System.out.println("Error controlado: " + e.getMessage());
            }
        }
    }
}
