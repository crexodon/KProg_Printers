package controller;

import view.SimulationView;
import io.Factory;
import io.Statistics;
import java.util.concurrent.atomic.AtomicLong;
import model.Actor;

/**
 * The main class it controls the flow of the simulation
 * 
 * @author Jaeger, Schmidt
 * @version 2016-07-07
 */
public class Simulation {
	
	/** is the simulation running*/
	public static boolean isRunning = false;  
	
	/** a speed factor for the clock to vary the speed of the clock in a simple way*/
	public static int SPEEDFACTOR = 1;
	
	/**the beat or speed of the clock, e.g. 300 means one beat every 300 milli seconds*/
	public static final int CLOCKBEAT = 300 * SPEEDFACTOR;
	
	/**the global clock */
	//the clock must be thread safe -> AtomicLong. The primitive type long isn't, even if synchronized
	private static AtomicLong clock = new AtomicLong(0); 
	
	
	/**
	 * starts the simulation
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		
		//a new simulation
		Simulation theSimulation = new Simulation();
		theSimulation.init();
		
	}
	
	/**
	 * initialize the simulation
	 * 
	 */
	private void init(){
		
		//create all stations and objects for the starting scenario out of XML
		Factory.createStartScenario();
				
		//the view of our simulation
		new SimulationView();
					
		// Why start() not run()?
		// start() causes the execution of the Method while Java calls the run() method of the Thread
		// With this way there will be two Threads running concurrently
		new HeartBeat().start();
		 		
		Statistics.show("---- Simulation gestartet ---\n");
				
		// start all the actor threads
		for (Actor actor : Actor.getAllActors()) {
			actor.start();		
						
		}
	}
			
	
	/**
	 * The heartbeat (the pulse) of the simulation, controls the clock.
	 * 
	 */
	private class HeartBeat extends Thread {
		
		@Override
		public void run() {
			
			while(true){
				
				try {
				
					Thread.sleep(CLOCKBEAT);
					
					//Increase the global clock
					clock.incrementAndGet();
					
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	
	/** Get the global time
	 * 
	 * @return the global time
	 */
	public static long getGlobalTime() {
		return clock.get();
	}
	
}
