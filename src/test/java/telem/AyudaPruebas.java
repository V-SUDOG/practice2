package telem;

import static org.junit.Assert.fail;

/**
 * Utilidades comunes de las pruebas (no es una clase de pruebas).
 * JUnit 4.11, la versión del pom, no incluye assertThrows (llegó en la 4.13), así que se
 * proporciona aquí con try/catch.
 */
final class AyudaPruebas {

    /** Fragmento de código que puede lanzar una excepción. */
    interface Ejecutable {
        void ejecutar() throws Throwable;
    }

    private AyudaPruebas() {
    }

    /**
     * Comprueba que el código lanza una excepción del tipo indicado (o de una subclase).
     *
     * @return la excepción lanzada, por si se quiere comprobar su mensaje
     */
    static <T extends Throwable> T assertLanza(Class<T> tipoEsperado, Ejecutable codigo) {
        Throwable lanzada = null;
        try {
            codigo.ejecutar();
        } catch (Throwable t) {
            lanzada = t;
        }
        if (lanzada == null) {
            fail("Se esperaba " + tipoEsperado.getSimpleName() + " pero no se lanzó ninguna excepción");
        } else if (!tipoEsperado.isInstance(lanzada)) {
            fail("Se esperaba " + tipoEsperado.getSimpleName() + " pero se lanzó " + lanzada);
        }
        return tipoEsperado.cast(lanzada);
    }
}
