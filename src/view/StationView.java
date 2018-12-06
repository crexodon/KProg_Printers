package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import io.Statistics;

/**
 * Class for the view of stations
 * 
 * @author Jaeger, Schmidt
 * @version 2017-10-28
 */
@SuppressWarnings("serial")
public class StationView extends ImageView {
	// to get the Label of the clicked Station
	private String label;

	// OnClick event to distribute StatisticsView by clicking on Station Image
	private MouseListener listener = new MouseAdapter() {
		public void mouseClicked(MouseEvent e) {
			// open window with Statistics and give label of clicked Station.
			Statistics statView = new Statistics();
			statView.startView(label);

		}

	};

	/**
	 * Creates a new view with an image (JLabel) and a MouseListener
	 * 
	 * @param label of the station
	 * @param image image of the view
	 * @param xPos  x position of the view
	 * @param yPos  y position of the view
	 */
	private StationView(String label, String image, int xPos, int yPos) {

		super(image, xPos, yPos);
		this.addMouseListener(listener);
		this.setToolTipText(label);
		this.label = label;
	}

	/**
	 * Creates a new view of the station
	 * 
	 * @param label of the station
	 * @param image image of the view
	 * @param xPos  x position of the view
	 * @param yPos  y position of the view
	 * 
	 * @return the StationView
	 */
	public static StationView create(String label, String image, int xPos, int yPos) {

		return new StationView(label, image, xPos, yPos);

	}
}
