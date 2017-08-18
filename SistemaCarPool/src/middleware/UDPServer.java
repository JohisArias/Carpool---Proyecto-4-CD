/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/**
 *
 * @author Andrés
 */
public class UDPServer extends Thread 
{ 
    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
 
    public UDPServer() throws IOException
    {
        this("UDPServer");
    }
 
    public UDPServer(String name) throws IOException
    {
        super(name);
        socket = new DatagramSocket(4445);
    }
 
    public void run()
    {
        try 
        {
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);        

            String dString=null, received = new String(packet.getData(), 0, packet.getLength());
            System.out.println(received);
            
            buf = dString.getBytes();

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    socket.close();
    }
}