package calendar.controller.guicontroller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import calendar.model.CalendarObserver;
import calendar.model.calendarclass.ICalendar;
import calendar.model.calendarmanagerclass.ICalendarManager;
import calendar.model.event.Event;
import calendar.view.GUIView;

public class GUIController implements IGUIController, CalendarObserver {
  private final ICalendarManager manager;
  private final GUIView view;
  private ICalendar currentCalendar;
  private List<Event> currentDisplayedEvents;

  public GUIController(ICalendarManager manager, GUIView view) {
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
    List<Event> events = currentCalendar.getEventsInRange(
            LocalDate.now().atStartOfDay(),
            LocalDate.now().plusDays(7).atTime(23, 59, 59));
    currentDisplayedEvents = events;
    view.printEvents(events);
  }

  @Override
  public void eventsUpdated(List<Event> events) {
    this.refreshView();
  }

  @Override
  public void handleAddEvent() {
    List<String> eventsData = view.showAddEventDialog();

    if (eventsData != null && eventsData.size() == 3) {
      String subject = eventsData.get(0);
      String start = eventsData.get(1);
      String end = eventsData.get(2);

      try {
        currentCalendar.createSingleEvent(subject, start, end);
        view.displayMessage("Added event: " + subject);
      } catch (Exception e) {
        view.displayException(e);
      }
    }
  }

  @Override
  public void handleEditEvent() {
    Event selectedEvent = view.getSelectedEvent();
    if (selectedEvent != null) {
      List<String> newData = view.showEditEventDialog(selectedEvent);
      if (newData != null && newData.size() == 3) {
        String newSubject = newData.get(0);
        String newStart = newData.get(1);
        String newEnd = newData.get(2);

        try {
          String originalSubject = selectedEvent.getSubject();
          String originalStart = selectedEvent.getStartDateTime()
                  .format(currentCalendar.getDateTimeFormatter());
          String originalEnd = selectedEvent.getEndDateTime()
                  .format(currentCalendar.getDateTimeFormatter());
          //update subject
          if (!originalSubject.equals(newSubject)) {
            currentCalendar.editSingleEvent("subject", originalSubject, originalStart,
                    originalEnd, newSubject);
          }
          //update start time
          if (!originalStart.equals(newStart)) {
            currentCalendar.editSingleEvent("start", originalSubject, originalStart,
                    originalEnd, newStart);
          }
          //update end time
          if (!originalEnd.equals(newEnd)) {
            currentCalendar.editSingleEvent("end", originalSubject, originalStart,
                    originalEnd, newEnd);
          }
          view.displayMessage("Updated event: " + originalSubject);
        } catch (Exception e) {
          view.displayException(e);
        }
      }
    } else {
      view.displayMessage("No event selected for editing.");
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
        manager.setCurrentCalendar(selected);
        currentCalendar = manager.getCurrentActiveCalendar();
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
      LocalDate startDate = LocalDate.parse(view.getStartDate());
      List<Event> event = currentCalendar.getEventsInRange(
              startDate.atStartOfDay(),
              startDate.plusDays(7).atTime(23, 59, 59));
      currentDisplayedEvents = event;
      view.printEvents(event);
    } catch (Exception e) {
      view.displayException(e);
    }
  }
}

