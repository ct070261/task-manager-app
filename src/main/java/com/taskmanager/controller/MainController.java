package com.taskmanager.controller;

import com.taskmanager.dao.TaskDAO;
import com.taskmanager.model.Task;
import com.taskmanager.util.AlertUtil;
import com.taskmanager.util.DateUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller cho màn hình chính của ứng dụng
 * Quản lý hiển thị và thao tác với danh sách tasks
 */
public class MainController {
    
    @FXML private DatePicker datePicker;
    @FXML private RadioButton filterAll;
    @FXML private RadioButton filterCompleted;
    @FXML private RadioButton filterPending;
    @FXML private CheckBox filterHigh;
    @FXML private CheckBox filterMedium;
    @FXML private CheckBox filterLow;
    @FXML private TextField searchField;
    
    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, Boolean> colCompleted;
    @FXML private TableColumn<Task, String> colTitle;
    @FXML private TableColumn<Task, String> colDescription;
    @FXML private TableColumn<Task, String> colPriority;
    @FXML private TableColumn<Task, String> colDate;
    @FXML private TableColumn<Task, Void> colActions;
    
    @FXML private Label lblTotalTasks;
    @FXML private Label lblCompletedTasks;
    @FXML private Label lblPendingTasks;
    @FXML private Label lblCompletionRate;
    @FXML private ProgressBar progressBar;
    
    @FXML private Label statusLabel;
    @FXML private Label dateLabel;
    
    private TaskDAO taskDAO;
    private ObservableList<Task> allTasks;
    private ObservableList<Task> filteredTasks;
    
    /**
     * Initialize controller
     */
    @FXML
    public void initialize() {
        taskDAO = new TaskDAO();
        allTasks = FXCollections.observableArrayList();
        filteredTasks = FXCollections.observableArrayList();
        
        // Set current date
        datePicker.setValue(LocalDate.now());
        updateDateLabel();
        
        // Setup table columns
        setupTableColumns();
        
        // Setup context menu for table
        setupContextMenu();
        
        // Load initial data
        loadTasks();
        
        // Update status
        statusLabel.setText("Sẵn sàng");
    }
    
    /**
     * Cấu hình các cột của bảng
     */
    private void setupTableColumns() {
        // Completed column with checkbox
        colCompleted.setCellFactory(col -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();
            
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Task task = getTableRow().getItem();
                    checkBox.setSelected(task.isCompleted());
                    checkBox.setOnAction(e -> handleToggleComplete(task));
                    setGraphic(checkBox);
                }
            }
        });
        
        // Title column
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colTitle.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Task task = getTableRow().getItem();
                    setText(item);
                    if (task.isCompleted()) {
                        setStyle("-fx-text-fill: #95a5a6; -fx-strikethrough: true;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
        
        // Description column
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDescription.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Task task = getTableRow().getItem();
                    setText(item);
                    if (task.isCompleted()) {
                        setStyle("-fx-text-fill: #95a5a6;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
        
        // Priority column with color coding
        colPriority.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getPriority().getDisplayName()));
        colPriority.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Task task = getTableRow().getItem();
                    setText(item);
                    
                    String style = switch (task.getPriority()) {
                        case HIGH -> "-fx-text-fill: #e74c3c; -fx-font-weight: bold;";
                        case MEDIUM -> "-fx-text-fill: #f39c12;";
                        case LOW -> "-fx-text-fill: #2ecc71;";
                    };
                    
                    if (task.isCompleted()) {
                        style += " -fx-strikethrough: true;";
                    }
                    setStyle(style);
                }
            }
        });
        
        // Date column
        colDate.setCellValueFactory(cellData -> 
            new SimpleStringProperty(DateUtil.formatDate(cellData.getValue().getTaskDate())));
        
        // Actions column with Edit and Delete buttons
        colActions.setCellFactory(col -> new TableCell<>() {
            private final Button btnEdit = new Button("Sửa");
            private final Button btnDelete = new Button("Xóa");
            private final HBox pane = new HBox(5, btnEdit, btnDelete);
            
            {
                btnEdit.setOnAction(e -> {
                    Task task = getTableRow().getItem();
                    if (task != null) {
                        handleEditTask(task);
                    }
                });
                
                btnDelete.setOnAction(e -> {
                    Task task = getTableRow().getItem();
                    if (task != null) {
                        handleDeleteTask(task);
                    }
                });
                
                btnEdit.getStyleClass().add("button");
                btnDelete.getStyleClass().add("button-danger");
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }
    
    /**
     * Cấu hình context menu cho bảng
     */
    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        
        MenuItem markComplete = new MenuItem("Đánh dấu hoàn thành");
        markComplete.setOnAction(e -> {
            Task task = taskTable.getSelectionModel().getSelectedItem();
            if (task != null && !task.isCompleted()) {
                handleToggleComplete(task);
            }
        });
        
        MenuItem markIncomplete = new MenuItem("Đánh dấu chưa hoàn thành");
        markIncomplete.setOnAction(e -> {
            Task task = taskTable.getSelectionModel().getSelectedItem();
            if (task != null && task.isCompleted()) {
                handleToggleComplete(task);
            }
        });
        
        MenuItem edit = new MenuItem("Sửa");
        edit.setOnAction(e -> {
            Task task = taskTable.getSelectionModel().getSelectedItem();
            if (task != null) {
                handleEditTask(task);
            }
        });
        
        MenuItem delete = new MenuItem("Xóa");
        delete.setOnAction(e -> {
            Task task = taskTable.getSelectionModel().getSelectedItem();
            if (task != null) {
                handleDeleteTask(task);
            }
        });
        
        MenuItem duplicate = new MenuItem("Nhân bản");
        duplicate.setOnAction(e -> {
            Task task = taskTable.getSelectionModel().getSelectedItem();
            if (task != null) {
                handleDuplicateTask(task);
            }
        });
        
        contextMenu.getItems().addAll(markComplete, markIncomplete, 
            new SeparatorMenuItem(), edit, delete, duplicate);
        
        taskTable.setContextMenu(contextMenu);
    }
    
    /**
     * Load tasks từ database
     */
    private void loadTasks() {
        try {
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate == null) {
                selectedDate = LocalDate.now();
            }
            
            List<Task> tasks = taskDAO.findByDate(selectedDate);
            allTasks.setAll(tasks);
            applyFilters();
            updateQuickStats();
            
            statusLabel.setText("Đã tải " + tasks.size() + " công việc");
        } catch (SQLException e) {
            AlertUtil.showError("Lỗi", "Không thể tải danh sách công việc: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Áp dụng bộ lọc lên danh sách tasks
     */
    private void applyFilters() {
        List<Task> filtered = allTasks.stream()
            .filter(this::matchesStatusFilter)
            .filter(this::matchesPriorityFilter)
            .filter(this::matchesSearchFilter)
            .collect(Collectors.toList());
        
        filteredTasks.setAll(filtered);
        taskTable.setItems(filteredTasks);
    }
    
    /**
     * Kiểm tra task có khớp với bộ lọc trạng thái không
     */
    private boolean matchesStatusFilter(Task task) {
        if (filterAll.isSelected()) {
            return true;
        } else if (filterCompleted.isSelected()) {
            return task.isCompleted();
        } else if (filterPending.isSelected()) {
            return !task.isCompleted();
        }
        return true;
    }
    
    /**
     * Kiểm tra task có khớp với bộ lọc priority không
     */
    private boolean matchesPriorityFilter(Task task) {
        return switch (task.getPriority()) {
            case HIGH -> filterHigh.isSelected();
            case MEDIUM -> filterMedium.isSelected();
            case LOW -> filterLow.isSelected();
        };
    }
    
    /**
     * Kiểm tra task có khớp với từ khóa tìm kiếm không
     */
    private boolean matchesSearchFilter(Task task) {
        String keyword = searchField.getText();
        if (keyword == null || keyword.trim().isEmpty()) {
            return true;
        }
        
        keyword = keyword.toLowerCase();
        String title = task.getTitle().toLowerCase();
        String description = task.getDescription() != null ? 
            task.getDescription().toLowerCase() : "";
        
        return title.contains(keyword) || description.contains(keyword);
    }
    
    /**
     * Cập nhật thống kê nhanh
     */
    private void updateQuickStats() {
        int total = allTasks.size();
        long completed = allTasks.stream().filter(Task::isCompleted).count();
        int pending = total - (int) completed;
        double completionRate = total > 0 ? (completed * 100.0 / total) : 0.0;
        
        lblTotalTasks.setText(String.valueOf(total));
        lblCompletedTasks.setText(String.valueOf(completed));
        lblPendingTasks.setText(String.valueOf(pending));
        lblCompletionRate.setText(String.format("%.0f%%", completionRate));
        progressBar.setProgress(completionRate / 100.0);
    }
    
    /**
     * Cập nhật label ngày
     */
    private void updateDateLabel() {
        LocalDate date = datePicker.getValue();
        if (date != null) {
            String dateStr = DateUtil.formatDate(date);
            if (DateUtil.isToday(date)) {
                dateLabel.setText("Hôm nay - " + dateStr);
            } else if (DateUtil.isYesterday(date)) {
                dateLabel.setText("Hôm qua - " + dateStr);
            } else if (DateUtil.isTomorrow(date)) {
                dateLabel.setText("Ngày mai - " + dateStr);
            } else {
                dateLabel.setText(dateStr);
            }
        }
    }
    
    // Event Handlers
    
    @FXML
    private void handleDateChange() {
        updateDateLabel();
        loadTasks();
    }
    
    @FXML
    private void handleFilterChange() {
        applyFilters();
    }
    
    @FXML
    private void handleSearch() {
        applyFilters();
    }
    
    @FXML
    private void handleAddTask() {
        showTaskDialog(null);
    }
    
    @FXML
    private void handleToday() {
        datePicker.setValue(LocalDate.now());
        handleDateChange();
    }
    
    @FXML
    private void handleRefresh() {
        loadTasks();
    }
    
    @FXML
    private void handleExit() {
        Platform.exit();
    }
    
    @FXML
    private void handleShowTasks() {
        // Already on tasks view
    }
    
    @FXML
    private void handleShowDailyReview() {
        showDailyReviewDialog();
    }
    
    @FXML
    private void handleShowStatistics() {
        showStatisticsWindow();
    }
    
    @FXML
    private void handleAbout() {
        AlertUtil.showInfo("Về ứng dụng", 
            "Task Manager v1.0.0\n\n" +
            "Ứng dụng quản lý công việc hàng ngày\n" +
            "Sử dụng JavaFX 21 và MySQL Database\n\n" +
            "© 2024");
    }
    
    @FXML
    private void handleCopyTasks() {
        showCopyTasksDialog();
    }
    
    /**
     * Toggle trạng thái hoàn thành của task
     */
    private void handleToggleComplete(Task task) {
        try {
            taskDAO.toggleComplete(task.getId());
            task.setCompleted(!task.isCompleted());
            taskTable.refresh();
            updateQuickStats();
            statusLabel.setText("Đã cập nhật trạng thái công việc");
        } catch (SQLException e) {
            AlertUtil.showError("Lỗi", "Không thể cập nhật trạng thái: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Hiển thị dialog để sửa task
     */
    private void handleEditTask(Task task) {
        showTaskDialog(task);
    }
    
    /**
     * Xóa task
     */
    private void handleDeleteTask(Task task) {
        boolean confirmed = AlertUtil.showConfirmation(
            "Xác nhận xóa",
            "Bạn có chắc chắn muốn xóa công việc:\n" + task.getTitle() + "?"
        );
        
        if (confirmed) {
            try {
                taskDAO.delete(task.getId());
                allTasks.remove(task);
                applyFilters();
                updateQuickStats();
                statusLabel.setText("Đã xóa công việc");
            } catch (SQLException e) {
                AlertUtil.showError("Lỗi", "Không thể xóa công việc: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Nhân bản task
     */
    private void handleDuplicateTask(Task task) {
        Task newTask = new Task();
        newTask.setTitle(task.getTitle() + " (Copy)");
        newTask.setDescription(task.getDescription());
        newTask.setTaskDate(task.getTaskDate());
        newTask.setPriority(task.getPriority());
        newTask.setCompleted(false);
        
        showTaskDialog(newTask);
    }
    
    /**
     * Hiển thị dialog thêm/sửa task
     */
    private void showTaskDialog(Task task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/task-form.fxml"));
            DialogPane dialogPane = loader.load();
            
            TaskController controller = loader.getController();
            controller.setTask(task);
            controller.setDatePicker(datePicker.getValue());
            
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle(task == null ? "Thêm công việc mới" : "Sửa công việc");
            
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                Task savedTask = controller.getTask();
                if (savedTask != null) {
                    loadTasks();
                    statusLabel.setText(task == null ? "Đã thêm công việc mới" : "Đã cập nhật công việc");
                }
            }
        } catch (IOException e) {
            AlertUtil.showError("Lỗi", "Không thể mở form: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Hiển thị dialog đánh giá cuối ngày
     */
    private void showDailyReviewDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/daily-review.fxml"));
            DialogPane dialogPane = loader.load();
            
            DailyReviewController controller = loader.getController();
            controller.setDate(datePicker.getValue());
            
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Đánh giá cuối ngày");
            
            dialog.showAndWait();
        } catch (IOException e) {
            AlertUtil.showError("Lỗi", "Không thể mở form đánh giá: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Hiển thị cửa sổ thống kê
     */
    private void showStatisticsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/statistics.fxml"));
            Scene scene = new Scene(loader.load());
            
            Stage stage = new Stage();
            stage.setTitle("Thống kê");
            stage.setScene(scene);
            stage.initModality(Modality.NONE);
            stage.show();
        } catch (IOException e) {
            AlertUtil.showError("Lỗi", "Không thể mở cửa sổ thống kê: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Hiển thị dialog copy tasks
     */
    private void showCopyTasksDialog() {
        try {
            // Tạo dialog tùy chỉnh
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Copy Tasks");
            dialog.setHeaderText("Copy công việc từ ngày này sang ngày khác");
            
            // Tạo content
            VBox content = new VBox(15);
            content.setPadding(new javafx.geometry.Insets(20));
            
            DatePicker sourceDate = new DatePicker(datePicker.getValue());
            DatePicker targetDate = new DatePicker(LocalDate.now());
            
            ListView<Task> taskListView = new ListView<>();
            taskListView.setPrefHeight(200);
            taskListView.setCellFactory(lv -> new CheckBoxListCell<>(task -> {
                javafx.beans.property.BooleanProperty observable = 
                    new javafx.beans.property.SimpleBooleanProperty(false);
                return observable;
            }, task -> task.getTitle()));
            
            // Load tasks khi chọn ngày nguồn
            sourceDate.setOnAction(e -> {
                try {
                    List<Task> tasks = taskDAO.findPendingByDate(sourceDate.getValue());
                    taskListView.getItems().setAll(tasks);
                } catch (SQLException ex) {
                    AlertUtil.showError("Lỗi", "Không thể tải danh sách tasks: " + ex.getMessage());
                }
            });
            
            // Load initial tasks
            List<Task> initialTasks = taskDAO.findPendingByDate(sourceDate.getValue());
            taskListView.getItems().setAll(initialTasks);
            
            content.getChildren().addAll(
                new Label("Ngày nguồn:"),
                sourceDate,
                new Label("Ngày đích:"),
                targetDate,
                new Label("Chọn tasks cần copy (chỉ hiển thị tasks chưa hoàn thành):"),
                taskListView
            );
            
            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Lấy các tasks được chọn
                List<Long> selectedIds = taskListView.getSelectionModel().getSelectedItems()
                    .stream()
                    .map(Task::getId)
                    .collect(Collectors.toList());
                
                if (selectedIds.isEmpty()) {
                    AlertUtil.showWarning("Cảnh báo", "Vui lòng chọn ít nhất một task để copy");
                    return;
                }
                
                taskDAO.copyTasksToDate(selectedIds, targetDate.getValue());
                AlertUtil.showInfo("Thành công", 
                    "Đã copy " + selectedIds.size() + " tasks sang ngày " + 
                    DateUtil.formatDate(targetDate.getValue()));
                
                // Refresh nếu đang xem ngày đích
                if (datePicker.getValue().equals(targetDate.getValue())) {
                    loadTasks();
                }
            }
        } catch (Exception e) {
            AlertUtil.showError("Lỗi", "Không thể copy tasks: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
