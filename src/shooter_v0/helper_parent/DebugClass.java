package shooter_v0.helper_parent;

public abstract class DebugClass {

	private final int MAX_CLASSNAME_LENGHT = 15;
	private final int MAX_THREADNAME_LENGTH = 25;
	protected int DEBUG_LEVEL=1;

	public synchronized void print(String text, int debugPriority) {
		if (debugPriority<=DEBUG_LEVEL) {
		String className = this.getClass().getName();
		className = className.substring(className.lastIndexOf(".") + 1, className.length());

		System.out.print("   thread: " + Thread.currentThread().getName());
		for (int i = 0; i < (MAX_THREADNAME_LENGTH - Thread.currentThread().getName().length()); i++)
			System.out.print("-");
		System.out.print("class:" + className);
		for (int i = 0; i < (MAX_CLASSNAME_LENGHT - className.length()); i++)
			System.out.print("-");
		System.out.println("  " + text);
		}
	}
	public synchronized void print(String text) {
		print(text,0);
	}

}
