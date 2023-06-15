package com.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttendanceEvent {
	@JsonProperty("time")
	private String time;

	@JsonProperty("event")
	private String event;

	public AttendanceEvent(String time, String event) {
		this.time = time;
		this.event = event;
	}

	public String getTime() {
		return time;
	}

	public String getEvent() {
		return event;
	}
}
