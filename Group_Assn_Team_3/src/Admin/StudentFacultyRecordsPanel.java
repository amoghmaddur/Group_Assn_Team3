package Admin;

import Business.Business;
import Business.Person.Person;
import Business.Profiles.FacultyProfile;
import Business.Profiles.StudentProfile;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Admin — Manage and search Student & Faculty records.
 * Supports Name, ID, and Department search, edit, and delete.
 * @author Manav
 */
public class StudentFacultyRecordsPanel extends JPanel {

    private final Business business;
    private JTable tblRecords;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JComboBox<String> searchTypeDropdown;
    private JButton btnSearch, btnEdit, btnDelete, btnRefresh;

    public StudentFacultyRecordsPanel(Business business, JPanel parentPanel) {
        this.business = business;
        initComponents();
        populateTable("");
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 255));

        JLabel title = new JLabel("Student & Faculty Records Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(0, 70, 140));
        add(title, BorderLayout.NORTH);

        // --- Table Section ---
        model = new DefaultTableModel(new Object[]{"ID", "Name", "Role", "Department", "Email"}, 0);
        tblRecords = new JTable(model);
        tblRecords.setRowHeight(25);
        add(new JScrollPane(tblRecords), BorderLayout.CENTER);

        // --- Search Bar ---
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBackground(new Color(245, 247, 255));
        searchTypeDropdown = new JComboBox<>(new String[]{"Name", "ID", "Department"});
        txtSearch = new JTextField(15);
        btnSearch = new JButton("Search");
        btnRefresh = new JButton("Refresh");
        styleButton(btnSearch);
        styleButton(btnRefresh);
        btnSearch.addActionListener(e -> searchRecords());
        btnRefresh.addActionListener(e -> populateTable(""));

        searchPanel.add(new JLabel("Search by:"));
        searchPanel.add(searchTypeDropdown);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnRefresh);
        add(searchPanel, BorderLayout.NORTH);

        // --- Bottom Buttons ---
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(new Color(245, 247, 255));

        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");
        styleButton(btnEdit);
        styleButton(btnDelete);

        btnEdit.addActionListener(this::handleEdit);
        btnDelete.addActionListener(this::handleDelete);

        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);

        add(btnPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(0, 102, 204));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(30, 130, 230));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 102, 204));
            }
        });
    }

    private void populateTable(String filter) {
        model.setRowCount(0);

        // --- Students ---
        for (StudentProfile sp : business.getStudentDirectory().getStudentList()) {
            Person p = sp.getPerson();
            if (filter.isEmpty() || p.getPersonName().toLowerCase().contains(filter.toLowerCase())) {
                model.addRow(new Object[]{
                        p.getPersonId(),
                        p.getPersonName(),
                        "Student",
                        sp.getDepartment() == null ? "N/A" : sp.getDepartment(),
                        p.getEmail()
                });
            }
        }

        // --- Faculty ---
        for (FacultyProfile fp : business.getFacultyDirectory().getFacultyList()) {
            Person p = fp.getPerson();
            if (filter.isEmpty() || p.getPersonName().toLowerCase().contains(filter.toLowerCase())) {
                model.addRow(new Object[]{
                        p.getPersonId(),
                        p.getPersonName(),
                        "Faculty",
                        fp.getDepartment() == null ? "N/A" : fp.getDepartment(),
                        p.getEmail()
                });
            }
        }
    }

    private void searchRecords() {
        String type = (String) searchTypeDropdown.getSelectedItem();
        String query = txtSearch.getText().trim().toLowerCase();

        model.setRowCount(0);
        if (query.isEmpty()) {
            populateTable("");
            return;
        }

        for (StudentProfile sp : business.getStudentDirectory().getStudentList()) {
            Person p = sp.getPerson();
            boolean match = false;
            switch (type) {
                case "Name":
                    match = p.getPersonName().toLowerCase().contains(query);
                    break;
                case "ID":
                    match = p.getPersonId().toLowerCase().contains(query);
                    break;
                case "Department":
                    match = sp.getDepartment() != null && sp.getDepartment().toLowerCase().contains(query);
                    break;
            }
            if (match) {
                model.addRow(new Object[]{p.getPersonId(), p.getPersonName(), "Student",
                        sp.getDepartment() == null ? "N/A" : sp.getDepartment(), p.getEmail()});
            }
        }

        for (FacultyProfile fp : business.getFacultyDirectory().getFacultyList()) {
            Person p = fp.getPerson();
            boolean match = false;
            switch (type) {
                case "Name":
                    match = p.getPersonName().toLowerCase().contains(query);
                    break;
                case "ID":
                    match = p.getPersonId().toLowerCase().contains(query);
                    break;
                case "Department":
                    match = fp.getDepartment() != null && fp.getDepartment().toLowerCase().contains(query);
                    break;
            }
            if (match) {
                model.addRow(new Object[]{p.getPersonId(), p.getPersonName(), "Faculty",
                        fp.getDepartment() == null ? "N/A" : fp.getDepartment(), p.getEmail()});
            }
        }
    }

    private void handleEdit(ActionEvent e) {
        int row = tblRecords.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a record to edit!");
            return;
        }

        String name = (String) model.getValueAt(row, 1);
        String newDept = JOptionPane.showInputDialog(this, "Enter new department:");
        String newEmail = JOptionPane.showInputDialog(this, "Enter new email:");

        if ((newDept == null || newDept.isEmpty()) && (newEmail == null || newEmail.isEmpty())) return;

        for (StudentProfile sp : business.getStudentDirectory().getStudentList()) {
            if (sp.getPerson().getPersonName().equals(name)) {
                if (newDept != null && !newDept.isEmpty()) sp.setDepartment(newDept);
                if (newEmail != null && !newEmail.isEmpty()) sp.getPerson().setEmail(newEmail);
                JOptionPane.showMessageDialog(this, "Student record updated!");
                populateTable("");
                return;
            }
        }

        for (FacultyProfile fp : business.getFacultyDirectory().getFacultyList()) {
            if (fp.getPerson().getPersonName().equals(name)) {
                if (newDept != null && !newDept.isEmpty()) fp.setDepartment(newDept);
                if (newEmail != null && !newEmail.isEmpty()) fp.getPerson().setEmail(newEmail);
                JOptionPane.showMessageDialog(this, "Faculty record updated!");
                populateTable("");
                return;
            }
        }
    }

    private void handleDelete(ActionEvent e) {
        int row = tblRecords.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a record to delete!");
            return;
        }

        String name = (String) model.getValueAt(row, 1);

        business.getStudentDirectory().getStudentList().removeIf(s -> s.getPerson().getPersonName().equals(name));
        business.getFacultyDirectory().getFacultyList().removeIf(f -> f.getPerson().getPersonName().equals(name));

        JOptionPane.showMessageDialog(this, "Record deleted successfully!");
        populateTable("");
    }
}
