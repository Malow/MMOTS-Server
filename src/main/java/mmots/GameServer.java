package mmots;

import java.util.ArrayList;
import java.util.List;

import com.github.malow.malowlib.MaloWLogger;
import com.github.malow.malowlib.MaloWProcess;
import com.github.malow.malowlib.ProcessEvent;
import com.google.gson.Gson;

import mmots.comstructs.ErrorResponse;
import mmots.comstructs.Request;
import mmots.comstructs.Response;

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
    MaloWLogger.info("New client added to GameServer: " + client.getChannelID() + " - " + client.email);
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
        Request req = new Gson().fromJson(packet.message, Request.class);
        switch (req.method)
        {
          case "Ping":
            packet.client.sendData(new Gson().toJson(new Response(req.method, true)));
            break;
          default:
            MaloWLogger.info("Unexpected msg received from client " + packet.client.getChannelID() + ": " + packet.message);
            packet.client.sendData(new Gson().toJson(new ErrorResponse(req.method, false, "Unexpected method")));
        }

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

  public void dropAllClients()
  {
    for (Client client : this.clients)
    {
      client.close();
    }
    for (Client client : this.clients)
    {
      client.waitUntillDone();
    }
    this.clients = new ArrayList<Client>();
  }
}
