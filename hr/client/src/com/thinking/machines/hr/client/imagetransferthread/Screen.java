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
public class Screen
{
byte bufferedImage[];
int x;
int y;
public void setImage(byte bufferedImage[])
{
this.bufferedImage=bufferedImage;
}
public byte[] getImage()
{
return this.bufferedImage;
}
public void setX(int x)
{

this.x=x;
}
public int getX()
{
return this.x;
}
public void setY(int y)
{
this.y=y;
}
public int getY()
{
return this.y;
}

}
