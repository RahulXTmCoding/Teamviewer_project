package com.thinking.machines.hr.client.connector;
import java.net.*;
public class Connector
{
Socket socket;
public Connector()
{}
public Connector(String server,int port)throws Exception
{
socket=new Socket(server,port);
}

public void connect(String server,int port)throws Exception
{
socket=new Socket(server,port);

}
public Socket getSocket()
{
return socket;
}
public void close()throws Exception
{
socket.close();
}
}