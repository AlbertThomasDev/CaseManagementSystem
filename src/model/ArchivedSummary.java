package model;

public class ArchivedSummary {

    // Attributes
    private int archivedSummaryId;
    private int archivedCaseId;
    private String summaryText;
    private String dateAdded;

    // Constructor
    public ArchivedSummary(int archivedSummaryId, int archivedCaseId,
                            String summaryText, String dateAdded) {
        this.archivedSummaryId = archivedSummaryId;
        this.archivedCaseId = archivedCaseId;
        this.summaryText = summaryText;
        this.dateAdded = dateAdded;
    }

    // Getters
    public int getArchivedSummaryId() { 
        return archivedSummaryId; 
    }

    public int getArchivedCaseId() { 
        return archivedCaseId; 
    }

    public String getSummaryText() { 
        return summaryText; 
    }

    public String getDateAdded() { 
        return dateAdded; 
    }

    // Setters
    public void setArchivedSummaryId(int archivedSummaryId) { 
        this.archivedSummaryId = archivedSummaryId; 
    }

    public void setArchivedCaseId(int archivedCaseId) { 
        this.archivedCaseId = archivedCaseId; 
    }

    public void setSummaryText(String summaryText) { 
        this.summaryText = summaryText; 
    }

    public void setDateAdded(String dateAdded) { 
        this.dateAdded = dateAdded; 
    }
    
}