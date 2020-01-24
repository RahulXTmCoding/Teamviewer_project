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
public class MouseEvent implements EventInterface
{








char hardWareType;

private int x;

private int y;

private int eventType;


private int key;


private int screenWidth;
private int screenHeight;

public MouseEvent()
{}

public MouseEvent(byte mouseEvent[])
{
try
{
ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(mouseEvent);
DataInputStream dataInputStream=new DataInputStream(byteArrayInputStream);
this.setHardwareType(dataInputStream.readChar());
this.setX(dataInputStream.readInt());
this.setY(dataInputStream.readInt());
this.setEventType(dataInputStream.readInt());
this.setKey(dataInputStream.readInt());
this.setScreenHeight(dataInputStream.readInt());
this.setScreenWidth(dataInputStream.readInt());
}catch(IOException ioException)
{
ioException.printStackTrace();
}
}
public void setScreenHeight(int ssh)
{
screenHeight=ssh;
}
public int getScreenHeight()
{
return screenHeight;
}

public void setScreenWidth(int ssw)
{
screenWidth=ssw;
}
public int getScreenWidth()
{
return screenWidth;
}


public void setHardwareType(char hardWareType)
{

this.hardWareType=hardWareType;

}

public char getHardwareType(){

return this.hardWareType;

}

public void setX(int x){

this.x=x;

}

public int getX(){

return this.x;

}

public void setY(int y){

this.y=y;

}

public int getY(){

return this.y;

}

public void setEventType(int eventType)
{

this.eventType=eventType;

}


public int getEventType(){

return this.eventType;

}

public void setKey(int key){
this.key=key;
}
public int getKey(){
return this.key;
}
}