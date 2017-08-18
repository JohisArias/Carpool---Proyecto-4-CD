/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Andrés
 */
public class FrontEnd {

    /**
     * @param args the command line arguments
     */
    public static ArrayList<String> inputqueue= new ArrayList<>();
    public static ArrayList<String> outputqueue= new ArrayList<>();
    public static int requestN;
    
    
    
    //direccion Broadcast
    public static InetAddress Broadcastaddress;
    public static int FEport = 6000, BEport=5000;
    
    public static String myname= "Andres";
    public static InetAddress myaddress;
    public static boolean isCentral=true;
    
    public static void main (String [] args)
    {
        
        myBroadCast();
        
        Thread hilo = new Thread(new MyThread());
        hilo.start();
        
        System.err.println("CHAO");
        try
        {
            myaddress=InetAddress.getByName("192.168.137.186");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
                
        boolean listening = true;
         /*
        try
        {
            ServerSocket FE = new ServerSocket(FEport);
            while(listening)
            {

                new ThreadServer(FE.accept()).start();
            }
            
        }catch(Exception e)
        {
            System.out.println("No se pudo establecer conexión en dicho puerto");
        }*/
    }
    
    public static void myBroadCast()
    {
        try
        {
            NetworkInterface network = NetworkInterface.getByName("wlan0");
            for(InterfaceAddress interfaz:network.getInterfaceAddresses())
            {
                InetAddress broadcast=interfaz.getBroadcast();
                if(broadcast==null)
                {
                    continue;
                }else
                {
                    Broadcastaddress = broadcast;
                }
            }
            
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
