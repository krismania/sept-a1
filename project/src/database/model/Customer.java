package database.model;

/**
 * A Customer in the system. Customer is an {@link Account} that also implements
 * {@link PersonalDetails}.
 *
 * @author krismania
 */
public class Customer extends Account implements PersonalDetails {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public Customer(String username, String firstName, String lastName, String email, String phoneNumber) {
        super(username);

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

    /**
     * Helper to format data types to Strings for processing
     *
     * @author krismania
     */
    @Override
    public String toString() {
        return String.format("Customer: %s, Name: %s %s, Email: %s, Phone: %s", username, firstName, lastName, email,
                phoneNumber);
    }
}
