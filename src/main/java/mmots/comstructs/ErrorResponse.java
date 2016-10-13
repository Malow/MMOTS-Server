package mmots.comstructs;

public class ErrorResponse extends Response
{
  public String error;

  public ErrorResponse(String message, boolean result, String error)
  {
    super(message, result);
    this.error = error;
  }
}
