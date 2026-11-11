package telem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Clase abstracta que recoge lo que es común a las cuatro políticas de precios, para no repetirlo:
 * validar los argumentos, contar los días de alquiler y redondear importes.
 * Cada política concreta solo define cómo se calcula el importe a partir de los días y del precio base
 * (método plantilla: {@link #calcularPrecioAlquiler} fija los pasos y {@link #calcularImporte} varía).
 *
 * Criterio de redondeo (el enunciado no lo especifica): el precio es un int, así que el importe
 * se calcula con aritmética entera exacta y se redondea una sola vez, al final, al entero más
 * próximo (los .5 hacia arriba). Así se evitan los errores de los números en coma flotante.
 */
public abstract class CalculadorPrecioBase implements ICalculadorPrecio {

    @Override
    public final int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas del alquiler no pueden ser nulas");
        }
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no puede ser nulo");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin (" + fechaFin
                    + ") no puede ser anterior a la de inicio (" + fechaInicio + ")");
        }
        // +1 para incluir el día de inicio (mismo criterio que Alquiler antes de aplicar el patrón)
        int dias = Math.toIntExact(ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1);
        return calcularImporte(dias, vehiculo.getPrecioBase());
    }

    /**
     * Calcula el importe según la política concreta.
     *
     * @param dias       días de alquiler (siempre mayor o igual que 1)
     * @param precioBase precio base diario del vehículo
     * @return importe total redondeado a entero
     */
    protected abstract int calcularImporte(int dias, int precioBase);

    /**
     * Divide dos enteros redondeando al entero más próximo (los .5 hacia arriba).
     * Solo válido para numerador y denominador positivos.
     */
    protected static int dividirRedondeando(long numerador, long denominador) {
        return (int) ((2 * numerador + denominador) / (2 * denominador));
    }
}
