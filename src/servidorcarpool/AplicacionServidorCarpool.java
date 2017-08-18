package servidorcarpool;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class AplicacionServidorCarpool {
    public static void main(String[] args)  {
        try {
            ServerSocket servidor = new ServerSocket(5000);
            while(true){
                Socket cliente = servidor.accept();
                Thread hilo = new Thread(new MyThread(cliente));
                hilo.start();
            }
        } catch (IOException ex) {
            
        }
    }
    
}
