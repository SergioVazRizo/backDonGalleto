DROP DATABASE IF EXISTS don_galleto;
CREATE DATABASE don_galleto;
USE don_galleto;

-- Tabla recipes
CREATE TABLE recipes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    yield INT NOT NULL,
    instructions TEXT
);

-- Tabla ingredients
CREATE TABLE ingredients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    stock DOUBLE NOT NULL,
    unit VARCHAR(255),
    minimum_stock DOUBLE,
    cost DOUBLE,
    expiration_date VARCHAR(160)
);

-- Tabla recipe_ingredients (relación muchos a muchos entre recipes e ingredients)
CREATE TABLE recipe_ingredients (
    recipe_id INT,
    ingredient_id INT,
    quantity DOUBLE NOT NULL,
    PRIMARY KEY (recipe_id, ingredient_id),
    FOREIGN KEY (recipe_id) REFERENCES recipes (id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredients (id)
);

-- Tabla cookies
CREATE TABLE cookies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    recipe_id INT,
    description VARCHAR(255),
    status ENUM('Existencia', 'Agotado') NOT NULL,
    stock INT NOT NULL,
    weight_per_unit DOUBLE,
    unit_price DOUBLE,
    package_500g_price DOUBLE,
    package_1000g_price DOUBLE,
    price_per_gram DOUBLE,
    FOREIGN KEY (recipe_id) REFERENCES recipes (id)
);

-- Tabla production
CREATE TABLE production (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cookie_id INT NOT NULL,
    production_status ENUM('preparacion', 'horneado', 'enfriamiento', 'lista') NOT NULL,
    units_produced INT,
    FOREIGN KEY (cookie_id) REFERENCES cookies (id)
);

-- Tabla sales
CREATE TABLE sales (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date varchar(100),
    total DOUBLE NOT NULL
);

-- Tabla sale_items (detalles de los productos vendidos en cada venta)
CREATE TABLE sale_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sale_id INT NOT NULL,
    cookie_id INT NOT NULL,
    quantity INT NOT NULL,
    sale_type ENUM('UNIT', 'WEIGHT', 'PACKAGE_500', 'PACKAGE_1000', 'AMOUNT') NOT NULL,
    total DOUBLE NOT NULL,
    FOREIGN KEY (sale_id) REFERENCES sales (id),
    FOREIGN KEY (cookie_id) REFERENCES cookies (id)
);
