package assign3;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;


@SuppressWarnings("serial")
public class SudokuFrame extends JFrame {
	private static SudokuFrame frame;
	private static Sudoku sudo;
	private static JTextArea jtaC;
	private static JTextArea jtaE;
	private static JButton Check;
	private static JCheckBox AutoCheck;
	private static JPanel jp;
	private static JFrame jf;
		
	public SudokuFrame() {
		super("Sudoku Solver");
		jf = new JFrame();
		jtaC = new JTextArea(15,20);
		jtaE = new JTextArea(15,20);
		jp = new JPanel();
		jf.setLayout(new BorderLayout(4,4));
		jtaC.setBorder(new TitledBorder("Puzzle"));
		jf.add(jtaC,BorderLayout.CENTER);
		jtaE.setBorder(new TitledBorder("Solution"));
		jf.add(jtaE,BorderLayout.EAST);
		jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
		Check = new JButton("Check");
		jp.add(Check);
		AutoCheck = new JCheckBox("Auto Check");
		AutoCheck.setSelected(true);
		jp.add(AutoCheck);
		jf.add(jp,BorderLayout.SOUTH);
		jf.setTitle("Sudoku Solver");
		jf.setLocationByPlatform(true);		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}
	
	private void newBoard(){
		sudo = new Sudoku(jtaC.getText());
	}
	
	private static void boardChange(){
		try{
			frame.newBoard();
			int solutions = sudo.solve();
			long elapsed = sudo.getElapsed();
			String str = "solutions:" + solutions + "\n" + "elapsed:" + elapsed + "ms";
			String str1 = sudo.getSolutionText() + str;
			jtaE.setText(str1);
		} catch (Exception e) {
			jtaE.setText("Parsing Error");
		}
	}
	
	private static void listeners(){
		//Check Button
		Check.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!AutoCheck.isSelected()){
					boardChange();
				}
			}
		});
		
		//JtaE changed Button
		jtaC.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				if(AutoCheck.isSelected()){
					boardChange();
				}
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				if(AutoCheck.isSelected()){
					boardChange();
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
			
		});
	}
	
	public static void main(String[] args) {
		// GUI Look And Feel
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		frame = new SudokuFrame();
		listeners();
	}

}
