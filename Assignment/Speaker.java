// Speaker class inherits from User
public class Speaker extends User {

    // Constructor to create a speaker object
    public Speaker(String id, String name, String email, String password) {
        // Call parent constructor
        super(id, name, email, password);
    }

    // Implementation of abstract method from User class
    @Override
    public void displayInfo() {
        System.out.println("Speaker ID: " + userID + " Name: " + username);
    }
}