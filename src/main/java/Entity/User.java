package Entity;

import java.util.Objects;

/*
@author TheMaliik
 */
public class User {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private int age;
    private String role; // admin, participant, organisateur

    public static User Current_User;

    public User() {}

    public User(int id) {
        this.id = id;
    }

    public User(int id, String nom, String prenom, String email, String motDePasse, int age, String role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.age = age;
        this.role = role;
    }

    public User(int id, String nom, String prenom, String email, String role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
    }

    public User(String nom, String prenom, String email, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
    }

    public User(int id, String nom, String prenom, String email, String motDePasse, String role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    public User(String email, String motDePasse) {
        this.email = email;
        this.motDePasse = motDePasse;
    }

    public User(String email, String motDePasse, String role) {
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (role == null || role.equals("admin") || role.equals("participant") || role.equals("organisateur")) {
            this.role = role;
        } else {
            throw new IllegalArgumentException("Role must be 'admin', 'participant', or 'organisateur'");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                getAge() == user.getAge() &&
                Objects.equals(getNom(), user.getNom()) &&
                Objects.equals(getPrenom(), user.getPrenom()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getMotDePasse(), user.getMotDePasse()) &&
                Objects.equals(getRole(), user.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNom(), getPrenom(), getEmail(), getMotDePasse(), getAge(), getRole());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", age=" + age +
                ", role='" + role + '\'' +
                '}';
    }

    public static User getCurrent_User() {
        return Current_User;
    }

    public static void setCurrent_User(User Current_User) {
        User.Current_User = Current_User;
    }
}