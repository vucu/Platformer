package platformer.services;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import platformer.datastructures.Position;
import platformer.gameobject.properties.ITransform;
import platformer.services.delegates.Remover;

public class GridMapService {
	public static final int EMPTY = 0;
	public static final int UNBREAKABLE_WALL = 1;
	public static final int BREAKABLE_WALL = 2;
	public static final int PLAYER = -1;

	private final Remover remover;

	private final int[][] state;
	private final int nRows;
	private final int nColumns;
	private final int gridSize;

	private Map<ITransform, Integer> objects = new HashMap<>();

	// Generate a gridmap to generate obstacles
	// Avoid tank positions
	public GridMapService(int gridSize, Rectangle world, Remover remover, Position tank1, Position tank2) {
		this.gridSize = gridSize;
		this.remover = remover;

		nRows = (int) world.getHeight() / gridSize;
		nColumns = (int) world.getWidth() / gridSize;

		state = new int[nRows][nColumns];

		randomize(tank1, tank2);
	}

	private void randomize(Position tank1, Position tank2) {
		// Make some random walls
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nColumns; j++) {
				double r = Math.random();
				state[i][j] = EMPTY;
				if (r > 0.5 && r < 0.51)
					state[i][j] = UNBREAKABLE_WALL;
				if (r > 0.8 && r < 1)
					state[i][j] = BREAKABLE_WALL;
			}
		}

		// If this is the boundary, guarantee to have unbreakable wall there
		for (int i = 0; i < nRows; i++) {
			state[i][0] = UNBREAKABLE_WALL;
			state[i][nColumns - 1] = UNBREAKABLE_WALL;
		}
		for (int j = 0; j < nColumns; j++) {
			state[0][j] = UNBREAKABLE_WALL;
			state[nRows - 1][j] = UNBREAKABLE_WALL;
		}

		// If there is a tank, guarantee no wall there
		int r = tank1.y / gridSize;
		int c = tank1.x / gridSize;
		removeWallsAround(r, c);

		r = tank2.y / gridSize;
		c = tank2.x / gridSize;
		removeWallsAround(r, c);
	}

	private void removeWallsAround(int r, int c) {
		for (int i = r - 1; i <= r + 1; i++) {
			for (int j = c - 1; j <= c + 1; j++) {
				state[i][j] = EMPTY;
			}
		}
	}

	// *** Interface for factory ***
	public int getRowCount() {
		return nRows;
	}

	public int getColumnCount() {
		return nColumns;
	}

	public int getState(int row, int col) {
		return state[row][col];
	}

	public Position getWorldPosition(int row, int col) {
		int y = row * gridSize + gridSize / 2;
		int x = col * gridSize + gridSize / 2;
		return new Position(x, y);
	}

	// *** Interface for components
	private int getRow(Position worldPosition) {
		return (worldPosition.y - gridSize / 2) / gridSize;
	}

	private int getCol(Position worldPosition) {
		return (worldPosition.x - gridSize / 2) / gridSize;
	}

	public void update() {
		// Remove destroyed objects
		remover.removeIfDestroyed(objects.keySet());

		// Re-initialize the states to empty
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nColumns; j++) {
				state[i][j] = EMPTY;
			}
		}

		// Update the state based on the objects
		objects.forEach((object, type) -> {
			Position position = object.getPosition();
			int r = this.getRow(position);
			int c = this.getCol(position);
			state[r][c] = type;
		});
	}

	// *** Interface for objects ***
	public void register(ITransform object, int type) {
		objects.put(object, type);
	}
}
