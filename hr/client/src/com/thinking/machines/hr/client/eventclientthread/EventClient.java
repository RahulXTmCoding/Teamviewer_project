package com.thinking.machines.hr.client.eventclientthread;
import com.thinking.machines.hr.client.events.*;
import com.thinking.machines.hr.client.eventclientthread.*;
import com.thinking.machines.hr.client.connector.*;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import java.util.*;
import java.util.concurrent.*;
public class EventClient extends Thread
{
Socket socket;
private boolean exit=false;
public EventClient(Connector c)
{
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

InputStream is;
DataInputStream isr;
ObjectInputStream osr;
PerformEvent pe=new PerformEvent();
isr=new DataInputStream(socket.getInputStream());
DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());

while(!exit)
{
byte contentType=isr.readByte();
Double contentSize=isr.readDouble();
if(!(contentType==2||contentType==3))
{
dataOutputStream.writeByte(0); 
continue;
}
dataOutputStream.writeByte(1);
ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
DataOutputStream dataOutput=new DataOutputStream(byteArrayOutputStream); 
if(contentType==2)
{
byte mouseEventArray[];
for(int i=0;i<contentSize;i++) dataOutput.writeByte(isr.readByte());
mouseEventArray=byteArrayOutputStream.toByteArray();
MouseEvent mouseEvent=new MouseEvent(mouseEventArray);
pe.addEvent(mouseEvent);
}
else
{
byte keyboardEventArray[];
for(int i=0;i<contentSize;i++) dataOutput.writeByte(isr.readByte());
keyboardEventArray=byteArrayOutputStream.toByteArray();
KeyboardEvent keyboardEvent=new KeyboardEvent(keyboardEventArray);
pe.addEvent(keyboardEvent);
}
}
pe.terminate();
}
catch(Exception e)
{
e.printStackTrace();
}
}
}