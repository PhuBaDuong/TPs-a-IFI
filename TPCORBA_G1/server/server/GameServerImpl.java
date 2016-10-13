package server;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import serverP4G.Game;
import serverP4G.GameHelper;
import serverP4G.GameServerPOA;
import serverP4G.GameState;
import serverP4G.ReturnCode;
import setting.Parameters;

public class GameServerImpl extends GameServerPOA {
	private String listGames[];
	private int numberOfGame = 0;

	public GameServerImpl() {
		listGames = new String[Parameters.MAX_GAME_NUMBER];
		for (int i = 0; i < Parameters.MAX_GAME_NUMBER; i++) {
			listGames[i] = new String("");
		}
	}

	@Override
	public String[] listAllGames() {
		System.out.println("GameServerImpl : listGames method invoked");
		for (int index = 0; index < numberOfGame; index++) {
			if (listGames[index] != null && !listGames[index].equals("")) {
				if (getGame(listGames[index]).getGameData().state == GameState.TERMINATED) {
					for (int j = index; j < numberOfGame - 1; j++) {
						listGames[j] = listGames[j + 1];
					}
					listGames[numberOfGame - 1] = "";
					numberOfGame--;
				}
			}
		}
		return listGames;
	}

	@Override
	public String[] listOpenGames() {
		System.out.println("GameServerImpl : listOpenGames method start");
		String listOpenGames[] = new String[Parameters.MAX_GAME_NUMBER];
		for (int index = 0; index < Parameters.MAX_GAME_NUMBER; index++)
			listOpenGames[index] = new String("");
		int i = 0, j = 0;
		while (i < listGames.length && !listGames[i].equals("")) {
			if (getGame(listGames[i]).getGameData().state == GameState.CREATED
					|| getGame(listGames[i]).getGameData().state == GameState.ABANDONED) {
				listOpenGames[j++] = listGames[i];
			}
			i++;
		}
		return listOpenGames;
	}

	@Override
	public Game getGame(String id) {
		System.out.println("GameServerImpl : getGame method invoked with gameid = " + id);
		try {
			// initialize the ORB.
			Properties props = new Properties();
			props.put("org.omg.CORBA.ORBInitialHost", Parameters.IP_HOST);
			props.put("org.omg.CORBA.ORBInitialPort", Parameters.PORT);
			props.put("com.sun.CORBA.giop.ORBGIOPVersion", "1.0");
			ORB orb = ORB.init(listGames, props);
			// get the root naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			// Use NamingContextExt instead of NamingContext. This
			// is
			// part of the Interoperable naming Service.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// resolve the Object Reference in Naming
			Game game = GameHelper.narrow(ncRef.resolve_str(id));
			if (game == null) {
				System.out.println("Game server impl ERR : get gameId null");
			}
			// if this fails, a BAD_PARAM will be thrown
			return game;
		} catch (Exception ex) {
			System.out.println("Game server impl ERR : " + ex.getMessage());
		}
		return null;
	}

	@Override
	// synchronized conserve the number of game
	public synchronized Game createGame(String creator) { // creator = player1
		if (numberOfGame > Parameters.MAX_GAME_NUMBER) {
			System.out.println(
					"GameServerImpl Error : Server only permit to create " + Parameters.MAX_GAME_NUMBER + " games");
			return null;
		}
		GameImpl gameImpl = new GameImpl(creator);
		// set game id equal current system time
		gameImpl.setGameId(String.valueOf(System.currentTimeMillis()));
		gameImpl.setReturnCode(ReturnCode.G_OPEN);

		listGames[numberOfGame++] = gameImpl.getGameId();
		System.out.println("GameServerImpl : Games length is " + numberOfGame);

		try {
			Properties props = new Properties();
			props.put("org.omg.CORBA.ORBInitialHost", Parameters.IP_HOST);
			props.put("org.omg.CORBA.ORBInitialPort", Parameters.PORT);
			props.put("com.sun.CORBA.giop.ORBGIOPVersion", "1.0");

			ORB orb = ORB.init(listGames, props);
			POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			poa.the_POAManager().activate();
			org.omg.CORBA.Object o = poa.servant_to_reference(gameImpl);

			Game game = GameHelper.narrow(o);
			// get the root naming context
			// NameService invokes the name service
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			// Use NamingContextExt which is part of the Interoperable
			// Naming Service (INS) specification.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			String name = listGames[numberOfGame - 1];
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, game);
			System.out.println("GameServerImpl : " + creator + " created a game!");
			return game;
		} catch (

		Exception ex)

		{
			System.out.println("ERROR : " + ex.getMessage());
		}
		return null;
	}
}
