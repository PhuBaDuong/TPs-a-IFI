#include <iostream>
#include <limits>
#include <cstring>
#include <string>
//#include <stdio.h>
#include <cstdlib>
#include <cassert>
#include "P4G.hh"
#include "Setting.hpp"



using namespace std;
using namespace serverP4G;

static CORBA::Object_ptr getObjectReference(CORBA::ORB_ptr orb);
void afficher(serverP4G::Game_ptr game);
void listOpenedGames(serverP4G::GameServer_var gameserver);
void listAllGames(serverP4G::GameServer_var gameserver);
void jouer(serverP4G::Game_ptr game, const char * player);
bool saisie(int &n, int lim);
//////////////////////////////////////////////////////////////////////

int main (int argc, char **argv) {
	
	GameServer_var gameserver = 0;
	char *pseudo = new char[30];
	Game_ptr game;
	
  try {
	  
    CORBA::ORB_var orb = CORBA::ORB_init(argc, argv);

    CORBA::Object_var obj = getObjectReference(orb);


	if (!CORBA::is_nil(obj)) {
		
		cout<<"obj != null"<<endl;
		
		gameserver = GameServer::_narrow(obj);

		if (!CORBA::is_nil(gameserver)) {
			
		   int choix;
		   cin.clear();
		  menu:	
		   cout<<"----------Bienvenu dans P4G----------"<<endl;
		   cout<<"------1 : Lister tous les jeux-------"<<endl;
		   cout<<"------2 : Lister les jeux ouverts----"<<endl;
		   cout<<"------3 : Se connecter à un jeux"<<endl;
		   cout<<"------4 : Créer un jeux"<<endl;
		   cout<<"Entrez votre choix: ";
		   //cin>>choix;
		   saisie(choix, 4);
		   if(choix == 1){
			   listAllGames(gameserver);
		   }
		   else if(choix == 2){
			   
			   listOpenedGames(gameserver);
		   } 
 		   else if(choix == 3){
			   
			   listOpenedGames(gameserver);
			   char *id = new char[20];
			   cout<<"Entrez l'ID du jeu: ";
			   cin>>id;
			   Game_ptr gme = gameserver->getGame(id);
			   if (!CORBA::is_nil(gme)) {
				   
				    ReturnCode code;
					cout<<"Entrez votre pseudo: ";
					cin>>pseudo;
					code = gme->connectGame(pseudo);
					if(code == ::SUCCESS){
						if (!CORBA::is_nil(gme)) {
							cout<<"Bienvenue "<<pseudo<<endl;
							jouer(gme, pseudo);
						}
					}
					else{
						cout<<"Connexion échouée!"<<endl;
					}
				
					
			   }
			   else{
					cout<<"Ce jeu n'existe pas!"<<endl;
				}
			   delete []id;
			   
		   } 
		   else if(choix == 4){
			   cout<<"Entrez votre pseudo: ";
			   cin>>pseudo;
			   game = gameserver->createGame(pseudo);
			   cout<<game->getGameId()<<endl;
			   jouer(game, pseudo);
		   } 
		   else goto menu;
		   
		}
		else{
				
			cout<<"gameserver == null"<<endl;
		}
	}
    orb->destroy();
  }
  catch (CORBA::TRANSIENT&) {
    cerr << "Caught system exception TRANSIENT -- unable to contact the "
         << "server." << endl;
  }
  catch (CORBA::SystemException& ex) {
    cerr << "Caught a CORBA::" << ex._name() << endl;
  }
  catch (CORBA::Exception& ex) {
    cerr << "Caught CORBA::Exception: " << ex._name() << endl;
  }
  return 0;
}

//////////////////////////////////////////////////////////////////////

static CORBA::Object_ptr getObjectReference(CORBA::ORB_ptr orb){

  CosNaming::NamingContext_var rootContext;
  
  try {
    // Obtain a reference to the root context of the Name service:
    CORBA::Object_var obj;
    obj = orb->resolve_initial_references("NameService");

    // Narrow the reference returned.
    rootContext = CosNaming::NamingContext::_narrow(obj);

    if (CORBA::is_nil(rootContext)) {
      cerr << "Failed to narrow the root naming context." << endl;
      return CORBA::Object::_nil();
    }
  }
  catch (CORBA::NO_RESOURCES&) {
    cerr << "Caught NO_RESOURCES exception. You must configure omniORB "
	 << "with the location" << endl
	 << "of the naming service." << endl;
    return CORBA::Object::_nil();
  }
  catch (CORBA::ORB::InvalidName& ex) {
    // This should not happen!
    cerr << "Service required is invalid [does not exist]." << endl;
    return CORBA::Object::_nil();
  }

  // Create a name object, containing the name test/context:
  CosNaming::Name name;
  name.length(1);

  name[0].id   = (const char*) "GameServer";       // string copied
  name[0].kind = (const char*) ""; // string copied
  // Note on kind: The kind field is used to indicate the type
  // of the object. This is to avoid conventions such as that used
  // by files (name.type -- e.g. test.ps = postscript etc.)

  try {
    // Resolve the name to an object reference.
    return rootContext->resolve(name);
  }
  catch (CosNaming::NamingContext::NotFound& ex) {
    // This exception is thrown if any of the components of the
    // path [contexts or the object] aren't found:
    cerr << "Context not found." << endl;
  }
  catch (CORBA::TRANSIENT& ex) {
    cerr << "Caught system exception TRANSIENT -- unable to contact the "
         << "naming service." << endl
	 << "Make sure the naming server is running and that omniORB is "
	 << "configured correctly." << endl;
  }
  catch (CORBA::SystemException& ex) {
    cerr << "Caught a CORBA::" << ex._name()
	 << " while using the naming service." << endl;
  }
  return CORBA::Object::_nil();
}


void afficher(serverP4G::Game_ptr game){
	
	GameData data =  game->getGameData();
	CORBA::Char *grille = data.matrix;

	for ( int i(0); i<6; i++){
		cout<<grille[i*7]<<"	"<<grille[i*7+1]<<"	"<<grille[i*7+2]<<"	"<<grille[i*7+3]<<"	"<<grille[i*7+4]<<"	"<<grille[i*7+5]<<"	"<<grille[i*7+6]<<endl;
		cout<<""<<endl;
	}
	cout<<"--------------------------------------------------"<<endl;
	cout<<"--------------------------------------------------"<<endl;
	
}

void listOpenedGames(serverP4G::GameServer_var gameserver){
	
	ListGames_var listOfOpenedGames = gameserver->listOpenGames();
	cout<<"Nombre des jeux ouverts : "<<listOfOpenedGames->length()<<endl;
	if(listOfOpenedGames->length() != 0){
						   
		for (CORBA::ULong i(0); i < listOfOpenedGames->length(); i++){
				
			if(listOfOpenedGames[i] != NULL){
				cout<<listOfOpenedGames[i]<<endl;
			}		
								
		}
							
	}
	else{
							
		cout<<"Il n'y pas de jeux ouverts"<<endl;
	}
				
}

void listAllGames(serverP4G::GameServer_var gameserver){
	
	ListGames_var listOfGames = gameserver->listAllGames();
	cout<<"Nombre de tous les jeux : "<<listOfGames->length()<<endl;
	if(listOfGames->length() != 0){
				   
		for (CORBA::ULong i(0); i < listOfGames->length(); i++){
			
			if(listOfGames[i] != NULL){
				
				cout<<listOfGames[i]<<endl;
			}
		}
	}
	else{
					
		cout<<"Il n'y pas encore de jeux"<<endl;
	}
	
}

void jouer(serverP4G::Game_ptr game, const char * player){
	
	debut:
	ReturnCode code;
	int col;
	GameData data;
	GameState etat;
	if (!CORBA::is_nil(game)) {
		
		afficher(game);
		cout<<""<<endl;
		cout<<""<<endl;
	}	
	
	while(game->ismyTurn(player) !=true); //attendre mon tour
	
	data = game->getGameData();
	etat = data.state; //quelle est l'état du jeu?
	if(etat == ::TERMINATED){
		cout<<"--------------------------"<<endl;
		cout<<"-----Vous avez perdu!-----"<<endl;
		cout<<"--------------------------"<<endl;
	}
	else if(etat == ::ABANDONED){
		cout<<"-------------------------------------------"<<endl;
		cout<<"-----Votre adversaire a quitté le jeu!-----"<<endl;
		cout<<"-------------------------------------------"<<endl;		
	}
	else{
	
		if (!CORBA::is_nil(game)) {
		
			afficher(game);
			cout<<""<<endl;
			cout<<""<<endl;
		}		
		cout<<"Choisissez le numero d'une colonne de 1 à 7 et 8 pour quitter"<<endl;
		saisie(col, 8); //choix de colonne

		if(col == 1)code = game->play(player,::ONE);
		if(col == 2)code = game->play(player,::TWO);
		if(col == 3)code = game->play(player,::THREE);
		if(col == 4)code = game->play(player,::FOUR);
		if(col == 5)code = game->play(player,::FIVE);
		if(col == 6)code = game->play(player,::SIX);
		if(col == 7)code = game->play(player,::SEVEN);
		if(col == 8) game->quitPlayer(player);
		//
	
		if(code == ::G_OPEN){
		
			cout<<"Nombre de joueurs insuffisant!"<<endl;
			goto debut;
		}
		else if(code == ::WIN){
			cout<<"--------------------------"<<endl;
			cout<<"-----Vous avez gagné!-----"<<endl;
			cout<<"--------------------------"<<endl;
		}
		else{
		
			goto debut;
		}
	}
}

//fonction pour controler la saisie
bool saisie(int &n, int lim){
	
	cout<<"Entrez un nombre entre 1 et "<<lim<<endl;
	while( !(cin>>n) || n<1 || n>lim ){
		if(cin.eof()){
				return false;
		}
		else if( cin.fail()){
			cout << "Saisie incorrecte, recommencez : "<<endl;;  
			cin.clear();
			cin.ignore(numeric_limits<streamsize>::max(),'\n');
		}
		else{
			cout << "Le chiffre n'est pas entre 1 et "<<lim<<", recommencez : "<<endl;;
		}
		cin.clear();
	}
}
