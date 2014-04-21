package net.sav.algos;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;


public class AVLTree {

	static class Node {
		int key;
		int leftHeight;
		int rightHeight;
		Node left;
		Node right;
		
		Node(int key) { 
			this.key = key;
			this.leftHeight = 0;
			this.left = null;
			this.right = null;
		}

		int height() {
			return Math.max(leftHeight, rightHeight);
		}
	}
	
	Node root;
	
	public boolean isEmpty() {
		return root == null;
	}

	public void insert(int val) {
		this.insNode(new Node(val));
	}

	public void insNode(Node node) {	
		root = recInsNode(root, node);
	}	
	
	private Node recInsNode(Node localRoot, Node node) {
		if (localRoot == null) {
			return node;
		}
		if (node.key > localRoot.key) {
			localRoot.right = recInsNode(localRoot.right, node);
			localRoot.rightHeight = localRoot.right.height() + 1;
		} else {
			localRoot.left = recInsNode(localRoot.left, node);			
			localRoot.leftHeight = localRoot.left.height() + 1;
		}
		int diffHeight = localRoot.leftHeight - localRoot.rightHeight;
		
		if (diffHeight > 1 || diffHeight < -1) {
			// > 1 : unbalanced to the left
			// < -1 : unbalanced to the right
			localRoot = rebalance(localRoot, diffHeight);
		}
			
		return localRoot;
	}

	private Node rebalance(Node localRoot, int diffHeight) {
		Node p, t;
		int diffChildren;
		if (diffHeight > 0) {
			// balance left side
			diffChildren = localRoot.left.leftHeight - localRoot.left.rightHeight;
			if (diffChildren > 0) {
				return rebalanceSameSide(localRoot, diffHeight);
			}
			p = localRoot.left; // change its right 
			t = p.right; // change its left
			localRoot.left = t;
			p.right = t.left;
			p.rightHeight = t.leftHeight;
			t.left = p;
			t.leftHeight = p.height()+1;
			localRoot.leftHeight = t.height() +1;
			
		} else {
			// balance right side
			diffChildren = localRoot.right.leftHeight - localRoot.right.rightHeight;
			if (diffChildren < 0) {
				return rebalanceSameSide(localRoot, diffHeight);				
			}
			p = localRoot.right; // change its left
			t = p.left; // change its right
			localRoot.right = t;
			p.left = t.right;
			p.leftHeight = t.rightHeight;
			t.right = p;
			t.rightHeight = p.height()+1;
			localRoot.rightHeight = t.height()+1;
		}
		return rebalanceSameSide(localRoot, diffHeight);
	}
	
	// Rebalance RR or LL cases
	private Node rebalanceSameSide(Node localRoot, int diffHeight) {
		Node p;
		if (diffHeight > 0) {
			// balance left side
			p = localRoot.left;
			localRoot.left = p.right;
			localRoot.leftHeight = p.rightHeight;
			p.right = localRoot;
			p.rightHeight = localRoot.height()+1;
		} else {
			// balance right side
			p = localRoot.right;
			localRoot.right = p.left;
			localRoot.rightHeight = p.leftHeight;
			p.left = localRoot;
			p.leftHeight = localRoot.height()+1;
		}
		return p;
	}
	
	public void printTree() {
		recPrint(root, 0);
	}
	
	private void recPrint(Node node, int level) {		
		if (node == null) 
			System.out.println(StringUtils.repeat('\t', level) + level + ": -/");
		else {
			System.out.println(StringUtils.repeat('\t', level) + level + ":" + node.key + " [" + node.leftHeight + ", " + node.rightHeight + "]" );
			if (node.left != null || node.right != null) {
				recPrint(node.left, level+1);
				recPrint(node.right, level+1);
			}
		}
	}
	
	public boolean find(int key) {
		Node p = root;
		while (p != null) {
			if (key < p.key) {
				p = p.left;
			} 
			else if (key > p.key){
				p = p.right;
			} 
			else return true;
		}
		return false;
	}

	class RemovalRec {
		RemovalRec(Node node, boolean removed) {
			this.node = node;
			this.removed = removed;
		}
		Node node;
		boolean removed = false;
	}
	
	public boolean remove(int key) {
		RemovalRec rr = recRemove(root, key);
		root = rr.node;
		return rr.removed;
	}
	
	private Node recSeekRemove(Node node, Node target) {
		if (node.right != null) {
			node.right = recSeekRemove(node.right, target);
			node.rightHeight = (node.right == null) ? 0 : node.right.height()+1;
			return node;
		} else {
			target.key = node.key;
			return node.left;
		}
	}

	private RemovalRec recRemove(Node node, int key) {
		RemovalRec ret;
		if (node == null) {
			return new RemovalRec(null, false);
		}
		// found it
		if (node.key == key) {
			// if only child or leaf
			if (node.left == null || node.right == null) {
				if (node.left != null) {
					return new RemovalRec(node.left, true);
				} else if (node.right != null) {
					return new RemovalRec(node.right, true);
				} else 
					return new RemovalRec(null, true);
			}
			else {
				node.left = recSeekRemove(node.left, node); 
				node.leftHeight = node.left == null ? 0 : node.left.height()+1;
				ret = new RemovalRec(node.left, true);
			}
		} else if (key < node.key) {
			ret = recRemove(node.left, key);
			node.left = ret.node;
			node.leftHeight = ret.node == null ? 0 : ret.node.height()+1;
		
		} else {
			ret = recRemove(node.right, key);
			node.right = ret.node;
			node.rightHeight = ret.node == null ? 0 : ret.node.height()+1;
		}

		// if something was removed, check if needs rebalance
		if (ret.removed == true) {
			int diffHeight = node.leftHeight - node.rightHeight;
			
			if (diffHeight > 1 || diffHeight < -1) {
				// > 1 : unbalanced to the left
				// < -1 : unbalanced to the right
				node = rebalance(node, diffHeight);
			}
		}

		ret.node = node;
		return ret;
	}

///////
	
	public static void main(String [] args) {
		AVLTree tree =  new AVLTree();
		Random rndGen = new Random();
		
		for (int i : Arrays.asList(0,1,2,3,6,5,4)) {
			System.out.println(">> " + i);
			tree.insert(i);
			tree.printTree();
		}
		tree.remove(3);
		tree.printTree();
	}
	
}
