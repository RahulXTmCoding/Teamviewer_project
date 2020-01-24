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
public class ScreenShot extends Thread
{

Robot robot;
Rectangle rect;
ScreenBuffer screenBuffer;
private boolean exit=false;

public ScreenShot(ScreenBuffer screenBuffer) throws Exception
{

robot=new Robot();
rect=new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
 this.screenBuffer=screenBuffer;
this.start();
}
public void terminate()
{

this.exit=true;
}
public void run()
{
Screen screen;

while(!exit)
{
try{
screen=new Screen();
float quality=0.05f;
BufferedImage bufferedImage=robot.createScreenCapture(rect);

ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
 
if (!writers.hasNext())
            throw new IllegalStateException("No writers found");
 
ImageWriter writer = (ImageWriter) writers.next();
ImageOutputStream ios = ImageIO.createImageOutputStream(byteArrayOutputStream);
ImageWriteParam param = writer.getDefaultWriteParam();
param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
param.setCompressionQuality(quality);
writer.setOutput(ios);
writer.write(null, new IIOImage(bufferedImage, null, null), param);
byte a[]=byteArrayOutputStream.toByteArray();

screen.setImage(a);
       Point b = MouseInfo.getPointerInfo().getLocation();

screen.setX((int) b.getX());
screen.setY((int) b.getY());

screenBuffer.add(screen);
ios.close();
        writer.dispose();
}catch(Exception e)
{
e.printStackTrace();
continue;
}
}
}

}