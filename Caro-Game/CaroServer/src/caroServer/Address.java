package caroServer;

import java.util.Vector;

public class Address {

	public static Vector<Integer> port1 = new Vector<Integer>(); // For server game
	public static Vector<Integer> port2 = new Vector<Integer>(); // For server chat
	public static Vector<String> ip = new Vector<String>();
	public static Vector<String> name = new Vector<String>();
	
	public Address(Integer port1, Integer port2, String ip) {
		this.port1.add(port1);
		this.port2.add(port2);
		this.ip.add(ip);
	}
}
