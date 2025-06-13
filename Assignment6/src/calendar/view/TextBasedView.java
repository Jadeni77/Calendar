package calendar.view;

import java.io.IOException;
import java.util.List;

import calendar.model.event.Event;

/**
 * This class represents a text-based view of a calendar application, which is able
 * to display messages and errors, as well as print lists of events and the availability
 * of the calendar at a certain time.
 */
public class TextBasedView implements ICalendarView {
  private final Appendable out;

  public TextBasedView(Appendable out) {
    this.out = out;
  }

  @Override
  public void displayMessage(String message) {
    try {
      this.out.append(message).append("\n");
    } catch (IOException e) {
      throw new IllegalStateException("Cannot display message", e);
    }
  }

  @Override
  public void displayException(Exception e) {
    try {
      this.out.append("An error was encountered:").append("\n");
      this.out.append(e.getMessage()).append("\n");
    } catch (IOException ex) {
      throw new IllegalStateException("Cannot display error", ex);
    }
  }

  @Override
  public void printEvents(List<Event> events) {
    try {
      for (Event event : events) {
        this.out.append("\u2022").append(event.toString()).append("\n");
      }
    } catch (IOException e) {
      throw new IllegalStateException("Cannot print events", e);
    }
  }

  @Override
  public void showStatusOnDayTime(boolean status) {
    try {
      if (status) {
        this.out.append("Busy\n");
      } else {
        this.out.append("Available\n");
      }
    } catch (IOException e) {
      throw new IllegalStateException("Cannot display status", e);
    }
  }
}
