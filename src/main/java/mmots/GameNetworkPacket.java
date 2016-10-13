package mmots;

import com.github.malow.malowlib.ProcessEvent;

public class GameNetworkPacket extends ProcessEvent
{
  public Client client;
  public String message;

  public GameNetworkPacket(Client client, String message)
  {
    this.client = client;
    this.message = message;
  }
}
