import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;


public class Matching
{
	public static HashTable<String, Pair> hashTable;
	public static int line_num;
	public static ArrayList<String> initial_input = new ArrayList<>();
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input) throws IOException {
		char command_letter = input.charAt(0);
		String command_input = input.substring(2);

		if (command_letter == '<') {
			hashTable = new HashTable<>();
			BufferedReader bf = new BufferedReader(new FileReader(command_input));
			String line;
			line_num = 1;

			while ((line = bf.readLine())!= null) {
				initial_input.add(line);
				for (int j =0; j <= line.length()-6; j++){
					String sub_str = line.substring(j,j+6);
					Pair pair = new Pair(line_num, j+1);

					hashTable.insert(sub_str, pair);
				}
				line_num ++;
			}
			bf.close();

		}else if (command_letter == '@') {
			int slot_num = Integer.parseInt(command_input);
			if (slot_num<0 || slot_num>=100) throw new IOException();

			hashTable.slot[slot_num].print();

		}else if (command_letter == '?') {
			int mod = command_input.length()%6;
			int i = 0;
			boolean NOT_FOUND = false;
			String result_string = null;

			String first_target = command_input.substring(0, 6);
			AVLNode first_node = hashTable.search(first_target);
			if (first_node == null) {
				NOT_FOUND = true;		// pattern doesn't exist
			} else {
				LinkedList<Pair> first_coords = first_node.coordinates;
				LinkedList<Pair> results = new LinkedList<>(first_coords);

				if (mod == 0) { i = 6; }
				i += mod ;

				while (i <= command_input.length()-6) {
					String second_target = command_input.substring(i, i+6);
					AVLNode second_node = hashTable.search(second_target);
					if (second_node == null) {
						NOT_FOUND = true;
						break;
					}
					LinkedList<Pair> second_coords = second_node.coordinates;

					for (Pair pair : first_coords) {
						Pair p = new Pair(pair.first, pair.second + i);
						if (!second_coords.contains(p)) {
							results.remove(pair);
						}
					}

					if (results.isEmpty()) {
						NOT_FOUND = true;
						break;
					}

					i+= 6;
				}
				
				StringBuilder str = new StringBuilder();
				for (Pair p : results) {
					str.append(p.toString()).append(" ");
				}
				result_string = str.toString().trim();
			}

			if (NOT_FOUND) {
				System.out.println("(0, 0)");
			}else {
				System.out.println(result_string);
			}
			

		}else if (command_letter == '/') {
			AVLNode node = hashTable.search(command_input);
			int return_value = node.coordinates.size();

			ArrayList<Pair> patternIndex;
			int startIndex = 0;
			for (int i =0 ; i<initial_input.size(); i++) {
				String this_line = initial_input.get(i);
				if (this_line.contains(command_input)) {
					patternIndex = patternIndex(this_line, command_input);

					if ( patternIndex.get(0).first-5 > 0) {
						startIndex = patternIndex.get(0).first-5;
						//System.out.println("line: "+i+1 + ", start index: " + startIndex+1);
					}

					// remove all the patterns
					while (startIndex <= this_line.length()-6) {
						String target = this_line.substring(startIndex, startIndex+6);
						hashTable.simpleRemove(target,new Pair(i+1, startIndex+1));
						startIndex ++;
					}

					// replace initial line with new string with removed pattern
					String newString = removePattern(initial_input.get(i), patternIndex);
					initial_input.set(i, newString);

					// re-hash the new string
					if (newString.length() >= 6) {
						for (int j =0; j <= newString.length()-6; j++){
							String sub_str = newString.substring(j,j+6);
							Pair pair = new Pair(line_num, j+1);

							hashTable.insert(sub_str, pair);
						}
					}
				}
			}

			System.out.println(return_value);


		}else if (command_letter == '+') {
			for (int j =0; j <= command_input.length()-6; j++){
				String sub_str = command_input.substring(j,j+6);
				Pair pair = new Pair(line_num, j+1);

				hashTable.insert(sub_str, pair);
			}
			initial_input.add(command_input);

			System.out.println(line_num);
		}
		else throw new IOException();


	}
	public static String removePattern (String input, ArrayList<Pair> patternIndex) {
		StringBuilder str = new StringBuilder(input);
		int i =0 ;
		for (Pair pair : patternIndex) {
			str.delete(pair.first - 6*i, pair.second - 6*i);
			i++;
		}
		return str.toString();
	}

	public static ArrayList<Pair> patternIndex (String input, String pattern) {
		ArrayList<Integer> index = new ArrayList<>();
		StringBuilder str = new StringBuilder(input);
		ArrayList<Pair> result = new ArrayList<>();

		int j = input.indexOf(pattern);
		while (j >=0) {
			index.add(j);
			j = input.indexOf(pattern, j+1);
		}


		int HOLD = -1;
		for (int i = 0; i<index.size()-1; i++) {
			int curr = index.get(i);

			if (index.get(i+1) - curr > 6) {  //get rid from [HOLD] ~ [i + 5]
				if (HOLD > 0) {
					HOLD = -1;
					result.add(new Pair(HOLD, i+6));
				} else {
					result.add(new Pair(i, i+6));
					HOLD = -1;
				}
			} else {
				if (HOLD == -1) {
					HOLD = curr;
				}
			}
		}
		int k = index.get(index.size()-1);
		if (HOLD == -1) {
			result.add(new Pair(k,k+6 ));
		}else {
			result.add(new Pair(HOLD, k+6));
		}
		return result;
	}
}

class Pair implements Comparable<Pair> {
	public int first;
	public int second;
	public Pair(int first, int second) {
		this.first = first;
		this.second = second;
	}

	@Override
	public int compareTo(Pair o) {
		if (first < o.first) return -1;
		if (first > o.first) return 1;
		if (second < o.second) return -1;
		if (second > o.second) return 1;
		return 0;
	}
	public String toString() {
		String str = "(" + this.first + ", " + this.second + ")";
		return str;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Pair other = (Pair) obj;
		return Objects.equals(first, other.first) && Objects.equals(second, other.second);
	}

	@Override
	public int hashCode() {
		return Objects.hash(first, second);
	}
}

class HashTable<T extends Comparable<T>, K extends Comparable<K>> {
	public AVLTree[] slot;
	int numItems;

	public HashTable() {
		slot = new AVLTree[100];
		for (int i = 0; i < 100; i++) {
			slot[i] = new AVLTree<T,K>();
		}
		this.numItems = 0;
	}
	public void simpleRemove (T value, K pair) {
		int hash_num = hash(value);
		AVLNode<T, K> node = slot[hash_num].search(value);
		if (node.coordinates.size() == 1) {
			System.out.println("remove whole node");
			slot[hash_num].delete(value);
		} else {
			System.out.println("only remove coord");
			node.coordinates.remove(pair);
		}
	}

	// method
	private int hash(T value) {
		String key = value.toString();
		int sum = 0;
		for (char ch : key.toCharArray()) {
			sum += (int) ch;
		}
		return sum % 100;
	}
	public void insert(T value, K item) {
		int hashValue = hash(value);
		slot[hashValue].insert(value, item);
		numItems++;
	}

	public AVLNode<T,K> search (T value) {
		int hash_num = hash(value);
		AVLTree<T,K> tree = slot[hash_num];

		AVLNode<T,K> node = tree.search(value);
		if (node.value == null) { return null; }	// tree is empty or NIL return, not found
		return node;
	}
	// returns the AVL node corresponding to the given 'string' value

}


class AVLNode<T extends Comparable<T>, K extends Comparable<K>> {
	T value; //string with length 6
	int height;
	LinkedList<K> coordinates; //to handle values with same msg
	AVLNode<T, K> left, right;

	AVLNode(T String, AVLNode<T,K> lchild, AVLNode<T,K> rchild) {
		value = String;
		height = 1;
		coordinates = new LinkedList<>();
		left = lchild;
		right = rchild;
	}
	AVLNode(T String, AVLNode<T,K> lchild, AVLNode<T,K> rchild, int h) {
		value = String;
		height = h;
		coordinates = new LinkedList<>();
		left = lchild;
		right = rchild;
	}

}

class AVLTree<T extends Comparable<T>, K extends  Comparable<K>> {
	public AVLNode<T, K> root;
	public AVLNode<T,K> NIL = new AVLNode(null,null,null,0);
	public AVLTree() {
		root = NIL;
	}
	void delete(T value) {
		root = deleteNode(root, value);
	}

	private AVLNode<T, K> deleteNode(AVLNode<T, K> node, T value) {
		// Perform standard BST delete
		if (node == NIL) {
			return NIL;
		}

		int cmp = value.compareTo(node.value);
		if (cmp < 0) {
			node.left = deleteNode(node.left, value);
		} else if (cmp > 0) {
			node.right = deleteNode(node.right, value);
		} else {
			// Found the node to delete

			// Case 1: Node has no children or only one child
			if (node.left == NIL || node.right == NIL) {
				AVLNode<T, K> child = (node.left != NIL) ? node.left : node.right;
				if (child == NIL) {
					node = NIL; // Node is a leaf node
				} else {
					node = child; // Replace node with its child
				}
			}
			// Case 2: Node has two children
			else {
				AVLNode<T, K> successor = findSuccessor(node.right);
				node.value = successor.value;
				node.coordinates = successor.coordinates;
				node.right = deleteNode(node.right, successor.value);
			}
		}

		if (node != NIL) {
			// Update the height of the current node
			node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));

			// Perform AVL tree balancing if necessary
			node = balance(node);
		}

		return node;
	}

	private AVLNode<T,K> findSuccessor(AVLNode<T,K> node) {
		while (node.left != NIL) {
			node = node.left;
		}
		return node;
	}

	private AVLNode<T, K> balance(AVLNode<T, K> node) {
		// Perform AVL tree balancing
		int balanceFactor = getBalanceFactor(node);

		// Left-Left case
		if (balanceFactor > 1 && node.left.right.height <= node.left.left.height)
			return rightRotate(node);

		// Right-Right case
		if (balanceFactor < -1 && node.right.left.height <= node.right.right.height)
			return leftRotate(node);

		// Left-Right case
		if (balanceFactor > 1 && node.left.right.height > node.left.left.height) {
			node.left = leftRotate(node.left);
			return rightRotate(node);
		}

		// Right-Left case
		if (balanceFactor < -1 && node.right.left.height > node.right.right.height) {
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}

		return node;
	}
	public AVLNode<T, K> search(T target) {
		AVLNode<T, K> node = searchNode(root, target);
		return node;
	}

	// Recursive helper method to search for a value in the AVL Tree
	private AVLNode<T, K> searchNode(AVLNode<T, K> node, T target) {
		if (node == NIL || target.compareTo(node.value) == 0) {
			return node;
		}
		if (target.compareTo(node.value) < 0) {
			return searchNode(node.left, target);
		} else {
			return searchNode(node.right, target);
		}
	}

	// Get the height of a node
	private int getHeight(AVLNode<T, K> node) {
		return node.height;
	}

	// Update the height of a node
	private void updateHeight(AVLNode<T, K> node) {
		node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
	}

	// Get the balance factor of a node
	private int getBalanceFactor(AVLNode<T, K> node) {
		if (node == NIL)
			return 0;
		return getHeight(node.left) - getHeight(node.right);
	}

	// Perform a right rotation
	private AVLNode<T, K> rightRotate(AVLNode<T, K> node) {
		AVLNode<T, K> leftChild = node.left;
		AVLNode<T, K> rightGrandchild = leftChild.right;

		// Perform rotation
		leftChild.right = node;
		node.left = rightGrandchild;

		// Update heights
		updateHeight(node);
		updateHeight(leftChild);

		// Return the new root
		return leftChild;
	}

	// Perform a left rotation
	private AVLNode<T, K> leftRotate(AVLNode<T, K> node) {
		AVLNode<T, K> rightChild = node.right;
		AVLNode<T, K> leftGrandchild = rightChild.left;

		// Perform rotation
		rightChild.left = node;
		node.right = leftGrandchild;

		// Update heights
		updateHeight(node);
		updateHeight(rightChild);

		// Return the new root
		return rightChild;
	}

	// Insert a value into the AVL Tree
	public void insert(T value, K coord) {
		root = insertNode(root, value, coord);
	}

	// Recursive helper method to insert a value into the AVL Tree
	private AVLNode<T, K> insertNode(AVLNode<T, K> node, T value, K coord) {
		// Perform regular BST insertion

		int compareResult = 0;
		if (node != NIL) {
			compareResult = value.compareTo(node.value);
		}

		if (node == NIL){
			node = new AVLNode<>(value, NIL, NIL);
			node.coordinates.add(coord);

		} else if (compareResult < 0) {
			node.left = insertNode(node.left, value, coord);
		} else if (compareResult > 0) {
			node.right = insertNode(node.right, value, coord);
		} else {
			// Duplicate value found, add to linked list of duplicates
			node.coordinates.add(coord);
		}

		// Update the height of the current node
		updateHeight(node);

		return balance(node);
	}

	// Print the AVL Tree in preorder traversal
	public void print() {
		StringBuilder str = new StringBuilder();
		if (root == NIL) {
			System.out.println("EMPTY");
		} else {
			preorderHelper(root, str);
			String result = str.toString().replaceFirst(" ", "");
			System.out.println(result);
		}

	}

	// Recursive helper method to print the AVL Tree in preorder traversal
	private void preorderHelper(AVLNode<T, K> node, StringBuilder str) {
		if (node != NIL) {
			str.append(" ").append(node.value);
			preorderHelper(node.left, str);
			preorderHelper(node.right, str);
		}
	}

}