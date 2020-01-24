import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.*;



class UI extends Thread{
 private Dimension dimension;
 private JFrame jframe;
 private ServerJTree serverJTree;
 JTree serverSideJTree;
 public UI(){
  serverJTree=new ServerJTree();
  dimension=Toolkit.getDefaultToolkit().getScreenSize();
  jframe=new JFrame("File Transfer");
  jframe.setBackground(Color.black);
  jframe.setSize((int)dimension.getWidth()/2,(int)dimension.getHeight()/2);
  jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  start();
 }
 
 public void run(){
  
   while(!serverJTree.isReady()){
    System.out.println(serverJTree.isReady());
    try{Thread.sleep(2000);}catch(Exception exception){}
   }
   System.out.println(serverJTree.isReady());
    serverSideJTree=serverJTree.getJTree();
      showTree();
 }   


public void showTree()
{
    jframe.setLayout(null);
    JScrollPane serverSideJTreeScrollPane=new JScrollPane(serverSideJTree,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
 serverSideJTreeScrollPane.setBounds(0,0,(int)dimension.getWidth()/2-20,(int)dimension.getHeight()/2-20);
jframe.add(serverSideJTreeScrollPane);   
jframe.setVisible(true);

}

 public static void main(String[] gg){
  UI ui=new UI();
 }
}

    

class ServerJTree extends Thread{
 private JTree fileTree;
 private DefaultMutableTreeNode rootNode;
 public boolean isServerJTreeReady=false;
 public ServerJTree(){
  start();
 }
 public void run(){
  populateModel();
 }
 public boolean isReady(){
  return this.isServerJTreeReady;
 }
 private void populateModel(){
  rootNode=new DefaultMutableTreeNode("Root");
  File[] fileDir=File.listRoots();
  FileThread fileThreads[]=new FileThread[1];
  int y=0;
  for(File file:fileDir){

   File[] subFiles=file.listFiles();
   DefaultMutableTreeNode node=new DefaultMutableTreeNode(file);
   rootNode.add(node);
   fileThreads[y++]=new FileThread(node,subFiles);
break; 
 } 
  boolean fileThreadAlive;
  while(true){
   fileThreadAlive=false;
   for(FileThread fileThread:fileThreads){
    if(fileThread.isAlive()) fileThreadAlive=true;
   }
   if(!fileThreadAlive) break; 
  }
  fileTree=new JTree(rootNode);
  this.isServerJTreeReady=true;
  System.out.println("Server JTree Ready");
 }
 public JTree getJTree(){
  if(isServerJTreeReady) return this.fileTree;
  return null;
 }
}

class FileThread extends Thread{
 private DefaultMutableTreeNode root;
 private File[] childrenFiles;

 public FileThread(DefaultMutableTreeNode root, File[] childrenFiles){
  this.root=root;
  this.childrenFiles=childrenFiles;
  start();
 }
 public void run(){
  recursiveAdd(root,childrenFiles);
 }
 public void recursiveAdd(DefaultMutableTreeNode root, File[] childrenFiles){
  for(File file:childrenFiles){
   if(file.isFile()) root.add(new DefaultMutableTreeNode(file.getName()));
   else if(file.isDirectory()){
    DefaultMutableTreeNode node=new DefaultMutableTreeNode(file.getName());
    root.add(node);
    File[] subChildren=file.listFiles();
    if(subChildren==null) continue;
    recursiveAdd(node,subChildren);
   }
  }
 }
}