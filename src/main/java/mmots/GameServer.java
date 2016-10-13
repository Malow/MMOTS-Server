package mmots;

import java.util.ArrayList;
import java.util.List;

import com.github.malow.malowlib.MaloWProcess;
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
    client.setNotifier(this);
    this.clients.add(client);
    System.out.println("New client added to GameServer: " + client.email);
  }

  @Override
  public void life()
  {
    while (this.stayAlive)
    {
      ProcessEvent ev = this.waitEvent();
      if (ev instanceof GameNetworkPacket)
      {
        GameNetworkPacket packet = (GameNetworkPacket) ev;
        System.out.println("Msg received from client " + packet.client.getChannelID() + ": " + packet.message);
        packet.client.sendData("Response hihi");
      }
    }
  }

  @Override
  public void closeSpecific()
  {
    for (Client client : this.clients)
    {
      client.close();
    }
    for (Client client : this.clients)
    {
      client.waitUntillDone();
    }
  }
}
