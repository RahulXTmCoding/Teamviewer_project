package com.thinking.machines.hr.client.imagetransferthread;
import java.io.*;
import java.nio.*;
import java.net.*; 
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import java.util.*;
import java.util.concurrent.*;
import com.thinking.machines.hr.client.connector.*;
public class ImageTransfer extends Thread
{

Socket socket;
ScreenBuffer screenBuffer;
private boolean exit=false;
public ImageTransfer()
{

}
public ImageTransfer(Connector c,ScreenBuffer screenBuffer)
{
this.screenBuffer=screenBuffer;
socket=c.getSocket();
this.start();
}
public void terminate()
{
this.exit=true;
}
public void run()
{
try
{
OutputStream outputStream=null;
int eventType=1;
Double contentSize;
ByteArrayOutputStream byteArrayOutputStream=null;
outputStream=socket.getOutputStream();
DataInputStream dis=new DataInputStream(socket.getInputStream());
     while(!exit)
     {
Screen screen=screenBuffer.poll();

ByteArrayOutputStream header=new ByteArrayOutputStream();
DataOutputStream dataOutputStream = new DataOutputStream(header);
dataOutputStream.writeByte(1);
int size=byteArrayOutputStream.size();
dataOutputStream.writeDouble(size);
 dataOutputStream.flush();
byte headerArray[]=new byte[9];
headerArray=header.toByteArray();
        outputStream.write(headerArray);


 


byte ack=dis.readByte();
if(ack==0)
{

continue;
}


outputStream.write(screen.getImage());

/*
byte ImageByteArray[]=screen.getImage();
int index=0;
while(index<size)
{
for(int i=0;i<1024&&index<size;i++)
{
outputStream.write(ImageByteArray[index++]);
}
 ack=dis.readByte();

if(ack==0)
{

continue;
}


}


*/

 ack=dis.readByte();

if(ack==0)
{

continue;
}
 dataOutputStream=new DataOutputStream(outputStream);
dataOutputStream.writeInt(screen.getX());
dataOutputStream.writeInt(screen.getY());


     


}

  }

 catch(Throwable t)
 {
 t.printStackTrace();
 }
}




}