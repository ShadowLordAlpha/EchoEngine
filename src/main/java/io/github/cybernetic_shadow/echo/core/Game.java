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
	 * This method should be used to update the physics in a game
	 */
	public void update() {
		
	}
	
	/**
	 * This method should be used to guess the physics in a game using interpolation or extrapolation.
	 * It should not be used to update the physics as it has a variable time-step unlike the update method and can cause
	 * strange things to happen with floating point numbers.
	 */
	public void polate() {
		
	}
}
