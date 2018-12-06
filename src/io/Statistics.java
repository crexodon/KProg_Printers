package io;

import view.StatisticsView;

/**
 * A class for printing statistics
 * 
 * @author Jaeger, Schmidt
 * @version 2015-11-18
 */
public class Statistics extends Thread {

	private static String buffer;

	private static StatisticsView statView;

	/**
	 * appends the given String to the buffer
	 *
	 * @param message the message to append
	 */
	public static void update(String message) {

		buffer = buffer + message + "\n";
	}

	/**
	 * writes the given String to console
	 *
	 * @param message the message to write to console
	 */
	public static void show(String message) {

		System.out.println(message);
	}

	/**
	 * Creates a new JFrame for Statistics
	 * 
	 * @param label
	 */
	public void startView(String label) {
		statView = new StatisticsView(label);
	}

	/**
	 * Updates the existing statistics JFrame
	 * 
	 */
	public static void updateView() {
		if (statView != null) {
			statView.updateFrame();
		}
	}

}
