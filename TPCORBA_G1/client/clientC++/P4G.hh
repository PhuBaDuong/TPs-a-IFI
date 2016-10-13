// This file is generated by omniidl (C++ backend)- omniORB_4_2. Do not edit.
#ifndef __P4G_hh__
#define __P4G_hh__

#ifndef __CORBA_H_EXTERNAL_GUARD__
#include <omniORB4/CORBA.h>
#endif

#ifndef  USE_stub_in_nt_dll
# define USE_stub_in_nt_dll_NOT_DEFINED_P4G
#endif
#ifndef  USE_core_stub_in_nt_dll
# define USE_core_stub_in_nt_dll_NOT_DEFINED_P4G
#endif
#ifndef  USE_dyn_stub_in_nt_dll
# define USE_dyn_stub_in_nt_dll_NOT_DEFINED_P4G
#endif






#ifdef USE_stub_in_nt_dll
# ifndef USE_core_stub_in_nt_dll
#  define USE_core_stub_in_nt_dll
# endif
# ifndef USE_dyn_stub_in_nt_dll
#  define USE_dyn_stub_in_nt_dll
# endif
#endif

#ifdef _core_attr
# error "A local CPP macro _core_attr has already been defined."
#else
# ifdef  USE_core_stub_in_nt_dll
#  define _core_attr _OMNIORB_NTDLL_IMPORT
# else
#  define _core_attr
# endif
#endif

#ifdef _dyn_attr
# error "A local CPP macro _dyn_attr has already been defined."
#else
# ifdef  USE_dyn_stub_in_nt_dll
#  define _dyn_attr _OMNIORB_NTDLL_IMPORT
# else
#  define _dyn_attr
# endif
#endif



_CORBA_MODULE serverP4G

_CORBA_MODULE_BEG

  enum GameState { CREATED, RUNNING, TERMINATED, ABANDONED /*, __max_GameState=0xffffffff */ };
  typedef GameState& GameState_out;

  typedef ::CORBA::Char GameMatrix[42];
  typedef ::CORBA::Char GameMatrix_slice;

  _CORBA_MODULE_INLINE GameMatrix_slice* GameMatrix_alloc() {
    return new GameMatrix_slice[42];
  }

  _CORBA_MODULE_INLINE GameMatrix_slice* GameMatrix_dup(const GameMatrix_slice* _s) {
    if (!_s) return 0;
    GameMatrix_slice* _data = GameMatrix_alloc();
    if (_data) {
      for (_CORBA_ULong _0i0 = 0; _0i0 < 42; _0i0++){
        
        _data[_0i0] = _s[_0i0];

      }
  
    }
    return _data;
  }

  _CORBA_MODULE_INLINE void GameMatrix_copy(GameMatrix_slice* _to, const GameMatrix_slice* _from){
    for (_CORBA_ULong _0i0 = 0; _0i0 < 42; _0i0++){
      
      _to[_0i0] = _from[_0i0];

    }
  
  }

  _CORBA_MODULE_INLINE void GameMatrix_free(GameMatrix_slice* _s) {
    delete [] _s;
  }

  class GameMatrix_copyHelper {
  public:
    static inline GameMatrix_slice* alloc() { return ::serverP4G::GameMatrix_alloc(); }
    static inline GameMatrix_slice* dup(const GameMatrix_slice* p) { return ::serverP4G::GameMatrix_dup(p); }
    static inline void free(GameMatrix_slice* p) { ::serverP4G::GameMatrix_free(p); }
  };

  typedef _CORBA_Array_Fix_Var<GameMatrix_copyHelper,GameMatrix_slice> GameMatrix_var;
  typedef _CORBA_Array_Fix_Forany<GameMatrix_copyHelper,GameMatrix_slice> GameMatrix_forany;

  typedef GameMatrix_slice* GameMatrix_out;

  struct GameData {
    typedef _CORBA_ConstrType_Fix_Var<GameData> _var_type;

    
    GameState state;

    GameMatrix matrix;

  

    void operator>>= (cdrStream &) const;
    void operator<<= (cdrStream &);
  };

  typedef GameData::_var_type GameData_var;

  typedef GameData& GameData_out;

  struct Players {
    typedef _CORBA_ConstrType_Variable_Var<Players> _var_type;

    
    ::CORBA::String_member player1;

    ::CORBA::String_member player2;

  

    void operator>>= (cdrStream &) const;
    void operator<<= (cdrStream &);
  };

  typedef Players::_var_type Players_var;

  typedef _CORBA_ConstrType_Variable_OUT_arg< Players,Players_var > Players_out;

  enum ReturnCode { SUCCESS, WIN, XNULL, G_OVER, G_OPEN, P_INV /*, __max_ReturnCode=0xffffffff */ };
  typedef ReturnCode& ReturnCode_out;

  enum Column { ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN /*, __max_Column=0xffffffff */ };
  typedef Column& Column_out;

  typedef char* GameId;
  typedef ::CORBA::String_var GameId_var;
  typedef ::CORBA::String_out GameId_out;

  class ListGames_var;

  class ListGames : public _CORBA_Unbounded_Sequence_String {
  public:
    typedef ListGames_var _var_type;
    inline ListGames() {}
    inline ListGames(const ListGames& _s)
      : _CORBA_Unbounded_Sequence_String(_s) {}

    inline ListGames(_CORBA_ULong _max)
      : _CORBA_Unbounded_Sequence_String(_max) {}
    inline ListGames(_CORBA_ULong _max, _CORBA_ULong _len, char** _val, _CORBA_Boolean _rel=0)
      : _CORBA_Unbounded_Sequence_String(_max, _len, _val, _rel) {}

  

    inline ListGames& operator = (const ListGames& _s) {
      _CORBA_Unbounded_Sequence_String::operator=(_s);
      return *this;
    }
  };

  class ListGames_out;

  class ListGames_var {
  public:
    inline ListGames_var() : _pd_seq(0) {}
    inline ListGames_var(ListGames* _s) : _pd_seq(_s) {}
    inline ListGames_var(const ListGames_var& _s) {
      if (_s._pd_seq)  _pd_seq = new ListGames(*_s._pd_seq);
      else             _pd_seq = 0;
    }
    inline ~ListGames_var() { if (_pd_seq)  delete _pd_seq; }
      
    inline ListGames_var& operator = (ListGames* _s) {
      if (_pd_seq)  delete _pd_seq;
      _pd_seq = _s;
      return *this;
    }
    inline ListGames_var& operator = (const ListGames_var& _s) {
      if (&_s != this) {
        if (_s._pd_seq) {
          if (!_pd_seq)  _pd_seq = new ListGames;
          *_pd_seq = *_s._pd_seq;
        }
        else if (_pd_seq) {
          delete _pd_seq;
          _pd_seq = 0;
        }
      }
      return *this;
    }
    inline _CORBA_String_element operator [] (_CORBA_ULong _s) {
      return (*_pd_seq)[_s];
    }

  

    inline ListGames* operator -> () { return _pd_seq; }
    inline const ListGames* operator -> () const { return _pd_seq; }
#if defined(__GNUG__)
    inline operator ListGames& () const { return *_pd_seq; }
#else
    inline operator const ListGames& () const { return *_pd_seq; }
    inline operator ListGames& () { return *_pd_seq; }
#endif
      
    inline const ListGames& in() const { return *_pd_seq; }
    inline ListGames&       inout()    { return *_pd_seq; }
    inline ListGames*&      out() {
      if (_pd_seq) { delete _pd_seq; _pd_seq = 0; }
      return _pd_seq;
    }
    inline ListGames* _retn() { ListGames* tmp = _pd_seq; _pd_seq = 0; return tmp; }
      
    friend class ListGames_out;
    
  private:
    ListGames* _pd_seq;
  };

  class ListGames_out {
  public:
    inline ListGames_out(ListGames*& _s) : _data(_s) { _data = 0; }
    inline ListGames_out(ListGames_var& _s)
      : _data(_s._pd_seq) { _s = (ListGames*) 0; }
    inline ListGames_out(const ListGames_out& _s) : _data(_s._data) {}
    inline ListGames_out& operator = (const ListGames_out& _s) {
      _data = _s._data;
      return *this;
    }
    inline ListGames_out& operator = (ListGames* _s) {
      _data = _s;
      return *this;
    }
    inline operator ListGames*&()  { return _data; }
    inline ListGames*& ptr()       { return _data; }
    inline ListGames* operator->() { return _data; }

    inline _CORBA_String_element operator [] (_CORBA_ULong _i) {
      return (*_data)[_i];
    }

  

    ListGames*& _data;

  private:
    ListGames_out();
    ListGames_out& operator=(const ListGames_var&);
  };

#ifndef __serverP4G_mGame__
#define __serverP4G_mGame__
  class Game;
  class _objref_Game;
  class _impl_Game;
  
  typedef _objref_Game* Game_ptr;
  typedef Game_ptr GameRef;

  class Game_Helper {
  public:
    typedef Game_ptr _ptr_type;

    static _ptr_type _nil();
    static _CORBA_Boolean is_nil(_ptr_type);
    static void release(_ptr_type);
    static void duplicate(_ptr_type);
    static void marshalObjRef(_ptr_type, cdrStream&);
    static _ptr_type unmarshalObjRef(cdrStream&);
  };

  typedef _CORBA_ObjRef_Var<_objref_Game, Game_Helper> Game_var;
  typedef _CORBA_ObjRef_OUT_arg<_objref_Game,Game_Helper > Game_out;

#endif

  // interface Game
  class Game {
  public:
    // Declarations for this interface type.
    typedef Game_ptr _ptr_type;
    typedef Game_var _var_type;

    static _ptr_type _duplicate(_ptr_type);
    static _ptr_type _narrow(::CORBA::Object_ptr);
    static _ptr_type _unchecked_narrow(::CORBA::Object_ptr);
    
    static _ptr_type _nil();

    static inline void _marshalObjRef(_ptr_type, cdrStream&);

    static inline _ptr_type _unmarshalObjRef(cdrStream& s) {
      omniObjRef* o = omniObjRef::_unMarshal(_PD_repoId,s);
      if (o)
        return (_ptr_type) o->_ptrToObjRef(_PD_repoId);
      else
        return _nil();
    }

    static inline _ptr_type _fromObjRef(omniObjRef* o) {
      if (o)
        return (_ptr_type) o->_ptrToObjRef(_PD_repoId);
      else
        return _nil();
    }

    static _core_attr const char* _PD_repoId;

    // Other IDL defined within this scope.
    
  };

  class _objref_Game :
    public virtual ::CORBA::Object,
    public virtual omniObjRef
  {
  public:
    // IDL operations
    GameData getGameData();
    char* getGameId();
    Players* getPlayers();
    ReturnCode connectGame(const char* player);
    ReturnCode play(const char* player, ::serverP4G::Column col);
    ::CORBA::Boolean quitPlayer(const char* player);
    ::CORBA::Boolean ismyTurn(const char* player);

    // Constructors
    inline _objref_Game()  { _PR_setobj(0); }  // nil
    _objref_Game(omniIOR*, omniIdentity*);

  protected:
    virtual ~_objref_Game();

    
  private:
    virtual void* _ptrToObjRef(const char*);

    _objref_Game(const _objref_Game&);
    _objref_Game& operator = (const _objref_Game&);
    // not implemented

    friend class Game;
  };

  class _pof_Game : public _OMNI_NS(proxyObjectFactory) {
  public:
    inline _pof_Game() : _OMNI_NS(proxyObjectFactory)(Game::_PD_repoId) {}
    virtual ~_pof_Game();

    virtual omniObjRef* newObjRef(omniIOR*,omniIdentity*);
    virtual _CORBA_Boolean is_a(const char*) const;
  };

  class _impl_Game :
    public virtual omniServant
  {
  public:
    virtual ~_impl_Game();

    virtual GameData getGameData() = 0;
    virtual char* getGameId() = 0;
    virtual Players* getPlayers() = 0;
    virtual ReturnCode connectGame(const char* player) = 0;
    virtual ReturnCode play(const char* player, ::serverP4G::Column col) = 0;
    virtual ::CORBA::Boolean quitPlayer(const char* player) = 0;
    virtual ::CORBA::Boolean ismyTurn(const char* player) = 0;
    
  public:  // Really protected, workaround for xlC
    virtual _CORBA_Boolean _dispatch(omniCallHandle&);

  private:
    virtual void* _ptrToInterface(const char*);
    virtual const char* _mostDerivedRepoId();
    
  };


#ifndef __serverP4G_mGameServer__
#define __serverP4G_mGameServer__
  class GameServer;
  class _objref_GameServer;
  class _impl_GameServer;
  
  typedef _objref_GameServer* GameServer_ptr;
  typedef GameServer_ptr GameServerRef;

  class GameServer_Helper {
  public:
    typedef GameServer_ptr _ptr_type;

    static _ptr_type _nil();
    static _CORBA_Boolean is_nil(_ptr_type);
    static void release(_ptr_type);
    static void duplicate(_ptr_type);
    static void marshalObjRef(_ptr_type, cdrStream&);
    static _ptr_type unmarshalObjRef(cdrStream&);
  };

  typedef _CORBA_ObjRef_Var<_objref_GameServer, GameServer_Helper> GameServer_var;
  typedef _CORBA_ObjRef_OUT_arg<_objref_GameServer,GameServer_Helper > GameServer_out;

#endif

  // interface GameServer
  class GameServer {
  public:
    // Declarations for this interface type.
    typedef GameServer_ptr _ptr_type;
    typedef GameServer_var _var_type;

    static _ptr_type _duplicate(_ptr_type);
    static _ptr_type _narrow(::CORBA::Object_ptr);
    static _ptr_type _unchecked_narrow(::CORBA::Object_ptr);
    
    static _ptr_type _nil();

    static inline void _marshalObjRef(_ptr_type, cdrStream&);

    static inline _ptr_type _unmarshalObjRef(cdrStream& s) {
      omniObjRef* o = omniObjRef::_unMarshal(_PD_repoId,s);
      if (o)
        return (_ptr_type) o->_ptrToObjRef(_PD_repoId);
      else
        return _nil();
    }

    static inline _ptr_type _fromObjRef(omniObjRef* o) {
      if (o)
        return (_ptr_type) o->_ptrToObjRef(_PD_repoId);
      else
        return _nil();
    }

    static _core_attr const char* _PD_repoId;

    // Other IDL defined within this scope.
    
  };

  class _objref_GameServer :
    public virtual ::CORBA::Object,
    public virtual omniObjRef
  {
  public:
    // IDL operations
    ListGames* listAllGames();
    ListGames* listOpenGames();
    Game_ptr getGame(const char* id);
    Game_ptr createGame(const char* player);

    // Constructors
    inline _objref_GameServer()  { _PR_setobj(0); }  // nil
    _objref_GameServer(omniIOR*, omniIdentity*);

  protected:
    virtual ~_objref_GameServer();

    
  private:
    virtual void* _ptrToObjRef(const char*);

    _objref_GameServer(const _objref_GameServer&);
    _objref_GameServer& operator = (const _objref_GameServer&);
    // not implemented

    friend class GameServer;
  };

  class _pof_GameServer : public _OMNI_NS(proxyObjectFactory) {
  public:
    inline _pof_GameServer() : _OMNI_NS(proxyObjectFactory)(GameServer::_PD_repoId) {}
    virtual ~_pof_GameServer();

    virtual omniObjRef* newObjRef(omniIOR*,omniIdentity*);
    virtual _CORBA_Boolean is_a(const char*) const;
  };

  class _impl_GameServer :
    public virtual omniServant
  {
  public:
    virtual ~_impl_GameServer();

    virtual ListGames* listAllGames() = 0;
    virtual ListGames* listOpenGames() = 0;
    virtual Game_ptr getGame(const char* id) = 0;
    virtual Game_ptr createGame(const char* player) = 0;
    
  public:  // Really protected, workaround for xlC
    virtual _CORBA_Boolean _dispatch(omniCallHandle&);

  private:
    virtual void* _ptrToInterface(const char*);
    virtual const char* _mostDerivedRepoId();
    
  };


_CORBA_MODULE_END



_CORBA_MODULE POA_serverP4G
_CORBA_MODULE_BEG

  class Game :
    public virtual serverP4G::_impl_Game,
    public virtual ::PortableServer::ServantBase
  {
  public:
    virtual ~Game();

    inline ::serverP4G::Game_ptr _this() {
      return (::serverP4G::Game_ptr) _do_this(::serverP4G::Game::_PD_repoId);
    }
  };

  class GameServer :
    public virtual serverP4G::_impl_GameServer,
    public virtual ::PortableServer::ServantBase
  {
  public:
    virtual ~GameServer();

    inline ::serverP4G::GameServer_ptr _this() {
      return (::serverP4G::GameServer_ptr) _do_this(::serverP4G::GameServer::_PD_repoId);
    }
  };

_CORBA_MODULE_END



_CORBA_MODULE OBV_serverP4G
_CORBA_MODULE_BEG

_CORBA_MODULE_END





#undef _core_attr
#undef _dyn_attr

inline void operator >>=(serverP4G::GameState _e, cdrStream& s) {
  ::operator>>=((::CORBA::ULong)_e, s);
}

inline void operator <<= (serverP4G::GameState& _e, cdrStream& s) {
  ::CORBA::ULong _0RL_e;
  ::operator<<=(_0RL_e,s);
  if (_0RL_e <= serverP4G::ABANDONED) {
    _e = (serverP4G::GameState) _0RL_e;
  }
  else {
    OMNIORB_THROW(MARSHAL,_OMNI_NS(MARSHAL_InvalidEnumValue),
                  (::CORBA::CompletionStatus)s.completion());
  }
}

inline void operator >>=(serverP4G::ReturnCode _e, cdrStream& s) {
  ::operator>>=((::CORBA::ULong)_e, s);
}

inline void operator <<= (serverP4G::ReturnCode& _e, cdrStream& s) {
  ::CORBA::ULong _0RL_e;
  ::operator<<=(_0RL_e,s);
  if (_0RL_e <= serverP4G::P_INV) {
    _e = (serverP4G::ReturnCode) _0RL_e;
  }
  else {
    OMNIORB_THROW(MARSHAL,_OMNI_NS(MARSHAL_InvalidEnumValue),
                  (::CORBA::CompletionStatus)s.completion());
  }
}

inline void operator >>=(serverP4G::Column _e, cdrStream& s) {
  ::operator>>=((::CORBA::ULong)_e, s);
}

inline void operator <<= (serverP4G::Column& _e, cdrStream& s) {
  ::CORBA::ULong _0RL_e;
  ::operator<<=(_0RL_e,s);
  if (_0RL_e <= serverP4G::SEVEN) {
    _e = (serverP4G::Column) _0RL_e;
  }
  else {
    OMNIORB_THROW(MARSHAL,_OMNI_NS(MARSHAL_InvalidEnumValue),
                  (::CORBA::CompletionStatus)s.completion());
  }
}



inline void
serverP4G::Game::_marshalObjRef(::serverP4G::Game_ptr obj, cdrStream& s) {
  omniObjRef::_marshal(obj->_PR_getobj(),s);
}

inline void
serverP4G::GameServer::_marshalObjRef(::serverP4G::GameServer_ptr obj, cdrStream& s) {
  omniObjRef::_marshal(obj->_PR_getobj(),s);
}



#ifdef   USE_stub_in_nt_dll_NOT_DEFINED_P4G
# undef  USE_stub_in_nt_dll
# undef  USE_stub_in_nt_dll_NOT_DEFINED_P4G
#endif
#ifdef   USE_core_stub_in_nt_dll_NOT_DEFINED_P4G
# undef  USE_core_stub_in_nt_dll
# undef  USE_core_stub_in_nt_dll_NOT_DEFINED_P4G
#endif
#ifdef   USE_dyn_stub_in_nt_dll_NOT_DEFINED_P4G
# undef  USE_dyn_stub_in_nt_dll
# undef  USE_dyn_stub_in_nt_dll_NOT_DEFINED_P4G
#endif

#endif  // __P4G_hh__
