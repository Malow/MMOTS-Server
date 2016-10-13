package mmots.comstructs;

public class Response
{
  public boolean result;
  public String method;

  public Response(String method, boolean result)
  {
    this.result = result;
    this.method = method;
  }
}
