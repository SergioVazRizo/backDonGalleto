INSERT INTO recipes (name, yield, instructions)
VALUES 
('Sorpresa Nuez Don Galleto', 750, '1) Tamiza la harina con sal y polvo para hornear. 2) Bate la mantequilla con azúcar hasta que quede cremoso. 3) Incorpora leche y vainilla. 4) Agrega nueces y forma bolitas. 5) Hornea.'),
('ChocoChip Recipe', 30, '1) Mezclar ingredientes secos. 2) Añadir mantequilla y huevo. 3) Incorporar chispas de chocolate. 4) Hornear.');

INSERT INTO ingredients (name, stock, unit, minimum_stock, cost)
VALUES 
('Harina de trigo', 10000, 'gramos', 2000, 16.00),
('Mantequilla sin sal', 5000, 'gramos', 1000, 240.00),
('Azúcar', 5000, 'gramos', 1500, 36.00),
('Nueces troceadas', 2000, 'gramos', 500, 600.00);


INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity)
VALUES 
(1, 1, 5000), -- 5 kg de harina para Sorpresa Nuez Don Galleto
(1, 2, 2500), -- 2.5 kg de mantequilla para Sorpresa Nuez Don Galleto
(1, 3, 2500), -- 2.5 kg de azúcar para Sorpresa Nuez Don Galleto
(1, 4, 1500); -- 1.5 kg de nueces para Sorpresa Nuez Don Galleto


INSERT INTO cookies (name, recipe_id, description, status, stock, weight_per_unit, unit_price, package_500g_price, package_1000g_price, price_per_gram)
VALUES 
('Sorpresa Nuez Don Galleto', 1, 'Galletas con nueces troceadas y esencia de vainilla', 'Existencia', 750, 37, 8.00, 185.00, 370.00, 0.22),
('ChocoChip', 2, 'Deliciosas galletas con chispas de chocolate', 'Agotado', 0, 50.5, 15.99, 75.00, 145.00, 0.32);


INSERT INTO production (cookie_id, production_status, units_produced)
VALUES 
(1, 'lista', 750), -- 750 unidades producidas de Sorpresa Nuez Don Galleto
(2, 'preparacion', 50); -- 50 unidades de ChocoChip en preparación




CREATE OR REPLACE VIEW vw_cookie_details AS
SELECT 
    *
FROM 
    cookies;
    
    

CREATE VIEW recipe_details AS
SELECT 
    r.id AS recipe_id,
    r.name AS recipe_name,
    r.yield AS recipe_yield,
    r.instructions AS recipe_instructions,
    GROUP_CONCAT(i.id) AS ingredient_ids,  -- Concatenar todos los ingredient_id
    GROUP_CONCAT(ri.quantity) AS ingredient_quantities,  -- Concatenar las cantidades
    GROUP_CONCAT(i.name) AS ingredient_names,  -- Concatenar los nombres de los ingredientes
    GROUP_CONCAT(i.stock) AS ingredient_stocks,  -- Concatenar los stocks de los ingredientes
    GROUP_CONCAT(i.unit) AS ingredient_units  -- Concatenar las unidades de los ingredientes
FROM 
    recipes r
JOIN 
    recipe_ingredients ri ON r.id = ri.recipe_id
JOIN 
    ingredients i ON i.id = ri.ingredient_id
GROUP BY 
    r.id, r.name, r.yield, r.instructions;  -- Agrupar por los campos de la receta    
    
    

SELECT * FROM vw_cookie_details;
SELECT * FROM  ingredients;
SELECT * FROM  recipes;


INSERT INTO recipes (id, name, yield, instructions) VALUES
(1, 'Receta Clásica', 1000, 'Instrucciones para la receta clásica'),
(2, 'Receta de Chocolate', 800, 'Instrucciones para la receta de chocolate'),
(3, 'Receta de Nuez', 750, 'Instrucciones para la receta de nuez'),
(4, 'Receta de Avena y Miel', 900, 'Instrucciones para la receta de avena y miel'),
(5, 'Receta Integral', 850, 'Instrucciones para la receta integral'),
(6, 'Receta de Almendra', 700, 'Instrucciones para la receta de almendra'),
(7, 'Receta de Limón', 950, 'Instrucciones para la receta de limón'),
(8, 'Receta de Coco', 880, 'Instrucciones para la receta de coco'),
(9, 'Receta de Frutos Rojos', 800, 'Instrucciones para la receta de frutos rojos'),
(10, 'Receta de Menta', 750, 'Instrucciones para la receta de menta');


INSERT INTO cookies (name, recipe_id, description, status, stock, weight_per_unit, unit_price, package_500g_price, package_1000g_price, price_per_gram) VALUES
('Galleta Clásica Don Galleto', 1, 'Galleta clásica de mantequilla', 'Existencia', 1000, 35, 8.00, 230.00, 460.00, 0.023),
('Galleta de Chocolate Don Galleto', 2, 'Con trozos de chocolate oscuro', 'Existencia', 800, 37, 9.00, 240.00, 480.00, 0.025),
('Galleta Sorpresa Nuez Don Galleto', 3, 'Con trozos de nuez tostada', 'Existencia', 750, 35, 8.50, 235.00, 470.00, 0.024),
('Galleta de Avena y Miel', 4, 'Hechas con avena integral y miel', 'Existencia', 900, 36, 8.20, 233.00, 466.00, 0.024),
('Galleta Integral Don Galleto', 5, 'Con ingredientes 100% integrales', 'Existencia', 850, 34, 7.50, 225.00, 450.00, 0.022),
('Galleta de Almendra', 6, 'Con trozos de almendra crocante', 'Existencia', 700, 35, 9.20, 245.00, 490.00, 0.026),
('Galleta de Limón', 7, 'Con un toque fresco de limón', 'Existencia', 950, 33, 7.80, 228.00, 456.00, 0.023),
('Galleta de Coco', 8, 'Con coco rallado natural', 'Existencia', 880, 34, 8.10, 232.00, 464.00, 0.024),
('Galleta de Frutos Rojos', 9, 'Con arándanos y fresas deshidratadas', 'Existencia', 800, 35, 9.50, 250.00, 500.00, 0.027),
('Galleta de Menta', 10, 'Con un toque refrescante de menta', 'Existencia', 750, 36, 8.90, 240.00, 480.00, 0.025);


INSERT INTO cookies (name, recipe_id, description, status, stock, weight_per_unit, unit_price, package_500g_price, package_1000g_price, price_per_gram) VALUES
('Galleta Clásica Don Galleto', 1, 'Galleta clásica de mantequilla', 'Existencia', 1000, 35, 8.00, 230.00, 460.00, 0.023),
('Galleta de Chocolate Don Galleto', 2, 'Con trozos de chocolate oscuro', 'Existencia', 800, 37, 9.00, 240.00, 480.00, 0.025),
('Galleta Sorpresa Nuez Don Galleto', 3, 'Con trozos de nuez tostada', 'Existencia', 750, 35, 8.50, 235.00, 470.00, 0.024),
('Galleta de Avena y Miel', 4, 'Hechas con avena integral y miel', 'Existencia', 900, 36, 8.20, 233.00, 466.00, 0.024),
('Galleta Integral Don Galleto', 5, 'Con ingredientes 100% integrales', 'Existencia', 850, 34, 7.50, 225.00, 450.00, 0.022),
('Galleta de Almendra', 6, 'Con trozos de almendra crocante', 'Existencia', 700, 35, 9.20, 245.00, 490.00, 0.026),
('Galleta de Limón', 7, 'Con un toque fresco de limón', 'Existencia', 950, 33, 7.80, 228.00, 456.00, 0.023),
('Galleta de Coco', 8, 'Con coco rallado natural', 'Existencia', 880, 34, 8.10, 232.00, 464.00, 0.024),
('Galleta de Frutos Rojos', 9, 'Con arándanos y fresas deshidratadas', 'Existencia', 800, 35, 9.50, 250.00, 500.00, 0.027),
('Galleta de Menta', 10, 'Con un toque refrescante de menta', 'Existencia', 750, 36, 8.90, 240.00, 480.00, 0.025);

INSERT INTO ingredients (id, name, stock, unit, minimum_stock, cost, expiration_date) VALUES
(1, 'Harina de trigo', 100.0, 'kg', 5.0, 16.0, '2024-12-31'),
(2, 'Mantequilla sin sal', 50.0, 'kg', 2.5, 240.0, '2024-11-30'),
(3, 'Azúcar', 50.0, 'kg', 2.5, 36.0, '2024-12-31'),
(4, 'Leche', 10.0, 'litro', 1.0, 25.0, '2024-10-15'),
(5, 'Sal', 2.0, 'kg', 0.05, 60.0, '2025-01-01'),
(6, 'Polvo para hornear', 1.0, 'kg', 0.05, 200.0, '2025-06-01'),
(7, 'Esencia de vainilla', 0.5, 'litro', 0.02, 1000.0, '2025-12-01'),
(8, 'Chocolate oscuro', 20.0, 'kg', 1.5, 300.0, '2024-10-30'),
(9, 'Nueces troceadas', 15.0, 'kg', 1.5, 600.0, '2024-09-30'),
(10, 'Avena integral', 25.0, 'kg', 1.0, 150.0, '2024-12-15'),
(11, 'Harina integral', 30.0, 'kg', 1.0, 20.0, '2024-11-10'),
(12, 'Almendras', 15.0, 'kg', 1.5, 500.0, '2024-12-05'),
(13, 'Esencia de limón', 0.5, 'litro', 0.02, 1200.0, '2025-08-15'),
(14, 'Coco rallado', 10.0, 'kg', 1.0, 250.0, '2025-01-01'),
(15, 'Frutos rojos deshidratados', 8.0, 'kg', 1.2, 800.0, '2025-03-01'),
(16, 'Extracto de menta', 0.5, 'litro', 0.02, 1500.0, '2025-09-01');


-- Ingredientes comunes
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity) VALUES
(1, 1, 5.0), -- Harina
(1, 2, 2.5), -- Mantequilla
(1, 3, 2.5), -- Azúcar
(1, 4, 1.0), -- Leche
(1, 5, 0.05), -- Sal
(1, 6, 0.05), -- Polvo para hornear
(1, 7, 0.02), -- Esencia de vainilla

-- Variaciones por tipo de galleta
(2, 8, 1.5), -- Chocolate para galleta de chocolate
(3, 9, 1.5), -- Nueces para galleta de nuez
(4, 10, 1.0), -- Avena para galleta de avena
(5, 11, 1.0), -- Harina integral para galleta integral
(6, 12, 1.5), -- Almendras para galleta de almendra
(7, 13, 0.02), -- Esencia de limón para galleta de limón
(8, 14, 1.0), -- Coco rallado para galleta de coco
(9, 15, 1.2), -- Frutos rojos para galleta de frutos rojos
(10, 16, 0.02); -- Extracto de menta para galleta de menta
