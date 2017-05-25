package database.model;

/**
 * Business owner is a basic type of {@link Account}, that additionally stores
 * the name of the owner's business.
 *
 * @author krismania
 */
public class BusinessOwner extends Account {
    private String businessName;
    private String name;
    private String address;
    private String phoneNumber;

    public BusinessOwner(String username, String businessName, String ownerName, String address, String phone) {
        super(username);

        this.businessName = businessName;
        this.name = ownerName;
        this.address = address;
        this.phoneNumber = phone;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    /**
     * Helper to format data types to Strings for processing
     *
     * @author krismania
     */
    @Override
    public String toString() {
        return String.format("BO: %s, Name: %s, Business: %s, Address: %s, Phone: %s", username, name, businessName,
                address, phoneNumber);
    }
}
