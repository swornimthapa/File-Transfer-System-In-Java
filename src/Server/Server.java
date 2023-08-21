package Server;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    serverFrame serverframe;
     public Server(){
          serverframe = new serverFrame();
          Thread thread = new Thread(this);
          thread.start();
    }

    private void startServer() {

    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(11223);
            while(true){
                Socket clientsocket = serverSocket.accept();
                System.out.println("a new clinet has connected "+clientsocket);
                DataInputStream dataInputStream = new DataInputStream(clientsocket.getInputStream());
                Runnable r1 = () -> {
                    while(true){
                        int filenamelenght = 0;
                        try {
                            filenamelenght = dataInputStream.readInt();
                        } catch (IOException e) {
                            System.err.println("Error accepting client connection: " + e.getMessage());
                            break;
                        }
                        if (filenamelenght > 0) {
                            byte[] filenamebyte = new byte[filenamelenght];
                            try {
                                dataInputStream.readFully(filenamebyte, 0, filenamelenght);
                            } catch (IOException e) {
                                System.err.println("Error accepting client connection: " + e.getMessage());
                                break;
                            }
                            String filename = new String(filenamebyte);
                            System.out.println(filename);
                            int filecontentlenght = 0;
                            try {
                                filecontentlenght = dataInputStream.readInt();
                            } catch (IOException e) {
                                System.err.println("Error accepting client connection: " + e.getMessage());
                                break;
                            }
                            if (filecontentlenght > 0) {
                                byte[] filecontentbyte = new byte[filecontentlenght];
                                try {
                                    dataInputStream.readFully(filecontentbyte, 0, filecontentlenght);
                                } catch (IOException e) {
                                    System.err.println("Error accepting client connection: " + e.getMessage());
                                    break;
                                }
                                System.out.println("read contenet");
                                serverframe.showfiledetails(filename, filecontentlenght);

                            }
                        }
                    }
                };
                Thread thread1=new Thread(r1);
                thread1.start();
            }
        } catch (IOException e) {
            System.err.println("Error accepting client connection: " + e.getMessage());
        }
    }
}
