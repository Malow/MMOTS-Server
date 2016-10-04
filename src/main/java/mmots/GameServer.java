package mmots;

import java.util.ArrayList;
import java.util.List;

import com.github.malow.malowlib.NetworkChannel;
import com.github.malow.malowlib.NetworkPacket;
import com.github.malow.malowlib.MaloWProcess;
import com.github.malow.malowlib.ProcessEvent;

public class GameServer extends MaloWProcess
{
  private List<NetworkChannel> clients;

  public GameServer()
  {
    this.clients = new ArrayList<NetworkChannel>();
  }

  public void ClientConnected(NetworkChannel nc)
  {
    nc.setNotifier(this);
    nc.start();
    this.clients.add(nc);
  }

  @Override
  public void life()
  {
    while (this.stayAlive)
    {
      ProcessEvent ev = this.waitEvent();
      if (ev instanceof NetworkPacket)
      {
        NetworkPacket packet = (NetworkPacket) ev;
        System.out.println("Msg received from client " + packet.getSender().GetChannelID() + ": " + packet.getMessage());
      }
    }
  }

  @Override
  public void closeSpecific()
  {
    for (NetworkChannel client : this.clients)
    {
      client.close();
    }
  }
}
