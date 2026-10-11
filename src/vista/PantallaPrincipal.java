package vista;

import controlador.GestorSistema;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PantallaPrincipal extends JFrame {

    private JTabbedPane tabbedPane;
    private GestorSistema gestor; // Instancia del controlador MVC

    public PantallaPrincipal() {
        gestor = new GestorSistema();

        setTitle("SIGI - ULTRA Bienes Raíces (Arquitectura MVC)");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // =================================================================
        // BARRA DE MENÚ CON DESPLEGABLES
        // =================================================================
        JMenuBar menuBar = new JMenuBar();
        JMenu menuClientes = new JMenu("Clientes");
        JMenuItem itemGestionClientes = new JMenuItem("Gestión de clientes");
        menuClientes.add(itemGestionClientes);

        JMenu menuInmuebles = new JMenu("Inmuebles");
        JMenuItem itemGestionInmuebles = new JMenuItem("Gestión de inmuebles");
        menuInmuebles.add(itemGestionInmuebles);

        JMenu menuReservas = new JMenu("Reservas");
        JMenuItem itemRegistrarReserva = new JMenuItem("Registrar reserva");
        menuReservas.add(itemRegistrarReserva);

        JMenu menuContratos = new JMenu("Contratos");
        JMenuItem itemFormalizarContrato = new JMenuItem("Formalizar contrato");
        menuContratos.add(itemFormalizarContrato);

        JMenu menuReportes = new JMenu("Reportes");
        JMenuItem itemConsultarVencimientos = new JMenuItem("Consultar vencimientos");
        menuReportes.add(itemConsultarVencimientos);

        menuBar.add(new JMenu("Archivo"));
        menuBar.add(menuClientes);
        menuBar.add(menuInmuebles);
        menuBar.add(new JMenu("Visitas"));
        menuBar.add(menuReservas);
        menuBar.add(menuContratos);
        menuBar.add(menuReportes);
        setJMenuBar(menuBar);

        // =================================================================
        // CONTENEDOR DE PESTAÑAS Y EVENTOS
        // =================================================================
        tabbedPane = new JTabbedPane();
        add(tabbedPane);
        tabbedPane.addTab("Inicio", crearPanelInicio());

        itemGestionClientes.addActionListener(e -> abrirPestaña("Gestión de clientes", crearPanelClientes()));
        itemGestionInmuebles.addActionListener(e -> abrirPestaña("Gestión de inmuebles", crearPanelInmuebles()));
        itemRegistrarReserva.addActionListener(e -> abrirPestaña("Registrar reserva", crearPanelReserva()));
        itemFormalizarContrato.addActionListener(e -> abrirPestaña("Formalizar contrato", crearPanelContrato()));
        itemConsultarVencimientos.addActionListener(e -> abrirPestaña("Consultar vencimientos", crearPanelVencimientos()));
    }

    private void abrirPestaña(String titulo, JPanel panel) {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.getTitleAt(i).equals(titulo)) {
                tabbedPane.setSelectedIndex(i); return;
            }
        }
        tabbedPane.addTab(titulo, panel);
        int index = tabbedPane.indexOfTab(titulo);
        JPanel pnlTab = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTab.setOpaque(false);
        JLabel lblTitulo = new JLabel(titulo + "  ");
        JButton btnCerrar = new JButton("x");
        btnCerrar.setPreferredSize(new Dimension(18, 18));
        btnCerrar.setMargin(new Insets(0, 0, 0, 0));
        btnCerrar.setFocusable(false);
        btnCerrar.addActionListener(e -> {
            int i = tabbedPane.indexOfTabComponent(pnlTab);
            if (i != -1) tabbedPane.remove(i);
        });
        pnlTab.add(lblTitulo); pnlTab.add(btnCerrar);
        tabbedPane.setTabComponentAt(index, pnlTab);
        tabbedPane.setSelectedIndex(index);
    }

    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel lblBienvenida = new JLabel("<html><div style='text-align: center; padding: 20px;'>" +
                "<h1 style='color: #2c3e50;'>Bienvenido al Sistema Integral de Gestión Inmobiliaria (SIGI)</h1>" +
                "<p style='font-size: 14px; color: #7f8c8d;'>Prototipo Operacional - ULTRA Bienes Raíces</p><br><br>" +
                "<h3 style='color: #34495e;'>Guía de navegación:</h3>" +
                "<ul style='font-size: 14px; text-align: left; color: #2c3e50; line-height: 1.8;'>" +
                "<li><b>Cargar Clientes e Inmuebles:</b> Utilice los menús <i>Clientes</i> e <i>Inmuebles</i>.</li>" +
                "<li><b>Registrar Reserva:</b> Menú <i>Reservas -> Registrar reserva</i>.</li>" +
                "<li><b>Formalizar Contrato:</b> Menú <i>Contratos -> Formalizar contrato</i>.</li>" +
                "<li><b>Reporte de Vencimientos:</b> Menú <i>Reportes -> Consultar vencimientos</i>.</li>" +
                "</ul></div></html>", SwingConstants.CENTER);
        panel.add(lblBienvenida, BorderLayout.CENTER);
        return panel;
    }

    // =================================================================
    // PANEL GESTIÓN DE CLIENTES
    // =================================================================
    private JPanel crearPanelClientes() {
        JPanel panelBase = new JPanel(new BorderLayout(15, 15));
        panelBase.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel pnlCabecera = new JPanel(new GridLayout(2, 1));
        JLabel lblTitulo = new JLabel("Gestión de clientes");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        pnlCabecera.add(lblTitulo);
        pnlCabecera.add(new JLabel("Registre nuevos clientes en el sistema y visualice la cartera existente."));
        panelBase.add(pnlCabecera, BorderLayout.NORTH);

        JPanel pnlCuerpo = new JPanel(new BorderLayout(15, 0));

        JPanel pnlIzquierdo = new JPanel(new BorderLayout());
        pnlIzquierdo.setBorder(BorderFactory.createTitledBorder("1. Cartera de Clientes"));
        String[] colClientes = {"ID", "DNI", "Apellido", "Nombre", "Teléfono"};
        DefaultTableModel modClientes = new DefaultTableModel(null, colClientes) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaClientes = new JTable(modClientes);
        pnlIzquierdo.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);

        JButton btnActClientes = new JButton("↻ Actualizar lista");
        pnlIzquierdo.add(btnActClientes, BorderLayout.SOUTH);
        pnlIzquierdo.setPreferredSize(new Dimension(550, 0));

        btnActClientes.addActionListener(e -> {
            try { gestor.cargarClientesDesdeBD(modClientes); }
            catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage()); }
        });
        btnActClientes.doClick(); // Simula click para cargar al abrir

        JPanel pnlDerecho = new JPanel(new GridBagLayout());
        pnlDerecho.setBorder(BorderFactory.createTitledBorder("2. Registrar Nuevo Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx=0; gbc.gridy=0; pnlDerecho.add(new JLabel("DNI:"), gbc);
        gbc.gridx=1; JTextField txtDni = new JTextField(15); pnlDerecho.add(txtDni, gbc);
        gbc.gridx=0; gbc.gridy=1; pnlDerecho.add(new JLabel("Nombre:"), gbc);
        gbc.gridx=1; JTextField txtNombre = new JTextField(15); pnlDerecho.add(txtNombre, gbc);
        gbc.gridx=0; gbc.gridy=2; pnlDerecho.add(new JLabel("Apellido:"), gbc);
        gbc.gridx=1; JTextField txtApellido = new JTextField(15); pnlDerecho.add(txtApellido, gbc);
        gbc.gridx=0; gbc.gridy=3; pnlDerecho.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx=1; JTextField txtTelefono = new JTextField(15); pnlDerecho.add(txtTelefono, gbc);
        gbc.gridx=0; gbc.gridy=4; pnlDerecho.add(new JLabel("Email:"), gbc);
        gbc.gridx=1; JTextField txtEmail = new JTextField(15); pnlDerecho.add(txtEmail, gbc);

        gbc.gridx=1; gbc.gridy=5; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        JButton btnGuardarCliente = new JButton("Guardar Cliente");
        btnGuardarCliente.setBackground(new Color(41, 128, 185)); btnGuardarCliente.setForeground(Color.WHITE);
        pnlDerecho.add(btnGuardarCliente, gbc);

        btnGuardarCliente.addActionListener(e -> {
            if (txtDni.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "DNI, Nombre y Apellido son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                gestor.registrarClienteBD(txtDni.getText().trim(), txtNombre.getText().trim(), txtApellido.getText().trim(), txtTelefono.getText().trim(), txtEmail.getText().trim());
                JOptionPane.showMessageDialog(this, "Cliente guardado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                gestor.cargarClientesDesdeBD(modClientes);
                txtDni.setText(""); txtNombre.setText(""); txtApellido.setText(""); txtTelefono.setText(""); txtEmail.setText("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });

        pnlCuerpo.add(pnlIzquierdo, BorderLayout.WEST); pnlCuerpo.add(pnlDerecho, BorderLayout.CENTER);
        panelBase.add(pnlCuerpo, BorderLayout.CENTER);
        return panelBase;
    }

    // =================================================================
    // PANEL GESTIÓN DE INMUEBLES
    // =================================================================
    private JPanel crearPanelInmuebles() {
        JPanel panelBase = new JPanel(new BorderLayout(15, 15));
        panelBase.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel pnlCabecera = new JPanel(new GridLayout(2, 1));
        JLabel lblTitulo = new JLabel("Gestión de inmuebles");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        pnlCabecera.add(lblTitulo);
        pnlCabecera.add(new JLabel("Agregue nuevas propiedades al catálogo."));
        panelBase.add(pnlCabecera, BorderLayout.NORTH);

        JPanel pnlCuerpo = new JPanel(new BorderLayout(15, 0));

        JPanel pnlIzquierdo = new JPanel(new BorderLayout());
        pnlIzquierdo.setBorder(BorderFactory.createTitledBorder("1. Catálogo Completo"));
        String[] colInm = {"ID", "Dirección", "Tipo", "Operación", "Precio", "Estado"};
        DefaultTableModel modInm = new DefaultTableModel(null, colInm) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaInmuebles = new JTable(modInm);
        pnlIzquierdo.add(new JScrollPane(tablaInmuebles), BorderLayout.CENTER);

        JButton btnActInm = new JButton("↻ Actualizar lista");
        pnlIzquierdo.add(btnActInm, BorderLayout.SOUTH);
        pnlIzquierdo.setPreferredSize(new Dimension(550, 0));

        btnActInm.addActionListener(e -> {
            try { gestor.cargarTodosInmueblesDesdeBD(modInm); }
            catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage()); }
        });
        btnActInm.doClick();

        JPanel pnlDerecho = new JPanel(new GridBagLayout());
        pnlDerecho.setBorder(BorderFactory.createTitledBorder("2. Registrar Nuevo Inmueble"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx=0; gbc.gridy=0; pnlDerecho.add(new JLabel("Dirección:"), gbc);
        gbc.gridx=1; JTextField txtDireccion = new JTextField(15); pnlDerecho.add(txtDireccion, gbc);
        gbc.gridx=0; gbc.gridy=1; pnlDerecho.add(new JLabel("Descripción breve:"), gbc);
        gbc.gridx=1; JTextField txtDesc = new JTextField(15); pnlDerecho.add(txtDesc, gbc);
        gbc.gridx=0; gbc.gridy=2; pnlDerecho.add(new JLabel("Superficie (m2):"), gbc);
        gbc.gridx=1; JTextField txtSup = new JTextField("0", 15); pnlDerecho.add(txtSup, gbc);
        gbc.gridx=0; gbc.gridy=3; pnlDerecho.add(new JLabel("Tipo:"), gbc);
        gbc.gridx=1; JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Casa", "Departamento", "Local", "Terreno"}); pnlDerecho.add(cbTipo, gbc);
        gbc.gridx=0; gbc.gridy=4; pnlDerecho.add(new JLabel("Operación:"), gbc);
        gbc.gridx=1; JComboBox<String> cbOperacion = new JComboBox<>(new String[]{"Alquiler", "Venta"}); pnlDerecho.add(cbOperacion, gbc);
        gbc.gridx=0; gbc.gridy=5; pnlDerecho.add(new JLabel("Precio ($):"), gbc);
        gbc.gridx=1; JTextField txtPrecio = new JTextField("0.00", 15); pnlDerecho.add(txtPrecio, gbc);

        gbc.gridx=1; gbc.gridy=6; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        JButton btnGuardarInmueble = new JButton("Guardar Inmueble");
        btnGuardarInmueble.setBackground(new Color(41, 128, 185)); btnGuardarInmueble.setForeground(Color.WHITE);
        pnlDerecho.add(btnGuardarInmueble, gbc);

        btnGuardarInmueble.addActionListener(e -> {
            if (txtDireccion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "La dirección es obligatoria.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                gestor.registrarInmuebleBD(txtDesc.getText().trim(), txtDireccion.getText().trim(),
                        Double.parseDouble(txtSup.getText()), cbTipo.getSelectedItem().toString(),
                        cbOperacion.getSelectedItem().toString(), Double.parseDouble(txtPrecio.getText()));
                JOptionPane.showMessageDialog(this, "Inmueble guardado y disponible.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                gestor.cargarTodosInmueblesDesdeBD(modInm);
                txtDireccion.setText(""); txtDesc.setText(""); txtSup.setText("0"); txtPrecio.setText("0.00");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error al guardar (Verifique los números): " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });

        pnlCuerpo.add(pnlIzquierdo, BorderLayout.WEST); pnlCuerpo.add(pnlDerecho, BorderLayout.CENTER);
        panelBase.add(pnlCuerpo, BorderLayout.CENTER);
        return panelBase;
    }

    // =================================================================
    // PANEL REGISTRAR RESERVA (CU09)
    // =================================================================
    private JPanel crearPanelReserva() {
        gestor.resetClienteSeleccionado(); // Limpiamos la memoria del controlador

        JPanel panelBase = new JPanel(new BorderLayout(15, 15));
        panelBase.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel pnlCabecera = new JPanel(new GridLayout(2, 1));
        JLabel lblTitulo = new JLabel("Registrar reserva");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        pnlCabecera.add(lblTitulo);
        pnlCabecera.add(new JLabel("Seleccione un inmueble disponible, busque al cliente e ingrese las condiciones de la reserva."));
        panelBase.add(pnlCabecera, BorderLayout.NORTH);

        JPanel pnlCuerpo = new JPanel(new BorderLayout(15, 0));

        JPanel pnlIzquierdo = new JPanel(new BorderLayout());
        pnlIzquierdo.setBorder(BorderFactory.createTitledBorder("1. Seleccionar inmueble (Solo Disponibles)"));
        String[] colInmuebles = {"ID", "Dirección", "Tipo", "Operación", "Precio"};
        DefaultTableModel modInmuebles = new DefaultTableModel(null, colInmuebles) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable tablaInmuebles = new JTable(modInmuebles);
        tablaInmuebles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pnlIzquierdo.add(new JScrollPane(tablaInmuebles), BorderLayout.CENTER);

        JButton btnActInmuebles = new JButton("↻ Actualizar lista");
        pnlIzquierdo.add(btnActInmuebles, BorderLayout.SOUTH);
        pnlIzquierdo.setPreferredSize(new Dimension(450, 0));

        btnActInmuebles.addActionListener(e -> {
            try { gestor.cargarInmueblesDisponibles(modInmuebles); }
            catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage()); }
        });
        btnActInmuebles.doClick();

        JPanel pnlDerecho = new JPanel(new GridLayout(2, 1, 0, 15));

        JPanel pnlCliente = new JPanel(new BorderLayout(10, 10));
        pnlCliente.setBorder(BorderFactory.createTitledBorder("2. Buscar cliente"));
        JPanel pnlBuscador = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlBuscador.add(new JLabel("DNI Cliente: "));
        JTextField txtBuscarCliente = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(Color.DARK_GRAY); btnBuscar.setForeground(Color.WHITE);
        pnlBuscador.add(txtBuscarCliente); pnlBuscador.add(btnBuscar);

        JLabel lblClienteSel = new JLabel("[Busque un cliente por DNI]");
        lblClienteSel.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
        lblClienteSel.setHorizontalAlignment(SwingConstants.CENTER);

        btnBuscar.addActionListener(e -> {
            try {
                String resultado = gestor.buscarClientePorDni(txtBuscarCliente.getText().trim());
                lblClienteSel.setText(resultado);
                if (gestor.getIdClienteSeleccionadoReserva() != -1) {
                    lblClienteSel.setForeground(new Color(39, 174, 96));
                } else {
                    lblClienteSel.setForeground(Color.RED);
                }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage()); }
        });

        pnlCliente.add(pnlBuscador, BorderLayout.NORTH); pnlCliente.add(lblClienteSel, BorderLayout.CENTER);

        JPanel pnlDatos = new JPanel(new GridBagLayout());
        pnlDatos.setBorder(BorderFactory.createTitledBorder("3. Datos de la reserva"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0; pnlDatos.add(new JLabel("Tipo de reserva:"), gbc);
        gbc.gridx=1; JComboBox<String> cbTipo = new JComboBox<>(new String[]{"Onerosa", "Administrativa"}); pnlDatos.add(cbTipo, gbc);
        gbc.gridx=0; gbc.gridy=1; pnlDatos.add(new JLabel("Monto pactado ($):"), gbc);
        gbc.gridx=1; JTextField txtMonto = new JTextField("0.00", 15); pnlDatos.add(txtMonto, gbc);
        gbc.gridx=0; gbc.gridy=2; pnlDatos.add(new JLabel("Fecha venc. (dd/mm/yyyy):"), gbc);
        gbc.gridx=1; JTextField txtFecha = new JTextField(15); pnlDatos.add(txtFecha, gbc);

        gbc.gridx=1; gbc.gridy=3; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        JButton btnRegistrar = new JButton("Registrar reserva");
        btnRegistrar.setBackground(new Color(46, 204, 113)); btnRegistrar.setForeground(Color.WHITE);
        pnlDatos.add(btnRegistrar, gbc);

        btnRegistrar.addActionListener(e -> {
            int filaSel = tablaInmuebles.getSelectedRow();
            if (filaSel == -1) { JOptionPane.showMessageDialog(this, "Debe seleccionar un inmueble.", "Advertencia", JOptionPane.WARNING_MESSAGE); return; }
            try {
                int idInmueble = Integer.parseInt(modInmuebles.getValueAt(filaSel, 0).toString());
                String tipoReserva = cbTipo.getSelectedItem().toString();
                double monto = Double.parseDouble(txtMonto.getText());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate fechaVenc = LocalDate.parse(txtFecha.getText(), formatter);

                // El Controlador hace el trabajo
                gestor.registrarReservaBD(idInmueble, tipoReserva, monto, java.sql.Date.valueOf(fechaVenc));

                JOptionPane.showMessageDialog(this, "¡Reserva registrada con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                gestor.cargarInmueblesDisponibles(modInmuebles);
                txtMonto.setText("0.00"); txtFecha.setText("");
                lblClienteSel.setText("[Busque un cliente por DNI]"); lblClienteSel.setForeground(Color.BLACK);
                gestor.resetClienteSeleccionado();
            } catch (DateTimeParseException ex) { JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use dd/mm/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });

        pnlDerecho.add(pnlCliente); pnlDerecho.add(pnlDatos);
        pnlCuerpo.add(pnlIzquierdo, BorderLayout.WEST); pnlCuerpo.add(pnlDerecho, BorderLayout.CENTER);
        panelBase.add(pnlCuerpo, BorderLayout.CENTER);
        return panelBase;
    }

    // =================================================================
    // PANEL FORMALIZAR CONTRATO (CU10)
    // =================================================================
    private JPanel crearPanelContrato() {
        JPanel panelBase = new JPanel(new BorderLayout(15, 15));
        panelBase.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel pnlCabecera = new JPanel(new GridLayout(2, 1));
        JLabel lblTitulo = new JLabel("Formalizar contrato");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        pnlCabecera.add(lblTitulo);
        pnlCabecera.add(new JLabel("Seleccione la reserva vigente, agregue las partes con su rol y complete las condiciones del contrato."));
        panelBase.add(pnlCabecera, BorderLayout.NORTH);

        JPanel pnlCuerpo = new JPanel(new BorderLayout(15, 0));

        JPanel pnlIzquierdo = new JPanel(new GridLayout(2, 1, 0, 15));
        pnlIzquierdo.setPreferredSize(new Dimension(500, 0));

        JPanel pnlReserva = new JPanel(new BorderLayout());
        pnlReserva.setBorder(BorderFactory.createTitledBorder("1. Seleccionar reserva"));
        String[] colReservas = {"N.º Reserva", "Inmueble", "Cliente", "Vence"};
        DefaultTableModel modReservas = new DefaultTableModel(null, colReservas){
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaReservas = new JTable(modReservas);
        tablaReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pnlReserva.add(new JScrollPane(tablaReservas), BorderLayout.CENTER);

        JButton btnActReservas = new JButton("↻ Actualizar reservas");
        pnlReserva.add(btnActReservas, BorderLayout.SOUTH);
        btnActReservas.addActionListener(e -> {
            try { gestor.cargarReservasVigentesBD(modReservas); }
            catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage()); }
        });
        btnActReservas.doClick();

        JPanel pnlPartes = new JPanel(new BorderLayout(5, 5));
        pnlPartes.setBorder(BorderFactory.createTitledBorder("2. Agregar partes"));
        JPanel pnlControlesPartes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlControlesPartes.add(new JLabel("DNI:"));
        JTextField txtDniParte = new JTextField(10); pnlControlesPartes.add(txtDniParte);
        pnlControlesPartes.add(new JLabel("Rol:"));
        JComboBox<String> cbRol = new JComboBox<>(new String[]{"Locador", "Locatario", "Vendedor", "Comprador", "Garante"});
        pnlControlesPartes.add(cbRol);
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(Color.DARK_GRAY); btnAgregar.setForeground(Color.WHITE);
        pnlControlesPartes.add(btnAgregar);

        String[] colPartes = {"ID Cliente", "DNI", "Rol"};
        DefaultTableModel modPartes = new DefaultTableModel(null, colPartes){
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaPartes = new JTable(modPartes);
        pnlPartes.add(pnlControlesPartes, BorderLayout.NORTH); pnlPartes.add(new JScrollPane(tablaPartes), BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> {
            try {
                String dni = txtDniParte.getText().trim();
                int idCliente = gestor.validarDniYObtenerId(dni);
                if (idCliente != -1) {
                    modPartes.addRow(new Object[]{idCliente, dni, cbRol.getSelectedItem().toString()});
                    txtDniParte.setText("");
                } else { JOptionPane.showMessageDialog(this, "El DNI no corresponde a un cliente registrado.", "Error", JOptionPane.ERROR_MESSAGE); }
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage()); }
        });

        pnlIzquierdo.add(pnlReserva); pnlIzquierdo.add(pnlPartes);

        JPanel pnlDerecho = new JPanel(new BorderLayout());
        JPanel pnlCondiciones = new JPanel(new GridBagLayout());
        pnlCondiciones.setBorder(BorderFactory.createTitledBorder("3. Condiciones del contrato"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10); gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx=0; gbc.gridy=0; pnlCondiciones.add(new JLabel("Tipo de contrato:"), gbc);
        gbc.gridx=1; JComboBox<String> cbTipoContrato = new JComboBox<>(new String[]{"Alquiler", "Venta"}); pnlCondiciones.add(cbTipoContrato, gbc);
        gbc.gridx=0; gbc.gridy=1; pnlCondiciones.add(new JLabel("Monto total/mensual ($):"), gbc);
        gbc.gridx=1; JTextField txtMonto = new JTextField("0.00", 15); pnlCondiciones.add(txtMonto, gbc);
        gbc.gridx=0; gbc.gridy=2; pnlCondiciones.add(new JLabel("Fecha de inicio (dd/mm/yyyy):"), gbc);
        gbc.gridx=1; JTextField txtInicio = new JTextField(15); pnlCondiciones.add(txtInicio, gbc);
        gbc.gridx=0; gbc.gridy=3; pnlCondiciones.add(new JLabel("Fecha de fin (si aplica):"), gbc);
        gbc.gridx=1; JTextField txtFin = new JTextField(15); pnlCondiciones.add(txtFin, gbc);

        JPanel pnlBotonDer = new JPanel(new BorderLayout());
        pnlBotonDer.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        JButton btnFormalizar = new JButton("Formalizar contrato");
        btnFormalizar.setBackground(new Color(46, 204, 113)); btnFormalizar.setForeground(Color.WHITE);
        pnlBotonDer.add(new JLabel("<html><font color='gray'>La reserva quedará formalizada y el inmueble ocupado.</font></html>"), BorderLayout.CENTER);
        pnlBotonDer.add(btnFormalizar, BorderLayout.SOUTH);

        btnFormalizar.addActionListener(e -> {
            int filaRes = tablaReservas.getSelectedRow();
            if(filaRes == -1) { JOptionPane.showMessageDialog(this, "Seleccione una reserva.", "Advertencia", JOptionPane.WARNING_MESSAGE); return; }
            if(modPartes.getRowCount() < 2) { JOptionPane.showMessageDialog(this, "Un contrato requiere al menos 2 partes.", "Error", JOptionPane.ERROR_MESSAGE); return; }

            try {
                int idReserva = Integer.parseInt(modReservas.getValueAt(filaRes, 0).toString());
                String tipoContrato = cbTipoContrato.getSelectedItem().toString();
                double monto = Double.parseDouble(txtMonto.getText());
                DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                java.sql.Date sqlInicio = java.sql.Date.valueOf(LocalDate.parse(txtInicio.getText(), f));
                java.sql.Date sqlFin = txtFin.getText().isEmpty() ? null : java.sql.Date.valueOf(LocalDate.parse(txtFin.getText(), f));

                // El Controlador hace el trabajo
                gestor.formalizarContratoBD(idReserva, tipoContrato, monto, sqlInicio, sqlFin, modPartes);

                JOptionPane.showMessageDialog(this, "¡Contrato formalizado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                gestor.cargarReservasVigentesBD(modReservas);
                modPartes.setRowCount(0); txtMonto.setText("0.00"); txtInicio.setText(""); txtFin.setText("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error al formalizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        });

        pnlDerecho.add(pnlCondiciones, BorderLayout.NORTH); pnlDerecho.add(pnlBotonDer, BorderLayout.SOUTH);
        pnlCuerpo.add(pnlIzquierdo, BorderLayout.WEST); pnlCuerpo.add(pnlDerecho, BorderLayout.CENTER);
        panelBase.add(pnlCuerpo, BorderLayout.CENTER);
        return panelBase;
    }

    // =================================================================
    // PANEL CONSULTAR VENCIMIENTOS (CU11)
    // =================================================================
    private JPanel crearPanelVencimientos() {
        JPanel panelBase = new JPanel(new BorderLayout(15, 15));
        panelBase.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel pnlCabecera = new JPanel(new GridLayout(2, 1));
        JLabel lblTitulo = new JLabel("Consultar vencimientos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        pnlCabecera.add(lblTitulo);
        pnlCabecera.add(new JLabel("Listado de contratos vigentes que finalizan en los próximos 60 días."));
        panelBase.add(pnlCabecera, BorderLayout.NORTH);

        JPanel pnlCuerpo = new JPanel(new BorderLayout(0, 15));

        JPanel pnlReporte = new JPanel(new BorderLayout());
        pnlReporte.setBorder(BorderFactory.createTitledBorder("Reporte"));
        JPanel pnlFiltro = new JPanel(new BorderLayout());
        pnlFiltro.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        pnlFiltro.add(new JLabel("Período: desde la fecha actual hasta los próximos 60 días."), BorderLayout.WEST);
        JButton btnGenerar = new JButton("Generar reporte");
        btnGenerar.setBackground(Color.DARK_GRAY); btnGenerar.setForeground(Color.WHITE);
        pnlFiltro.add(btnGenerar, BorderLayout.EAST);
        pnlReporte.add(pnlFiltro, BorderLayout.CENTER);

        JPanel pnlTabla = new JPanel(new BorderLayout());
        pnlTabla.setBorder(BorderFactory.createTitledBorder("Contratos próximos a vencer"));
        String[] colVenc = {"N.º Contrato", "Inmueble", "Locatario", "Fecha de fin", "Días restantes"};
        DefaultTableModel modVenc = new DefaultTableModel(null, colVenc){
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaVencimientos = new JTable(modVenc);
        pnlTabla.add(new JScrollPane(tablaVencimientos), BorderLayout.CENTER);

        JLabel lblMensaje = new JLabel("Haga clic en 'Generar reporte' para consultar.");
        lblMensaje.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
        lblMensaje.setPreferredSize(new Dimension(0, 40));
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);

        btnGenerar.addActionListener(e -> {
            try {
                gestor.generarReporteVencimientosBD(modVenc, 60);
                if (modVenc.getRowCount() == 0) lblMensaje.setText("No hay contratos próximos a vencer en el período consultado.");
                else lblMensaje.setText("Reporte generado exitosamente.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage()); }
        });

        pnlCuerpo.add(pnlReporte, BorderLayout.NORTH); pnlCuerpo.add(pnlTabla, BorderLayout.CENTER); pnlCuerpo.add(lblMensaje, BorderLayout.SOUTH);
        panelBase.add(pnlCuerpo, BorderLayout.CENTER);

        return panelBase;
    }
}