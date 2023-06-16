package com.models;

public class AttendanceEvent {

	private String time;
	private String event;

	public AttendanceEvent(String time, String event) {
		this.time = time;
		this.event = event;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getTime() {
		return time;
	}

	public String getEvent() {
		return event;
	}
}
