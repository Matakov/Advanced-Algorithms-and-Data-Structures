/**
 * @author Franjo
 *
 */
import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void displayTree(Node root)
	{
		Stack globalStack = new Stack();
		globalStack.push(root);	
		int emptyLeaf = 32;
		boolean isRowEmpty = false;
		System.out.println("****......................................................****");
		while(isRowEmpty==false)
		{

			Stack localStack = new Stack();
			isRowEmpty = true;
			for(int j=0; j<emptyLeaf; j++)
				System.out.print(' ');
			while(globalStack.isEmpty()==false)
			{
				Node temp = (Node) globalStack.pop();
				if(temp != null)
				{
					System.out.print(temp.value);
					localStack.push(temp.leftChild);
					localStack.push(temp.rightChild);
					if(temp.leftChild != null ||temp.rightChild != null)
						isRowEmpty = false;
				}
				else
				{
					System.out.print("--");
					localStack.push(null);
					localStack.push(null);
				}
				for(int j=0; j<emptyLeaf*2-2; j++)
					System.out.print(' ');
			}
			System.out.println();
			emptyLeaf /= 2;
			while(localStack.isEmpty()==false)
				globalStack.push( localStack.pop() );
		}
	System.out.println("****......................................................****");
} 
	
	
	public static void preOrder(Node node){
		if(node!= null){
			System.out.print(node.value + " ");
			preOrder(node.getLeftChild());
			preOrder(node.getRightChild());
		}
		
	}
	
	public static void byLevel(Node root){
	     Queue<Node> level  = new LinkedList<>();
	     if (root==null){
	    	 return;
	     }
	     int lvl = root.height;
	     level.add(root);
	     while(!level.isEmpty()){
	         Node node = level.poll();
	         if (lvl!=node.height){
	        	 lvl=node.height;
	        	 System.out.println(" ");
	         }
	         System.out.print(node.value + " " + node.height + " ");
	         if(node.leftChild!= null)
	         level.add(node.leftChild);
	         if(node.rightChild!= null)
	         level.add(node.rightChild);
	     }
	}
	
	public static void byLevel2(Node root){
	     Queue<Node> level  = new LinkedList<>();
	     Queue<Node> level2  = new LinkedList<>();
	     if (root==null){
	    	 return;
	     }
	     int lvl = root.height;
	     level.add(root);
	     while(!level.isEmpty() || !level2.isEmpty()){
	    	 //prvi
	    	 System.out.println(" ");
	    	 while(!level.isEmpty()){
	    		 Node node = level.poll();
		         System.out.print(node.value + " ");
		         if(node.leftChild!= null) level2.add(node.leftChild);
		         if(node.rightChild!= null) level2.add(node.rightChild);
		     }
	    	  
	    	 
	         
	         //drugi
	    	 System.out.println(" ");
	         while(!level2.isEmpty()){
	        	 Node node = level2.poll();
		         
		         System.out.print(node.value + " ");
		         if(node.leftChild!= null) level.add(node.leftChild);
		         if(node.rightChild!= null) level.add(node.rightChild); 
		     }
	         
	         
	     }
	}
	
	
	
	public static int getOrder(String input){
		switch(input){
			case "Insert":
			case "In":
			case "insert":
			case "in":
			case "i":
				return 1;
			case "Remove":
			case "remove":
			case "r":
				return 2;
			case "Exit": case "exit":
			default:
				return 0;
		}
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length < 1 || args.length > 1){
				System.out.println("When starting app arguments must contain input file!");
				System.exit(0);
		}
		AVLTree tree = new AVLTree();
	    BufferedReader reader = new BufferedReader(new FileReader(args[0]));
	    String c;
	    while((c = reader.readLine()) != null) {
	    	 String[] parts = c.split(" ");
	         for(int i=0;i<parts.length;i++){
	        	 tree.root = tree.insert(tree.root, Integer.parseInt(parts[i]));
				 //System.out.println("\n");
				 //preOrder(tree.root);
				 }
				  
		}
		reader.close();
		 
		//System.out.println("\nThe preorder traversal of constructed tree is : ");
	    //preOrder(tree.root);
		System.out.println("\nThe level traversal of constructed tree is : ");
		//byLevel2(tree.root);
		displayTree(tree.root);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    String type;
	    String inputdata;
	    int vary;;
	    while(true){
	    	System.out.println("\nDo you want to insert data, remove data or exit?");	    	
	    	type=in.readLine();
	    	vary = getOrder(type);
	    	
	    	if(vary==1){
	    		System.out.println("\nEnter data to add: ");
	    		inputdata=in.readLine();
		    	tree.root = tree.insert(tree.root, Integer.valueOf(inputdata));
				//System.out.println("\nThe preorder traversal of constructed tree is : ");
			    //preOrder(tree.root);
			    System.out.println("\nThe level traversal of constructed tree is : ");
			    //byLevel2(tree.root);
			    displayTree(tree.root);
	    	} else if(vary==2){
		    	System.out.println("\nEnter data to delete: ");
			    inputdata=in.readLine();
				tree.root = tree.deletion(tree.root, Integer.valueOf(inputdata));
				//System.out.println("\nThe preorder traversal of constructed tree is : ");
			    //preOrder(tree.root);
				System.out.println("\nThe level traversal of constructed tree is : ");
				//byLevel2(tree.root);
				displayTree(tree.root);
	    	}else{
			    System.out.println("Exiting...");
				return;
	    	}
	    }
	     
	}

}
