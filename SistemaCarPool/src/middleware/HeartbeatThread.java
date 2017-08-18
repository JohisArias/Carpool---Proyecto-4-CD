/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware;

import java.net.InetAddress;
import java.util.StringTokenizer;

/**
 *
 * @author Andrés
 */
public class HeartbeatThread extends Thread
{
    String nodename;
    InetAddress nodeaddress;
    InetAddress Broadcastaddress;
    int port;
    
    public HeartbeatThread (InetAddress direccionBroadcast, int puerto, String nombre, InetAddress direccionNodo)
    {
        Broadcastaddress = direccionBroadcast;
        port = puerto; 
        nodename = nombre;
        nodeaddress = direccionNodo;
    }
    
    public void Sending()
    {
        while(true)
        {
            try
            {
                Thread.sleep(5000);
                String aux=nodeaddress.toString();
                StringTokenizer token = new StringTokenizer(aux, "/");
                String address = token.nextToken();
                String message="nodo/"+nodename+'|'+address;
                new ThSender(Broadcastaddress, port, message).start();
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
