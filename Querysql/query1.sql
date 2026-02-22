DROP DATABASE FastFoodBD;
DROP TABLE repartidor;
DROP TABLE pedido;
DROP TABLE entrega;
---------
CREATE DATABASE IF NOT EXISTS FastFoodBD;
USE FastFoodBD;

CREATE TABLE repartidor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

ALTER TABLE repartidor ADD tipo_vehiculo VARCHAR(50);
ALTER TABLE repartidor ADD disponible BOOLEAN DEFAULT TRUE;

CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(150) NOT NULL,
    tipo VARCHAR(30) NOT NULL, -- COMIDA | ENCOMIENDA | EXPRESS
    estado VARCHAR(20) NOT NULL -- PENDIENTE | EN_REPARTO | ENTREGADO
);
ALTER TABLE pedido ADD COLUMN repartidor VARCHAR(100);

CREATE TABLE entrega (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_repartidor INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id),
    FOREIGN KEY (id_repartidor) REFERENCES repartidor(id)
    );
    
    ALTER TABLE entrega ADD COLUMN estado_entrega VARCHAR(20) NOT NULL;
    ALTER TABLE entrega DROP COLUMN fecha, DROP COLUMN hora,
    ADD COLUMN fecha_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP;
    

