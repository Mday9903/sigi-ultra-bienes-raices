package controlador;

import modelo.ConexionBD;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class GestorSistema {

    private int idClienteSeleccionadoReserva = -1;

    public GestorSistema() {
    }

    // =================================================================
    // MÉTODOS DE GESTIÓN DE CLIENTES
    // =================================================================
    public void cargarClientesDesdeBD(DefaultTableModel modeloTabla) throws Exception {
        modeloTabla.setRowCount(0);
        String sql = "SELECT id_cliente, dni_cliente, apellido, nombre, telefono FROM cliente";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{rs.getInt("id_cliente"), rs.getString("dni_cliente"), rs.getString("apellido"), rs.getString("nombre"), rs.getString("telefono")});
            }
        }
    }

    public void registrarClienteBD(String dni, String nombre, String apellido, String tel, String email) throws Exception {
        String sql = "INSERT INTO cliente (dni_cliente, nombre, apellido, telefono, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, dni); pst.setString(2, nombre); pst.setString(3, apellido); pst.setString(4, tel); pst.setString(5, email);
            pst.executeUpdate();
        }
    }

    // =================================================================
    // MÉTODOS DE GESTIÓN DE INMUEBLES
    // =================================================================
    public void cargarTodosInmueblesDesdeBD(DefaultTableModel modeloTabla) throws Exception {
        modeloTabla.setRowCount(0);
        String sql = "SELECT id_inmueble, direccion, tipo_inmueble, tipo_operacion, precio, estado FROM inmueble";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{rs.getInt("id_inmueble"), rs.getString("direccion"), rs.getString("tipo_inmueble"), rs.getString("tipo_operacion"), "$" + rs.getDouble("precio"), rs.getString("estado")});
            }
        }
    }

    public void registrarInmuebleBD(String desc, String dir, double sup, String tipo, String op, double precio) throws Exception {
        String sql = "INSERT INTO inmueble (descripcion, direccion, superficie, tipo_inmueble, tipo_operacion, precio, estado, id_usuario) VALUES (?, ?, ?, ?, ?, ?, 'Disponible', 1)";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, desc); pst.setString(2, dir); pst.setDouble(3, sup); pst.setString(4, tipo); pst.setString(5, op); pst.setDouble(6, precio);
            pst.executeUpdate();
        }
    }

    public void cargarInmueblesDisponibles(DefaultTableModel modeloTabla) throws Exception {
        modeloTabla.setRowCount(0);
        String sql = "SELECT id_inmueble, direccion, tipo_inmueble, tipo_operacion, precio FROM inmueble WHERE estado = 'Disponible'";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{rs.getInt("id_inmueble"), rs.getString("direccion"), rs.getString("tipo_inmueble"), rs.getString("tipo_operacion"), rs.getDouble("precio")});
            }
        }
    }

    // =================================================================
    // MÉTODOS DE RESERVAS (CU09)
    // =================================================================
    public String buscarClientePorDni(String dni) throws Exception {
        String sql = "SELECT id_cliente, nombre, apellido FROM cliente WHERE dni_cliente = ?";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, dni);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    idClienteSeleccionadoReserva = rs.getInt("id_cliente");
                    return rs.getString("apellido") + ", " + rs.getString("nombre") + " (DNI: " + dni + ")";
                } else {
                    idClienteSeleccionadoReserva = -1;
                    return "Cliente no encontrado.";
                }
            }
        }
    }

    public int getIdClienteSeleccionadoReserva() {
        return idClienteSeleccionadoReserva;
    }

    public void resetClienteSeleccionado() {
        idClienteSeleccionadoReserva = -1;
    }

    public void registrarReservaBD(int idInmueble, String tipo, double monto, java.sql.Date fechaVenc) throws Exception {
        if (idClienteSeleccionadoReserva == -1) {
            throw new Exception("Debe buscar y seleccionar un cliente válido.");
        }
        Connection con = ConexionBD.conectar();
        if (con == null) throw new Exception("Error de conexión a la base de datos.");
        try {
            con.setAutoCommit(false); // Iniciar transacción
            String sqlInsert = "INSERT INTO reserva (tipo_reserva, fecha, fecha_vencimiento, monto, estado, id_inmueble, id_cliente, id_usuario) VALUES (?, CURDATE(), ?, ?, 'Vigente', ?, ?, 1)";
            try (PreparedStatement pstIns = con.prepareStatement(sqlInsert)) {
                pstIns.setString(1, tipo); pstIns.setDate(2, fechaVenc); pstIns.setDouble(3, monto); pstIns.setInt(4, idInmueble); pstIns.setInt(5, idClienteSeleccionadoReserva);
                pstIns.executeUpdate();
            }
            String sqlUpdate = "UPDATE inmueble SET estado = 'Reservado' WHERE id_inmueble = ?";
            try (PreparedStatement pstUpd = con.prepareStatement(sqlUpdate)) {
                pstUpd.setInt(1, idInmueble); pstUpd.executeUpdate();
            }
            con.commit(); // Confirmar
        } catch (Exception e) {
            con.rollback(); throw e;
        } finally { con.setAutoCommit(true); con.close(); }
    }

    // =================================================================
    // MÉTODOS DE CONTRATOS Y REPORTES (CU10 y CU11)
    // =================================================================
    public void cargarReservasVigentesBD(DefaultTableModel modeloTabla) throws Exception {
        modeloTabla.setRowCount(0);
        String sql = "SELECT r.id_reserva, i.direccion, CONCAT(c.apellido, ', ', c.nombre) AS cliente, r.fecha_vencimiento " +
                "FROM reserva r INNER JOIN inmueble i ON r.id_inmueble = i.id_inmueble " +
                "INNER JOIN cliente c ON r.id_cliente = c.id_cliente WHERE r.estado = 'Vigente'";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{rs.getInt("id_reserva"), rs.getString("direccion"), rs.getString("cliente"), rs.getDate("fecha_vencimiento")});
            }
        }
    }

    public int validarDniYObtenerId(String dni) throws Exception {
        String sql = "SELECT id_cliente FROM cliente WHERE dni_cliente = ?";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, dni);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return rs.getInt("id_cliente");
            }
        }
        return -1;
    }

    public void formalizarContratoBD(int idReserva, String tipoContrato, double monto, java.sql.Date inicio, java.sql.Date fin, DefaultTableModel modPartes) throws Exception {
        Connection con = ConexionBD.conectar();
        if (con == null) throw new Exception("Error de conexión a la base de datos.");
        try {
            con.setAutoCommit(false);

            // Insertar Contrato
            String sqlContrato = "INSERT INTO contrato (tipo_contrato, fecha_inicio, fecha_fin, monto, estado, id_reserva, id_usuario) VALUES (?, ?, ?, ?, 'Vigente', ?, 1)";
            int idContrato = -1;
            try (PreparedStatement pst = con.prepareStatement(sqlContrato, Statement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, tipoContrato); pst.setDate(2, inicio); pst.setDate(3, fin); pst.setDouble(4, monto); pst.setInt(5, idReserva);
                pst.executeUpdate();
                try (ResultSet rs = pst.getGeneratedKeys()) { if (rs.next()) idContrato = rs.getInt(1); }
            }

            // Insertar Partes
            String sqlParte = "INSERT INTO parte_contrato (id_contrato, id_cliente, rol) VALUES (?, ?, ?)";
            try (PreparedStatement pst = con.prepareStatement(sqlParte)) {
                for (int i = 0; i < modPartes.getRowCount(); i++) {
                    pst.setInt(1, idContrato); pst.setInt(2, Integer.parseInt(modPartes.getValueAt(i, 0).toString())); pst.setString(3, modPartes.getValueAt(i, 2).toString()); pst.addBatch();
                }
                pst.executeBatch();
            }

            // Actualizar Reserva e Inmueble
            String sqlReserva = "UPDATE reserva SET estado = 'Formalizada' WHERE id_reserva = ?";
            int idInmueble = -1;
            try (PreparedStatement pst = con.prepareStatement(sqlReserva)) { pst.setInt(1, idReserva); pst.executeUpdate(); }

            String sqlGetInmo = "SELECT id_inmueble FROM reserva WHERE id_reserva = ?";
            try (PreparedStatement pst = con.prepareStatement(sqlGetInmo)) {
                pst.setInt(1, idReserva);
                try (ResultSet rs = pst.executeQuery()) { if (rs.next()) idInmueble = rs.getInt("id_inmueble"); }
            }

            String estadoInmo = tipoContrato.equals("Alquiler") ? "Alquilado" : "Vendido";
            String sqlUpdInmo = "UPDATE inmueble SET estado = ? WHERE id_inmueble = ?";
            try (PreparedStatement pst = con.prepareStatement(sqlUpdInmo)) { pst.setString(1, estadoInmo); pst.setInt(2, idInmueble); pst.executeUpdate(); }

            con.commit();
        } catch (Exception e) { con.rollback(); throw e; } finally { con.setAutoCommit(true); con.close(); }
    }

    public void generarReporteVencimientosBD(DefaultTableModel modeloTabla, int dias) throws Exception {
        modeloTabla.setRowCount(0);
        String sql = "SELECT co.id_contrato, i.direccion, CONCAT(cl.apellido, ', ', cl.nombre) AS locatario, co.fecha_fin, DATEDIFF(co.fecha_fin, CURDATE()) AS dias_restantes " +
                "FROM contrato co INNER JOIN reserva r ON r.id_reserva = co.id_reserva " +
                "INNER JOIN inmueble i ON i.id_inmueble = r.id_inmueble " +
                "INNER JOIN parte_contrato pc ON pc.id_contrato = co.id_contrato AND pc.rol = 'Locatario' " +
                "INNER JOIN cliente cl ON cl.id_cliente = pc.id_cliente " +
                "WHERE co.estado = 'Vigente' AND co.fecha_fin BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? DAY) ORDER BY co.fecha_fin";
        try (Connection con = ConexionBD.conectar(); PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, dias);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    modeloTabla.addRow(new Object[]{rs.getInt("id_contrato"), rs.getString("direccion"), rs.getString("locatario"), rs.getDate("fecha_fin"), rs.getInt("dias_restantes")});
                }
            }
        }
    }
}