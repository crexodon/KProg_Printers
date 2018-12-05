package io;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import model.Station;
import model.TheObject;
import model.TheObject.TheObservable;
import model.TheObject.TheObservale;
import model.TheObject.TheObservebale;


/**
 * A class for printing statistics
 * 
 * @author Jaeger, Schmidt
 * @version 2015-11-18
 */
public class Statistics implements Observer{

	private static String buffer; 
	
	/** appends the given String to the buffer
	 *
	 * @param message the message to append
	 */
	public static void update(String message) {
		
		buffer = buffer + message + "\n";
	}
	
	/** writes the given String to console
	 *
	 * @param message the message to write to console
	 */
	public static void show(String message) {
		
		System.out.println(message);
	}
	
	public boolean isNewProcessTime(int newProcessTime) {
		return true;
	}
	
	public boolean newTheNextStation(Station newNextStation) {
		return true;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		TheObservable TheObservable= (TheObservable) arg0;
		Integer info = (Integer)arg1;
		int newProcessTime = info.intValue();
		if(isNewProcessTime(TheObservable.getProcessTime())){
			System.out.println(TheObservable.getObject()+"has  the new process time"+newProcessTime);
			TheObservable.deleteObservers();
		}
		if(newTheNextStation(TheObservable.getNextStation())) {
			System.out.println(TheObservable.getObject() +"has a new next station"+TheObservable.getNextStation());
			TheObservable.deleteObservers();
		}
	}


	
}
