package mmots;

import java.util.ArrayList;
import java.util.List;

import com.github.malow.malowlib.MaloWProcess;
import com.github.malow.malowlib.NetworkChannel;
import com.github.malow.malowlib.NetworkPacket;
import com.github.malow.malowlib.ProcessEvent;
import com.google.gson.Gson;

import mmots.comstructs.AuthenticationRequest;
import mmots.comstructs.AuthenticationResponse;

public class Authenticator extends MaloWProcess
{
  private GameServer gameServer;
  private List<NetworkChannel> clients;

  public Authenticator(GameServer gameServer)
  {
    super();
    this.gameServer = gameServer;
    this.clients = new ArrayList<NetworkChannel>();
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
        AuthenticationRequest req = new Gson().fromJson(packet.getMessage(), AuthenticationRequest.class);
        if (req != null && req.isValid())
        {
          Client client = new Client(packet.getSender(), req.email, req.authToken);
          gameServer.addAuthorizedClient(client);
          this.clients.remove(packet.getSender());
          packet.getSender().sendData(new Gson().toJson(new AuthenticationResponse(true)));
        }
        else
        {
          packet.getSender().sendData(new Gson().toJson(new AuthenticationResponse(false)));
        }
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
    for (NetworkChannel client : this.clients)
    {
      client.waitUntillDone();
    }
  }

  public void ClientConnected(NetworkChannel nc)
  {
    System.out.println("New client added to Authenticator: " + nc.getChannelID());
    nc.setNotifier(this);
    nc.start();
    this.clients.add(nc);
  }

}
