package io;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import model.ProcessStation;
import model.TheObject;
import view.StatisticsView;

import model.Station;
import model.TheObject.TheObservable;
import java.util.Observable;
import java.util.Observer


/**
 * A class for printing statistics
 *
 * @author Jaeger, Schmidt / Team 12
 * @version 2018-12-06
 */
public class Statistics extends Thread implements Observer {

	private static String buffer;

	private static StatisticsView statView;

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


	/**
	 * Builds an Array filled with all the Statistics of Stations and Objects
	 */
	public static void buildAndSendPrintMessage() {

		// Initialize needed Arrays
		ArrayList<ProcessStation> allStations = ProcessStation.getAllProcessStations();
		ArrayList<TheObject> allObjects = TheObject.getAllObjects();
		ArrayList<String> allMessages = new ArrayList<String>();

		// empty String to get a line since \n doesn�t work
		String empty = " ";

		// Iterate through all Stations
		for (ProcessStation station : allStations) {
			// Build Stringdata for each Station
			String statTyp = "Station Typ: " + station.getLabel();
			String statVis = "Anzahl der behandelten Objekte: " + station.getNumberOfVisitedObjects();
			String statTime = "Zeit zum Behandeln aller Objekte: " + station.getInUseTime();
			String statAVG = "Durchnittliche Behandlungsdauer: " + station.getAvgTreatmentTime();
			String statPeek = "Erfuhr einen Peek von: " + station.getInQueuePeek();

			// add Stringdata to Message Array
			allMessages.add(statTyp);
			allMessages.add(statVis);
			allMessages.add(statTime);
			allMessages.add(statAVG);
			allMessages.add(statPeek);
			allMessages.add(empty);
		}

		// Iterate through all Objects
		for (TheObject object : allObjects) {
			// Build Stringdata for each Object
			String objectName = "Objekt: " + object.getLabel();
			String objectTime = "Zeit zum Behandeln des Objekts: " + object.getTreatmentTime();

			// add Stringdata to Message Array
			allMessages.add(objectName);
			allMessages.add(objectTime);
			allMessages.add(empty);
		}

		// send Message array to printing Method
		print(allMessages);
	}

	/**
	 * prints the given String to a Document
	 * 
	 * @param allMessages ArrayList of Strings to be Written on a Document
	 */
	public static void print(ArrayList<String> allMessages) {

		try {

			// Create the file and writer
			File file = new File("Statistics.txt");
			FileWriter fw = new FileWriter(file.getName());
			PrintWriter writer = new PrintWriter(fw);

			// Iterate through built Message array
			for (String message : allMessages) {
				// write each message into a line
				writer.println(message);
			}
			// close the writer
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
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
		// TODO Auto-generated method stub
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
}
