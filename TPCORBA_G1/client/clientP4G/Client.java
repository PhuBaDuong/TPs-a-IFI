package clientP4G;

import java.util.Properties;

import org.omg.CORBA.*;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import serverP4G.GameServer;
import serverP4G.GameServerHelper;
import setting.Parameters;

public class Client {
	private ClientHome clientHome;
	static GameServer gameServer;

	public Client(GameServer gameServer) {
		clientHome = new ClientHome(gameServer);
	}

	public static void main(String args[]) {
		try {
			// initialize the ORB.
			Properties props = new Properties();
			props.put("org.omg.CORBA.ORBInitialHost", Parameters.IP_HOST);
			props.put("org.omg.CORBA.ORBInitialPort", Parameters.PORT);
			props.put("com.sun.CORBA.giop.ORBGIOPVersion", "1.0");
			ORB orb = ORB.init(args, props);

			// get the root naming context
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			// Use NamingContextExt instead of NamingContext. This is
			// part of the Interoperable naming Service.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

			// resolve the Object Reference in Naming
			String name = "GameServer";
			gameServer = GameServerHelper.narrow(ncRef.resolve_str(name));

			// if this fails, a BAD_PARAM will be thrown
			if (gameServer == null) {
				System.out.println("Stringified object is of wrong type");
				System.exit(-1);
			}
			System.out.println("Client : Obtained referrence GameServer object!");
			Client client = new Client(gameServer);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
