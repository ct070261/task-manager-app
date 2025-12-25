package com.taskmanager;

import com.taskmanager.dao.DatabaseConnection;
import com.taskmanager.util.AlertUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class - Entry point của ứng dụng Task Manager
 * Khởi động JavaFX application và load giao diện chính
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Test database connection
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();
            if (!dbConnection.testConnection()) {
                AlertUtil.showError("Lỗi kết nối Database", 
                    "Không thể kết nối đến MySQL database.\n\n" +
                    "Vui lòng kiểm tra:\n" +
                    "1. MySQL server đã được khởi động\n" +
                    "2. Database 'task_manager_db' đã được tạo\n" +
                    "3. Thông tin kết nối trong application.properties đúng\n\n" +
                    "Xem README.md để biết hướng dẫn chi tiết.");
                System.exit(1);
            }
            
            // Load main FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            Parent root = loader.load();
            
            // Create scene
            Scene scene = new Scene(root);
            
            // Setup stage
            primaryStage.setTitle("Task Manager - Quản lý công việc hàng ngày");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(false);
            primaryStage.setMinWidth(1000);
            primaryStage.setMinHeight(600);
            
            // Show stage
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Lỗi khởi động ứng dụng", 
                "Không thể khởi động ứng dụng: " + e.getMessage());
            System.exit(1);
        }
    }
    
    @Override
    public void stop() {
        // Đóng database connection khi thoát ứng dụng
        DatabaseConnection.getInstance().closeConnection();
        System.out.println("Ứng dụng đã đóng");
    }
    
    /**
     * Main method - entry point
     */
    public static void main(String[] args) {
        launch(args);
    }
}
