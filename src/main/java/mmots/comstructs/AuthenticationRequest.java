package mmots.comstructs;

public class AuthenticationRequest extends Request
{
  public String email;
  public String authToken;

  public AuthenticationRequest(String method, String email, String authToken)
  {
    super(method);
    this.email = email;
    this.authToken = authToken;
  }

  @Override
  public boolean isValid()
  {
    if (super.isValid() && this.email != null && this.authToken != null) return true;
    return false;
  }
}
