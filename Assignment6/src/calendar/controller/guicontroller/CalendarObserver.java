package calendar.controller.guicontroller;

import calendar.model.event.Event;
import java.util.List;

/**
 * This interface represents an Observer used to notify any listeners of changes made to
 * an event or other component of a calendar model.
 */
public interface CalendarObserver {

  void eventsUpdated(List<Event> events);
}