package Client;




import UI_elements.RoundedButton;
import UI_elements.RoundedLabel;
import UI_elements.RoundedPanel;

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


public class clientFrame implements ActionListener, MouseListener {
    JFrame frame;
    JButton chooseFLle;
    JButton sendFile;

    File[] filetosend;

    JButton downloadbutton;
    JButton downloadAllbutton;
    Client client;

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
    JTextField sendingSecretkeyfield;
    JTextField receivingSecretkeyfield;

    public clientFrame(Client clinet){
        this.client = clinet;

        frame= new JFrame("File Transfer System");
        frame.setSize(1200,700);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.BLACK);
        initializeComponent();
    }

    private void initializeComponent() {
        RoundedLabel recivedfilelable = new RoundedLabel("Incomming Files",30);
        recivedfilelable.setBounds(820,25,150,30);

        recivedfilelable.setBackground(Color.decode("#00563E"));
        recivedfilelable.setForeground(Color.white);
        recivedfilelable.setBorder(BorderFactory.createLineBorder(Color.decode("#013221")));
        recivedfilelable.setHorizontalAlignment(JLabel.CENTER);
        frame.getContentPane().add(recivedfilelable);


        //for incomming
        tableModel=new DefaultTableModel();
        tableModel.addColumn("Filename");
        tableModel.addColumn("File size");
        filedetailstable=new JTable(tableModel){
            public TableCellEditor getCellEditor(int row, int column) {
                return null; // Return null cell editor to make cells non-editable
            }
        };
        filedetailstable.setBackground(Color.white);

        JScrollPane tablescrollpane = new JScrollPane(filedetailstable);
        tablescrollpane.setBounds(640,60,500,200);
        tablescrollpane.setBackground(Color.white);
        tablescrollpane.getViewport().setBackground(Color.WHITE);
        tablescrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
//            tablescrollpane.setBorder(null);
        filedetailstable.addMouseListener(this);
        frame.add(tablescrollpane);

        downloadbutton = new RoundedButton("Download",30);
        downloadbutton.setBounds(990,270,150,30);
        downloadbutton.setBackground(Color.decode("#00563E"));
        downloadbutton.setBorder(BorderFactory.createLineBorder(Color.decode("#013221")));
        downloadbutton.setForeground(Color.white);
        downloadbutton.addActionListener(this);
        frame.getContentPane().add(downloadbutton);


        downloadAllbutton = new RoundedButton("Download All",30);
        downloadAllbutton.setBounds(830,270,150,30);
        downloadAllbutton.setBackground(Color.decode("#00563E"));
        downloadAllbutton.setBorder(BorderFactory.createLineBorder(Color.decode("#013221")));
        downloadAllbutton.setForeground(Color.white);
        downloadAllbutton.addActionListener(this);
        frame.getContentPane().add(downloadAllbutton);



        //for outgoing
        selectedtableModel=new DefaultTableModel();
        selectedtableModel.addColumn("Filename");
        selectedtableModel.addColumn("File size");
        selectedfiledetailstable=new JTable(selectedtableModel){
            public TableCellEditor getCellEditor(int row, int column) {
                return null; // Return null cell editor to make cells non-editable
            }
        };
        selectedfiledetailstable.setBackground(Color.white);
        JScrollPane selectedtablescrollpane = new JScrollPane(selectedfiledetailstable);
        selectedtablescrollpane.setBackground(Color.white);
        selectedtablescrollpane.getViewport().setBackground(Color.WHITE);
        selectedtablescrollpane.setBounds(40,60,500,200);
        selectedtablescrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        frame.add(selectedtablescrollpane);


        RoundedLabel sendfilelabel = new RoundedLabel("Outgoing Files",30);
        sendfilelabel.setBounds(220,25,150,30);
        sendfilelabel.setHorizontalAlignment(JLabel.CENTER);
//            sendfilelabel.setOpaque(true);
        sendfilelabel.setBackground(Color.decode("#660001"));
        sendfilelabel.setForeground(Color.white);
        sendfilelabel.setBorder(BorderFactory.createLineBorder(Color.decode("#3D0C01")));
        frame.getContentPane().add(sendfilelabel);


        chooseFLle = new RoundedButton("Choose file",30);
        chooseFLle.setBounds(230,270,150,30);
        chooseFLle.setBackground(Color.decode("#660001"));
        chooseFLle.setBorder(BorderFactory.createLineBorder(Color.decode("#3D0C01")));
        chooseFLle.setForeground(Color.white);
        chooseFLle.addActionListener(this);
        frame.getContentPane().add(chooseFLle);


        sendFile = new RoundedButton("Send file",30);
        sendFile.setBounds(390,270,150,30);
        sendFile.setBackground(Color.decode("#660001"));
        sendFile.setBorder(BorderFactory.createLineBorder(Color.decode("#3D0C01")));
        sendFile.setForeground(Color.white);
        sendFile.addActionListener(this);
        frame.getContentPane().add( sendFile);



        //for displaying connection status
        forconnectionstatus = new JPanel();
        forconnectionstatus.setBackground(Color.BLACK);
        forconnectionstatus.setLayout(new BoxLayout(forconnectionstatus, BoxLayout.Y_AXIS));
        connectionStatuscrollpane = new JScrollPane(forconnectionstatus);
        connectionStatuscrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        connectionStatuscrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        connectionStatuscrollpane.setBounds(20,565,1188,100);
        connectionStatuscrollpane.getViewport().setBackground(Color.BLACK);
        connectionStatuscrollpane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(connectionStatuscrollpane);


        //for displaying sending status
        forSendingstatus = new JPanel();
        forSendingstatus.setBackground(Color.BLACK);
        forSendingstatus.setLayout(new BoxLayout(forSendingstatus, BoxLayout.Y_AXIS));

        sendingStatuscrollpane = new JScrollPane(forSendingstatus);
        sendingStatuscrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        sendingStatuscrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sendingStatuscrollpane.setBounds(40,360,500,150);
        sendingStatuscrollpane.getViewport().setBackground(Color.BLACK);
        sendingStatuscrollpane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add( sendingStatuscrollpane);



        //for displaying receiving status
        forReceivingstatus = new JPanel();
        forReceivingstatus.setBackground(Color.BLACK);
        forReceivingstatus.setLayout(new BoxLayout(forReceivingstatus, BoxLayout.Y_AXIS));

        ReceivingStatuscrollpane = new JScrollPane(forReceivingstatus);
        ReceivingStatuscrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        ReceivingStatuscrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        ReceivingStatuscrollpane.setBounds(640,360,500,150);
        ReceivingStatuscrollpane.getViewport().setBackground(Color.BLACK);
        //        sendingStatuscrollpane.setBackground(Color.BLACK);
        ReceivingStatuscrollpane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add( ReceivingStatuscrollpane);

        //for secret key
        RoundedLabel sendingsecretkeylabel = new RoundedLabel("Secret Key",30);
        sendingsecretkeylabel .setBounds(40,310,150,30);
        sendingsecretkeylabel .setHorizontalAlignment(JLabel.CENTER);
        sendingsecretkeylabel .setBackground(Color.decode("#660001"));
        sendingsecretkeylabel .setForeground(Color.white);
        sendingsecretkeylabel .setBorder(BorderFactory.createLineBorder(Color.decode("#3D0C01")));
        frame.getContentPane().add(sendingsecretkeylabel);

        sendingSecretkeyfield = new JTextField();
        sendingSecretkeyfield.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        sendingSecretkeyfield.setBorder(BorderFactory.createLineBorder(Color.decode("#013221")));
        sendingSecretkeyfield.setBounds(230,310, 200, 30);
        frame.getContentPane().add( sendingSecretkeyfield);


        RoundedLabel receivingsecretkeylabel = new RoundedLabel("Secret Key",30);
        receivingsecretkeylabel .setBounds(640,310,150,30);
        receivingsecretkeylabel .setHorizontalAlignment(JLabel.CENTER);
        receivingsecretkeylabel.setBackground(Color.decode("#00563E"));
        receivingsecretkeylabel.setForeground(Color.white);
        receivingsecretkeylabel.setBorder(BorderFactory.createLineBorder(Color.decode("#013221")));
        receivingsecretkeylabel.setHorizontalAlignment(JLabel.CENTER);
        frame.getContentPane().add(receivingsecretkeylabel);

        receivingSecretkeyfield= new JTextField();
        receivingSecretkeyfield.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        receivingSecretkeyfield.setBorder(BorderFactory.createLineBorder(Color.decode("#013221")));
        receivingSecretkeyfield.setBounds(830,310, 200, 30);
        frame.getContentPane().add(receivingSecretkeyfield);



        RoundedPanel reveivedfilepanel = new RoundedPanel(30);
        reveivedfilepanel.setBounds(620,20,540,510);
        reveivedfilepanel.setBackground(Color.decode("#013221"));
        frame.getContentPane().add(reveivedfilepanel);

        RoundedPanel sendingfilepanel = new RoundedPanel(30);
        sendingfilepanel.setBounds(20,20,540,510);
        sendingfilepanel.setBackground(Color.decode("#3D0C01"));
        frame.getContentPane().add(sendingfilepanel);



    }
    public String getSendingsecretkey(){
        return sendingSecretkeyfield.getText();
    }

    public String getReceivingsecretkey(){
        return receivingSecretkeyfield.getText();
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
        JScrollBar verticalScrollBar = connectionStatuscrollpane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());

        connectionStatuscrollpane.revalidate();
        connectionStatuscrollpane.repaint();
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
        JScrollBar verticalScrollBar = connectionStatuscrollpane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());

        connectionStatuscrollpane.revalidate();
        connectionStatuscrollpane.repaint();

    }
    public void displaySendingstatus(String filename,String type){
        JPanel jpstatus = new JPanel();
        jpstatus.setBackground(Color.BLACK);
        jpstatus.setLayout(new BoxLayout(jpstatus, BoxLayout.Y_AXIS));
        JLabel jlstatus = null;
        Font timesNewRomanFont;
        switch (type){
            case "FILE_INFO_SENT":
                jlstatus = new JLabel("SENT INFO : "+filename);
                timesNewRomanFont = new Font("Times New Roman", Font.PLAIN, 15);
                jlstatus.setFont(timesNewRomanFont);
                jlstatus.setForeground(Color.GREEN);
                break;
            case "FILE_CONTENT_SENDING":
                jlstatus = new JLabel("SENDING : "+filename);
                timesNewRomanFont = new Font("Times New Roman", Font.ITALIC, 15);
                jlstatus.setFont(timesNewRomanFont);
                jlstatus.setForeground(Color.RED);
                break;
            case "FILE_CONTENT_SENT":
                jlstatus = new JLabel("SENT : "+filename);
                timesNewRomanFont = new Font("Times New Roman", Font.PLAIN, 15);
                jlstatus.setFont(timesNewRomanFont);
                jlstatus.setForeground(Color.GREEN);
                break;
        }

        jlstatus.setBorder(new EmptyBorder(1,0,1,0));
        jpstatus.add(jlstatus);
        forSendingstatus.add(jpstatus);

        sendingStatuscrollpane.setViewportView(forSendingstatus);
        JScrollBar verticalScrollBar = connectionStatuscrollpane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());

        connectionStatuscrollpane.revalidate();
        connectionStatuscrollpane.repaint();
    }
    public void displayReceivingStatus(String filename, String type){
        JPanel jpstatus = new JPanel();
        jpstatus.setBackground(Color.BLACK);
        jpstatus.setLayout(new BoxLayout(jpstatus, BoxLayout.Y_AXIS));
        JLabel jlstatus = null;
        Font timesNewRomanFont;
        switch (type){
            case "RECEIVING_CONTENT":
                jlstatus = new JLabel("DOWNLOADING : "+filename);
                timesNewRomanFont = new Font("Times New Roman", Font.ITALIC, 15);
                jlstatus.setFont(timesNewRomanFont);
                jlstatus.setForeground(Color.red);
                break;
            case "RECEIVED_CONTENT":
                jlstatus = new JLabel("DOWNLOAD COMPLETED : "+filename);
                timesNewRomanFont = new Font("Times New Roman", Font.PLAIN, 15);
                jlstatus.setFont(timesNewRomanFont);
                jlstatus.setForeground(Color.GREEN);

        }

        jlstatus.setBorder(new EmptyBorder(1,0,1,0));
        jpstatus.add(jlstatus);
        forReceivingstatus.add(jpstatus);

        ReceivingStatuscrollpane.setViewportView(forReceivingstatus);

        JScrollBar verticalScrollBar = connectionStatuscrollpane.getVerticalScrollBar();
        verticalScrollBar.setValue(verticalScrollBar.getMaximum());

        connectionStatuscrollpane.revalidate();
        connectionStatuscrollpane.repaint();
    }
    public void showfiledetails(String filename, int filesize ){
        Object[] newRow = {filename,filesize};
        tableModel.addRow(newRow);
    }
    public void showSelecteffiledetails(File[] file){
        for(File filetemp :file){
            String filename = filetemp.getName();
            int filesize = (int) filetemp.length();
//            System.out.println(filename+filesize);
            Object[] newRow = {filename,filesize};
            selectedtableModel.addRow(newRow);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==downloadbutton){
            if(!isDownloading){
                if(!receivingSecretkeyfield.getText().isEmpty()){
                    if(tableModel.getRowCount()>0) {
                        if (previewSelectedrowindex != -1) {
                            isDownloading=true;
                            JFileChooser jFileChooser = new JFileChooser();
                            jFileChooser.setPreferredSize(new Dimension(600,450));

                            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                            jFileChooser.setDialogTitle(" Chose a folder to Download");
                            if(jFileChooser.showDialog(null,"open") == JFileChooser.APPROVE_OPTION){
                                String downloadFolder = String.valueOf(jFileChooser.getSelectedFile());
                                String filename = (String) tableModel.getValueAt(previewSelectedrowindex, 0);
                                for(File file : Client.receivedFIlelist){
                                    if(filename.equals(file.getName())){
                                        client.sendrequestfordownload(file,downloadFolder);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }else {
                    JOptionPane.showMessageDialog(frame, "Please enter the SecretKey First", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            isDownloading=false;
        }

        if(e.getSource()==downloadAllbutton){
            if(!isDownloading){
                if(!receivingSecretkeyfield.getText().isEmpty()){
                    if(tableModel.getRowCount()>0) {
                        isDownloading=true;
                        JFileChooser jFileChooser = new JFileChooser();
                        jFileChooser.setPreferredSize(new Dimension(600,450));
                        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        jFileChooser.setDialogTitle(" Chose a folder to Download");
                        if(jFileChooser.showDialog(null,"open") == JFileChooser.APPROVE_OPTION) {
                            String downloadFolder = String.valueOf(jFileChooser.getSelectedFile());
                            client.downloadall(downloadFolder);
                        }
//                        Client.receivedDownloadallfilelist.clear();
                    }
                }else {
                    JOptionPane.showMessageDialog(frame, "Please enter the SecretKey First", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
            isDownloading=false;
        }

       //for sending file
        if(e.getSource() == chooseFLle){

            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setPreferredSize(new Dimension(600,450));
            jFileChooser.setMultiSelectionEnabled(true);
            jFileChooser.setDialogTitle(" Chose a file to send");
            if(jFileChooser.showDialog(null,"open") == JFileChooser.APPROVE_OPTION){
                selectedtableModel.setRowCount(0);
                filetosend = jFileChooser.getSelectedFiles();  //filetosend will have the path of the selected file
                showSelecteffiledetails(filetosend);
            }
        }
        if(e.getSource()==sendFile){
            if(filetosend!=null && !sendingSecretkeyfield.getText().isEmpty()){
                client.passfiletosend(filetosend);
            }else {
                JOptionPane.showMessageDialog(frame, "Please enter a SecretKey and Chose a File First", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        if(e.getSource()==filedetailstable){
            previewSelectedrowindex=filedetailstable.getSelectedRow();

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
