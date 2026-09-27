-- SIGI - Sistema Integral de Gestión Inmobiliaria
-- Script 4: Borrado de registros

USE sigi;


-- Parte A - Borrados puntuales


-- A1. Cancelar una visita programada.
DELETE FROM visita
WHERE  id_visita = 5;

-- A2. Anular una reserva vigente que no se formalizó.
DELETE FROM reserva
WHERE  id_reserva = 7;

UPDATE inmueble
SET    estado = 'Disponible'
WHERE  id_inmueble = 7;

-- A3. Intento de borrar un cliente con operaciones registradas.
--     La base lo rechaza porque la clave foránea no tiene ON DELETE
--     (comportamiento RESTRICT), lo que protege la integridad
--     referencial. Se deja comentado para no interrumpir el script:

-- DELETE FROM cliente WHERE id_cliente = 4;

--   -> ERROR 1451: Cannot delete or update a parent row:
--      a foreign key constraint fails (`sigi`.`parte_contrato`, CONSTRAINT `fk_parte_cliente` FOREIGN KEY (`id_cliente`) REFERENCES `cliente` (`id_cliente`))



-- Parte B - Vaciado completo de la base
/* Se borra en orden inverso a las dependencias de claves foráneas:
 primero las tablas hijas y al final las tablas padre.*/
DELETE FROM parte_contrato;
DELETE FROM contrato;
DELETE FROM pago;
DELETE FROM reserva;
DELETE FROM visita;
DELETE FROM inmueble_propietario;
DELETE FROM inmueble;
DELETE FROM cliente;
DELETE FROM usuario;
