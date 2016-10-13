package server;

import java.util.Properties;

import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import serverP4G.GameServer;
import serverP4G.GameServerHelper;
import setting.Parameters;

public class Server {
	public static void main(String args[]) {
		try {
			// init ORB
			Properties props = new Properties();
			props.put("org.omg.CORBA.ORBInitialHost", Parameters.IP_HOST);
			props.put("org.omg.CORBA.ORBInitialPort", Parameters.PORT);
			props.put("com.sun.CORBA.giop.ORBGIOPVersion", "1.0");
			ORB orb = ORB.init(args, props);
			// init POA
			POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			poa.the_POAManager().activate();

			// create a GameServer object
			GameServerImpl gameServerImpl = new GameServerImpl();

			// create the object reference
			org.omg.CORBA.Object o = poa.servant_to_reference(gameServerImpl);
			GameServer gameServer = GameServerHelper.narrow(o);

			// get the root naming context
			// NameService invokes the name service
			org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
			// Use NamingContextExt which is part of the Interoperable
			// Naming Service (INS) specification.
			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			String name = "GameServer";
			NameComponent path[] = ncRef.to_name(name);
			ncRef.rebind(path, gameServer);

			while (true) {
				System.out.println("Server is ready!");
				orb.run();
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			System.out.println("Error : " + e.getStackTrace());
		}
	}
}
