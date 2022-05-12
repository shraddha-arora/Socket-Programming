# Socket-Programming

Socket programming is a way of connecting two nodes on a network to communicate with each other. One socket(node) listens on a particular port at an IP, while other socket reaches out to the other to form a connection. The server forms the listener socket while the client reaches out to the server.

Socket programming in Java is used for communication between the applications that are running on different JRE. It can be either connection-oriented or connectionless. On the whole, a socket is a way to establish a connection between a client and a server. A socket in Java is one endpoint of a two-way communication link between two programs running on the network. A socket is bound to a port number so that the TCP layer can identify the application that data is destined to be sent to.

Here, we are writing java code to implement the following using a client and server:

The client sends server 4 values, for example X, n, B, C where,  X is the adjacency matrix of a directed graph with 5 nodes A B C D E, and n is the length of the path from node B to node C.

The server responds back with two responses:

(a)    positive Y response (or negative N response) if there exists (or doesn't exist) a path of length n from B to C.

(b)    the image of the directed graph with nodes A B C D E proving the validity of the response.

For simplicity, assume a 5-node graph with nodes named A, B, C, D, E.

<img src="https://user-images.githubusercontent.com/72869822/168046679-2bd5ce74-0ebc-4a45-a161-fb08ed6ac507.png" width=50% height=50%>

<img src="https://user-images.githubusercontent.com/72869822/168046752-17d1ab47-94ce-4826-bbfd-aae731f3ddf9.png" width=50% height=50%>

<img src="https://user-images.githubusercontent.com/72869822/168046797-7c9e94c2-376c-4bb2-8f65-24e904d008de.png" width=50% height=50%>
                  

First a socket connection is established. Here we are using local host and port number 6789 to establish a TCP connection. This stream is used for both sending and receiving data, after which the connection is closed.

Once ther server is up and running, the client will send requests to the server and wait for a response.

In this program, Graphstream has been used to implement the graph. GraphStream is a graph handling Java library that focuses on the dynamics aspects of graphs. Its main focus is on the modeling of dynamic interaction networks of various sizes.

# Running the program

To run the given program, create a Maven project and install the following modules:

    1. graphstream.gs.core
    
    2. graphstream.gs.ui.swing
    
Run Server.java and when the message saying "Server started" is displayed, run Client.java and enter all inputs as required!
