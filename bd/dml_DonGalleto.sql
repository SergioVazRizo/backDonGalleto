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
    
    
    

SELECT * FROM vw_cookie_details;
SELECT * FROM  ingredients;
SELECT * FROM  recipes;
