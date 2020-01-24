import java.util.*;

class Server
{
private ServerSocket serverSocket;
private int port=6565;
Map<String,Socket> clientMap=new HashMap<>();
List<int> listOfPorts=new LinkedList<>();
Random r=new Random(1500);
public Server()
{

startListening();
}
public void startListening()
{
Socket socket;
while(true)
{
socket=serverSocket.accept();

String ts = String.valueOf(System.currentTimeMillis());
    String rand = UUID.randomUUID().toString();
    String id=DigestUtils.sha1Hex(ts + rand);
clientMap.put(id,socket);
new ClientManager(this,socket,id);
}
}


public void remoteControlStart(String id1,String id2)
{
Socket s=clientMap.get(id1);
Socket s1=clientMap.get(id2);

DataOutputStream dis=new DataOutputStream(s);
DataOutputStream dis1=new DataOutputStream(s1);
int port1=r.nextInt(20000);
while (listOfPorts.contains(port1)) port1 = r.nextInt(9999);
    listOfPorts.add(port1);
dis.writeInt(port1);

int port11=r.nextInt(20000);
while (listOfPorts.contains(port11)) port11 = r.nextInt(9999);
    listOfPorts.add(port11);
dis.writeInt(port11);


int port2=r.nextInt(20000);
while (listOfPorts.contains(port2)) port2 = r.nextInt(9999);
    listOfPorts.add(port2);
dis1.writeInt(port2);

int port21=r.nextInt(20000);
while (listOfPorts.contains(port21)) port21 = r.nextInt(9999);
    listOfPorts.add(port21);
dis1.writeInt(port21);


Socket c11=new ServerSocket(port1).accept();
Socket c12=new ServerSocket(port11).accept();

Socket c21=new ServerSocket(port2).accept();
Socket c22=new ServerSocket(port21).accept();

new RemoteControlThread(c11,c12,c21,c22);

}
public void fileTransferStart(String id1,String id2)
{
Socket s=clientMap.get(id1);
Socket s1=clientMap.get(id2);

DataOutputStream dis=new DataOutputStream(s);
DataOutputStream dis1=new DataOutputStream(s1);
int port1=r.nextInt(20000);
while (listOfPorts.contains(port1)) port1 = r.nextInt(9999);
    listOfPorts.add(port1);
dis.writeInt(port1);


int port2=r.nextInt(20000);
while (listOfPorts.contains(port2)) port2 = r.nextInt(9999);
    listOfPorts.add(port2);
dis1.writeInt(port2);


Socket c11=new ServerSocket(port1).accept();


Socket c21=new ServerSocket(port2).accept();


new FileTransferThread(c11,c21);

}
}
class RemoteControlThread extends Thread
{

Socket c11;
Socket c12;

Socket c21;
Socket c22;
public RemoteControlThread(Socket c11,Socket c12,Socket c21,Socket c22)
{
this.c11=c11;
this.c12=c12;
this.c21=c21;
this.c22=c22;
start();
}
public void run(){}

}

class FileTransferThread extends Thread
{

Socket c11;
Socket c12;


public FileTransferThread(Socket c11,Socket c12)
{
this.c11=c11;
this.c12=c12;

start();
}
public void run(){}

}

class ClientManager extends Thread
{
private Server parent;
private Socket socket;
private String id;
public isAlive=true;
public ClientManager(Server parent,Socket socket,String id)
{
this.parent=parent;
this.socket=socket;
this.id=id;
start();

}
public void run()
{
try
{
DataInputStream dis=new DataInputStream(socket.getInputStream());
DataOutputStream dos=new DataOutptSream(socket.getOutputStream());
dos.writeUTF(id);
dis.readByte();
// will listen to request from connected user
while(isAlive)
{
byte requestByte=dis.readByte();
String friendsId=dis.readUTF();
if(requestByte==1)
{
remoteControlStart(this.id,friendsId);
}
if(requestByte==2)
{
fileTransferStart(this.id,friendsId);
}
}
}catch(Exception e)
{
isAlive=false;
}
}


} 


