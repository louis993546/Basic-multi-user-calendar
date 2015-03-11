package hkust.cse.calendar.listener;

import hkust.cse.calendar.unit.TimeMachine;

public interface TimeMachineListener {
	public void timeElapsed(TimeMachine sender);
	public void timeStopped(TimeMachine sender);
}