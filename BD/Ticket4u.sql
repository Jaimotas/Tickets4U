
CREATE DATABASE IF NOT EXISTS ticket4u;
USE ticket4u;


CREATE TABLE Usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol ENUM('admin', 'cliente') NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE Evento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_admin INT NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    fecha_inicio DATE NOT NULL,
    ubicacion VARCHAR(200),
    ciudad VARCHAR(100),
    fecha_fin DATE,
    aforo INT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE Pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL UNIQUE,
    id_evento INT NOT NULL UNIQUE,
    total DECIMAL(10, 2) NOT NULL,
    pagado BOOLEAN DEFAULT FALSE,
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE Ticket (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL UNIQUE,
    id_pedido INT NOT NULL UNIQUE,
    id_evento INT NOT NULL UNIQUE,
    qr VARCHAR(255) NOT NULL UNIQUE,
    estado ENUM('activo', 'usado', 'cancelado') DEFAULT 'activo',
    fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE Descuento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL UNIQUE,
    id_pedido INT NOT NULL UNIQUE,
    id_evento INT NOT NULL UNIQUE,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    porcentaje DECIMAL(5, 2) NOT NULL,
    tiempo_expiracion DATETIME NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);