package mmots;

import com.github.malow.malowlib.NetworkChannel;
import com.github.malow.malowlib.NetworkServer;

public class RequestListener extends NetworkServer
{
  private Authenticator authenticator;

  public RequestListener(int port, Authenticator authenticator)
  {
    super(port);
    this.authenticator = authenticator;
  }

  @Override
  public void clientConnected(NetworkChannel nc)
  {
    this.authenticator.ClientConnected(nc);
  }

}
