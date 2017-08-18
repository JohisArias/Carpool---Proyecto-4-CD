package backend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsultasSQL extends Thread {

    Connection conect;
    String received;
    
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    public ConsultasSQL() {

        try {
            String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Carpool;user=sa; password=Bryan123;";
            conect = DriverManager.getConnection(connectionUrl);
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
    @Override
    public void run() {
    
        System.err.println("holaaa");
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(5002);
        } catch (SocketException ex) {
            Logger.getLogger(ConsultasSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
                byte[] buf = new byte[256];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch (IOException ex) {
            Logger.getLogger(ConsultasSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
               received = new String(packet.getData(), 0, packet.getLength());
                System.err.println("Recibo el mensaje: "+received);
       StringTokenizer tokens=new StringTokenizer(received,",");     
        if(1== Integer.parseInt(tokens.nextToken())){
            try {
                InetAddress Broadcastaddress=null;
                myBroadCast(Broadcastaddress);
                 new ThClient(Broadcastaddress, 5001, "REspuesta" ).start(); 
            } catch (Exception ex) {
                Logger.getLogger(ConsultasSQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    }

    public String clave(String consulta) throws SQLException {
        String clave = "";

        Statement st;
        ResultSet rs;

        st = conect.createStatement();
        rs = st.executeQuery(consulta);
        while (rs.next()) {
            clave += rs.getString(1);
        }
        return clave;
    }

public boolean Conectar(String nombre, String password)
    {
        boolean admitido=true;
        
        String script = "SELECT * FROM Persona WHERE nombrePersona=? and password=?";
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:3306/base_CD", "root", "Andres-Mysql10");
            pst=conn.prepareStatement(script);
            pst.setString(1, nombre);
            pst.setString(2, password);
            rs=pst.executeQuery();
            if(rs.next())
            {
                System.out.println("Usuario permitido. \n Bienvenido "+nombre);
            }
            else
            {
                System.out.println("Usuario no admitido.");
                admitido=false;
            }
            
        }catch(Exception e)
        {
            System.out.println(e);
        }
        
        return admitido;
    }

    
    
    public String[] obtenerTodaLaInformacionSQL(String consulta) throws SQLException {
        String[] informacion = new String[7];
        Statement st;
        ResultSet rs;
        st = conect.createStatement();
        rs = st.executeQuery(consulta);
        while (rs.next()) {
            informacion[0] = rs.getString(1);
            informacion[1] = rs.getString(2);
            informacion[2] = rs.getString(3);
            informacion[3] = rs.getString(4);
            informacion[4] = rs.getString(5);
            informacion[5] = rs.getString(6);
            informacion[6] = rs.getString(7);
        }
        return informacion;
    }

    public ArrayList obtenerTodasLasRutasSQL(String consulta) throws SQLException {
        ArrayList<String[]> todasLasRutas = new ArrayList();
        Statement st;
        ResultSet rs;
        st = conect.createStatement();
        rs = st.executeQuery(consulta);
        while (rs.next()) {
            String[] rutas = new String[7];
            rutas[0] = rs.getString(1);
            rutas[1] = rs.getString(2);
            rutas[2] = rs.getString(3);
            rutas[3] = rs.getString(4);
            rutas[4] = rs.getString(5);
            rutas[5] = rs.getString(6);
            rutas[6] = rs.getString(7);
            todasLasRutas.add(rutas);
        }
        return todasLasRutas;
    }
    
    


    public void actualizarTablaUsuarioSQL(String[] informacion, String apodo) throws SQLException {
        Statement st;
        PreparedStatement ps = conect.prepareStatement(
                "update usuario set NOMBREUSUARIO = ?, APELLIDOUSUARIO = ? , TELEFONO1USUARIO = ?, TELEFONO2USUARIO = ?,CUIDADUSUARIO = ? ,EDADUSUARIO = ? ,CORREOUSUARIO = ? where APODOUSUARIO = ?");
        ps.setString(1, informacion[0]);
        ps.setString(2, informacion[1]);
        ps.setString(3, informacion[2]);
        ps.setString(4, informacion[3]);
        ps.setString(5, informacion[4]);
        ps.setInt(6, Integer.parseInt(informacion[5]));
        ps.setString(7, informacion[6]);
        ps.setString(8, apodo);
        ps.executeUpdate();
        ps.close();
    }

    public void actualizarTablaRutaSQL(String[] ruta, String idRuta) throws SQLException {
        Statement st;
        PreparedStatement ps = conect.prepareStatement(
                "update ruta set NUMERODEASIENTOS=? where IDRUTA = ?");
        ps.setInt(1, Integer.parseInt(ruta[4]));
        ps.setInt(2, Integer.parseInt(ruta[0]));

        ps.executeUpdate();
        ps.close();
    }

    public void actualizarTablaDescripcionSQL(int idCliente, int idRuta) throws SQLException {
        Statement st;
        PreparedStatement ps = conect.prepareStatement("insert into DESCRIPCIONDEVIAJE(IDCLIENTE,IDRUTA) values (?,?)");
        ps.setInt(1, idCliente);
        ps.setInt(2, idRuta);
        ps.executeUpdate();
        ps.close();
    }
    
    public void calificarTablaDescripcionSQL(int idCliente,int idRuta,int calificacion) throws SQLException {
        Statement st;
        PreparedStatement ps = conect.prepareStatement("update descripciondeviaje set CALIFICACION=? where IDCLIENTE = ? and IDRUTA = ?");
        ps.setInt(1, calificacion);
        ps.setInt(2, idCliente);
        ps.setInt(3, idRuta);
        ps.executeUpdate();
        ps.close();
    }
    
    public void eliminarSuscripcionTablaDescripcionSQL(int idCliente, int idRuta) throws SQLException {
        Statement st;
        PreparedStatement ps = conect.prepareStatement("delete DESCRIPCIONDEVIAJE where (IDCLIENTE = ? and IDRUTA =?)");
        ps.setInt(1, idCliente);
        ps.setInt(2, idRuta);
        ps.executeUpdate();
        ps.close();
    }


    public String[] obtenerDatosDeClienteSQL(String consulta) throws SQLException {
        String[] cliente = new String[2];
        Statement st;
        ResultSet rs;
        st = conect.createStatement();
        rs = st.executeQuery(consulta);
        while (rs.next()) {

            cliente[0] = rs.getString(1);
            cliente[1] = rs.getString(2);

        }
        return cliente;
    }

        public static void myBroadCast(InetAddress Broadcastaddress)
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
                    Broadcastaddress = broadcast;
                }
            }
            
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
