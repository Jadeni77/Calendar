package calendar.controller.guiadapter;

public interface ViewEventListenerAdapter {

  /**
   * Handles the addition of an event.
   *
   * @param subject the subject of the event
   * @param start   the start time of the event
   * @param end     the end time of the event
   */
  void onAddEvent(String subject, String start, String end);

  /**
   * Handles the editing of an event.
   *
   * @param originalSubject the original subject of the event
   * @param originalStart   the original start time of the event
   * @param originalEnd     the original end time of the event
   * @param newSubject      the new subject of the event
   * @param newStart        the new start time of the event
   * @param newEnd          the new end time of the event
   */
  void onEditEvent(String originalSubject, String originalStart, String originalEnd,
                       String newSubject, String newStart, String newEnd);

  void onCreateCalendar(String name, String timeZone);

  void onSwitchCalendar(String name);

  void onRefreshSchedule(String startDate);
}
