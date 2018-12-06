package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.JLabel;

import model.ProcessStation;

/**
 * Class for the Statistic window
 * 
 * @author Sommer, Depta, Nguyen, Kauertz
 * @version 2018-12-01
 */

@SuppressWarnings("serial")
public class StatisticsView extends JFrame {

	/** Statistics window width */
	private final static int WIDTH = 350;

	/** statistics window height */
	private final static int HEIGHT = 300;

	/** statistics window title */
	private final static String TITLE = "Statistiken";

	/** Panel used for the Statistics */
	private StatisticsPanel statisticsPanel;

	/** Label of the given Station */
	public String label;

	/**
	 * Creates a JFrame statistics window Safes given Stations label
	 * 
	 * @param label of the clicked Station
	 */
	public StatisticsView(String label) {
		this.label = label;
		this.init();
	}

	/**
	 * initialize the statistics window
	 * 
	 */

	private void init() {
		/** a panel where the Statistics are shown in a live feed */
		statisticsPanel = new StatisticsPanel(label);

		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setSize(WIDTH, HEIGHT);
		this.setTitle(TITLE);

		// put the statistics panel into our JFrame
		this.getContentPane().add(statisticsPanel);
		this.setVisible(true);
	}

	/**
	 * Calls method to Update the Panel within Clockbeat
	 * 
	 */
	public void updateFrame() {
		statisticsPanel.updateStatistics();
	}

	/**
	 * inner JPanel class where the statistics are shown
	 * 
	 */
	private static class StatisticsPanel extends JPanel {

		// Initialize all needed Variables and Arrays
		private ArrayList<ProcessStation> allProcessStations = ProcessStation.getAllProcessStations();
		private ArrayList<JLabel> allLabel = new ArrayList<JLabel>();
		private String label;

		/**
		 * Constructor initializes the panel and its Layout
		 * 
		 * @param label of the clicked Station
		 */
		private StatisticsPanel(String label) {
			this.printStatistics(label);
			this.setLayout(new GridLayout(5, 1));
			this.setBackground(Color.LIGHT_GRAY);
		}

		/**
		 * Sets the different Labels, checks for the Station and Appends correct Values
		 * to Labels
		 * 
		 * @param label of the clicked Station
		 */
		private void printStatistics(String label) {
			this.label = label;

			// Setting all Strings
			String stationType = "Station Typ: ";
			String objectCounter = "Anzahl der behandelten Objekte; ";
			String handleTimeAll = "Zeit zum behandeln aller Objekte: ";
			String avgHandlingTime = "Durchschnittliche Behandlungsdauer: ";
			String peek = "Erfuhr einen Peek von: ";

			// Iterating through all Stations (foreach)
			for (ProcessStation station : allProcessStations) {
				// Checks for Correct Station
				if (station.getLabel() == label) {
					// Appends Values to Strings
					stationType = stationType + label;
					objectCounter = objectCounter + station.getNumberOfVisitedObjects();
					handleTimeAll = handleTimeAll + station.getInUseTime();
					avgHandlingTime = avgHandlingTime + station.getAvgTreatmentTime();
					peek = peek + station.getInQueuePeek();
				}
			}

			// Shifts made up Strings to JLabel
			JLabel peekLabel = new JLabel(peek);
			JLabel stationTypeLabel = new JLabel(stationType);
			JLabel objectCounterLabel = new JLabel(objectCounter);
			JLabel handleTimeAllLabel = new JLabel(handleTimeAll);
			JLabel avgHandlingTimeLabel = new JLabel(avgHandlingTime);

			// Appends all Labels to ArrayList for Iteration
			allLabel.add(stationTypeLabel);
			allLabel.add(objectCounterLabel);
			allLabel.add(handleTimeAllLabel);
			allLabel.add(avgHandlingTimeLabel);
			allLabel.add(peekLabel);

			addLabels(allLabel);
		}

		/**
		 * sets Alignment for all JLabels and adds them to the Panel
		 * 
		 * @param allLabel
		 */
		private void addLabels(ArrayList<JLabel> allLabel) {
			for (JLabel label : allLabel) {
				label.setHorizontalAlignment(SwingConstants.LEFT);
				label.setVerticalAlignment(SwingConstants.CENTER);
				this.add(label);
			}
		}

		/**
		 * Updates all Labels within Clockbeat
		 * 
		 */
		protected void updateStatistics() {
			// Iterates through all ProcessStations
			for (ProcessStation station : allProcessStations) {
				// Found the right Station?
				if (station.getLabel() == this.label) {
					// Call updateLabel()
					updateLabels(allLabel, station.getLabel(), station.getNumberOfVisitedObjects(),
							station.getInUseTime(), station.getAvgTreatmentTime(), station.getInQueuePeek());
				}
			}
		}

		/** Updating all Label from the Panel with the new Values
		 * 
		 * @param allJLabel        all label in the Panel
		 * @param stationLabel     the Label of the Station
		 * @param numbOfVisitors   how many Objects have visited this Station
		 * @param inUseTime        how long did the Station need to Handle all the
		 *                         visitors
		 * @param avgTreatmentTime Average time it needed for a single visitor to handle
		 * @param peek             of the Queue that this Station had
		 */
		private void updateLabels(ArrayList<JLabel> allJLabel, String stationLabel, int numbOfVisitors, int inUseTime,
				int avgTreatmentTime, int peek) {
			// Reevaluate all Label from the Panel
			allJLabel.get(0).setText("Station Typ: " + stationLabel);
			allJLabel.get(1).setText("Anzahl der behandelten Objekte; " + numbOfVisitors);
			allJLabel.get(2).setText("Zeit zum behandeln aller Objekte: " + inUseTime);
			allJLabel.get(3).setText("Durchschnittliche Behandlungsdauer: " + avgTreatmentTime);
			allJLabel.get(4).setText("Erfuhr einen Peek von: " + peek);
		}

	}
}