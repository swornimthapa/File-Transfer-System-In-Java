package Server;

import FIle.MyFile;
import FIle.filePreview;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;


public class serverFrame implements ActionListener, MouseListener {
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
        filedetailstable=new JTable(tableModel){
            public TableCellEditor getCellEditor(int row, int column) {
                return null; // Return null cell editor to make cells non-editable
            }
        };
        JScrollPane tablescrollpane = new JScrollPane(filedetailstable);
        tablescrollpane.setBounds(480,50,700,300);
        filedetailstable.addMouseListener(this);
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

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource()==filedetailstable){
            int selectedRowIndex = filedetailstable.getSelectedRow();
            if (selectedRowIndex != -1) {
                String filename = (String) tableModel.getValueAt(selectedRowIndex, 0);
                int fileSize = (int) tableModel.getValueAt(selectedRowIndex, 1);
                System.out.println("selected row:" + filedetailstable.getSelectedRow());
                System.out.println("Selected Filename: " + filename);
                System.out.println("Selected File Size: " + fileSize);
                int fileid = filedetailstable.getSelectedRow();
                for(MyFile file:Server.filelist){
                    if(fileid==file.getId()){
                        filePreview preview = new filePreview(file.getName(),file.getData(),file.getFileExtension());
                    }
                }

            }
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
