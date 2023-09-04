package Client;

import FIle.MyFile;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


public class Client implements Runnable{
    clientFrame clientFrame;
    File filetosend ;
    Thread thread;
    Socket socket;
    int id=0;
    Runnable r2;
    Runnable threadfordownloadrequest;
    Runnable threadfordownloadallrequest;
    String downloadFolder;
    File filefordownload; //contains the name of file to download
    //    public static ArrayList<MyFile> filelist = new ArrayList<>();
    public static ArrayList<File> receivedFIlelist = new ArrayList<>();
    ArrayList<File> toDownloadfilelist = new ArrayList<>();
    HashSet<File> alreadyDownloadedfilelist = new HashSet<>();
    boolean isListenig=false;
    boolean isSending=false;
    public Client(String ipaddress,int portno){

        clientFrame = new clientFrame(this);
        try {
            socket = new Socket(ipaddress,portno);
            thread= new Thread(this);
            thread.start();
        } catch (IOException e) {
            System.err.println("Error accepting client connection: " + e.getMessage() + " : Restart with correct IP and Port Number");
        }


    }
    //    public void passfiletosend(File filetosend){
//        this.filetosend = filetosend;
//        Thread thread2=new Thread(r2);
//        thread2.start();
//    }
    public void sendrequestfordownload(File filename,String downloadFolder){
        this.downloadFolder = downloadFolder;
        this.filefordownload = filename;
        Thread thread3 = new Thread(threadfordownloadrequest);
        thread3.start();
    }
    public void downloadall(String downloadFolder) {
        this.downloadFolder = downloadFolder;
        for(File file : receivedFIlelist){
            if(!alreadyDownloadedfilelist.contains(file)){
                toDownloadfilelist.add(file);
            }
        }
        Thread thread4 = new Thread(threadfordownloadallrequest);
        thread4.start();

    }
    @Override
    public void run() {
        try {
            while(true){
//                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                //for receiving file
                Runnable r1 = () -> {
                    DataInputStream dataInputStream = null;
                    try {
                        dataInputStream = new DataInputStream(socket.getInputStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    while(true){
                        if(!isListenig){
                            isListenig=true;
                            //receive status
                            int statuslenght=0;
                            try {
                                statuslenght = dataInputStream.readInt();
                            } catch (IOException e) {
                                System.err.println("Error accepting client connection: " + e.getMessage() + ": Check if the Host is Running And Restart Your Application");
                                break;
                            }
                            byte[] statusbyte = new byte[statuslenght];
                            try {
                                dataInputStream.readFully(statusbyte,0,statuslenght);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            String status = new String(statusbyte);

                            switch (status){
                                case "SEND_FILE_INFO":
                                    int numberOffiles=0;
                                    try {
                                        numberOffiles = dataInputStream.readInt();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    System.out.println(numberOffiles);
                                    System.out.println("hello");
                                    for(int i=0;i<numberOffiles;i++){
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
                                            File file = new File(filename);


                                            //receives size of the file
                                            int filezsize=0;
                                            try {
                                                filezsize=dataInputStream.readInt();
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                            receivedFIlelist.add(file);
//                                            alreadyDownloadedfilelist.add(file);
                                            clientFrame.showfiledetails(file.getName(), filezsize);


                                        }
                                    }
                                    break;
                                case "FILE_CONTENT"  :
                                    int numberOffilecontenttoget=0;
                                    try {
                                        numberOffilecontenttoget = dataInputStream.readInt();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    for(int i=0;i<numberOffilecontenttoget;i++){
                                        int filenamelenght = 0;
                                        int bytes=0;
                                        try {
                                            filenamelenght = dataInputStream.readInt();
                                        } catch (IOException e) {
                                            System.err.println("Error accepting client connection: " + e.getMessage());
                                            break;
                                        }
                                        if(filenamelenght>0){
                                            byte[] filenamebyte = new byte[filenamelenght];
                                            try {
                                                dataInputStream.readFully(filenamebyte, 0, filenamelenght);
                                            } catch (IOException e) {
                                                System.err.println("Error accepting client connection: " + e.getMessage());
                                                break;
                                            }
                                            String filename = new String(filenamebyte);


                                            try{
                                                long size = dataInputStream.readLong(); // read file size
                                                String direcotry = downloadFolder + "/" + filename;
                                                FileOutputStream fileOut = new FileOutputStream(direcotry);
                                                byte[] buffer = new byte[1024];
                                                while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
                                                    // Here we write the file using write method
                                                    fileOut.write(buffer, 0, bytes);
                                                    size -= bytes; // read upto file size
                                                }
                                                // Here we received file
                                                System.out.println("File is Received");


//                                        FileOutputStream fileOut = new FileOutputStream(filename);
//                                        byte[] buffer = new byte[1024];
//                                        int bytesRead;
//                                        int pos = 0;
//                                        while ((bytesRead = dataInputStream.read(buffer, 0, 1024)) > 0) {
//                                            pos += bytesRead;
////                                            System.out.println(pos + " bytes (" + bytesRead + " bytes read)");
//                                            fileOut.write(buffer, 0, bytesRead);
//                                        }
                                                fileOut.close();
//                                        filename= null;
                                            } catch (FileNotFoundException e) {
                                                throw new RuntimeException(e);
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }

                                    }
                                    break;
                            }
                            isListenig=false;
                        }else{
                            System.err.println("A process of  receiving files is in process");
                        }

                    }
                    isListenig=false;

                };

                threadfordownloadallrequest = () -> {
                    if(!isSending){
                        isSending=true;
                        DataOutputStream out;
                        try {
                            out = new DataOutputStream(socket.getOutputStream());

                            String status = "SEND_FOR_DOWNLOAD";
                            byte[] statusbyte = status.getBytes();
                            out.writeInt(statusbyte.length);
                            out.write(statusbyte);

                            out.writeInt(toDownloadfilelist.size());

                            for(int i=0;i<toDownloadfilelist.size();i++){
                                String filename = toDownloadfilelist.get(i).getAbsolutePath();
                                byte[] filenamebyte = filename.getBytes();
                                out.writeInt(filenamebyte.length);
                                out.write(filenamebyte); //sends path of the file to download
                            }

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        toDownloadfilelist.clear();
                        isSending=false;
                    }else {
                        System.err.println("A process of sending  files is in process");
                    }
                };

                threadfordownloadrequest = () ->{
                    if(!isSending){
                        isSending=true;
                        DataOutputStream out;
                        try {
                            out = new DataOutputStream(socket.getOutputStream());

                            String status = "SEND_FOR_DOWNLOAD";
                            byte[] statusbyte = status.getBytes();
                            out.writeInt(statusbyte.length);
                            out.write(statusbyte);

                            out.writeInt(1);

                            String filename = filefordownload.getAbsolutePath();
                            byte[] filenamebyte = filename.getBytes();
                            out.writeInt(filenamebyte.length);
                            out.write(filenamebyte); //sends path of the file to download

                            alreadyDownloadedfilelist.add(new File(filename));

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        isSending=false;
                    }else {
                        System.err.println("A process of sending  files is in process");

                    }

                };


                Thread thread1=new Thread(r1);
                thread1.start();
                thread1.join();
                break;

            }
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

