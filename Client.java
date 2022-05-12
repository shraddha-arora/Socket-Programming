import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Scanner;

class Client extends JFrame {
    static Image global_img;

    public void paint(Graphics g) {
        super.paint(g);
        Image img = global_img;
        g.drawImage(img, 50, 50, this); //fetching image using bytes
    }

    public static void main(String[] argv) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of nodes");
        int numberOfNodes = scanner.nextInt(); //Input the number of nodes
        if (numberOfNodes < 3 || numberOfNodes > 10) {
            System.out.println("Value out of range"); //Given range for number of nodes = [3, 10]
        }
        int[][] adjacencyMatrix = new int[numberOfNodes][numberOfNodes];
        System.out.println("Enter adjacency matrix");
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = 0; j < numberOfNodes; j++) {
                adjacencyMatrix[i][j] = scanner.nextInt(); //Input adjacency matrix
            }
        }
        int pathLength, sourceNode, destinationNode;
        char sourceNodeChar, destinationNodeChar;
        System.out.println("Enter path length");
        pathLength = scanner.nextInt(); //Input path length
        System.out.println("Enter source node");
        sourceNodeChar = scanner.next().charAt(0); //Input source node as character
        sourceNode = (int) Character.toUpperCase(sourceNodeChar) - (int) 'A'; //converting source node to integer for use in code
        //System.out.println(sourceNode);
        System.out.println("Enter destination node");
        destinationNodeChar = scanner.next().charAt(0); //Input destination node as character
        destinationNode = (int) Character.toUpperCase(destinationNodeChar) - (int) 'A'; //converting destination node to integer for use in code
        //System.out.println(destinationNode);
        try {
            Socket clients = new Socket("127.0.0.1", 6789);
            DataInputStream ip = new DataInputStream(clients.getInputStream());
            DataOutputStream op = new DataOutputStream(clients.getOutputStream());
            op.writeInt(numberOfNodes);
            op.flush();
            for (int i = 0; i < numberOfNodes; i++) {
                for (int j = 0; j < numberOfNodes; j++) {
                    op.writeInt(adjacencyMatrix[i][j]);
                }
            }
            op.flush();
            op.writeInt(pathLength);
            op.flush();
            op.writeInt(sourceNode);
            op.flush();
            op.writeInt(destinationNode);
            op.flush();
            int isPath = ip.readInt();
            if (isPath == 1) {
                System.out.println("Yes, there exists a path of length " + pathLength + " from " + Character.toUpperCase(sourceNodeChar) + " to " + Character.toUpperCase(destinationNodeChar));
            } else {
                System.out.println("No, there doesn't exist a path of length " + pathLength + " from " + Character.toUpperCase(sourceNodeChar) + " to " + Character.toUpperCase(destinationNodeChar));
            }
            byte[] sizeAr = new byte[4];
            ip.read(sizeAr);
            int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
            byte[] imageAr = new byte[size];
            ip.read(imageAr);
            global_img = ImageIO.read(new ByteArrayInputStream(imageAr));

            JFrame frame = new Client();
            frame.setTitle("Frame Client");
            frame.setSize(600, 600);
            frame.setVisible(true);
            System.out.println("Image Received");

            clients.close(); //close the connection
        } catch (IOException ex) {
        }
    }
}
