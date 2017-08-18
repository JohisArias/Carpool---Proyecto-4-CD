package frontend;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EnviarInformacionServidor {
    public static void confirmarCliente(Socket cliente){
        try {
            ObjectOutputStream out = null;
            out = new ObjectOutputStream(cliente.getOutputStream());
            out.writeObject("cliente aceptado");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    public static void enviarInformacion(Socket cliente, String[] informacion){
        try {
            ObjectOutputStream out = null;
            out = new ObjectOutputStream(cliente.getOutputStream());
            out.writeObject(informacion);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    public static void enviarRutas(Socket cliente, ArrayList<String[]> rutas){
        try {
            ObjectOutputStream out = null;
            out = new ObjectOutputStream(cliente.getOutputStream());
            out.writeObject(rutas);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    public static void confirmarClienteActualizacion(Socket cliente,String mensaje){
        try {
            ObjectOutputStream out = null;
            out = new ObjectOutputStream(cliente.getOutputStream());
            out.writeObject(mensaje);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
