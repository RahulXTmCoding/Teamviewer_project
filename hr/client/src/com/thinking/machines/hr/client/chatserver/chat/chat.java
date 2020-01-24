import com.thinking.machines.hr.client.chatserver.*;
class chat
{

public static void main(String gg[])
{
ChatApp c=new ChatApp();
c.setServer(5004);
c.start();
}
}