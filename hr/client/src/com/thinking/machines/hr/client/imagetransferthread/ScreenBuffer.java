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

public class ScreenBuffer
{
Queue<Screen> screenBuffer=new LinkedList<>();
int max=25;

synchronized public void add(Screen screen) throws Exception
{
if(screenBuffer.size()>=max)
{
wait();
}
screenBuffer.add(screen);
notify();
}
synchronized public Screen poll() throws Exception
{
if(screenBuffer.size()<=0)
{
wait();
}

Screen screen=screenBuffer.poll();
notify();
return screen;

}
}