
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client  extends JFrame {
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    private JLabel heading= new JLabel("Client Area");
    private JTextArea messageArea= new JTextArea();
    private JTextField messageInput= new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);


    public Client(){
        try {
            System.out.println(" Sending request to Client");
        socket = new Socket("127.0.0.1",7777);
        System.out.println("Connection done");
        
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

          CreateGUI();
          hangleEvents();
            StartReading();
            StartWritiing();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    public void hangleEvents(){
        messageInput.addKeyListener( new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==10){
                    String ContentToSend=messageInput.getText();
                    messageArea.append("Me :"+ContentToSend);
                    out.println(ContentToSend);
                    out.flush();
                    messageInput.setText(" ");
                    messageInput.requestFocus();
            }

    }});
    }

        public void CreateGUI(){
            this.setTitle("Client Messanger[END]");
            this.setSize(500, 500);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setVisible(true);

            //coding for the components
            heading.setFont(font);
            messageArea.setFont(font);
            messageInput.setFont(font);
            

            // heading.setIcon(new ImageIcon("download.png"));
            String imagePath = "download.png"; // Change this to your image path
            ImageIcon icon = new ImageIcon("C:\\Users\\dilpreet\\Desktop\\R\\chatapp\\download.png");
            if (icon.getIconWidth() == -1) {
                System.out.println("Image not found or could not be loaded: " + imagePath);
            } else {
                // heading.setIcon(icon);
                Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            heading.setIcon(new ImageIcon(scaledImage));
            }
            messageArea.setEditable(false);
            heading.setHorizontalTextPosition(SwingConstants.CENTER);

            heading.setVerticalTextPosition(SwingConstants.BOTTOM);
            heading.setHorizontalAlignment(SwingConstants.CENTER);

            heading.setBorder(BorderFactory.createEmptyBorder(20,20 ,20, 20));

            //frame ka layout set karna hai 
            this.setLayout(new BorderLayout());

            //adding components to frame
             this.add(heading,BorderLayout.NORTH);
             JScrollPane jScrollPane=new JScrollPane(messageArea);
             this.add(jScrollPane,BorderLayout.CENTER);
             this.add(messageInput,BorderLayout.SOUTH);


        }




        public void StartReading(){
            Runnable r1=()->{
                try {
                while(true){
                    System.out.println("Reader Started");

                    String msg= br.readLine();
                    if(msg.equals("terminate")){
                        System.out.println(" program is terminated");
                       JOptionPane.showMessageDialog(this, "server terminated the chat");
                       messageInput.setEnabled(false);
                       socket.close();
                        break;
                    }
                    // System.out.println("Server :"+msg);
                    messageArea.append("\nServer :"+msg);
                    messageArea.append("\n");
                }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                
            };
            new Thread(r1).start();
    
        }
        public void StartWritiing(){
            Runnable r2=()->{
                try {
                    while(true && !socket.isClosed()){
                        BufferedReader br1= new BufferedReader(new InputStreamReader(System.in));
                        String content=br1.readLine();
                        out.println(content);
                        out.flush();
                        if(content.equals("terminate")){
                            socket.close();
                            break;
                        }
        
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
              
            };
            new Thread(r2).start();

    }
    public static void main(String[] args) {
        System.out.println("this is the client ");
        new Client();
    }
    
}