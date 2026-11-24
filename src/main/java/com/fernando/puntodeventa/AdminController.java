//Bibliotecas a utilizar/ utilizadas
package com.fernando.puntodeventa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.HashMap;
import java.sql.*;
import java.time.LocalDate;
import java.util.UUID;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import sun.jvm.hotspot.code.Location;
// esto es un comentario aleatorio
// puede generarse esto con spreenboot


//-------------------------------------------------------------------------------------------
//Conjunto donde se muestra la ventana del Administrador
public class AdminController {

    //Apartado Registrar Nuevo Empleado
    @FXML
    private TextField txtNuevoNombre;
    @FXML
    private TextField txtPrimerapellido;
    @FXML
    private TextField txtsegundoapellido;
    @FXML
    private TextField txtNuevoPuesto;
    @FXML
    private DatePicker dpNuevaFecha;

    //Accion a hacer al precionar Boton de Agregar
    @FXML
    private void btnAgregarEmpleado(ActionEvent event){
        System.out.println("---iNICIANDO REGISTRO NUEVO EMPLEADO---");
        String nombre = txtNuevoNombre.getText();
        String appellido1 = txtPrimerapellido.getText();
        String apellido2 = txtsegundoapellido.getText();
        String id_puesto = txtNuevoPuesto.getText();
        LocalDate fecha = dpNuevaFecha.getValue();
        String password = UUID.randomUUID().toString().substring(0,7);
        String sql = "INSERT INTO Employees (id_job, name_employee, first_name, last_name, date_entry, password) VALUES (?, ?, ?, ?, ?, ?)";
        String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, Integer.parseInt(id_puesto)); // Convertimos el texto a número
            pstmt.setString(2, nombre);
            pstmt.setString(3, appellido1);
            pstmt.setString(4, apellido2);
            pstmt.setString(6, password);

            // Verificamos que la fecha no sea nula antes de convertirla a texto
            if (fecha != null) {
                pstmt.setString(5, fecha.toString());
            } else {
                pstmt.setString(5, ""); // O manejar un error si la fecha es obligatoria
            }

            // 3. Ejecutamos el guardado en la base de datos
            pstmt.executeUpdate();

            // 4. Refrescamos la tabla visual para ver al empleado de inmediato
            cargarDatosTabla();

            // 5. Limpiamos las cajas de texto para un nuevo registro
            txtNuevoNombre.clear();
            txtPrimerapellido.clear();
            txtsegundoapellido.clear();
            txtNuevoPuesto.clear();
            dpNuevaFecha.setValue(null);
            password = null;

        } catch (Exception e) {
            System.out.println("Error al guardar en la base de datos: " + e.getMessage());
        }

        String mensajeFinal = "\nDatos del Nuevo Empleado:\n"
                + "Nombre Completo: " + nombre + " " + appellido1 + " " + apellido2 + "\n"
                + "Fecha Ingreso: " + fecha + "\n"
                + "Puesto asignado: " + id_puesto;
        Alert alertaExito = new Alert(Alert.AlertType.INFORMATION);
        alertaExito.setTitle("---NUEVO EMPLEADO AGREGADO---");
        alertaExito.setHeaderText(null);
        alertaExito.setContentText(mensajeFinal);
        alertaExito.showAndWait();

    }




    //-------------------------------------------------------------------------------------------
    //Apartado Buscar Empleado
    @FXML
    private TextField txtDatoBusqueda;
    //Accion que va a hacer al precionar el boton de editar
    @FXML
    private void btnEditarCampos(ActionEvent event){
        Empleado empleadoSeleccionado = tablaEmpleados.getSelectionModel().getSelectedItem();

        if (empleadoSeleccionado == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Selección requerida");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, selecciona un empleado de la tabla para editar sus datos.");
            alerta.showAndWait();
            return;
        }

        // 2. Extraemos los datos ya modificados de las cajas de texto
        String nombreNuevo = txtNuevoNombre.getText();
        String apellido1Nuevo = txtPrimerapellido.getText();
        String apellido2Nuevo = txtsegundoapellido.getText();
        String idPuestoNuevo = txtNuevoPuesto.getText();
        LocalDate fechaNueva = dpNuevaFecha.getValue();

        // 3. Ejecutamos la instrucción SQL UPDATE
        String sql = "UPDATE Employees SET id_job = ?, name_employee = ?, first_name = ?, last_name = ?, date_entry = ? WHERE id_employee = ?";
        String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignamos los datos nuevos
            pstmt.setInt(1, Integer.parseInt(idPuestoNuevo));
            pstmt.setString(2, nombreNuevo);
            pstmt.setString(3, apellido1Nuevo);
            pstmt.setString(4, apellido2Nuevo);
            pstmt.setString(5, (fechaNueva != null) ? fechaNueva.toString() : "");

            // El candado de seguridad: el ID del empleado que tocamos en la tabla
            pstmt.setInt(6, empleadoSeleccionado.getId());

            pstmt.executeUpdate();

            cargarDatosTabla(); // Refrescamos la pantalla al instante

            Alert alertaExito = new Alert(Alert.AlertType.INFORMATION);
            alertaExito.setTitle("Edición Exitosa");
            alertaExito.setHeaderText(null);
            alertaExito.setContentText("Los datos han sido actualizados correctamente.");
            alertaExito.showAndWait();
            txtNuevoNombre.clear();
            txtPrimerapellido.clear();
            txtsegundoapellido.clear();
            txtNuevoPuesto.clear();
            dpNuevaFecha.setValue(null);
            tablaEmpleados.getSelectionModel().clearSelection();

        } catch (Exception e) {
            System.out.println("Error al editar en la base de datos: " + e.getMessage());
        }
    }
    @FXML
    private void btnEliminarEmpleado(ActionEvent event){
        Empleado empleadoSeleccionado = tablaEmpleados.getSelectionModel().getSelectedItem();
        if (empleadoSeleccionado == null){
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Selección requerida");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, selecciona un empleado de la tabla antes de presionar Eliminar.");
            alerta.showAndWait();
            return;
        }
        Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle("Confirmar Baja");
        alertaConfirmacion.setHeaderText("Vas a dar de baja a: " + empleadoSeleccionado.getNombre() + " " + empleadoSeleccionado.getPrimerApellido() + " " + empleadoSeleccionado.getSegundoApellido());
        alertaConfirmacion.setContentText("¿Estás seguro? El registro no se borrará, pero su estado cambiará a 'Inactivo'.");
        if (alertaConfirmacion.showAndWait().get() == ButtonType.OK){
            String sql = "UPDATE Employees SET availability = 'Inactivo' WHERE id_employee = ?";
            String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1,empleadoSeleccionado.getId());
                pstmt.executeUpdate();

                cargarDatosTabla();
                Alert alertaExito = new Alert(Alert.AlertType.INFORMATION);
                alertaExito.setTitle("Baja exitosa");
                alertaExito.setHeaderText(null);
                alertaExito.setContentText("El empleado ha sido marcado como Inactivo.");
                alertaExito.showAndWait();
            }catch (Exception e){
                System.out.println("Error al dar de baja al empleado: " + e.getMessage());
            }
        }
    }

    //Accion a hacer al precionar el boton de Buscar
    @FXML
    private void btnBuscarEmpleado(ActionEvent event){
        String dato_busqueda = txtDatoBusqueda.getText();
        if (dato_busqueda.isEmpty()){
            cargarDatosTabla();
        }
        ObservableList<Empleado> listaFiltrada = FXCollections.observableArrayList();
        String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
        String sql = "SELECT * FROM Employees WHERE id_employee = ? OR name_employee LIKE ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try {
                pstmt.setInt(1,Integer.parseInt(dato_busqueda));
            }catch (NumberFormatException e){
                pstmt.setInt(1,-1);
            }
            pstmt.setString(2,"%"+ dato_busqueda + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id_employee");
                int idPuesto = rs.getInt("id_job");
                String nombre = rs.getString("name_employee");
                String ap1 = rs.getString("first_name");
                String ap2 = rs.getString("last_name");
                String fecha = rs.getString("date_entry");
                String contraseña = rs.getString("password");
                String disponibilidad = rs.getString("availability");
                Empleado empleado = new Empleado(id, idPuesto, nombre, ap1, ap2, fecha, contraseña, disponibilidad);
                listaFiltrada.add(empleado);
            }
            tablaEmpleados.setItems(listaFiltrada);
            if (listaFiltrada.isEmpty()) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Búsqueda sin resultados");
                alerta.setHeaderText(null);
                alerta.setContentText("No se encontró ningún empleado con ese ID o Nombre.");
                alerta.showAndWait();
            }

        }catch (Exception e){
            System.out.println("Error al buscar empleado: " + e.getMessage());
        }
    }
    //--------------------------------------------------------------------------------------------------------------
    //Tabla General de Busqueda en Catalogo Comercial
    @FXML
    private TableView<Catalogo_Comercial> TablaCatalogo;
    @FXML
    private TableColumn<Catalogo_Comercial,Integer> col_id_producto;
    @FXML
    private TableColumn<Catalogo_Comercial,String> col_producto;
    @FXML
    private TableColumn<Catalogo_Comercial,Integer> col_id_provedor_tablaproducto;
    @FXML
    private TableColumn<Catalogo_Comercial,Double> col_id_categoria_tablaproducto;
    @FXML
    private TableColumn<Catalogo_Comercial, Double> col_costo_u;
    @FXML
    private TableColumn<Catalogo_Comercial,Double> col_precio_u;
    @FXML
    private TableColumn<Catalogo_Comercial, Double> col_precio_c;
    @FXML
    private TableColumn<Catalogo_Comercial, Double> col_iva;

    private void CargarDatosDeTablaCatalogo(){
        ObservableList<Catalogo_Comercial> listaCatalogo = FXCollections.observableArrayList();
        String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
        String sql = "SELECT * FROM Products";
        try(Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                int id_producto = rs.getInt("id_product");
                String nombre_producto = rs.getString("name_product");
                int id_proveedor = rs.getInt("id_supplier");
                int id_categoria = rs.getInt("id_category");
                double costo_uni = rs.getDouble("cost_unitary");
                double precio_uni = rs.getDouble("price_unitary");
                double precio_caja = rs.getDouble("price_box");
                double IVA = rs.getDouble("percentage_IVA");
                Catalogo_Comercial Catalogo_Comercial = new Catalogo_Comercial(id_producto,nombre_producto,id_proveedor,id_categoria,costo_uni,precio_uni,precio_caja,IVA);
                listaCatalogo.add(Catalogo_Comercial);
            }
            TablaCatalogo.setItems(listaCatalogo);

        }catch (Exception e){
            System.out.println("Error de Carga de Datos de Productos : "+e);
        }
    }




    //--------------------------------------------------------------------------------------------------------------
    //Tabla Categorias
    @FXML
    private TableView<Categorias> tablaCategoria;
    @FXML
    private TableColumn<Categorias, Integer> col_id_categoria;
    @FXML
    private TableColumn<Categorias, String> col_Categoria;
    private void CargarDatosDeTablaCategoria(){
        ObservableList<Categorias> listaCategorias = FXCollections.observableArrayList();
        String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
        String sql = "SELECT * FROM Category";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                int id_categoria = rs.getInt("id_category");
                String nombre_categoria = rs.getString("name_category");
                Categorias categorias = new Categorias(id_categoria,nombre_categoria);
                listaCategorias.add(categorias);
            }
            tablaCategoria.setItems(listaCategorias);

        }catch (Exception e){
            System.out.println("Error de Carga de Datos de Categoria : "+ e);
        }
    }

    //-----------------------------------------------------------------------------------------------
    //Tabla Provedor
    @FXML
    private TableView<Provedor> tablaProvedores;
    @FXML
    private TableColumn<Provedor, Integer> col_id_provedor;
    @FXML
    private TableColumn<Provedor, String> col_Nombre_Provedor;

    @FXML
    private TableColumn<Provedor, Integer> col_Contacto;

    private void CargarDatosDeTablaProveedores(){
        ObservableList<Provedor> listaProvedores = FXCollections.observableArrayList();
        String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
        String sql = "SELECT * FROM Supplier";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            while (rs.next()){
                int id_provedor = rs.getInt("id_supplier");
                String Nombre_proveedor = rs.getString("name_supplier");
                int contacto = rs.getInt(("phone_number"));
                Provedor proveedor = new Provedor(id_provedor,Nombre_proveedor,contacto);
                listaProvedores.add(proveedor);
            }
            tablaProvedores.setItems(listaProvedores);

        }catch (Exception e){
            System.out.println("Error de Carga de Datos de Proveedor: "+ e);
        }
    }



    //-------------------------------------------------------------------------------------------
    // Tabla General de Visualizacion (Tabla Emplea>dos)
    @FXML
    private TableView<Empleado> tablaEmpleados; // La tabla gigante
    @FXML
    private TableColumn<Empleado, Integer> colId;
    @FXML
    private TableColumn<Empleado, Integer> colIdPuesto;
    @FXML
    private TableColumn<Empleado, String> colNombre;
    @FXML
    private TableColumn<Empleado, String> colPrimerApellido;
    @FXML
    private TableColumn<Empleado, String> colSegundoApellido;
    @FXML
    private TableColumn<Empleado, String> colFecha;
    @FXML
    private TableColumn<Empleado, String> colContraseña;
    @FXML
    private TableColumn<Empleado, String> colDisponibilidadEmpleado;

    private void cargarDatosTabla() {
        // Esta lista especial actualizará la tabla automáticamente
        ObservableList<Empleado> listaEmpleados = FXCollections.observableArrayList();

        String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";

        String sql = "SELECT * FROM Employees";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Sacamos los datos de la base de datos (asegúrate que los nombres en rs.get() coincidan con las columnas de tu SQLite)
                int id = rs.getInt("id_employee");
                int idPuesto = rs.getInt("id_job");
                String nombre = rs.getString("name_employee");
                String ap1 = rs.getString("first_name");
                String ap2 = rs.getString("last_name");
                String fecha = rs.getString("date_entry");
                String contraseña = rs.getString("password");
                String disponibilidad = rs.getString("availability");


                // Metemos los datos al Molde y lo agregamos a la lista
                Empleado empleado = new Empleado(id, idPuesto, nombre, ap1, ap2, fecha, contraseña, disponibilidad);
                listaEmpleados.add(empleado);
            }

            // Finalmente, le pasamos la lista llena a nuestra tabla visual
            tablaEmpleados.setItems(listaEmpleados);

        } catch (Exception e) {
            System.out.println("Error al cargar la tabla: " + e.getMessage());
        }
    }

    //-------------------------------------------------------------------------------------------
    //Table of Jobs
    @FXML
    private TableView<Trabajos> tablaTrabajos;
    @FXML
    private TableColumn<Trabajos, String> IDPuesto;
    @FXML
    private TableColumn<Trabajos, String> colPuestoTabla;
    public void cargar_empleos(){
        ObservableList<Trabajos> listaJobs =FXCollections.observableArrayList();
        String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
        String sql = "SELECT id_job,name_job FROM Jobs";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()){
                int id_job = rs.getInt("id_job");
                String name_job = rs.getString("name_job");
                Trabajos trabajos = new Trabajos(id_job,name_job);
                listaJobs.add(trabajos);
            }
            tablaTrabajos.setItems(listaJobs);

        }catch (Exception e){
            System.out.println("Error al cargar la tabla de trabajos: " + e.getMessage());
        }
    }


    //-----------------------------------------------------------------------------------------
    //Funcion de acciones al mostrar la primera pantalla el Main
    @FXML
    public void initialize() {

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        colIdPuesto.setCellValueFactory(new PropertyValueFactory<>("idPuesto"));

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        colPrimerApellido.setCellValueFactory(new PropertyValueFactory<>("primerApellido"));

        colSegundoApellido.setCellValueFactory(new PropertyValueFactory<>("segundoApellido"));

        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        colContraseña.setCellValueFactory(new PropertyValueFactory<>("contraseña"));
        colDisponibilidadEmpleado.setCellValueFactory(new PropertyValueFactory<>("disponibilidad"));

        IDPuesto.setCellValueFactory(new PropertyValueFactory<>("id_job"));
        colPuestoTabla.setCellValueFactory(new PropertyValueFactory<>("name_job"));


        tablaEmpleados.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Autocompleta las cajas de texto con los datos de la fila tocada
                txtNuevoNombre.setText(newValue.getNombre());
                txtPrimerapellido.setText(newValue.getPrimerApellido());
                txtsegundoapellido.setText(newValue.getSegundoApellido());
                txtNuevoPuesto.setText(String.valueOf(newValue.getIdPuesto()));


                if (newValue.getFecha() != null && !newValue.getFecha().isEmpty()) {
                    dpNuevaFecha.setValue(LocalDate.parse(newValue.getFecha()));
                } else {
                    dpNuevaFecha.setValue(null);
                }
            }
        });
        col_id_provedor.setCellValueFactory(new PropertyValueFactory<>("id_proveedor"));
        col_Nombre_Provedor.setCellValueFactory(new PropertyValueFactory<>("nombre_provedor"));
        col_Contacto.setCellValueFactory(new PropertyValueFactory<>("contacto"));

        col_id_categoria.setCellValueFactory(new PropertyValueFactory<>("id_category"));
        col_Categoria.setCellValueFactory(new  PropertyValueFactory<>("name_category"));

        col_id_producto.setCellValueFactory(new PropertyValueFactory<>("id_producto"));
        col_producto.setCellValueFactory(new PropertyValueFactory<>("nombre_product"));
        col_id_provedor_tablaproducto.setCellValueFactory(new PropertyValueFactory<>("id_provedor"));
        col_id_categoria_tablaproducto.setCellValueFactory(new PropertyValueFactory<>("id_categoria"));
        col_costo_u.setCellValueFactory(new PropertyValueFactory<>("costo_u"));
        col_precio_u.setCellValueFactory(new PropertyValueFactory<>("precio_u"));
        col_precio_c.setCellValueFactory(new PropertyValueFactory<>("precio_c"));
        col_iva.setCellValueFactory(new PropertyValueFactory<>("IVA"));

        cargarDatosTabla();
        cargar_empleos();
        CargarDatosDeTablaProveedores();
        CargarDatosDeTablaCategoria();
        CargarDatosDeTablaCatalogo();
    }


    // -------------------------------------------------------------------------------
    //Apartado Busqueda en Catalogo Comercial
    @FXML
    private TextField txtDato_de_Busqueda;

    @FXML
    private CheckBox chkProveedores;

    @FXML
    private CheckBox chkProductos;

    @FXML
    private CheckBox chkCategorias;

    @FXML
    private CheckBox chkTodos;

    @FXML
    private void btn_busqueda(ActionEvent event){

        String dato = txtDato_de_Busqueda.getText().trim();
        if (dato.isEmpty()) {
            System.out.println("El campo de búsqueda está vacío.");
            return;
        }


        if (chkProductos.isSelected()) {
            ObservableList<Catalogo_Comercial> listaFiltrada = FXCollections.observableArrayList();
            String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
            String sql = "SELECT * FROM Products WHERE id_product = ? OR name_product LIKE ?";

            System.out.println("Buscando '" + dato + "' en la tabla de Productos...");
            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                try {
                    pstmt.setInt(1,Integer.parseInt(dato));
                }catch (NumberFormatException e){
                    pstmt.setInt(1,-1);
                }
                pstmt.setString(2,"%"+ dato + "%");
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()){
                    int id = rs.getInt("id_product");
                    String nombre_producto = rs.getString("name_product");
                    int id_proveedor = rs.getInt("id_supplier");
                    int id_categoria = rs.getInt("id_category");
                    double costo_uni =rs.getDouble("cost_unitary");
                    double precio_uni = rs.getDouble("price_unitary");
                    double precio_caja = rs.getDouble("price_box");
                    double Iva = rs.getDouble("percentage_IVA");

                    Catalogo_Comercial catalogoComercial = new Catalogo_Comercial(id, nombre_producto, id_proveedor, id_categoria, costo_uni, precio_uni, precio_caja, Iva);
                    listaFiltrada.add(catalogoComercial);
                }
                TablaCatalogo.setItems(listaFiltrada);
                if (listaFiltrada.isEmpty()) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setTitle("Búsqueda sin resultados");
                    alerta.setHeaderText(null);
                    alerta.setContentText("No se encontró ningún Producto con ese ID o Nombre.");
                    alerta.showAndWait();
                }
        }catch (Exception e){
                System.out.println("Error al buscar Producto: " + e.getMessage());
            }

            }
        if (chkProveedores.isSelected()) {
            ObservableList<Provedor> listaFiltrada = FXCollections.observableArrayList();
            String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
            String sql = "SELECT * FROM Supplier WHERE id_supplier = ? OR name_supplier LIKE ? OR phone_number LIKE ?";

            System.out.println("Buscando '" + dato + "' en la tabla de Proveedores...");
            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                try {
                    pstmt.setInt(1,Integer.parseInt(dato));
                    pstmt.setInt(3,Integer.parseInt(dato));
                }catch (NumberFormatException e){
                    pstmt.setInt(1,-1);
                }
                pstmt.setString(2,"%"+ dato + "%");

                ResultSet rs = pstmt.executeQuery();
                while (rs.next()){
                    int id = rs.getInt("id_supplier");
                    String nombre_provedor = rs.getString("name_supplier");
                    int contacto = rs.getInt("phone_number");
                    Provedor proveedor = new Provedor(id, nombre_provedor, contacto);
                    listaFiltrada.add(proveedor);
                }
                tablaProvedores.setItems(listaFiltrada);
                if (listaFiltrada.isEmpty()) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setTitle("Búsqueda sin resultados");
                    alerta.setHeaderText(null);
                    alerta.setContentText("No se encontró ningún Proveedor con ese ID o Nombre.");
                    alerta.showAndWait();
                }
            }catch (Exception e){
                System.out.println("Error al buscar Categoria: " + e.getMessage());
            }
            System.out.println("Buscando '" + dato + "' en la tabla de Proveedores...");



        }
        if (chkCategorias.isSelected()) {
            ObservableList<Categorias> listaFiltrada = FXCollections.observableArrayList();
            String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
            String sql = "SELECT * FROM Category WHERE id_category = ? OR name_category LIKE ?";

            System.out.println("Buscando '" + dato + "' en la tabla de Proveedores...");
            try (Connection conn = DriverManager.getConnection(url);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                try {
                    pstmt.setInt(1,Integer.parseInt(dato));
                }catch (NumberFormatException e){
                    pstmt.setInt(1,-1);
                }
                pstmt.setString(2,"%"+ dato + "%");
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()){
                    int id = rs.getInt("id_category");
                    String nombre_categoria = rs.getString("name_category");

                    Categorias categorias = new Categorias(id, nombre_categoria);
                    listaFiltrada.add(categorias);
                }
                tablaCategoria.setItems(listaFiltrada);
                if (listaFiltrada.isEmpty()) {
                    Alert alerta = new Alert(Alert.AlertType.WARNING);
                    alerta.setTitle("Búsqueda sin resultados");
                    alerta.setHeaderText(null);
                    alerta.setContentText("No se encontró ningún Proveedor con ese ID o Nombre.");
                    alerta.showAndWait();
                }
            }catch (Exception e){
                System.out.println("Error al buscar Categoria: " + e.getMessage());
            }
            System.out.println("Buscando '" + dato + "' en la tabla de Proveedores...");

        }

    }
    //Botton clean
    @FXML
    private void btn_limpiar(ActionEvent event){
        txtDato_de_Busqueda.clear();
        tablaProvedores.getSelectionModel().clearSelection();
        tablaCategoria.getSelectionModel().clearSelection();
        TablaCatalogo.getSelectionModel().clearSelection();
        CargarDatosDeTablaCategoria();
        CargarDatosDeTablaCatalogo();
        CargarDatosDeTablaProveedores();
    }

    @FXML
    private void accionSeleccionarTodos(ActionEvent event) {
        // Leemos si el usuario acaba de prender o apagar la casilla "Todos"
        boolean activado = chkTodos.isSelected();

        // Le pasamos ese mismo estado a las otras tres casillas
        chkProveedores.setSelected(activado);
        chkProductos.setSelected(activado);
        chkCategorias.setSelected(activado);
    }
    //boton editar en Busqueda ebn Catalogo

    @FXML
    private void btnEditar_Catalogo_Comercial(ActionEvent event){

        //Editar Proveedor
        Provedor provedorSeleccionado = tablaProvedores.getSelectionModel().getSelectedItem();
        Categorias categoriaSeleccionada = tablaCategoria.getSelectionModel().getSelectedItem();

        Catalogo_Comercial productoSeleccionado = TablaCatalogo.getSelectionModel().getSelectedItem();

        if (provedorSeleccionado == null &&  productoSeleccionado == null && categoriaSeleccionada == null){
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Selección requerida");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor, selecciona un campo de la tablas para editar sus datos.");
            alerta.showAndWait();
            return;
        }
        //editar Proveedor
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Editar_Proveedor.fxml"));
            Parent root= loader.load();
            EditarProveedorController controlador = loader.getController();
            controlador.recibirDatos(provedorSeleccionado);
            Stage ventana_edicion = new Stage();
            ventana_edicion.setTitle("Editar Proveedor");
            ventana_edicion.setScene(new Scene(root));
            ventana_edicion.initModality(Modality.APPLICATION_MODAL);
            ventana_edicion.showAndWait();

            CargarDatosDeTablaProveedores();
            tablaProvedores.getSelectionModel().clearSelection();
        }catch (Exception e){
            System.out.println("Error al Abrir ventana modal de Edicion de Proveedor: "+e.getMessage()); e.printStackTrace();

        }
        //Fin de Edicion  de Proveedor
        //Editar Producto

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Editar_Producto.fxml"));
            Parent root= loader.load();
            EditarProductoController controladorProduct = loader.getController();
            controladorProduct.recibirDatosProductos(productoSeleccionado);
            Stage ventana_edicion = new Stage();
            ventana_edicion.setTitle("Editar Producto");
            ventana_edicion.setScene(new Scene(root));
            ventana_edicion.initModality(Modality.APPLICATION_MODAL);
            ventana_edicion.showAndWait();
            CargarDatosDeTablaCatalogo();
        }catch (Exception e){
            System.out.println("Error al Abrir ventana modal de Edicion de Producto: "+e.getMessage()); e.printStackTrace();
        }
        //Editar Categoria
        try{
            FXMLLoader loader =  new FXMLLoader(getClass().getResource("Editar_Categoria.fxml"));
            Parent root = loader.load();
            EditarCategoriaController controladorCategoria = loader.getController();
            controladorCategoria.recibirDatosCategorias(categoriaSeleccionada);
            Stage ventana_edicion =  new Stage();
            ventana_edicion.setTitle("Editar Producto");
            ventana_edicion.setScene(new Scene(root));
            ventana_edicion.initModality(Modality.APPLICATION_MODAL);
            ventana_edicion.showAndWait();
            CargarDatosDeTablaCategoria();
            tablaCategoria.getSelectionModel().clearSelection();
        }catch (Exception e){
            System.out.println("Error al editar ena l a base de de datos Categoria"+e.getMessage());
        }
    }

    //-----------------------------------------------------------------------------------------
    //Pestaña Editar Catologo:
    @FXML
    private TextField add_nombre_proveedor;
    @FXML
    private TextField add_numero_proveedor;
    @FXML
    private void btnAdd(ActionEvent event){
    //Add Proveedor
        String addProveedor = add_nombre_proveedor.getText();
        String addContacto = add_numero_proveedor.getText();
        if (addProveedor == null || addProveedor.trim().isEmpty() || addContacto == null || addContacto.trim().isEmpty()){
            Alert alertaVacio = new Alert(Alert.AlertType.WARNING);
            alertaVacio.setTitle("Campos Vacíos");
            alertaVacio.setHeaderText(null);
            alertaVacio.setContentText("Por favor, llena el nombre y el número del proveedor.");
            alertaVacio.showAndWait();
            return;
        }

        String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
        String sql = "INSERT INTO Supplier (name_supplier, phone_number) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(url); PreparedStatement pstmt = connection.prepareStatement(sql)){
            try {
                pstmt.setString(1, addProveedor);
                pstmt.setString(2, addContacto);
                pstmt.executeUpdate();

                Alert alertaExito = new Alert(Alert.AlertType.INFORMATION);
                alertaExito.setTitle("Registro Exitoso");
                alertaExito.setHeaderText(null);
                alertaExito.setContentText("El proveedor se ha agregado correctamente al catálogo.");
                alertaExito.showAndWait();
                CargarDatosDeTablaProveedores();
                add_nombre_proveedor.clear();
                add_numero_proveedor.clear();

            }catch (Exception e ){
                System.out.printf("Error al añadir Proveedor (error de guardado) "+e.getMessage());
            }
            ResultSet rs = pstmt.executeQuery();


        }catch (Exception e){
            System.out.printf("Error al Conectar Base de Datos: "+ e.getMessage());
        }
    }
    //add categoria
    @FXML
    private TextField add_nombre_categoria;
    @FXML
    private void btn_add_categoria(ActionEvent event){
        String addCategoria = add_nombre_categoria.getText();

        if (addCategoria == null || addCategoria.trim().isEmpty() ){
            Alert alertaVacio = new Alert(Alert.AlertType.WARNING);
            alertaVacio.setTitle("Campos Vacíos");
            alertaVacio.setHeaderText(null);
            alertaVacio.setContentText("Por favor, llena el nombre y el número del proveedor.");
            alertaVacio.showAndWait();
            return;
        }
        String url = "jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
        String sql = "INSERT INTO Category (name_category) VALUES (?)";
        try(Connection connection = DriverManager.getConnection(url); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            try {
                pstmt.setString(1,addCategoria);
                pstmt.executeUpdate();
                Alert alertaExito = new Alert(Alert.AlertType.INFORMATION);
                alertaExito.setTitle("Registro Exitoso");
                alertaExito.setHeaderText(null);
                alertaExito.setContentText("La Categoria se ha agregado correctamente");
                alertaExito.showAndWait();
                CargarDatosDeTablaCategoria();
                add_nombre_proveedor.clear();
                add_nombre_categoria.clear();
            }catch (Exception e){
                System.out.println("Error al añadir Categoria (error de guardado)");
            }
            ResultSet rs = pstmt.executeQuery();
        }catch (Exception e){
            System.out.printf("Error al Conectar Base de Datos: "+ e.getMessage());
        }
    }
    //add producto
    @FXML
    private TextField add_nombre_producto;
    @FXML
    private ComboBox<String> combo_box_proveedor;
    @FXML
    private ComboBox<String> combo_box_categoria;
    @FXML
    private TextField add_costo_producto;
    @FXML
    private TextField add_precio_uni_producto;
    @FXML
    private TextField add_precio_caja_producto;
    // llenamos comboBox
    private HashMap<String,Integer> mapaProveedores =  new HashMap<>();
    private HashMap<String,Integer> mapaCategorias = new HashMap<>();
    //cargamos ComboBoxes
    public void actualizarComboBox(){
        combo_box_proveedor.getItems().clear();
        combo_box_categoria.getItems().clear();
        mapaProveedores.clear();
        mapaCategorias.clear();
        String url ="jdbc:sqlite:C:\\Users\\ferna\\Downloads\\Programacion\\Proyects\\SQL Punto de venta\\punto_venta_lleno.db";
        //Cargamos proveedores
        try (Connection conn = DriverManager.getConnection(url)){
            String sqlProv = "SELECT id_supplier, name_supplier FROM Supplier";
            try (PreparedStatement pstmtProv = conn.prepareStatement(sqlProv);ResultSet rsProv = pstmtProv.executeQuery()){
                while (rsProv.next()){
                    int id = rsProv.getInt("id_supplier");
                    String nombre = rsProv.getString("name_supplier");
                    mapaProveedores.put(nombre,id);//guardamos lo extraido
                    combo_box_categoria.getItems().add(nombre); //solo mostramos el texto
                }
            }catch (Exception e){
                System.out.println("Error de llenado de ComboBox de Proveedor"+e.getMessage());
            }
            //aqui me quede
        }catch (Exception e){

        }
    }
}
