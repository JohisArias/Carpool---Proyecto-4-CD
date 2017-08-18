/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Andrés
 */
public class ThSender extends Thread
{   
    InetAddress address;
    String message;
    int port;
    DatagramSocket channel;
        
    public ThSender(InetAddress direccion, int puerto, String mensaje) throws IOException
    {
        channel = new DatagramSocket();
        address = direccion;
        port = puerto;
        message = mensaje;
    }
    
    public void Writing()
    {
        try 
        {
            byte[] buf = new byte[256];
            buf=message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            channel.send(packet);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }  
    
    public void run() 
    {
        Writing();
    }
}
