package telem;

/**
 * Política de cliente frecuente, con descuento por tramos (no un descuento único):
 * los tres primeros días se pagan con un 20 % de descuento sobre el precio base y
 * a partir del cuarto día, con un 40 % de descuento.
 * Ejemplo con precio base 50 y 4 días: 3 x 40 + 1 x 30 = 150.
 */
public class CalculadorPrecioClienteFrecuente extends CalculadorPrecioBase {

    private static final int DIAS_PRIMER_TRAMO = 3;
    private static final int PORCENTAJE_A_PAGAR_PRIMER_TRAMO = 80;   // 100 % - 20 %
    private static final int PORCENTAJE_A_PAGAR_SEGUNDO_TRAMO = 60;  // 100 % - 40 %

    @Override
    protected int calcularImporte(int dias, int precioBase) {
        int diasPrimerTramo = Math.min(dias, DIAS_PRIMER_TRAMO);
        int diasSegundoTramo = dias - diasPrimerTramo;
        long numerador = (long) precioBase * (diasPrimerTramo * PORCENTAJE_A_PAGAR_PRIMER_TRAMO
                + diasSegundoTramo * PORCENTAJE_A_PAGAR_SEGUNDO_TRAMO);
        return dividirRedondeando(numerador, 100);
    }
}
