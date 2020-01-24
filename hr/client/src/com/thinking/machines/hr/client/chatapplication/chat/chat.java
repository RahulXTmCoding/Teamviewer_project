import com.thinking.machines.hr.client.chatapplication.*;
class chat
{

public static void main(String gg[])
{
ChatApp c=new ChatApp();
c.connectTo("localhost",5004);
c.start();
}
}