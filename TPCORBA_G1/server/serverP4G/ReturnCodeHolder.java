package serverP4G;

/**
* serverP4G/ReturnCodeHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from P4G.idl
* Thursday, March 10, 2016 4:04:26 PM ICT
*/


// G_OVER - jeu termine, G_OPEN - jeu ouvert (manque d'un joueur), P_INV - joueur invalide
public final class ReturnCodeHolder implements org.omg.CORBA.portable.Streamable
{
  public serverP4G.ReturnCode value = null;

  public ReturnCodeHolder ()
  {
  }

  public ReturnCodeHolder (serverP4G.ReturnCode initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = serverP4G.ReturnCodeHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    serverP4G.ReturnCodeHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return serverP4G.ReturnCodeHelper.type ();
  }

}
