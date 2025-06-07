# Calendar Program

This application can be run in two modes: interactive mode and headless mode.
First, if you are using IntelliJ IDEA, you can edit the run configuration to set the
main class to `calendar.App` with argument --mode interactive or you can run the following
command in the terminal for interactive mode:

javac calendar.App --mode interactive

For headless mode, you do the same but set the argument to --mode headless <Command.txt>  or
you can run the following command in the terminal:

javac calendar.App --mode headless Command.txt

where Command.txt is a command file that contains the commands you want to run in headless mode.
Make sure to include a quit command at the end of the file to exit the program.

Working Features:

* Event Creation:
    * Single Event: create single event <subject> <yyyy-MM-ddTHH:mm> <yyyy-MM-ddTHH:mm>
    * Recurring Events (by repeats): create recurring event <subject> <yyyy-MM-ddTHH:mm>
      <yyyy-MM-ddTHH:mm> <weekdays> <repeats>
    * Recurring Events (by until date): create recurring event <subject>
      <yyyy-MM-ddTHH:mm> <yyyy-MM-ddTHH:mm> <weekdays> until <yyyy-MM-dd>
    * All-Day Events (single day): create all-day event <subject> <yyyy-MM-dd>
    * All-Day Recurring Events (by repeats): create all-day recurring event <subject>
      <yyyy-MM-dd> <weekdays> <repeats>
    * All-Day Recurring Events (by until date): create all-day recurring event <subject> <
      yyyy-MM-dd>
      <weekdays> until <yyyy-MM-dd>
* Show Status:
    * show status on <yyyy-MM-ddTHH:mm>: Checks if the calendar is busy at a specific date and time.
* Print Events:
    * print events on <yyyy-MM-dd>: Lists all events occurring on a specific date.
    * print events from <yyyy-MM-ddTHH:mm> to <yyyy-MM-ddTHH:mm>: Lists all events within a
      date-time range.
* Edit Events:
    * Single Event: edit event <property> "<subject>" from <start_datetime> to <end_datetime>
      with "<newValue>"
        * Support properties: subject, start_datetime, end_datetime, description, location, and
          status
        * Note: start_datetime and end_datetime must be in the same format as the original event.
    * Multiple Events: edit events <property> "<subject>" from <start_datetime> to <end_datetime>
      with "<newValue>"
        * Support properties: subject, start_datetime, end_datetime, description, location, and
          status
        * Note: start_datetime and end_datetime must be in the same format as the original event.
    * Event Series: edit event series <property> "<subject>" from <start_datetime> to <end_datetime>
      with "<newValue>"
        * Support properties: subject, start_datetime, end_datetime, description, location, and
          status
        * Note: start_datetime and end_datetime must be in the same format as the original event.

For contribution:

* Core Model (CalendarModel.java, ICalendar.java):
    * Xiaobin implements the core methods and overall structure of the calendar model.
    * Christopher improved the design and structure of the calendar model, allowing it
      to handle delegation more effectively and concisely.
* Event Classes (Event.java, RecurringRuleEvent.java):
    * Xiaobin implements the Event class using the builder pattern, and the RecurringRuleEvent class
    * Christopher implements the toString method for the Event class to allow for better event
      representation.
* Enums (EventStatus.java, DayOfWeekAbbreviation.java, Location.java):
    * Xiaobin implements the enums classes to represent event status, day-of-the-week abbreviations,
      and locations.
* Command Design Pattern (CalendarCommand.java, CreateCommand.java, EditCommand.java,
  MenuCommand.java, PrintCommand.java, ShowCommand.java):
    * Christopher came up with the command design pattern to handle different commands in the
      calendar application. Design
      the overall structure of the command pattern, allowing for better separation of concerns and
      easier extensibility.
    * Xiaobin implements the command classes methods to handle specific commands in the calendar
      application.
* View Layer (ICalendarView.java, TextBasedView.java):
    * Christopher implements the view layer to handle user interactions and display information in a
      text-based format, as
      well as the full implementation of the methods.
* Main Application (App.java):
    * Xiaobin implements the main application class to handle the command-line interface and user
      input.
    * Christopher improved the main application class to successfully run the calendar application
      in
      both interactive and headless modes
      as well as the functionality to read commands from a file in headless mode.
* Testing:
    * Xiaobin tests all the Command classes and some parts of the Controller classes.
    * Christopher creates the MockModel class for testing, all the res folders with three text
      files,
      and tests the CalendarModel, Event classes, RecurringRuleEvent, and the View classes.

Additional Notes:

Comment from Xiaobin:

* This project is long and complex, such that it gave more insight into the design patterns and
  how to implement them in Java. The command design pattern is a great way to handle different
  commands
  in a calendar application, and it allows for better separation of concerns and easier
  extensibility.
* The MVC model is a great way to handle the separation of concerns in a calendar application,
  and it allows for better organization of the code. The view layer is responsible for handling user
  interactions
  and displaying information, while the model layer is responsible for handling the data and
  business logic.
  It improves a lot of my understanding of the overall Java documentations and how to use them
  effectively.
* Christopher and I have a great collaboration on this project, and we learned a lot from each
  other. We had some disagreements on the design of the calendar model, but we were able to come to
  a consensus and implement a solution that works well for both of us. He is a great partner to
  work with, and I appreciate for his helps and contributions to this project. I really
  enjoyed working with him on this project, and I look forward to working with him again in the
  future in part 2.

Comments from Christopher:

* It took me a while to get going with this project. It was hard to wrap my head around fully
  building an MVC
  design from scratch, as well as some aspects of the command design. The hardest part for me was
  designing the controller while
  considering how it should be tested. The lecture notes, as well as the provided lecture code,
  helped me a lot. I feel that as
  confused as I was, I now understand these ideas a lot better.
* I am happy that last week's lab had us set up a Git repo. Git has been going very smoothly and has
  proven to be a valuable tool
  for collaboration.
* As Xiaobin mentioned above, we had conflicting ideas on how to design the model/controller.
  However, we came to a very nice
  conclusion, and what we have now is just as good if not better than our original ideas. I'm
  thankful that Xiaobin was on top
  of his work, and he was very kind and easy to work with. We communicated with each other well and
  gave each other good feedback.
  Looking forward to further collaboration with him!
