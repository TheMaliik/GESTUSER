package Services;

import Entity.User;
import Securite.BCrypt;
import Utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TheMaliik
 */
public class ServiceUser implements IUser<User> {

    public static int idUser;
    public static String nom;
    public static String prenom;
    public static String email;
    public static int age;
    public static String role;
    static Connection connection;

    public ServiceUser() {
        connection = DBConnection.getInstance().getConnection();
    }

    public static List<User> afficher() throws SQLException {
        List<User> clients = new ArrayList<>();
        String req = "SELECT * FROM user";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                User p = new User();
                p.setId(rs.getInt("id"));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                p.setEmail(rs.getString("email"));
                p.setMotDePasse(rs.getString("motDePasse"));
                p.setAge(rs.getInt("age"));
                p.setRole(rs.getString("role"));
                clients.add(p);
            }
        }
        return clients;
    }

    @Override
    public void ajouter(User client) throws SQLException {
        if (client.getMotDePasse() == null || client.getMotDePasse().length() < 8) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères.");
        }
        String hashedPassword = BCrypt.hashpw(client.getMotDePasse(), BCrypt.gensalt(12));
        String req = "INSERT INTO user (nom, prenom, email, motDePasse, age, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(req)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, hashedPassword);
            stmt.setInt(5, client.getAge());
            stmt.setString(6, client.getRole());
            int result = stmt.executeUpdate();
            System.out.println(result + " enregistrement ajouté.");
        }
    }

    public void modifier(User client) throws SQLException {
        if (client.getMotDePasse() == null || client.getMotDePasse().length() < 8) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères.");
        }
        String hashedPassword = BCrypt.hashpw(client.getMotDePasse(), BCrypt.gensalt(12));
        String req = "UPDATE user SET nom=?, prenom=?, email=?, motDePasse=?, age=?, role=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(req)) {
            stmt.setString(1, client.getNom());
            stmt.setString(2, client.getPrenom());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, hashedPassword);
            stmt.setInt(5, client.getAge());
            stmt.setString(6, client.getRole());
            stmt.setInt(7, client.getId());
            int result = stmt.executeUpdate();
            System.out.println(result + " modifié avec succès !");
        }
    }

    public void supprimer(User client) throws SQLException {
        String req = "DELETE FROM user WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(req)) {
            stmt.setInt(1, client.getId());
            stmt.executeUpdate();
        }
    }

    public boolean existemail(String email) throws SQLException {
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public List<User> Search(String searchTerm) throws SQLException {
        List<User> allUsers = afficher();
        return allUsers.stream()
                .filter(user -> user.getNom().startsWith(searchTerm) ||
                        user.getPrenom().startsWith(searchTerm) ||
                        user.getEmail().startsWith(searchTerm) ||
                        user.getRole().startsWith(searchTerm))
                .collect(Collectors.toList());
    }

    public User findById(int id) throws SQLException {
        User u = new User();
        String sql = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    u.setId(rs.getInt("id"));
                    u.setNom(rs.getString("nom"));
                    u.setPrenom(rs.getString("prenom"));
                    u.setEmail(rs.getString("email"));
                    u.setMotDePasse(rs.getString("motDePasse"));
                    u.setAge(rs.getInt("age"));
                    u.setRole(rs.getString("role"));
                }
            }
        }
        return u;
    }

    public static String login(User t) {
        String message = "";
        try {
            if (t == null || t.getEmail() == null || t.getEmail().trim().isEmpty() || t.getMotDePasse() == null || t.getMotDePasse().trim().isEmpty()) {
                System.out.println("Login failed: Invalid user or empty fields - Email: " + (t != null ? t.getEmail() : "null") + ", Password: " + (t != null ? t.getMotDePasse() : "null"));
                return message = "Champs vide ou utilisateur non valide.";
            }

            String email = t.getEmail().trim();
            String password = t.getMotDePasse().trim();
            System.out.println("Attempting login with email: " + email);

            String req = "SELECT * FROM user WHERE LOWER(email) = LOWER(?)";
            try (PreparedStatement pst = connection.prepareStatement(req)) {
                pst.setString(1, email);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("motDePasse");
                        System.out.println("User found in database: Email=" + rs.getString("email") + ", Stored Password=" + storedPassword);

                        if (BCrypt.checkpw(password, storedPassword)) {
                            idUser = rs.getInt("id");
                            nom = rs.getString("nom");
                            prenom = rs.getString("prenom");
                            email = rs.getString("email");
                            age = rs.getInt("age");
                            role = rs.getString("role");
                            System.out.println("Login successful: Salut " + nom + " " + prenom + ", Role: " + role);
                        } else {
                            System.out.println("Password mismatch: Input password does not match stored hash");
                            return message = "Mot de passe incorrect.";
                        }
                    } else {
                        System.out.println("No user found with email: " + email);
                        return message = "Email non trouvé.";
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            return message = "Erreur lors de la connexion: " + e.getMessage();
        }
        return message;
    }

    public void ModifMDP(String email, String motDePasse) throws SQLException {
        if (motDePasse == null || motDePasse.length() < 8) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 8 caractères.");
        }
        String hashedPassword = BCrypt.hashpw(motDePasse, BCrypt.gensalt(12));
        String req = "UPDATE user SET motDePasse=? WHERE email=?";
        try (PreparedStatement stmt = connection.prepareStatement(req)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);
            stmt.executeUpdate();
        }
    }

    public List<User> triEmail() throws SQLException {
        List<User> users = afficher();
        return users.stream()
                .sorted((o1, o2) -> o1.getEmail().compareTo(o2.getEmail()))
                .collect(Collectors.toList());
    }

    public List<User> triNom() throws SQLException {
        List<User> users = afficher();
        return users.stream()
                .sorted((o1, o2) -> o1.getNom().compareTo(o2.getNom()))
                .collect(Collectors.toList());
    }
}