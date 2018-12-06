package model;

import controller.Simulation;
import io.Statistics;
import view.TheObjectView;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
/**
 * Class for the objects
 *
 * @author Jaeger, Schmidt
 * @version 2016-07-08
 */


final public class TheObject extends Actor {
	/** the quality of objects*/
	private static int  numbersOfObjects = 16;

	/** the view of the object */
	public TheObjectView theView;

	/** the process time of the object */
	private int processTime;

	/** the speed of the object, the higher the lower */
	private int mySpeed;

	/** all the station (labels) where the object have to go to */
	private ArrayList<String> stationsToGo = new ArrayList<String>();

	/**
	 * a pointer to the actual position of the stationsToGo list, start position is
	 * 0
	 */
	private int stationListPointer = 0;

	/** list of all objects */
	private static ArrayList<TheObject> allObjects = new ArrayList<TheObject>();

	/**
	 * the actual station where this object is in, null if it's not in a station or
	 * a stations queue
	 */
	private Station actualStation = null;

  
	/**
	 * the next station where this object will be in, null if it's not in a station or
	 * a stations queue
	 */
	private Station theNextStation = null;

	/** the instance of our static inner Measurement class */
	Measurement measurement = new Measurement();

	/** create a object of the class TheObservable */
	private TheObservable myObservable = new TheObservable();

	/** create the getter method to get the variable myObservable*/
	public TheObservable getTheObservable() {
		return this.myObservable;
	}

	/**
	 * (private!) Constructor, creates a new object model and send it to the start
	 * station
	 *
	 * @param label        of the object
	 * @param stationsToGo the stations to go
	 * @param processtime  the processing time of the object, affects treatment by a
	 *                     station
	 * @param speed        the moving speed of the object
	 * @param xPos         x position of the object
	 * @param yPos         y position of the object
	 * @param image        image of the object
	 */
	private TheObject(String label, ArrayList<String> stationsToGo, int processtime, int speed, int xPos, int yPos,
			String image) {
		super(label, xPos, yPos);

		// create the view
		this.theView = TheObjectView.create(label, image, xPos, yPos);

		TheObject.allObjects.add(this); // add object to the static list

		this.stationsToGo = stationsToGo;
		this.processTime = processtime;
		this.mySpeed = speed;

		// the first station to go to is the start station
		Station station = this.getNextStation();

		// enter the in queue of the start station
		this.enterInQueue(station);

	}

	/**
	 * Create a new object model
	 *
	 * @param label        of the object
	 * @param stationsToGo the stations to go
	 * @param processtime  the processing time of the object, affects treatment by a
	 *                     station
	 * @param speed        the moving speed of the object
	 * @param xPos         x position of the object
	 * @param yPos         y position of the object
	 * @param image        image of the object
	 */
	public static void create(String label, ArrayList<String> stationsToGo, int processtime, int speed, int xPos,
			int yPos, String image) {

		new TheObject(label, stationsToGo, processtime, speed, xPos, yPos, image);

	}

	/**
	 * Chose the next station to go to
	 * 
	 * @return the next station or null if no station was found
	 */
	private Station getNextStation() {

		// we are at the end of the list
		if (this.stationsToGo.size() < stationListPointer)
			return null;

		// get the label of the next station from the list and increase the list pointer
		String stationLabel = this.stationsToGo.get(stationListPointer++);

		// looking for the matching station and return it
		for (Station station : Station.getAllStations()) {

			if (stationLabel.equals(station.getLabel()))
				return station;

		}

		return null; // the matching station isn't found
	}

	/**
	 * Chooses a suited incoming queue of the given station and enter it
	 * 
	 * @param station the station from where the queue should be chosen
	 * 
	 */
	private void enterInQueue(Station station) {

		// get the stations incoming queues
		ArrayList<SynchronizedQueue> inQueues = station.getAllInQueues();

		// there is just one queue, enter it
		if (inQueues.size() == 1)
			inQueues.get(0).offer(this);

		// Do we have more than one incoming queue?
		// We have to make a decision which queue we choose -> your turn
		else {

			// get the first queue and it's size
			SynchronizedQueue queueBuffer = inQueues.get(0);
			int queueSize = queueBuffer.size();

			// Looking for the shortest queue (in a simple way)
			for (SynchronizedQueue inQueue : inQueues) {

				if (inQueue.size() < queueSize) {
					queueBuffer = inQueue;
					queueSize = inQueue.size();
				}
			}

			// enter the queue
			queueBuffer.offer(this);

		}

		// set actual station to the just entered station
		this.actualStation = station;

	}

	/**
	 * Chooses a suited outgoing queue of the given station and enter it
	 * 
	 * @param station the station from where the queue should be chosen
	 */
	void enterOutQueue(Station station) {

		// get the stations outgoing queues
		ArrayList<SynchronizedQueue> outQueues = station.getAllOutQueues();

		// there is just one queue, enter it
		if (outQueues.size() == 1)
			outQueues.get(0).offer(this);

		// Do we have more than one outgoing queue?
		// We have to make a decision which queue we choose -> your turn
		else {

			// get the first queue and it's size
			SynchronizedQueue queueBuffer = outQueues.get(0);
			int queueSize = queueBuffer.size();

			// Looking for the shortest queue (in a simple way)
			for (SynchronizedQueue inQueue : outQueues) {
        
		//When man creates a object of the class TheObject, one another object of the class TheObservable is automatic created!
		myObservable.theObject = this;

	}

	/** the inner class TheObservable of the class TheObject*/
	public class TheObservable extends Observable {
		//the Variable of the class TheObject
		public TheObject theObject;

		// the getter method to get the variable theObject
		public TheObject getObject() {
			return this.theObject;
		}

		//the method to activate the observer notifications
		public void notifyObserver(Object info) {
			setChanged();
			super.notifyObservers(info);
		}

		// the setter method used to set a new process time
		public int setProcessTime(int newProcessTime) {
			TheObject.this.processTime = newProcessTime;
			notifyObserver(new Integer(newProcessTime));
			return processTime;
		}

		// the getter method used to get a new process time
		public int getProcessTime() {
			return TheObject.this.processTime;
		}

		// the setter method used to set a new next station
		public Station setNextStation(String theStationLabel) {
			// we are at the end of the list
			if (TheObject.this.stationsToGo.size() < stationListPointer)
				return null;

			// get the label of the next station from the list and increase the list pointer
			theStationLabel = TheObject.this.stationsToGo.get(stationListPointer++);

			// looking for the matching station and return it
			for (Station station : Station.getAllStations()) {

				if (theStationLabel.equals(station.getLabel()))
					notifyObserver(theStationLabel);
				station = TheObject.this.theNextStation;
				return station;


			}

			return null; // the matching station isn't found
		}

		// the getter method used to get a new next station
		public Station getNextStation() {
			return TheObject.this.theNextStation;
		}
	}

	/**
	 * Create a new object model
	 *
	 * @param label        of the object
	 * @param stationsToGo the stations to go
	 * @param processtime  the processing time of the object, affects treatment by a
	 *                     station
	 * @param speed        the moving speed of the object
	 * @param xPos         x position of the object
	 * @param yPos         y position of the object
	 * @param image        image of the object
	 */
	public static void create(String label, ArrayList<String> stationsToGo, int processtime, int speed, int xPos,
							  int yPos, String image) throws ObjectLimitException {

		//create a quality of objects
		if( numbersOfObjects > 0) {
			numbersOfObjects =  numbersOfObjects -1;
			new TheObject(label, stationsToGo, processtime, speed, xPos, yPos, image);
		}
		else throw new ObjectLimitException();
	}

	/**
	 * Chose the next station to go to
	 *
	 * @return the next station or null if no station was found
	 */
	private Station getNextStation() {

		// we are at the end of the list
		if (this.stationsToGo.size() < stationListPointer)
			return null;

		// get the label of the next station from the list and increase the list pointer
		String stationLabel = this.stationsToGo.get(stationListPointer++);

		// looking for the matching station and return it
		for (Station station : Station.getAllStations()) {

			if (stationLabel.equals(station.getLabel()))
				return station;

		}

		return null; // the matching station isn't found
	}

	/**
	 * Chooses a suited incoming queue of the given station and enter it
	 *
	 * @param station the station from where the queue should be chosen
	 *
	 */
	private void enterInQueue(Station station) {

		// get the stations incoming queues
		ArrayList<SynchronizedQueue> inQueues = station.getAllInQueues();

		// there is just one queue, enter it
		if (inQueues.size() == 1)
			inQueues.get(0).offer(this);

			// Do we have more than one incoming queue?
			// We have to make a decision which queue we choose -> your turn
		else {

			// get the first queue and it's size
			SynchronizedQueue queueBuffer = inQueues.get(0);
			int queueSize = queueBuffer.size();

			// Looking for the shortest queue (in a simple way)
			for (SynchronizedQueue inQueue : inQueues) {
        
				if (inQueue.size() < queueSize) {
					queueBuffer = inQueue;
					queueSize = inQueue.size();
				}
			}

			// enter the queue
			queueBuffer.offer(this);

		}

		// set actual station to the just entered station
		this.actualStation = station;

	}

	/**
	 * Chooses a suited outgoing queue of the given station and enter it
	 *
	 * @param station the station from where the queue should be chosen
	 */
	void enterOutQueue(Station station) {

		// get the stations outgoing queues
		ArrayList<SynchronizedQueue> outQueues = station.getAllOutQueues();

		// there is just one queue, enter it
		if (outQueues.size() == 1)
			outQueues.get(0).offer(this);

			// Do we have more than one outgoing queue?
			// We have to make a decision which queue we choose -> your turn
		else {

			// get the first queue and it's size
			SynchronizedQueue queueBuffer = outQueues.get(0);
			int queueSize = queueBuffer.size();

			// Looking for the shortest queue (in a simple way)
			for (SynchronizedQueue inQueue : outQueues) {

				if (inQueue.size() < queueSize) {
					queueBuffer = inQueue;
					queueSize = inQueue.size();
				}
			}

			// enter the queue
			queueBuffer.offer(this);

		}
	}

	@Override
	protected boolean work() {

		// the object is leaving the station -> set actual station to null
		this.actualStation = null;

		// choose the next station to go to
		Station station = this.getNextStation();

		// only move if there is a next station found
		if (station == null)
			return false;

		// let the object move to the chosen station

		Statistics.show(this.getLabel() + " geht zur " + station.getLabel());

		// while target is not achieved
		while (!(station.getXPos() == this.xPos && station.getYPos() == this.yPos)) {

			// move to the station
			if (station.getXPos() > this.xPos)
				this.xPos++;
			if (station.getYPos() > this.yPos)
				this.yPos++;

			if (station.getXPos() < this.xPos)
				this.xPos--;
			if (station.getYPos() < this.yPos)
				this.yPos--;

			// set our view to the new position
			((Component) theView).setLocation(this.xPos, this.yPos);

			// let the thread sleep for the sequence time
			try {
				Thread.sleep(Simulation.SPEEDFACTOR * mySpeed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		Statistics.show(this.getLabel() + " erreicht " + station.getLabel());

		// the object has reached the station, now the object chooses an incoming queue
		// and enter it
		this.enterInQueue(station);

		// wake up the station
		station.wakeUp();

		// work is done
		return false;


	}

	/**
	 * A (static) inner class for measurement jobs. The class records specific
	 * values of the object during the simulation. These values can be used for
	 * statistic evaluation.
	 */
	static class Measurement {

		/** the treated time by all processing stations, in seconds */
		int myTreatmentTime = 0;

	}

	/**
	 * Print some statistics
	 *
	 */
	public void printStatistics() {

		String theString = "\nObjekt: " + this.label;
		theString = theString + "\nZeit zum Behandeln des Objekts: " + measurement.myTreatmentTime;

		Statistics.show(theString);

	}

	/**
	 * Get all objects
	 *
	 * @return a list of all objects
	 */
	public static ArrayList<TheObject> getAllObjects() {
		return allObjects;
	}

	/**
	 * Get the actual station where this object is in
	 *
	 * @return the actual station where this object is in, null if it's not in a
	 *         station or a stations queue
	 */
	public Station getActualStation() {
		return actualStation;
	}

	/**
	 * Get the objects processing time
	 *
	 * @return the processing time
	 */
	public int getProcessTime() {
		return processTime;
	}
}

