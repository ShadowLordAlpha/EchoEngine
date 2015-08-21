package io.github.cybernetic_shadow.core;

public class Main {

	public static void main(String[] args) {
		System.out.println(Thread.currentThread().getId());
		new Core().start(new Application());
	}
}
