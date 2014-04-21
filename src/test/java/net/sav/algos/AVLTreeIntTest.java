package net.sav.algos;

import java.util.Arrays;

import net.sav.algos.AVLTree.Node;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AVLTreeIntTest {

	AVLTree avlTree;
	
	@Before
	public void startUp () {
		avlTree = new AVLTree();
	}
	
	private void assertNodeAndHeights(AVLTree.Node node, int keyExpected, int leftHExpected, int rightHExpected) {
		Assert.assertEquals(keyExpected, node.key);
		Assert.assertEquals(leftHExpected, node.leftHeight);
		Assert.assertEquals(rightHExpected, node.rightHeight);
	}
	
	@Test
	public void shouldCreateFirstNode() {
		Assert.assertNull(avlTree.root);
		avlTree.insNode(new AVLTree.Node(10));
		Assert.assertNotNull(avlTree.root);
		assertNodeAndHeights(avlTree.root, 10, 0, 0);
	}
	
	@Test
	public void shouldCreateNodeOnLeft() {
		avlTree.insNode(new AVLTree.Node(10));
		avlTree.insNode(new AVLTree.Node(9));
		
		AVLTree.Node testRoot = avlTree.root;

		assertNodeAndHeights(testRoot, 10, 1, 0);
		assertNodeAndHeights(testRoot.left, 9, 0, 0);
	}
	
	@Test
	public void shouldCreateNodeOnRight() {
		avlTree.insNode(new AVLTree.Node(10));
		avlTree.insNode(new AVLTree.Node(11));
		
		AVLTree.Node testRoot = avlTree.root;

		assertNodeAndHeights(testRoot, 10, 0, 1);
		assertNodeAndHeights(testRoot.right, 11, 0, 0);
	}
	
	@Test
	public void shouldCreateNodeSOnRightAndLeft() {
		avlTree.insNode(new AVLTree.Node(10));
		avlTree.insNode(new AVLTree.Node(11));
		avlTree.insNode(new AVLTree.Node(9));
		
		AVLTree.Node testRoot = avlTree.root;

		assertNodeAndHeights(testRoot, 10, 1, 1);
		assertNodeAndHeights(testRoot.right, 11, 0, 0);
		assertNodeAndHeights(testRoot.left, 9, 0, 0);
	}
	
	@Test
	public void shouldCreateNodeOnLeft2() {
		
		// balanced tree
		avlTree.insNode(new AVLTree.Node(10));
		avlTree.insNode(new AVLTree.Node(11));
		avlTree.insNode(new AVLTree.Node(9));
	
		// far left
		avlTree.insNode(new AVLTree.Node(8));
		
		AVLTree.Node testRoot = avlTree.root;
		
		assertNodeAndHeights(testRoot, 10, 2, 1);

		// far left node
		AVLTree.Node testFarLeft = testRoot.left.left;

		assertNodeAndHeights(testFarLeft, 8, 0, 0);
	}
	
	@Test 
	public void shouldCreateNode2LevelsDownOnLeftAndRebalance() {
		avlTree.insNode(new AVLTree.Node(10));
		avlTree.insNode(new AVLTree.Node(9));
		avlTree.insNode(new AVLTree.Node(8));
	
		assertNodeAndHeights(avlTree.root, 9, 1, 1);
		assertNodeAndHeights(avlTree.root.left, 8, 0, 0);
		assertNodeAndHeights(avlTree.root.right, 10, 0, 0);
	}

	@Test
	public void shouldCreateNode2LevelsDonwOnRightAndRebalance() {
		avlTree.insNode(new AVLTree.Node(8));
		avlTree.insNode(new AVLTree.Node(9));
		avlTree.insNode(new AVLTree.Node(10));
		
		assertNodeAndHeights(avlTree.root, 9, 1, 1);
		assertNodeAndHeights(avlTree.root.left, 8, 0, 0);
		assertNodeAndHeights(avlTree.root.right, 10, 0, 0);
	}

	@Test
	public void shouldCreateNode2LevelsDonwOnLeftThenOnRightAndRebalance() {
		avlTree.insNode(new AVLTree.Node(10));
		avlTree.insNode(new AVLTree.Node(8));
		avlTree.insNode(new AVLTree.Node(9));
		
		assertNodeAndHeights(avlTree.root, 9, 1, 1);
		assertNodeAndHeights(avlTree.root.left, 8, 0, 0);
		assertNodeAndHeights(avlTree.root.right, 10, 0, 0);
	}
	
	@Test 
	public void shouldCreateNode2LevelsDownOnRightThenOnLeftAndRebalance() {
		avlTree.insNode(new AVLTree.Node(10));
		avlTree.insNode(new AVLTree.Node(12));
		avlTree.insNode(new AVLTree.Node(11));
		
		assertNodeAndHeights(avlTree.root, 11, 1, 1);
		assertNodeAndHeights(avlTree.root.left, 10, 0, 0);
		assertNodeAndHeights(avlTree.root.right, 12, 0, 0);
	}

	@Test
	public void shouldRemoveElementInRoot() {
		avlTree.insert(10);
		Assert.assertTrue(avlTree.remove(10));	
		Assert.assertNull(avlTree.root);
	}

	@Test
	public void shouldRemoveElementUnderRoot() {
		avlTree.insert(10);
		avlTree.insert(9);
		
		Assert.assertTrue(avlTree.remove(9));	
		Assert.assertNotNull(avlTree.root);
		Assert.assertNull(avlTree.root.left);
		assertNodeAndHeights(avlTree.root, 10, 0, 0);
	}

	@Test
	public void shouldRemoveElementUnderRoot2() {
		avlTree.insert(10);
		avlTree.insert(12);
		
		Assert.assertTrue(avlTree.remove(12));	
		Assert.assertNotNull(avlTree.root);
		Assert.assertNull(avlTree.root.right);
		assertNodeAndHeights(avlTree.root, 10, 0, 0);
	}
	
	@Test
	public void shouldRemoveRootAndMakeChildNewRoot() {
		avlTree.insert(10);
		avlTree.insert(9);
		
		Assert.assertTrue(avlTree.remove(10));	
		Assert.assertNotNull(avlTree.root);
		Assert.assertNull(avlTree.root.left);
		assertNodeAndHeights(avlTree.root, 9, 0, 0);
	}

	@Test
	public void shouldRemoveRootAndMakeChildNewRoot2() {
		avlTree.insert(10);
		avlTree.insert(12);
		
		Assert.assertTrue(avlTree.remove(10));	
		Assert.assertNotNull(avlTree.root);
		Assert.assertNull(avlTree.root.right);
		assertNodeAndHeights(avlTree.root, 12, 0, 0);
	}

	@Test
	public void shouldRemoveRootWith2Children() {
		avlTree.insert(10);
		avlTree.insert(12);
		avlTree.insert(8);
		
		Assert.assertTrue(avlTree.remove(10));	
		Assert.assertNull(avlTree.root.left);
		assertNodeAndHeights(avlTree.root, 8, 0, 1);
	}	

	@Test
	public void shouldRemoveRootWithChildrenAndRebalance() {
		avlTree.insert(10);
		avlTree.insert(12);
		avlTree.insert(13);
		avlTree.insert(8);
		
		Assert.assertTrue(avlTree.remove(10));	
		assertNodeAndHeights(avlTree.root, 12, 1, 1);
	}	
	
	@Test
	public void shouldRemoveFromLeavesAndRebalance() {
		for (int i : Arrays.asList(0,1,2,3,6,5,4)) {
			avlTree.insert(i);
		}		
		avlTree.remove(2);
		avlTree.remove(0);
		avlTree.remove(1);
		
		assertNodeAndHeights(avlTree.root, 4, 1, 2);		
	}
	
}
