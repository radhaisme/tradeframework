/*
 * Copyright (c) 2012 Jeremy Goetsch
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jgoetsch.eventtrader.filter;

import java.util.Map;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;

import com.jgoetsch.eventtrader.Msg;

/**
 * Filtering message processor that will process messages only between the specified
 * hours of the day. This is useful to contrain processing of messages to be within
 * regular trading hours.
 * 
 * @author jgoetsch
 *
 */
public class TimeOfDayFilter<M extends Msg> extends FilterProcessor<M> {

	private LocalTime after;
	private LocalTime before;
	private DateTimeZone timeZone = DateTimeZone.forID("America/New_York");

	public TimeOfDayFilter() {
	}

	public TimeOfDayFilter(LocalTime startTime, LocalTime endTime) {
		if (startTime.compareTo(endTime) > 1)
			throw new IllegalArgumentException("startTime cannot be after endTime");
		this.after = startTime;
		this.before = endTime;
	}

	@Override
	protected boolean handleProcessing(M msg, Map<Object,Object> context) {
		LocalTime msgTime = new LocalTime(msg.getDate(), timeZone);
		return (after == null || msgTime.compareTo(after) >= 0)
				&& (before == null || msgTime.compareTo(before) < 0);
	}

	public static TimeOfDayFilter<? extends Msg> getUSStockRTHFilter() {
		return new TimeOfDayFilter<Msg>(new LocalTime(9, 30), new LocalTime(16, 0));
	}

	public String getAfter() {
		return after.toString();
	}

	public void setAfter(String after) {
		this.after = new LocalTime(after, timeZone);
	}

	public String getBefore() {
		return before.toString();
	}

	public void setBefore(String before) {
		this.before = new LocalTime(before, timeZone);
	}

	public DateTimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(DateTimeZone timeZone) {
		this.timeZone = timeZone;
	}
}
