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
  private static RequestListener requestListener;

  private static void startup()
  {
    AccountServerConfig config = new AccountServerConfig();
    config.databaseName = "AccountServer";
    config.databaseUser = "AccServUsr";
    config.databasePassword = "password";
    config.httpsApiCertPassword = "password";
    config.httpsApiPort = 7000;

    config.gmailUsername = "gladiatormanager.noreply";
    config.gmailPassword = "passwordFU";

    AccountServer.start(config);

    gameServer = new GameServer();
    requestListener = new RequestListener(7001, gameServer);
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
