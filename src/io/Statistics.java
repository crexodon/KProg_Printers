package io;

import model.Station;
import model.TheObject.TheObservable;

import java.util.Observable;
import java.util.Observer;

import view.StatisticsView;
/**
 * A class for printing statistics
 *
 * @author Jaeger, Schmidt / Team 12
 * @version 2018-12-06
 */
public class Statistics extends Thread implements Observer{

	private static String buffer;
  
  private static StatisticsView statView;

	public static void update(String message) {

		buffer = buffer + message + "\n";
	}

	/** writes the given String to console
   *
	 *
	 * @param message the message to write to console
	 */
	public static void show(String message) {

		System.out.println(message);
	}

	//useful: check new process time
	public boolean isNewProcessTime(int newProcessTime) {
		return true;
	}

	//useful: check the new next station
	public boolean newTheNextStation(Station newNextStation) {
		return true;
	}

	/** reaction to notification
	 * @param o the observed object, here: a object or also a TheObservable
	 * @param p some information the object (TheObservable) sent along
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		TheObservable TheObservable= (TheObservable) arg0;
		Integer info = (Integer)arg1;
		int newProcessTime = info.intValue();
		// recognize the situation
		if(isNewProcessTime(TheObservable.getProcessTime())){
			System.out.println(TheObservable.getObject()+"has  the new process time"+newProcessTime);
			TheObservable.deleteObservers();
		}
		else if(newTheNextStation(TheObservable.getNextStation())) {
			System.out.println(TheObservable.getObject() +"has a new next station"+TheObservable.getNextStation());
			TheObservable.deleteObservers();
		}
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
