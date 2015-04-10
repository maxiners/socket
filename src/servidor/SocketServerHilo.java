package servidor;

import java.awt.event.ActionEvent;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketServerHilo implements Runnable {
    final ArrayList<Socket> clientes;
    String recibido;
    OutputStream osalida;
    DataOutputStream dsalida;

    InputStream ientrada;
    DataInputStream dentrada;
    
    private final Gui_Servidor gui_Servidor;
	
    Socket socket;

    public SocketServerHilo(Socket lsocket,Gui_Servidor gui_Servidor1,ArrayList<Socket> clientes){
        this.gui_Servidor=gui_Servidor1;
        this.clientes=clientes;
        gui_Servidor.Benviar.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(!gui_Servidor.Aescribir.getText().equals("")){
                    escribirTodos(clientes,"Servidor -> "+gui_Servidor.Aescribir.getText());
                    gui_Servidor.Achat.append("\nServidor -> "+gui_Servidor.Aescribir.getText());
                    gui_Servidor.Aescribir.setText("");
                    }
            }
        });
        gui_Servidor.Aescribir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent e){
                if(e.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER){
                    if(!gui_Servidor.Aescribir.getText().equals("")){
                    escribirTodos(clientes,"Servidor -> "+gui_Servidor.Aescribir.getText());
                    gui_Servidor.Achat.append("\nServidor -> "+gui_Servidor.Aescribir.getText());
                    gui_Servidor.Aescribir.setText("");
                    }
                }
            }
            public void keyReleased(java.awt.event.KeyEvent e){
                if(e.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER){
                    gui_Servidor.Aescribir.setText("");
                }
            }
        });
            try{
                socket = lsocket;
                gui_Servidor.jTextArea.append("\n"+new Date().toString()+"  "+socket.getInetAddress().getCanonicalHostName()+" Se conectó!");
            }
            catch (Exception excepcion) {
		System.out.println(excepcion);
            }
    }
    public void escribir(Socket cliente,String msm){
        Thread escritura = new Thread(
            new Runnable() {
            @Override
            public void run() {
                try {
                    DataOutputStream dataOutputStream = new DataOutputStream(cliente.getOutputStream());
                    dataOutputStream.writeUTF(msm);
                    dataOutputStream.flush();

                }catch (IOException ex) {
                    Logger.getLogger(SocketServerHilo.class.getName()).log(Level.SEVERE, null, ex);
                }
             }
            }
        );
        escritura.start();
        
    }
    public void run() {
        
        try {
            ientrada = socket.getInputStream();
        } catch (IOException ex) {
            Logger.getLogger(SocketServerHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
            dentrada = new DataInputStream(ientrada);

            while(!socket.isClosed()){
                try {
                    recibido = dentrada.readUTF();
                } catch (IOException ex) {
                    limpieza();
                    break;
                    //Logger.getLogger(SocketServerHilo.class.getName()).log(Level.SEVERE, null, ex);
                }
                if(recibido.equals("exit()")){
                    limpieza();
                    break;//rompe ciclo, en caso de enviar la peticion de salida
                }
                else{
                    escribirTodos(clientes, recibido);
                }


                //System.out.println("recibido desde el cliente: " + recibido);
                gui_Servidor.jTextArea.append("\n"+new Date().toString()+"  "+socket.getInetAddress().getCanonicalHostName()+" Escribió");
                gui_Servidor.Achat.append("\n"+recibido);

            }
            gui_Servidor.jTextArea.append("\n"+new Date().toString()+"  "+socket.getInetAddress().getCanonicalHostName()+" Desconectado!");
           
    }
    public void limpieza(){
        Thread hilo = new Thread(
        new Runnable() {

        @Override
        public void run() {
            for(int i=0; i<clientes.size();i++){
                if(socket.equals(clientes.get(i))){
                    clientes.remove(i);
                    try {
                        dentrada.close();
                        //dsalida.close();
                        ientrada.close();
                        //osalida.close();
                        socket.close();

                    } catch (IOException ex) {
                        Logger.getLogger(SocketServerHilo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
            }
        });
        hilo.start();
    }
        public void escribirTodos(ArrayList<Socket> clientes, String recibido){
            //System.out.println("metodo escribirtodo: "+recibido);
            for(int i=0; i<clientes.size();i++){
                escribir(clientes.get(i), recibido);
            }
        }
    }
