import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class formulaire {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Formulaire de Connexion");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel emailLabel = new JLabel("Email :");
        emailLabel.setBounds(10, 20, 80, 25);
        panel.add(emailLabel);

        JTextField emailText = new JTextField(20);
        emailText.setBounds(100, 20, 165, 25);
        panel.add(emailText);

        JLabel passwordLabel = new JLabel("Mot de passe :");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Se Connecter");
        loginButton.setBounds(10, 80, 150, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailText.getText();
                String password = new String(passwordText.getPassword());

                if (verifierUtilisateur(email, password)) {
                    JOptionPane.showMessageDialog(null, "Connexion réussie !");
                    // Ouvrir le code Java supplémentaire ici
                    // ...
                } else {
                    JOptionPane.showMessageDialog(null, "Email ou mot de passe incorrect.");
                }
            }
        });
    }

    public static boolean verifierUtilisateur(String email, String motDePasse) {
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop_db?characterEncoding=UTF-8", "root", "");
            String sql = "SELECT * FROM users WHERE email = ?";
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, email);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String hashedPasswordFromDB = resultSet.getString("password"); // Récupérer le mot de passe haché de la base de données
                    String hashedPassword = hashMotDePasse(motDePasse); // Hacher le mot de passe saisi par l'utilisateur
                    
                    // Comparer les mots de passe hachés
                    return hashedPasswordFromDB.equals(hashedPassword);
                }
                return false; // Aucun utilisateur avec cet email n'a été trouvé
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Gérer la fermeture de la connexion
        }
    }

    private static String hashMotDePasse(String motDePasse) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(motDePasse.getBytes());
            
            // Convertir le tableau de bytes en représentation hexadécimale
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}