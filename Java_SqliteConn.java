package Java_SqliteConn;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import org.mindrot.jbcrypt.BCrypt;

public class Java_SqliteConn extends javax.swing.JFrame {

    Connection sqlConn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    int q, i;

    public Java_SqliteConn() {
        initComponents();
        setTitle("Client Management System");
        setSize(800, 700);
        setLocationRelativeTo(null);
        updateDB();
    }

    public void updateDB() {
        try {

            Class.forName("org.sqlite.JDBC");

            // Connect to SQLite database
            sqlConn = DriverManager.getConnection("jdbc:sqlite:identifier.sqliteclient");

            pst = sqlConn.prepareStatement("SELECT * FROM clients");

            rs = pst.executeQuery();
            ResultSetMetaData stData = rs.getMetaData();

            q = stData.getColumnCount();

            DefaultTableModel RecordTable = (DefaultTableModel) jTable1.getModel();
            RecordTable.setRowCount(0);

            while (rs.next()) {
                Vector<String> columnData = new Vector<>();

                for (i = 1; i <= q; i++) {
                    columnData.add(rs.getString("id"));
                    columnData.add(rs.getString("username"));
                    columnData.add(rs.getString("name"));
                    columnData.add(rs.getString("surname"));
                    columnData.add(rs.getString("profession"));
                    columnData.add(rs.getString("experience_level"));
                    columnData.add(rs.getString("created_at"));
                }
                RecordTable.addRow(columnData);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new JPanel();
        jPanel2 = new JPanel();
        JLabel jLabel1 = new JLabel();
        JLabel jLabel2 = new JLabel();
        JLabel jLabel3 = new JLabel();
        JLabel jLabel4 = new JLabel();
        JLabel jLabel5 = new JLabel();
        JLabel jLabel6 = new JLabel();
        jtxtUsername = new JTextField();
        jtxtName = new JTextField();
        jtxtSurname = new JTextField();
        jtxtProfession = new JTextField();
        jtxtExperienceLevel = new JComboBox<>(new String[]{"Entry", "Intermediate", "Expert"});
        jPasswordField = new JPasswordField();
        JScrollPane jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        JButton jbtnAdd = new JButton();
        JButton jbtnReset = new JButton();
        JButton jbtnExit = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        jPanel1.setBorder(BorderFactory.createTitledBorder("Client Form"));
        jPanel1.setLayout(new GridLayout(7, 2, 10, 10));

        jLabel2.setText("Username:");
        jPanel1.add(jLabel2);
        jPanel1.add(jtxtUsername);

        jLabel3.setText("Name:");
        jPanel1.add(jLabel3);
        jPanel1.add(jtxtName);

        jLabel4.setText("Surname:");
        jPanel1.add(jLabel4);
        jPanel1.add(jtxtSurname);

        jLabel5.setText("Profession:");
        jPanel1.add(jLabel5);
        jPanel1.add(jtxtProfession);

        jLabel6.setText("Experience Level:");
        jPanel1.add(jLabel6);
        jPanel1.add(jtxtExperienceLevel);

        JLabel jLabel7 = new JLabel("Password:");
        jPanel1.add(jLabel7);
        jPanel1.add(jPasswordField);

        getContentPane().add(jPanel1, BorderLayout.NORTH);

        jTable1.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Username", "Name", "Surname", "Profession", "Experience Level", "Created At"}
        ));
        jScrollPane1.setViewportView(jTable1);
        getContentPane().add(jScrollPane1, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        jbtnAdd.setText("Add Client");
        jbtnAdd.addActionListener(evt -> jbtnAddActionPerformed());

        jbtnReset.setText("Reset");
        jbtnReset.addActionListener(evt -> resetForm());

        jbtnExit.setText("Exit");
        jbtnExit.addActionListener(evt -> System.exit(0));

        buttonPanel.add(jbtnAdd);
        buttonPanel.add(jbtnReset);
        buttonPanel.add(jbtnExit);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        pack();
    }

    private void jbtnAddActionPerformed() {
        if (jtxtUsername.getText().equals("") || jtxtName.getText().equals("") || jtxtSurname.getText().equals("") ||
                jtxtProfession.getText().equals("") || new String(jPasswordField.getPassword()).equals("")) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
        } else {
            String username = jtxtUsername.getText();
            String name = jtxtName.getText();
            String surname = jtxtSurname.getText();
            String profession = jtxtProfession.getText();
            String experienceLevel = jtxtExperienceLevel.getSelectedItem().toString();
            String password = new String(jPasswordField.getPassword());

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            try {
                Class.forName("org.sqlite.JDBC");
                sqlConn = DriverManager.getConnection("jdbc:sqlite:identifier.sqliteclient");
                pst = sqlConn.prepareStatement("INSERT INTO clients(username, name, surname, profession, experience_level, password) VALUES (?, ?, ?, ?, ?, ?)");

                pst.setString(1, username);
                pst.setString(2, name);
                pst.setString(3, surname);
                pst.setString(4, profession);
                pst.setString(5, experienceLevel);
                pst.setString(6, hashedPassword);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Client Added Successfully");
                updateDB();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    private void resetForm() {
        jtxtUsername.setText("");
        jtxtName.setText("");
        jtxtSurname.setText("");
        jtxtProfession.setText("");
        jPasswordField.setText("");
        jtxtExperienceLevel.setSelectedIndex(0);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Java_SqliteConn().setVisible(true));
    }

    private JPanel jPanel1;
    private JPanel jPanel2;
    private JTable jTable1;
    private JTextField jtxtUsername;
    private JTextField jtxtName;
    private JTextField jtxtSurname;
    private JTextField jtxtProfession;
    private JComboBox<String> jtxtExperienceLevel;
    private JPasswordField jPasswordField;
}
