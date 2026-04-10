package model;

public class Notification {

    // Attributes
    private int notificationId;
    private int caseId;
    private String message;
    private String hearingDate;
    private String dateCreated;

    // Constructor
    public Notification(int notificationId, int caseId,
                        String message, String hearingDate,
                        String dateCreated) {
        this.notificationId = notificationId;
        this.caseId = caseId;
        this.message = message;
        this.hearingDate = hearingDate;
        this.dateCreated = dateCreated;
    }

    // Getters
    public int getNotificationId() { 
        return notificationId; 
    }

    public int getCaseId() { 
        return caseId; 
    }

    public String getMessage() { 
        return message; 
    }

    public String getHearingDate() { 
        return hearingDate; 
    }

    public String getDateCreated() { 
        return dateCreated; 
    }

    // Setters
    public void setNotificationId(int notificationId) { 
        this.notificationId = notificationId; 
    }
    public void setCaseId(int caseId) { 
        this.caseId = caseId; 
    }
    public void setMessage(String message) { 
        this.message = message; 
    }

    public void setHearingDate(String hearingDate) { 
        this.hearingDate = hearingDate; 
    }

    public void setDateCreated(String dateCreated) { 
        this.dateCreated = dateCreated; 
    }
    
}