package calendar.model;

import calendar.model.event.Event;
import java.util.List;

public interface CalendarObserver {
  void eventsUpdated(List<Event> events);
}