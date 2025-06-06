package calendar.model.event;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import calendar.model.enumclass.EventStatus;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * This JUnit test class tests the functionality of the getter methods within the Event class,
 * its Builder subclass, and the toString() method.
 */
public class EventTest {
  private Event event;

  /**
   * Initializes an Event object with its builder functionality, which is then used to test
   * other methods within the Event class.
   */
  @Before
  public void setUp() {
    this.event = new Event.EventBuilder()
            .subject("Hi").startDateTime(LocalDateTime.of(2023, 10, 1, 10, 0))
        .endDateTime(LocalDateTime.of(2023, 10, 1, 12, 0))
        .description("This is a test event.")
        .status(EventStatus.PRIVATE)
        .build();
  }

  /**
   * Tests that the getSubject() method retrieves the correct data.
   */
  @Test
  public void testGetSubject() {
    assertEquals("Hi", event.getSubject());
  }

  /**
   * Tests that the getStartDateTime() method retrieves the correct data.
   */
  @Test
  public void testGetStartDateTime() {
    assertEquals(LocalDateTime.of(2023, 10, 1, 10, 0), event.getStartDateTime());
  }

  /**
   * Tests that the getEndDateTime() method retrieves the correct data.
   */
  @Test
  public void testGetEndDateTime() {
    assertEquals(LocalDateTime.of(2023, 10, 1, 12, 0), event.getEndDateTime());
  }

  /**
   * Tests that the getDescription() method retrieves the correct data.
   */
  @Test
  public void testGetDescription() {
    assertEquals("This is a test event.", event.getDescription());
  }

  /**
   * Tests that the getLocation() method retrieves the correct data.
   */
  @Test
  public void testGetLocation() {
    assertNull(event.getLocation());
  }

  /**
   * Tests that the getStatus() method retrieves the correct data.
   */
  @Test
  public void testGetStatus() {
    assertEquals(EventStatus.PRIVATE, event.getStatus());
  }

  /**
   * Tests that the toString() method produces the expected output in the form of a string,
   * containing the relevant information about the Event it is called on.
   */
  @Test
  public void testToString() {
    assertEquals("Hi - Starts: 2023-10-01T10::00, Ends: 2023-10-01T12::00", event.toString());
  }
}