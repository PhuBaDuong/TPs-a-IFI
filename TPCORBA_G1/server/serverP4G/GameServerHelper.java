package serverP4G;


/**
* serverP4G/GameServerHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from P4G.idl
* Thursday, March 10, 2016 4:04:26 PM ICT
*/

abstract public class GameServerHelper
{
  private static String  _id = "IDL:serverP4G/GameServer:1.0";

  public static void insert (org.omg.CORBA.Any a, serverP4G.GameServer that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static serverP4G.GameServer extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (serverP4G.GameServerHelper.id (), "GameServer");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static serverP4G.GameServer read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_GameServerStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, serverP4G.GameServer value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static serverP4G.GameServer narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof serverP4G.GameServer)
      return (serverP4G.GameServer)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      serverP4G._GameServerStub stub = new serverP4G._GameServerStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static serverP4G.GameServer unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof serverP4G.GameServer)
      return (serverP4G.GameServer)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      serverP4G._GameServerStub stub = new serverP4G._GameServerStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}