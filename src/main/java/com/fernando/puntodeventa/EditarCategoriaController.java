package com.fernando.puntodeventa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class EditarCategoriaController {
    @FXML private TextField id_categoriaSeleccionado;
    @FXML private TextField nombre_CategoriaSeleccionado;

    private Categorias categorias;

    public void recibirDatosCategorias(Categorias categorias){
        this.categorias = categorias;
        id_categoriaSeleccionado.setText(String.valueOf(categorias.getId_category()));
        nombre_CategoriaSeleccionado.setText(String.valueOf(categorias.getName_category()));

    }
    //parte de updateBtn
    @FXML
    private void updateBtn(ActionEvent event){

        String id_categoria = id_categoriaSeleccionado.getText();
        String nuevo_nombre_categoria = nombre_CategoriaSeleccionado.getText();

        //INSTRUCCION A SQL
        String sql = "UPDATE Category SET name_category = ? WHERE id_category = ?";
        String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //Asignamos los datos en el codigo sql

            pstmt.setString(1,nuevo_nombre_categoria);
           pstmt.setString(2,id_categoria);
            pstmt.executeUpdate();
            Alert alertaExito = new Alert(Alert.AlertType.INFORMATION);
            alertaExito.setTitle("Edición Exitosa");
            alertaExito.setHeaderText(null);
            alertaExito.setContentText("Los datos han sido actualizados correctamente.");
            alertaExito.showAndWait();
            Stage ventanaActual = (Stage) nombre_CategoriaSeleccionado.getScene().getWindow();
            ventanaActual.close();
        }catch (Exception e){
            System.out.println("Error al editar en la base de datos Proveedor: " + e.getMessage());
        }
    }
    @FXML
    private void CancelBtn(ActionEvent event){
        Stage stage = (Stage) nombre_CategoriaSeleccionado.getScene().getWindow();
        stage.close();

    }

}
