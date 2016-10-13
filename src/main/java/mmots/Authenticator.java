package mmots;

import java.util.ArrayList;
import java.util.List;

import com.github.malow.accountserver.AccountServer;
import com.github.malow.accountserver.database.AccountAccessor.WrongAuthentificationTokenException;
import com.github.malow.malowlib.MaloWProcess;
import com.github.malow.malowlib.NetworkChannel;
import com.github.malow.malowlib.ProcessEvent;
import com.google.gson.Gson;

import mmots.comstructs.AuthenticationRequest;
import mmots.comstructs.ErrorResponse;
import mmots.comstructs.Response;

public class Authenticator extends MaloWProcess
{
  public static final String REQUEST_NAME = "Authentication";
  private GameServer gameServer;
  private List<Client> clients;

  public Authenticator(GameServer gameServer)
  {
    super();
    this.gameServer = gameServer;
    this.clients = new ArrayList<Client>();
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
        AuthenticationRequest req = new Gson().fromJson(packet.message, AuthenticationRequest.class);
        if (req != null && req.isValid() && req.method.equals(REQUEST_NAME))
        {
          this.handleRequest(req, packet.client);
        }
        else
        {
          packet.client.sendData(new Gson().toJson(new ErrorResponse(req.method, false, "Unexpected method")));
        }
      }
    }
  }

  private void handleRequest(AuthenticationRequest req, Client client)
  {
    try
    {
      Long accId = AccountServer.checkAuthentication(req.email, req.authToken);
      client.accId = accId;
      client.email = req.email;
      client.authToken = req.authToken;
      gameServer.addAuthorizedClient(client);
      this.clients.remove(client);
      client.sendData(new Gson().toJson(new Response(req.method, true)));
    }
    catch (WrongAuthentificationTokenException e)
    {
      client.sendData(new Gson().toJson(new Response(req.method, false)));
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
    if (nc instanceof Client)
    {
      Client client = (Client) nc;
      System.out.println("New client added to Authenticator: " + client.getChannelID());
      client.setNotifier(this);
      client.start();
      this.clients.add(client);
    }
    else
    {
      throw new RuntimeException("Client connected that was not of type Client");
    }
  }

}
