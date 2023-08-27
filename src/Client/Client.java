package Client;

import FIle.MyFile;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Client implements Runnable{
    clientFrame clientFrame;
    File filetosend ;
    Thread thread;
    Socket socket;
    int id=0;
    Runnable r2;
    public static ArrayList<MyFile> filelist = new ArrayList<>();

    public Client(String ipaddress,int portno){

        clientFrame = new clientFrame(this);
        try {
            socket = new Socket(ipaddress,portno);
        } catch (IOException e) {
            System.err.println("Error accepting client connection: " + e.getMessage());
        }
        thread= new Thread(this);
        thread.start();

    }
    public void passfiletosend(File filetosend){
        this.filetosend = filetosend;
        Thread thread2=new Thread(r2);
        thread2.start();
    }

    @Override
    public void run() {
        try {
            while(true){
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                //for receiving file
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
                                clientFrame.showfiledetails(filename, filecontentlenght);
                                String fileExtension=getFileExtendion(filename);
                                filelist.add(new MyFile(id,filename,filecontentbyte,fileExtension));
                                id++;
                            }
                        }
                    }

                };
                //for sending file to the clinet
                 r2 = () -> {
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
                        System.err.println("Error accepting clinet connection: " + e.getMessage());
                    }
                };

                Thread thread1=new Thread(r1);
                thread1.start();
                thread1.join();
                break;

            }
        } catch (IOException e) {
            System.err.println("Error accepting client connection: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private String getFileExtendion(String filename) {
        int i = filename.lastIndexOf('.');
        if(i>0){
            return filename.substring(i+1);
        }else {
            return "no extension found";
        }
    }
}

