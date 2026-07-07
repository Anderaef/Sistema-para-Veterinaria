

-- 1. TABLA USUARIOS
CREATE TABLE usuarios (
    id_usuario SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('admin','veterinario','recepcionista')),
    creado_por VARCHAR(50) NOT NULL,
    creado_fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modificado_por VARCHAR(50),
    modificado_fecha TIMESTAMP,
    eliminado_por VARCHAR(50),
    eliminado_fecha TIMESTAMP,
    estado BOOLEAN DEFAULT TRUE
);

-- 2. TABLA CLIENTES
CREATE TABLE clientes (
    id_cliente SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    creado_por VARCHAR(50) NOT NULL,
    creado_fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modificado_por VARCHAR(50),
    modificado_fecha TIMESTAMP,
    eliminado_por VARCHAR(50),
    eliminado_fecha TIMESTAMP,
    estado BOOLEAN DEFAULT TRUE
);

-- 3. TABLA MASCOTAS
CREATE TABLE mascotas (
    id_mascota SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    especie VARCHAR(30),
    raza VARCHAR(50),
    edad INT CHECK (edad >= 0),
    id_cliente INT REFERENCES clientes(id_cliente) ON DELETE CASCADE,
    creado_por VARCHAR(50) NOT NULL,
    creado_fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modificado_por VARCHAR(50),
    modificado_fecha TIMESTAMP,
    eliminado_por VARCHAR(50),
    eliminado_fecha TIMESTAMP,
    estado BOOLEAN DEFAULT TRUE
);

-- 4. TABLA CITAS
CREATE TABLE citas (
    id_cita SERIAL PRIMARY KEY,
    id_mascota INT REFERENCES mascotas(id_mascota) ON DELETE CASCADE,
    id_veterinario INT REFERENCES usuarios(id_usuario),
    fecha_hora TIMESTAMP NOT NULL,
    motivo VARCHAR(200),
    estado_cita VARCHAR(20) NOT NULL DEFAULT 'PROGRAMADA'
        CHECK (estado_cita IN (
            'PROGRAMADA',
            'COMPLETADA',
            'CANCELADA',
            'NO COMPLETADA'
        )),
    creado_por VARCHAR(50) NOT NULL,
    creado_fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modificado_por VARCHAR(50),
    modificado_fecha TIMESTAMP,
    eliminado_por VARCHAR(50),
    eliminado_fecha TIMESTAMP,
    estado BOOLEAN DEFAULT TRUE
);

-- 5. TABLA HISTORIAL CLÍNICO 
CREATE TABLE historial_clinico (
    id_historial SERIAL PRIMARY KEY,
    id_mascota INT REFERENCES mascotas(id_mascota) ON DELETE CASCADE,
    id_cita INT NOT NULL REFERENCES citas(id_cita) ON DELETE CASCADE,
    diagnostico TEXT NOT NULL,
    tratamiento TEXT,
    fecha_consulta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    aplico_inyeccion BOOLEAN DEFAULT FALSE,
    receto_medicinas BOOLEAN DEFAULT FALSE,
    detalle_insumos TEXT,
    creado_por VARCHAR(50) NOT NULL,
    estado BOOLEAN DEFAULT TRUE
);

-- 6. TABLA FACTURACIÓN
CREATE TABLE facturacion (
    id_factura SERIAL PRIMARY KEY,
    id_cita INT REFERENCES citas(id_cita),
    monto DECIMAL(10,2) CHECK (monto >= 0),
    monto_consulta DECIMAL(10,2) DEFAULT 20.00,
    monto_inyecciones DECIMAL(10,2) DEFAULT 0.00,
    monto_medicinas DECIMAL(10,2) DEFAULT 0.00,
    fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    creado_por VARCHAR(50) NOT NULL,
    estado BOOLEAN DEFAULT TRUE
);

-- 7. USUARIO INICIAL
INSERT INTO usuarios (username, password, nombre, apellido, rol, creado_por)
VALUES ('Ander', 'aaef2003', 'Anderson', 'Espinosa', 'admin', 'sistema');

-- 8. VERIFICACIÓN
SELECT * FROM usuarios;