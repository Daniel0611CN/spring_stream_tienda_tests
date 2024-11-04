package org.iesvdm.tienda;

import org.iesvdm.tienda.modelo.Fabricante;
import org.iesvdm.tienda.modelo.Producto;
import org.iesvdm.tienda.repository.FabricanteRepository;
import org.iesvdm.tienda.repository.ProductoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.boot.test.context.SpringBootTest;

import static java.util.Comparator.*;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;


@SpringBootTest
class TiendaApplicationTests {

	@Autowired
	FabricanteRepository fabRepo;
	
	@Autowired
	ProductoRepository prodRepo;
	private PropertyResourceConfigurer propertyResourceConfigurer;

	@Test
	void testAllFabricante() {
		var listFabs = fabRepo.findAll();
		
		listFabs.forEach(f -> {
			System.out.println(">>"+f+ ":");
			f.getProductos().forEach(System.out::println);
		});
	}
	
	@Test
	void testAllProducto() {
		var listProds = prodRepo.findAll();

		listProds.forEach( p -> {
			System.out.println(">>"+p+":"+"\nProductos mismo fabricante "+ p.getFabricante());
			p.getFabricante().getProductos().forEach(pF -> System.out.println(">>>>"+pF));
		});
				
	}

	
	/**
	 * 1. Lista los nombres y los precios de todos los productos de la tabla producto
	 */
	@Test
	void test1() {
		var listProds = prodRepo.findAll();
//		record Producto(String nombre, Double precio) {}
		var result = listProds.stream()
//				.map(p -> new Producto(p.getNombre(), p.getPrecio()))
				.map(p -> p.getNombre() + ", " + p.getPrecio())
				.toList();

		result.forEach(System.out::println);

//		Assertions.assertEquals("Disco duro SATA3 1TB", result.getFirst().nombre);
//		Assertions.assertEquals(86.99, result.getFirst().precio);
		Assertions.assertEquals("Disco duro SATA3 1TB, 86.99", result.getFirst());
	}
	
	
	/**
	 * 2. Devuelve una lista de Producto completa con el precio de euros convertido a dólares .
	 */
	@Test
	void test2() {
		var listProdsPrecEur = prodRepo.findAll();
		var result = listProdsPrecEur.stream()
				.map(p -> {
					Producto pDolar = new Producto();
					pDolar.setNombre(p.getNombre());
					pDolar.setPrecio(p.getPrecio()*1.08);
					pDolar.setCodigo(p.getCodigo());
					pDolar.setFabricante(p.getFabricante());
					return pDolar;
				}).toList();

		result.forEach(System.out::println);

		Assertions.assertTrue(result.getFirst().getPrecio() > listProdsPrecEur.getFirst().getPrecio());
	}
	
	/**
	 * 3. Lista los nombres y los precios de todos los productos, convirtiendo los nombres a mayúscula.
	 */
	@Test
	void test3() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.map(p -> p.getNombre().toUpperCase() + " -> " + p.getPrecio())
				.toList();

		result.forEach(System.out::println);

		Assertions.assertTrue(listProds.size() == result.size());
		Assertions.assertTrue(result.get(0).contains(listProds.get(0).getNombre().toUpperCase()));
	}
	
	/**
	 * 4. Lista el nombre de todos los fabricantes y a continuación en mayúsculas los dos primeros caracteres del nombre del fabricante.
	 */
	@Test
	void test4() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.map(f -> f.getNombre() + " -> " + f.getNombre().substring(0, 2).toUpperCase())
				.toList();

		result.forEach(System.out::println);

		Assertions.assertTrue(listFabs.size() == result.size());
		Assertions.assertTrue(result.get(0).contains(listFabs.get(0).getNombre().substring(0, 2).toUpperCase()));
	}
	
	/**
	 * 5. Lista el código de los fabricantes que tienen productos.
	 */
	@Test
	void test5() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(f -> !f.getProductos().isEmpty())
				.map(Fabricante::getCodigo)
				.toList();

		result.forEach(System.out::println);

		Assertions.assertTrue(result.contains(listFabs.get(0).getCodigo()));
	}
	
	/**
	 * 6. Lista los nombres de los fabricantes ordenados de forma descendente.
	 */
	@Test
	void test6() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.sorted(comparing(Fabricante::getNombre).reversed())
				.toList();

		result.forEach(x -> System.out.println(x.getNombre()));

		Assertions.assertTrue(listFabs.size() == result.size());
		Assertions.assertEquals(listFabs.get(0), result.get(result.size() - 1));
	}
	
	/**
	 * 7. Lista los nombres de los productos ordenados en primer lugar por el nombre de forma ascendente y en segundo lugar por el precio de forma descendente.
	 */
	@Test
	void test7() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.sorted(comparing(Producto::getNombre).thenComparing(Producto::getPrecio, reverseOrder())) // se pone reverseOrder que invierte solo el precio, si ponemos reversed, invierte todo;
				.toList();

		result.forEach(x -> System.out.println(x.getNombre() + " - " + x.getPrecio() + " €"));

		Assertions.assertTrue(listProds.size() == result.size());
		Assertions.assertEquals(result.get(0).getNombre(), "Disco SSD 1 TB");

	}
	
	/**
	 * 8. Devuelve una lista con los 5 primeros fabricantes.
	 */
	@Test
	void test8() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.limit(5)
				.toList();

		result.forEach(System.out::println);

		Assertions.assertEquals(result.size(), 5);
		Assertions.assertTrue(result.get(0).getNombre().equals("Asus"));
	}
	
	/**
	 * 9.Devuelve una lista con 2 fabricantes a partir del cuarto fabricante. El cuarto fabricante también se debe incluir en la respuesta.
	 */
	@Test
	void test9() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.skip(3)
				.limit(2)
				.toList();

		result.forEach(System.out::println);

		Assertions.assertTrue(result.size() == 2);
		Assertions.assertEquals(result.get(0).getNombre(), "Samsung");
	}
	
	/**
	 * 10. Lista el nombre y el precio del producto más barato
	 */
	@Test
	void test10() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.min(comparing(Producto::getPrecio))
				.map(p -> p.getNombre() + " -> " + p.getPrecio())
				.stream().toList();

		result.forEach(System.out::println);

		Assertions.assertEquals(result.size(), 1);
		Assertions.assertTrue(result.get(0).contains("Impresora"));
	}
	
	/**
	 * 11. Lista el nombre y el precio del producto más caro
	 */
	@Test
	void test11() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.max(comparing(Producto::getPrecio))
				.map(p -> p.getNombre() + " -> " + p.getPrecio())
				.stream().toList();

		result.forEach(System.out::println);

		Assertions.assertEquals(result.size(), 1);
		Assertions.assertTrue(result.get(0).contains("GeForce"));
	}
	
	/**
	 * 12. Lista el nombre de todos los productos del fabricante cuyo código de fabricante es igual a 2.
	 * 
	 */
	@Test
	void test12() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(f -> f.getFabricante().getCodigo() == 2)
				.map(Producto::getNombre)
				.toList();

		result.forEach(System.out::println);

		Assertions.assertEquals(result.size(), 2);
		Assertions.assertTrue(result.get(0).contains("Portátil"));
	}
	
	/**
	 * 13. Lista el nombre de los productos que tienen un precio menor o igual a 120€.
	 */
	@Test
	void test13() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getPrecio() <= 120)
				.map(Producto::getNombre)
				.toList();

		result.forEach(System.out::println);

		Assertions.assertEquals(result.size(), 3);
	}
	
	/**
	 * 14. Lista los productos que tienen un precio mayor o igual a 400€.
	 */
	@Test
	void test14() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getPrecio() >= 400)
				.toList();

		result.forEach(System.out::println);

		Assertions.assertTrue(result.get(0).getPrecio() >= 400);
		Assertions.assertTrue(listProds.size() >= result.size());
	}
	
	/**
	 * 15. Lista todos los productos que tengan un precio entre 80€ y 300€. 
	 */
	@Test
	void test15() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getPrecio() >= 80 && p.getPrecio() <= 300)
				.toList();

		result.forEach(System.out::println);

		Assertions.assertTrue(result.get(0).getPrecio() >= 80 && result.get(0).getPrecio() <= 300);
		Assertions.assertTrue(listProds.size() >= result.size());
		Assertions.assertEquals(result.size(), 7);
	}
	
	/**
	 * 16. Lista todos los productos que tengan un precio mayor que 200€ y que el código de fabricante sea igual a 6.
	 */
	@Test
	void test16() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getPrecio() > 200 && p.getFabricante().getCodigo() == 6)
				.toList();

		result.forEach(System.out::println);

		Assertions.assertTrue(result.get(0).getPrecio() > 200 && result.get(0).getFabricante().getCodigo() == 6);
		Assertions.assertEquals(result.size(), 1);
	}
	
	/**
	 * 17. Lista todos los productos donde el código de fabricante sea 1, 3 o 5 utilizando un Set de codigos de fabricantes para filtrar.
	 */
	@Test
	void test17() {
		var listProds = prodRepo.findAll();

		// Set se refiere a una lista, no un setter;
		Set<Integer> setProductos = Set.of(1, 3, 5);

//		setProductos.add(1);
//		setProductos.add(3);
//		setProductos.add(5);

//        for (Producto listProd : listProds) {
//            if (listProd.getFabricante().getCodigo() == 1 || listProd.getFabricante().getCodigo() == 3 || listProd.getFabricante().getCodigo() == 5) {
//                setProductos.add(listProd.getFabricante().getCodigo());
//            }
//        }

		// Esto no sirve, ya que solo estamos comprobando que contenga ese número, por lo que con un set que tenga 1, 3, 5 nos sirve.
		// Ya que lo que vamos a comprobar para filtrar ese que el set contenga ese código;

		var result = listProds.stream()
				.filter(p -> setProductos.contains(p.getFabricante().getCodigo()))
				.toList();

		result.forEach(System.out::println);

		Assertions.assertEquals(result.size(), 5);
		Assertions.assertTrue(result.get(0).getFabricante().getCodigo() == 5);
	}
	
	/**
	 * 18. Lista el nombre y el precio de los productos en céntimos.
	 */
	@Test
	void test18() {
		var listProds = prodRepo.findAll();
		record Tupla(String nombre, Double precio){} // Los record se utilizan para acceder de manera directa a un atributo;
		var result = listProds.stream()
				.map(p -> new Tupla(p.getNombre(), p.getPrecio()*100))
				.toList();

		// record es un objeto inmutable, por lo que no tiene ni getter ni setter, es por ello que se accede de forma directa;

		result.forEach(System.out::println);

		Assertions.assertTrue(listProds.get(0).getPrecio() < result.get(0).precio);
		Assertions.assertEquals(result.size(), 11);
		Assertions.assertEquals("Disco duro SATA3 1TB", result.getFirst().nombre);
		Assertions.assertEquals(8699.0d, result.getFirst().precio);
	}
	
	
	/**
	 * 19. Lista los nombres de los fabricantes cuyo nombre empiece por la letra S
	 */
	@Test
	void test19() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
//				.filter(f -> f.getNombre().toLowerCase().charAt(0) == 's')
//				.filter(f -> f.getNombre().substring(0, 1).equalsIgnoreCase("s"))
				.filter(f -> f.getNombre().toLowerCase().startsWith("s"))
				.toList();

		result.forEach(x -> System.out.println("Nombre: " + x.getNombre()));

		Assertions.assertTrue(result.get(0).getNombre().toLowerCase().startsWith("s"));
		Assertions.assertEquals(result.size(), 2);
		Assertions.assertEquals(result.get(0).getNombre(), "Samsung");
	}

	/**
	 * 20. Devuelve una lista con los productos que contienen la cadena Portátil en el nombre.
	 */
	@Test
	void test20() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getNombre().matches(".*Port[a|á]til.*"))
				.toList();

		result.forEach(System.out::println);

		Assertions.assertEquals(result.size(), 2);
		Assertions.assertTrue(result.get(0).getNombre().contains("Portátil"));
	}
	
	/**
	 * 21. Devuelve una lista con el nombre de todos los productos que contienen la cadena Monitor en el nombre y tienen un precio inferior a 215 €.
	 */
	@Test
	void test21() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getNombre().contains("Monitor") && p.getPrecio() < 215)
				.toList();

		result.forEach(x -> System.out.println(x.getNombre()));

		Assertions.assertEquals(1, result.size());
	}
	
	/**
	 * 22. Lista el nombre y el precio de todos los productos que tengan un precio mayor o igual a 180€. 
	 * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre (en orden ascendente).
	 */
	@Test
	void test22() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getPrecio() >= 180)
				.sorted(comparing(Producto::getPrecio, reverseOrder()).thenComparing(Producto::getNombre))
				.toList();

		result.forEach(x -> System.out.println(x.getNombre() + " -> " + x.getPrecio() + " €"));

		Assertions.assertTrue(result.getFirst().getPrecio() > result.get(1).getPrecio());
	}
	
	/**
	 * 23. Devuelve una lista con el nombre del producto, precio y nombre de fabricante de todos los productos de la base de datos. 
	 * Ordene el resultado por el nombre del fabricante, por orden alfabético.
	 */
	@Test
	void test23() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.sorted(comparing(p -> p.getFabricante().getNombre()))
				.map(p -> p.getNombre() + " - " + p.getPrecio() + " - "  + p.getFabricante().getNombre())
				.toList();

		result.forEach(System.out::println);

		Assertions.assertEquals(result.size(), listProds.size());
		Assertions.assertTrue(result.get(0).contains("Asus"));
	}
	
	/**
	 * 24. Devuelve el nombre del producto, su precio y el nombre de su fabricante, del producto más caro.
	 */
	@Test
	void test24() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.max(comparing(Producto::getPrecio))
				.map(p -> p.getNombre() + " - " + p.getPrecio() + " - " + p.getFabricante().getNombre())
				.stream().toList();

		result.forEach(System.out::println);

	}
	
	/**
	 * 25. Devuelve una lista de todos los productos del fabricante Crucial que tengan un precio mayor que 200€.
	 */
	@Test
	void test25() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getFabricante().getNombre().equals("Crucial") && p.getPrecio() > 200)
				.toList();

		result.forEach(System.out::println);

		// Assertions;
	}
	
	/**
	 * 26. Devuelve un listado con todos los productos de los fabricantes Asus, Hewlett-Packard y Seagate
	 */
	@Test
	void test26() {
		var listProds = prodRepo.findAll();
		Set<String> nombresFab = Set.of("Asus", "Hewlett-Packard", "Seagate");
		var result = listProds.stream()
				.filter(p -> nombresFab.contains(p.getFabricante().getNombre()))
				.toList();

		result.forEach(System.out::println);
	}
	
	/**
	 * 27. Devuelve un listado con el nombre de producto, precio y nombre de fabricante, de todos los productos que tengan un precio mayor o igual a 180€. 
	 * Ordene el resultado en primer lugar por el precio (en orden descendente) y en segundo lugar por el nombre.
	 * El listado debe mostrarse en formato tabla. Para ello, procesa las longitudes máximas de los diferentes campos a presentar y compensa mediante la inclusión de espacios en blanco.
	 * La salida debe quedar tabulada como sigue:

Producto                Precio             Fabricante
-----------------------------------------------------
GeForce GTX 1080 Xtreme|611.5500000000001 |Crucial
Portátil Yoga 520      |452.79            |Lenovo
Portátil Ideapd 320    |359.64000000000004|Lenovo
Monitor 27 LED Full HD |199.25190000000003|Asus

	 */		
	@Test
	void test27() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getPrecio() >= 180)
				.sorted(comparing(Producto::getPrecio, reverseOrder()).thenComparing(Producto::getNombre))
				.map(p -> p.getNombre() + "|" + p.getPrecio() + "|" + p.getFabricante().getNombre())
				.toList();

		System.out.println("Producto                Precio          Fabricante\n------------------------------------------------");
		result.forEach(System.out::println);

		// Hacer los assertions;
	}
	
	/**
	 * 28. Devuelve un listado de los nombres fabricantes que existen en la base de datos, junto con los nombres productos que tiene cada uno de ellos. 
	 * El listado deberá mostrar también aquellos fabricantes que no tienen productos asociados. 
	 * SÓLO SE PUEDEN UTILIZAR STREAM, NO PUEDE HABER BUCLES
	 * La salida debe queda como sigue:
Fabricante: Asus

            	Productos:
            	Monitor 27 LED Full HD
            	Monitor 24 LED Full HD

Fabricante: Lenovo

            	Productos:
            	Portátil Ideapd 320
            	Portátil Yoga 520

Fabricante: Hewlett-Packard

            	Productos:
            	Impresora HP Deskjet 3720
            	Impresora HP Laserjet Pro M26nw

Fabricante: Samsung

            	Productos:
            	Disco SSD 1 TB

Fabricante: Seagate

            	Productos:
            	Disco duro SATA3 1TB

Fabricante: Crucial

            	Productos:
            	GeForce GTX 1080 Xtreme
            	Memoria RAM DDR4 8GB

Fabricante: Gigabyte

            	Productos:
            	GeForce GTX 1050Ti

Fabricante: Huawei

            	Productos:


Fabricante: Xiaomi

            	Productos:

	 */
	@Test
	void test28() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.map(f -> "\nFabricante: " + f.getNombre()
						+ "\n\n				Productos:\n				"
						+ f.getProductos().stream()
						.map(p -> p.getNombre() + "\n				")
						.collect(Collectors.joining()));

		result.forEach(System.out::println);
	}
	
	/**
	 * 29. Devuelve un listado donde sólo aparezcan aquellos fabricantes que no tienen ningún producto asociado.
	 */
	@Test
	void test29() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(fabricante -> fabricante.getProductos().isEmpty())
				.toList();

		result.forEach(System.out::println);

		Assertions.assertEquals(8, result.get(0).getCodigo());
		Assertions.assertEquals(2, result.size());
	}
	
	/**
	 * 30. Calcula el número total de productos que hay en la tabla productos. Utiliza la api de stream.
	 */
	@Test
	void test30() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream().count();

		System.out.println("En la tabla productos hay: " + result + " productos.");

		Assertions.assertEquals(11, result);
	}

	
	/**
	 * 31. Calcula el número de fabricantes con productos, utilizando un stream de Productos.
	 */
	@Test
	void test31() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.map(Producto::getPrecio)
				.distinct()
				.count();

		System.out.println("Hay " + result + " fabricantes con productos.");

		Assertions.assertEquals(7, result);
	}
	
	/**
	 * 32. Calcula la media del precio de todos los productos
	 */
	@Test
	void test32() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.mapToDouble(Producto::getPrecio)
				.average();

		System.out.println(result.orElse(0.0));

		Assertions.assertEquals(OptionalDouble.of(271.7236363636364), result);
	}
	
	/**
	 * 33. Calcula el precio más barato de todos los productos. No se puede utilizar ordenación de stream.
	 */
	@Test
	void test33() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.mapToDouble(Producto::getPrecio).min();

		System.out.println(result.orElse(0.0));

		Assertions.assertEquals(OptionalDouble.of(59.99), result);
	}
	
	/**
	 * 34. Calcula la suma de los precios de todos los productos.
	 */
	@Test
	void test34() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.mapToDouble(Producto::getPrecio)
				.sum();

		System.out.println(result);

		Assertions.assertEquals(2988.96, result);
	}
	
	/**
	 * 35. Calcula el número de productos que tiene el fabricante Asus.
	 */
	@Test
	void test35() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getFabricante().getNombre().equals("Asus"))
				.count();

		System.out.println(result);

		Assertions.assertEquals(2, result);
	}
	
	/**
	 * 36. Calcula la media del precio de todos los productos del fabricante Asus.
	 */
	@Test
	void test36() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getFabricante().getNombre().equals("Asus"))
				.mapToDouble(Producto::getPrecio)
				.average();

		System.out.println(result);

		Assertions.assertEquals(OptionalDouble.of(223.995), result);
	}
	
	
	/**
	 * 37. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos que tiene el fabricante Crucial. 
	 *  Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
	 */
	@Test
	void test37() {
		var listProds = prodRepo.findAll();
		var result = listProds.stream()
				.filter(p -> p.getFabricante().getNombre().equalsIgnoreCase("Crucial"))
				.map(p -> new Double[]{p.getPrecio(), p.getPrecio(), p.getPrecio(), 1.0})
				.reduce((doubles, doubles2) -> new Double[] {
						Math.min(doubles[0], doubles2[0]), Math.max(doubles[1], doubles2[1]), doubles[2]+doubles2[2], doubles[3]+doubles2[3]})
				.orElse(new Double[]{});

		Double media = result[3]>0 ? result[2]/result[3]: 0.0; // Si el result[3] > 0 pues entonces result[2]/result[3], sino media = 0.0;
		System.out.println("El valor mínimo: " + result[0] + "\nEl valor máximo: " + result[1] + "\nEl valor medio: " + result[2] + "\nNúmero total de valores: " + result[3]);
	}
	
	/**
	 * 38. Muestra el número total de productos que tiene cada uno de los fabricantes. 
	 * El listado también debe incluir los fabricantes que no tienen ningún producto. 
	 * El resultado mostrará dos columnas, una con el nombre del fabricante y otra con el número de productos que tiene. 
	 * Ordene el resultado descendentemente por el número de productos. Utiliza String.format para la alineación de los nombres y las cantidades.
	 * La salida debe queda como sigue:
	 
     Fabricante     #Productos
-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
           Asus              2
         Lenovo              2
Hewlett-Packard              2
        Samsung              1
        Seagate              1
        Crucial              2
       Gigabyte              1
         Huawei              0
         Xiaomi              0

	 */
	@Test
	void test38() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.map(p -> String.format("%15s", p.getNombre()) + String.format("%15s", String.valueOf(p.getProductos().stream().count())))
				.toList();

		System.out.println("     Fabricante     #Productos\n-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
		result.forEach(System.out::println);

		Assertions.assertTrue(result.get(0).contains("2"));
	}
	
	/**
	 * 39. Muestra el precio máximo, precio mínimo y precio medio de los productos de cada uno de los fabricantes. 
	 * El resultado mostrará el nombre del fabricante junto con los datos que se solicitan. Realízalo en 1 solo stream principal. Utiliza reduce con Double[] como "acumulador".
	 * Deben aparecer los fabricantes que no tienen productos.
	 */
	@Test
	void test39() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream();
//				.filter(p -> p.getFabricante().getNombre().equalsIgnoreCase("Crucial"))
//				.map(f -> new Double[]{, 1.0})
//				.reduce((doubles, doubles2) -> new Double[] {
//						Math.min(doubles[0], doubles2[0]), Math.max(doubles[1], doubles2[1]), doubles[2]+doubles2[2], doubles[3]+doubles2[3]})
//				.orElse(new Double[]{});

//		Double media = result[3]>0 ? result[2]/result[3]: 0.0; // Si el result[3] > 0 pues entonces result[2]/result[3], sino media = 0.0;
//		System.out.println("El valor mínimo: " + result[0] + "\nEl valor máximo: " + result[1] + "\nEl valor medio: " + result[2] + "\nNúmero total de valores: " + result[3]);

//				.map(f -> f.getProductos().stream().map(p -> p.getPrecio()).;

		System.out.println(result);


	}
	
	/**
	 * 40. Muestra el precio máximo, precio mínimo, precio medio y el número total de productos de los fabricantes que tienen un precio medio superior a 200€. 
	 * No es necesario mostrar el nombre del fabricante, con el código del fabricante es suficiente.
	 */
	@Test
	void test40() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(f -> f.getProductos().stream().mapToDouble(Producto::getPrecio).average().orElse(0) > 200)
				.map(f -> f.getProductos().stream().map(p -> p.getCodigo() + " -> " + p.getPrecio()))
				.toList();

//		for (int i = 0; i < result.size(); i++) {
//			var max = result;
//		}

		result.forEach(System.out::println);
	}
	
	/**
	 * 41. Devuelve un listado con los nombres de los fabricantes que tienen 2 o más productos.
	 */
	@Test
	void test41() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(f -> f.getProductos().stream().count() >= 2)
				.toList();

		result.forEach(System.out::println);

		Assertions.assertTrue(result.size() <= listFabs.size());
	}
	
	/**
	 * 42. Devuelve un listado con los nombres de los fabricantes y el número de productos que tiene cada uno con un precio superior o igual a 220 €. 
	 * Ordenado de mayor a menor número de productos.
	 */
	@Test
	void test42() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.sorted(comparing(f -> f.getProductos().stream().count(), reverseOrder()))
				.map(f -> f.getNombre() + ", " + f.getProductos().stream().filter(p -> p.getPrecio() >= 220).count())
				.toList();

		result.forEach(System.out::println);
	}
	
	/**
	 * 43.Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
	 */
	@Test
	void test43() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.filter(f -> f.getProductos().stream().mapToDouble(p -> p.getPrecio()).sum() > 1000)
				.map(f -> f.getNombre())
				.toList();

		result.forEach(System.out::println);

		Assertions.assertEquals("Lenovo", result.get(0));
	}
	
	/**
	 * 44. Devuelve un listado con los nombres de los fabricantes donde la suma del precio de todos sus productos es superior a 1000 €
	 * Ordenado de menor a mayor por cuantía de precio de los productos.
	 */
	@Test
	void test44() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.sorted(comparing(f -> f.getProductos().stream().mapToDouble(p -> p.getPrecio()).sum()))
				.filter(f -> f.getProductos().stream().mapToDouble(p -> p.getPrecio()).sum() > 1000)
				.map(f -> f.getNombre())
				.toList();

		result.forEach(System.out::println);

		Assertions.assertTrue(result.get(0).equals("Lenovo"));
		Assertions.assertEquals(1, result.size());
	}

	/**
	 * 45. Devuelve un listado con el nombre del producto más caro que tiene cada fabricante. 
	 * El resultado debe tener tres columnas: nombre del producto, precio y nombre del fabricante. 
	 * El resultado tiene que estar ordenado alfabéticamente de menor a mayor por el nombre del fabricante.
	 */
	@Test
	void test45() {
		var listFabs = fabRepo.findAll();
		var result = listFabs.stream()
				.sorted(comparing(Fabricante::getNombre))
				.map(f -> f.getProductos().stream().max(comparing(Producto::getPrecio)).map(p -> p.getNombre() + ", " + p.getPrecio()) + ", " + f.getNombre())
				.toList();

		result.forEach(System.out::println);

		Assertions.assertTrue(result.get(0).contains("Asus"));
	}
	
	/**
	 * 46. Devuelve un listado de todos los productos que tienen un precio mayor o igual a la media de todos los productos de su mismo fabricante.
	 * Se ordenará por fabricante en orden alfabético ascendente y los productos de cada fabricante tendrán que estar ordenados por precio descendente.
	 */
	@Test
	void test46() {
		var listFabs = fabRepo.findAll();

		var result = listFabs.stream()
				.sorted(comparing(Fabricante::getNombre).thenComparing(f -> f.getProductos().stream().mapToDouble(p -> p.getPrecio()).sum(), reverseOrder()))
				.map(f -> f.getProductos().stream().filter(p -> p.getPrecio() >= p.getFabricante().getProductos().stream().mapToDouble(o -> o.getPrecio()).average().getAsDouble()).toList())
				.toList();

		result.forEach(System.out::println);

		Assertions.assertTrue(result.get(4).isEmpty());
	}

}
