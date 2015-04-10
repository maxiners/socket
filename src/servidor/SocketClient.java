package servidor;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
 
public class SocketClient {
 
    public static void main(String args[]) {

    String recibido;
    OutputStream osalida;
    DataOutputStream dsalida;

    InputStream ientrada;
    DataInputStream dentrada;

    Socket cliente;

        try {
            cliente = new Socket("192.168.8.100", 3000);  
            osalida = cliente.getOutputStream();
            dsalida = new DataOutputStream(osalida);

            ientrada = cliente.getInputStream();
            dentrada = new DataInputStream(ientrada);

            recibido = dentrada.readUTF();
            System.out.println("Recibido desde el Server" + recibido);

            dsalida.writeUTF("Hola sockets server desde el cliente");
            Thread.sleep(1000);
            dsalida.writeUTF("hola que hace");
            Thread.sleep(1000);
            dsalida.writeUTF("ya casi me voy");
            Thread.sleep(1000);
            dsalida.writeUTF("bye");
            dsalida.writeUTF(JOptionPane.showInputDialog("mensaje a server"));
            dsalida.writeUTF("jjj");

            /*dsalida.close();
            dentrada.close();
            cliente.close();*/
        }
        catch (Exception e) {
                System.err.println("Error: " + e);
        }
    }
}