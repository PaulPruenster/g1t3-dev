package at.qe.skeleton.model.demo;

import java.util.Date;

import at.qe.skeleton.model.Userx;

/**
 * A class which represents a logEntry.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
public class LogEntry {

    private Userx user;
    private Date timestamp = new Date();
    private LogEntryType logType;

    public LogEntry(Userx user, LogEntryType logType) {
            super();
            this.user = user;
            this.logType = logType;
    }

    public Userx getUser() {
            return user;
    }

    public void setUser(Userx user) {
            this.user = user;
    }

    public Date getTimestamp() {
            return timestamp;
    }

    public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
    }

    public LogEntryType getLogType() {
            return logType;
    }

    public void setLogType(LogEntryType logType) {
            this.logType = logType;
    }

}
