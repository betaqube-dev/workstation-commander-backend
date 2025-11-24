package dev.betaqube.wc.workday;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "workday.messages")
public class WorkdayMessagesProperties {

	private String weekendStart;
	private String alreadyStarted;
	private String notInProgress;
	private String lunchAlreadyMarked;
	private String notOnLunch;
	private String alreadyEnded;
	private String notStarted;
	private String userNotFound;
	private String alertNoSessionAfterStart;
	private String alertMissingLunch;
	private String alertPastEnd;
	private String alertReturnFromLunch;

	public String getWeekendStart() {
		return weekendStart;
	}

	public void setWeekendStart(String weekendStart) {
		this.weekendStart = weekendStart;
	}

	public String getAlreadyStarted() {
		return alreadyStarted;
	}

	public void setAlreadyStarted(String alreadyStarted) {
		this.alreadyStarted = alreadyStarted;
	}

	public String getNotInProgress() {
		return notInProgress;
	}

	public void setNotInProgress(String notInProgress) {
		this.notInProgress = notInProgress;
	}

	public String getLunchAlreadyMarked() {
		return lunchAlreadyMarked;
	}

	public void setLunchAlreadyMarked(String lunchAlreadyMarked) {
		this.lunchAlreadyMarked = lunchAlreadyMarked;
	}

	public String getNotOnLunch() {
		return notOnLunch;
	}

	public void setNotOnLunch(String notOnLunch) {
		this.notOnLunch = notOnLunch;
	}

	public String getAlreadyEnded() {
		return alreadyEnded;
	}

	public void setAlreadyEnded(String alreadyEnded) {
		this.alreadyEnded = alreadyEnded;
	}

	public String getNotStarted() {
		return notStarted;
	}

	public void setNotStarted(String notStarted) {
		this.notStarted = notStarted;
	}

	public String getUserNotFound() {
		return userNotFound;
	}

	public void setUserNotFound(String userNotFound) {
		this.userNotFound = userNotFound;
	}

	public String getAlertNoSessionAfterStart() {
		return alertNoSessionAfterStart;
	}

	public void setAlertNoSessionAfterStart(String alertNoSessionAfterStart) {
		this.alertNoSessionAfterStart = alertNoSessionAfterStart;
	}

	public String getAlertMissingLunch() {
		return alertMissingLunch;
	}

	public void setAlertMissingLunch(String alertMissingLunch) {
		this.alertMissingLunch = alertMissingLunch;
	}

	public String getAlertPastEnd() {
		return alertPastEnd;
	}

	public void setAlertPastEnd(String alertPastEnd) {
		this.alertPastEnd = alertPastEnd;
	}

	public String getAlertReturnFromLunch() {
		return alertReturnFromLunch;
	}

	public void setAlertReturnFromLunch(String alertReturnFromLunch) {
		this.alertReturnFromLunch = alertReturnFromLunch;
	}
}
