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
SELECT p.nombre, p.precio FROM producto p ORDER BY p.nombre, p.precio DESC;

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
SELECT p.nombre, p.precio FROM producto p WHERE p.precio >= 180 ORDER BY precio DESC, p.nombre;

-- Test 23
SELECT p.nombre, p.precio, f.nombre FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo ORDER BY f.nombre;

-- Test 24
SELECT p.nombre, p.precio, f.nombre FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo ORDER BY p.precio DESC LIMIT 1;

-- Test 25
SELECT * FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo WHERE f.nombre = 'Crucial' AND p.precio >= 200;

-- Test 26
SELECT * FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo WHERE f.nombre = 'Asus' OR f.nombre = 'Hewlett-Packard' OR f.nombre = 'Seagate';

 -- Test 27
SELECT p.nombre, p.precio, f.nombre FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo WHERE p.precio >= 180 ORDER BY p.precio DESC, p.nombre;

-- Test 28
SELECT f.nombre, GROUP_CONCAT(p.nombre ORDER BY p.nombre) FROM fabricante f JOIN producto p ON f.codigo = p.codigo_fabricante GROUP BY f.codigo ORDER BY f.codigo;

-- Test 29
SELECT f.nombre AS fabricante FROM fabricante f LEFT JOIN producto p ON f.codigo = p.codigo_fabricante WHERE p.codigo IS NULL;

-- Test 30
SELECT COUNT(*) AS total_productos FROM producto;

-- Test 31
SELECT COUNT(DISTINCT f.codigo) AS fabricantes_con_productos FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo;

-- Test 32
SELECT AVG(p.precio) AS media_precio FROM producto p;

-- Test 33
SELECT MIN(p.precio) AS precio_minimo FROM producto p;

-- Test 34
SELECT SUM(p.precio) AS suma_precio FROM producto p;

-- Test 35
SELECT COUNT(*) AS productos_asus FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo WHERE f.nombre = 'Asus';

-- Test 36
SELECT AVG(p.precio) AS media_precio_asus FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo WHERE f.nombre = 'Asus';

-- Test 37
SELECT
    MIN(p.precio) AS precio_minimo,
    MAX(p.precio) AS precio_maximo,
    AVG(p.precio) AS precio_medio,
    COUNT(*) AS total_productos
FROM producto p JOIN fabricante f ON p.codigo_fabricante = f.codigo WHERE f.nombre = 'Crucial';

-- Test 38
SELECT f.nombre AS fabricante, COUNT(p.codigo) AS num_productos FROM fabricante f
LEFT JOIN producto p ON f.codigo = p.codigo_fabricante GROUP BY f.codigo ORDER BY f.codigo;

-- Test 39
SELECT f.nombre AS fabricante,
       MAX(p.precio) AS max_precio,
       MIN(p.precio) AS min_precio,
       AVG(p.precio) AS precio_medio
FROM fabricante f LEFT JOIN producto p ON f.codigo = p.codigo_fabricante GROUP BY f.codigo;

-- Test 40
SELECT f.codigo        AS codigo_fabricante,
       MAX(p.precio)   AS max_precio,
       MIN(p.precio)   AS min_precio,
       AVG(p.precio)   AS precio_medio,
       COUNT(p.codigo) AS num_productos
FROM fabricante f
         LEFT JOIN producto p ON f.codigo = p.codigo_fabricante
GROUP BY f.codigo
HAVING AVG(p.precio) > 200;

-- Test 41
SELECT f.nombre AS fabricante FROM fabricante f JOIN producto p ON f.codigo = p.codigo_fabricante
GROUP BY f.codigo HAVING COUNT(p.codigo) >= 2;

-- Test 42
SELECT f.nombre AS fabricante, COUNT(p.codigo) AS num_productos FROM fabricante f
JOIN producto p ON f.codigo = p.codigo_fabricante WHERE p.precio >= 220
GROUP BY f.codigo ORDER BY num_productos DESC;

-- Test 43
SELECT f.nombre AS fabricante FROM fabricante f JOIN producto p ON f.codigo = p.codigo_fabricante
GROUP BY f.codigo HAVING SUM(p.precio) > 1000;

-- Test 44
SELECT f.nombre AS fabricante FROM fabricante f JOIN producto p ON f.codigo = p.codigo_fabricante
GROUP BY f.codigo HAVING SUM(p.precio) > 1000 ORDER BY SUM(p.precio);

-- Test 45
SELECT f.nombre AS fabricante, p.nombre AS producto_mas_caro, p.precio AS precio
FROM fabricante f JOIN producto p ON f.codigo = p.codigo_fabricante
WHERE p.precio = (SELECT MAX(p2.precio) FROM producto p2 WHERE p2.codigo_fabricante = f.codigo)
ORDER BY f.nombre;

-- Test 46
SELECT f.nombre AS fabricante, p.nombre AS producto, p.precio AS precio
FROM fabricante f JOIN producto p ON f.codigo = p.codigo_fabricante
WHERE p.precio >= (SELECT AVG(p2.precio) FROM producto p2 WHERE p2.codigo_fabricante = f.codigo)
ORDER BY f.nombre, p.precio DESC;