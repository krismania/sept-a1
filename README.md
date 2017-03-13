# Software Engineering: Process &amp; Techniques Assignment

## Project Description
You are required to design, develop and test an application named "Appointment Booking
System". This is basically a booking system that can be used by any business, allowing a
customer to book a time slot for an appointment. The system can be for any kind of
business, e.g., a hairdresser, a gym, or dentist. The application will display a customer’s
booking after it is entered and allow them to provide address, contact and other details. If
the customer has made a booking previously and their information has been stored, the
application will allow them to retrieve that information. A customer should also be able to
track their booking status. Besides providing the required functionalities, your program
should incorporate appropriate error handling, e.g., booking outside allowed times, or
double-bookings.

In Part 1, you can focus on customer registration and implement the system for a single
business. You may allow the system to define parameters such as allowed booking times
(e.g., 9am till 5pm, Monday to Friday) in a config file or database (as below).
(Part 2 will allow a business to register itself and define those details when it registers.)

## Functional requirements
1. The system should display a main page: When your program starts, a main page should be displayed as login/register pages. The program must authenticate and authorise users and based on user type/role, i.e., business owner or customer.
2. User registration is only for customers; business owner information can be accessible through a file called business.txt (or a db), including business name, business owner name, address, phone, username and password. Customers can register themselves via data entry; this information should be saved in customerinfo.txt (or a db).
3. If the owner can login successfully then the owner is able to add a new employee, add working time/dates for the next month, look at the summaries of bookings, new booking, show all workers’ availability for the next 7 days.
4. Customer only view available days/time but not yet book a slot (Part 2);
5. A text file (customerinfo.txt as shown below) should be created; an alternative is to use a database which must be distributed with the application. Each line shows a customer’s name, username, password, address, contact number. You should create at least 3 entries manually for testing purposes. (In practice, customer information would likely be encrypted, but this can be ignored for this assignment.)

You are required to use a recent version of PhP or Java (JDK 1.8 or later).
Code quality and clarity will be marked so make sure you properly document and lay out
your code, avoid hardcoding, use a logging framework, etc. Analyse the requirements and
think carefully about the basic class design before commencing coding.

You do not have to provide a GUI interface in Part 1 but you can if you prefer. If you use a
console interface, the interface (and what values to be entered) should be very clear and should
be error-checked to avoid crashing!

More details and clarifications of Requirements will be posted to Blackboard
