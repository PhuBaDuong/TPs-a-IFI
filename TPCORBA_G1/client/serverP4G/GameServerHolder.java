package serverP4G;

/**
* serverP4G/GameServerHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from P4G.idl
* Thursday, March 10, 2016 4:04:26 PM ICT
*/

public final class GameServerHolder implements org.omg.CORBA.portable.Streamable
{
  public serverP4G.GameServer value = null;

  public GameServerHolder ()
  {
  }

  public GameServerHolder (serverP4G.GameServer initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = serverP4G.GameServerHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    serverP4G.GameServerHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return serverP4G.GameServerHelper.type ();
  }

}
