// Handles the login and logout process for the system.
public class Auth {
    private String username;
    private String password;

    public Auth() {
        this.username = "admin";
        this.password = "1234";
    }

    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public void logout() {
        System.out.println("Logged out.");
    }
}
