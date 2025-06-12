package calendar.model.calendarclass;

import java.time.ZoneId;

/**
 * This class represents a mock NewCalendarModel, which logs the data inputted to each function
 * to a StringBuilder in order to verify that data has been received correctly.
 */
public class NewCalendarMockModel extends MockModel {
  private String name;
  private ZoneId timeZone;

  public NewCalendarMockModel(String name, ZoneId timeZone, StringBuilder modelLog) {
    super(modelLog);
    this.name = name;
    this.timeZone = timeZone;
  }

  /**
   * Logs the input of the given event to this Model's log StringBuilder.
   *
   * @return a dummy String value
   */
  public String getName() {
    this.log.append("Calendar Name Queried\n");
    return "";
  }

  /**
   * Logs the input of the given event to this Model's log StringBuilder.
   *
   * @param name the name intended to be changed to
   */
  public void setName(String name) {
    this.log.append("Calendar Name Changed to ").append(name).append("\n");
  }

  /**
   * Logs the input of the given event to this Model's log StringBuilder.
   *
   * @return a dummy value
   */
  public ZoneId getTimeZone() {
    this.log.append("Timezone Queried\n");
    return null;
  }

  /**
   * Logs the input of the given event to this Model's log StringBuilder.
   *
   * @param timeZone the time zone intended to be set to
   */
  public void setTimeZone(ZoneId timeZone) {
    this.log.append("Timezone Changed to ").append(timeZone).append("\n");
  }
}
