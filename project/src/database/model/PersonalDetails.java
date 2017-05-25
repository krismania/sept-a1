package database.model;

/**
 * Defines getters and setters for several personal details, such as name and
 * phone number.
 *
 * @author krismania
 */
public interface PersonalDetails {
    public String getFirstName();

    public String getLastName();

    public String getEmail();

    public String getPhoneNumber();

    public void setFirstName(String name);

    public void setLastName(String name);

    public void setEmail(String email);

    public void setPhoneNumber(String phone);
}
