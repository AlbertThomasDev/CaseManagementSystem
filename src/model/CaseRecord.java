package model;

public class CaseRecord {
    
    // Attributes
    private int caseId;
    private String defendantName;
    private String defendantDob;
    private String caseType;
    private String hearingDate;
    private String caseStatus;

    // Constructor
    public CaseRecord(int caseId, String defendantName, String defendantDob, 
                      String caseType, String hearingDate, String caseStatus) {
        this.caseId = caseId;
        this.defendantName = defendantName;
        this.defendantDob = defendantDob;
        this.caseType = caseType;
        this.hearingDate = hearingDate;
        this.caseStatus = caseStatus;
    }

    // Getters
    public int getCaseId() { 
        return caseId; 
    }
    
    public String getDefendantName() { 
        return defendantName; 
    }

    public String getDefendantDob() { 
        return defendantDob; 
    }

    public String getCaseType() { 
        return caseType; 
    }

    public String getHearingDate() { 
        return hearingDate; 
    }

    public String getCaseStatus() { 
        return caseStatus; 
    }

    
    
    // Setters
    public void setCaseId(int caseId) { 
        this.caseId = caseId; 
    }

    public void setDefendantName(String defendantName) { 
        this.defendantName = defendantName; 
    }

    public void setDefendantDob(String defendantDob) { 
        this.defendantDob = defendantDob; 
    }

    public void setCaseType(String caseType) { 
        this.caseType = caseType; 
    }

    public void setHearingDate(String hearingDate) { 
        this.hearingDate = hearingDate; 
    }

    public void setCaseStatus(String caseStatus) { 
        this.caseStatus = caseStatus; 
    }
}