package com.thinking.machines.hr.client.chatserver;
import java.util.Scanner;
import java.net.*;
import java.io.*;
public class ChatApp
{
Socket s1;

public ChatApp()
{
s1=null;


}
public ChatApp(Socket c1)
{
this.s1=c1;

}
public void setServer(int port1)
{
try{
s1=new ServerSocket(port1).accept();

}catch(Exception e)
{
e.printStackTrace();
}
}
public void start()
{
new Receiver(s1);
new Sender(s1);

}


}

class Receiver extends Thread
{
Socket socket;
private boolean exit=false;
Receiver(Socket s)
{
try{
socket=s;
}catch(Exception e)
{}
start();

}
public void teminate()
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
System.out.println("friend-->"+receivedMessage);
}
}
catch(Exception e)
{
e.printStackTrace();
}
}

}

class Sender extends Thread
{
Socket socket;
private boolean exit=false;
Sender(Socket s)
{
try{
socket=s;
}catch(Exception e)
{}
start();

}
public void run()
{try
{
DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
while(!exit)
{
Scanner input=new Scanner(System.in);
String message=input.nextLine();
dos.writeUTF(message);

}
}
catch(Exception e)
{
e.printStackTrace();
}
}
public void teminate()
{
this.exit=true;
}

}
