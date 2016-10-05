package mmots.comstructs;

import com.github.malow.accountserver.comstructs.AuthorizedRequest;

public class AuthenticationRequest extends AuthorizedRequest
{
  public AuthenticationRequest(String email, String authToken)
  {
    super(email, authToken);
  }
}
