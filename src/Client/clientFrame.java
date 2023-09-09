package Client;



import javax.swing.*;

import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class clientFrame implements ActionListener, MouseListener {
    JFrame frame;
    JButton chooseFLle;
    JButton sendFile;
    JLabel subtitile;
    File[] filetosend;
    JButton previewbutton;
    JButton downloadbutton;
    JButton downloadAllbutton;
    Client client;
    //    JFileChooser jFileChooser;
    JTable filedetailstable;
    DefaultTableModel tableModel;
    int previewSelectedrowindex;
    DefaultTableModel selectedtableModel;
    JTable selectedfiledetailstable;
    JPanel forconnectionstatus;
    JPanel forSendingstatus;
    JScrollPane connectionStatuscrollpane;
    JScrollPane sendingStatuscrollpane;
    JScrollPane ReceivingStatuscrollpane;
    JPanel forReceivingstatus;
    public static boolean isDownloading=false;

    public clientFrame(Client clinet){
        this.client = clinet;

        frame= new JFrame("Clinet");
        frame.setSize(1200,700);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.white);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponent();
    }

    private void initializeComponent() {
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
        tablescrollpane.setBounds(670,50,500,200);
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
        selectedtablescrollpane.setBounds(10,50,500,200);
        filedetailstable.addMouseListener(this);
//        filedetailstable.addMouseListener(this);
        frame.add(selectedtablescrollpane);
//        previewbutton = new JButton("Preview");
//        previewbutton.setBounds(870,360,150,30);
//        previewbutton.addActionListener(this);
//        frame.getContentPane().add(previewbutton);

        downloadbutton = new JButton("Download");
        downloadbutton.setBounds(1030,260,150,30);
        downloadbutton.addActionListener(this);
        frame.getContentPane().add(downloadbutton);

        downloadAllbutton = new JButton("Download All");
        downloadAllbutton.setBounds(870,260,150,30);
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
        chooseFLle.setBounds(10,260,150,30);
        chooseFLle.addActionListener(this);
        frame.add(chooseFLle);


        sendFile = new JButton("send file");
        sendFile.setBounds(320,260,150,30);
        sendFile.addActionListener(this);
        frame.add(sendFile);

        //for displaying connection status
        forconnectionstatus = new JPanel();
        forconnectionstatus.setBackground(Color.BLACK);
        forconnectionstatus.setLayout(new BoxLayout(forconnectionstatus, BoxLayout.Y_AXIS));

        connectionStatuscrollpane = new JScrollPane(forconnectionstatus);
        connectionStatuscrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        connectionStatuscrollpane.setBounds(0,565,1188,100);
//        connectionStatuscrollpane.setBackground(Color.BLACK);
        connectionStatuscrollpane.getViewport().setBackground(Color.BLACK);
        connectionStatuscrollpane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(connectionStatuscrollpane);
//        connectionStatuscrollpane.add(forconnectionstatus);

        //for displaying sending status
        forSendingstatus = new JPanel();
        forSendingstatus.setBackground(Color.BLACK);
        forSendingstatus.setLayout(new BoxLayout(forSendingstatus, BoxLayout.Y_AXIS));

        sendingStatuscrollpane = new JScrollPane(forSendingstatus);
        sendingStatuscrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sendingStatuscrollpane.setBounds(10,350,500,150);
        sendingStatuscrollpane.getViewport().setBackground(Color.BLACK);
//        sendingStatuscrollpane.setBackground(Color.BLACK);
        sendingStatuscrollpane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add( sendingStatuscrollpane);
//        sendingStatuscrollpane.add(forSendingstatus);


        //for displaying receiving status
        forReceivingstatus = new JPanel();
        forReceivingstatus.setBackground(Color.BLACK);
        forReceivingstatus.setLayout(new BoxLayout(forReceivingstatus, BoxLayout.Y_AXIS));

        ReceivingStatuscrollpane = new JScrollPane(forReceivingstatus);
        ReceivingStatuscrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        ReceivingStatuscrollpane.setBounds(670,350,500,150);
        ReceivingStatuscrollpane.getViewport().setBackground(Color.BLACK);
//        sendingStatuscrollpane.setBackground(Color.BLACK);
        ReceivingStatuscrollpane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add( ReceivingStatuscrollpane);



    }
    public void displayBadconnectionstatus(String status){
        JPanel jpstatus = new JPanel();
        jpstatus.setBackground(Color.BLACK);
        jpstatus.setLayout(new BoxLayout(jpstatus, BoxLayout.Y_AXIS));


        JLabel jlstatus = new JLabel(status);
        Font timesNewRomanFont = new Font("Times New Roman", Font.ITALIC, 15);
        jlstatus.setFont(timesNewRomanFont);
        jlstatus.setForeground(Color.RED);
        jlstatus.setBorder(new EmptyBorder(1,0,1,0));
        jpstatus.add(jlstatus);
        forconnectionstatus.add(jpstatus);


        connectionStatuscrollpane.setViewportView(forconnectionstatus);
    }
    public void displayGoodconnectionstatus(String status){
        JPanel jpstatus = new JPanel();
        jpstatus.setBackground(Color.BLACK);
        jpstatus.setLayout(new BoxLayout(jpstatus, BoxLayout.Y_AXIS));


        JLabel jlstatus = new JLabel(status);
        Font timesNewRomanFont = new Font("Times New Roman", Font.PLAIN, 12);
        jlstatus.setFont(timesNewRomanFont);
        jlstatus.setForeground(Color.GREEN);
        jlstatus.setBorder(new EmptyBorder(5,0,5,0));
        jpstatus.add(jlstatus);
        forconnectionstatus.add(jpstatus);


        connectionStatuscrollpane.setViewportView(forconnectionstatus);

    }
    public void displaySendingstatus(String filename,String type){
        JPanel jpstatus = new JPanel();
        jpstatus.setBackground(Color.BLACK);
        jpstatus.setLayout(new BoxLayout(jpstatus, BoxLayout.Y_AXIS));
        JLabel jlstatus = null;
        switch (type){
            case "FILE_INFO_SENT":
                jlstatus = new JLabel("Sent "+filename+" Info");
                break;
            case "FILE_CONTENT_SENDING":
                jlstatus = new JLabel("Sending "+filename+"..........");
                break;
            case "FILE_CONTENT_SENT":
                jlstatus = new JLabel("Sent "+filename);
                break;
        }
        Font timesNewRomanFont = new Font("Times New Roman", Font.PLAIN, 15);
        jlstatus.setFont(timesNewRomanFont);
        jlstatus.setForeground(Color.RED);
        jlstatus.setBorder(new EmptyBorder(1,0,1,0));
        jpstatus.add(jlstatus);
        forSendingstatus.add(jpstatus);

        sendingStatuscrollpane.setViewportView(forSendingstatus);
    }
    public void displayReceivingStatus(String filename, String type){
        JPanel jpstatus = new JPanel();
        jpstatus.setBackground(Color.BLACK);
        jpstatus.setLayout(new BoxLayout(jpstatus, BoxLayout.Y_AXIS));
        JLabel jlstatus = null;
        switch (type){
            case "RECEIVING_CONTENT":
                jlstatus = new JLabel(filename+" : DOWNLOADING ...");
                break;
            case "RECEIVED_CONTENT":
                jlstatus = new JLabel(filename+" : Download Completed");

        }
        Font timesNewRomanFont = new Font("Times New Roman", Font.PLAIN, 15);
        jlstatus.setFont(timesNewRomanFont);
        jlstatus.setForeground(Color.green);
        jlstatus.setBorder(new EmptyBorder(1,0,1,0));
        jpstatus.add(jlstatus);
        forReceivingstatus.add(jpstatus);

        ReceivingStatuscrollpane.setViewportView(forReceivingstatus);
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
                            for(File file : Client.receivedFIlelist){
                                if(filename.equals(file.getName())){
                                    client.sendrequestfordownload(file,downloadFolder);
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
                        client.downloadall(downloadFolder);
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
                client.passfiletosend(filetosend);

            }
        }
    }
    //        if(e.getSource() == chooseFLle){
//            JFileChooser jFileChooser = new JFileChooser();
//            jFileChooser.setDialogTitle(" Chose a file to send");
//            if(jFileChooser. == JFileChooser.APPROVE_OPTION){
//                subtitile.setText(filetosend.getName());
//            }
//        }
//        if(e.getActionCommand().equals("ApproveSelection")){
//            if(filetosend!=null){
////                subtitile.setText("please select a file to send first");
//                System.out.println("dfsf");
//                client.passfiletosend(filetosend);
//            }
//        }
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
