package hkust.cse.calendar.gui;

import java.awt.Color;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

class CalCellRenderer extends DefaultTableCellRenderer

{

	private int r;

	private int c;

	public CalCellRenderer(boolean today, int numOfAppts) {
		if (today) 
			setForeground(Color.red);	
		 else
			setForeground(Color.black);
		
		if(numOfAppts>0) {
			setBackground(Color.green);
		}
		else {
			setBackground(Color.white);
		}
		setHorizontalAlignment(SwingConstants.RIGHT);
		setVerticalAlignment(SwingConstants.TOP);
	}

	public int row() {
		return r;
	}

	public int col() {
		return c;
	}

}
