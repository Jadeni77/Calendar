package calendar.model.calendarmanagerclass;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import calendar.model.calendarclass.NewCalendarMockModel;


/**
 * This class represents a mock CalendarManagerModel, which logs the data inputted from each
 * function to a StringBuilder in order to verify that data has been received correctly.
 */
public class CalendarManagerMockModel implements ICalendarManager {
  public StringBuilder log;
  private final Map<String, NewCalendarMockModel> calendars;
  private final String currentCalendarName;

  /**
   * Constructs a CalendarManagerMockModel with the given StringBuilder as its log.
   */
  public CalendarManagerMockModel(StringBuilder log) {
    this.log = log;
    this.calendars = new HashMap<>();
    calendars.put("test1", new NewCalendarMockModel(
            "test1", ZoneId.of("America/New_York"), new StringBuilder()));
    this.currentCalendarName = "test1";
  }

  /**
   * Constructs a CalendarManagerMockModel with a test calendar in the map.
   */
  public CalendarManagerMockModel(NewCalendarMockModel modelMock) {
    this.log = new StringBuilder();
    this.calendars = new HashMap<>();
    calendars.put("test1", modelMock);
    this.currentCalendarName = "test1";
  }

  @Override
  public void createCalendar(String name, String zoneId) {
    log.append("Calendar created: ").append(name).append(", ").append(zoneId).append("\n");
  }

  @Override
  public void editCalendar(String nameOfCalendarToEdit, String property, String newValue) {
    log.append("Calendar edited: ").append(nameOfCalendarToEdit).append(", edit ")
            .append(property).append(" to ").append("\n");
  }

  @Override
  public void setCurrentCalendar(String name) {
    log.append("Current calendar changed: ").append(name).append("\n");
  }

  /**
   * To retrieve the calendar that is currently  "active" or being "focused on"
   * by the user. It represents the user's immediate working context.
   * If there is no current active calendar, or it is not in the manager's list of calendars,
   * this method will throw an exception prompting the user to select a calendar before proceeding.
   *
   * @return the current calendar being in use
   */
  @Override
  public NewCalendarMockModel getCurrentActiveCalendar() {
    log.append("Current calendar queried");
    return calendars.get(currentCalendarName);
  }

  @Override
  public NewCalendarMockModel getTargetCalendar(String name) {
    log.append("Target calendar queried: ").append(name).append("\n");
    return calendars.get(currentCalendarName);
  }

  @Override
  public void copyEvent(String eventName, String startDateTime, String calendarName,
                        String newDateTime) {
    log.append("Copy event: ").append(eventName).append(", starts ").append(startDateTime)
            .append(", to calendar ").append(calendarName).append(", on ").append(newDateTime)
            .append("\n");
  }

  @Override
  public void copyEventsOnDate(String date, String calendarName, String newDate) {
    log.append("Copy events on: ").append(date).append(", to calendar ")
            .append(calendarName).append(", on ").append(newDate).append("\n");
  }

  @Override
  public void copyEventsBetweenDates(String startDate, String endDate, String calendarName,
                                     String newDate) {
    log.append("Copy event from: ").append(startDate).append(" to ").append(endDate)
            .append(", to calendar ").append(calendarName).append(", on ").append(newDate)
            .append("\n");
  }
}
