/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware;

import java.util.Comparator;

/**
 *
 * @author Andrés
 */
public class CompareNodes implements Comparator<Nodo>{
    @Override
    public int compare(Nodo n1, Nodo n2) {
        return n1.getName().compareToIgnoreCase(n2.getName());
    }
    
}
