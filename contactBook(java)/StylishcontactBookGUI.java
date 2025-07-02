import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Contact {
    String name, phone, email;

    Contact(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    Object[] toRow() {
        return new Object[]{name, phone, email};
    }
}

public class StylishContactBookGUI extends JFrame {
    private JTextField nameField, phoneField, emailField, searchField;
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<Contact> contactList = new ArrayList<>();

    public StylishContactBookGUI() {
        setTitle("Stylish Contact Book");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 💡 Header Banner
        JPanel header = new JPanel();
        header.setBackground(new Color(60, 90, 150));
        header.setPreferredSize(new Dimension(900, 80));
        JLabel title = new JLabel("📘 Contact Book");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // 🔧 Form Panel (Left side)
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        formPanel.setPreferredSize(new Dimension(280, 500));
        formPanel.setBorder(BorderFactory.createTitledBorder("Contact Details"));

        nameField = new JTextField();
        phoneField = new JTextField();
        emailField = new JTextField();

        nameField.setPreferredSize(new Dimension(180, 28));
        phoneField.setPreferredSize(new Dimension(180, 28));
        emailField.setPreferredSize(new Dimension(180, 28));

        customizeField(nameField);
        customizeField(phoneField);
        customizeField(emailField);

        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        customizeButton(addBtn);
        customizeButton(updateBtn);

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(addBtn);
        formPanel.add(updateBtn);

        // 📋 Table Panel (Right side)
        tableModel = new DefaultTableModel(new String[]{"Name", "Phone", "Email"}, 0);
        table = new JTable(tableModel);

        // Column width fix
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(200);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Contacts"));

        // ✅ Split Panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, scrollPane);
        splitPane.setResizeWeight(0.3);
        add(splitPane, BorderLayout.CENTER);

        // 🔍 Bottom Panel
        JPanel bottomPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        JButton deleteBtn = new JButton("Delete");

        customizeField(searchField);
        customizeButton(searchBtn);
        customizeButton(deleteBtn);

        bottomPanel.add(new JLabel("🔍 Search:"));
        bottomPanel.add(searchField);
        bottomPanel.add(searchBtn);
        bottomPanel.add(deleteBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // 🧠 Actions
        addBtn.addActionListener(e -> addContact());
        updateBtn.addActionListener(e -> updateContact());
        deleteBtn.addActionListener(e -> deleteContact());
        searchBtn.addActionListener(e -> searchContact());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                nameField.setText((String) table.getValueAt(row, 0));
                phoneField.setText((String) table.getValueAt(row, 1));
                emailField.setText((String) table.getValueAt(row, 2));
            }
        });

        setVisible(true);
    }

    private void addContact() {
    String name = nameField.getText().trim();
    String phone = phoneField.getText().trim();
    String email = emailField.getText().trim();

    if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill all fields");
        return;
    }

    if (!isValidPhone(phone)) {
        JOptionPane.showMessageDialog(this, "Phone number must be 10 digits");
        return;
    }

    if (!isValidEmail(email)) {
        JOptionPane.showMessageDialog(this, "Invalid email format");
        return;
    }

    Contact contact = new Contact(name, phone, email);
    contactList.add(contact);
    tableModel.addRow(contact.toRow());
    clearFields();
}


    private void updateContact() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow >= 0) {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields required!");
            return;
        }

        if (!isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10 digits");
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email format");
            return;
        }

        Contact contact = contactList.get(selectedRow);
        contact.name = name;
        contact.phone = phone;
        contact.email = email;

        tableModel.setValueAt(name, selectedRow, 0);
        tableModel.setValueAt(phone, selectedRow, 1);
        tableModel.setValueAt(email, selectedRow, 2);

        clearFields();
    } else {
        JOptionPane.showMessageDialog(this, "Select a contact to update.");
    }
}


    private void deleteContact() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            contactList.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Select a contact to delete.");
        }
    }

    private void searchContact() {
        String query = searchField.getText().trim().toLowerCase();
        tableModel.setRowCount(0);
        for (Contact c : contactList) {
            if (c.name.toLowerCase().contains(query)) {
                tableModel.addRow(c.toRow());
            }
        }
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }
    private boolean isValidEmail(String email) {
    return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
}

private boolean isValidPhone(String phone) {
    return phone.matches("^\\d{10}$"); // 10-digit phone number
}


    private void customizeButton(JButton btn) {
        btn.setBackground(new Color(60, 130, 190));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }

    private void customizeField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StylishContactBookGUI::new);
    }
}
