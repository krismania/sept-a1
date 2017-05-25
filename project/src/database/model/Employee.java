package database.model;

/**
 * An employee in the system. Each employee is identified by their ID and has
 * personal information as specified in {@link PersonalDetails}
 * 
 * @author krismania
 */
public class Employee implements PersonalDetails {
    public final int ID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    // TN Constructor
    public Employee(int ID, String firstName, String lastName, String email, String phoneNumber) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public void setFirstName(String name) {
        firstName = name;
    }

    @Override
    public void setLastName(String name) {
        lastName = name;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setPhoneNumber(String phone) {
        phoneNumber = phone;
    }
}
