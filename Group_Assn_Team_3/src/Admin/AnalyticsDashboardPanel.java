package Admin;

import Business.Business;
import Business.UserAccounts.UserAccount;
import Business.Profiles.StudentProfile;
import Business.Profiles.FacultyProfile;
import Business.UserAccounts.Role;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;

/**
 * University analytics dashboard for Admin.
 * Shows overall statistics like users by role, course counts, and revenue.
 * @author Manav
 */
public class AnalyticsDashboardPanel extends JPanel {

    private final Business business;
    private JTable tblSummary;
    private JLabel lblUsers, lblCourses, lblRevenue;
    private DefaultTableModel model;

    public AnalyticsDashboardPanel(Business business, JPanel rightPanel) {
        this.business = business;
        initComponents();
        populateData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 255));

        JLabel title = new JLabel("University Analytics Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(0, 70, 140));
        add(title, BorderLayout.NORTH);

        // --- Table Setup ---
        model = new DefaultTableModel(new Object[]{"Metric", "Value"}, 0);
        tblSummary = new JTable(model);
        tblSummary.setRowHeight(25);
        tblSummary.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblSummary.setBackground(Color.WHITE);
        tblSummary.setGridColor(new Color(200, 200, 200));
        add(new JScrollPane(tblSummary), BorderLayout.CENTER);

        // --- Footer Summary ---
        JPanel footerPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        footerPanel.setBackground(new Color(230, 238, 255));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblUsers = new JLabel("", SwingConstants.CENTER);
        lblCourses = new JLabel("", SwingConstants.CENTER);
        lblRevenue = new JLabel("", SwingConstants.CENTER);

        lblUsers.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblCourses.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblRevenue.setFont(new Font("Segoe UI", Font.BOLD, 16));

        lblUsers.setForeground(new Color(0, 102, 204));
        lblCourses.setForeground(new Color(0, 102, 204));
        lblRevenue.setForeground(new Color(0, 102, 204));

        footerPanel.add(lblUsers);
        footerPanel.add(lblCourses);
        footerPanel.add(lblRevenue);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private void populateData() {
        model.setRowCount(0);

        // --- Count total users by role ---
        HashMap<Role, Integer> roleCount = new HashMap<>();
        for (UserAccount ua : business.getUserAccountDirectory().getUserAccountList()) {
            roleCount.put(ua.getRole(), roleCount.getOrDefault(ua.getRole(), 0) + 1);
        }

        int totalUsers = 0;
        for (Role r : roleCount.keySet()) {
            int count = roleCount.get(r);
            totalUsers += count;
            model.addRow(new Object[]{"Active Users - " + r, count});
        }

        // --- Fake course + enrollment data (simulate until Part 3 adds real courses) ---
        int totalCourses = business.getFacultyDirectory().getFacultyList().size() * 2; // assume 2 courses per faculty
        model.addRow(new Object[]{"Total Courses Offered (est.)", totalCourses});

        int totalEnrolled = business.getStudentDirectory().getStudentList().size() * 3; // assume each student in 3 courses
        model.addRow(new Object[]{"Total Student Enrollments (est.)", totalEnrolled});

        // --- Tuition revenue (simulate per student) ---
        double tuitionPerStudent = 1200.00; // placeholder amount
        double totalRevenue = business.getStudentDirectory().getStudentList().size() * tuitionPerStudent;
        model.addRow(new Object[]{"Tuition Revenue Summary ($)", String.format("%.2f", totalRevenue)});

        // --- Footer summaries ---
        lblUsers.setText("👥 Total Active Users: " + totalUsers);
        lblCourses.setText("📚 Courses Offered: " + totalCourses);
        lblRevenue.setText("💰 Tuition Revenue: $" + String.format("%.2f", totalRevenue));
    }
}
