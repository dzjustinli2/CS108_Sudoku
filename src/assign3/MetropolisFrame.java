package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;

import java.sql.*;
import java.util.*;

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
	private JPanel jpE2;
	private JButton add;
	private JButton search;
	//private JButton re_source;
	private BasicTableModel model;
	private JScrollPane scrollpane;
	private JComboBox<String> popine;
	private JComboBox<String> match;

	public MetropolisFrame() {
		super();
		jf = new JFrame();
		jf.setLayout(new BorderLayout(4,4));
		northPanel();
		eastPanel();
		centerPanel();
		graphics();
		listeners();
	}
	
	private void centerPanel(){
		model = new BasicTableModel();
		// Create a table using that model
		model.addColumn("Metropolis");
		model.addColumn("Continent");
		model.addColumn("Population");
		jt = new JTable(model);
		// Create a scroll pane in the center, and put
		// the table in it
		scrollpane = new JScrollPane(jt);
		scrollpane.setPreferredSize(new Dimension(500,300));
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
		//re_source = new JButton("Source SQL");
		jpE.add(add);
		jpE.add(search);
		//jpE.add(re_source);
		jpE2 = new JPanel();
		jpE2.setLayout(new BoxLayout(jpE2, BoxLayout.Y_AXIS));
		popine = new JComboBox<String>(){
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }

        };
		match = new JComboBox<String>(){
            @Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }

        };
		popine.addItem("Population Larger Than");
		popine.addItem("Population Less Than Or Equal");
		match.addItem("Exact Match");
		match.addItem("Partial Match");
		jpE2.add(popine);
		jpE2.add(match);
		jpE2.setBorder(new TitledBorder("Search Options"));
		jpE.add(jpE2);
	}
	
	private void graphics(){
		jf.add(jpN,BorderLayout.NORTH);
		jf.add(jpE,BorderLayout.EAST);
		jf.add(scrollpane,BorderLayout.CENTER);
		//Set the title
		jf.setTitle("Metropolis Viewer");
		//Set everything in place
		jf.setLocationByPlatform(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}
	
	private void clearRows(){
		while(model.getRowCount() > 0){
			model.deleteRow(0);
		}
	}
	
	private void clearText(){
		metro.setText("");
		conti.setText("");
		popu.setText("");
	}
	private void listeners(){
		add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
            	con.openDB();
            	addLogic();
            	con.closeDB();
            }
        });
		
		search.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
            	con.openDB();
            	searchLogic();
            	con.closeDB();
            }
        });
		
		/*re_source.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
            	con.openDB();
            	con.re_source();
            	con.closeDB();
            }
        });*/
	}
	
	private void addLogic(){
		clearRows();
    	ResultSet rs = con.add(metro.getText(), conti.getText(), popu.getText());
    	clearText();
    	if(rs == null) return;
    	ArrayList<String> ar = new ArrayList<String>(3);
    	try{
    		rs.next();
    		for(int i = 1; i < 4; i++){
    			ar.add(rs.getString(i));
    		}
    	}catch(SQLException ex){
    		ex.printStackTrace();
    	}
    	model.addRow(ar);
	}
	
	private void searchLogic(){
		clearRows();
    	boolean mat = false;
    	boolean pop = false;
    	if((popine.getSelectedItem()).equals("Population Larger Than")){
    		pop = true;
    	}
    	if((match.getSelectedItem()).equals("Exact Match")){
    		mat = true;
    	}
    	ResultSet rs = con.search(metro.getText(), conti.getText(), popu.getText(),pop,mat);
    	if(rs == null) return;
    	try{
    		while(rs.next()){
    			ArrayList<String> ar = new ArrayList<String>();
	    		for(int i = 1; i < 4; i++){
	    			ar.add(rs.getString(i));
	    			/*if(addCol(metro.getText(),conti.getText(),popu.getText(),i)){
	    				ar.add(rs.getString(i));
	    			}else{
	    				ar.add(null);
	    			}*/
	    		}
	    		model.addRow(ar);
    		}
    	}catch(SQLException ex){
    		ex.printStackTrace();
    	}
    	clearText();
	}
	
	/*
	private boolean addCol(String metro, String conti, String popu, int index){
		if(metro.equals("") && conti.equals("") && popu.equals("")) return true;
		if(metro.equals("") && index == 1) return false;
		if(conti.equals("") && index == 2) return false;
		if(popu.equals("") && index == 3) return false;
		return true;
	}*/

	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		metroFrame = new MetropolisFrame();
		metroFrame.toString();
		con = new Metropolis();
	}
}
