package platformer.services;

import java.util.HashSet;
import java.util.Set;

public class KeyboardService {
	private Set<Integer> keysPressed;
	private Set<Integer> keysReleased;
	private Set<Integer> keysHold;

	public KeyboardService() {
		keysPressed = new HashSet<>();
		keysReleased = new HashSet<>();
		keysHold = new HashSet<>();
	}

	public void pressKey(int keyCode) {
		keysPressed.add(keyCode);
		keysHold.add(keyCode);
	}

	public void releaseKey(int keyCode) {
		keysReleased.add(keyCode);
		keysHold.remove(keyCode);
	}

	public void clear() {
		keysPressed = new HashSet<>();
		keysReleased = new HashSet<>();
	}

	public Set<Integer> keysHold() {
		return keysHold;
	}

	public Set<Integer> keysPressed() {
		return keysPressed;
	}

	public Set<Integer> keysReleased() {
		return keysReleased;
	}

	public boolean check(int keyCode) {
		return keysHold.contains(keyCode);
	}

	public boolean checkPressed(int keyCode) {
		return keysPressed.contains(keyCode);
	}

	public boolean checkReleased(int keyCode) {
		return keysReleased.contains(keyCode);
	}

	public boolean checkAny() {
		return !keysHold.isEmpty();
	}

	public boolean checkAnyPressed() {
		return !keysPressed.isEmpty();
	}

	public boolean checkAnyReleased() {
		return !keysReleased.isEmpty();
	}
}