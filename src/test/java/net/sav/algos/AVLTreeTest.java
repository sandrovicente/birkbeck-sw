package net.sav.algos;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class AVLTreeTest {
	AVLTree avlTree;
	
	@Before
	public void startUp () {
		avlTree = new AVLTree();
	}
	
	@Test 
	public void shouldCreateAvlTree() {
		Assert.assertNotNull(avlTree);
		Assert.assertTrue(avlTree.isEmpty());
	}
	
	@Test
	public void shouldInsertElement() {
		avlTree.insert(9);
		
		Assert.assertFalse(avlTree.isEmpty());
	}

	@Test
	public void shouldNotFindNonExistingNode() {
		Assert.assertFalse(avlTree.find(10));
	}
	
	@Test
	public void shouldFindExistingOneNode() {
		avlTree.insert(10);
		Assert.assertTrue(avlTree.find(10));
	}
	
	@Test
	public void shouldFindOnlyExistingNodeInTreeWithSomeElements() {
		List<Integer> elements = Arrays.asList(0,1,2,3,4,5,6,7,8,9,19,18,17,16,15,14,13,12,11,10);
		for (int i : elements) {
			avlTree.insert(i);
		}

		// walk on the list on reverse order
		ListIterator<Integer> lit = elements.listIterator(elements.size());
		while (lit.hasPrevious()) {
			Assert.assertTrue(avlTree.find(lit.previous()));
		}

		Assert.assertFalse(avlTree.find(21));
	}
		
	@Test
	public void shouldNoOpRemoveExistingElementFromEmptyTree() {
		Assert.assertFalse(avlTree.remove(20));
	}

}
