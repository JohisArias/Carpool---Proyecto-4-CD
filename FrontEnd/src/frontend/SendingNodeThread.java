/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import java.net.InetAddress;
import java.util.StringTokenizer;

/**
 *
 * @author Andrés
 */
public class SendingNodeThread extends Thread
{
    String nodename;
    InetAddress nodeaddress;
    InetAddress address;
    int port;
    
    public SendingNodeThread (InetAddress direccion, int puerto, String nombre, InetAddress direccionNodo)
    {
        address = direccion;
        port = puerto; 
        nodename = nombre;
        nodeaddress = direccionNodo;
    }
    
    public void Sending()
    {        
        while(true)
        {
            String direccion=nodeaddress.toString();
            StringTokenizer tokens = new StringTokenizer(direccion, "/");
            direccion=tokens.nextToken();
            String message="nodo/"+nodename+'|'+direccion;
            
            try
            {
                new ThClient(address, port, message).start();
                System.err.println("mensaje enviado: "+message);
                Thread.sleep(5000);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void run()
    {
        
        Sending();
    }
}
