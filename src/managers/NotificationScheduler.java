package managers;

import model.CaseRecord;
import model.Notification;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NotificationScheduler {

    private CaseRecordManager caseRecordManager;
    private ScheduledExecutorService scheduler;

    // Notification templates
    private static final String TEMPLATE_TODAY =
        "TODAY: You have a hearing for %s today on %s";
    private static final String TEMPLATE_FOUR_DAYS =
        "UPCOMING: %s has a hearing in 4 days on %s";
    private static final String TEMPLATE_IN_BETWEEN =
        "REMINDER: %s has a hearing in %d days on %s";

    // Constructor
    public NotificationScheduler(CaseRecordManager caseRecordManager) {
        this.caseRecordManager = caseRecordManager;
    }

    // Start scheduler — immediate check then every 12 hours
    public void start() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            checkForUpcomingHearings();
            caseRecordManager.cleanupOldNotifications();
        }, 0, 12, TimeUnit.HOURS);
    }

    // Stop scheduler
    public void stop() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    // Check for upcoming hearings within 5 days
    public List<Notification> checkForUpcomingHearings() {
        LocalDate today = LocalDate.now();
        List<CaseRecord> allCases = caseRecordManager.getAllCaseRecords();

        for (CaseRecord caseRecord : allCases) {
            try {
                LocalDate hearingDate = LocalDate.parse(caseRecord.getHearingDate());
                long daysUntilHearing = ChronoUnit.DAYS.between(today, hearingDate);

                // Only notify for hearings within 5 days and not passed
                if (daysUntilHearing >= 0 && daysUntilHearing <= 4) {
                    String message = buildMessage(caseRecord, daysUntilHearing);
                    caseRecordManager.createNotification(
                        caseRecord.getCaseId(),
                        message,
                        caseRecord.getHearingDate()
                    );
                }
            } catch (Exception e) {
                System.out.println("Error parsing hearing date: " + e.getMessage());
            }
        }

        return caseRecordManager.getAllNotifications();
    }

    // Build notification message based on days until hearing
    private String buildMessage(CaseRecord caseRecord, long daysUntilHearing) {
        if (daysUntilHearing == 0) {
            // Template 1 — Same day
            return String.format(TEMPLATE_TODAY,
                caseRecord.getDefendantName(),
                caseRecord.getHearingDate());
        } else if (daysUntilHearing == 4) {
            // Template 2 — 4 days before
            return String.format(TEMPLATE_FOUR_DAYS,
                caseRecord.getDefendantName(),
                caseRecord.getHearingDate());
        } else {
            // Template 3 — In between
            return String.format(TEMPLATE_IN_BETWEEN,
                caseRecord.getDefendantName(),
                daysUntilHearing,
                caseRecord.getHearingDate());
        }
    }
}