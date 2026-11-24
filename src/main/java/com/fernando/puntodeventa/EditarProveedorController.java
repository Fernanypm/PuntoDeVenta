package com.fernando.puntodeventa;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class EditarProveedorController {
    @FXML private TextField id_proveedorSeleccionado;
    @FXML private TextField nombre_proveedor_seleccionado;
    @FXML private TextField contacto_proveedor_seleccionado;

    private Provedor proveedorSeleccionado;

    public void recibirDatos(Provedor provedor){
        this.proveedorSeleccionado=provedor;
        id_proveedorSeleccionado.setText(String.valueOf(provedor.getId_proveedor()));
        nombre_proveedor_seleccionado.setText(String.valueOf(provedor.getNombre_provedor()));
        contacto_proveedor_seleccionado.setText(String.valueOf(provedor.getContacto()));
    }
    //aqui programa e. boton de actulizar y de cancelar
    @FXML
    private void updateBtn(ActionEvent event){
        String nombreNuevo = nombre_proveedor_seleccionado.getText();
        String numeroContacto =  contacto_proveedor_seleccionado.getText();
        String idProveedorSelec = id_proveedorSeleccionado.getText();

        //Instrucciones SQL
        String sql = "UPDATE Supplier SET  name_supplier = ?, phone_number= ? WHERE id_supplier = ?";

        String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //Asignamos los datos en el codigo sql

            pstmt.setString(1,nombreNuevo);
            pstmt.setString(2,numeroContacto);
            pstmt.setString(3,idProveedorSelec);
            pstmt.executeUpdate();
            Alert alertaExito = new Alert(Alert.AlertType.INFORMATION);
            alertaExito.setTitle("Edición Exitosa");
            alertaExito.setHeaderText(null);
            alertaExito.setContentText("Los datos han sido actualizados correctamente.");
            alertaExito.showAndWait();
            Stage ventanaActual = (Stage) nombre_proveedor_seleccionado.getScene().getWindow();
            ventanaActual.close();

        }catch (Exception e){
            System.out.println("Error al editar en la base de datos Proveedor: " + e.getMessage());
        }
    }
    @FXML
    private void CancelBtn(ActionEvent event){
        Stage stage = (Stage) nombre_proveedor_seleccionado.getScene().getWindow();
        stage.close();
        
    }
}
