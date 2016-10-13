package serverP4G;


/**
* serverP4G/GameServerOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from P4G.idl
* Thursday, March 10, 2016 4:04:26 PM ICT
*/

public interface GameServerOperations 
{
  String[] listAllGames ();

  // retourner la liste de tous les jeux du serveur
  String[] listOpenGames ();

  // retourner la liste des jeux ouverts
  serverP4G.Game getGame (String id);

  // chercher l'IOR du jeu id
  serverP4G.Game createGame (String player);
} // interface GameServerOperations