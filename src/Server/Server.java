package Server;

import FIle.MyFile;

import javax.swing.*;
import javax.swing.plaf.TableHeaderUI;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Server implements Runnable{
    serverFrame serverframe;
    File[] filetosendarray ;
    Socket clientsocket;
    int countselectedfiles=0;
    int portno;
    int numberoffilestosend=0;

    Runnable r2;
    Runnable filecontentsendthred;
    Runnable threadfordownloadrequest;
    Runnable threadfordownloadallrequest;
    boolean isListening=false;
    boolean isSending=false;
    String downloadFolder;
    File filefordownload; //contains the name of file to download
    ArrayList<File> toDownloadfilelist = new ArrayList<>();
    HashSet<File> alreadyDownloadedfilelist = new HashSet<>();
    public static ArrayList<File> receivedFIlelist = new ArrayList<>();
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
           countselectedfiles++;
        }
        Thread thread2=new Thread(r2);
        thread2.start();
    }
    public void sendrequestfordownload(File filename,String downloadFolder){
        this.downloadFolder = downloadFolder;
        this.filefordownload = filename;
        Thread thread4 = new Thread(threadfordownloadrequest);
        thread4.start();
    }
    public void downloadall(String downloadFolder) {
        this.downloadFolder = downloadFolder;
        for(File file : receivedFIlelist){
            if(!alreadyDownloadedfilelist.contains(file)){
                toDownloadfilelist.add(file);
            }
        }
        Thread thread5 = new Thread(threadfordownloadallrequest);
        thread5.start();

    }
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(portno);
            while(true){
                clientsocket = serverSocket.accept();
                System.out.println("a new clinet has connected "+clientsocket);
                serverframe.displayGoodconnectionstatus("A new cilent has conncted : " + clientsocket);
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
                                serverframe.displayBadconnectionstatus("Error accepting client connection: " + e.getMessage());
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
                                            serverframe.displayBadconnectionstatus("Error accepting client connection: " + e.getMessage());

                                            break;
                                        }
                                        if (filenamelenght > 0) {
                                            byte[] filenamebyte = new byte[filenamelenght];
                                            try {
                                                dataInputStream.readFully(filenamebyte, 0, filenamelenght);
                                            } catch (IOException e) {
                                                System.err.println("Error accepting client connection: " + e.getMessage());
                                                serverframe.displayBadconnectionstatus("Error accepting client connection: " + e.getMessage());

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
                                            serverframe.showfiledetails(file.getName(), filezsize);
                                        }
                                    }
                                    break;
                                case "FILE_CONTENT"  :
                                    //for receving and downloading file content sent
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
                                            serverframe.displayBadconnectionstatus("Error accepting client connection: " + e.getMessage());

                                            break;
                                        }
                                        if(filenamelenght>0){
                                            byte[] filenamebyte = new byte[filenamelenght];
                                            try {
                                                dataInputStream.readFully(filenamebyte, 0, filenamelenght);
                                            } catch (IOException e) {
                                                System.err.println("Error accepting client connection: " + e.getMessage());
                                                serverframe.displayBadconnectionstatus("Error accepting client connection: " + e.getMessage());

                                                break;
                                            }
                                            String filename = new String(filenamebyte);

                                            try{
                                                long size = dataInputStream.readLong(); // read file size
                                                String direcotry = downloadFolder + "/" + filename;
                                                FileOutputStream fileOut = new FileOutputStream(direcotry);
                                                byte[] buffer = new byte[1024];
                                                serverframe.displayReceivingStatus(filename,"RECEIVING_CONTENT");
                                                while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
                                                    // Here we write the file using write method
                                                    fileOut.write(buffer, 0, bytes);
                                                    size -= bytes; // read upto file size
                                                }
                                                serverframe.displayReceivingStatus(filename,"RECEIVED_CONTENT");
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
                                            } catch (FileNotFoundException e) {
                                                throw new RuntimeException(e);
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }

                                    }
                                    break;
                                //for receiving download request files and sending download through filecontentsendthread
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
                                            Thread thread3 = new Thread(filecontentsendthred);
                                            thread3.start();
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    break;
                            }
                            isListening=false;
                        }else {
                            System.err.println("A process of  receiving files is in process");
                            serverframe.displayBadconnectionstatus("A process of  receiving files is in process");

                            break;
                        }

                    }
                    isListening=false;
                };
                //for sending the file name/ request for download all
                threadfordownloadallrequest = () -> {
                    if(!isSending){
                        isSending=true;
                        DataOutputStream out;
                        try {
                            out = new DataOutputStream(clientsocket.getOutputStream());

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
                        serverframe.displayBadconnectionstatus("A process of sending  files is in process");


                    }
                };
                //for sending file names / request for selected download
                threadfordownloadrequest = () ->{
                    if(!isSending){
                        isSending=true;
                        DataOutputStream out;
                        try {
                            out = new DataOutputStream(clientsocket.getOutputStream());

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
                        serverframe.displayBadconnectionstatus("A process of sending  files is in process");

                    }
                };
                //for sending file content
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
                                int totalBytesSent = 0;
//                        while ((bytesRead = fileInputStream.read(buffer, 0, 1024)) >= 0) {
//                            out.write(buffer, 0, bytesRead);
//                            out.flush();
//                            pos += bytesRead;
////                            System.out.println(pos + " bytes (" + bytesRead + " bytes read)");
//                        }
//                                serverframe.setSendingfilename(filename);
                                serverframe.displaySendingstatus(filecontenttosendnamearray.get(i).getName(),"FILE_CONTENT_SENDING");
                                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                                    // Send the file to Server Socket
                                    out.write(buffer, 0, bytesRead);
                                     totalBytesSent += bytesRead;
//                                    int progress = (int) (((double)(totalBytesSent /  filecontenttosendnamearray.get(i).length())) * 100);
//                                    System.out.println(progress);
//                                    if(progress >= 10 && progress % 10 == 0){
//                                        serverframe.setprogress(progress);
//                                    }
                                    out.flush();
                                }
                                serverframe.displaySendingstatus(filecontenttosendnamearray.get(i).getName(),"FILE_CONTENT_SENT");

                                fileInputStream.close();
                            }
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        filecontenttosendnamearray.clear();
                    }else {
                        System.err.println("A process of sending files is in process");
                        serverframe.displayBadconnectionstatus("A process of sending files is in process");

                    }
                    isSending=false;

                };
//                for sending file info to the peers
                r2 = () -> {
                    if(!isSending){
                        isSending=true;
                        try {
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

                                    String filename = filetosend.getAbsolutePath();
                                    byte[] filenamebyte = filename.getBytes();
                                    out.writeInt(filenamebyte.length);
                                    out.write(filenamebyte);

                                    //sending the size of file in bytes
                                    out.writeInt((int) filetosend.length());
                                    filetosend=null;
                                    serverframe.displaySendingstatus(filename,"FILE_INFO_SENT");
                                }
                            }
                        } catch (IOException e) {
                            System.err.println("Error accepting clinet connection: " + e.getMessage());
                            serverframe.displayBadconnectionstatus("Error accepting clinet connection: " + e.getMessage());

                        }
                        isSending=false;
                    }else {
                        System.err.println("A process of sending and receiving files is in process");
                        serverframe.displayBadconnectionstatus("A process of sending and receiving files is in process");


                    }

                };
                Thread thread1 = new Thread(r1);
                thread1.start();
                thread1.join();


            }
        } catch (IOException e) {
            System.err.println("Error accepting client connection: " + e.getMessage());
            serverframe.displayBadconnectionstatus("Error accepting clinet connection: " + e.getMessage());


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
