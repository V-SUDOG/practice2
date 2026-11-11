package telem;

/**
 * Política ofertón: 30 % de descuento sobre el precio base y, si el alquiler dura más de siete días,
 * un 10 % adicional de descuento sobre el total.
 * "Más de siete días" significa 8 o más: con exactamente 7 días no se aplica el descuento adicional.
 */
public class CalculadorPrecioOferton extends CalculadorPrecioBase {

    private static final int PORCENTAJE_A_PAGAR = 70;               // 100 % - 30 % de descuento
    private static final int DIAS_SIN_DESCUENTO_ADICIONAL = 7;
    private static final int PORCENTAJE_A_PAGAR_ADICIONAL = 90;     // 100 % - 10 % de descuento

    @Override
    protected int calcularImporte(int dias, int precioBase) {
        long numerador = (long) dias * precioBase * PORCENTAJE_A_PAGAR;
        long denominador = 100;
        if (dias > DIAS_SIN_DESCUENTO_ADICIONAL) {
            numerador *= PORCENTAJE_A_PAGAR_ADICIONAL;
            denominador *= 100;
        }
        return dividirRedondeando(numerador, denominador);
    }
}
