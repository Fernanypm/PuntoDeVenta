package com.fernando.puntodeventa;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HelloController {
    @FXML
    private TextField txtIdEmpleado;

    @FXML
    private  PasswordField txtPassword;



    @FXML
    public void btnEntrarClick(ActionEvent event) {
        String usuario = txtIdEmpleado.getText();
        String password = txtPassword.getText();
        //Programacion pantalla de inicio
        String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
        String sql = "SELECT id_job FROM Employees WHERE id_employee = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1,usuario);
            pstm.setString(2,password);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()){
                int id_job = rs.getInt("id_job");

                if (id_job == 1 || id_job == 2 || id_job == 3){
                    Alert alertaExito = new Alert(Alert.AlertType.INFORMATION);
                    alertaExito.setTitle("--Bienvenido--");
                    alertaExito.setHeaderText(null);
                    alertaExito.setContentText("✅ Acceso Autorizado. Entrando al sistema...");
                    alertaExito.showAndWait();
                    try{
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloController.class.getResource("admin-view.fxml"));
                        Scene sceneAdmin = new Scene(fxmlLoader.load(),800,600);
                        Stage ventanaActual = (Stage) txtIdEmpleado.getScene().getWindow();
                        ventanaActual.setTitle("Panel de Administrador - Punto de Venta");
                        ventanaActual.setScene(sceneAdmin);
                        ventanaActual.show();
                    }catch (IOException e ){
                        System.out.println("Error al cargar la pantalla de administrador"+e.getMessage());

                    }

                    System.out.println();
                } else {
                    Alert alertaError = new Alert(Alert.AlertType.ERROR);
                    alertaError.setTitle("--Error de Permiso--");
                    alertaError.setHeaderText(null);
                    alertaError.setContentText("Acceso Denegado: Tu puesto no tiene los permisos necesarios.");
                    alertaError.showAndWait();
                }

            } else {
                Alert alertaError = new Alert(Alert.AlertType.ERROR);
                alertaError.setTitle("Acceso Denegado");
                alertaError.setHeaderText(null);
                alertaError.setContentText("Usuario o contraseña incorrectos. Intenta de nuevo.");
                alertaError.showAndWait();
            }

        }catch (Exception e){
            Alert alertaCritica = new Alert(Alert.AlertType.ERROR);
            alertaCritica.setTitle("Error Fatal");
            alertaCritica.setHeaderText("No se pudo conectar a la base de datos");
            alertaCritica.setContentText(e.getMessage());
            alertaCritica.showAndWait();
        }

    }
}
