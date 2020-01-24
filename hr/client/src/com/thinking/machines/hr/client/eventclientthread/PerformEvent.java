package com.thinking.machines.hr.client.eventclientthread;
import com.thinking.machines.hr.client.events.*;
import com.thinking.machines.hr.client.events.EventInterface;
import com.thinking.machines.hr.client.eventclientthread.*;
import java.io.*;
import java.nio.*;
import java.net.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.imageio.stream.ImageOutputStream;
import java.util.*;
import java.util.concurrent.*;
public class PerformEvent extends Thread
{

Robot r;
double ratiox;
double ratioy;
private boolean exit=false;
Queue<EventInterface> events=new LinkedList<EventInterface>();
PerformEvent()
{

try{
r=new Robot();
start();
}catch(Exception e)
{
e.printStackTrace();
}
}
public void addEvent(EventInterface eh)
{
events.add(eh);
resume();
}


public void run()
{
EventInterface eh;
while(!exit)
{
if(events.isEmpty())
{
suspend();
}
eh=events.poll();
if(eh.getHardwareType()=='m')
{
mouseEventPerform((MouseEvent)eh);

}
if(eh.getHardwareType()=='k')
{
keyboardEventPerform((KeyboardEvent)eh);
}

}

}
public void terminate()
{
this.exit=true;
}
public void mouseEventPerform(MouseEvent eh)
{

ratiox=(double)1280/eh.getScreenWidth();
ratioy=(double)720/eh.getScreenHeight();
r.mouseMove((int)(eh.getX()*ratiox),(int)(eh.getY()*ratioy));

if(eh.getEventType()==1)
{
//r.mousePress(eh.getKey());
//r.mouseRelease(eh.getKey());
}
else
if(eh.getEventType()==2)
{
r.mousePress(eh.getKey());
}
else
if(eh.getEventType()==3)
{
r.mouseRelease(eh.getKey());
}
else
if(eh.getEventType()==4)
{
}
else
if(eh.getEventType()==5)
{
}
else
if(eh.getEventType()==6)
{
}
else
if(eh.getEventType()==7)
{

}



}
public void keyboardEventPerform(KeyboardEvent eh)
{

if(eh.getEventType()==2)
{

r.keyPress(eh.getKey()); 
}
else
if(eh.getEventType()==3)
{
r.keyRelease(eh.getKey()); 
}
}




}