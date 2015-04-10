package servidor;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class SocketServer {
    
    //SocketServer socketServer;
    final ArrayList<Socket> clientes;
    
    public SocketServer(Gui_Servidor gui_Servidor){
        clientes = new ArrayList<>();
            try {
                ServerSocket servidor = new ServerSocket(3000, 65000);
                do{
                    System.out.println("Esperando cliente");							
                    clientes.add(servidor.accept());
                    Runnable nuevoSocket = new SocketServerHilo(clientes.get(clientes.size()-1), gui_Servidor,clientes);
                    Thread hiloSocket = new Thread(nuevoSocket);
                    hiloSocket.start();
                    System.out.println(clientes.size()+" tamano");
                }while(true);
            }
            catch (IOException excepcion) {			
                System.out.println(excepcion);
            }
        
    }
}