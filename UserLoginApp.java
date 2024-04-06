import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.JTableHeader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


 class UserSignUpApp extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton signUpButton;
    private JButton loginButton;

    public UserSignUpApp() {
        super("Sign Up Form");
        createUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
    }

    private void createUI() {
        setLayout(new FlowLayout());

        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        signUpButton = new JButton("Sign Up");
        loginButton = new JButton("Already have an account? Log in");

        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(signUpButton);
        add(loginButton);

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (registerUser(emailField.getText(), new String(passwordField.getPassword()))) {
                        JOptionPane.showMessageDialog(UserSignUpApp.this, "Account Created Successfully");
                        UserSignUpApp.this.setVisible(false);
                        UserSignUpApp.this.dispose();
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                new UserLoginApp().setVisible(true);
                            }
                        });
                        // Optionally, redirect the user to the login page or main app
                    } else {
                        JOptionPane.showMessageDialog(UserSignUpApp.this, "Failed to Create Account");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(UserSignUpApp.this, "Sign Up Failed: " + ex.getMessage());
                }
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserSignUpApp.this.setVisible(false); // Cachez la fenêtre de création de compte
                UserSignUpApp.this.dispose(); // Libérez les ressources de la fenêtre
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new UserLoginApp().setVisible(true); // Affichez l'écran de connexion
                    }
                });
            }
        });
    }

    private boolean registerUser(String email, String password) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fnoc?characterEncoding=UTF-8", "root", "")) {
            // Assuming there's a users table with email and password fields
            String query = "INSERT INTO projet (mail, password) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            int result = ps.executeUpdate();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserSignUpApp().setVisible(true);
            }
        });
    }
}
public class UserLoginApp extends JFrame {
    private JTextField userField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public UserLoginApp() {
        super("Login Form");
        createUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);
    }

    private void createUI() {
        setLayout(new FlowLayout());

        userField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        add(new JLabel("email:"));
        add(userField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (authenticate(userField.getText(), new String(passwordField.getPassword()))) {
                        JOptionPane.showMessageDialog(UserLoginApp.this, "Login Successful");
                        UserLoginApp.this.setVisible(false);
                        UserLoginApp.this.dispose();
                        // Affiche le gestionnaire de clients après une connexion réussie
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                new GestionClientSav().setVisible(true);
                            }
                        });
                    } else {
                        JOptionPane.showMessageDialog(UserLoginApp.this, "Invalid Username or Password");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(UserLoginApp.this, "Login Failed: " + ex.getMessage());
                }
            }
        });
    }

    private boolean authenticate(String username, String password) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection conn = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/fnoc?characterEncoding=UTF-8", "root", "")) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM projet WHERE mail = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // La classe GestionClientSav intégrée commence ici
    static class GestionClientSav extends JFrame {
        private JTextField nomField, prenomField, telephoneField, numeroTicketField;
        private JTextArea reclamationArea;
        private DefaultTableModel tableModel;
        private JTextField dateField;
        private JComboBox<String> etatTicketComboBox;
        private Connection conn; // Connexion à la base de données MySQL

        // Générez un numéro de ticket aléatoire
        Random random = new Random();

        private int genererNumeroTicketUnique() {
            int numeroTicket;
            do {
                numeroTicket = new Random().nextInt(1000) + 1;
            } while (ticketsTableContainsNumeroTicket(numeroTicket));

            return numeroTicket;
        }

        private Timer timer;

        private boolean ticketsTableContainsNumeroTicket(int numeroTicket) {
            // Votre logique pour vérifier si le numéro de ticket existe déjà dans le
            // tableau
            return false;
        }

        private JLabel createLogoLabel(int width, int height) {
            ImageIcon logoIcon = new ImageIcon("C:/Users/mat91/OneDrive/Bureau/java/images.png");
            Image scaledImage = logoIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);
            JLabel logoLabel = new JLabel(scaledLogoIcon);
            return logoLabel;
        }

        private void chargerDonneesDepuisBaseDeDonnees() {
            try {
                // Créez une requête SQL SELECT pour récupérer les données de la table
                String query = "SELECT nom, prenom, telephone, reclamation, Date, Numero_du_tickets, Etat_du_ticket FROM reclamation";

                // Créez une instruction SQL
                Statement statement = conn.createStatement();

                // Exécutez la requête et obtenez un résultat (ensemble de résultats)
                ResultSet resultSet = statement.executeQuery(query);

                // Effacez toutes les lignes existantes dans le tableau Swing
                tableModel.setRowCount(0);

                // Parcourez les résultats et ajoutez chaque ligne au modèle de tableau Swing
                while (resultSet.next()) {
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String telephone = resultSet.getString("telephone");
                    String reclamation = resultSet.getString("reclamation");
                    String date = resultSet.getString("date");
                    String numeroTicket = resultSet.getString("Numero_du_tickets");
                    String etatTicket = resultSet.getString("Etat_du_ticket");

                    String[] rowData = { nom, prenom, telephone, reclamation, date, numeroTicket,
                            etatTicket };
                    tableModel.addRow(rowData);
                }

                // Fermez le résultat et l'instruction
                resultSet.close();
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de la récupération des données : " + ex.getMessage(),
                        "Erreur SQL", JOptionPane.ERROR_MESSAGE);
            }
        }

        public GestionClientSav() {
            setTitle("Formulaire de demande d'assistance");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            try {
                // étape 1: charger la classe de driver
                Class.forName("com.mysql.jdbc.Driver");

                // étape 2: créer l'objet de connexion
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/fnoc?characterEncoding=UTF-8", "root",
                        "");

                // Initialisez l'objet tableModel ici
                String[] columnNames = { "Nom", "Prénom", "Téléphone", "Réclamation", "Date", "Numéro de ticket",
                        "État du ticket" };
                tableModel = new DefaultTableModel(columnNames, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        // Rendre toutes les cellules du tableau non éditables
                        return false;
                    }
                };

                // étape 3: créer l'objet statement
                Statement stmt = conn.createStatement();
                ResultSet res = stmt.executeQuery("SELECT * FROM reclamation");

                // étape 4: exécuter la requête
                while (res.next()) {
                    String id = res.getString(1);
                    String nom = res.getString(2);
                    String prenom = res.getString(3);
                    String telephone = res.getString(4);
                    String reclamation = res.getString(5);
                    String date = res.getString(6);
                    int numero_ticket = res.getInt(7);
                    String etat_ticket = res.getString(8);
                    // Ajouter les données au modèle de tableau

                    tableModel
                            .addRow(new Object[] { nom, prenom, telephone, reclamation, date, numero_ticket,
                                    etat_ticket });

                }

            } catch (Exception e) {
                System.out.println(e);
                System.out.println("erreur");
            }

            pack();
            setLocationRelativeTo(null);
            setVisible(true);

            JPanel formulairePanel = new JPanel(new BorderLayout(30, 30));
            JPanel inputPanel = new JPanel(new GridLayout(10, 1, 0, 0));
            JPanel tablePanel = new JPanel(new BorderLayout());

            add(formulairePanel);
            int logoWidth = 120;
            int logoHeight = 120;
            JLabel logoLabel = createLogoLabel(logoWidth, logoHeight);
            formulairePanel.add(logoLabel, BorderLayout.NORTH);

            inputPanel.add(new JLabel("Nom :"));
            nomField = new JTextField(20);
            inputPanel.add(nomField);

            inputPanel.add(new JLabel("Prénom :"));
            prenomField = new JTextField(20);
            inputPanel.add(prenomField);

            inputPanel.add(new JLabel("Telephone :"));
            telephoneField = new JTextField(20);
            inputPanel.add(telephoneField);

            inputPanel.add(new JLabel("Réclamation :"));
            reclamationArea = new JTextArea(5, 20);
            reclamationArea.setWrapStyleWord(true);
            reclamationArea.setLineWrap(true);
            inputPanel.add(new JScrollPane(reclamationArea));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(new Date());
            inputPanel.add(new JLabel("Date d'aujourd'hui :"));
            dateField = new JTextField(currentDate);
            dateField.setEditable(false);
            inputPanel.add(dateField);

            inputPanel.add(new JLabel("Numéro de ticket :"));
            numeroTicketField = new JTextField(20);
            numeroTicketField.setEditable(false); // Le champ numéro de ticket ne sera pas éditable manuellement
            inputPanel.add(numeroTicketField);

            etatTicketComboBox = new JComboBox<>(new String[] { "Ouvert", "En cours", "Fermé" });
            inputPanel.add(new JLabel("État du ticket :"));
            inputPanel.add(etatTicketComboBox);

            timer = new Timer(10000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chargerDonneesDepuisBaseDeDonnees(); // Appelez la méthode de chargement des données
                }
            });
            timer.start(); // Démarrez le timer

            JButton envoyerButton = new JButton("Envoyer");
            envoyerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String nom = nomField.getText();
                    String prenom = prenomField.getText();
                    String telephone = telephoneField.getText();
                    String reclamation = reclamationArea.getText();
                    String date = dateField.getText();
                    int numeroTicket = genererNumeroTicketUnique(); // Générez le numéro de ticket aléatoire
                    numeroTicketField.setText(String.valueOf(numeroTicket)); // Mettez à jour le champ numéro de ticket
                    String etatTicket = etatTicketComboBox.getSelectedItem().toString();

                    // Insérez les données dans la table de la base de données
                    try {
                        String query = "INSERT INTO reclamation (nom, prenom, telephone, reclamation, Date, Numero_du_tickets, Etat_du_ticket) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(query);
                        preparedStatement.setString(1, nom);
                        preparedStatement.setString(2, prenom);
                        preparedStatement.setString(3, telephone);
                        preparedStatement.setString(4, reclamation);
                        preparedStatement.setString(5, date);
                        preparedStatement.setInt(6, numeroTicket);
                        preparedStatement.setString(7, etatTicket);
                        preparedStatement.executeUpdate();
                        preparedStatement.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                                "Erreur lors de l'insertion des données : " + ex.getMessage(),
                                "Erreur SQL", JOptionPane.ERROR_MESSAGE);
                    }

                    // Ajoutez les données dans le modèle de la table Swing
                    String[] rowData = { nom, prenom, telephone, reclamation, date, String.valueOf(numeroTicket),
                            etatTicket };
                    tableModel.addRow(rowData);
                }
            });
            inputPanel.add(envoyerButton);

            formulairePanel.add(inputPanel, BorderLayout.CENTER);

            String[] columnNames = { "Nom", "Prénom", "Téléphone", "Réclamation", "Date", "Numéro de ticket",
                    "État du ticket" };
            tableModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Rendre toutes les cellules du tableau non éditables
                    return false;
                }
            };
            JTable tableau = new JTable(tableModel);

            JTableHeader header = tableau.getTableHeader();
            tablePanel.add(header, BorderLayout.NORTH);

            JScrollPane scrollPane = new JScrollPane(tableau);
            tablePanel.add(scrollPane, BorderLayout.CENTER);

            formulairePanel.add(tablePanel, BorderLayout.SOUTH);

            add(formulairePanel);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);

            JButton modifierButton = new JButton("Modifier");
            modifierButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = tableau.getSelectedRow();
                    if (selectedRow != -1) {
                        // Récupérez les valeurs des cellules de la ligne sélectionnée
                        String nouveauNom = nomField.getText();
                        String nouveauPrenom = prenomField.getText();
                        String nouveauTelephone = telephoneField.getText();
                        String nouvelleReclamation = reclamationArea.getText();
                        String nouvelleDate = dateField.getText();
                        String nouvelEtatTicket = etatTicketComboBox.getSelectedItem().toString();

                        // Mettez à jour les données dans le modèle de la table Swing
                        tableModel.setValueAt(nouveauNom, selectedRow, 0);
                        tableModel.setValueAt(nouveauPrenom, selectedRow, 1);
                        tableModel.setValueAt(nouveauTelephone, selectedRow, 2);
                        tableModel.setValueAt(nouvelleReclamation, selectedRow, 3);
                        tableModel.setValueAt(nouvelleDate, selectedRow, 4);
                        tableModel.setValueAt(nouvelEtatTicket, selectedRow, 6);

                        // Récupérez également le numéro de ticket pour la mise à jour de la base de
                        // données
                        int numeroTicket = Integer.parseInt(tableModel.getValueAt(selectedRow, 5).toString());

                        // Mettez à jour les données dans la base de données
                        mettreAJourTicket(numeroTicket, nouveauNom, nouveauPrenom, nouveauTelephone,
                                nouvelleReclamation,
                                nouvelleDate, nouvelEtatTicket);
                    } else {
                        JOptionPane.showMessageDialog(null, "Veuillez sélectionner une ligne à modifier.", "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            inputPanel.add(modifierButton);

            // Ajoutez un bouton "Supprimer" à votre inputPanel
            JButton supprimerButton = new JButton("Supprimer");
            supprimerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = tableau.getSelectedRow();
                    if (selectedRow != -1) { // Vérifiez si une ligne est sélectionnée
                        // Supprimez la ligne de la table Swing
                        tableModel.removeRow(selectedRow);

                        // Supprimez la ligne correspondante de la base de données
                        int numeroTicket = Integer.parseInt(tableModel.getValueAt(selectedRow, 5).toString());
                        supprimerLigneDeLaBaseDeDonnees(numeroTicket);
                        String query = "DELETE INTO reclamation (nom, prenom, telephone, reclamation, Date, Numero_du_tickets, Etat_du_ticket) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    } else {
                        JOptionPane.showMessageDialog(null, "Veuillez sélectionner une ligne à supprimer.",
                                "Avertissement",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
            inputPanel.add(supprimerButton);

            JButton comptageTicketsButton = new JButton("Comptage Tickets");
            inputPanel.add(comptageTicketsButton);

            comptageTicketsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    JFrame comptageFrame = new JFrame("Nombre de Tickets");
                    comptageFrame.setSize(300, 200);
                    comptageFrame.setLayout(new FlowLayout());
                    comptageFrame.setLocationRelativeTo(null); // Centrer le JFrame

                    int nombreDeTickets = compterTickets();

                    JLabel nombreTicketsLabel = new JLabel("Nombre total de tickets : " + nombreDeTickets);
                    comptageFrame.add(nombreTicketsLabel);

                    JButton closeButton = new JButton("Fermer");
                    comptageFrame.add(closeButton);

                    closeButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            comptageFrame.dispose(); // Ferme la fenêtre
                        }
                    });

                    comptageFrame.setVisible(true);
                }
            });
            // Fermez la connexion à la base de données lors de la fermeture de
            // l'application
            addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }

        private void mettreAJourTicket(int numeroTicket, String nouveauNom, String nouveauPrenom,
                String nouveauTelephone,
                String nouvelleReclamation, String nouvelleDate, String nouvelEtatTicket) {
            try {
                String query = "UPDATE reclamation SET nom = ?, prenom = ?, telephone = ?, reclamation = ?, Date = ?, Etat_du_ticket = ? WHERE Numero_du_tickets = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, nouveauNom);
                preparedStatement.setString(2, nouveauPrenom);
                preparedStatement.setString(3, nouveauTelephone);
                preparedStatement.setString(4, nouvelleReclamation);
                preparedStatement.setString(5, nouvelleDate);
                preparedStatement.setString(6, nouvelEtatTicket);
                preparedStatement.setInt(7, numeroTicket);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de la mise à jour du ticket : " + ex.getMessage(),
                        "Erreur SQL", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Méthode pour supprimer une ligne de la base de données
        private void supprimerLigneDeLaBaseDeDonnees(int numeroTicket) {
            try {
                // Créez la requête SQL pour supprimer le ticket avec le numéro correspondant
                String query = "DELETE FROM reclamation WHERE Numero_du_tickets = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, numeroTicket);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du ticket : " + ex.getMessage(),
                        "Erreur SQL", JOptionPane.ERROR_MESSAGE);
            }
        }

        private int compterTickets() {
            int nombreDeTickets = 0;
            try {
                String query = "SELECT COUNT(*) FROM reclamation";
                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    nombreDeTickets = resultSet.getInt(1);
                }
                resultSet.close();
                statement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Erreur lors de la récupération du nombre de tickets : " + ex.getMessage(),
                        "Erreur SQL", JOptionPane.ERROR_MESSAGE);
            }
            return nombreDeTickets;
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new GestionClientSav();
                }
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UserLoginApp().setVisible(true);
            }
        });
    }
}
