/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrontEnd;

import static FrontEnd.FrontEndServer.BEport;
import static FrontEnd.FrontEndServer.BroadcastIP;
import static FrontEnd.FrontEndServer.inputqueue;
import static FrontEnd.FrontEndServer.myBroadCast;
import static FrontEnd.FrontEndServer.outputqueue;
import static FrontEnd.FrontEndServer.serverAddress;
import static FrontEnd.FrontEndServer.serverName;
import static FrontEnd.FrontEndServer.serverNode;
import static FrontEnd.FrontEndServer.typeofnode;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import middleware.HeartbeatThread;
import middleware.Nodo;
import middleware.ThListener;

/**
 *
 * @author Andrés
 */
public class BackEndNode 
{
    public static int BEport = 5000;
    public static InetAddress BroadcastIP;
    public static InetAddress CentralNodeAddress;
    
    public static String serverName="Luis";
    public static InetAddress serverAddress;
    public static Nodo serverNode;
    public static boolean typeofnode = true;
    
    //colas de solicitudes para los clientes
    public static ArrayList<Nodo> nodeList= new ArrayList<>();

    
    public static void main(String [] args)
    {
        myBroadCast();
        try
        {
            serverAddress = InetAddress.getByName("192.168.1.9");
            CentralNodeAddress = InetAddress.getByName("192.168.1.14");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        serverNode=new Nodo(serverName, BroadcastIP);
        
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(new ThListener(BEport, BroadcastIP, typeofnode, serverNode,CentralNodeAddress));
        executor.submit(new HeartbeatThread(BroadcastIP, BEport, serverName, serverAddress));
        
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
                    BroadcastIP = broadcast;
                }
            }
            
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }    
}
