package calendar.controller.guicontroller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.swing.*;

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
  private List<Event> currentDisplayedEvents;

  public GUIController(ICalendarManager manager, IGUIView view) {
    this.manager = manager;
    this.view = view;
  }

  @Override
  public void start() {
    String defaultCalendarName = "Default";
    String systemTimeZone = ZoneId.systemDefault().toString();
    manager.createCalendar(defaultCalendarName, systemTimeZone);
    manager.setCurrentCalendar(defaultCalendarName);

    currentCalendar = manager.getCurrentActiveCalendar();
    // Add the current calendar as an observer to listen for events updates
    currentCalendar.addObserver(this);
    //add the default calendar to the view JComboBox or dropdown
    view.addCalendar(defaultCalendarName);

    this.refreshView();
    view.displayMessage("Calendar application started.");

  }

  private void refreshView() {
//    List<Event> events = currentCalendar.getEventsInRange(
//            LocalDate.now().atStartOfDay(),
//            LocalDate.now().plusDays(7).atTime(23, 59, 59));
//    currentDisplayedEvents = events;
//    view.refreshEvents(currentDisplayedEvents);
    view.setStartDate(LocalDate.now());
    handleRefreshSchedule();
  }

  @Override
  public void eventsUpdated(List<Event> events) {
    this.refreshView();
  }

  @Override
  public void handleAddEvent() {
    List<String> eventsData = view.showAddEventDialog();

    if (eventsData != null && eventsData.size() == 6) {
      String subject = eventsData.get(0);
      String start = eventsData.get(1);
      String end = eventsData.get(2);
      String description = eventsData.get(3);
      String location = eventsData.get(4);
      String status = eventsData.get(5);

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
  }

  @Override
  public void handleEditEvent() {
    try {
      Event selectedEvent = currentCalendar.getEvent(
              view.getSelectedEventSubject(),
              LocalDateTime.parse(view.getSelectedEventStart(),
                      currentCalendar.getDateTimeFormatter()),
              LocalDateTime.parse(view.getSelectedEventEnd(),
                      currentCalendar.getDateTimeFormatter())
      );
      if (selectedEvent != null) {
        List<String> newData = view.showEditEventDialog();
        if (newData != null && newData.size() == 2) {
          String editedProperty = newData.get(0);
          String newValue = newData.get(1);

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
        }
      } else {
        view.displayMessage("No event selected for editing.");
      }
    } catch (IllegalArgumentException e) {
      view.displayException(e);
    }
  }

  @Override
  public void handleCreateCalendar() {
    List<String> calendarData = view.showCreateCalendarDialog();
    if (calendarData != null && calendarData.size() == 2) {
      String calendarName = calendarData.get(0);
      String timeZone = calendarData.get(1);

      try {
        manager.createCalendar(calendarName, timeZone);
        view.addCalendar(calendarName);
        view.displayMessage("Created calendar: " + calendarName);
      } catch (Exception e) {
        view.displayException(e);
      }
    }
  }

  @Override
  public void handleSwitchCalendar() {
    String selected = view.getSelectedCalendar();
    if (selected != null && !selected.equals("Default")) {
      try {
        // Remove the current calendar observer before switching
        currentCalendar.removeObserver(this);

        manager.setCurrentCalendar(selected);
        currentCalendar = manager.getCurrentActiveCalendar();
        //add observer to the new current calendar
        currentCalendar.addObserver(this);
        view.displayMessage("Switched to calendar: " + selected);
        refreshView();
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

