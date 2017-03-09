package main;
/**
 * Created by James on 09-Mar-17.
 */
public class BusinessOwner {
    //Variables
    private String businessOwner;
    private String businessUser;
    private String businessPwd;

    //Constructors
    public BusinessOwner() { }

    public BusinessOwner(String bOwner, String bUser, String bPwd)
    {
        this.businessOwner = bOwner;
        this.businessUser = bUser;
        this.businessPwd = bPwd;
    }

    //Getters
    public String getBusinessOwner()
    {
        return businessOwner;
    }

    public String getBusinessUser()
    {
        return businessUser;
    }

    public String getBusinessPwd()
    {
        return businessPwd;
    }

    //User forget password option. Need to filter password to ensure matches
    //criteria
    public void changePassword(String newPwd) {
        businessPwd = newPwd;
        System.out.println("Password successfully changed!");
    }
}
