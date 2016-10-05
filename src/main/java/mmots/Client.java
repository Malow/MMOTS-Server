package mmots;

import com.github.malow.malowlib.NetworkChannel;

public class Client
{
  public NetworkChannel networkChannel;
  public String email;
  public String authToken;

  public Client(NetworkChannel networkChannel, String email, String authToken)
  {
    this.networkChannel = networkChannel;
    this.email = email;
    this.authToken = authToken;
  }
}
