package calendar.model.calendarclass;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import calendar.model.event.Event;

/**
 * Represents a calendar model with a name and time zone.
 * This class provides methods to convert between LocalDateTime and ZonedDateTime
 * in the context of the calendar's time zone.
 */
public class NewCalendarModel extends CalendarModel {
  private String name;
  private ZoneId timeZone;

  /**
   * Constructor for a NewCalendarModel.
   *
   * @param name     the name of the calendar
   * @param timeZone the time zone of the calendar
   */
  public NewCalendarModel(String name, ZoneId timeZone) {
    this.name = name;
    this.timeZone = timeZone;
  }

  /**
   * Get the name of the calendar.
   *
   * @return the name of the calendar
   */
  public String getName() {
    return this.name;
  }

  /**
   * Set the name of the calendar.
   *
   * @param name the new name of the calendar
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the time zone of the calendar.
   *
   * @return the time zone of the calendar
   */
  public ZoneId getTimeZone() {
    return this.timeZone;
  }

  /**
   * Set the time zone of the calendar.
   *
   * @param timeZone the new time zone of the calendar
   */
  public void setTimeZone(ZoneId timeZone) {
    ZoneId lastTimeZone = this.timeZone;
    this.timeZone = timeZone;
    List<Event> events = filterSeriesIds(new ArrayList<>(this.events.values()));
    for (Event e : events) {
      // original localDateTimes
      LocalDateTime originalStart = e.getStartDateTime();
      LocalDateTime originalEnd = e.getEndDateTime();

      // changing time zones with ZonedDateTime conversion
      LocalDateTime newStartLDT = originalStart.atZone(lastTimeZone)
              .withZoneSameInstant(timeZone)
              .toLocalDateTime();
      LocalDateTime newEndLDT = originalEnd.atZone(lastTimeZone)
              .withZoneSameInstant(timeZone)
              .toLocalDateTime();
      // editing event to have new start and end
      handleDateChanging(e, originalStart, originalEnd, newStartLDT, newEndLDT);
    }
  }

  /**
   * Converts a LocalDateTime to a ZonedDateTime in the context of this calendar's time zone. And
   * modify the event's start and end times accordingly by using the editSingleEvent method.
   * @param e the event to edit
   * @param oldStart the old start date and time of the event
   * @param oldEnd the old end date and time of the event
   * @param newStart the new start date and time of the event
   * @param newEnd the new end date and time of the event
   */
  private void handleDateChanging(Event e, LocalDateTime oldStart, LocalDateTime oldEnd,
                                  LocalDateTime newStart, LocalDateTime newEnd) {
    if (oldStart.isBefore(newStart)) {
      editMultipleEvents("end", e.getSubject(), oldStart.format(dateTimeFormatter),
              newEnd.format(dateTimeFormatter), true);
      editMultipleEvents("start", e.getSubject(), oldStart.format(dateTimeFormatter),
              newStart.format(dateTimeFormatter), true);
    } else if (newStart.isBefore(oldStart)) {
      editMultipleEvents("start", e.getSubject(), oldStart.format(dateTimeFormatter),
              newStart.format(dateTimeFormatter), true);
      editMultipleEvents("end", e.getSubject(), newStart.format(dateTimeFormatter),
              newEnd.format(dateTimeFormatter), true);
    }
  }

  private List<Event> filterSeriesIds(List<Event> events) {
    ArrayList<String> ids = new ArrayList<>();
    for (Event e : new ArrayList<>(events)) {
      if (e.getSeriesId() != null) {
        if (!ids.contains(e.getSeriesId())) {
          ids.add(e.getSeriesId());
        }
        else {
          events.remove(e);
        }
      }
    }
    return events;
  }
}
