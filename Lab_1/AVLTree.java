public class AVLTree {

	static Node root;
	
	
	//Pomoæna funkcija za raèunanje visine èvora
	int height(Node N){
		if(N==null){
			return 0;
		}
		return N.height;
	}
	
	//Pomoæna funkcija za dobivanje veæega od 2 integera
	int max(int a,int b){
		return (a > b) ? a : b;
	}
	
	//Funkcija za desno rotiranje
	Node rightRotation(Node y){
		//System.out.println("We are doing a right rotation");
		Node LC = y.leftChild;
		Node RCofLC = LC.rightChild;
		
		//izvedi rotaciju
		LC.setRight(y);
		y.setLeft(RCofLC);
				
		//izracunaj nove visine
		y.height=max(height(y.leftChild),height(y.rightChild))+1;
		LC.height=max(height(LC.leftChild),height(LC.rightChild))+1;
		
		//vrati novi korijen
		return LC;
		
	}
	
	Node leftRotation(Node y){
		//System.out.println("We are doing a left rotation");
		//System.out.println("Right child of a node is:"+y.rightChild);
		Node RC = y.rightChild;
		//System.out.println("Left child of a right child is:"+RC.leftChild);
		Node LCofRC = RC.leftChild;
		
		
		//izvedi rotaciju
		y.setRight(LCofRC);
		RC.setLeft(y);
		
				
		//izracunaj nove visine
		y.height=max(height(y.leftChild),height(y.rightChild))+1;
		RC.height=max(height(RC.leftChild),height(RC.rightChild))+1;
		
		return RC;
	}
	
	
	//uravnotezenost stabla
	int getBalance(Node N){
		if(N==null){
			return 0;
		}
		return height(N.leftChild)-height(N.rightChild);
	}
	
	Node insert(Node node, int key){
		//Obavi obican insert
		if(node == null){
			//System.out.println("New root is "+key);
			return (new Node(key));
		}
		if(node.getValue()==key){
			System.out.println("The value is already in the tree");
			return node;
		}
		if(node.getValue()>key){
				//System.out.println("Go left");
				node.leftChild = insert(node.leftChild,key);
		}
		else{
				//System.out.println("Go right");
				node.rightChild = insert(node.rightChild,key);
		}
		
		//2. Updejtaj visinu roditelja cvora
		node.height = max(height(node.leftChild), height(node.rightChild)) + 1;
		
		/* 3. Get the balance factor of this ancestor node to check whether
        this node became unbalanced */
       int balance = getBalance(node);
		
       // Ako cvor nije balansiran, tada postoje 4 slucajeva
       // Left Left Case
       if (balance > 1 && key < node.leftChild.value) {
           return rightRotation(node);
       }

       // Right Right Case
       if (balance < -1 && key > node.rightChild.value) {
           return leftRotation(node);
       }

       // Left Right Case
       if (balance > 1 && key > node.leftChild.value) {
           node.leftChild = leftRotation(node.leftChild);
           return rightRotation(node);
       }

       // Right Left Case
       if (balance < -1 && key < node.rightChild.value) {
           node.rightChild = rightRotation(node.rightChild);
           return leftRotation(node);
       }

       /* return the (unchanged) node pointer */
       return node;
       
	}
	
	
	//Funkcija da vrati list u stablu sa najmanjom vrijednosti --> najlijeviji list
	Node minValueNode(Node node){
		Node current = node;
		while (current.leftChild != null){
			current=current.leftChild;
		}
		return current;
		
	}
	
	//Funkcija za brisanje èvorova
	Node deletion(Node node,int key){
		if (node == null){
			return node;
		}
		//ako je vrijednost manja od vrijednosti cvora, onda je u lijevom podstablu
		if (key<node.value){
			node.leftChild=deletion(node.leftChild,key);
		}
		//ako je vrijednostveca od vrijednosti cvora, onda je u desnom podstablu
		else if (key > node.value){
			node.rightChild=deletion(node.rightChild,key);
		}
		else{
			//cvor ima jedno dijete
			System.out.println("Pronasao sam kljuc!");
			if((node.leftChild==null) || (node.rightChild==null)){
				Node temp=null;
				if (node.leftChild==null){
					temp = node.rightChild;
				}
				else{
					temp = node.leftChild;
				}
				//cvor nema diece
				if (temp==null){
					temp = node;
					node = null;
				}
				else {
					node = temp;
				}
			}
			else {
				//Cvor ima oba djetete
				Node temp = minValueNode(node.rightChild);
				
				node.value=temp.value;
				
				node.rightChild = deletion(node.rightChild,temp.value);
			}
			
			
		}
		
		if(node == null){
			return node;
		}
		
		//prilagodi visinu trenutnog cvora
		node.height=max(height(node.leftChild),height(node.rightChild))+1;
		
		
		//provjeri uskladnost cvora
		int balance = getBalance(node);
		
		// If this node becomes unbalanced, then there are 4 cases
	    // Left Left Case
	    if (balance > 1 && getBalance(node.leftChild) >= 0) {
	    	return rightRotation(node);
	    }
	 
	    // Left Right Case
	    if (balance > 1 && getBalance(node.leftChild) < 0) {
	    	node.leftChild = leftRotation(node.leftChild);
	        return rightRotation(node);
	    }
	 
	    // Right Right Case
	    if (balance < -1 && getBalance(node.rightChild) <= 0) {
	    	return leftRotation(node);
	    }
	 
	    // Right Left Case
	    if (balance < -1 && getBalance(node.rightChild) > 0) {
	    	node.rightChild = rightRotation(node.rightChild);
	        return leftRotation(node);
	    }
	 
	    
		
		return node;
		
		
	}
}
