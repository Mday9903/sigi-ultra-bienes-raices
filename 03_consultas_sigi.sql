-- SIGI - Sistema Integral de Gestión Inmobiliaria
-- Script 3: Consultas SQL

USE sigi;


-- Consulta 1 - CU07 Consultar Inmuebles (RFS07)
-- Inmuebles disponibles para alquiler, con sus propietarios.
SELECT  i.id_inmueble,
        i.direccion,
        i.tipo_inmueble,
        i.precio,
        GROUP_CONCAT(CONCAT(c.apellido, ', ', c.nombre) SEPARATOR ' / ') AS propietarios
FROM    inmueble i
        INNER JOIN inmueble_propietario ip ON ip.id_inmueble = i.id_inmueble
        INNER JOIN cliente c               ON c.id_cliente   = ip.id_cliente
WHERE   i.tipo_operacion = 'Alquiler'
  AND   i.estado = 'Disponible'
GROUP BY i.id_inmueble, i.direccion, i.tipo_inmueble, i.precio
ORDER BY i.precio;


-- Consulta 2 - CU04 Buscar Cliente (RFS04)
-- Búsqueda flexible por nombre, apellido o DNI.

SET @criterio = 'López' COLLATE utf8mb4_spanish_ci;

SELECT  id_cliente, dni_cliente, apellido, nombre, telefono, email
FROM    cliente
WHERE   nombre      LIKE CONCAT('%', @criterio, '%')
   OR   apellido    LIKE CONCAT('%', @criterio, '%')
   OR   dni_cliente LIKE CONCAT('%', @criterio, '%')
ORDER BY apellido, nombre;


-- Consulta 3 - CU11 Consultar Vencimientos (RFS11)
-- Contratos vigentes que finalizan en los próximos 60 días.

SELECT  co.id_contrato,
        i.direccion                           AS inmueble,
        CONCAT(cl.apellido, ', ', cl.nombre)  AS locatario,
        cl.telefono,
        co.fecha_fin,
        DATEDIFF(co.fecha_fin, CURDATE())     AS dias_restantes
FROM    contrato co
        INNER JOIN reserva r         ON r.id_reserva   = co.id_reserva
        INNER JOIN inmueble i        ON i.id_inmueble  = r.id_inmueble
        INNER JOIN parte_contrato pc ON pc.id_contrato = co.id_contrato
                                    AND pc.rol = 'Locatario'
        INNER JOIN cliente cl        ON cl.id_cliente  = pc.id_cliente
WHERE   co.estado = 'Vigente'
  AND   co.fecha_fin BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 60 DAY)
ORDER BY co.fecha_fin;


-- Consulta 4 - CU12 Registrar Pago (RFS12)
-- Total abonado y saldo pendiente de cada reserva onerosa vigente.
-- El saldo no se almacena: se calcula.

SELECT  r.id_reserva,
        i.direccion                          AS inmueble,
        CONCAT(c.apellido, ', ', c.nombre)   AS cliente,
        r.monto                              AS monto_pactado,
        COALESCE(SUM(p.monto), 0)            AS total_abonado,
        r.monto - COALESCE(SUM(p.monto), 0)  AS saldo_pendiente
FROM    reserva r
        INNER JOIN inmueble i ON i.id_inmueble = r.id_inmueble
        INNER JOIN cliente c  ON c.id_cliente  = r.id_cliente
        LEFT  JOIN pago p     ON p.id_reserva  = r.id_reserva
WHERE   r.tipo_reserva = 'Onerosa'
  AND   r.estado = 'Vigente'
GROUP BY r.id_reserva, i.direccion, c.apellido, c.nombre, r.monto;


-- Consulta 5 - CU08 Coordinar Visita (RFS08)
-- Agenda de visitas programadas, con inmueble, cliente y agente.

SELECT  v.fecha_hora,
        i.direccion                          AS inmueble,
        CONCAT(c.apellido, ', ', c.nombre)   AS cliente,
        c.telefono,
        u.nombre                             AS agente
FROM    visita v
        INNER JOIN inmueble i ON i.id_inmueble = v.id_inmueble
        INNER JOIN cliente c  ON c.id_cliente  = v.id_cliente
        INNER JOIN usuario u  ON u.id_usuario  = v.id_usuario
WHERE   v.estado = 'Programada'
  AND   v.fecha_hora >= NOW()
ORDER BY v.fecha_hora;


-- Consulta 6 - Información para la gerencia (objetivo del sistema)
-- Disponibilidad de la cartera: cantidad de inmuebles por tipo de operación y estado.

SELECT  tipo_operacion,
        estado,
        COUNT(*) AS cantidad
FROM    inmueble
GROUP BY tipo_operacion, estado
ORDER BY tipo_operacion, estado;
