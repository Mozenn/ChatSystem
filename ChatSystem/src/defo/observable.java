package defo;

public interface observable {
	
	void addObserver(Observer o); 
	
	void removeObserver(Observer o); 
	
	void notifyObservers(); 

}
