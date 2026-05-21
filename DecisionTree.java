import java.io.*;
import java.util.*;
// Matthew Wang


// tree logic, includes making, moving through, saving, and loading the tree
public class DecisionTree {

    // fields
    private BinaryTreeNode root;

    // sets up the tree with a default starting state
    public DecisionTree() {

        root = new BinaryTreeNode("Do you want to go on a trip?");
        root.left = new BinaryTreeNode("Panama City Beach");
        root.right = new BinaryTreeNode("Bye");
    }

    // constructor
    public DecisionTree(BinaryTreeNode root) {
        this.root = root;
    }

    // returns root
    public BinaryTreeNode getRoot() {
        return root;
    }

    // traverse tree until reaching a leaf
    public void findDestination(Scanner sc) {

        BinaryTreeNode current = root;
        // if not a leaf
        while (!current.isLeaf()) {
            // print current nodes data
            System.out.println("\n" + current.data);

            System.out.print("Enter Y or N: ");
            String answer = sc.nextLine().trim().toUpperCase();

            // keep asking if the user gives a bad input
            while (!answer.equals("Y") && !answer.equals("N")) {

                System.out.print("Please enter Y or N: ");
                // clean up response
                answer = sc.nextLine().trim().toUpperCase();
            }

            // yes to the left, no to the right
            if (answer.equals("Y")) {
                current = current.left;

            } else {
                current = current.right;
            }

        }

        // reached a leaf, give final recommendation
        if (current.data.equals("Bye")) {
            System.out.println("\n No recommendation can be provided.");

        } else {
            System.out.println("\n The following destination is recommended: " + current.data);

        }
    }

    // run learning loop, lets expert teach the system
    public void buildExpertSystem(Scanner sc) {
        System.out.println("\n Building ExpertSystem:");

        boolean keepGoing = true;
        // continue remembering and building the expert tree until a leaf is reached
        while (keepGoing) {

            BinaryTreeNode current = root;
            BinaryTreeNode parent = null;
            boolean wentLeft = false;

            // following the expert input, get to a leaf
            while (!current.isLeaf()) {
                System.out.println("\n" + current.data);
                System.out.print("Y/N: ");
                String ans = sc.nextLine().trim().toUpperCase();

                while (!ans.equals("Y") && !ans.equals("N")) {

                    System.out.print("Please enter Y or N: ");
                    ans = sc.nextLine().trim().toUpperCase();
                }

                parent = current;
                if (ans.equals("Y")) {

                    current = current.left;
                    wentLeft = true;
                } else {

                    current = current.right;
                    wentLeft = false;

                }
            }

            // check if the systems guess is right or not once we get to a leaf
            if (current.data.equals("Bye")) {

                System.out.println("\n Returning to menu.");
                break;
            }

            System.out.println("\nI'd recommend you going to: " + current.data);
            System.out.print("Is this where you'd like to go? (Y/N): ");
            String correct = sc.nextLine().trim().toUpperCase();

            while (!correct.equals("Y") && !correct.equals("N")) {
                System.out.print("Please enter Y or N: ");
                correct = sc.nextLine().trim().toUpperCase();
            }

            if (correct.equals("Y")) {
                System.out.println("\n Expert system predicted the right answer.");
            } else {
                // if guess was wrong
                System.out.print("\n Enter your target destination: ");
                String newDest = sc.nextLine().trim();

                // ask for a question to build the tree w
                System.out.print("Please give me a question that can distinguish between \"" + current.data + "\" and \"" + newDest + "\": ");
                String newQuestion = sc.nextLine().trim();


                System.out.print("For \"" + newDest + "\" what would the answer to this question be? (Y/N): ");
                String newAnswer = sc.nextLine().trim().toUpperCase();

                while (!newAnswer.equals("Y") && !newAnswer.equals("N")) {
                    System.out.print("Please enter Y or N: ");
                    newAnswer = sc.nextLine().trim().toUpperCase();
                }

                // replace the old leaf with new data
                BinaryTreeNode newNode = new BinaryTreeNode(newQuestion);
                String oldDest = current.data;


                if (newAnswer.equals("Y")) {
                    newNode.left = new BinaryTreeNode(newDest);
                    newNode.right = new BinaryTreeNode(oldDest);
                } else {
                    newNode.left = new BinaryTreeNode(oldDest);
                    newNode.right = new BinaryTreeNode(newDest);
                }

                // put new node in proper pos
                if (parent == null) {
                    root = newNode;
                } else if (wentLeft) {
                    parent.left = newNode;
                } else {
                    parent.right = newNode;
                }

                System.out.println("\nGot it! I've learned about " + newDest + ".");

            }
            // check if expert is done
            System.out.print("\nWould you like to continue building the tree? (Y/N): ");
            String cont = sc.nextLine().trim().toUpperCase();
            if (!cont.equals("Y")) {
                keepGoing = false;
            }
        }

        // save the build
        System.out.print("\nEnter a filename to save the tree: ");
        String filename = sc.nextLine().trim();
        saveToFile(filename);
        System.out.println("Tree saved to " + filename);
    }

    // saves tree using preorder traversal - each line has NODE_TYPE and data
    public void saveToFile(String filename) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            savePreorder(root, pw);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());

        }
    }

    // build recursively in preorder, leaf and internal to identify placement
    private void savePreorder(BinaryTreeNode node, PrintWriter pw) {
        if (node == null) {
            pw.println("NULL");
            return;
        }

        if (node.isLeaf()) {
            pw.println("LEAF|" + node.data);
        } else {
            pw.println("INTERNAL|" + node.data);
            savePreorder(node.left, pw);
            savePreorder(node.right, pw);
        }
    }

    // read the file
    public static DecisionTree loadFromFile(String filename) {

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            //use queue to store everyhing in order
            Queue<String> lines = new LinkedList<>();
            String line;
            //read to the end
            while ((line = br.readLine()) != null) {
                lines.add(line.trim());
            }

            // build from the queue
            BinaryTreeNode root = buildFromQueue(lines);

            // if format issue
            if (root == null) {
                System.out.println("File was empty or malformed.");
                return null;
            }
            // wrap in decision tree
            return new DecisionTree(root);
        } catch (IOException e) {
            // exceptions
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }
    }

    // recursively make the tree from the preorder line queue
    private static BinaryTreeNode buildFromQueue(Queue<String> lines) {

        if (lines.isEmpty()) return null;

        String line = lines.poll();
        if (line.equals("NULL")) return null;

        // split into type and data
        String[] parts = line.split("\\|", 2);
        if (parts.length < 2) return null;


        String type = parts[0];
        String data = parts[1];

        // new node to hold data
        BinaryTreeNode node = new BinaryTreeNode(data);

        // has 2 chilrden
        if (type.equals("INTERNAL")) {
            node.left = buildFromQueue(lines);
            node.right =  buildFromQueue(lines);
        }

        return node;
    }

    // print sideways so its easier to debug
    public void printTree() {
        printHelper(root, "", true);
    }

    private void printHelper(BinaryTreeNode node, String indent, boolean isLeft) {

        if (node == null) return;
        System.out.println(indent + (isLeft ? "[Y] " : "[N] ") + node.data);
        // left then right tree printed recrusively
        printHelper(node.left, indent + "    ", true);
        printHelper(node.right, indent + "    ", false);

    }

}
