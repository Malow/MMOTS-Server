package mmots;

import java.net.Socket;

import com.github.malow.malowlib.NetworkChannel;
import com.github.malow.malowlib.ProcessEvent;

public class Client extends NetworkChannel
{
  public Long accId;
  public String email;
  public String authToken;

  public Client(Socket socket)
  {
    super(socket);
    this.accId = null;
    this.email = null;
    this.authToken = null;
  }

  public Client(Socket socket, Long accId, String email, String authToken)
  {
    super(socket);
    this.accId = accId;
    this.email = email;
    this.authToken = authToken;
  }

  @Override
  protected ProcessEvent createEvent(String msg)
  {
    return new GameNetworkPacket(this, msg);
  }
}
