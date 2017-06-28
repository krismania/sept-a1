# Software Engineering: Process &amp; Techniques
## Group Project (S1 2017): Appointment Booking System

We were tasked with creating a booking system in Java to be used by both business owners and customers. The project was mainly focussed on working with an agile process with week-long sprints and constantly changing requirements.

## Team Information

### Team Name
extendedClass

### Members
James McLennan
Kristian Giglia
Richard Kuoch
Tim Novice

### Tutorial
**Tutor:** Homy Ash

### Important Links
**GitHub:** https://github.com/krismania/se-pt-a1

**Trello:** https://trello.com/b/FCcJDb3Z/extendedclass

**Slack:** https://extendedclass.slack.com

**Lean Testing:** https://app.leantesting.com/en/projects/extendedclass/23342

## Our Solution

Our application revolves around a singleton controller object (`main.Controller`), which takes care of communication between the GUI and the database. Business logic is largely handled within the `database` package, which contains classes for reading from & writing to SQLite databases, as well as classes that model business objects such as employees and bookings. The `gui` package contains JavaFX FXML documents and the classes tied to their function, with a focus on presentation.
