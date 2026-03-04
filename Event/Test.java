package Event;

import java.util.Scanner;

public class Test {
    Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        String[] information = new String[4];
        user userObj = new user();  
        System.out.println(userObj.getUsername(0));
        Test testObj = new Test();
       information = testObj.displaySignUpInterface(information);

        signUp signUpObj = new signUp(information[0], information[1], information[2], information[3]);
        userObj.createAccount(signUpObj);
        
        
        
    }

    public String[] displaySignUpInterface(String[] information) {
            System.out.println("--------------------------Sign Up--------------------------");
            System.out.println("Please enter your name: ");
            information[0] = scan.nextLine();            
            System.out.println("Please enter your email: ");
            information[1] = scan.nextLine();
            System.out.println("Please enter your password: ");
            information[2] = scan.nextLine();
            System.out.println("Please confirm your password: ");
            information[3] = scan.nextLine();
            System.out.println("------------------------------------------------------------");
        
         
        return information;
    }
}
