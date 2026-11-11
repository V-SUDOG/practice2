package telem;

import java.time.LocalDate;

import org.json.JSONObject;

/**
 * Modela un alquiler de un vehículo por parte de un cliente, con fechas de inicio y fin.
 * Almacena los alquileres realizados en un array estático y proporciona métodos para calcular el precio del alquiler.
 * Es el contexto del patrón Estrategia: delega el cálculo del precio en un {@link ICalculadorPrecio},
 * que puede cambiarse sin modificar esta clase. Por defecto usa la política normal.
 * Alquiler
 */
public class Alquiler {
    
    private Vehiculo vehiculo;
    private Cliente cliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private ICalculadorPrecio calculadorPrecio = new CalculadorPrecioNormal();

    private static Alquiler[] alquileres = new Alquiler[1000];

    // Se conserva por compatibilidad; ahora el precio base diario lo aporta Vehiculo.getPrecioBase().
    public static final int PRECIO_BASE = 50;
    private static int contadorAlquileres = 0;

    public Alquiler(Vehiculo vehiculo, Cliente cliente, LocalDate fechaInicio, LocalDate fechaFin) {
        this.vehiculo = vehiculo;
        this.cliente = cliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    /**
     * Crea un alquiler indicando desde el principio la política de precios que se le aplica.
     */
    public Alquiler(Vehiculo vehiculo, Cliente cliente, LocalDate fechaInicio, LocalDate fechaFin,
            ICalculadorPrecio calculadorPrecio) {
        this(vehiculo, cliente, fechaInicio, fechaFin);
        this.calculadorPrecio = validarPolitica(calculadorPrecio);
    }

    /**
     * Cambia la política de precios del alquiler (la estrategia). No hay getter a propósito:
     * toString() serializa con JSON todos los getters y la estrategia no forma parte de los datos.
     * @throws IllegalArgumentException si la política es nula
     */
    public void setCalculadorPrecio(ICalculadorPrecio calculadorPrecio) {
        this.calculadorPrecio = validarPolitica(calculadorPrecio);
    }

    private static ICalculadorPrecio validarPolitica(ICalculadorPrecio calculadorPrecio) {
        if (calculadorPrecio == null) {
            throw new IllegalArgumentException("La política de precios no puede ser nula");
        }
        return calculadorPrecio;
    }

    public static Alquiler[] obtenerAlquileres() {
        return alquileres; 
    }

    public static void agregarAlquiler(Alquiler alquiler) {
        if (contadorAlquileres < alquileres.length) {
            alquileres[contadorAlquileres] = alquiler;
            contadorAlquileres++;
        } else {
            System.out.println("No se pueden agregar más alquileres. Capacidad máxima alcanzada.");
        }
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin) {
        return calculadorPrecio.calcularPrecioAlquiler(fechaInicio, fechaFin, vehiculo);
    }

    @Override 
    public String toString() {
        return new JSONObject(this).toString();
    }

    public static void main(String[] args) {
        Vehiculo vehiculo1 = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
        Cliente cliente1 = new Cliente("Juan Pérez", "123456789");
        LocalDate hoy = LocalDate.now();
        Alquiler alquiler1 = 
            new Alquiler(vehiculo1, cliente1, hoy, hoy.plusDays(3));
        System.out.println(alquiler1.toString());
        System.out.println("-----------------------------------------------");

        Vehiculo vehiculo2 = 
            new VehiculoIndustrial("ABC123", Categoria.NORMAL, 2020, 1000, 3);
        Alquiler alquiler2 = 
            new Alquiler(vehiculo2, cliente1, hoy, hoy.plusDays(3));
        System.out.println(alquiler2.toString());
        System.out.println("-----------------------------------------------");
    }
}
