package Main;

import Client.Client;
import Client.clientFrame;
import Server.Server;
import  Server.serverFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window implements ActionListener {
    JFrame frame=new JFrame("File Transfer System");
    JButton startserver=new JButton("Start Server");
    JButton startclient=new JButton("Start Client");
    JButton startclinet;
    JTextField iptextfield;
    JTextField porttextfield;

    Window(){
        frame.setResizable(false);
        frame.setSize(900,500);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.white);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializecomponents();
    }

    private void initializecomponents() {
        JLabel iplabel = new JLabel("IP Address");
        iplabel.setBounds(400,110,100,30);
        iplabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(iplabel);

        iptextfield=new JTextField();
        iptextfield.setBounds(570,110,200,30);
        frame.add(iptextfield);

        JLabel portlable= new JLabel("Port no :" );
        portlable.setBounds(400,150,100,30);
        portlable.setBorder(BorderFactory.createLineBorder(Color.black));
        frame.add(portlable);

        porttextfield= new JTextField();
        porttextfield.setBounds(570,150,200,30);
        frame.add(porttextfield);

        startclient.setBounds(500,300,150,30);
        frame.add(startclient);
        startclient.addActionListener(this);

        startserver.setBounds(80,300,150,30);
        frame.add(startserver);
        startserver.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startserver){
            Server server = new Server();
//            frame.dispose();
        }
        if(e.getSource()==startclient){
//            if(iptextfield.getText().isEmpty() || porttextfield.getText().isEmpty()){
//                JOptionPane.showMessageDialog(frame,"FIll all the details of the Server","Error",JOptionPane.ERROR_MESSAGE);
//            }
//            else {
//                String ipaddress = iptextfield.getText();
//                String portno = porttextfield.getText();
////                frame.dispose();
//            }
            Client client = new Client();
        }

    }
}
