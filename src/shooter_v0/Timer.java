package shooter_v0;

import org.eclipse.swt.widgets.Display;

public class Timer {
	private Runnable engine;
	private boolean running = false;

	public Timer(Display display, int interval, Runnable target) {
		engine = new Runnable() {
			public void run() {
				if (running) {
					target.run();
					display.timerExec(interval, this);
				}
			}
		};
	}

	public void start() {
		running=true;
		engine.run();
	}

	public void stop() {
		running=false;
	}
}
