package calendar.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import calendar.model.enumclass.EventStatus;
import calendar.model.enumclass.Location;
import calendar.model.event.Event;

/**
 * This class represents an interactive GUI for a virtual calendar application. It offers a
 * schedule view that displays events in the order they occur in for up to 10 events. It also
 * offers a monthly view to select a day to start from in the schedule view. It supports the
 * creation of events and calendars, editing events, switching calendars, and refreshing the view,
 * all through the use of buttons, and entering information in text fields, spinners, and cards.
 */
public class GUIView extends JFrame implements IGUIView {
  private final DefaultTableModel eventTableModel;
  private final JComboBox<String> calendarComboBox; //the dropdown for calendars
  private final JLabel statusLabel; //label to show the status of the selected date and time
  private final JTable eventTable;
  private final JTabbedPane tabbedPane;
  private final JPanel monthPanel;
  private final JPanel schedulePanel;
  private final JPanel controlPanel;
  private final JLabel monthLabel; //from starter
  private YearMonth currentMonth; //from starter
  private JSpinner startDateSpinner; //the text field for start date indicate the date for the calendar

  private JButton addEventButton;
  private JButton editEventButton;
  private JButton createCalendarButton;
  private JButton refreshButton;
  private JButton prevMonthButton;
  private JButton nextMonthButton;

  private Map<LocalDate, List<String>> events;

  private final DateTimeFormatter formatter;
  private final SimpleDateFormat dateFormat;

  /**
   * Initializes a new GUI view with the proper components.
   */
  public GUIView() {
    this.eventTableModel = new DefaultTableModel();
    this.eventTable = new JTable(eventTableModel);
    this.tabbedPane = new JTabbedPane();
    this.monthPanel = new JPanel(new BorderLayout());
    this.schedulePanel = new JPanel(new BorderLayout());
    this.controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    this.currentMonth = YearMonth.now();
    this.monthLabel = new JLabel("", SwingConstants.CENTER);
    this.statusLabel = new JLabel("Ready");
    this.calendarComboBox = new JComboBox<>();

    this.events = new HashMap<>();

    this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    setTitle("Calendar Application");
    setSize(1000, 700);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    initializeUI();
    setLocationRelativeTo(null);
  }

  /**
   * Initializes the user interface components of the calendar application.
   * This method sets up the main panel, control panel, event table, and other UI elements.
   */
  private void initializeUI() {
    setLayout(new BorderLayout());

    tabbedPane.addTab("Schedule View", schedulePanel);
    tabbedPane.addTab("Month View", monthPanel);
    add(tabbedPane, BorderLayout.CENTER);

    initializeScheduleView();

    initializeMonthView();

    initializeControlPanel();

    add(statusLabel, BorderLayout.SOUTH);
  }

  /**
   * Initializes the control panel of the calendar application.
   * This method sets up the calendar selection dropdown, date control, and action buttons.
   */
  private void initializeControlPanel() {
    //calendar selection dropdown
    controlPanel.add(new JLabel("Calendar:"));
    calendarComboBox.addItem("Default");
    calendarComboBox.setActionCommand("switchCalendarButton");
    controlPanel.add(calendarComboBox);

    //date control
    controlPanel.add(new JLabel("Start Date:"));
    startDateSpinner = new JSpinner(new SpinnerDateModel());
    JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
    startDateSpinner.setEditor(dateEditor);
    controlPanel.add(startDateSpinner);

    //Action buttons
    addEventButton = new JButton("Add Event");
    addEventButton.setActionCommand("addEventButton");
    controlPanel.add(addEventButton);

    //create calendar button
    createCalendarButton = new JButton("Create Calendar");
    createCalendarButton.setActionCommand("createCalendarButton");
    controlPanel.add(createCalendarButton);

    refreshButton = new JButton("Refresh");
    refreshButton.setActionCommand("refreshScheduleButton");
    controlPanel.add(refreshButton);

    add(controlPanel, BorderLayout.NORTH);

    //when user type, bring them to that date with events
    startDateSpinner.addChangeListener(e -> {
      try {
        Date startDate = (Date) startDateSpinner.getValue();
        LocalDate date = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        showEventsForDate(date);
      } catch (Exception ex) {
        this.displayException(new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd"));
      }
    });
  }

  /**
   * Initializes the schedule view of the calendar application.
   * This method sets up the event table, adds columns, and initializes the edit button.
   */
  private void initializeScheduleView() {
    eventTableModel.addColumn("Subject");
    eventTableModel.addColumn("Start Time");
    eventTableModel.addColumn("End Time");
    eventTableModel.addColumn("Description");
    eventTableModel.addColumn("Location");
    eventTableModel.addColumn("Status");
    eventTable.setRowSelectionAllowed(true);
    eventTable.setColumnSelectionAllowed(false);
    eventTable.setDefaultEditor(Object.class, null);
    eventTable.getTableHeader().setReorderingAllowed(false);

    JScrollPane scrollPane = new JScrollPane(eventTable);
    schedulePanel.add(scrollPane, BorderLayout.CENTER);

    //edit button
    editEventButton = new JButton("Edit Event");
    editEventButton.setActionCommand("editEventButton");
    schedulePanel.add(editEventButton, BorderLayout.SOUTH);
  }

  /**
   * Initializes the month view of the calendar application.
   * This method sets up the navigation buttons, month label, and the grid for displaying days.
   */
  private void initializeMonthView() {
    JPanel navigatePanel = new JPanel(new BorderLayout());
    prevMonthButton = new JButton("<");
    nextMonthButton = new JButton(">");
    prevMonthButton.setActionCommand("prevMonthButton");
    nextMonthButton.setActionCommand("nextMonthButton");

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(prevMonthButton);
    buttonPanel.add(nextMonthButton);

    navigatePanel.add(buttonPanel, BorderLayout.WEST);
    navigatePanel.add(monthLabel, BorderLayout.CENTER);
    monthPanel.add(navigatePanel, BorderLayout.NORTH);

    updateMonthView();
  }

  /**
   * Updates the month view by clearing the existing grid and creating a new one.
   */
  private void updateMonthView() {
    //keep the navigation buttons and month label
    //clear existing grid
    if (monthPanel.getComponentCount() > 1) {
      monthPanel.remove(1);
    }

    //create calendar grid
    JPanel gridPanel = new JPanel(new GridLayout(0, 7, 5, 5));
    gridPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    //set month label
    monthLabel.setText(currentMonth.getMonth() + " " + currentMonth.getYear());

    //add day headers
    for (DayOfWeek day : DayOfWeek.values()) {
      //mon tue wed thu fri sat sun
      JLabel label = new JLabel(day.getDisplayName(TextStyle.SHORT, Locale.getDefault()));
      label.setHorizontalAlignment(SwingConstants.CENTER);
      gridPanel.add(label);
    }


    //fill in days
    int dayOfWeekValue = currentMonth.atDay(1).getDayOfWeek().getValue();
    for (int i = 1; i < dayOfWeekValue; i++) {
      gridPanel.add(new JLabel("")); //empty labels for leading days
    }

    //from starter
    //add day buttons
    for (int day = 1; day <= currentMonth.lengthOfMonth(); day++) {
      LocalDate date = currentMonth.atDay(day);
      JButton dayButton = new JButton(String.valueOf(day));

      // Highlight today
      if (date.equals(LocalDate.now())) {
        dayButton.setBackground(new Color(220, 240, 255));
      }

      dayButton.addActionListener(e -> showEvents(date));
      gridPanel.add(dayButton);
    }

    monthPanel.add(gridPanel, BorderLayout.CENTER);
    monthPanel.revalidate();
    monthPanel.repaint();

  }

  //exactly from the starter
  private void showEvents(LocalDate date) {
    this.showEventsForDate(date);
//    List<String> dayEvents = events.getOrDefault(date, new ArrayList<>());
//    String eventList = dayEvents.isEmpty() ? "No events" : String.join("\n", dayEvents);
//    String newEvent = JOptionPane.showInputDialog(monthPanel, "Events on " + date + ":\n" + eventList + "\n\nAdd new event:");
//    if (newEvent != null && !newEvent.trim().isEmpty()) {
//      dayEvents.add(newEvent);
//      events.put(date, dayEvents);
//    }
  }

  @Override
  public void switchToScheduleView() {
    tabbedPane.setSelectedIndex(0);
  }

  @Override
  public void setStartDate(LocalDate startDate) {
    Date date = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    startDateSpinner.setValue(date);
  }

  @Override
  public void showEventsForDate(LocalDate date) {
    setStartDate(date);
    switchToScheduleView();
    //triger refesh to show events for the selected date
    refreshButton.doClick();
  }

  //fron starter
  @Override
  public void changeMonth(int direction) {
    currentMonth = currentMonth.plusMonths(direction);
    updateMonthView();
  }

  @Override
  public void setListeners(ActionListener listener) {
    addEventButton.addActionListener(listener);
    createCalendarButton.addActionListener(listener);
    editEventButton.addActionListener(listener);
    refreshButton.addActionListener(listener);
    calendarComboBox.addActionListener(listener);
    prevMonthButton.addActionListener(listener);
    nextMonthButton.addActionListener(listener);
  }

  @Override
  public void showStatusOnDayTime(boolean status) {
    //dont need it in GUI
  }

  @Override
  public void addCalendar(String name) {
    //add a calendar to the dropdown
    calendarComboBox.addItem(name);
  }

  @Override
  public String getSelectedEventSubject() {
    int selectedRow = eventTable.getSelectedRow();
    if (selectedRow == -1) {
      throw new IllegalArgumentException("No event selected!");
    }
    return (String) eventTable.getValueAt(selectedRow, 0);
  }

  @Override
  public String getSelectedEventStart() {
    int selectedRow = eventTable.getSelectedRow();
    if (selectedRow == -1) {
      throw new IllegalArgumentException("No event selected!");
    }
    return (String) eventTable.getValueAt(selectedRow, 1);
  }

  @Override
  public String getSelectedEventEnd() {
    int selectedRow = eventTable.getSelectedRow();
    if (selectedRow == -1) {
      throw new IllegalArgumentException("No event selected!");
    }
    return (String) eventTable.getValueAt(selectedRow, 2);
  }

  @Override
  public String getSelectedCalendar() {
    return (String) calendarComboBox.getSelectedItem();
  }

  @Override
  public List<String> showAddEventDialog() {
    List<String> result = new ArrayList<>();

    JDialog dialog = new JDialog(this, "Input Dialog", true);
    dialog.setLayout(new BorderLayout());
    dialog.setTitle("Add Event");
    dialog.setResizable(false);

    JPanel popup = new JPanel();
    popup.setLayout(new BoxLayout(popup, BoxLayout.Y_AXIS));

    JPanel textPanel1 = new JPanel();
    JTextField textField1 = new JTextField(20);
    JLabel label1 = new JLabel("Name: ");
    textPanel1.add(label1);
    textPanel1.add(textField1);

    // TODO make date current date, keep time
    JPanel dateTimePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel dateTimeLabel = new JLabel("Start Date/Time: ");
    SpinnerDateModel dateTimeModel = new SpinnerDateModel();
    JSpinner dateSpinner = new JSpinner(dateTimeModel);
    dateTimePanel.add(dateTimeLabel);
    dateTimePanel.add(dateSpinner);

    // TODO make date current date, keep time
    JPanel dateTimePanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel dateTimeLabel2 = new JLabel("End Date/Time: ");
    Date plusOneMin = new Date(new Date().getTime() + 60000);
    SpinnerDateModel dateTimeModel2 = new SpinnerDateModel(
            plusOneMin, null, null, Calendar.MINUTE);
    JSpinner dateSpinner2 = new JSpinner(dateTimeModel2);
    dateTimePanel2.add(dateTimeLabel2);
    dateTimePanel2.add(dateSpinner2);

    JButton okButton = new JButton("OK");
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(okButton);

    //Description panel
    JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JTextField descriptionTextField = new JTextField(20);
    JLabel descriptionLabel = new JLabel("Description: ");
    descriptionPanel.add(descriptionLabel);
    descriptionPanel.add(descriptionTextField);

    //Location panel
    JPanel locationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JComboBox<Location> locationComboBox = new JComboBox<>(Location.values());
    JLabel locationLabel = new JLabel("Location: ");
    locationPanel.add(locationLabel);
    locationPanel.add(locationComboBox);

    //Status panel
    JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JComboBox<EventStatus> statusComboBox = new JComboBox<>(EventStatus.values());
    JLabel statusLabel = new JLabel("Status: ");
    statusPanel.add(statusLabel);
    statusPanel.add(statusComboBox);

    popup.add(textPanel1);
    popup.add(dateTimePanel);
    popup.add(dateTimePanel2);
    popup.add(descriptionPanel);
    popup.add(locationPanel);
    popup.add(statusPanel);

    dialog.add(popup);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    okButton.addActionListener(e -> {
      String name = textField1.getText();
      Date startDate = (Date) dateSpinner.getValue();
      Date endDate = (Date) dateSpinner2.getValue();
      LocalDateTime startLDT = startDate.toInstant()
              .atZone(ZoneId.systemDefault())
              .toLocalDateTime();
      LocalDateTime endLDT = endDate.toInstant()
              .atZone(ZoneId.systemDefault())
              .toLocalDateTime();
      String startDateStr = this.formatter.format(startLDT);
      String endDateStr = this.formatter.format(endLDT);

      if (name.trim().isEmpty()) {
        JOptionPane.showMessageDialog(dialog, "Please enter a name!");
        return;
      }
      if (startDateStr.trim().isEmpty()) {
        JOptionPane.showMessageDialog(dialog, "Please enter a start date!");
        return;
      }
      if (endDateStr.trim().isEmpty()) {
        JOptionPane.showMessageDialog(dialog, "Please enter a end date!");
        return;
      }

      result.add(name);
      result.add(startDateStr);
      result.add(endDateStr);
      result.add(descriptionTextField.getText());
      result.add(Objects.requireNonNull(locationComboBox.getSelectedItem()).toString());
      result.add(Objects.requireNonNull(statusComboBox.getSelectedItem()).toString());
      dialog.dispose();
    });

    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);

    return result;
  }

  @Override
  public List<String> showEditEventDialog() {
    ArrayList<String> result = new ArrayList<>();

    ArrayList<String> selectedCard = new ArrayList<>(List.of("Subject"));

    JDialog dialog = new JDialog(this, "Input Dialog", true);
    dialog.setLayout(new BorderLayout());
    dialog.setTitle("Edit Event");
    dialog.setResizable(false);

    CardLayout cardLayout = new CardLayout();
    JPanel cardPanel = new JPanel(cardLayout);

    JPanel subjectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel subjectLabel = new JLabel("Enter a new subject: ");
    JTextField textField1 = new JTextField(20);
    subjectPanel.add(subjectLabel);
    subjectPanel.add(textField1);
    JPanel subjectWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
    subjectWrapper.add(subjectPanel);

    JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel startLabel = new JLabel("Enter a new start date/time: ");
    SpinnerDateModel dateTimeModel = new SpinnerDateModel();
    JSpinner dateSpinner = new JSpinner(dateTimeModel);
    startPanel.add(startLabel);
    startPanel.add(dateSpinner);
    JPanel startWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
    startWrapper.add(startPanel);

    JPanel endPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JLabel endLabel = new JLabel("Enter a new end date/time: ");
    JSpinner dateSpinner2 = new JSpinner(dateTimeModel);
    endPanel.add(endLabel);
    endPanel.add(dateSpinner2);
    JPanel endWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
    endWrapper.add(endPanel);

    cardPanel.add(subjectWrapper, "Subject");
    cardPanel.add(startWrapper, "Start");
    cardPanel.add(endWrapper, "End");

    //Add new properties to the card layout
    JPanel descriptionPanel = this.createTextPanel("Description:", new JTextField(20));
    JPanel locationPanel = this.createComboPanel("Location:", Location.values());
    JPanel statusPanel = this.createComboPanel("Status:", EventStatus.values());

    cardPanel.add(descriptionPanel, "Description");
    cardPanel.add(locationPanel, "Location");
    cardPanel.add(statusPanel, "Status");

    String[] options = {"Subject", "Start", "End", "Description", "Location", "Status"};
    JComboBox<String> cardSelector = new JComboBox<>(options);
    cardSelector.addActionListener(e -> {
      String selected = (String) cardSelector.getSelectedItem();
      selectedCard.set(0, selected);
      cardLayout.show(cardPanel, selected);
    });

    JButton okButton = new JButton("OK");
    okButton.addActionListener(e -> {
      switch (selectedCard.get(0)) {
        case "Subject":
          result.add("subject");
          result.add(textField1.getText());
          break;
        case "Start":
          result.add("start");
          Date startDate = (Date) dateSpinner.getValue();
          LocalDateTime startLDT = startDate.toInstant()
                  .atZone(ZoneId.systemDefault())
                  .toLocalDateTime();
          String startDateStr = this.formatter.format(startLDT);
          result.add(startDateStr);
          break;
        case "End":
          result.add("end");
          Date endDate = (Date) dateSpinner2.getValue();
          LocalDateTime endLDT = endDate.toInstant()
                  .atZone(ZoneId.systemDefault())
                  .toLocalDateTime();
          String endDateStr = this.formatter.format(endLDT);
          result.add(endDateStr);
          break;
        case "Description":
          result.add("description");
          result.add(((JTextField) getComponentFromPanel(descriptionPanel, 1)).getText());
          break;
        case "Location":
          result.add("location");
          result.add(Objects.requireNonNull(((JComboBox<?>) getComponentFromPanel(locationPanel, 1))
                  .getSelectedItem()).toString());
          break;
        case "Status":
          result.add("status");
          result.add(Objects.requireNonNull(((JComboBox<?>) getComponentFromPanel(statusPanel, 1))
                  .getSelectedItem()).toString());
          break;
      }
      dialog.dispose();
    });

    JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel selectorLabel = new JLabel("Choose a property to edit: ");
    selectorPanel.add(selectorLabel);
    selectorPanel.add(cardSelector);

    JPanel okButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    okButtonPanel.add(okButton);

    // Assemble dialog
    dialog.add(cardPanel, BorderLayout.CENTER);
    dialog.add(selectorPanel, BorderLayout.NORTH);
    dialog.add(okButtonPanel, BorderLayout.SOUTH);

    dialog.pack();
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);

    return result;
  }

  /**
   * Retrieves a component from a panel at the specified index.
   *
   * @param panel the panel to retrieve the component from
   * @param index the index of the component to retrieve
   * @return the component at the specified index
   */
  private Component getComponentFromPanel(JPanel panel, int index) {
    return ((JPanel) panel.getComponent(0)).getComponent(index);
  }

  /**
   * Creates a text panel with a label and a text field.
   * @param labelText the text for the label
   * @param textField the text field to be added to the panel
   * @return
   */
  private JPanel createTextPanel(String labelText, JTextField textField) {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panel.add(new JLabel(labelText));
    panel.add(textField);
    JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
    wrapper.add(panel);
    return wrapper;
  }

  /**
   * Creates a combo panel with a label and a combo box populated with enum values.
   * @param labelText the text for the label
   * @param enumValues the enum values to populate the combo box
   * @return a JPanel containing the label and combo box
   */
  private JPanel createComboPanel(String labelText, Enum[] enumValues) {
    JComboBox<String> comboBox = new JComboBox<>();
    for (Enum value : enumValues) {
      comboBox.addItem(value.toString());
    }
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panel.add(new JLabel(labelText));
    panel.add(comboBox);
    JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
    wrapper.add(panel);
    return wrapper;
  }

  @Override
  public List<String> showCreateCalendarDialog() {
    List<String> result = new ArrayList<>();

    JDialog dialog = new JDialog(this, "Create Calendar", true);
    dialog.setLayout(new BorderLayout());
    dialog.setTitle("Create Calendar");
    dialog.setSize(400, 200);

    JPanel contentPanel = new JPanel(new GridLayout(3, 2, 5, 5));
    contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    //name selection
    JLabel nameLabel = new JLabel("Calendar Name:");
    JTextField nameTextField = new JTextField(20);
    contentPanel.add(nameLabel);
    contentPanel.add(nameTextField);

    //time zone selection
    JLabel tzLabel = new JLabel("Time Zone:");
    JComboBox<String> tzComboBox = new JComboBox<>();
    tzComboBox.setEditable(true); //allow user to type for tz

    ZoneId.getAvailableZoneIds().stream().sorted().forEach(tzComboBox::addItem);
    contentPanel.add(tzLabel);
    contentPanel.add(tzComboBox);

    JPanel buttonPanel = new JPanel();
    JButton okButton = new JButton("OK");
    JButton cancelButton = new JButton("Cancel");
    buttonPanel.add(okButton);
    buttonPanel.add(cancelButton);

    okButton.addActionListener(e -> {
      String name = nameTextField.getText();
      String tz = Objects.requireNonNull(tzComboBox.getSelectedItem()).toString();

      if (name.isEmpty()) {
        JOptionPane.showMessageDialog(dialog, "Please enter a calendar name!");
      }
      if (tz.trim().isEmpty()) {
        JOptionPane.showMessageDialog(dialog, "Please select or enter a time zone!");
      }

      try {
        ZoneId.of(tz); //validate the time zone
      } catch (DateTimeException ex) {
        JOptionPane.showMessageDialog(dialog, "Invalid time zone: " + tz);
      }
      result.add(name);
      result.add(tz);
      dialog.dispose();
    });

    cancelButton.addActionListener(e -> dialog.dispose());

    dialog.add(contentPanel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);

    return result;
  }

  @Override
  public String getStartDate() {
    return dateFormat.format((Date) startDateSpinner.getValue());
  }

  @Override
  public void displayMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
    updateStatusLabel(message);

  }

  @Override
  public void displayException(Exception e) {
    JOptionPane.showMessageDialog(this, e.getMessage());
    updateStatusLabel("Error: " + e.getMessage());

  }

  @Override
  public void printEvents(List<Event> events) {
    //sort the event displaying by start time
    events.sort(Comparator.comparing(Event::getStartDateTime));
    this.eventTableModel.setRowCount(0);
    for (Event e : events) {
      eventTableModel.addRow(new Object[]{
              e.getSubject(),
              e.getStartDateTime().format(formatter),
              e.getEndDateTime().format(formatter),
              e.getDescription(),
              e.getLocation() != null ? e.getLocation().toString() : "",
              e.getStatus().toString()
      });
    }
  }

  /**
   * Updates the status label with a given message.
   *
   * @param message The message to be displayed in the status label
   */
  private void updateStatusLabel(String message) {
    statusLabel.setText(message);
  }

}
