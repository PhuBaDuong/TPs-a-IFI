CXXFLAGS=-g
LDFLAGS=-lomniORB4 -lomnithread -lomniDynamic4

all: idl  Client

idl: P4G.hh P4GSK.cc

P4G.hh P4GSK.cc: P4G.idl
	omniidl -bcxx P4G.idl
Client.o: P4G.hh Client.cpp
Client:	P4GSK.o Client.o
	g++ P4GSK.o Client.o $(LDFLAGS) -o Client
	
clean:
	rm -f *.o
