package Event;

import java.util.Scanner;

public class Test {
    
    public static void main(String[] args) {
        String[] information = new String[4];
        user userObj = new user();  
        System.out.println(userObj.getUsername(0));
        Test testObj = new Test();
        signUp signUpObj = new signUp();
       information = displaySignUpInterface(information, signUpObj);

        
        userObj.createAccount(signUpObj);
        
        
        
    }

    public static  String[] displaySignUpInterface(String[] information, signUp signUpObj) {
        Scanner scan = new Scanner(System.in);

        boolean result=true;
        while(result){
            String[] msg = {"Please enter your name: ", "Please enter your email: ", "Please enter your password: ", "Please confirm your password: "};
            Boolean [] validationResult = {signUpObj.validationExist(information[0]),signUpObj.validationName(information[0]), signUpObj.validationEmail(information[1]), signUpObj.validationPassword(information[2]), signUpObj.validationPassword2(information[3])};
            result = false;
            System.out.println("--------------------------Sign Up--------------------------");
            
            while (result){
                for (int i=0;i<4;i++){
                 System.out.println(msg[i]);
                 information[i]=scan.nextLine();
                 if(signUpObj.validationEmpty(information[i])){
                    result= false;
                    break;
                 }
                 else if (!(validationResult[i])){
                    result= false;
                    break;
                 }  
                 result = true; 
                }
            }
            


            System.out.println("------------------------------------------------------------");
        
        }

         
        return information;
    }
}
