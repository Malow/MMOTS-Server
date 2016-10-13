package mmots.comstructs;

public class Request
{
  public String method;

  public Request(String method)
  {
    this.method = method;
  }

  public boolean isValid()
  {
    if (this.method != null) return true;
    return false;
  }
}
