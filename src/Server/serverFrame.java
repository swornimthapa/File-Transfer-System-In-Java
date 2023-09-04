package Server;

import Client.Client;
import FIle.MyFile;
import FIle.filePreview;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class serverFrame implements ActionListener, MouseListener {
    JFrame frame;
    JTable filedetailstable;
    JTable selectedfiledetailstable;
    DefaultTableModel tableModel;
    DefaultTableModel selectedtableModel;
    JButton chooseFLle;
    JButton sendFile;
    JButton downloadAllbutton;
    File[] filetosend;
    JButton previewbutton;
    JButton downloadbutton;
    Server server;
    int previewSelectedrowindex;
    public static boolean isDownloading=false;

    serverFrame(Server server){
        this.server=server;

        frame =new JFrame("Server");
        frame.setResizable(false);
        frame.setSize(1200,700);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.WHITE);

        initializecomponents();
    }

    private void initializecomponents() {
        JLabel recivedfilelable = new JLabel("Incomming Files");
        recivedfilelable.setBounds(670,10,500,30);
        recivedfilelable.setOpaque(true);
        recivedfilelable.setBackground(Color.lightGray);
        recivedfilelable.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        recivedfilelable.setHorizontalAlignment(JLabel.CENTER);
        frame.getContentPane().add(recivedfilelable);


        tableModel=new DefaultTableModel();
        tableModel.addColumn("Filename");
        tableModel.addColumn("File size");
        filedetailstable=new JTable(tableModel){
            public TableCellEditor getCellEditor(int row, int column) {
                return null; // Return null cell editor to make cells non-editable
            }
        };
        JScrollPane tablescrollpane = new JScrollPane(filedetailstable);
        tablescrollpane.setBounds(670,50,500,300);
        filedetailstable.addMouseListener(this);
        frame.add(tablescrollpane);


        selectedtableModel=new DefaultTableModel();
        selectedtableModel.addColumn("Filename");
        selectedtableModel.addColumn("File size");
        selectedfiledetailstable=new JTable(selectedtableModel){
            public TableCellEditor getCellEditor(int row, int column) {
                return null; // Return null cell editor to make cells non-editable
            }
        };
        JScrollPane selectedtablescrollpane = new JScrollPane(selectedfiledetailstable);
        selectedtablescrollpane.setBounds(10,50,500,300);
//        filedetailstable.addMouseListener(this);
        frame.add(selectedtablescrollpane);


//        previewbutton = new JButton("Preview");
//        previewbutton.setBounds(870,360,150,30);
//        previewbutton.addActionListener(this);
//        frame.getContentPane().add(previewbutton);

        downloadbutton = new JButton("Download");
        downloadbutton.setBounds(1030,360,150,30);
        downloadbutton.addActionListener(this);
        frame.getContentPane().add(downloadbutton);

        downloadAllbutton = new JButton("Download All");
        downloadAllbutton.setBounds(870,360,150,30);
        downloadAllbutton.addActionListener(this);
        frame.getContentPane().add(downloadAllbutton);


        JLabel sendfilelabel = new JLabel("Outgoing Files");
        sendfilelabel.setBounds(10,10,460,30);
        sendfilelabel.setHorizontalAlignment(JLabel.CENTER);
        sendfilelabel.setOpaque(true);
        sendfilelabel.setBackground(Color.lightGray);
        sendfilelabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.getContentPane().add(sendfilelabel);


        chooseFLle = new JButton("choose file");
        chooseFLle.setBounds(10,360,150,30);
        chooseFLle.addActionListener(this);
        frame.add(chooseFLle);


        sendFile = new JButton("send file");
        sendFile.setBounds(320,360,150,30);
        sendFile.addActionListener(this);
        frame.add(sendFile);

    }

    public void showfiledetails(String filename, int filesize ){
       Object[] newRow = {filename,filesize};
       tableModel.addRow(newRow);
    }
    public void showSelecteffiledetails(File[] file){
        for(File filetemp :file){
            String filename = filetemp.getName();
            int filesize = (int) filetemp.length();
            System.out.println(filename+filesize);
            Object[] newRow = {filename,filesize};
            selectedtableModel.addRow(newRow);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //for receiving file
//        if(e.getSource()==previewbutton){
//            if(!isDownloading){
//                if(tableModel.getRowCount()>0){
//                    if (previewSelectedrowindex != -1) {
//                        String filename = (String) tableModel.getValueAt(previewSelectedrowindex, 0);
//                        int fileSize = (int) tableModel.getValueAt(previewSelectedrowindex, 1);
//                        System.out.println("selected row:" + filedetailstable.getSelectedRow());
//                        System.out.println("Selected Filename: " + filename);
//                        System.out.println("Selected File Size: " + fileSize);
//                        int fileid = filedetailstable.getSelectedRow();
//                        for(MyFile file:Server.filelist){
//                            if(fileid==file.getId()){
//                                filePreview preview = new filePreview(file.getName(),file.getData(),file.getFileExtension());
//                                break;
//                            }
//                        }
//
//                    }
//                }
//            }
//
//        }
        if(e.getSource()==downloadbutton){
            if(!isDownloading){
                if(tableModel.getRowCount()>0) {
                    if (previewSelectedrowindex != -1) {
                        isDownloading=true;
                        JFileChooser jFileChooser = new JFileChooser();
                        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        jFileChooser.setDialogTitle(" Chose a folder to Download");
                        if(jFileChooser.showDialog(null,"open") == JFileChooser.APPROVE_OPTION){
                            String downloadFolder = String.valueOf(jFileChooser.getSelectedFile());
                            String filename = (String) tableModel.getValueAt(previewSelectedrowindex, 0);
//                            System.out.println(filename);
//                            int fileid = filedetailstable.getSelectedRow();
                            for(File file : Server.receivedFIlelist){
                                if(filename.equals(file.getName())){
                                    server.sendrequestfordownload(file,downloadFolder);
                                    break;
                                }
                            }
                        }
                    }
                }

            }
            isDownloading=false;
        }

        if(e.getSource()==downloadAllbutton){
            if(!isDownloading){
                if(tableModel.getRowCount()>0) {
                    isDownloading=true;
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    jFileChooser.setDialogTitle(" Chose a folder to Download");
                    if(jFileChooser.showDialog(null,"open") == JFileChooser.APPROVE_OPTION) {
                        String downloadFolder = String.valueOf(jFileChooser.getSelectedFile());
                        server.downloadall(downloadFolder);
                    }
//                        Client.receivedDownloadallfilelist.clear();
                }
            }
            isDownloading=false;
        }

        //for sending file
        if(e.getSource() == chooseFLle){

            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setMultiSelectionEnabled(true);
            jFileChooser.setDialogTitle(" Chose a file to send");
            if(jFileChooser.showDialog(null,"open") == JFileChooser.APPROVE_OPTION){
                selectedtableModel.setRowCount(0);
                filetosend = jFileChooser.getSelectedFiles();  //filetosend will have the path of the selected file
                showSelecteffiledetails(filetosend);
            }
        }
        if(e.getSource()==sendFile){
            if(filetosend==null){
                System.out.println("please select a file to send first");
//                subtitile.setText("please select a file to send first");
            }else{
                System.out.println("dfsf");
                server.passfiletosend(filetosend);

            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource()==filedetailstable){
            previewSelectedrowindex=filedetailstable.getSelectedRow();
//            int selectedRowIndex = filedetailstable.getSelectedRow();
//            if (selectedRowIndex != -1) {
//                String filename = (String) tableModel.getValueAt(selectedRowIndex, 0);
//                int fileSize = (int) tableModel.getValueAt(selectedRowIndex, 1);
//                System.out.println("selected row:" + filedetailstable.getSelectedRow());
//                System.out.println("Selected Filename: " + filename);
//                System.out.println("Selected File Size: " + fileSize);
//                int fileid = filedetailstable.getSelectedRow();
//                for(MyFile file:Server.filelist){
//                    if(fileid==file.getId()){
//                        filePreview preview = new filePreview(file.getName(),file.getData(),file.getFileExtension());
//                    }
//                }
//
//            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
