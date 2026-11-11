package telem;

/**
 * Política normal: precio base diario x número de días de alquiler.
 */
public class CalculadorPrecioNormal extends CalculadorPrecioBase {

    @Override
    protected int calcularImporte(int dias, int precioBase) {
        return dias * precioBase;
    }
}
