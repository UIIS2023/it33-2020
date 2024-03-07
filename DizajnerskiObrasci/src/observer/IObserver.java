package observer;

public interface IObserver {
	
	public void update(boolean modify, boolean delete,boolean undo, boolean redo);
	
}
