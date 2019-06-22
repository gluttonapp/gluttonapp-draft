package com.gluttonapp;

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

import java.util.Scanner;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;

public class App {
    public static void main(String[] args) {
        Cluster cluster = connectToDatabase();
        GraphTraversalSource g = getGraphTraversalSource(cluster);

        System.out.println("Using cluster connection: " + cluster.toString());
        System.out.println("Using traversal source: " + g.toString());

        displayMenu(g);

        cluster.close();
        System.exit(0);
    }

    private static void displayMenu(GraphTraversalSource g) {
        int option = -1;
        while (option != 0)
        {
            option = showMenu();
            switch (option) {
                case 1:
                    //Get Vertex Count
                    System.out.println("Vertex count: " + getVertexCount(g));
                    break;
                default:
                    System.out.println("Sorry, please enter valid Option");
            }
        }

        System.out.println("Exiting GluttonApp, Bye!");
    }

    private static int showMenu() {

        int option = -1;
        Scanner keyboard = new Scanner(System.in);
        System.out.println();
        System.out.println("Main Menu:");
        System.out.println("--------------");
        System.out.println("1) Get Count of the Vertices");
        System.out.println("0) Quit");
        System.out.println("--------------");
        System.out.println("Enter your choice:");
        option = keyboard.nextInt();

        return option;
    }

    private static Long getVertexCount(GraphTraversalSource g) {
        GraphTraversal t = g.V();
        t = t.count();
        return (Long)t.next();
    }

    private static Cluster connectToDatabase() {
        Cluster.Builder builder = Cluster.build();
        builder.addContactPoint("localhost");
        builder.port(8182);

        return builder.create();
    }

    public static GraphTraversalSource getGraphTraversalSource(Cluster cluster) {
        return traversal().withRemote(DriverRemoteConnection.using(cluster));
    }
}