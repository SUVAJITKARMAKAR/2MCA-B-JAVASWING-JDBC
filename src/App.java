import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.FileWriter;
import java.io.IOException;

public class App extends JFrame {

    private JTextField usernameField, ageField, userIDField, followersField, followingField;

    public App() {
        setTitle("Social Media Application");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel ageLabel = new JLabel("Age:");
        ageField = new JTextField();

        JLabel userIDLabel = new JLabel("UserID:");
        userIDField = new JTextField();

        JLabel followersLabel = new JLabel("Followers:");
        followersField = new JTextField();

        JLabel followingLabel = new JLabel("Following:");
        followingField = new JTextField();

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitButtonClicked();
            }
        });

        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);
        mainPanel.add(ageLabel);
        mainPanel.add(ageField);
        mainPanel.add(userIDLabel);
        mainPanel.add(userIDField);
        mainPanel.add(followersLabel);
        mainPanel.add(followersField);
        mainPanel.add(followingLabel);
        mainPanel.add(followingField);
        mainPanel.add(new JLabel()); 
        mainPanel.add(submitButton);

        add(mainPanel);
    }

    private void submitButtonClicked() {
        String username = usernameField.getText();
        String age = ageField.getText();
        String userID = userIDField.getText();
        String followers = followersField.getText();
        String following = followingField.getText();

        // Connect to the MySQL database and store the information
        storeInDatabase(username, age, userID, followers, following);

        // Optionally, store the information in a text file
        storeInTextFile(username, age, userID, followers, following);

        // Display a message to the user
        String userInfo = "Username: " + username + "\n" +
                          "Age: " + age + "\n" +
                          "UserID: " + userID + "\n" +
                          "Followers: " + followers + "\n" +
                          "Following: " + following;

        JOptionPane.showMessageDialog(this, userInfo, "User Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void storeInDatabase(String username, String age, String userID, String followers, String following) {
        // Use JDBC to connect to the MySQL database and insert the data
        String jdbcUrl = "jdbc:mysql://localhost:3306/social_media";
        String dbUser = "SUVAJIT";
        String dbPassword = "SUVAJITk@8617";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String insertQuery = "INSERT INTO user_info (username, age, user_id, followers, following) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, age);
                preparedStatement.setString(3, userID);
                preparedStatement.setString(4, followers);
                preparedStatement.setString(5, following);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void storeInTextFile(String username, String age, String userID, String followers, String following) {
        // Store the information in a text file
        try (FileWriter writer = new FileWriter("user_info.txt", true)) {
            writer.write("Username: " + username + "\n");
            writer.write("Age: " + age + "\n");
            writer.write("UserID: " + userID + "\n");
            writer.write("Followers: " + followers + "\n");
            writer.write("Following: " + following + "\n");
            writer.write("\n"); // Add a newline for separation between entries
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Initialize the MySQL database (create the table if not exists)
        initializeDatabase();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App().setVisible(true);
            }
        });
    }

    private static void initializeDatabase() {
        // Use JDBC to create the table if not exists
        String jdbcUrl = "jdbc:mysql://localhost:3306/social_media";
        String dbUser = "SUVAJIT";
        String dbPassword = "SUVAJITk@8617";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword)) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS user_info (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50)," +
                    "age VARCHAR(10)," +
                    "user_id VARCHAR(20)," +
                    "followers VARCHAR(10)," +
                    "following VARCHAR(10))";

            try (PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
