package serverP4G;


/**
* serverP4G/GameServerPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from P4G.idl
* Thursday, March 10, 2016 4:04:26 PM ICT
*/

public abstract class GameServerPOA extends org.omg.PortableServer.Servant
 implements serverP4G.GameServerOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("listAllGames", new java.lang.Integer (0));
    _methods.put ("listOpenGames", new java.lang.Integer (1));
    _methods.put ("getGame", new java.lang.Integer (2));
    _methods.put ("createGame", new java.lang.Integer (3));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // serverP4G/GameServer/listAllGames
       {
         String $result[] = null;
         $result = this.listAllGames ();
         out = $rh.createReply();
         serverP4G.ListGamesHelper.write (out, $result);
         break;
       }


  // retourner la liste de tous les jeux du serveur
       case 1:  // serverP4G/GameServer/listOpenGames
       {
         String $result[] = null;
         $result = this.listOpenGames ();
         out = $rh.createReply();
         serverP4G.ListGamesHelper.write (out, $result);
         break;
       }


  // retourner la liste des jeux ouverts
       case 2:  // serverP4G/GameServer/getGame
       {
         String id = serverP4G.GameIdHelper.read (in);
         serverP4G.Game $result = null;
         $result = this.getGame (id);
         out = $rh.createReply();
         serverP4G.GameHelper.write (out, $result);
         break;
       }


  // chercher l'IOR du jeu id
       case 3:  // serverP4G/GameServer/createGame
       {
         String player = in.read_string ();
         serverP4G.Game $result = null;
         $result = this.createGame (player);
         out = $rh.createReply();
         serverP4G.GameHelper.write (out, $result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:serverP4G/GameServer:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public GameServer _this() 
  {
    return GameServerHelper.narrow(
    super._this_object());
  }

  public GameServer _this(org.omg.CORBA.ORB orb) 
  {
    return GameServerHelper.narrow(
    super._this_object(orb));
  }


} // class GameServerPOA
