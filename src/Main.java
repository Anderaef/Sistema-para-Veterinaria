

import vista.JFLoginVista;
import controlador.LoginControlador;

public class Main {
    public static void main(String[] args) {
        
        JFLoginVista login = new JFLoginVista();
        
        new LoginControlador(login);
        
        login.setVisible(true);
    }
}