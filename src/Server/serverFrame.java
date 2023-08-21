package Server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class serverFrame implements ActionListener {
    JFrame frame;
    JTable filedetailstable;
    DefaultTableModel tableModel;
    JButton chooseFLle;
    JButton sendFile;
    JLabel subtitile;
    File filetosend;
    serverFrame(){
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
        recivedfilelable.setBounds(480,10,700,30);
        recivedfilelable.setOpaque(true);
        recivedfilelable.setBackground(Color.lightGray);
        recivedfilelable.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        recivedfilelable.setHorizontalAlignment(JLabel.CENTER);
        frame.getContentPane().add(recivedfilelable);


        tableModel=new DefaultTableModel();
        tableModel.addColumn("Filename");
        tableModel.addColumn("File size");
        filedetailstable=new JTable(tableModel);
        JScrollPane tablescrollpane = new JScrollPane(filedetailstable);
        tablescrollpane.setBounds(480,50,700,300);
        frame.add(tablescrollpane);

        JLabel sendfilelabel = new JLabel("Outgoing Files");
        sendfilelabel.setBounds(10,10,460,30);
        sendfilelabel.setHorizontalAlignment(JLabel.CENTER);
        sendfilelabel.setOpaque(true);
        sendfilelabel.setBackground(Color.lightGray);
        sendfilelabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.getContentPane().add(sendfilelabel);

        chooseFLle = new JButton("choose file");
        chooseFLle.setBounds(10,50,150,30);
        chooseFLle.addActionListener(this);
        frame.add(chooseFLle);


        JLabel filetosendlabel = new JLabel("File To Send");
        filetosendlabel.setBounds(10,280,150,30);
        filetosendlabel.setHorizontalAlignment(JLabel.CENTER);
        filetosendlabel.setOpaque(true);
        filetosendlabel.setBackground(Color.lightGray);
        filetosendlabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.getContentPane().add(filetosendlabel);

        subtitile = new JLabel("File To Send Not Selected");
        subtitile.setBounds(170,280,300,30);
        subtitile.setHorizontalAlignment(JLabel.CENTER);
        subtitile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(subtitile);

        sendFile = new JButton("send file");
        sendFile.setBounds(320,320,150,30);
        sendFile.addActionListener(this);
        frame.add(sendFile);

    }

    public void showfiledetails(String filename, int filesize ){
       Object[] newRow = {filename,filesize};
       tableModel.addRow(newRow);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        if(e.getSource() == chooseFLle){
//            JFileChooser jFileChooser = new JFileChooser();
//            jFileChooser.setDialogTitle(" Chose a file to send");
//            if(jFileChooser.showDialog(null,"open") == JFileChooser.APPROVE_OPTION){
//                filetosend = jFileChooser.getSelectedFile();  //filetosend will have the path of the selected file
//                subtitile.setText(filetosend.getName());
//            }
//        }
//        if(e.getSource()==sendFile){
//            if(filetosend==null){
//                subtitile.setText("please select a file to send first");
//            }else{
//                System.out.println("dfsf");
//                client.passfiletosend(filetosend);
//            }
//        }
    }
}
