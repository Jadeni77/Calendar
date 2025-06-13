# Calendar - Version 2

## Changes made:
* Moved logic from CalendarController to CalendarManagerController, and now using composition.
(A CalendarController that may be delegated from the CalendarManagerController).
* CalendarManagerController handles input, catches errors, and processes commands unique to it,
while the CalendarController no longer loops and only takes in a single input to process given to 
it by the ManagerController.
  * The CalendarManagerController possesses all the same functionality as the CalendarController
  does, with additional commands. 
  * Since this version of the program calls for the use of a manager for multiple calendars, 
  this use of composition can be practical and does not change the way the
  users interact with the program beyond the addition of the new commands.
  

* We realized the logic within the CalendarModel class for editing commands did not properly
change the starting/ending times of events in a series. We changed the logic of the
editMultipleEvents method and added a few helper methods to fix this.
  * The model's previous logic edited each event's property to the same date/time and did not base
  the edit off of the difference in time from the original start/end. 
  * It also did not error when series dates were edited to span multiple days. We determined 
  that it would be more practical for the times to change relative to the difference between 
  the old and new date/time of the selected event, and that it should not allow series events 
  to enter the illegal state of spanning multiple days.
* The App file no longer uses a CalendarController as its controller, but a CalendarManagerController. 
  * The App file had to change to incorporate the capabilities added in this assignment from the
  new classes and interfaces we implemented.
* The fields within the CalendarModel class (the list of events and series events and date/time
formatters) were changed to protected from private.
  * The NewCalendarModel class extends CalendarModel, and we needed to access the formatters
  and lists of events within certain methods in the NewCalendarModel class. This was an acceptable
    change because the 'protected' access modifier still protects the fields from being accessed
    publicly, and it allows subclasses to access them.


## How to use:
To use this application, navigate to the directory of the Assignment5.JAR file that comes with the
build of this program in your computer's terminal or command prompt.
This program can be run in either interactive or headless mode. Enter the following commands to
run the JAR file in either mode:
* Interactive:   java -jar Assignment5.jar --mode interactive
* Headless:      java -jar Assignment5.jar --mode headless <file.txt>
For headless mode, <file.txt.> represents the name of a file with the commands you wish to execute,
one per line. Any invalid command will be ignored and the program will continue running. Ensure
that the command "quit" or "q" is entered on the last line of the file to exit cleanly. If there is
no exit command, the program will error and then terminate.


## Working features:
* All functionality from Assignment 4 within a single calendar is properly working. This includes:
Creating and editing events, querying events, and querying the availability of a given point in
time on the calendar.


* This program supports the management of multiple different calendars that posses these
functionalities, and possess a name and a time zone. Calendars can be created and their properties
can be edited. (Note: The editing of a calendar's timezone will affect the starting and ending
date/times within.) 
  * Events can also be copied from one calendar to another. When copied events are
  edited, only that event will change. 
  * A calendar must be selected to be "focused on" before any functionality for events can be used.
  * Feedback from inputted commands, whether confirmation of a command's success or an error message,
  will be displayed to the user as the program runs (after each command for interactive mode, and in
  a log file for headless mode).


## List of commands:
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
* Code Refactoring:
  * Both of us modified the CalendarController to properly function in the context of its use in
  the new CalendarManagerController.
  * Christopher modified the logic within CalendarModel to properly edit the times of series
  events, and to restrict editing said events only to cases where the events would still be valid,
  as discussed in the "Changes made" section.
* Manager Controller/Command Classes
  * Christopher planned and designed how the CalendarManagerController would interact with the
  model and its command classes, ensuring they have correct signatures and do not require the use
  of casting.
  * Xiaobin implemented most of the method logic for the New Command features inside the command classes.
  * Christopher made the manager controller delegate to the calendar controller (composition) 
  in the case of invalid methods or those that apply to a specific calendar.
  * Both of us handled message displaying and error catching, as well as selection of an "active"
  calendar.
* Manager Model
  * Christopher worked on the design of the model and its usage elsewhere, and ensured that the 
  model would use composition instead of inheritance. He also made minor tweaks to some of the
  methods within.
  * Xiaobin started the general structure of the CalendarManagerModel and implemented the
  methods that involved the new features being added, such as creating a new calendar, editing a
  calendar, and copying events between calendars.
* New Calendar Model
  * Christopher implemented the logic for changing timezones
  * Xiaobin implemented the structure of the NewCalendarModel and the rest of the methods beside
  changing the timezone.
* Main Application (App.java)
  * We made the changes to the App file together.
* Testing
  * Christopher wrote tests for CalendarManagerModel, NewCalendarModel, CalendarManagerController,
  and abstracted the tests for the calendar models and calendar/manager controllers into abstract
  classes which were inherited by the affected test files.
  * Xiaobin wrote all the tests for the command classes, and fix some of the tests base on Christopher's
  changed.


## Additional comments:
* From Chris
  * This assignment was a very fun challenge! The hardest part of this assignment for me was
  designing the new controller. We had an original design which used inheritance, and required
  casting in a lot of parts of it. I was able to eliminate the need for casting in the
  implementation, but it took a long time to wrap my head around. Office hours helped me, as well
  as the lecture notes, but I was able to figure it out mostly on my own. The new design favors
  composition over inheritance, which is nice because I have a good grip on the concept now.
  * The tutorial for the JAR file didn't work for me on Windows, as I had to manually select
  "Build" from the options menu and build the artifacts folder from there. Maybe the tutorial
  should be updated for the next time this assignment is used.
  * Xiaobin was pleasant to work with again! No complaints, we got this assignment done in a timely
  manner this week.

* From Xiaobin
  * This assignment is easier than the last one, but it is still a challenge to implement the
  new features. The most difficult part for me is to implement the logic of the new command classes.
  Because at first, we use inheritance to design the CalendarManagerModel, but it is not a good
  design as there are a lot of type casting and instanceof checks. So we changed to use composition
  and that eliminated the need for type casting.
  * The tutorial for the JAR file is not clear enough, and I think the tutorial should be updated
  as the IntellJ version are being updated too.
  * Chris is a great partner to work with, and we have a good communication during the whole
  assignment. We both learned a lot from this assignment, and I think we did a great job.