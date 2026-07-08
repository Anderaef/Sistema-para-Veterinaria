package modelo;

public class Sesion {

    public static int idUsuario;
    public static String usuarioActual;
    public static String rol;
    public static String nombre;
    public static String apellido;

    public static String getNombreCompleto() {
        if (nombre != null && apellido != null) {
            return nombre + " " + apellido;
        }
        return usuarioActual;
    }
}