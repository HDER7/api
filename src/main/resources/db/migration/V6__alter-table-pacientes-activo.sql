ALTER TABLE pacientes add activo tinyint;
UPDATE pacientes set activo = 1;