package assign3;

import javax.swing.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class MetropolisFrame extends JFrame {
	private static MetropolisFrame metroFrame;
	private static Metropolis con;
	private JFrame jf;
	private JTable jt;
	private JPanel jpN;
	private JTextField metro;
	private JTextField conti;
	private JTextField popu;
	private JPanel jpE;
	private JButton add;
	private JButton search;
	private JComboBox<String> popine;
	private JComboBox<String> match;

	public MetropolisFrame() {
		super();
		jf = new JFrame();
		jf.setLayout(new BorderLayout(4,4));
		northPanel();
		eastPanel();
		graphics();
	}

	private void northPanel(){
		jpN = new JPanel();
		jpN.setLayout(new BoxLayout(jpN, BoxLayout.X_AXIS));
		metro = new JTextField();
		conti = new JTextField();
		popu = new JTextField();		
		JLabel metroLabel = new JLabel("Metropolis: ");
		JLabel contiLabel = new JLabel("Continent: ");
		JLabel popuLabel = new JLabel("Population: ");
		metroLabel.setLabelFor(metro);
		contiLabel.setLabelFor(conti);
		popuLabel.setLabelFor(popu);
		jpN.add(metroLabel);
		jpN.add(metro);
		jpN.add(contiLabel);
		jpN.add(conti);
		jpN.add(popuLabel);
		jpN.add(popu);
	}
	
	private void eastPanel(){
		jpE = new JPanel();
		jpE.setLayout(new BoxLayout(jpE, BoxLayout.Y_AXIS));
		add = new JButton("Add");
		search = new JButton("Search");
		jpE.add(add);
		jpE.add(search);
		popine = new JComboBox<String>();
		match = new JComboBox<String>();
		popine.addItem("Population Larger Than");
		popine.addItem("Population Less Than");
		match.addItem("Exact Match");
		match.addItem("Partial Match");
		jpE.add(popine);
		jpE.add(match);
	}
	
	private void graphics(){
		jf.add(jpN,BorderLayout.NORTH);
		jf.add(jpE,BorderLayout.EAST);
		jf.add(jt,BorderLayout.CENTER);
		//Set the title
		jf.setTitle("Metropolis Viewer");
		//Set everything in place
		jf.setLocationByPlatform(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}
	
	private static void listeners(){
		
	}

	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		metroFrame = new MetropolisFrame();
		con = new Metropolis();
		
		listeners();
	}
}
