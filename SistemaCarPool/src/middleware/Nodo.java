/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware;

import java.net.InetAddress;

/**
 *
 * @author Andrés
 */
public class Nodo 
{
    String name;
    InetAddress address;
    Boolean isMaster;
    public Nodo()
    {
    
    }

    public Nodo(String nombre, InetAddress direccion, boolean nodoCentral)
    {
        this.name = nombre;
        this.address = direccion;
        this.isMaster=nodoCentral;
    }
    
    public Nodo(String nombre, InetAddress direccion)
    {
        this.name = nombre;
        this.address = direccion;
        this.isMaster=false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }
    
    
    
}
