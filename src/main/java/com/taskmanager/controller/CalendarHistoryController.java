package com.taskmanager.controller;

import com.taskmanager.dao.DailyReviewDAO;
import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.DailyReview;
import com.taskmanager.model.DayStatistics;
import com.taskmanager.model.Task;
import com.taskmanager.util.AlertUtil;
import com.taskmanager.util.DateUtil;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Controller cho màn hình lịch sử công việc theo lịch
 * Hiển thị calendar với màu sắc biểu thị số lượng task hoàn thành
 */
public class CalendarHistoryController {
    
    @FXML private Label lblMonthYear;
    @FXML private GridPane calendarGrid;
    @FXML private ListView<Task> taskListView;
    @FXML private TextArea notesArea;
    @FXML private Label lblSelectedDate;
    @FXML private Label lblStats;
    
    private TaskDAO taskDAO;
    private DailyReviewDAO dailyReviewDAO;
    private YearMonth currentMonth;
    private LocalDate selectedDate;
    private Map<LocalDate, DayStatistics> statsMap;
    
    @FXML
    public void initialize() {
        taskDAO = new TaskDAO();
        dailyReviewDAO = new DailyReviewDAO();
        currentMonth = YearMonth.now();
        selectedDate = LocalDate.now();
        
        renderCalendar();
        loadDayDetails(selectedDate);
    }
    
    /**
     * Render calendar cho tháng hiện tại
     */
    private void renderCalendar() {
        // Clear existing calendar
        calendarGrid.getChildren().clear();
        
        // Update month/year label
        String monthName = currentMonth.getMonth().getDisplayName(TextStyle.FULL, new Locale("vi", "VN"));
        lblMonthYear.setText(monthName + " " + currentMonth.getYear());
        
        // Load statistics for the month
        LocalDate startDate = currentMonth.atDay(1);
        LocalDate endDate = currentMonth.atEndOfMonth();
        
        try {
            statsMap = taskDAO.getStatisticsByDateRange(startDate, endDate);
        } catch (SQLException e) {
            AlertUtil.showError("Lỗi", "Không thể tải dữ liệu thống kê: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        // Add day headers (T2 - CN)
        String[] dayHeaders = {"T2", "T3", "T4", "T5", "T6", "T7", "CN"};
        for (int i = 0; i < 7; i++) {
            Label header = new Label(dayHeaders[i]);
            header.setFont(Font.font("System", FontWeight.BOLD, 12));
            header.setMaxWidth(Double.MAX_VALUE);
            header.setAlignment(Pos.CENTER);
            header.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 5px;");
            calendarGrid.add(header, i, 0);
        }
        
        // Get first day of month and calculate offset
        LocalDate firstDay = currentMonth.atDay(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday
        int offset = dayOfWeek - 1; // Convert to 0-based index
        
        // Fill calendar cells
        int daysInMonth = currentMonth.lengthOfMonth();
        int row = 1;
        int col = offset;
        
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.atDay(day);
            VBox dayCell = createDayCell(date);
            
            calendarGrid.add(dayCell, col, row);
            
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
        
        // Set column constraints
        for (int i = 0; i < 7; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / 7);
            calendarGrid.getColumnConstraints().add(colConstraints);
        }
    }
    
    /**
     * Tạo ô ngày trong calendar
     */
    private VBox createDayCell(LocalDate date) {
        VBox cell = new VBox();
        cell.setAlignment(Pos.TOP_CENTER);
        cell.setPadding(new Insets(5));
        cell.setMinHeight(60);
        cell.setMaxWidth(Double.MAX_VALUE);
        cell.setSpacing(3);
        
        // Day number label
        Label dayLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dayLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        // Get statistics for this day
        DayStatistics stats = statsMap.get(date);
        
        // Set background color based on completed tasks
        String bgColor;
        String textColor = "black";
        
        if (stats == null || !stats.hasTasks()) {
            // No tasks - normal color
            bgColor = "#ecf0f1";
        } else {
            int completedTasks = stats.getCompletedTasks();
            
            if (completedTasks == 0) {
                // Has tasks but none completed - light red
                bgColor = "#fadbd8";
            } else if (completedTasks <= 2) {
                // Few completed tasks - light green
                bgColor = "#d5f4e6";
            } else if (completedTasks <= 5) {
                // Medium completed tasks - medium green
                bgColor = "#82e0aa";
            } else {
                // Many completed tasks - dark green
                bgColor = "#27ae60";
                textColor = "white";
            }
            
            // Add task count label
            Label countLabel = new Label(completedTasks + "/" + stats.getTotalTasks());
            countLabel.setFont(Font.font("System", 10));
            countLabel.setStyle("-fx-text-fill: " + textColor + ";");
            cell.getChildren().add(countLabel);
        }
        
        // Highlight today
        if (DateUtil.isToday(date)) {
            cell.setStyle("-fx-background-color: " + bgColor + "; " +
                         "-fx-border-color: #e74c3c; -fx-border-width: 2px; " +
                         "-fx-cursor: hand;");
        } else if (date.equals(selectedDate)) {
            // Highlight selected date
            cell.setStyle("-fx-background-color: " + bgColor + "; " +
                         "-fx-border-color: #3498db; -fx-border-width: 2px; " +
                         "-fx-cursor: hand;");
        } else {
            cell.setStyle("-fx-background-color: " + bgColor + "; " +
                         "-fx-border-color: #bdc3c7; -fx-border-width: 1px; " +
                         "-fx-cursor: hand;");
        }
        
        dayLabel.setStyle("-fx-text-fill: " + textColor + ";");
        cell.getChildren().add(0, dayLabel);
        
        // Add click handler
        cell.setOnMouseClicked(event -> {
            selectedDate = date;
            loadDayDetails(date);
            renderCalendar(); // Re-render to update selection
        });
        
        return cell;
    }
    
    /**
     * Load chi tiết công việc và ghi chú cho ngày được chọn
     */
    private void loadDayDetails(LocalDate date) {
        lblSelectedDate.setText("Ngày: " + DateUtil.formatDate(date));
        
        try {
            // Load tasks for the selected date
            List<Task> tasks = taskDAO.findByDate(date);
            taskListView.getItems().setAll(tasks);
            
            // Custom cell factory to display task info
            taskListView.setCellFactory(param -> new ListCell<Task>() {
                @Override
                protected void updateItem(Task task, boolean empty) {
                    super.updateItem(task, empty);
                    if (empty || task == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        String priorityIcon = switch (task.getPriority()) {
                            case HIGH -> "🔴";
                            case MEDIUM -> "🟡";
                            case LOW -> "🟢";
                        };
                        String status = task.isCompleted() ? "✓" : "○";
                        setText(status + " " + priorityIcon + " " + task.getTitle());
                        
                        if (task.isCompleted()) {
                            setStyle("-fx-text-fill: #7f8c8d; -fx-strikethrough: true;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            });
            
            // Load daily review notes
            DailyReview review = dailyReviewDAO.findByDate(date);
            if (review != null && review.getNotes() != null) {
                notesArea.setText(review.getNotes());
            } else {
                notesArea.setText("(Chưa có ghi chú cho ngày này)");
            }
            
            // Update statistics label
            int total = tasks.size();
            long completed = tasks.stream().filter(Task::isCompleted).count();
            double rate = total > 0 ? (completed * 100.0 / total) : 0.0;
            lblStats.setText(String.format("Tổng: %d | Hoàn thành: %d | Tỷ lệ: %.0f%%", 
                                          total, completed, rate));
            
        } catch (SQLException e) {
            AlertUtil.showError("Lỗi", "Không thể tải dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handlePreviousMonth() {
        currentMonth = currentMonth.minusMonths(1);
        renderCalendar();
    }
    
    @FXML
    private void handleNextMonth() {
        currentMonth = currentMonth.plusMonths(1);
        renderCalendar();
    }
    
    @FXML
    private void handleToday() {
        currentMonth = YearMonth.now();
        selectedDate = LocalDate.now();
        renderCalendar();
        loadDayDetails(selectedDate);
    }
    
    @FXML
    private void handleClose() {
        // Close the window
        calendarGrid.getScene().getWindow().hide();
    }
}
