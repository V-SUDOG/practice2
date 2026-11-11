package telem;

/**
 * Política de temporada alta: se duplica el precio base diario.
 */
public class CalculadorPrecioTemporadaAlta extends CalculadorPrecioBase {

    private static final int FACTOR_TEMPORADA_ALTA = 2;

    @Override
    protected int calcularImporte(int dias, int precioBase) {
        return dias * precioBase * FACTOR_TEMPORADA_ALTA;
    }
}
