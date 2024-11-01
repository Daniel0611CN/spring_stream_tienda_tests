
-- Test 1
SELECT p.nombre, p.precio FROM producto p;

-- Test 2
SELECT p.codigo, p.nombre, p.precio*1.08 AS precio, p.codigo_fabricante FROM producto p;

-- Test 3
SELECT UPPER(p.nombre), p.precio FROM producto p;

-- Test 4
SELECT p.nombre, SUBSTRING(UPPER(p.nombre), 1, 2) AS subnombre FROM producto p;

-- Test 5
SELECT DISTINCT p.codigo_fabricante FROM producto p;

-- Test 6
SELECT f.nombre FROM fabricante f ORDER BY nombre DESC;

-- Test 7
SELECT p.nombre, p.precio FROM producto p ORDER BY p.nombre ASC, p.precio DESC;

-- Test 8
SELECT * FROM producto p LIMIT 5;

-- Test 9
SELECT * FROM fabricante limit 2 offset 3;

-- Test 10
SELECT p.nombre, p.precio FROM producto p WHERE precio = (SELECT MIN(precio) FROM producto);

-- Test 11
SELECT p.nombre, p.precio FROM producto p WHERE precio = (SELECT MAX(precio) FROM producto);

-- Test 12
SELECT p.nombre FROM producto p WHERE p.codigo_fabricante = 2;

-- Test 13
SELECT p.nombre FROM producto p WHERE p.precio <= 120;

-- Test 14
SELECT * FROM producto p WHERE p.precio >= 400;

-- Test 15
SELECT * FROM producto p WHERE p.precio BETWEEN 80 AND 300;

-- Test 16
SELECT * FROM producto p WHERE p.precio > 200 AND p.codigo_fabricante = 6;

-- Test 17
SELECT * FROM producto p WHERE p.codigo_fabricante = 1 OR p.codigo_fabricante = 3 OR p.codigo_fabricante = 5;

-- Test 18
SELECT p.nombre, p.precio*100 AS precio FROM producto p;

-- Test 19
SELECT f.nombre FROM fabricante f WHERE nombre LIKE 's%';

-- Test 20
SELECT * FROM producto p WHERE p.nombre LIKE '%Port_til%'; -- El guion bajo, reemplaza a cualquier caracter que venga;

-- Test 21
SELECT * FROM producto p WHERE p.nombre LIKE 'Monitor' AND p.precio < 125;

-- Test 22
SELECT p.nombre, p.precio FROM producto p WHERE p.precio >= 180 ORDER BY precio DESC, p.nombre ASC;

-- Test 27
SELECT p.nombre, p.precio, f.nombre FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo WHERE p.precio >= 180 ORDER BY p.precio DESC, p.nombre ASC;