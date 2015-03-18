package hkust.cse.calendar.unit;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.gui.CalGrid;
import hkust.cse.calendar.listener.TimeMachineListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

public class TimeMachine implements ActionListener {

	private Timer timer;
	private Timestamp startTime;
	private Timestamp endTime;
	private Timestamp currentTime;
	private int timeDelay;
	private Boolean isStart;
	private Boolean isRewind;
	//private CalGrid parent;
	private List<TimeMachineListener> listeners;
	
	
	public TimeMachine() {
		
		this.startTime = new Timestamp(0);
		this.currentTime = startTime;
		this.timeDelay = 1000;
		this.isStart = false;
		this.isRewind = false;
		//this.parent = parent;
		this.timer = new Timer(1000 , this);
		this.listeners = new ArrayList<TimeMachineListener>();
	}
	
	public void addElpasedListener(TimeMachineListener listener) {
		this.listeners.add(listener);
	}
	
	public void changeStartTime(Timestamp startTime) {
		this.startTime = startTime;
		this.currentTime = startTime;
	}
	
	public void changeTimeDelay(int timeDelay) {
		this.timeDelay = timeDelay;
	}
	
	public void changeEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	
	public void start() {
		if(this.isStart) return;
		this.timer.start();
		this.isStart = true;
	}
	
	public void reset() {
		this.startTime = new Timestamp(0);
		this.currentTime = startTime;
		this.timeDelay = 1000;
	}
	
	public void resume() {
		this.start();
	}
	
	public void rewind() {
		this.isRewind = true;
		this.start();
	}
	
	public void stop() {
		if(!this.isStart) return;
		this.timer.stop();
		this.isStart = false;
		this.isRewind = false;
		for(TimeMachineListener l : listeners) {
			l.timeStopped(this);
		}
	}
	
	public Boolean IsStart() {
		return this.isStart;
	}
	
	public Boolean IsStop() {
		return !this.isStart;
	}
	
	public Boolean IsRewind() {
		return this.isRewind;
	}
	
	public String toString() {
		return String.format("%04d-%02d-%02d %02d:%02d:%02d", this.currentTime.getYear() + 1900, this.currentTime.getMonth() + 1, this.currentTime.getDate(), this.currentTime.getHours(), this.currentTime.getMinutes(), this.currentTime.getSeconds());
	}
	
	public Timestamp getCurrentTime() {
		return this.currentTime;
	}
	
	public Timestamp getNextElapsedTime() {
		Timestamp t = (Timestamp)this.currentTime.clone();
		t.setTime(this.currentTime.getTime() + this.timeDelay);
		return t;
	}
	
	public int getTimeDelay() {
		return this.timeDelay;
	}
	
	public Timestamp getStartTime() {
		return this.startTime;
	}
	
	public Timestamp getEndTime() {
		return this.endTime;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.timer)	{
			
			if(this.isRewind) {
				this.currentTime.setTime(this.currentTime.getTime() - this.timeDelay);
			}
			else {
				this.currentTime.setTime(this.currentTime.getTime() + this.timeDelay);
			}
			
			for(TimeMachineListener l : listeners) {
				l.timeElapsed(this);
			}
			
			//check if current time beyond the end time
			if(this.currentTime.compareTo(this.endTime) > 0) {
				this.stop();
			}
		}
		
	}
	
}
