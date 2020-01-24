package com.thinking.machines.hr.client.filetransfer;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.zip.*;
public class FileTransfer
{
private JFrame frame; 
private JTree tree;
private boolean exit=false;
private CreateTree createTree;
public FileTransfer()
{

createTree=new CreateTree();



}
/*
private void populateTree()
{
File rootdir[]=File.listRoots();
DefaultMutableTreeNode rootNode=new DefaultMutableTreeNode("Root");


DefaultMutableTreeNode cNode=new DefaultMutableTreeNode("c");
DefaultMutableTreeNode eNode=new DefaultMutableTreeNode("e");
DefaultMutableTreeNode fNode=new DefaultMutableTreeNode("f");
rootNode.add(cNode);
rootNode.add(eNode);
rootNode.add(fNode);

for(File file:rootdir)
{
System.out.println(file);
DefaultMutableTreeNode Node=new DefaultMutableTreeNode(file);
rootNode.add(Node);

File arr[]=file.listFiles();

new RecursiveAdd(Node,arr);

}


if(f1.exists()&&f1.isDirectory())
{
File arr[]=f1.listFiles();
System.out.println(cNode.toString()+"this is c dir "+arr.toString());
recursiveAdd(cNode,arr);

}
if(f2.exists()&&f2.isDirectory())
{
File arr[]=f2.listFiles();
recursiveAdd(eNode,arr);

}
if(f3.exists()&&f3.isDirectory())
{
File arr[]=f3.listFiles();
recursiveAdd(fNode,arr);

}



while(Thread.activeCount()>1)
{
try{
Thread.sleep(1000);
}catch(Exception e)
{}
}
tree=new JTree(rootNode);

}
*/
private void showTree()
{
frame=new JFrame();
frame.setVisible(true);
frame.setSize(500,700);
frame.add(tree);
}
private FileTransferClient ftc;
public void startClient(Socket socket)
{


ftc=new FileTransferClient(socket,createTree);
}

public void terminate()
{
ftc.terminate();
}


}
class RecursiveAdd extends Thread
{
DefaultMutableTreeNode parent;
File childs[];
RecursiveAdd(DefaultMutableTreeNode parent,File childs[])
{
this.parent=parent;
this.childs=childs;
start();
}
public void run()
{
recursiveAdd(parent,childs);

}
private void recursiveAdd(DefaultMutableTreeNode parent,File[] childs)
{
for(File file:childs)
{
if(file.isFile())
{
DefaultMutableTreeNode node=new DefaultMutableTreeNode(file.getName());
parent.add(node);
}
else
if(file.isDirectory())
{

DefaultMutableTreeNode node=new DefaultMutableTreeNode(file.getName());
parent.add(node);
File[] a=file.listFiles();
if(a==null)
{
continue;
}

recursiveAdd(node,a);
}


}

}

}

class CreateTree extends Thread
{
private JTree tree;
private boolean ready=false;
CreateTree()
{
start();

}
public void run()
{
populateTree();
ready=true;
System.out.println("Tree ready");
}
private void populateTree()
{
File rootdir[]=File.listRoots();
DefaultMutableTreeNode rootNode=new DefaultMutableTreeNode("Root");

RecursiveAdd addArray[]=new RecursiveAdd[rootdir.length];
int y=0;
for(File file:rootdir)
{

DefaultMutableTreeNode Node=new DefaultMutableTreeNode(file);
rootNode.add(Node);

File arr[]=file.listFiles();

addArray[y++]=new RecursiveAdd(Node,arr);

}

boolean isAlive;
while(true)
{
isAlive=false;
for(RecursiveAdd recur:addArray)
{
if(recur.isAlive())
{
isAlive=true;
}
}
if(isAlive==false)
{
break;
}
}
tree=new JTree(rootNode);
}
public JTree getTree()
{
return tree;

}
public boolean isTreeReady()
{
return ready;
}
}
class FileTransferClient extends Thread
{
Socket socket;
CreateTree createTree;
JTree tree;
private boolean exit=false;
FileTransferClient(Socket socket,CreateTree createTree)
{
this.socket=socket;
this.createTree=createTree;
start();
}
public void run()
{

while(!createTree.isTreeReady());
tree=createTree.getTree();
try{
ByteArrayOutputStream treeModel=new ByteArrayOutputStream();
ByteArrayOutputStream dirHeader=new ByteArrayOutputStream();
ObjectOutputStream oos=new ObjectOutputStream(treeModel);
oos.writeObject(tree);
DataOutputStream dosd=new DataOutputStream(socket.getOutputStream());
DataOutputStream dos=new DataOutputStream(dirHeader);
dos.writeByte(4);
dos.writeDouble(treeModel.size());
OutputStream os=socket.getOutputStream();
os.write(dirHeader.toByteArray());
DataInputStream dis=new DataInputStream(socket.getInputStream());
int ack=dis.readByte();

if(ack!=1)
{
socket.close();
return;
}
int bytesRead;
os.write(treeModel.toByteArray());
InputStream is=socket.getInputStream();

while(!exit)
{
byte headerReceived[]=new byte[9];

is.read(headerReceived,0,9);
ByteArrayInputStream header=new ByteArrayInputStream(headerReceived);
DataInputStream dish=new DataInputStream(header);
byte requestByte=dish.readByte();
double contentSize=dish.readDouble();
//requestByte=5 means client will send file
//requestByte=6 means server will send file
dosd.writeByte(1);
if(requestByte==5)
{
System.out.println("file Request arive");
byte content[]=new byte[(int)contentSize];
dis.read(content,0,(int)contentSize);
String path=new String(content);
dos.writeByte(1);
System.out.println("path received");
File myFile = new File(path);


File zipFile=new File("dir_file.zip");
if (zipFile.exists() && zipFile.isFile())
  {
  zipFile.delete();
  }
zipFile.createNewFile();
FileOutputStream fos=new FileOutputStream(zipFile);
ZipOutputStream zipOutputStream=new ZipOutputStream(fos);
toZip(myFile,myFile.getName(),zipOutputStream);







 byte[] mybytearray = new byte[(int) zipFile.length()];
         
        FileInputStream fis = new FileInputStream(zipFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
       
         
        DataInputStream disf = new DataInputStream(bis);   
        disf.readFully(mybytearray, 0, mybytearray.length);
        dos.writeDouble(mybytearray.length); 
       System.out.println("length send"); 
         
        os.write(mybytearray, 0, mybytearray.length);
        
System.out.println("file send");
}
if(requestByte==6)
{
byte content[]=new byte[(int)contentSize];
dis.read(content,0,(int)contentSize);
String path=new String(content);

dos.writeByte(1);

double size=dis.readDouble();
//String fileName=dis.readUTF();
File myFile = new File(path);
File zipReceived=new File("dir_file.zip");
if (zipReceived.exists() && zipReceived.isFile())
  {
  zipReceived.delete();
  }
zipReceived.createNewFile();
OutputStream fileOutputStream=new FileOutputStream(zipReceived);
byte[] buffer = new byte[1024];   
while (size > 0 && (bytesRead = dis.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1)   
{   
            fileOutputStream.write(buffer, 0, bytesRead);   
            size -= bytesRead;   
}

ZipInputStream zis=new ZipInputStream(new FileInputStream(zipReceived));
ZipEntry entry=zis.getNextEntry();
while(entry!=null)
{
File fileTemp=newFile(myFile,entry);
FileOutputStream fos=new FileOutputStream(fileTemp);

int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            entry = zis.getNextEntry();
}
zis.closeEntry();
        zis.close();
System.out.println("File Received");

}

}

}catch(Exception e)
{

}




}
public File newFile(File destDir,ZipEntry zip) throws IOException
{
File destFile = new File(destDir, zip.getName());
String destDirPath = destDir.getCanonicalPath();
String destFilePath = destFile.getCanonicalPath();
if (!destFilePath.startsWith(destDirPath + File.separator)) {
throw new IOException("Entry is outside of the target dir: " + zip.getName());
}
return destFile;

}

public void toZip(File myFile,String fileName,ZipOutputStream zipOutputStream)throws IOException
{

if(myFile.isHidden())
{
return;
}


if(myFile.isDirectory())
{
if(myFile.getName().endsWith("/"))
{
zipOutputStream.putNextEntry(new ZipEntry(fileName));
zipOutputStream.closeEntry();
}
else
{
zipOutputStream.putNextEntry(new ZipEntry(fileName+"/"));
zipOutputStream.closeEntry();
}
File array[]=myFile.listFiles();
for(File file:array)
{
toZip(file,fileName+"/"+file.getName(),zipOutputStream);

}

return;
}
FileInputStream fis=new FileInputStream(myFile);
zipOutputStream.putNextEntry(new ZipEntry(fileName));
byte bytes[]=new byte[1024];
int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOutputStream.write(bytes, 0, length);
        }
        fis.close();
}
public void terminate()
{
this.exit=true;
}
}