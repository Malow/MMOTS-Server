package mmots;

import java.util.ArrayList;
import java.util.List;

import com.github.malow.malowlib.MaloWProcess;
import com.github.malow.malowlib.NetworkPacket;
import com.github.malow.malowlib.ProcessEvent;

public class GameServer extends MaloWProcess
{
  private List<Client> clients;

  public GameServer()
  {
    this.clients = new ArrayList<Client>();
  }

  public void addAuthorizedClient(Client client)
  {
    client.networkChannel.setNotifier(this);
    this.clients.add(client);
    System.out.println("New client added to GameServer: " + client.email);
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
        System.out.println("Msg received from client " + packet.getSender().getChannelID() + ": " + packet.getMessage());
        packet.getSender().sendData("Response hihi");
      }
    }
  }

  @Override
  public void closeSpecific()
  {
    for (Client client : this.clients)
    {
      client.networkChannel.close();
    }
    for (Client client : this.clients)
    {
      client.networkChannel.waitUntillDone();
    }
  }
}
