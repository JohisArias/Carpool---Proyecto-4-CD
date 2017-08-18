package frontend;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import FrontEnd.*;
import static frontend.FrontEnd.BEport;
import static frontend.FrontEnd.Broadcastaddress;
import static frontend.FrontEnd.myaddress;
import static frontend.FrontEnd.myname;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class MyThread implements Runnable {



    //Socket cliente;

    public MyThread() {
      //  this.cliente = cliente;
    }


    @Override
    public void run() {

        String[] informacion;
        String[] informacionCliente;
        ArrayList<String[]> rutas;
        ArrayList<String[]> noCalificados;
        String clave = "";
        String[] login; //System.out.println(ex);
        //System.out.println(ex1);
        String ruta[];
        String calificados[];
        String confirmacionInformacionRecibida;
        String log[]={"1","Bryan","12345"};
        login = log;
        System.err.println("hola");
        try {
            System.err.println("hola");
            new ThClient(Broadcastaddress, 5002, "1,SELECT CLAVEUSUARIO FROM USUARIO WHERE APODOUSUARIO = '" + login[1] + "' and IDUSUARIO=" + login[0]).start();
            
        } catch (IOException ex) {
            Logger.getLogger(MyThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(5001);
        } catch (SocketException ex) {
            Logger.getLogger(MyThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                
        try {
            socket.receive(packet);
        } catch (IOException ex) {
            Logger.getLogger(MyThread.class.getName()).log(Level.SEVERE, null, ex);
        }
                String received = new String(packet.getData(), 0, packet.getLength());
                System.err.println("Recibo el mensaje: "+received);
        
        clave = received;
        
        if (login[2].equals(clave)) {
        EnviarInformacionServidor.confirmarCliente(cliente);
        confirmacionInformacionRecibida = RecibirInformacionServidor.recibirConfirmacion(cliente);
        if (confirmacionInformacionRecibida.equals("ok")) {
        informacionCliente = consultas.obtenerDatosDeClienteSQL("SELECT * FROM CLIENTE");
        EnviarInformacionServidor.enviarInformacion(cliente, informacionCliente);
        confirmacionInformacionRecibida = RecibirInformacionServidor.recibirConfirmacion(cliente);
        while (true) {
        //Thread.sleep(2000);
        confirmacionInformacionRecibida = RecibirInformacionServidor.recibirTipoDeActualizacion(cliente);
        if (confirmacionInformacionRecibida.equals("solicitudinformacion")) {
        informacion = consultas.obtenerTodaLaInformacionSQL("SELECT NOMBREUSUARIO,APELLIDOUSUARIO,TELEFONO1USUARIO,TELEFONO2USUARIO,CUIDADUSUARIO,EDADUSUARIO,CORREOUSUARIO FROM USUARIO WHERE APODOUSUARIO ='" + login[1] + "'");
        EnviarInformacionServidor.enviarInformacion(cliente, informacion);
        confirmacionInformacionRecibida = RecibirInformacionServidor.recibirConfirmacion(cliente);
        }
        if (confirmacionInformacionRecibida.equals("solicitudinformacionchofer")) {
        informacion = consultas.obtenerTodaLaInformacionSQL("SELECT NOMBREUSUARIO,APELLIDOUSUARIO,TELEFONO1USUARIO,TELEFONO2USUARIO,CUIDADUSUARIO,EDADUSUARIO,CORREOUSUARIO FROM USUARIO WHERE APODOUSUARIO ='" + login[1] + "'");
        EnviarInformacionServidor.enviarInformacion(cliente, informacion);
        confirmacionInformacionRecibida = RecibirInformacionServidor.recibirConfirmacion(cliente);
        }
        
        if (confirmacionInformacionRecibida.equals("solicitudrutas")) {
        rutas = consultas.obtenerTodasLasRutasSQL("Select * from RUTA r where r.IDRUTA not in (select dv.IDRUTA from DESCRIPCIONDEVIAJE dv where dv.IDCLIENTE = " + informacionCliente[1] + ") and NUMERODEASIENTOS != 0 and fecha > getdate()");
        EnviarInformacionServidor.enviarRutas(cliente, rutas);
        confirmacionInformacionRecibida = RecibirInformacionServidor.recibirConfirmacion(cliente);
        }
        if (confirmacionInformacionRecibida.equals("solicitudviajes")) {
        rutas = consultas.obtenerTodasLasRutasSQL("select * from ruta r where r.fecha < getdate() and idruta in (select idruta from descripciondeviaje where idcliente =  " + informacionCliente[1] + ")");
        EnviarInformacionServidor.enviarRutas(cliente, rutas);
        confirmacionInformacionRecibida = RecibirInformacionServidor.recibirConfirmacion(cliente);
        }
        if (confirmacionInformacionRecibida.equals("actualizarbase")) {
        EnviarInformacionServidor.confirmarClienteActualizacion(cliente, "actualizando");
        informacion = RecibirInformacionServidor.recibirInformacionActualizada(cliente);
        consultas.actualizarTablaUsuarioSQL(informacion, login[1]);
        confirmacionInformacionRecibida = "";
        }
        if (confirmacionInformacionRecibida.equals("actualizarrutas")) {
        EnviarInformacionServidor.confirmarClienteActualizacion(cliente, "actualizando");
        ruta = RecibirInformacionServidor.recibirInformacionActualizada(cliente);
        consultas.actualizarTablaRutaSQL(ruta, ruta[0]);
        consultas.actualizarTablaDescripcionSQL(Integer.parseInt(informacionCliente[0]), Integer.parseInt(ruta[0]));
        confirmacionInformacionRecibida = "";
        
        }
        if (confirmacionInformacionRecibida.equals("solicitudsuscripciones")) {
        rutas = consultas.obtenerTodasLasRutasSQL("Select * from RUTA r where r.IDRUTA in (select dv.IDRUTA from DESCRIPCIONDEVIAJE dv where dv.IDCLIENTE = " + informacionCliente[1] + ") and r.fecha >getdate()");
        EnviarInformacionServidor.enviarRutas(cliente, rutas);
        confirmacionInformacionRecibida = RecibirInformacionServidor.recibirConfirmacion(cliente);
        }
        if (confirmacionInformacionRecibida.equals("cancelarruta")) {
        EnviarInformacionServidor.confirmarClienteActualizacion(cliente, "actualizando");
        ruta = RecibirInformacionServidor.recibirInformacionActualizada(cliente);
        consultas.actualizarTablaRutaSQL(ruta, ruta[0]);
        consultas.eliminarSuscripcionTablaDescripcionSQL(Integer.parseInt(informacionCliente[0]), Integer.parseInt(ruta[0]));
        confirmacionInformacionRecibida = "";
        
        }
        
        if (confirmacionInformacionRecibida.equals("solicitudnocalificados")) {
        noCalificados = consultas.obtenerTodasLasRutasSQL("select r.IDRUTA,r.IDCHOFER,r.PUNTODEPARTIDA,r.DESTINO,dv.CALIFICACION,r.FECHA,r.HORADEPARTIDA from ruta r inner join DESCRIPCIONDEVIAJE dv on r.IDRUTA = dv.IDRUTA where dv.CALIFICACION is null and dv.IDCLIENTE =" + informacionCliente[1] );
        EnviarInformacionServidor.enviarRutas(cliente, noCalificados);
        confirmacionInformacionRecibida = RecibirInformacionServidor.recibirConfirmacion(cliente);
        }
        
        if (confirmacionInformacionRecibida.equals("solicitudcalificados")) {
        noCalificados = consultas.obtenerTodasLasRutasSQL("select r.IDRUTA,r.IDCHOFER,r.PUNTODEPARTIDA,r.DESTINO,dv.CALIFICACION,r.FECHA,r.HORADEPARTIDA from ruta r inner join DESCRIPCIONDEVIAJE dv on r.IDRUTA = dv.IDRUTA where dv.CALIFICACION is not null and dv.IDCLIENTE = "+informacionCliente[1]);
        EnviarInformacionServidor.enviarRutas(cliente, noCalificados);
        confirmacionInformacionRecibida = RecibirInformacionServidor.recibirConfirmacion(cliente);
        }
        if (confirmacionInformacionRecibida.equals("actualizardescripcion")) {
        EnviarInformacionServidor.confirmarClienteActualizacion(cliente, "actualizando");
        calificados = RecibirInformacionServidor.recibirInformacionActualizada(cliente);
        int idCliente = Integer.parseInt(informacionCliente[1]);
        int idRuta = Integer.parseInt(calificados[0]);
        int calificacion = Integer.parseInt(calificados[4]);
        consultas.calificarTablaDescripcionSQL(idCliente,idRuta,calificacion);
        }
        
        }
        }
        } else {
        System.out.println("No existe usuario");
        }*/
    }

}
