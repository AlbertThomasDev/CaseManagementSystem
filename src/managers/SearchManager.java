package managers;

import model.CaseRecord;
import java.util.List;
import java.util.stream.Collectors;

public class SearchManager {

    private CaseRecordManager caseRecordManager;

    // Constructor
    public SearchManager(CaseRecordManager caseRecordManager) {
        this.caseRecordManager = caseRecordManager;
    }

    // Search case records based on criteria
    public List<CaseRecord> search(String defendantName, String caseType,
                                    String hearingDate, String caseStatus) {

        // Get all records from CaseRecordManager
        List<CaseRecord> allRecords = caseRecordManager.getAllCaseRecords();

        // If all criteria empty return everything
        if (isNullOrEmpty(defendantName) && isNullOrEmpty(caseType) &&
            isNullOrEmpty(hearingDate) && isNullOrEmpty(caseStatus)) {
            return allRecords;
        }

        // Filter based on criteria
        return allRecords.stream()
            .filter(record -> matchesCriteria(record, defendantName,
                                              caseType, hearingDate, caseStatus))
            .collect(Collectors.toList());
    }

    // Check if record matches all criteria
    private boolean matchesCriteria(CaseRecord record, String defendantName,
                                     String caseType, String hearingDate,
                                     String caseStatus) {
        boolean matches = true;

        if (!isNullOrEmpty(defendantName)) {
            matches = matches && record.getDefendantName().toLowerCase()
                .contains(defendantName.toLowerCase());
        }
        if (!isNullOrEmpty(caseType)) {
            matches = matches && record.getCaseType().toLowerCase()
                .contains(caseType.toLowerCase());
        }
        if (!isNullOrEmpty(hearingDate)) {
            matches = matches && record.getHearingDate()
                .contains(hearingDate);
        }
        if (!isNullOrEmpty(caseStatus)) {
            matches = matches && record.getCaseStatus().toLowerCase()
                .contains(caseStatus.toLowerCase());
        }

        return matches;
    }

    // Helper method to check if string is null or empty
    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}