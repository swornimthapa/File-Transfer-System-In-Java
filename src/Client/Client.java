package Client;

import javax.swing.*;
import java.io.*;
import java.net.Socket;


public class Client implements Runnable{
    File filetosend ;
    Thread thread;
    Socket socket;

    public Client(){

        clientFrame clientFrame = new clientFrame(this);
        try {
            socket = new Socket("localhost",11223);
        } catch (IOException e) {
            System.err.println("Error accepting client connection: " + e.getMessage());
        }

    }
    public void passfiletosend(File filetosend){
        this.filetosend = filetosend;
        thread= new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            FileInputStream fileInputStream = null;
            System.out.println(filetosend);
                if(filetosend!=null){
                    fileInputStream = new FileInputStream(filetosend.getAbsolutePath()); // to access the file
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                    String filename = filetosend.getName();
                    byte[] filenamebyte = filename.getBytes();

                    byte[] filecontentbyte = new byte[(int) filetosend.length()];
                    fileInputStream.read(filecontentbyte);

                    out.writeInt(filenamebyte.length);
                    out.write(filenamebyte);

                    out.writeInt(filecontentbyte.length);
                    out.write(filecontentbyte);

                    filetosend=null;
            }

        } catch (IOException e) {
            System.err.println("Error accepting server connection: " + e.getMessage());
        }
    }
}
