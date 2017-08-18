/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import static frontend.FrontEnd.Broadcastaddress;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 *
 * @author Andrés
 */
public class ThClient extends Thread
{   
    InetAddress address;
    String message;
    int port;
    DatagramSocket mysocket;
    ArrayList<String>InputQueue;
    
    
    public ThClient(InetAddress direccion, int puerto) throws IOException
    {
        address = direccion;
        port = puerto;
        mysocket= new DatagramSocket();
    }
    
    public ThClient(InetAddress direccion, int puerto, String mensaje) throws IOException
    {
        address = direccion;
        port = puerto;
        message = mensaje;
        mysocket= new DatagramSocket();
    }

    public ThClient(InetAddress direccion, int puerto, String mensaje, ArrayList<String>ColaEntrada) throws IOException
    {
        address = direccion;
        port = puerto;
        message = mensaje;
        InputQueue=ColaEntrada;
        mysocket= new DatagramSocket();
    }
    
    public void run() 
    {
        Writing();
    }
    
    public void Writing()
    {
        try 
        {
            byte[] buf = new byte[256];
            buf=message.getBytes();
            System.err.println(buf);
            address=Broadcastaddress;
            System.err.println(address);
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            mysocket.send(packet);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        mysocket.close();
    }
    
   }
