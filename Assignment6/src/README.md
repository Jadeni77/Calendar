# Calendar - Version 3

## Changes made:
* The CalendarModel class now contains and makes use of Observers to notify any listeners that a
  change to the model has been made.
  * There is a need for the model to refresh to properly display changes such as adding an event,
    editing an event, or switching to a different calendar. So, in order to do this, we deemed it was
    acceptable to modify our model code to include observers in order to efficiently inform the view
    whenever these changes are made.
* The constructor for Events that takes in 3 arguments now properly uses the 'endDateTime' argument.
  * In tests for the GUIController class, the mock model within the mock manager needed to utilize
  this constructor for the Event Class to properly initialize an event within its model, but the
  endDateTime argument was being ignored and passed through as null, so it was changed to properly
  pass it through.
* The logic for copying events in the ManagerModel was changed slightly to properly shift the time
to the desired starting time.
  * While looking over tests for the ManagerModel, we discovered that a test for copying events to
  new calendars with a different time would pass no matter the target start time. So, additional
  logic had to be added to account for the expected shift to ensure the tests would only pass in the
  expected cases.


## How to use:
To use this application, navigate to the directory of the Assignment5.JAR file that comes with the
build of this program in your computer's terminal or command prompt.
This program can be run in interactive or headless mode as a text-based calendar, or can be run
with a GUI. Enter the following commands to run the JAR file in either mode:
* Interactive:   java -jar Assignment5.jar --mode interactive
* Headless:      java -jar Assignment5.jar --mode headless <file.txt>
* GUI:           java -jar Assignment5.jar
For headless mode, <file.txt.> represents the name of a file with the commands you wish to execute,
one per line. Any invalid command will be ignored and the program will continue running. Ensure
that the command "quit" or "q" is entered on the last line of the file to exit cleanly. If there is
no exit command, the program will error and then terminate.


## Working features:
* All functionality from Assignment 4 and 5 is properly working. This includes:
  * Creating and editing events
  * Querying events
  * Querying the availability of a given point in time on a calendar
  * Creating and editing calendars
    * Calendars have a name and a time zone.
    * Changing the time zone of a calendar changes the times of any events within.
  * Selecting calendars for use
  * Copying events between calendars


## List of command-line commands:
* Menu Command: menu
  * Shows a list of all possible commands and their syntax.
* Calendar Creation: create calendar --name <calendarName> --timezone area/location
* Calendar Editing: edit calendar --name <name-of-calendar> --property <property-name> 
<new-property-value>
  * Properties to edit: 'name', 'timezone'
* Selecting (Use) Calendars: use calendar --name <calendarName>
* Event Creation:
  * Single Event: create single event yyyy-MM-ddTHH:mm yyyy-MM-ddTHH:mm
  * Recurring Events (by repeats): create recurring event yyyy-MM-ddTHH:mm yyyy-MM-ddTHH:mm
  * Recurring Events (by until date): create recurring event yyyy-MM-ddTHH:mm yyyy-MM-ddTHH:mm until
  * All-Day Events (single day): create all-day event
  * All-Day Recurring Events (by repeats): create all-day recurring event
  * All-Day Recurring Events (by until date): create all-day recurring event < yyyy-MM-dd> until
* Show Status:
  * show status on yyyy-MM-ddTHH:mm: Checks if the calendar is busy at a specific date and time.
* Print Events:
  * print events on : Lists all events occurring on a specific date.
  * print events from yyyy-MM-ddTHH:mm to yyyy-MM-ddTHH:mm: Lists all events within a date-time range.
* Edit Events:
  * Single Event: edit event "" from <start_datetime> to <end_datetime> with ""
    * Support properties: subject, start_datetime, end_datetime, description, location, and status
    * Note: start_datetime and end_datetime must be in the same format as the original event.
  * Multiple Events: edit events "" from <start_datetime> to <end_datetime> with ""
    * Support properties: subject, start_datetime, end_datetime, description, location, and status
    * Note: start_datetime and end_datetime must be in the same format as the original event.
  * Event Series: edit event series "" from <start_datetime> to <end_datetime> with ""
    * Support properties: subject, start_datetime, end_datetime, description, location, and status
    * Note: start_datetime and end_datetime must be in the same format as the original event.
* Copying Events:
  * Copy Single Event: copy event <eventName> on <dateStringTtimeString> --target <calendarName> 
  to <dateStringTtimeString>
    * Copies the event with the given name and start date/time to the target calendar, starting
    on the target date/time.
  * Copy Events on Day: copy events on <dateString> --target <calendarName> to <dateString>
    * Same behavior as the above command, except it copies all events on the given day.
  * Copy Events between Dates: copy events between <dateString> and <dateString> --target 
  <calendarName> to <dateString>
    * Same behavior as the other copy commands, except it copies all events within the specified
    interval.


## Contribution:
We coded in tandem for basically the first half of the assignment. This section may
not be entirely accurate because of this, but it is close enough.
* Code Refactoring:
  * Xiaobin incorporated the Observer pattern into the CalendarModel class.
  * Christopher made the small changes to the Event and ManagerModel classes.
* GUI View
  * Xiaobin created the structure of the GUI, placing the buttons and text fields. He also created
  and maintained the 'Month View' section.
  * Christopher handled the use of listeners for the view and implemented the logic for prompting
  the users for information and relaying that information to the view on button presses,
  mostly for adding and editing events.
  * Christopher made amendments to the appearance of the GUI and ensured data could be inputted in
  fail-safe ways (e.g. spinner, drop-down) when possible.
* GUI Controller
  * We designed the overall structure of the Controller and EventListenerAdapter together and how
  it connected to the view.
  * Xiaobin implemented most of the controller methods initially, Christopher implementing the
  rest.
  * Christopher corrected controller logic where necessary, and maintained it alongside Xiaobin.
* Main Application (App.java)
  * We made roughly equal changes to the App file.
* Testing
  * Christopher handled tests for the EventListenerAdapter and GUIController classes, as well as
  creating new mock classes needed for them.


## Additional comments:
* From Chris
  * We were at a complete loss at the start of this assignment. We still had a very hard time
  conceptualizing how to design our new controller and view. After many hours of talking it over
  together, we figured out something that would work (which ended up being a combination of our
  ideas). The lecture notes and especially the provided examples from class helped us while we
  talked through it.
  * The GUI was very confusing at times, because of how long the methods got. However, we started
  early, and had pretty much no problems with working together again, so we got this assignment
  done in a very timely fashion.
  * Feeling refreshed to be finished! :)

* From Xiaobin
  * This assignment was a bit challenging as we had not learned too much about GUI during lectures.
  We had a called for around like 3 hours at night just to discuss how to start the assignment and 
  the best way we can think of to approach the instructions while maintaining the structure of our
  previous assignments. Good thing we figured everything out and we were able to finish.
  * We spend a lot of time on the GUI, and we had a lot of fun working on it. Thankful to Chris for
  the time and effort.