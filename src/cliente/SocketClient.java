package cliente;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
 
public class SocketClient {
    String recibido;
    OutputStream osalida;
    DataOutputStream dsalida;
    InputStream ientrada;
    DataInputStream dentrada;
    Socket cliente;
   public SocketClient(String direccion, int puerto) throws IOException{
        cliente=new Socket(direccion, puerto);
        osalida = cliente.getOutputStream();
        dsalida = new DataOutputStream(osalida);
        ientrada = cliente.getInputStream();
        dentrada = new DataInputStream(ientrada);
   }
   /*public void conectar(){
   try {
   String direccion=JOptionPane.showInputDialog(null, "Introdusca la direccion del servidor","127.0.0.1");
   Integer puerto=Integer.getInteger(JOptionPane.showInputDialog(null, "Introdusca el puerto al que desea conectarse","3000"));
   cliente = new Socket(direccion, puerto);
   } catch (IOException ex) {
   Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
   }
   }*/
   public boolean status(){
       return cliente.isClosed();
   }
   public boolean serverIsLive() throws IOException{
   cliente.connect(cliente.getRemoteSocketAddress());
   return cliente.isConnected();
   }
    public void escribir(String mensaje){
        try {
            dsalida.writeUTF(mensaje);
        }catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String leer() throws IOException{
        recibido = dentrada.readUTF();
        return recibido;
    }
    public void cerrar(){
        try {
            dsalida.writeUTF("exit()");
            dsalida.close();
            dentrada.close();
            cliente.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}