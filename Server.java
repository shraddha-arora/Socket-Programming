import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.file.FileSinkImages;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Server {

    public static boolean checkIfPath(ArrayList<Integer>[] adjacencyList, int sourceNode, int destinationNode, int pathLength, int numberOfNodes) {
        ArrayList<Integer> pathList = new ArrayList<>();
        ArrayList<Integer> pathLengthList = new ArrayList<>();
        boolean[] visitedNodeArray = new boolean[numberOfNodes];
        pathList.add(sourceNode);
        checkDFS(adjacencyList, sourceNode, destinationNode, pathLengthList, visitedNodeArray, pathList); //Creating pathLength by calling DFS
        boolean isPathLengthPresent = pathLengthList.contains(pathLength); //checks if the desired path length is present
        return isPathLengthPresent;
    }


    private static void checkDFS(ArrayList<Integer>[] adjacencyList, int sourceNode, int destinationNode, ArrayList<Integer> pathLength, boolean[] visitedNodeArray, ArrayList<Integer> pathList) {
        //recursively check path, add to the list of path lengths

        if (sourceNode == destinationNode) {
            pathLength.add(pathList.size() - 1);
            return;
        }
        visitedNodeArray[sourceNode] = true;

        for (Integer i : adjacencyList[sourceNode]) {
            if (!visitedNodeArray[i]) {
                pathList.add(i); //using current node to start the traversal
                checkDFS(adjacencyList, i, destinationNode, pathLength, visitedNodeArray, pathList);
                pathList.remove(i); //deleting current node from the path
            }
        }
        visitedNodeArray[destinationNode] = false;
    }

    private static ArrayList<Integer>[] adjacencyMatrixToAdjacencyList(int numberOfNodes, int[][] adjacencyMatrix) {
        //converting given adjacency matrix to adjacency list for the purpose of applying dfs to the same

        ArrayList<Integer>[] arrayList = new ArrayList[numberOfNodes];
        for (int i = 0; i < numberOfNodes; i++) {
            arrayList[i] = new ArrayList<>();
        }
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                if (adjacencyMatrix[i][j] >= 1) {
                    arrayList[i].add(j);
                }
            }
        }
        return arrayList;
    }

    public static void main(String[] argv) throws Exception {
        try {
            ServerSocket serversocket = new ServerSocket(6789); //Open the server socket
            System.out.println("Server Started");
            while (true) {
                Socket socket = serversocket.accept(); //wait for the client request (Listen)
                DataInputStream ip = new DataInputStream(socket.getInputStream()); //create I/O streams for communicating to the client (Connect)
                DataOutputStream op = new DataOutputStream(socket.getOutputStream()); //create I/O streams for communicating to the client (Connect)
                int numberOfNodes = ip.readInt(); //perform communication with client (Receive)
                int[][] adjacencyMatrix = new int[numberOfNodes][numberOfNodes];
                for (int i = 0; i < numberOfNodes; i++) {
                    for (int j = 0; j < numberOfNodes; j++) {
                        adjacencyMatrix[i][j] = ip.readInt(); //perform communication with client (Receive)
                    }
                }
                int pathLength = ip.readInt(); //perform communication with client (Receive)
                int sourceNode = ip.readInt(); //perform communication with client (Receive)
                int destinationNode = ip.readInt(); //perform communication with client (Receive)

                System.out.println();
                System.out.println("Received number of nodes = " + numberOfNodes); //printing number of nodes received
                System.out.println("Received path length = " + pathLength); //printing path length received
                System.out.println("Received sourceNode = " + (char) (sourceNode + (int) 'A')); //printing source node received
                System.out.println("Received destinationNode = " + (char) (destinationNode + (int) 'A')); //printing destination node received
                System.out.println("Received adjacency matrix:"); //printing adjacency matrix received
                for (int i = 0; i < numberOfNodes; i++) {
                    for (int j = 0; j < numberOfNodes; j++) {
                        System.out.print(adjacencyMatrix[i][j] + " ");
                    }
                    System.out.println();
                }
                createGraph(numberOfNodes, adjacencyMatrix);
                ArrayList<Integer>[] adjacencyList = new ArrayList[numberOfNodes];
                adjacencyList = adjacencyMatrixToAdjacencyList(numberOfNodes, adjacencyMatrix);
                //printing adjacency list
//                for (int i = 0; i < numberOfNodes; i++) {
//                    for (int j : adjacencyList[i]) {
//                        System.out.print(j + " ");
//                    }
//                    System.out.println();
//                }

                boolean check = checkIfPath(adjacencyList, sourceNode, destinationNode, pathLength, numberOfNodes); //check if the required path exists
                int answer;
                if (check) {
                    answer = 1;
                } else {
                    answer = 0;
                }
                op.writeInt(answer); //perform communication with client (Send)

                BufferedImage image = ImageIO.read(new File("C:\\Users\\Shraddha\\IdeaProjects\\SocketProgrammingAssignment\\graphNode.png")); //reading the image of graph generated
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(image, "png", byteArrayOutputStream);
                byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array(); //sending the image as bytes to the client
                op.write(size);
                op.write(byteArrayOutputStream.toByteArray());
                op.flush();
                socket.close();
            }

        } catch (IOException ex) {
        }
    }

    private static void createGraph(int numberOfNodes, int[][] adjacencyMatrix) throws IOException {
        System.setProperty("org.graphstream.ui", "swing");
        MultiGraph graphNode = new MultiGraph("multiGraph");
        for (int i = 0; i < numberOfNodes; i++) {
            graphNode.addNode(String.valueOf((char) (i + (int) 'A')));
        }
        for (int i = 0; i < numberOfNodes; i++) {
            Node e1 = graphNode.getNode(String.valueOf((char) (i + (int) 'A')));
            e1.setAttribute("ui.style", "shape:circle;fill-color: green;size: 50px;");
            e1.setAttribute("ui.label", String.valueOf((char) (i + (int) 'A')));
        }
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    String startingNode = String.valueOf((char) (i + (int) 'A'));
                    String endingNode = String.valueOf((char) (j + (int) 'A'));
                    String id = startingNode + endingNode;
                    graphNode.addEdge(id, startingNode, endingNode, true);
                }
            }
        }
        FileSinkImages img = FileSinkImages.createDefault(); //createDefault takes screenshot of the image
        img.setOutputType(FileSinkImages.OutputType.PNG); //image attribute
        img.setResolution(400, 400); //image attribute
        img.setLayoutPolicy(FileSinkImages.LayoutPolicy.COMPUTED_FULLY_AT_NEW_IMAGE); //image attribute
        img.writeAll(graphNode, "graphNode.png"); //storing image
    }
}