package ui;

import managers.CaseRecordManager;
import model.Notification;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NotificationPanel extends JPanel {

    // Reference to MainView
    private MainView mainView;

    // Manager
    private CaseRecordManager caseRecordManager;

    // UI components
    private JPanel notificationsListPanel;
    private JLabel titleLabel;

    // Constructor
    public NotificationPanel(MainView mainView) {
        this.mainView = mainView;
        this.caseRecordManager = mainView.getCaseRecordManager();
        initializePanel();
    }

    // Initialize the panel
    private void initializePanel() {
        setLayout(new BorderLayout());

        // Title
        titleLabel = new JLabel("Notifications", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Notifications list
        JPanel notificationsPanel = new JPanel(new BorderLayout());
        notificationsPanel.setBorder(
            BorderFactory.createTitledBorder("Upcoming Hearings"));

        notificationsListPanel = new JPanel();
        notificationsListPanel.setLayout(
            new BoxLayout(notificationsListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(notificationsListPanel);
        notificationsPanel.add(scrollPane, BorderLayout.CENTER);
        add(notificationsPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e ->
            mainView.showPanel(MainView.MAIN_PANEL));
        buttonPanel.add(backBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Load notifications
    public void loadNotifications() {
        notificationsListPanel.removeAll();
        List<Notification> notifications =
            caseRecordManager.getAllNotifications();

        if (notifications.isEmpty()) {
            JLabel noNotificationsLabel = new JLabel(
                "No upcoming hearings within 5 days.");
            noNotificationsLabel.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));
            notificationsListPanel.add(noNotificationsLabel);
        } else {
            for (Notification notification : notifications) {
                // Notification panel
                JPanel notificationItem = new JPanel(new BorderLayout());
                notificationItem.setBorder(
                    BorderFactory.createEmptyBorder(5, 10, 5, 10));

                // Message label
                JLabel messageLabel = new JLabel(
                    "<html>" + notification.getMessage() + "</html>");
                notificationItem.add(messageLabel, BorderLayout.CENTER);

                // View Case button
                JButton viewBtn = new JButton("View Case");
                viewBtn.addActionListener(e -> {
                    mainView.viewCase(notification.getCaseId());
                });
                notificationItem.add(viewBtn, BorderLayout.EAST);

                notificationsListPanel.add(notificationItem);
                notificationsListPanel.add(new JSeparator());
            }
        }

        notificationsListPanel.revalidate();
        notificationsListPanel.repaint();
    }

    // Show popup for new notifications
    public void showNotificationPopup(List<Notification> notifications) {
        if (!notifications.isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("You have ")
                   .append(notifications.size())
                   .append(" upcoming hearing(s):\n\n");
            for (Notification notification : notifications) {
                message.append("• ")
                       .append(notification.getMessage())
                       .append("\n");
            }
            SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(
                    mainView,
                    message.toString(),
                    "Upcoming Hearings",
                    JOptionPane.INFORMATION_MESSAGE
                )
            );
        }
    }
}