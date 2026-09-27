-- SIGI - Sistema Integral de Gestión Inmobiliaria
-- Script 2: Inserción de registros de prueba

USE sigi;


-- Usuarios (contraseñas almacenadas como hash SHA-256)

INSERT INTO usuario (email, contrasena, nombre, rol, fecha_registro, estado_cuenta) VALUES
('lgarcia@ultrabienesraices.com',  SHA2('Agente2026!', 256), 'Laura García',   'Agente',         '2025-03-10', 'Activa'),
('mtorres@ultrabienesraices.com',  SHA2('Agente2026!', 256), 'Martín Torres',  'Agente',         '2025-06-02', 'Activa'),
('admin@ultrabienesraices.com',    SHA2('Admin2026!', 256),  'Carla Méndez',   'Administrativo', '2025-01-15', 'Activa'),
('gerencia@ultrabienesraices.com', SHA2('Gerente2026!', 256),'Roberto Ibáñez', 'Gerente',        '2025-01-15', 'Activa');


-- Clientes

INSERT INTO cliente (dni_cliente, cuit, nombre, apellido, telefono, email, fecha_nacimiento, estado_civil, direccion) VALUES
('22345678', '20223456789', 'Juan',    'Pérez',     '3874001122', 'jperez@mail.com',    '1971-04-12', 'Casado',     'Belgrano 450'),
('30111222', '27301112224', 'Ana',     'López',     '3874223344', 'alopez@mail.com',    '1983-09-25', 'Soltera',    'Mitre 980'),
('28765432', '20287654321', 'Pedro',   'Gómez',     '3874556677', 'pgomez@mail.com',    '1981-01-30', 'Casado',     'Caseros 1500'),
('25444555', '27254445556', 'María',   'Suárez',    '3874889900', 'msuarez@mail.com',   '1976-07-08', 'Divorciada', 'Alvarado 233'),
('33222111', '20332221118', 'Luis',    'Fernández', '3875112233', 'lfernandez@mail.com','1988-11-19', 'Soltero',    'España 1320'),
('29888777', '27298887772', 'Sofía',   'Díaz',      '3875334455', 'sdiaz@mail.com',     '1982-03-03', 'Casada',     'Urquiza 610'),
('24666333', '20246663339', 'Jorge',   'Ramos',     '3875556677', 'jramos@mail.com',    '1975-12-14', 'Casado',     'Santiago del Estero 45'),
('35777888', NULL,          'Carlos',  'Ruiz',      '3875778899', 'cruiz@mail.com',     '1991-05-21', 'Soltero',    'Pueyrredón 870'),
('20555999', '27205559993', 'Elena',   'Castro',    '3875990011', 'ecastro@mail.com',   '1968-08-02', 'Viuda',      'Zuviría 120'),
('31999000', NULL,          'Diego',   'Molina',    '3876112244', 'dmolina@mail.com',   '1985-10-17', 'Casado',     'Lerma 300');


-- Inmuebles 

INSERT INTO inmueble (descripcion, direccion, superficie, tipo_inmueble, tipo_operacion, precio, estado, id_usuario) VALUES
('Departamento 2 ambientes, balcón',   'Belgrano 870, 1.º A',  52.00,  'Departamento', 'Alquiler', 380000.00,   'Alquilado',  1),
('Casa 3 dormitorios con patio',       'Alvarado 233',         140.00, 'Casa',         'Alquiler', 650000.00,   'Alquilado',  1),
('Local comercial sobre avenida',      'Mitre 540',            85.00,  'Local',        'Alquiler', 900000.00,   'Alquilado',  2),
('Departamento 3 ambientes',           'Caseros 1210',         75.00,  'Departamento', 'Alquiler', 480000.00,   'Alquilado',  2),
('Departamento 1 ambiente',            'Belgrano 450, 3.º B',  38.00,  'Departamento', 'Alquiler', 320000.00,   'Alquilado',  1),
('Casa 2 dormitorios',                 'Urquiza 1050',         110.00, 'Casa',         'Venta',    85000000.00, 'Vendido',    2),
('Departamento 2 ambientes a estrenar','España 1320, 5.º C',   55.00,  'Departamento', 'Alquiler', 420000.00,   'Reservado',  1),
('Terreno 10 x 30',                    'Ruta 26, lote 14',     300.00, 'Terreno',      'Venta',    30000000.00, 'Reservado',  2),
('Casa 4 dormitorios con pileta',      'Los Ceibos 77',        220.00, 'Casa',         'Venta',    150000000.00,'Disponible', 1),
('Local en galería',                   'Florida 150, local 8', 30.00,  'Local',        'Alquiler', 350000.00,   'Disponible', 2),
('Departamento 2 ambientes',           'Pellegrini 640, 2.º D',50.00,  'Departamento', 'Alquiler', 400000.00,   'Disponible', 1);


-- Propietarios de cada inmueble 

INSERT INTO inmueble_propietario (id_inmueble, id_cliente) VALUES
(1, 1), (2, 1), (3, 9), (4, 9), (5, 1),
(6, 7), (7, 9), (8, 3), (9, 1), (9, 9), 
(10, 9), (11, 7);


-- Visitas

INSERT INTO visita (fecha_hora, estado, comentarios, id_inmueble, id_cliente, id_usuario) VALUES
(CONCAT(DATE_SUB(CURDATE(), INTERVAL 15 DAY), ' 10:00:00'), 'Realizada',  'Interesado, pidió condiciones', 7,  5,  1),
(CONCAT(DATE_SUB(CURDATE(), INTERVAL 12 DAY), ' 17:30:00'), 'Realizada',  'Consulta por financiación',     8,  8,  2),
(CONCAT(DATE_ADD(CURDATE(), INTERVAL 2 DAY),  ' 11:00:00'), 'Programada', NULL,                            9,  10, 1),
(CONCAT(DATE_ADD(CURDATE(), INTERVAL 3 DAY),  ' 16:00:00'), 'Programada', 'Primera visita',                11, 2,  1),
(CONCAT(DATE_ADD(CURDATE(), INTERVAL 3 DAY),  ' 18:00:00'), 'Programada', NULL,                            10, 6,  2);


-- Reservas
/* Las reservas 1 a 6 ya fueron formalizadas en contratos.
 Las reservas 7 y 8 están vigentes (inmuebles en estado Reservado).*/

INSERT INTO reserva (tipo_reserva, fecha, fecha_vencimiento, monto, estado, id_inmueble, id_cliente, id_usuario) VALUES
('Onerosa',        DATE_SUB(CURDATE(), INTERVAL 740 DAY), DATE_SUB(CURDATE(), INTERVAL 730 DAY), 380000.00, 'Formalizada', 1, 4, 1),
('Onerosa',        DATE_SUB(CURDATE(), INTERVAL 725 DAY), DATE_SUB(CURDATE(), INTERVAL 715 DAY), 650000.00, 'Formalizada', 2, 5, 1),
('Administrativa', DATE_SUB(CURDATE(), INTERVAL 700 DAY), DATE_SUB(CURDATE(), INTERVAL 690 DAY), 0,         'Formalizada', 3, 6, 2),
('Onerosa',        DATE_SUB(CURDATE(), INTERVAL 690 DAY), DATE_SUB(CURDATE(), INTERVAL 680 DAY), 480000.00, 'Formalizada', 4, 7, 2),
('Onerosa',        DATE_SUB(CURDATE(), INTERVAL 40 DAY),  DATE_SUB(CURDATE(), INTERVAL 30 DAY),  320000.00, 'Formalizada', 5, 2, 1),
('Onerosa',        DATE_SUB(CURDATE(), INTERVAL 90 DAY),  DATE_SUB(CURDATE(), INTERVAL 80 DAY),  8500000.00,'Formalizada', 6, 10, 2),
('Onerosa',        DATE_SUB(CURDATE(), INTERVAL 5 DAY),   DATE_ADD(CURDATE(), INTERVAL 10 DAY),  420000.00, 'Vigente',     7, 5, 1),
('Administrativa', DATE_SUB(CURDATE(), INTERVAL 2 DAY),   DATE_ADD(CURDATE(), INTERVAL 5 DAY),   0,         'Vigente',     8, 8, 2);


-- Pagos (solo sobre reservas onerosas)
INSERT INTO pago (fecha, monto, medio_pago, nro_comprobante, id_reserva) VALUES
(DATE_SUB(CURDATE(), INTERVAL 740 DAY), 380000.00,  'Transferencia', 'C-0001', 1),
(DATE_SUB(CURDATE(), INTERVAL 725 DAY), 650000.00,  'Efectivo',      'C-0002', 2),
(DATE_SUB(CURDATE(), INTERVAL 690 DAY), 480000.00,  'Transferencia', 'C-0003', 4),
(DATE_SUB(CURDATE(), INTERVAL 40 DAY),  320000.00,  'Tarjeta',       'C-0004', 5),
(DATE_SUB(CURDATE(), INTERVAL 90 DAY),  5000000.00, 'Transferencia', 'C-0005', 6),
(DATE_SUB(CURDATE(), INTERVAL 85 DAY),  3500000.00, 'Cheque',        'C-0006', 6),
(DATE_SUB(CURDATE(), INTERVAL 5 DAY),   200000.00,  'Efectivo',      'C-0007', 7);   


-- Contratos
/* Contratos 1 a 3: vencen dentro de los próximos 60 días (el 3, justo a los 60, valor límite incluido). 
Contrato 4: vence a los 61 días (valor límite excluido, CPC05). 
Contrato 5: vence en casi 3 años. Contrato 6: venta, sin fecha de fin.*/

INSERT INTO contrato (tipo_contrato, fecha_inicio, fecha_fin, monto, estado, id_reserva, id_usuario) VALUES
('Alquiler', DATE_SUB(CURDATE(), INTERVAL 722 DAY), DATE_ADD(CURDATE(), INTERVAL 8 DAY),   380000.00,   'Vigente', 1, 1),
('Alquiler', DATE_SUB(CURDATE(), INTERVAL 707 DAY), DATE_ADD(CURDATE(), INTERVAL 23 DAY),  650000.00,   'Vigente', 2, 1),
('Alquiler', DATE_SUB(CURDATE(), INTERVAL 682 DAY), DATE_ADD(CURDATE(), INTERVAL 60 DAY),  900000.00,   'Vigente', 3, 2),
('Alquiler', DATE_SUB(CURDATE(), INTERVAL 671 DAY), DATE_ADD(CURDATE(), INTERVAL 61 DAY),  480000.00,   'Vigente', 4, 2),
('Alquiler', DATE_SUB(CURDATE(), INTERVAL 30 DAY),  DATE_ADD(CURDATE(), INTERVAL 1065 DAY),320000.00,   'Vigente', 5, 1),
('Venta',    DATE_SUB(CURDATE(), INTERVAL 75 DAY),  NULL,                                   85000000.00, 'Vigente', 6, 2);

-- Partes de cada contrato

INSERT INTO parte_contrato (id_contrato, id_cliente, rol) VALUES
(1, 4, 'Locatario'), (1, 1, 'Locador'),  (1, 3, 'Garante'),
(2, 5, 'Locatario'), (2, 1, 'Locador'),  (2, 8, 'Garante'),
(3, 6, 'Locatario'), (3, 9, 'Locador'),
(4, 7, 'Locatario'), (4, 9, 'Locador'),  (4, 10, 'Garante'),
(5, 2, 'Locatario'), (5, 1, 'Locador'),  (5, 3, 'Garante'),
(6, 10, 'Comprador'),(6, 7, 'Vendedor');
