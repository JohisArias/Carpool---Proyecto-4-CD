package servidorcarpool;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class MyThread implements Runnable {

    Socket cliente;
    ConsultasSQL consultas;

    public MyThread(Socket cliente) {
        this.cliente = cliente;
        consultas = new ConsultasSQL();
    }

    @Override
    public void run() {

        String[] informacion;
        String[] informacionCliente;
        ArrayList<String[]> rutas;
        ArrayList<String[]> noCalificados;
        String clave = "";
        try {
            String login[];
            String ruta[];
            String calificados[];
            String confirmacionInformacionRecibida;

            login = RecibirInformacionServidor.recibirLogin(cliente);
            clave = consultas.clave("SELECT CLAVEUSUARIO FROM USUARIO WHERE APODOUSUARIO = '" + login[1] + "' and IDUSUARIO=" + login[0]);
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
            }

        } catch (SQLException ex) {
            try {
                //System.out.println(ex);
                cliente.close();
            } catch (IOException ex1) {
                //System.out.println(ex1);

            }
        }
    }

}
