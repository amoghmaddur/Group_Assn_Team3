package Admin;

import Business.Business;
import Business.Person.Person;
import Business.Profiles.Profile;
import Business.UserAccounts.Role;
import javax.swing.*;
import java.awt.*;
import java.util.UUID;

/**
 * Panel for Admin to register new persons (students, faculty, or registrar).
 * Supports role linkage, duplicate prevention, and auto-generated University ID.
 * @author Manav
 */
public class PersonRegistrationPanel extends JPanel {

    private final Business business;
    private JTextField txtFirstName, txtLastName, txtEmail, txtPhone, txtAddress, txtUsername, txtPassword;
    private JButton btnRegister;

    public PersonRegistrationPanel(Business business, JPanel parentPanel) {
        this.business = business;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 255));

        JLabel title = new JLabel("Register New Person", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(0, 70, 140));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 247, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridx = 1;
        txtFirstName = new JTextField(15);
        formPanel.add(txtFirstName, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridx = 1;
        txtLastName = new JTextField(15);
        formPanel.add(txtLastName, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(15);
        formPanel.add(txtEmail, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        txtPhone = new JTextField(15);
        formPanel.add(txtPhone, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        txtAddress = new JTextField(15);
        formPanel.add(txtAddress, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        txtUsername = new JTextField(15);
        formPanel.add(txtUsername, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        txtPassword = new JTextField(15);
        formPanel.add(txtPassword, gbc);
        y++;

        // Register button
        btnRegister = new JButton("Register Person");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setBackground(new Color(0, 102, 204));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = y + 1; gbc.gridwidth = 2;
        formPanel.add(btnRegister, gbc);

        add(formPanel, BorderLayout.CENTER);

        btnRegister.addActionListener(e -> handleRegister());
    }

    /**
     * Handles registration of a new person with validation and linkage.
     */
    private void handleRegister() {
        String f = txtFirstName.getText().trim();
        String l = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String addr = txtAddress.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (f.isEmpty() || l.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields except address are required!");
            return;
        }

        // Prevent duplicate email registration
        for (Person p : business.getPersonDirectory().getPersonList()) {
            if (email.equalsIgnoreCase(p.getEmail())) {
                JOptionPane.showMessageDialog(this, "Email already registered!");
                return;
            }
        }

        // Generate unique University ID
        String uniId = "UNI-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        // Create Person
        Person p = business.getPersonDirectory().newPerson(f + " " + l);
        p.setEmail(email);
        p.setPhone(phone);
        p.setAddress(addr);
        p.setPersonId(uniId);

        // Choose Role
        String[] roles = {"Student", "Faculty", "Registrar"};
        String roleChoice = (String) JOptionPane.showInputDialog(
                this,
                "Select role for this person:",
                "Choose Role",
                JOptionPane.PLAIN_MESSAGE,
                null,
                roles,
                "Student"
        );

        if (roleChoice == null) return; // user cancelled

        // Create Profile and link with role
        Profile profile = null;
        switch (roleChoice.toLowerCase()) {
            case "student":
                profile = business.getStudentDirectory().newStudentProfile(p);
                break;
            case "faculty":
                profile = business.getFacultyDirectory().newFacultyProfile(p);
                break;
            case "registrar":
                profile = business.getRegistrarDirectory().newRegistrarProfile(p);
                break;
        }

        if (profile == null) {
            JOptionPane.showMessageDialog(this, "Failed to create profile!");
            return;
        }

        // Create User Account
        business.getUserAccountDirectory().newUserAccount(
                profile,
                username,
                password,
                Role.valueOf(roleChoice.toUpperCase())
        );

        JOptionPane.showMessageDialog(this,
                "✅ " + roleChoice + " registered successfully!\nUniversity ID: " + uniId,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        clearFields();
    }

    private void clearFields() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
    }
}
