package model;

public class Summary {

    // Attributes
    private int summaryId;
    private int caseId;
    private String summaryText;
    private String dateAdded;

    // Constructor
    public Summary(int summaryId, int caseId, 
                   String summaryText, String dateAdded) {
        this.summaryId = summaryId;
        this.caseId = caseId;
        this.summaryText = summaryText;
        this.dateAdded = dateAdded;
    }

    // Getters
    public int getSummaryId() { 
        return summaryId; 
    }

    public int getCaseId() { 
        return caseId; 
    }

    public String getSummaryText() { 
        return summaryText; 
    }

    public String getDateAdded() { 
        return dateAdded; 
    }

    // Setters
    public void setSummaryId(int summaryId) { 
        this.summaryId = summaryId; 
    }

    public void setCaseId(int caseId) { 
        this.caseId = caseId; 
    }

    public void setSummaryText(String summaryText) { 
        this.summaryText = summaryText; 
    }

    public void setDateAdded(String dateAdded) { 
        this.dateAdded = dateAdded; 
    }
    
}