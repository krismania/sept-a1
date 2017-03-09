package main;
/**
 * Created by James on 09-Mar-17.
 */
public class Business {
    //Variables
    private String businessName;
    private String businessAddr;
    private String businessPhn;

    //Constructor
    public Business() { }

    public Business(String businessName, String businessAddr,
                    String businessPhn)
    {
        this.businessName = businessName;
        this.businessAddr = businessAddr;
        this.businessPhn = businessPhn;
    }

    public String getBusinessName()
    {
        return businessName;
    }

    public String getBusinessAddr()
    {
        return businessAddr;
    }

    public String getBusinessPhn()
    {
        return businessPhn;
    }

    /* METHOD: displayBusinessBookings
     * Display bookings for particular business. Needs to have an option to look at new bookings
     * need to specify what a new booking is vs old booking.(JM)
    */
    public void displayBusinessBookings(String businessName)
    {
        System.out.println("Welcome back " + businessName);
        System.out.println("Please wait while we pull up your bookings...");

        //Fetch bookings from bookings database/file
    }
}
