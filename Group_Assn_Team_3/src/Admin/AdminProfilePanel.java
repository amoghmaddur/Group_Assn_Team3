package Admin;

import Business.Business;
import Business.Person.Person;
import Business.UserAccounts.UserAccount;
import Business.UserAccounts.UserAccountDirectory;
import javax.swing.*;
import java.awt.*;

/**
 * Admin Profile Panel — allows admin to view and edit their own profile.
 * @author Manav
 */
public class AdminProfilePanel extends JPanel {

    private final Business business;
    private JTextField txtName, txtEmail, txtPhone, txtUsername, txtPassword;
    private JButton btnSave;
    private UserAccount adminAccount;

    public AdminProfilePanel(Business business, JPanel rightPanel) {
        this.business = business;
        this.adminAccount = findAdminAccount();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 255));

        JLabel title = new JLabel("My Profile", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(0, 70, 140));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 15, 15));
        formPanel.setBackground(new Color(245, 247, 255));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 200, 40, 200));

        txtName = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JTextField();

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(txtPhone);
        formPanel.add(new JLabel("Username:"));
        formPanel.add(txtUsername);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(txtPassword);

        btnSave = new JButton("Save Changes");
        styleButton(btnSave);
        btnSave.addActionListener(e -> saveProfile());
        formPanel.add(new JLabel(""));
        formPanel.add(btnSave);

        add(formPanel, BorderLayout.CENTER);
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(0, 102, 204));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
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

    private UserAccount findAdminAccount() {
        for (UserAccount ua : business.getUserAccountDirectory().getUserAccountList()) {
            if (ua.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return ua;
            }
        }
        return null;
    }

    private void loadData() {
        if (adminAccount == null) {
            JOptionPane.showMessageDialog(this, "Admin account not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Person p = adminAccount.getAssociatedPersonProfile().getPerson();
        txtName.setText(p.getPersonName());
        txtEmail.setText(p.getEmail());
        txtPhone.setText(p.getPhone());
        txtUsername.setText(adminAccount.getUsername());
        txtPassword.setText(adminAccount.getPassword());
    }

    private void saveProfile() {
        if (adminAccount == null) return;

        Person p = adminAccount.getAssociatedPersonProfile().getPerson();

        p.setPersonName(txtName.getText());
        p.setEmail(txtEmail.getText());
        p.setPhone(txtPhone.getText());
        adminAccount.setUsername(txtUsername.getText());
        adminAccount.setPassword(txtPassword.getText());

        JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
