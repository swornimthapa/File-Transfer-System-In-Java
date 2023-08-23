package FIle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class filePreview {

    public filePreview(String filename, byte[] filecontent , String fileExtension){
        JFrame frame = new JFrame("File Preview");
        frame.setSize(800,500);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));

        JLabel title = new JLabel("File Preview");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(20,0,10,0));

        JLabel promt = new JLabel("Are you sure you want to download"+filename);
        promt.setBorder(new EmptyBorder(20,0,10,0));
        promt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton jyes = new JButton("YES");
        jyes.setPreferredSize(new Dimension(150,75));

        JButton jno = new JButton("NO");
        jno.setPreferredSize(new Dimension(150,75));

        JLabel jfilecontent = new JLabel();
        jfilecontent.setPreferredSize(new Dimension(200,200));
        jfilecontent.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpbutton = new JPanel();
        jpbutton.setBorder(new EmptyBorder(20,0,10,0));
        jpbutton.add(jyes);
        jpbutton.add(jno);

        if(fileExtension.equalsIgnoreCase("txt")){
            jfilecontent.setText("<html>"+new String(filecontent)+"</html");
        }else if(fileExtension.equalsIgnoreCase("png")){
            jfilecontent.setIcon(new ImageIcon(filecontent));
        }else {
            JOptionPane.showMessageDialog(frame,"Cannot Show The Preview For this File","Error",JOptionPane.ERROR_MESSAGE);
        }

        jyes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File filetodownload = new File(filename);
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(filetodownload);
                    fileOutputStream.write(filecontent);
                    fileOutputStream.close();
                    frame.dispose();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        jno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        jPanel.add(title);
        jPanel.add(promt);
        jPanel.add(jfilecontent);
        jPanel.add(jpbutton);
        frame.add(jPanel);

        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

    }
}
