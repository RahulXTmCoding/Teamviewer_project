package com.thinking.machines.hr.client.events;
import com.thinking.machines.hr.client.events.EventInterface;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import java.util.*;
import java.util.concurrent.*;
public class KeyboardEvent implements EventInterface
{



char hardWareType;
private int key;
private int eventType;

public KeyboardEvent()
{

}

public KeyboardEvent(byte keyboardEvent[])
{
try{
ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(keyboardEvent);
DataInputStream dataInputStream=new DataInputStream(byteArrayInputStream);
this.setHardwareType(dataInputStream.readChar());

this.setEventType(dataInputStream.readInt());
this.setKey(dataInputStream.readInt());

}catch(IOException ioException)
{
ioException.printStackTrace();
}
}

public void setHardwareType(char hardWareType){
this.hardWareType=hardWareType;
}
public char getHardwareType(){
return this.hardWareType;
}
public void setKey(int key){
this.key=key;
}
public int getKey(){
return this.key;
}
public void setEventType(int eventType){
this.eventType=eventType;
}
public int getEventType(){
return this.eventType;
}

}