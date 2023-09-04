package Main;

import Client.Client;
import Client.clientFrame;
import Server.Server;
import  Server.serverFrame;
import UI_elements.RoundedButton;
import UI_elements.RoundedPanel;

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
    JTextField serverportfiled;

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
        JLabel serverportlable= new JLabel("Port no :" );
        serverportlable.setBounds(60,150,100,30);
        serverportlable.setBorder(BorderFactory.createLineBorder(Color.black));
        frame.add(serverportlable);

        serverportfiled= new JTextField();
        serverportfiled.setBounds(180,150,200,30);
        frame.add(serverportfiled);

        JLabel iplabel = new JLabel("IP Address");
        iplabel.setBounds(500,110,100,30);
        iplabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(iplabel);


        iptextfield=new JTextField();
        iptextfield.setBounds(670,110,200,30);
        frame.add(iptextfield);

        JLabel portlable= new JLabel("Port no :" );
        portlable.setBounds(500,150,100,30);
        portlable.setBorder(BorderFactory.createLineBorder(Color.black));
        frame.add(portlable);

        porttextfield= new JTextField();
        porttextfield.setBounds(670,150,200,30);
        frame.add(porttextfield);

        startclient.setBounds(500,300,150,30);
        frame.add(startclient);
        startclient.addActionListener(this);

        startserver.setBounds(80,300,150,30);
        frame.add(startserver);
        startserver.addActionListener(this);

        //example
//        RoundedButton b = new RoundedButton("test",20);
//        b.setBounds(10,10,150,30);
//        b.setBackground(Color.RED);
//        b.setBorder(BorderFactory.createLineBorder(Color.white));
//        frame.getContentPane().add(b);
//
//
//        RoundedPanel paneltest  = new RoundedPanel(30);
//        paneltest.setBounds(5,5,200,200);
//        frame.add(paneltest);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==startserver){
            if(serverportfiled.getText().isEmpty()){
                JOptionPane.showMessageDialog(frame,"FIll the port no for Server","Error",JOptionPane.ERROR_MESSAGE);
            }else{
                Server server = new Server(serverportfiled.getText());
            }
//            frame.dispose();
        }
        if(e.getSource()==startclient){
            if(iptextfield.getText().isEmpty() || porttextfield.getText().isEmpty()){
                JOptionPane.showMessageDialog(frame,"FIll all the details of the Server","Error",JOptionPane.ERROR_MESSAGE);
            }
            else {
                String ipaddress = iptextfield.getText();
                String portno = porttextfield.getText();
                Client client = new Client(ipaddress,Integer.parseInt(portno));
//                frame.dispose();
            }
        }

    }
}
