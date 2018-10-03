
public class Node {
	 int value;
	 Node leftChild;
	 Node rightChild;
	 int height = 1;
	 
	 public Node(int key){
		 value=key;
		 height=1;
		 
	 }
	 public void setLeft(Node lftChild){
		 leftChild=lftChild;
	 }
	 public void setRight(Node rghtChild){
		 rightChild=rghtChild;
	 }
	 public Node getLeftChild(){
		 return leftChild;
	 }
	 public Node getRightChild(){
		 return rightChild;
	 }
	 public void setValue(int val){
		 value=val;
	 }
	 public int getValue(){
		 return value;
	 }
	 
	 
}
