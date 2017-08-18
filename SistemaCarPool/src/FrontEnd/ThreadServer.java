/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FrontEnd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
/**
 *
 * @author Andrés
 */
public class ThreadServer extends Thread 
{
    Socket serversocket = null;

    public ThreadServer(Socket socket)
    {
        try
        {
            serversocket= socket;
        }
        catch(Exception E)
        {
            System.out.println("No se pudo establecer un servidor en alguno de los puertos");
        }
    }

    public void ConnectClient()
    {
        try
        {
            int Myorder=FrontEndServer.requestN;
            BufferedReader reader = new BufferedReader(new InputStreamReader(serversocket.getInputStream()));
            PrintWriter writer = new PrintWriter(serversocket.getOutputStream(),true); 
            FrontEndServer.inputqueue.add(Myorder+"|"+reader.readLine());
            FrontEndServer.requestN++;
            
            Thread.sleep(60000);
            
            for(String value:FrontEndServer.outputqueue)
            {
                StringTokenizer token= new StringTokenizer(value, "|");
                int valueRequest=Integer.parseInt(token.nextToken());
                String content = token.nextToken();
                if(valueRequest==Myorder)
                {
                    writer.print(content);
                }
            }            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void run()
    {
        ConnectClient();
    }
}
