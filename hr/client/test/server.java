import java.util.*;
import java.net.*;
import java.io.*;
class TeamViewer
{
Map<String,Socket> connectedUsers=new HashMap<>();
private ServerSocket serverSocket; 
TeamViewer() throws IOException
{
serverSocket=new ServerSocket(50505);
new Listener(serverSocket,connectedUsers);

}

}
class psp
{
public static void main(String gg[])throws IOException
{
TeamViewer t=new TeamViewer();

}
}
class Listener extends Thread
{
Map<String,Socket> connectedUsers=new HashMap<>();
private ServerSocket serverSocket; 
Listener(ServerSocket s,Map<String,Socket> map)
{
serverSocket=s;
connetedUsers=map;
start();
}
public void run()
{

Socket socket;
while(true)
{

socket=serverSocket.accept();
DataInputStream dis=new DataInputStream(socket.getInputStream());
String id=dis.readUTF();
connectedUsers.put(id,socket);
new UserThread(socket,connectedUsers);
System.out.println(id+" ----  "+socket.toString());
}

}

}
class UserThread extends Thread
{
Socket socket;
Map<String,Socket> connectedUsers;
UserThread(Socket socket,Map<String,Socket> connectedUsers)
{
this.connectedUsers=connectedUsers;
this.socket=socket;
start();
}
pubic void run()
{
DataInputStream dis=new DataInputStream(socket.getInputStream());
DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
byte request=dis.readByte();
dos.writeByte(1);
String id=dis.readUTF();


}

}

class TextTransferModule
{



}