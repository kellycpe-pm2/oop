// Abstract class representing a general system user
// Organizer and Speaker will inherit from this class
public abstract class User {

    // Basic user information shared by all users
    protected String userID;
    protected String username;
    protected String email;
    protected String password;

    // Constructor to initialize user information
    public User(String userID, String username, String email, String password) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getter method to return user ID
    public String getUserID() {
        return userID;
    }

    // Getter method to return username
    public String getUsername() {
        return username;
    }

    // Abstract method (must be implemented by child classes)
    public abstract void displayInfo();
}