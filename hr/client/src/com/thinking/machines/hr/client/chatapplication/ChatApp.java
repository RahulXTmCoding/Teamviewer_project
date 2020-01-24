package com.thinking.machines.hr.client.chatapplication;
import com.thinking.machines.hr.client.connector.*;
import com.thinking.machines.hr.client.interfaces.*;
import java.util.Scanner;
import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
public class ChatApp
{
Connector c1;
Receiver r;
Sender s;
ChatPanelInterface parent;
public ChatApp(ChatPanelInterface parent)
{
c1=null;
this.parent=parent;
}
public ChatApp(Connector c1)
{
this.c1=c1;
}
public void connectTo(String server,int port1)
{
try{
c1=new Connector(server,port1);

}catch(Exception e)
{
e.printStackTrace();
}
}
public void start()
{
r=new Receiver(c1,this);
s=new Sender(c1);

}
public void sendMessage(String message)
{
s.setMessage(message);

}
public void setMessage(String message)
{
parent.setMessage(message);
}
public void setStatus(String status)
{
parent.setStatus(status);
}

public void terminate()
{
r.terminate();
s.terminate();
try{
c1.close();
}catch(Exception e)
{}
}

}

class Receiver extends Thread
{
Socket socket;
private boolean exit=false;
ChatApp parent;
Receiver(Connector c,ChatApp parent)
{
this.parent=parent;
socket=c.getSocket();
start();

}
public void terminate()
{
this.exit=true;
}
public void run()
{
try{
DataInputStream dis=new DataInputStream(socket.getInputStream());
while(!exit)
{
String receivedMessage=dis.readUTF();
if(receivedMessage.equals("")||receivedMessage==null) continue;
parent.setMessage(receivedMessage);
}
}
catch(Exception e)
{
parent.setStatus("Offline");
}
}

}

class Sender extends Thread
{
Socket socket;
String message;
private boolean exit=false;
Sender(Connector c)
{
socket=c.getSocket();
start();

}
public void setMessage(String message)
{
this.message=message;
resume();
}
public void run()
{try
{
DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
while(!exit)
{
/*Scanner input=new Scanner(System.in);
String message=input.nextLine();
*/
if(message==null)
{
suspend();
}

dos.writeUTF(message);
message=null;
}
}
catch(Exception e)
{
terminate();
}
}
public void terminate()
{
this.exit=true;
}

}