package hkust.cse.calendar.gui;

import java.awt.Color;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

class CalCellRenderer extends DefaultTableCellRenderer
{
	private int r;
	private int c;

	public CalCellRenderer(Object value, boolean hasAppointment ) {
		CommonCalCellRenderer(value, hasAppointment);
	}

	public CalCellRenderer(Object value) {
		CommonCalCellRenderer(value, false);//default has Appointment = false
	}
	
	public void CommonCalCellRenderer(Object value, boolean hasAppointment) {
		if (value != null) {
			setForeground(Color.red);
		} else
			setForeground(Color.black);
		if(hasAppointment){
			setBackground(Color.orange);
		}else{
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
