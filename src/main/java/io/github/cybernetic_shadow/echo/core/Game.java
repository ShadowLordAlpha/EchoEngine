package io.github.cybernetic_shadow.echo.core;

public abstract class Game {

	/**
	 * This Method is run before the splash screen is displayed but after GLFW and LWJGL 3 are initialized.
	 */
	public void preInit() {

	}

	/**
	 * This Method is run while the splash screen is displayed.
	 */
	public void init() {

	}

	/**
	 * This Method is run after the splash screen is closed and the main screen is displayed.
	 */
	public void postInit() {

	}

	/**
	 * This method renders its contents to the screen
	 */
	public void render() {
		
	}

	/**
	 * This method should be used to update the physics in a game. When normally running this method will have a
	 * constant Delta Time however if a constant time is not needed it is recommended to run this method from the polate
	 * method instead of creating another update type method. If a constant time is needed use polate to interpolate or
	 * extrapolate the position that the objects should be displayed in the render stage to avoid temporal aliasing.
	 * @param deltaTime 
	 */
	public void update(double deltaTime) {
		
	}

	/**
	 * This method should be used to guess the physics in a game using interpolation or extrapolation. It should not be
	 * used to update the physics as it has a variable time-step unlike the update method and can cause strange things
	 * to happen with floating point numbers.
	 * @param accumulator 
	 */
	public void polate(double accumulator) {
		
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
