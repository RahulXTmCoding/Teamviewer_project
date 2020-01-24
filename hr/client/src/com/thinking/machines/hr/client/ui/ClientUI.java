package com.thinking.machines.hr.client.ui;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import com.thinking.machines.hr.client.events.*;
import com.thinking.machines.hr.client.imagetransferthread.*;
import com.thinking.machines.hr.client.eventclientthread.*;
import com.thinking.machines.hr.client.connector.*;
import com.thinking.machines.hr.client.filetransfer.*;
import com.thinking.machines.hr.client.interfaces.*;
import com.thinking.machines.hr.client.chatapplication.*;
public class ClientUI
{
private ClientFrame clientFrame;
public ClientUI()
{
initComponent();
setApperiance();


}
private void initComponent()
{
clientFrame=new ClientFrame();
}
private void setApperiance()
{


}
}
class ClientFrame extends JFrame
{
private MainPanel mainPanel;
private RemoteControlPanel rcPanel;
private FileTransferPanel ftPanel;
private ChatPanel chatPanel;
public ClientFrame()
{
initComponent();
setApperiance();
addListener();
}
private  void initComponent()
{
mainPanel=new MainPanel(this);
rcPanel=new RemoteControlPanel(this);
ftPanel=new FileTransferPanel(this);
chatPanel=new ChatPanel(this);
}
private void setApperiance()
{
setLayout(null);
setSize(300,350);
setLocation(50,50);
mainPanel.setBounds(0,0,300,350);
rcPanel.setBounds(0,0,300,350);
ftPanel.setBounds(0,0,300,350);
chatPanel.setBounds(0,0,300,350);
add(mainPanel);
add(rcPanel);
add(ftPanel);
add(chatPanel);
setVisible(true);
setDefaultCloseOperation(EXIT_ON_CLOSE);
}
public void addListener()
{

}
public void setMainPanel()
{
rcPanel.setVisible(false);
ftPanel.setVisible(false);
mainPanel.setVisible(true);

chatPanel.setVisible(false);
}
public void setRemoteControlPanel(Connector c1,Connector c2)
{

ftPanel.setVisible(false);
mainPanel.setVisible(false);
rcPanel.setVisible(true);
chatPanel.setVisible(false);
rcPanel.startClient(c1,c2);
}
public void setFileTransferPanel(Connector c1)
{

rcPanel.setVisible(false);
chatPanel.setVisible(false);
mainPanel.setVisible(false);
ftPanel.setVisible(true);
ftPanel.startClient(c1);
}
public void setChatPanel(String server)
{
rcPanel.setVisible(false);
chatPanel.setVisible(true);
mainPanel.setVisible(false);
ftPanel.setVisible(false);
chatPanel.startChatClient(server);
}


}

class MainPanel extends JPanel implements ActionListener
{
private JRadioButton remoteControl;
private JRadioButton fileTransfer;
private JRadioButton chatting;
private JLabel label;
private JLabel error;
private JLabel partnerId;
private JButton connect;
private ButtonGroup buttonGroup; 
private JTextField IPField;
private ClientFrame parentFrame;
private Connector c1;
private Connector c2;

public MainPanel(ClientFrame parentFrame)
{
this.parentFrame=parentFrame;
initComponent();
setApperiance();
addListener();
}
private  void initComponent()
{
remoteControl=new JRadioButton("Remote Control");
fileTransfer=new JRadioButton("Transfer File");
chatting=new JRadioButton("Chat");
connect=new JButton("Connect");
label=new JLabel();
buttonGroup=new ButtonGroup();
IPField=new JTextField();
partnerId=new JLabel();
error=new JLabel();
}
private void setApperiance()
{
buttonGroup.add(remoteControl);
buttonGroup.add(fileTransfer);
buttonGroup.add(chatting);
label.setText("Control Remote Computer");
partnerId.setText("partner Id");
setLayout(null);
setSize(250,450);
setLocation(30,50);
label.setBounds(30,10,200,40);
label.setFont(new Font("Serif", Font.PLAIN, 18));
partnerId.setBounds(30,60,80,20);
partnerId.setFont(new Font("Serif", Font.PLAIN, 14));
IPField.setBounds(30,90,200,40);
remoteControl.setBounds(30,140,150,40);
fileTransfer.setBounds(30,180,150,40);
chatting.setBounds(30,220,150,40);
connect.setBounds(30,270,150,40);
error.setBounds(150,70,80,20);
error.setFont(new Font("Serif", Font.PLAIN, 9));
error.setForeground(Color.RED);
add(label);
add(partnerId);
add(IPField);
add(remoteControl);
add(fileTransfer);
add(chatting);
add(connect);
add(error);
}
private void addListener()
{
connect.addActionListener(this);
}
public void actionPerformed(ActionEvent ae)
{
if(ae.getSource()==connect)
{
if(remoteControl.isSelected())
{

String server=IPField.getText().toString().trim();

if(server==null||server.equals(""))
{
error.setText("IP address Required");
return;
}
try{
c1=new Connector(server,5004);
c2=new Connector(server,5005);
 parentFrame.setRemoteControlPanel(c1,c2);
}catch(Exception e)
{
e.printStackTrace();
}

}
if(fileTransfer.isSelected())
{
//fileTransfer thread
String server=IPField.getText().toString().trim();

if(server==null||server.equals(""))
{
error.setText("IP address Required");
return;
}
try{
c1=new Connector(server,5006);
}catch(Exception e)
{
e.printStackTrace();
}
parentFrame.setFileTransferPanel(c1);
}
if(chatting.isSelected())
{
String server=IPField.getText().toString().trim();

if(server==null||server.equals(""))
{
error.setText("IP address Required");
return;
}


parentFrame.setChatPanel(server);
}
}
}



}
class RemoteControlPanel extends Panel implements ActionListener
{
private ClientFrame parentFrame;
private JButton back;
ScreenBuffer screenBuffer;
ScreenShot screenShot;
ImageTransfer imageTransfer;
private Connector c1;
private Connector c2;
EventClient eventClient;
RemoteControlPanel(ClientFrame parentFrame)
{
this.parentFrame=parentFrame;
initComponent();
setApperiance();
addListener();
}
private void initComponent()
{
back=new JButton("BACK");
}
private void setApperiance()
{
setLayout(null);
setVisible(false);
back.setBounds(100,20,80,40);
add(back);


}
private void addListener()
{
back.addActionListener(this);
}
public void actionPerformed(ActionEvent ae)
{
if(ae.getSource()==back)
{
closeConnection();
parentFrame.setMainPanel();
}

}
public void startClient(Connector c1,Connector c2)
{
this.c1=c1;
this.c2=c2;
try{
screenBuffer=new ScreenBuffer();
 screenShot=new ScreenShot(screenBuffer);
 imageTransfer=new ImageTransfer(c1,screenBuffer);

 eventClient=new EventClient(c2);
}catch(Exception e)
{
}
}
public void closeConnection()
{
try{
if(c1!=null)
{
c1.close();
}
if(c2!=null)
{
c2.close();
}
if(imageTransfer!=null)
{
imageTransfer.terminate();
}
if(screenShot!=null)
{
screenShot.terminate();
}
if(eventClient!=null)
{
eventClient.terminate();
}
}catch(Exception e)
{
e.printStackTrace();
}
}

}
class FileTransferPanel extends Panel implements ActionListener
{
private ClientFrame parentFrame;
private JButton back;
private Connector c1;
private FileTransfer f;
FileTransferPanel(ClientFrame parentFrame)
{
this.parentFrame=parentFrame;
initComponent();
setApperiance();
addListener();
}
private void initComponent()
{
f=new FileTransfer();
back=new JButton("BACK");
}
private void setApperiance()
{
setLayout(null);
setVisible(false);
back.setBounds(100,20,80,40);
add(back);


}
private void addListener()
{
back.addActionListener(this);
}
public void actionPerformed(ActionEvent ae)
{
if(ae.getSource()==back)
{
try{
c1.close();

}catch(Exception e)
{}
f.terminate();
parentFrame.setMainPanel();
}

}
public void startClient(Connector c1)
{
this.c1=c1;
 
f.startClient(c1.getSocket());
}
}
class ChatPanel extends JPanel implements ActionListener,ChatPanelInterface,KeyListener
{
private DefaultStyledDocument document;
private JTextPane innerPanel;
private JTextField message;
private JLabel label1;
private JLabel label3;
private JLabel label2;
private JButton send;
private JScrollPane scrollPane;
private ChatApp c;
private int messageLocation;
private ClientFrame parent;
private JButton back;
StyleContext context = new StyleContext();
        
        Style style = context.addStyle("test", null);
ChatPanel(ClientFrame parent)
{
this.parent=parent;

initComponent();
setApperiance();
addListener();
}

private void initComponent()
{
document=new DefaultStyledDocument();
back=new JButton("Back");
innerPanel=new JTextPane(document);
message=new JTextField();
label1=new JLabel("Chat Application");
label3=new JLabel("Offline");

label2=new JLabel("Type Your Message Here");
send= new JButton("Send");
scrollPane = new JScrollPane(innerPanel);

}
private void setApperiance()
{
this.setFocusable(true);
this.requestFocusInWindow();
innerPanel.setEditable(false);

setVisible(false);
innerPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        innerPanel.setLayout(new BoxLayout(innerPanel, 1));

scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

setLayout(null);
label3.setBounds(120,10,60,40);
label1.setBounds(10,10,100,40);
scrollPane.setBounds(10,60,260,160);
label2.setBounds(10,230,150,30);
message.setBounds(10,270,180,30);
send.setBounds(190,270,80,30);
back.setBounds(200,10,70,40);
add(back);
add(label1);
add(label3);
add(scrollPane);
add(label2);
add(message);
add(send);
}
private void addListener()
{
send.addActionListener(this);
back.addActionListener(this);
this.addKeyListener(this);
}
public void actionPerformed(ActionEvent ae)
{
if(ae.getSource()==send)
{
String receivedMessage=message.getText().toString().trim();
try{
document.insertString(document.getLength(),"You:-"+receivedMessage+"\n",style);
}catch(Exception e)
{}
c.sendMessage(receivedMessage);


}
if(ae.getSource()==back)
{
parent.setMainPanel();
c.terminate();
}
}
public void setMessage(String message)
{
try{
document.insertString(document.getLength(),"Friend:-"+message+"\n",style);
}catch(Exception e)
{
}
}
public void startChatClient(String server)
{
c=new ChatApp(this);
c.connectTo(server,5004);
c.start();
label3.setText("online");
}
public  void keyPressed(KeyEvent e)
{
if(e.getKeyCode()==13)
{
String receivedMessage=message.getText().toString().trim();
try{
document.insertString(document.getLength(),"You:-"+receivedMessage+"\n",style);
}catch(Exception ex)
{}
c.sendMessage(receivedMessage);
}
}  
public  void keyReleased(KeyEvent e)
{

}  
public  void keyTyped(KeyEvent e)  
{

}
public void setStatus(String status)
{
label3.setText(status);
}
}