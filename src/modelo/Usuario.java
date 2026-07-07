package modelo;

public class Usuario {
    private int id;
    private String username;
    private String password;
    private String rol;

    public Usuario(int id, String username, String password, String rol) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public String getUsername() { return username; }
    public String getRol() { return rol; }
}