import java.util.Scanner;
// Matthew Wang
// handles menu and connects everything
public class TravelExpertSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DecisionTree tree = null;

        System.out.println("Tree Expert System Program");

        boolean running = true;

        // menu
        while (running) {
            System.out.println("\nMain Menu:");
            System.out.println("  1. Build an Expert System");
            System.out.println("  2. Find a Destination");
            System.out.println("  3. Print Tree");
            System.out.println("  4. Quit");
            System.out.print("\nYour choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    // use the tree already loaded or just use default 2 node tree to begin building
                    if (tree == null) {
                        tree = new DecisionTree();
                    }
                    tree.buildExpertSystem(sc);
                    break;


                case "2":
                    // ask for a tree file
                    if (tree == null) {
                        System.out.print("No tree in the system currently, enter filename to load from: ");
                        String fname = sc.nextLine().trim();
                        tree = DecisionTree.loadFromFile(fname);

                        // if cant load
                        if (tree == null) {
                            System.out.println("Could not load tree. Returning to menu.");
                            break;
                        }

                        System.out.println("Tree loaded");
                    }
                    tree.findDestination(sc);
                    break;

                case "3":
                    if (tree == null) {
                        System.out.println("No tree in system currently.");

                    } else {
                        System.out.println("\nDecision Tree Structure:");
                        tree.printTree();
                    }
                    break;

                case "4":
                    // end program
                    System.out.println("\n Program Ending.");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Please enter 1, 2, 3, or 4.");
            }
        }

        sc.close();
    }
}