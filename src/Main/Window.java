
package Main;

import Client.Client;
import Server.Server;
import UI_elements.RoundedButton;
import UI_elements.RoundedPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window implements ActionListener {
    JFrame frame = new JFrame("File Transfer System");
    JButton startserver = new JButton("Start Server");
    JButton startclient = new JButton("Start Client");
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


        initializecomponents();
    }

    private void initializecomponents() {
        // Create the test panel for the server section with a blue color scheme
        RoundedPanel test = new RoundedPanel(30);
        test.setBounds(50, 130, 350, 200); // Adjust the size as needed
        test.setBackground(new Color(255, 0, 0)); // Red color
        test.setLayout(null); // Set layout to null to position components manually

        // Create the test1 panel for the client section with a green color scheme
        RoundedPanel test1 = new RoundedPanel(30);
        test1.setBounds(460, 130, 350, 200); // Adjust the size as needed
        test1.setBackground(new Color(102, 204, 102)); // Green color
        test1.setLayout(null); // Set layout to null to position components manually

        // Create and add "Server" rounded button to the server section (test panel)
        RoundedButton serverButton = new RoundedButton("Server", 20);
        serverButton.setBounds(10, 10, 100, 30);
        serverButton.setBackground(new Color(139, 0, 0)); // Dark red
        serverButton.setForeground(Color.white); // White text
        serverButton.setBorder(null);
        serverButton.setOpaque(false);
        test.add(serverButton);

        // Create and add "Client" rounded button to the client section (test1 panel)
        RoundedButton clientButton = new RoundedButton("Client", 20);
        clientButton.setBounds(10, 10, 100, 30);
        clientButton.setBackground(new Color(0, 102, 51)); // Dark green
        clientButton.setForeground(Color.white); // White text
        clientButton.setBorder(null);
        clientButton.setOpaque(false);
        test1.add(clientButton);

        // Create and add components for the server section (test panel)
        JLabel serverportlable = new JLabel("Port no :", SwingConstants.CENTER);
        serverportlable.setBounds(10, 50, 100, 30);
        serverportlable.setBorder(BorderFactory.createLineBorder(Color.black));
        serverportlable.setOpaque(true);
        serverportlable.setBackground(Color.WHITE);
        test.add(serverportlable);

        serverportfiled = new JTextField();
        serverportfiled.setBounds(120, 50, 200, 30);
        test.add(serverportfiled);


        startserver.setBounds(10, 90, 150, 30);
        test.add(startserver);
        startserver.addActionListener(this);
        startserver.setBackground(Color.GREEN);

        // Create and add components for the client section (test1 panel)
        JLabel iplabel = new JLabel("IP Address:", SwingConstants.CENTER);
        iplabel.setBounds(10, 50, 100, 30);
        iplabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        iplabel.setOpaque(true);
        iplabel.setBackground(Color.WHITE);
        test1.add(iplabel);

        iptextfield = new JTextField();
        iptextfield.setBounds(120, 50, 200, 30);
        test1.add(iptextfield);

        JLabel portlable = new JLabel("Port no :", SwingConstants.CENTER);
        portlable.setBounds(10, 90, 100, 30);
        portlable.setBorder(BorderFactory.createLineBorder(Color.black));
        portlable.setOpaque(true);
        portlable.setBackground(Color.WHITE);
        test1.add(portlable);

        porttextfield = new JTextField();
        porttextfield.setBounds(120, 90, 200, 30);
        test1.add(porttextfield);

        startclient.setBounds(10, 130, 150, 30);
        test1.add(startclient);
        startclient.addActionListener(this);
        startclient.setBackground(Color.RED);

        // Add the "Server" and "Client" panels to the frame
        frame.add(test);
        frame.add(test1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startserver) {
            if (serverportfiled.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Fill the port no for Server", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Server server = new Server(serverportfiled.getText());
            }
        }
        if (e.getSource() == startclient) {
            if (iptextfield.getText().isEmpty() || porttextfield.getText().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Fill all the details of the Server", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                String ipaddress = iptextfield.getText();
                String portno = porttextfield.getText();
                Client client = new Client(ipaddress, Integer.parseInt(portno));
            }
        }
    }
}

