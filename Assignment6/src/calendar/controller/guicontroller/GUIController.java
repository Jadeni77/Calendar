package calendar.controller.guicontroller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import calendar.model.calendarclass.ICalendar;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.model.enumclass.EventStatus;
import calendar.model.enumclass.Location;
import calendar.model.event.Event;
import calendar.view.IGUIView;

public class GUIController implements IGUIController, CalendarObserver {
  private final ICalendarManager manager;
  private final IGUIView view;
  private ICalendar currentCalendar;
  private String defaultCalendarName;

  public GUIController(ICalendarManager manager, IGUIView view) {
    this.manager = manager;
    this.view = view;

    defaultCalendarName = "Default";
    String systemTimeZone = ZoneId.systemDefault().toString();
    manager.createCalendar(defaultCalendarName, systemTimeZone);
    manager.setCurrentCalendar(defaultCalendarName);

    currentCalendar = manager.getCurrentActiveCalendar();
  }

  @Override
  public void start() {
    // Add the current calendar as an observer to listen for events updates
    currentCalendar.addObserver(this);
    //add the default calendar to the view JComboBox or dropdown
    view.addCalendar(defaultCalendarName);

    this.refreshView();
    view.displayMessage("Calendar application started.");
  }

  private void refreshView() {
    view.setStartDate(LocalDate.now());
    handleRefreshSchedule();
  }

  @Override
  public void eventsUpdated(List<Event> events) {
    this.refreshView();
  }

  @Override
  public void handleAddEvent(String subject, String start, String end, String description,
                             String location, String status) {
    Event.EventBuilder builder = new Event.EventBuilder()
            .subject(subject)
            .startDateTime(LocalDateTime.parse(start, currentCalendar.getDateTimeFormatter()))
            .endDateTime(LocalDateTime.parse(end, currentCalendar.getDateTimeFormatter()))
            .description(description)
            .location(Location.valueOf(location))
            .status(EventStatus.valueOf(status));

    Event newEvent;

    try {
      newEvent = builder.build();
      currentCalendar.addEvent(newEvent);
      view.displayMessage("Added event: " + subject + " !");
    } catch (Exception e) {
      view.displayException(e);
    }
  }

  @Override
  public void handleEditEvent(String editedProperty, String newValue) {
    try {
      Event selectedEvent = currentCalendar.getEvent(
              view.getSelectedEventSubject(),
              LocalDateTime.parse(view.getSelectedEventStart(),
                      currentCalendar.getDateTimeFormatter()),
              LocalDateTime.parse(view.getSelectedEventEnd(),
                      currentCalendar.getDateTimeFormatter())
      );
      if (selectedEvent != null) {
        try {
          String originalSubject = selectedEvent.getSubject();
          String originalStart = selectedEvent.getStartDateTime()
                  .format(currentCalendar.getDateTimeFormatter());
          String originalEnd = selectedEvent.getEndDateTime()
                  .format(currentCalendar.getDateTimeFormatter());
          currentCalendar.editSingleEvent(editedProperty, originalSubject, originalStart,
                  originalEnd, newValue);
          view.displayMessage("Updated event: " + originalSubject);
        } catch (Exception e) {
          view.displayException(e);
        }
      } else {
        view.displayMessage("No event selected for editing.");
      }
    } catch (IllegalArgumentException e) {
      view.displayException(e);
    }
  }

  @Override
  public void handleCreateCalendar(String calendarName, String timeZone) {
    try {
      manager.createCalendar(calendarName, timeZone);
      view.addCalendar(calendarName);
      view.displayMessage("Created calendar: " + calendarName);
    } catch (Exception e) {
      view.displayException(e);
    }
  }

  @Override
  public void handleSwitchCalendar() {
    String selected = view.getSelectedCalendar();
    if (selected != null) {
      try {
        // Remove the current calendar observer before switching
        currentCalendar.removeObserver(this);

        manager.setCurrentCalendar(selected);
        currentCalendar = manager.getCurrentActiveCalendar();
        //add observer to the new current calendar
        currentCalendar.addObserver(this);
        refreshView();
        view.displayMessage("Switched to calendar: " + selected);
      } catch (Exception e) {
        view.displayException(e);
      }
    }
  }

  @Override
  public void handleRefreshSchedule() {
    try {
      String startDate = view.getStartDate();
      LocalDate date = LocalDate.parse(startDate);

      LocalDateTime startOfDay = date.atStartOfDay();
      LocalDateTime endOfDay = date.atTime(23, 59, 59);

      List<Event> events = currentCalendar.getEventsInRange(startOfDay, endOfDay);

      if (events.size() > 10) {
        events = events.subList(0, 10); // Limit to 10 events for display
        view.displayMessage("Showing only the first 10 events.");
      }

      view.printEvents(events);

    } catch (Exception e) {
      view.displayException(e);
    }
  }

  @Override
  public void handlePrevMonth() {
    view.changeMonth(-1);

  }

  @Override
  public void handleNextMonth() {
    view.changeMonth(1);

  }
}

