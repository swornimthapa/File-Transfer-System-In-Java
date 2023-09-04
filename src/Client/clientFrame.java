package Client;



import javax.swing.*;

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


//        JLabel filetosendlabel = new JLabel("File To Send");
//        filetosendlabel.setBounds(10,280,150,30);
//        filetosendlabel.setHorizontalAlignment(JLabel.CENTER);
//        filetosendlabel.setOpaque(true);
//        filetosendlabel.setBackground(Color.lightGray);
//        filetosendlabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        frame.getContentPane().add(filetosendlabel);
//
//        subtitile = new JLabel("File To Send Not Selected");
//        subtitile.setBounds(170,280,300,30);
//        subtitile.setHorizontalAlignment(JLabel.CENTER);
//        subtitile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        frame.add(subtitile);

        sendFile = new JButton("send file");
        sendFile.setBounds(320,360,150,30);
        sendFile.addActionListener(this);
        frame.add(sendFile);


//        JLabel chosefilelabel= new JLabel("Chose A File To Send");
//        chosefilelabel.setBounds(10,50,460,30);
//        chosefilelabel.setHorizontalAlignment(JLabel.CENTER);
//        chosefilelabel.setOpaque(true);
//        chosefilelabel.setBackground(Color.lightGray);
//        chosefilelabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        frame.getContentPane().add(chosefilelabel);

//        jFileChooser= new JFileChooser();
//        jFileChooser.setBounds(10,40,400,400);
//        sendFile=jFileChooser.getUI().getDefaultButton(jFileChooser);
//        sendFile.addActionListener(this);
//        frame.add(jFileChooser);


//        JLabel titile = new JLabel("send file");
//        titile.setBounds(550,10,150,30);
//        titile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        frame.add(titile);


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
