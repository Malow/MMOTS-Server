package mmots;

import com.github.malow.malowlib.NetworkChannel;
import com.github.malow.malowlib.NetworkServer;

public class RequestListener extends NetworkServer
{
  private GameServer gameServer;

  public RequestListener(int port, GameServer gameServer)
  {
    super(port);
    this.gameServer = gameServer;
  }

  @Override
  public void clientConnected(NetworkChannel nc)
  {
    this.gameServer.ClientConnected(nc);
  }

}
