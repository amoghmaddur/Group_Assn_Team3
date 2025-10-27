package Admin;

import Business.Business;
import Business.Person.Person;
import Business.Profiles.Profile;
import Business.UserAccounts.Role;
import Business.UserAccounts.UserAccount;
import Business.UserAccounts.UserAccountDirectory;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Admin panel for managing User Accounts (create, edit, delete).
 * Works perfectly with your current Business and UserAccount structure.
 * @author Manav
 */
public class UserAccountManagementPanel extends JPanel {

    private final Business business;
    private JTable tblAccounts;
    private DefaultTableModel model;
    private JTextField txtUsername, txtPassword;
    private JComboBox<String> roleDropdown;
    private JButton btnAdd, btnDelete, btnUpdate, btnRefresh;

    public UserAccountManagementPanel(Business business, JPanel rightPanel) {
        this.business = business;
        initComponents();
        populateTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 255));

        // --- Title ---
        JLabel title = new JLabel("User Account Management", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(0, 70, 140));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        // --- Table ---
        model = new DefaultTableModel(new Object[]{"Username", "Role", "Person Name"}, 0);
        tblAccounts = new JTable(model);
        tblAccounts.setRowHeight(25);
        tblAccounts.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblAccounts.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(new JScrollPane(tblAccounts), BorderLayout.CENTER);

        // --- Form Section ---
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBackground(new Color(230, 238, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        txtUsername = new JTextField();
        txtPassword = new JTextField();
        roleDropdown = new JComboBox<>(new String[]{"Admin", "Student", "Faculty", "Registrar"});

        formPanel.add(new JLabel("Username:"));
        formPanel.add(txtUsername);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(txtPassword);
        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleDropdown);

        add(formPanel, BorderLayout.NORTH);

        // --- Buttons ---
        btnAdd = new JButton("Add Account");
        btnUpdate = new JButton("Update Password");
        btnDelete = new JButton("Delete Account");
        btnRefresh = new JButton("Refresh");

        styleButton(btnAdd);
        styleButton(btnUpdate);
        styleButton(btnDelete);
        styleButton(btnRefresh);

        btnAdd.addActionListener(this::handleAdd);
        btnUpdate.addActionListener(this::handleUpdate);
        btnDelete.addActionListener(this::handleDelete);
        btnRefresh.addActionListener(e -> populateTable());

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBackground(new Color(245, 247, 255));
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);

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

    /** Populates the table with existing user accounts */
    private void populateTable() {
        model.setRowCount(0);
        for (UserAccount ua : business.getUserAccountDirectory().getUserAccountList()) {
            Profile p = ua.getAssociatedPersonProfile();
            String name = (p != null && p.getPerson() != null)
                    ? p.getPerson().getPersonName()
                    : "N/A";
            model.addRow(new Object[]{ua.getUsername(), ua.getRole(), name});
        }
    }

    /** Add new user account */
    private void handleAdd(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String roleStr = (String) roleDropdown.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and Password required!");
            return;
        }

        UserAccountDirectory uad = business.getUserAccountDirectory();

        // Prevent duplicates
        for (UserAccount ua : uad.getUserAccountList()) {
            if (ua.getUsername().equalsIgnoreCase(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists!");
                return;
            }
        }

        // Create or link a new person
        Person person = business.getPersonDirectory().newPerson(username);
        Profile profile = null;

        switch (roleStr) {
            case "Admin":
                profile = business.getEmployeeDirectory().newEmployeeProfile(person);
                break;
            case "Student":
                profile = business.getStudentDirectory().newStudentProfile(person);
                break;
            case "Faculty":
                profile = business.getFacultyDirectory().newFacultyProfile(person);
                break;
            case "Registrar":
                profile = business.getRegistrarDirectory().newRegistrarProfile(person);
                break;
        }

        if (profile != null) {
            Role role = Role.valueOf(roleStr.toUpperCase());
            uad.newUserAccount(profile, username, password, role);
            JOptionPane.showMessageDialog(this, "Account created successfully!");
            populateTable();
        }
    }

    /** Update user password */
    private void handleUpdate(ActionEvent e) {
        int row = tblAccounts.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an account to update!");
            return;
        }

        String username = (String) model.getValueAt(row, 0);
        String newPassword = JOptionPane.showInputDialog(this, "Enter new password:");
        if (newPassword == null || newPassword.isEmpty()) return;

        for (UserAccount ua : business.getUserAccountDirectory().getUserAccountList()) {
            if (ua.getUsername().equals(username)) {
                ua.setPassword(newPassword);
                JOptionPane.showMessageDialog(this, "Password updated!");
                populateTable();
                return;
            }
        }
    }

    /** Delete user account */
    private void handleDelete(ActionEvent e) {
        int row = tblAccounts.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an account to delete!");
            return;
        }

        String username = (String) model.getValueAt(row, 0);
        business.getUserAccountDirectory().getUserAccountList()
                .removeIf(ua -> ua.getUsername().equals(username));

        JOptionPane.showMessageDialog(this, "Account deleted!");
        populateTable();
    }
}
