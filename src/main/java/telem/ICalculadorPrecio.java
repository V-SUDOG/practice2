package telem;

import java.time.LocalDate;

/**
 * Estrategia (patrón Estrategia) para calcular el precio de un alquiler.
 * Cada política de precios es una implementación distinta de esta interfaz.
 * El contexto, {@link Alquiler}, solo conoce esta interfaz y no las clases concretas.
 */
public interface ICalculadorPrecio {

    /**
     * Calcula el precio total de alquilar un vehículo entre dos fechas.
     * Los días se cuentan de forma inclusiva: la fecha de inicio y la de fin cuentan como días
     * de alquiler, de modo que si ambas coinciden el alquiler dura 1 día.
     *
     * @param fechaInicio primer día del alquiler
     * @param fechaFin    último día del alquiler (no puede ser anterior a la fecha de inicio)
     * @param vehiculo    vehículo alquilado; su {@link Vehiculo#getPrecioBase()} es el precio base diario
     * @return precio total del alquiler, en unidades enteras
     * @throws IllegalArgumentException si algún argumento es nulo o si fechaFin es anterior a fechaInicio
     */
    int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo);
}
