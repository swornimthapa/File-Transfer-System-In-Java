
package Main;

import Client.Client;
import Server.Server;
import UI_elements.RoundedButton;
import UI_elements.RoundedLabel;
import UI_elements.RoundedPanel;

import javax.annotation.processing.RoundEnvironment;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.CollationElementIterator;

public class Window implements ActionListener {
    JFrame frame = new JFrame("File Transfer System");
    JButton startserver;
    JButton startclient;
    JTextField iptextfield;
    JTextField porttextfield;
    JTextField serverportfiled;

    Window() {
        frame.setResizable(false);
        frame.setSize(900, 500);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.white);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.black);

        initializecomponents();
    }

    private void initializecomponents() {

        RoundedLabel serverlable1 = new RoundedLabel("Host A Connection",30);
        serverlable1.setBounds(50,35,330,70);
        serverlable1.setForeground(Color.white);
        serverlable1.setBackground(Color.decode("#660001"));
        serverlable1.setBorder(BorderFactory.createLineBorder(Color.decode("#3D0C01")));
        frame.getContentPane().add(serverlable1);

        RoundedLabel clientlable1 = new RoundedLabel("Connect To The Host",30);
        clientlable1.setBounds(490,35,330,70);
        clientlable1.setForeground(Color.white);
        clientlable1.setBackground(Color.decode("#00563E"));
        clientlable1.setBorder(BorderFactory.createLineBorder(Color.decode("#013221")));
        frame.getContentPane().add(clientlable1);

        RoundedPanel serverpanel1 = new RoundedPanel(30);
        serverpanel1.setBounds(20,20,400,100);
        serverpanel1.setBackground(Color.decode("#3D0C01"));
        frame.getContentPane().add(serverpanel1);

        RoundedPanel clinetpanel1 = new RoundedPanel(30);
        clinetpanel1.setBounds(460,20,400,100);
        clinetpanel1.setBackground(Color.decode("#013221"));
        frame.getContentPane().add(clinetpanel1);



        RoundedLabel serverportlabel = new RoundedLabel("Port No  ",30);
        serverportlabel.setBounds(40,180,100,30);
        serverportlabel.setForeground(Color.white);
        serverportlabel.setBackground(Color.decode("#660001"));
        serverportlabel.setBorder(BorderFactory.createLineBorder(Color.decode("#3D0C01")));
        frame.getContentPane().add(serverportlabel);


        //for server
        serverportfiled = new JTextField();
        serverportfiled.setBorder(BorderFactory.createLineBorder(Color.decode("#3D0C01")));
        serverportfiled.setBounds(150, 180, 200, 30);
        frame.getContentPane().add(serverportfiled);

        JLabel portlimit  = new JLabel("limit -> 0 - 65535");
        portlimit.setForeground(Color.white);
        portlimit.setBackground(Color.white);
        portlimit.setBounds(190,212,100,30);
        Font timesNewRomanFont = new Font("Times New Roman", Font.ITALIC, 12);
        portlimit.setFont(timesNewRomanFont);
        frame.getContentPane().add(portlimit);


        //for clinet
        RoundedLabel clientportlabel = new RoundedLabel("Port No  ",30);
        clientportlabel.setBounds(480, 180, 100, 30);
        clientportlabel.setForeground(Color.white);
        clientportlabel.setBackground(Color.decode("#00563E"));
        clientportlabel.setBorder(BorderFactory.createLineBorder(Color.decode("#013221")));
        frame.getContentPane().add(clientportlabel);


        porttextfield = new JTextField();
        porttextfield.setBorder(BorderFactory.createLineBorder(Color.decode("#013221")));
        porttextfield.setBounds(590, 180, 200, 30);
        frame.getContentPane().add( porttextfield );


        RoundedLabel clientIPlabel = new RoundedLabel("IP Address  ",30);
        clientIPlabel.setBounds(480, 240, 100, 30);
        clientIPlabel.setForeground(Color.white);
        clientIPlabel.setBackground(Color.decode("#00563E"));
        clientIPlabel.setBorder(BorderFactory.createLineBorder(Color.decode("#013221")));
        frame.getContentPane().add(clientIPlabel);

        iptextfield = new JTextField();
        iptextfield.setBorder(BorderFactory.createLineBorder(Color.decode("#013221")));
        iptextfield.setBounds(590, 240, 200, 30);
        frame.getContentPane().add(iptextfield);

        startclient = new RoundedButton("Connect",30);
        startclient.setBounds(590, 370, 150, 30);
        startclient.setForeground(Color.white);
        startclient.setBackground(Color.decode("#00563E"));
        startclient.setBorder(BorderFactory.createLineBorder(Color.decode("#013221")));
        startclient.addActionListener(this);
        frame.getContentPane().add(startclient);

        startserver = new RoundedButton("Host",30);
        startserver.setBounds(150, 370, 150, 30);
        startserver.setForeground(Color.white);
        startserver.setBackground(Color.decode("#660001"));
        startserver.setBorder(BorderFactory.createLineBorder(Color.decode("#3D0C01")));
        startserver.addActionListener(this);
        frame.getContentPane().add( startserver);

        RoundedPanel serverpanel2 = new RoundedPanel(30);
        serverpanel2.setBounds(20,140,400,300);
        serverpanel2.setBackground(Color.decode("#3D0C01"));
        frame.getContentPane().add(serverpanel2);

        RoundedPanel clinetpanel2 = new RoundedPanel(30);
        clinetpanel2.setBounds(460,140,400,300);
        clinetpanel2.setBackground(Color.decode("#013221"));
        frame.getContentPane().add(clinetpanel2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startserver) {
            if (serverportfiled.getText().isEmpty() || Integer.parseInt(serverportfiled.getText())<0 || Integer.parseInt(serverportfiled.getText())>65535){
                JOptionPane.showMessageDialog(frame, "Correctly Fill The Port No For Server", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Server server = new Server(serverportfiled.getText());
            }
        }
        if (e.getSource() == startclient) {
            if (iptextfield.getText().isEmpty() || porttextfield.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Fill all the details of the Host", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String ipaddress = iptextfield.getText();
                String portno = porttextfield.getText();
                Client client = new Client(ipaddress, Integer.parseInt(portno));
            }
        }
    }
}

