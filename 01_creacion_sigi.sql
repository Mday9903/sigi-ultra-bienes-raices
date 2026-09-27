-- =============================================================
-- SIGI - Sistema Integral de Gestión Inmobiliaria
-- ULTRA Bienes Raíces
-- Script 1: Creación de la base de datos y sus tablas
-- Motor: MySQL 8.0 (InnoDB, utf8mb4)
-- =============================================================

DROP DATABASE IF EXISTS sigi;
CREATE DATABASE sigi
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish_ci;
USE sigi;

-- -------------------------------------------------------------
-- Tabla usuario (UsuarioRegistrado)
-- -------------------------------------------------------------
CREATE TABLE usuario (
    id_usuario      INT            NOT NULL AUTO_INCREMENT,
    email           VARCHAR(100)   NOT NULL,
    contrasena      VARCHAR(255)   NOT NULL,   -- se almacena el hash, no la clave en texto plano
    nombre          VARCHAR(100)   NOT NULL,
    rol             VARCHAR(30)    NOT NULL,
    fecha_registro  DATE           NOT NULL,
    estado_cuenta   VARCHAR(20)    NOT NULL DEFAULT 'Activa',
    CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
    CONSTRAINT uq_usuario_email UNIQUE (email),
    CONSTRAINT ck_usuario_rol CHECK (rol IN ('Agente', 'Administrativo', 'Gerente')),
    CONSTRAINT ck_usuario_estado CHECK (estado_cuenta IN ('Activa', 'Suspendida'))
) ENGINE = InnoDB;

-- -------------------------------------------------------------
-- Tabla cliente (Cliente)
-- -------------------------------------------------------------
CREATE TABLE cliente (
    id_cliente        INT            NOT NULL AUTO_INCREMENT,
    dni_cliente       VARCHAR(15)    NOT NULL,
    cuit              VARCHAR(13)    NULL,
    nombre            VARCHAR(60)    NOT NULL,
    apellido          VARCHAR(60)    NOT NULL,
    telefono          VARCHAR(20)    NOT NULL,
    email             VARCHAR(100)   NULL,
    fecha_nacimiento  DATE           NULL,
    estado_civil      VARCHAR(20)    NULL,
    direccion         VARCHAR(150)   NULL,
    CONSTRAINT pk_cliente PRIMARY KEY (id_cliente),
    CONSTRAINT uq_cliente_dni UNIQUE (dni_cliente)
) ENGINE = InnoDB;

-- -------------------------------------------------------------
-- Tabla inmueble (Inmueble)
-- -------------------------------------------------------------
CREATE TABLE inmueble (
    id_inmueble     INT             NOT NULL AUTO_INCREMENT,
    descripcion     VARCHAR(255)    NULL,
    direccion       VARCHAR(150)    NOT NULL,
    superficie      DECIMAL(10,2)   NULL,
    tipo_inmueble   VARCHAR(30)     NOT NULL,
    tipo_operacion  VARCHAR(10)     NOT NULL,
    precio          DECIMAL(14,2)   NOT NULL,
    estado          VARCHAR(15)     NOT NULL DEFAULT 'Disponible',
    id_usuario      INT             NOT NULL,
    CONSTRAINT pk_inmueble PRIMARY KEY (id_inmueble),
    CONSTRAINT fk_inmueble_usuario FOREIGN KEY (id_usuario)
        REFERENCES usuario (id_usuario),
    CONSTRAINT ck_inmueble_tipo CHECK (tipo_inmueble IN ('Casa', 'Departamento', 'Local', 'Terreno')),
    CONSTRAINT ck_inmueble_operacion CHECK (tipo_operacion IN ('Alquiler', 'Venta')),
    CONSTRAINT ck_inmueble_estado CHECK (estado IN ('Disponible', 'Reservado', 'Alquilado', 'Vendido')),
    CONSTRAINT ck_inmueble_precio CHECK (precio > 0)
) ENGINE = InnoDB;

-- Índice para acelerar el filtrado de CU07 (RNF04)
CREATE INDEX ix_inmueble_operacion_estado ON inmueble (tipo_operacion, estado);

-- -------------------------------------------------------------
-- Tabla inmueble_propietario (asociación "posee", N:M)
-- -------------------------------------------------------------
CREATE TABLE inmueble_propietario (
    id_inmueble  INT  NOT NULL,
    id_cliente   INT  NOT NULL,
    CONSTRAINT pk_inmueble_propietario PRIMARY KEY (id_inmueble, id_cliente),
    CONSTRAINT fk_propietario_inmueble FOREIGN KEY (id_inmueble)
        REFERENCES inmueble (id_inmueble) ON DELETE CASCADE,
    CONSTRAINT fk_propietario_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_cliente)
) ENGINE = InnoDB;

-- -------------------------------------------------------------
-- Tabla visita (Visita)
-- -------------------------------------------------------------
CREATE TABLE visita (
    id_visita    INT            NOT NULL AUTO_INCREMENT,
    fecha_hora   DATETIME       NOT NULL,
    estado       VARCHAR(15)    NOT NULL DEFAULT 'Programada',
    comentarios  VARCHAR(255)   NULL,
    id_inmueble  INT            NOT NULL,
    id_cliente   INT            NOT NULL,
    id_usuario   INT            NOT NULL,
    CONSTRAINT pk_visita PRIMARY KEY (id_visita),
    CONSTRAINT fk_visita_inmueble FOREIGN KEY (id_inmueble)
        REFERENCES inmueble (id_inmueble),
    CONSTRAINT fk_visita_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_cliente),
    CONSTRAINT fk_visita_usuario FOREIGN KEY (id_usuario)
        REFERENCES usuario (id_usuario),
    -- Evita la superposición horaria de un agente (CU08, flujo 4a)
    CONSTRAINT uq_visita_agente_horario UNIQUE (id_usuario, fecha_hora),
    CONSTRAINT ck_visita_estado CHECK (estado IN ('Programada', 'Realizada', 'Cancelada'))
) ENGINE = InnoDB;

-- -------------------------------------------------------------
-- Tabla reserva (Reserva)
-- -------------------------------------------------------------
CREATE TABLE reserva (
    id_reserva         INT             NOT NULL AUTO_INCREMENT,
    tipo_reserva       VARCHAR(15)     NOT NULL,
    fecha              DATE            NOT NULL,
    fecha_vencimiento  DATE            NOT NULL,
    monto              DECIMAL(14,2)   NOT NULL DEFAULT 0,
    estado             VARCHAR(15)     NOT NULL DEFAULT 'Vigente',
    id_inmueble        INT             NOT NULL,
    id_cliente         INT             NOT NULL,
    id_usuario         INT             NOT NULL,
    CONSTRAINT pk_reserva PRIMARY KEY (id_reserva),
    CONSTRAINT fk_reserva_inmueble FOREIGN KEY (id_inmueble)
        REFERENCES inmueble (id_inmueble),
    CONSTRAINT fk_reserva_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_cliente),
    CONSTRAINT fk_reserva_usuario FOREIGN KEY (id_usuario)
        REFERENCES usuario (id_usuario),
    CONSTRAINT ck_reserva_tipo CHECK (tipo_reserva IN ('Onerosa', 'Administrativa')),
    CONSTRAINT ck_reserva_estado CHECK (estado IN ('Vigente', 'Vencida', 'Formalizada')),
    CONSTRAINT ck_reserva_fechas CHECK (fecha_vencimiento >= fecha),
    CONSTRAINT ck_reserva_monto CHECK (monto >= 0),
    -- Una reserva administrativa registra el monto en cero (CU09, flujo 2a)
    CONSTRAINT ck_reserva_administrativa CHECK (tipo_reserva = 'Onerosa' OR monto = 0)
) ENGINE = InnoDB;

-- -------------------------------------------------------------
-- Tabla pago (Pago) - composición con reserva
-- -------------------------------------------------------------
CREATE TABLE pago (
    id_pago          INT             NOT NULL AUTO_INCREMENT,
    fecha            DATE            NOT NULL,
    monto            DECIMAL(14,2)   NOT NULL,
    medio_pago       VARCHAR(20)     NOT NULL,
    nro_comprobante  VARCHAR(30)     NOT NULL,
    id_reserva       INT             NOT NULL,
    CONSTRAINT pk_pago PRIMARY KEY (id_pago),
    CONSTRAINT fk_pago_reserva FOREIGN KEY (id_reserva)
        REFERENCES reserva (id_reserva) ON DELETE CASCADE,
    CONSTRAINT uq_pago_comprobante UNIQUE (nro_comprobante),
    CONSTRAINT ck_pago_monto CHECK (monto > 0),
    CONSTRAINT ck_pago_medio CHECK (medio_pago IN ('Efectivo', 'Transferencia', 'Tarjeta', 'Cheque'))
) ENGINE = InnoDB;

-- -------------------------------------------------------------
-- Tabla contrato (Contrato)
-- -------------------------------------------------------------
CREATE TABLE contrato (
    id_contrato    INT             NOT NULL AUTO_INCREMENT,
    tipo_contrato  VARCHAR(10)     NOT NULL,
    fecha_inicio   DATE            NOT NULL,
    fecha_fin      DATE            NULL,       -- NULL en contratos de venta
    monto          DECIMAL(14,2)   NOT NULL,
    estado         VARCHAR(15)     NOT NULL DEFAULT 'Vigente',
    id_reserva     INT             NOT NULL,
    id_usuario     INT             NOT NULL,
    CONSTRAINT pk_contrato PRIMARY KEY (id_contrato),
    CONSTRAINT fk_contrato_reserva FOREIGN KEY (id_reserva)
        REFERENCES reserva (id_reserva),
    CONSTRAINT fk_contrato_usuario FOREIGN KEY (id_usuario)
        REFERENCES usuario (id_usuario),
    -- Cada reserva se formaliza en un único contrato (multiplicidad 0..1)
    CONSTRAINT uq_contrato_reserva UNIQUE (id_reserva),
    CONSTRAINT ck_contrato_tipo CHECK (tipo_contrato IN ('Alquiler', 'Venta')),
    CONSTRAINT ck_contrato_estado CHECK (estado IN ('Vigente', 'Finalizado', 'Rescindido')),
    CONSTRAINT ck_contrato_fechas CHECK (fecha_fin IS NULL OR fecha_fin > fecha_inicio),
    CONSTRAINT ck_contrato_monto CHECK (monto > 0)
) ENGINE = InnoDB;

-- Índice para acelerar la consulta de vencimientos (CU11)
CREATE INDEX ix_contrato_fecha_fin ON contrato (estado, fecha_fin);

-- -------------------------------------------------------------
-- Tabla parte_contrato (ParteContrato) - composición con contrato
-- -------------------------------------------------------------
CREATE TABLE parte_contrato (
    id_contrato  INT           NOT NULL,
    id_cliente   INT           NOT NULL,
    rol          VARCHAR(15)   NOT NULL,
    CONSTRAINT pk_parte_contrato PRIMARY KEY (id_contrato, id_cliente),
    CONSTRAINT fk_parte_contrato FOREIGN KEY (id_contrato)
        REFERENCES contrato (id_contrato) ON DELETE CASCADE,
    CONSTRAINT fk_parte_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_cliente),
    CONSTRAINT ck_parte_rol CHECK (rol IN ('Locador', 'Locatario', 'Vendedor', 'Comprador', 'Garante'))
) ENGINE = InnoDB;
