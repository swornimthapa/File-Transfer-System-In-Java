package Server;

import FIle.MyFile;

import javax.swing.*;
import javax.swing.plaf.TableHeaderUI;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Server implements Runnable{
    serverFrame serverframe;
    File[] filetosendarray ;
    Socket clientsocket;
    int countselectedfiles=0;
    int id=0;
    int portno;
    int numberoffilestosend=0;

    File filecontenttosendname;
    Runnable r2;
    Runnable filecontentsendthred;
    boolean isListening=false;
    boolean isSending=false;
    public static ArrayList<MyFile> filelist = new ArrayList<>();
    ArrayList<File> filecontenttosendnamearray = new ArrayList<>();
    public Server(String portno){
        this.portno=Integer.parseInt(portno); //to start the server
        serverframe = new serverFrame(this);
        Thread thread = new Thread(this); // to seperate serverFrame and sever class
        thread.start();
    }

    public void passfiletosend(File[] filetosend){
        this.filetosendarray = filetosend;
        countselectedfiles=0;
        for(int i=0;i<filetosendarray.length;i++){
//            System.out.println(filetosendarray[i]);
           countselectedfiles++;
        }
        Thread thread2=new Thread(r2);
        thread2.start();
    }
    //    public void sendrequestfordownload(String filename){
//         this.filefordownload = filename;
//         Thread thread3 = new Thread(threadfordownloadrequest);
//         thread3.start();
//    }
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(portno);
            while(true){
                clientsocket = serverSocket.accept();
                System.out.println("a new clinet has connected "+clientsocket);
//                DataInputStream dataInputStream = new DataInputStream(clientsocket.getInputStream());
                Runnable r1 = () -> {
                    DataInputStream dataInputStream = null;
                    try {
                        dataInputStream = new DataInputStream(clientsocket.getInputStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    while(true){
                        if(!isListening){
                            isListening=true;
                            //receive status
                            int statuslenght=0;
                            try {
                                statuslenght = dataInputStream.readInt();
                            } catch (IOException e) {
                                System.err.println("Error accepting client connection: " + e.getMessage());
                                break;
                            }
                            byte[] statusbyte = new byte[statuslenght];
                            try {
                                dataInputStream.readFully(statusbyte,0,statuslenght);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            String status = new String(statusbyte);

                            switch (status) {
                                //for sending
                                case "SEND_FOR_DOWNLOAD":
                                        try {
                                            numberoffilestosend=dataInputStream.readInt();
                                            for(int i=0;i<numberoffilestosend;i++) {
                                                int filenamelength = 0;
                                                filenamelength = dataInputStream.readInt();
                                                if (filenamelength > 0) {
                                                    byte[] filenamebyte = new byte[filenamelength];
                                                    dataInputStream.readFully(filenamebyte, 0, filenamelength);
                                                    String filename = new String(filenamebyte);
                                                    File filecontenttosendname = new File(filename);
                                                    filecontenttosendnamearray.add(filecontenttosendname);
                                                    System.out.println(filename);


                                                }
                                            }
                                            Thread thread2 = new Thread(filecontentsendthred);
                                            thread2.start();
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    break;
                            }
                            isListening=false;
                        }else {
                            System.err.println("A process of  receiving files is in process");
                            break;
                        }

                    }
                    isListening=false;
                };

                //for sending
                filecontentsendthred = () ->
                {
                    if(!isSending){
                        isSending=true;
                        try{
                            DataOutputStream out = new DataOutputStream(clientsocket.getOutputStream());


                            //sending status
                            String status = "FILE_CONTENT";
                            byte[] statusbyte = status.getBytes();
                            out.writeInt(statusbyte.length);
                            out.write(statusbyte);

                            out.writeInt(numberoffilestosend);

                            for(int i=0;i<numberoffilestosend;i++){
                                FileInputStream fileInputStream  = new FileInputStream(filecontenttosendnamearray.get(i));


                                String filename = filecontenttosendnamearray.get(i).getName();
                                byte[] filenamebyte = filename.getBytes();
                                out.writeInt(filenamebyte.length);
                                out.write(filenamebyte);

                                out.writeLong(filecontenttosendnamearray.get(i).length());

                                byte[] buffer = new byte[1024];
                                int bytesRead;
                                int pos = 0;
//                        while ((bytesRead = fileInputStream.read(buffer, 0, 1024)) >= 0) {
//                            out.write(buffer, 0, bytesRead);
//                            out.flush();
//                            pos += bytesRead;
////                            System.out.println(pos + " bytes (" + bytesRead + " bytes read)");
//                        }
                                while ((bytesRead = fileInputStream.read(buffer))
                                        != -1) {
                                    // Send the file to Server Socket
                                    out.write(buffer, 0, bytesRead);
                                    out.flush();
                                }
                                fileInputStream.close();
                            }

//                        out.close();
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        filecontenttosendnamearray.clear();
//                    filecontenttosendname=null;

                    }else {
                        System.err.println("A process of sending files is in process");
                    }
                    isSending=false;

                };
//                for sending file to the clinet
                r2 = () -> {
                    if(!isSending){
                        isSending=true;
                        try {
//                        FileInputStream fileInputStream = null;
                            System.out.println(filetosendarray);
                            if(filetosendarray!=null){
                                DataOutputStream out = new DataOutputStream(clientsocket.getOutputStream());

                                //sending status of f
                                String status = "SEND_FILE_INFO";
                                byte[] statusbyte = status.getBytes();
                                out.writeInt(statusbyte.length);
                                out.write(statusbyte);

                                out.writeInt(filetosendarray.length);
                                for(File filetosend : filetosendarray){
//                                fileInputStream = new FileInputStream(filetosend.getAbsolutePath()); // to access the file

                                    String filename = filetosend.getAbsolutePath();
                                    byte[] filenamebyte = filename.getBytes();
                                    out.writeInt(filenamebyte.length);
                                    out.write(filenamebyte);

                                    //sending the size of file in bytes
                                    out.writeInt((int) filetosend.length());

//                            fileInputStream.close();
//                            out.close();
                                    filetosend=null;
                                }



                            }
                        } catch (IOException e) {
                            System.err.println("Error accepting clinet connection: " + e.getMessage());
                        }
                        isSending=false;
                    }else {
                        System.err.println("A process of sending and receiving files is in process");

                    }

                };
                Thread thread1 = new Thread(r1);
                thread1.start();
                thread1.join();


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
