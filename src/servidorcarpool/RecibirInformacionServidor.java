package servidorcarpool;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class RecibirInformacionServidor {

    public static String[] recibirLogin(Socket cliente){
        String[] login =null;
        try {
            ObjectInputStream in ;
            
            in = new ObjectInputStream(cliente.getInputStream());
            login = (String[]) in.readObject();
           
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex);
        }
         return login;
    }

    public static String recibirConfirmacion(Socket cliente) {
        String confirmacion = "";
        try {
            ObjectInputStream in ;
            in = new ObjectInputStream(cliente.getInputStream());
            confirmacion = "" + in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex);
        }
        return confirmacion;
    }

    public static String[] recibirInformacionActualizada(Socket cliente) {
        String[] informacionActualizada = null;
        try {
            ObjectInputStream in;
            in = new ObjectInputStream(cliente.getInputStream());
            informacionActualizada = (String[]) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex);
        }
        return informacionActualizada;
    }

    public static String recibirTipoDeActualizacion(Socket cliente) {
        String confirmacion = "";
        try {
            ObjectInputStream in ;
            in = new ObjectInputStream(cliente.getInputStream());
            confirmacion = "" + in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex);
        }
        return confirmacion;
    }

}
