package serverP4G;


/**
* serverP4G/Players.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from P4G.idl
* Thursday, March 10, 2016 4:04:26 PM ICT
*/

public final class Players implements org.omg.CORBA.portable.IDLEntity
{
  public String player1 = null;

  // nickname du joueur 1
  public String player2 = null;

  public Players ()
  {
  } // ctor

  public Players (String _player1, String _player2)
  {
    player1 = _player1;
    player2 = _player2;
  } // ctor

} // class Players
