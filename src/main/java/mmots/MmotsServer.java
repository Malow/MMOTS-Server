package mmots;

import java.util.Scanner;

import com.github.malow.accountserver.AccountServer;
import com.github.malow.accountserver.AccountServerConfig;
import com.github.malow.malowlib.MaloWLogger;
import com.github.malow.malowlib.MaloWLogger.LogLevel;

public class MmotsServer
{
  public static void main(String[] args)
  {
    startup();

    handleInput();

    shutdown();
  }

  private static GameServer gameServer;
  private static Authenticator authenticator;
  private static RequestListener requestListener;

  private static void startup()
  {
    MaloWLogger.setLoggingThreshold(LogLevel.INFO);
    MaloWLogger.setLogToFile(true);
    MaloWLogger.setLogToSyso(true);
    MaloWLogger.setLogToSpecificFiles(false);
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

  private static void handleInput()
  {
    String input = "";
    Scanner in = new Scanner(System.in);
    while (!input.equals("shutdown"))
    {
      input = in.next();
      if (input.equals("DropAllGameClients")) dropAllGameClients();
    }
    in.close();
  }

  private static void dropAllGameClients()
  {
    gameServer.dropAllClients();
  }
}
