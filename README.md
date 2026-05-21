# yes-or-no

a self-learning travel recommendation expert system built on a binary decision tree in Java. it asks you yes/no questions to narrow down a destination — and when it's wrong, it learns from you and grows the tree so it won't be wrong the same way again.

---

## the idea

expert systems are one of the oldest ideas in AI: encode a domain expert's knowledge as a set of rules, then let users query it. the twist here is that the rules aren't hardcoded — the tree teaches itself through conversation. every time it guesses wrong, it asks the user for a new question that distinguishes the wrong answer from the right one, inserts that question into the tree at exactly the right spot, and remembers it forever via file save.

the mechanic is simple: every internal node is a yes/no question. every leaf is a destination. you answer questions until you hit a leaf, and that's the recommendation. if the leaf is wrong, you correct it, give it a question to split on, and the tree gains a new branch.

---

## how the tree works

**structure** — `BinaryTreeNode` holds a `String data` field (either a question or a destination), plus `left` and `right` children. left is always the "yes" branch, right is always "no". leaf nodes have both children null.

**traversal** — `findDestination()` walks the tree from root to leaf, printing each question and reading Y/N input. validation loops until a valid answer is given. reaches a leaf and either prints a recommendation or a "no recommendation" message if it lands on the default "Bye" node.

**learning loop** — `buildExpertSystem()` does the same traversal but after reaching a leaf checks if the guess was correct. if not, it prompts for the real destination and a distinguishing question, then replaces the old leaf with a new internal node:

```
old leaf: "Panama City Beach"
user wants: "Aspen"
question: "Do you prefer cold weather?"

new subtree:
    [Do you prefer cold weather?]
       Y /              \ N
    "Aspen"    "Panama City Beach"
```

the new node drops into the tree exactly where the old leaf was, using the `parent` and `wentLeft` pointers tracked during traversal.

**save/load** — trees are serialized to plain text files using preorder traversal. each line is either `INTERNAL|question text` or `LEAF|destination`. loading rebuilds the tree by reading lines into a queue and recursively constructing nodes — `INTERNAL` nodes pull their left and right children from the next two recursive calls, `LEAF` nodes terminate.

```
INTERNAL|Do you want to go on a trip?
LEAF|Panama City Beach
LEAF|Bye
```

this format is human-readable, easy to debug, and trivially extensible.

---

## what each file does

| file | description |
|---|---|
| `BinaryTreeNode.java` | single tree node — data, left, right, isLeaf() |
| `DecisionTree.java` | tree logic — traversal, learning loop, preorder save/load, debug print |
| `TravelExpertSystem.java` | menu driver — connects user input to the tree |

---

## running it

```bash
javac BinaryTreeNode.java DecisionTree.java TravelExpertSystem.java
java TravelExpertSystem
```

on first run, choose option 1 to start building the expert system. when done, save it to a file. next run, load that file with option 2 to query what you built. option 3 prints the current tree sideways for debugging.

---

## example session

```
Main Menu:
  1. Build an Expert System
  2. Find a Destination
  3. Print Tree
  4. Quit

Your choice: 1

Do you want to go on a trip?
Y/N: Y

I'd recommend you going to: Panama City Beach
Is this where you'd like to go? (Y/N): N

Enter your target destination: Aspen
Please give me a question that can distinguish between "Panama City Beach" and "Aspen": Do you prefer cold weather?
For "Aspen" what would the answer to this question be? (Y/N): Y

Got it! I've learned about Aspen.
```
