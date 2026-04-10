package model;

public class ArchivedCaseRecord {

    // Attributes
    private int archivedCaseId;
    private int originalCaseId;
    private String defendantName;
    private String defendantDob;
    private String caseType;
    private String hearingDate;
    private String caseStatus;
    private String archiveDate;

    // Constructor
    public ArchivedCaseRecord(int archivedCaseId, int originalCaseId,
                               String defendantName, String defendantDob,
                               String caseType, String hearingDate,
                               String caseStatus, String archiveDate) {
        this.archivedCaseId = archivedCaseId;
        this.originalCaseId = originalCaseId;
        this.defendantName = defendantName;
        this.defendantDob = defendantDob;
        this.caseType = caseType;
        this.hearingDate = hearingDate;
        this.caseStatus = caseStatus;
        this.archiveDate = archiveDate;
    }

    // Getters
    public int getArchivedCaseId() { 
        return archivedCaseId; 
    }

    public int getOriginalCaseId() { 
        return originalCaseId; 
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

    public String getArchiveDate() { 
        return archiveDate; 
    }

    // Setters
    public void setArchivedCaseId(int archivedCaseId) { 
        this.archivedCaseId = archivedCaseId; 
    }

    public void setOriginalCaseId(int originalCaseId) { 
        this.originalCaseId = originalCaseId; 
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

    public void setArchiveDate(String archiveDate) { 
        this.archiveDate = archiveDate; 
    }
    
}