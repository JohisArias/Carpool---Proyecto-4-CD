/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrontEnd;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import middleware.*;
/**
 *
 * @author Andrés
 */
public class FrontEndServer 
{
    public static int FEport = 6000;
    public static int BEport = 5000;
    public static InetAddress BroadcastIP;
    
    public static String serverName="Andres";
    public static InetAddress serverAddress;
    public static Nodo serverNode;
    public static boolean typeofnode = true;
    
    //colas de solicitudes para los clientes
    public static ArrayList<Nodo> nodeList= new ArrayList<>();
    public static ArrayList<String> inputqueue= new ArrayList<>();
    public static ArrayList<String> outputqueue= new ArrayList<>();
    public static int requestN;
        
    public static void main (String [] args)
    {
        myBroadCast();
        try
        {
            serverAddress = InetAddress.getByName("192.168.1.10");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        serverNode=new Nodo(serverName, BroadcastIP);
//        new SendingNodeThread(Broadcastaddress, BEport,myname,myaddress).start();
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(new ThListener(BEport, BroadcastIP, typeofnode, serverNode, outputqueue, inputqueue));
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
