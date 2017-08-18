/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.StringTokenizer;
/**
 *
 * @author Andrés
 */
public class ThListener extends Thread {

//comunication parameters    
    int port;
    DatagramSocket Listenersocket;
    InetAddress Broadcastaddress;
    InetAddress centralnodeaddress;    

//node internal parameters
    Nodo mynode = null;
    String myname;    
    InetAddress myaddress;
    boolean isMaster;

//files administration parameters    
    ArrayList<Nodo> nodelist;
    ArrayList<String>InputQueue;
    ArrayList<String>OutputQueue;
    
//as normal node
    public ThListener(int puerto, InetAddress direccionBroadcast, boolean tiponodo, Nodo nodo, InetAddress direccionNodoCentral)
    {   
        port = puerto;
        Broadcastaddress = direccionBroadcast;
        isMaster=tiponodo;
        
        mynode = nodo;
        myname = mynode.getName();
        myaddress = mynode.getAddress();
        
        nodelist= new ArrayList<>();
        
        try
        {
            centralnodeaddress=direccionNodoCentral;
            Listenersocket = new DatagramSocket(port);
        }
        catch(Exception e)
        {
            System.err.println("error al crear socket para nodo back end");
            e.printStackTrace();
        }        
    }

//as central node    
    public ThListener(int puerto, InetAddress direccionBroadcast, boolean tiponodo, Nodo nodo, ArrayList<String>ColaEntrada, ArrayList<String>ColaSalida)
    {   
        port = puerto;
        Broadcastaddress= direccionBroadcast;
        isMaster=tiponodo;
        
        mynode = nodo;
        myname = mynode.getName();
        myaddress = mynode.getAddress();
            
        nodelist= new ArrayList<>();        
        InputQueue = ColaEntrada;
        OutputQueue = ColaSalida;
        
        try
        {
            Listenersocket = new DatagramSocket(port);
        }
        catch(Exception e)
        {
            System.err.println("error al crear el socket de comunicacion para nodo central");
            e.printStackTrace();
        }
    }    
    
    public void Receiving()
    {
        byte[] buf = new byte[256];
        try 
        {
            while(true)
            {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                Listenersocket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.err.println("Recibo el mensaje: "+received);
                ParsingMessage(received);
                Thread.sleep(1000);
                //SendingData(response);
            }
        }
        catch (Exception e)
        {
            System.err.println("Error al recibir el paquete");
            e.printStackTrace();
        }
    }
      
    public void ParsingMessage(String mensaje)
    {
        StringTokenizer tokens = new StringTokenizer(mensaje,"/");
        String type = tokens.nextToken();
        if(type.equals("nodo"))
        {
            UpdateRoutingTable(tokens.nextToken());
            for(Nodo n:nodelist)
            {
                System.out.println("nodo: "+n.getName()+" - "+n.getAddress());
            }
        }
        if(type.equals(myname))
            CheckRequestStatus(tokens.nextToken());
    }
    
    public void UpdateRoutingTable(String nuevonodo)
    {
//        if(!nodelist.contains(mynode))
//            nodelist.add(mynode);
            
        StringTokenizer tokens = new StringTokenizer(nuevonodo, "|");
        String nodename = tokens.nextToken();
        InetAddress nodeaddress = null;
        Nodo newnode = null;

        try
        {
            nodeaddress = InetAddress.getByName(tokens.nextToken());    
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        newnode = new Nodo(nodename, nodeaddress);
        boolean band = true;
        
        for(Nodo n:nodelist)
        {
            if(n.getAddress().equals(newnode.getAddress()))
            {
                band=false;
                break;
            }
        }
        
        if(band)
        {
            nodelist.add(newnode);
            nodelist.sort(new CompareNodes());
        }
    }
    
    public void CheckRequestStatus(String paquete)
    {
        StringTokenizer tokens = new StringTokenizer(paquete, "@");
        boolean flag = Boolean.parseBoolean(tokens.nextToken());
        String content = tokens.nextToken();
        String result;
        if(flag == false)
        {
            RequestProcessing(content);
        }
        else
        {
            OutputQueue.add(content);
        }
    }
    
    public void RequestProcessing(String contenido)
    {
        StringTokenizer tokens = new StringTokenizer(contenido, "|");
        int actid = Integer.parseInt(tokens.nextToken());
        int numOrder = Integer.parseInt(tokens.nextToken());
        String request = tokens.nextToken();
        
        switch(actid)
        {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
        
    public void SendingData(String salida)
    {
        if(isMaster==true)
        {
            int max=InputQueue.size();
            int cont=0;
            while(cont!=max)
            {
                for(Nodo n:nodelist)
                {
                    SendingAsCentralNode(n.getName(), InputQueue.get(cont));
                    cont++;
                }                
            }    
        }
        else
        {
            if(!salida.equals(null))
            {
                SendingAsNode(salida);
            }
        }
    }
    
    public void SendingAsCentralNode(String idnodo, String mensaje)
    {  
        boolean flag = false;
        String request= idnodo+"/"+flag+"@"+mensaje;
        try
        {
            new ThSender(Broadcastaddress, port, request).start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }    

    public void SendingAsNode(String respuesta)
    {
        String centralnodeid=null;
        for(Nodo n:nodelist)
        {
            if(n.getAddress().equals(centralnodeaddress))
            {
                centralnodeid=n.getName();
                break;
            }
        }
        boolean flag = true;
        String response=centralnodeid+"/"+flag+"@"+respuesta;
        try 
        {
            new ThSender(centralnodeaddress, port, response).start();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void run ()
    {        
        Receiving();
    }
}
