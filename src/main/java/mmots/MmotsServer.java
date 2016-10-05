package mmots;

import java.util.Scanner;

import com.github.malow.accountserver.AccountServer;
import com.github.malow.accountserver.AccountServerConfig;

public class MmotsServer
{
  public static void main(String[] args)
  {
    startup();

    goToInput();

    shutdown();
  }

  private static GameServer gameServer;
  private static Authenticator authenticator;
  private static RequestListener requestListener;

  private static void startup()
  {
    startupHttpsServer(7000);
    startupSocketServer(7001);
  }

  private static void startupHttpsServer(int port)
  {
    AccountServerConfig config = new AccountServerConfig();
    config.databaseName = "AccountServer";
    config.databaseUser = "AccServUsr";
    config.databasePassword = "password";
    config.httpsApiCertPassword = "password";
    config.httpsApiPort = port;

    config.gmailUsername = "gladiatormanager.noreply";
    config.gmailPassword = "passwordFU";

    AccountServer.start(config);
  }

  public static void startupSocketServer(int port)
  {
    gameServer = new GameServer();
    gameServer.start();
    authenticator = new Authenticator(gameServer);
    authenticator.start();
    requestListener = new RequestListener(port, authenticator);
    requestListener.start();
  }

  private static void shutdown()
  {
    AccountServer.close();
    requestListener.close();
    gameServer.close();
  }

  private static void goToInput()
  {
    String input = "";
    Scanner in = new Scanner(System.in);
    while (!input.equals("shutdown"))
    {
      System.out.print("> ");
      input = in.next();
    }
    in.close();
  }
}
