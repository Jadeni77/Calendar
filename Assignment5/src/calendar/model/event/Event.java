package calendar.model.event;

import calendar.model.enumclass.EventStatus;
import calendar.model.enumclass.Location;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * This class represents an event in a calendar.
 * Contains details such as subject, start and end times, description, location, and public status.
 */
public class Event {
  private final String subject;
  private final LocalDateTime startDateTime;
  private final LocalDateTime endDateTime;
  private final String description;
  private final Location location;
  private final EventStatus status;
  private final String seriesId; // For recurring events: the ID of the series they belong to
  private final boolean isAllDayEvent;
  private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH::mm");

  /**
   * Constructor for creating a new event with all details.
   *
   * @param subject       The subject of the event.
   * @param startDateTime The start date and time of the event.
   * @param endDateTime   The end date and time of the event.
   * @param description   A description of the event.
   * @param location      The location of the event.
   * @param status        The status of the event (public or private).
   * @param seriesId      The ID of the series this event belongs to, if applicable.
   */
  private Event(String subject, LocalDateTime startDateTime, LocalDateTime endDateTime,
                String description, Location location, EventStatus status, String seriesId,
                boolean isAllDayEvent) {
    this.checkValidation(subject, startDateTime, endDateTime);
    this.subject = subject;
    this.startDateTime = startDateTime;
    this.endDateTime = endDateTime;
    this.description = description;
    this.location = location;
    this.status = Objects.requireNonNullElse(status, EventStatus.PUBLIC);
    this.seriesId = seriesId;
    this.isAllDayEvent = isAllDayEvent;
  }

  /**
   * Constructor for creating a new event with initialize information.
   *
   * @param subject       The subject of the event.
   * @param startDateTime The start date and time of the event.
   * @param endDateTime   The end date and time of the event.
   */
  public Event(String subject, LocalDateTime startDateTime, LocalDateTime endDateTime) {
    this(subject, startDateTime, null, null, null, EventStatus.PUBLIC, null, false);
  }

  /**
   * Validates the event details. Ensures that the subject is not null or blank,
   * and that the end date and time is not before the start date and time.
   *
   * @param subject       The subject of the event.
   * @param startDateTime The start date and time of the event.
   * @param endDateTime   The end date and time of the event.
   * @throws IllegalArgumentException if any validation fails.
   */
  private void checkValidation(String subject, LocalDateTime startDateTime,
                               LocalDateTime endDateTime) {
    Objects.requireNonNull(subject, "Subject cannot be null");
    Objects.requireNonNull(startDateTime, "Start date and time cannot be null");
    Objects.requireNonNull(endDateTime, "End date and time cannot be null");

    if (subject.isBlank()) {
      throw new IllegalArgumentException("Subject cannot be blank");
    }
    if (endDateTime.isBefore(startDateTime)) {
      throw new IllegalArgumentException("End date and time cannot be before start date and time");
    }
  }

  /**
   * Builder class for creating an Event instance.
   */
  public static class EventBuilder {
    private String subject;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String description;
    private Location location;
    private EventStatus status;
    private String seriesId;
    private boolean isAllDayEvent;


    /**
     * Default constructor for the EventBuilder.
     * Initializes the isAllDayEvent to false.
     */
    public EventBuilder() {
      this.isAllDayEvent = false;
    }

    public EventBuilder subject(String subject) {
      this.subject = subject;
      return this;
    }

    public EventBuilder startDateTime(LocalDateTime startDateTime) {
      this.startDateTime = startDateTime;
      return this;
    }

    /**
     * Sets the end date and time of the event.
     * If this is set, the event is not considered an all-day event.
     *
     * @param endDateTime The end date and time of the event.
     * @return this builder instance for method chaining.
     */
    public EventBuilder endDateTime(LocalDateTime endDateTime) {
      this.endDateTime = endDateTime;
      this.isAllDayEvent = false;
      return this;
    }

    /**
     * Sets the description of the event.
     *
     * @param description The description of the event.
     * @return this builder instance for method chaining.
     */
    public EventBuilder description(String description) {
      this.description = description;
      return this;
    }

    /**
     * Sets the location of the event.
     *
     * @param location the location of the event.
     * @return this builder instance for method chaining.
     */
    public EventBuilder location(Location location) {
      this.location = location;
      return this;
    }

    /**
     * Sets the status of the event.
     *
     * @param status The status of the event (public or private).
     * @return this builder instance for method chaining.
     */
    public EventBuilder status(EventStatus status) {
      this.status = status;
      return this;
    }

    /**
     * Sets the series ID of the event.
     * This is used for recurring events to link them to a series.
     *
     * @param seriesId The ID of the series this event belongs to.
     * @return this builder instance for method chaining.
     */
    public EventBuilder seriesId(String seriesId) {
      this.seriesId = seriesId;
      return this;
    }

    /**
     * Sets whether the event is an all-day event.
     *
     * @param isAllDayEvent true if the event is an all-day event, false otherwise.
     * @return this builder instance for method chaining.
     */
    public EventBuilder isAllDayEvent(boolean isAllDayEvent) {
      this.isAllDayEvent = isAllDayEvent;
      if (isAllDayEvent && this.endDateTime == null) {
        //set endDateTime to the same day at 5 PM if not provided
        this.startDateTime = this.startDateTime.toLocalDate().atTime(8, 0);
        this.endDateTime = this.startDateTime.toLocalDate().atTime(17, 0);
      }
      return this;
    }

    /**
     * Builds the Event instance with the provided details.
     *
     * @return a new Event instance.
     * @throws IllegalArgumentException if required fields are not set.
     */
    public Event build() {
      Objects.requireNonNull(this.subject, "Subject must be set in builder.");
      Objects.requireNonNull(this.startDateTime,
              "Start date/time must be set in builder.");

      if (!this.isAllDayEvent && this.endDateTime == null) {
        throw new IllegalArgumentException("End date/time " +
                "must be provided for non-all-day events.");
      }
      return new Event(this.subject, this.startDateTime, this.endDateTime, this.description,
              this.location, this.status, this.seriesId, this.isAllDayEvent);
    }
  }

  /**
   * Gets the subject of the event.
   *
   * @return the subject of the event.
   */
  public String getSubject() {
    return this.subject;
  }

  /**
   * Gets the start date and time of the event.
   *
   * @return the start date and time of the event.
   */
  public LocalDateTime getStartDateTime() {
    return this.startDateTime;
  }

  /**
   * Gets the end date and time of the event.
   *
   * @return the end date and time of the event.
   */
  public LocalDateTime getEndDateTime() {
    return this.endDateTime;
  }

  /**
   * Gets the description of the event.
   *
   * @return the description of the event.
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Gets the location of the event.
   *
   * @return the location of the event.
   */
  public Location getLocation() {
    return this.location;
  }

  /**
   * Gets the status of the event.
   *
   * @return the status of the event.
   */
  public EventStatus getStatus() {
    return this.status;
  }

  /**
   * Gets the ID of the series this event belongs to, if applicable.
   *
   * @return the series ID of the event, or null if it is not part of a series.
   */
  public String getSeriesId() {
    return this.seriesId;
  }

  /**
   * Gets whether the event is an all-day event.
   *
   * @return true if the event is an all-day event, false otherwise.
   */
  public boolean getIsAllDayEvent() {
    return this.isAllDayEvent;
  }

  /**
   * Outputs the relevant information about this Event as a String.
   * @return a String representing the contents of this Event
   */
  @Override
  public String toString() {
    String res = this.subject + " - Starts: " + this.startDateTime.format(dtf)
            + ", Ends: " + this.endDateTime.format(dtf);
    if (this.location != null) {
      res += ", Location: " + this.location;
    }
    return res;
  }
}
