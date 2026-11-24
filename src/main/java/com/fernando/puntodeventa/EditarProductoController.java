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

public class EditarProductoController {
    @FXML private TextField id_productoSeleccionado;
    @FXML private TextField nombre_producto_seleccionado;
    @FXML private TextField costo_U_Seleccionado;
    @FXML private TextField precio_U_Seleccionado;
    @FXML private TextField precio_C_Seleccionado;
    @FXML private TextField IVA_Producto_Seleccionado;

    //Parte de Comboxs Categoria y Proveedor
    @FXML
    private  ComboBox<String> cmbProveedor;
    @FXML
    private ComboBox <String> cmbCategoria;
    private Map<String, Integer> mapaProveedores = new HashMap<>();
    private Map<String, Integer> mapaCategorias = new HashMap<>();
    private Catalogo_Comercial productoSeleccionado;

    //
    @FXML
    public void initialize(){
        String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            // 1. Llenar el ComboBox de Proveedores
            String sqlProv = "SELECT id_supplier, name_supplier FROM Supplier";
            ResultSet rsProv = conn.createStatement().executeQuery(sqlProv);
            ObservableList<String> listaProveedores = FXCollections.observableArrayList();

            while (rsProv.next()) {
                mapaProveedores.put(rsProv.getString("name_supplier"), rsProv.getInt("id_supplier"));
                listaProveedores.add(rsProv.getString("name_supplier"));
            }
            cmbProveedor.setItems(listaProveedores);

            // 2. Llenar el ComboBox de Categorías
            String sqlCat = "SELECT id_category, name_category FROM Category";
            ResultSet rsCat = conn.createStatement().executeQuery(sqlCat);
            ObservableList<String> listaCategorias = FXCollections.observableArrayList();

            while (rsCat.next()) {
                mapaCategorias.put(rsCat.getString("name_category"), rsCat.getInt("id_category"));
                listaCategorias.add(rsCat.getString("name_category"));
            }
            cmbCategoria.setItems(listaCategorias);
        }catch (Exception e){
            System.out.println("Error al cargar las listas: " + e.getMessage());
        }
    }

    //
    public void recibirDatosProductos(Catalogo_Comercial producto){
        this.productoSeleccionado = producto;
        id_productoSeleccionado.setText(String.valueOf(producto.getId_producto()));

        nombre_producto_seleccionado.setText(String.valueOf(producto.getNombre_product()));
        costo_U_Seleccionado.setText(String.valueOf(producto.getCosto_u()));
        precio_U_Seleccionado.setText(String.valueOf(producto.getPrecio_u()));
        precio_C_Seleccionado.setText(String.valueOf(producto.getPrecio_c()));
        IVA_Producto_Seleccionado.setText(String.valueOf(producto.getIVA()));
        // A. Buscar y seleccionar el PROVEEDOR actual
        // A. Buscar y seleccionar el PROVEEDOR actual
        for (Map.Entry<String, Integer> entradaProv : mapaProveedores.entrySet()) {
            // ATENCIÓN AQUÍ: Borra "getId_proveedor()", escribe un punto "." después de "producto"
            // y deja que IntelliJ te sugiera el nombre correcto de tu getter.
            if (entradaProv.getValue() == producto.getId_provedor()) {
                cmbProveedor.setValue(entradaProv.getKey());
                break;
            }
        }

        // B. Buscar y seleccionar la CATEGORÍA actual
        for (Map.Entry<String, Integer> entradaCat : mapaCategorias.entrySet()) {
            // Aplica el mismo paso aquí con "getId_categoria()"
            if (entradaCat.getValue() == producto.getId_categoria()) {
                cmbCategoria.setValue(entradaCat.getKey());
                break;
            }
        }
    }
    //Programamos boton actualizar
    @FXML
    private void updateBtn(ActionEvent event){
        String idProductoSelec= id_productoSeleccionado.getText();
        String nombreProductoSelec = nombre_producto_seleccionado.getText();
        String costoUniSelec = costo_U_Seleccionado.getText();
        String precioUniSelec = precio_U_Seleccionado.getText();
        String precioCajaSelec = precio_C_Seleccionado.getText();
        String provSeleccionado = cmbProveedor.getValue();
        String catSeleccionada = cmbCategoria.getValue();
        int idProveedorReal = mapaProveedores.get(provSeleccionado);
        int idCategoriaReal = mapaCategorias.get(catSeleccionada);
        String ivaSelec = IVA_Producto_Seleccionado.getText();

        String sql = "UPDATE Products SET name_product = ?,  id_supplier = ?, id_category = ?, cost_unitary = ?, price_unitary = ?,price_Box =? , percentage_IVA = ? WHERE id_product = ?";
        String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
        try(Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreProductoSelec);
            pstmt.setInt(2,idProveedorReal);
            pstmt.setInt(3,idCategoriaReal);
            pstmt.setString(4, costoUniSelec);
            pstmt.setString(5,precioUniSelec);
            pstmt.setString(6, precioCajaSelec);
            pstmt.setString(7,ivaSelec);
            pstmt.setString(8, idProductoSelec);
            pstmt.executeUpdate();

            Alert alertaExito = new Alert(Alert.AlertType.INFORMATION);
            alertaExito.setTitle("Edición Exitosa");
            alertaExito.setHeaderText(null);
            alertaExito.setContentText("Los datos han sido actualizados correctamente.");
            alertaExito.showAndWait();
            Stage ventanaActual = (Stage) nombre_producto_seleccionado.getScene().getWindow();
            ventanaActual.close();
        }catch (Exception e){
            System.out.println("Error al Actualizar Valores: "+ e.getMessage());
        }
    }
    @FXML
    private void CancelBtn(ActionEvent event){
        Stage stage = (Stage) nombre_producto_seleccionado.getScene().getWindow();
        stage.close();
    }

    //aqui me quede 10/16/2026
}
