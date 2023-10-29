import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Main {
    public static void main(String[] args) {
        String archivocsv = "C:/Users/Miguel/Downloads/student-scores.csv"; //Crear variable con la ruta del archivo csv

        // Leer el archivo csv
        try (BufferedReader br = new BufferedReader(new FileReader(archivocsv))) { // Crear un objeto BufferedReader para leer el archivo
            List<Estudiante> lista_estudiantes = br.lines() // Obtener un Stream de las líneas del archivo
                    .skip(1) // Saltar la primera línea (encabezado)
                    .map(line -> { // Convertir cada línea en un objeto Estudiante
                        String[] data = line.split(","); // Separar los datos de la línea por comas
                        if (data.length >= 6) { // Verificar que la línea contenga todos los datos necesarios
                            int id = Integer.parseInt(data[0]); // Convertir el id a entero
                            String first_name = data[1]; // Obtener el nombre
                            String last_name = data[2]; // Obtener el apellido
                            String gender = data[3]; // Obtener el género
                            String career_aspiration = data[4]; // Obtener la carrera de aspiración
                            int math_score = Integer.parseInt(data[5]); // Convertir el puntaje en matemáticas a entero
                            return new Estudiante(id, first_name, last_name, gender, career_aspiration, math_score); // Crear un objeto Estudiante con los datos
                        } else {
                            // Manejo de filas que no contienen todos los datos necesarios
                            // Por ejemplo, devolver null o un objeto especial para filtrar luego.
                            return null;
                        }
                    })
                    .filter(student -> student != null) // Filtrar filas que no contienen todos los datos
                    .collect(Collectors.toList()); // Convertir el Stream en una lista

            // 1. Aspirantes por carrera
            Map<String, Long> aspirantesPorCarrera = lista_estudiantes.stream() // Obtener un Stream de los estudiantes
                    .collect(Collectors.groupingBy(
                            Estudiante::getCareer_aspiration, // Agrupar por la carrera de aspiración
                            Collectors.counting() // Contar el número de estudiantes en cada grupo
                    ));

            System.out.println("\nCantidad de Aspirantes por Carrera:");

            aspirantesPorCarrera.forEach((carrera, cantidad) -> { // Iterar sobre cada grupo
                System.out.println("Carrera: " + carrera + ", Cantidad de Aspirantes: " + cantidad); // Imprimir el nombre de la carrera y la cantidad de estudiantes en el grupo
            });


            // 2. Total de mujeres por carrera
            Map<String, Long> totalMujeresPorCarrera = lista_estudiantes.stream() // Obtener un Stream de los estudiantes
                    .filter(estudiante -> "female".equals(estudiante.getGender())) // Filtrar solo a las estudiantes mujeres
                    .collect(Collectors.groupingBy(
                            Estudiante::getCareer_aspiration, // Agrupar por la carrera de aspiración
                            Collectors.counting() // Contar el número de estudiantes en cada grupo
                    ));

            System.out.println("\nCantidad de Mujeres por Carrera:");

            totalMujeresPorCarrera.forEach((carrera, cantidad) -> { // Iterar sobre cada grupo
                System.out.println("Carrera: " + carrera + ", Total de Mujeres: " + cantidad); // Imprimir el nombre de la carrera y la cantidad de estudiantes en el grupo
            });


            // 3. Total de hombres por carrera
            Map<String, Long> totalHombresPorCarrera = lista_estudiantes.stream() // Obtener un Stream de los estudiantes
                    .filter(estudiante -> "male".equals(estudiante.getGender())) // Filtrar solo a los estudiantes hombres
                    .collect(Collectors.groupingBy(
                            Estudiante::getCareer_aspiration, // Agrupar por la carrera de aspiración
                            Collectors.counting() // Contar el número de estudiantes en cada grupo
                    ));

            System.out.println("\nCantidad de Hombres por Carrera:");

            totalHombresPorCarrera.forEach((carrera, cantidad) -> { // Iterar sobre cada grupo
                System.out.println("Carrera: " + carrera + ", Total de Hombres: " + cantidad); // Imprimir el nombre de la carrera y la cantidad de estudiantes en el grupo
            });


            // 4. Estudiante con el puntaje más alto por carrera
            Map<String, Optional<Estudiante>> estudianteConPuntajeMasAltoPorCarrera = lista_estudiantes.stream() // Obtener un Stream de los estudiantes
                    .collect(Collectors.groupingBy(
                            Estudiante::getCareer_aspiration, // Agrupar por la carrera de aspiración
                            Collectors.maxBy(Comparator.comparingDouble(Estudiante::getMath_score)) // Encontrar al estudiante con el puntaje más alto
                    ));

            System.out.println("\nEstudiantes con el Promedio mas Alto por Carrera:");

            estudianteConPuntajeMasAltoPorCarrera.forEach((carrera, estudianteOptional) -> { // Iterar sobre cada grupo
                if (estudianteOptional.isPresent()) { // Verificar que el estudiante con el puntaje más alto exista
                    Estudiante estudiante = estudianteOptional.get(); // Obtener el estudiante
                    System.out.println("Carrera: " + carrera + ", Estudiante con el puntaje más alto: " + estudiante.getFirst_name() + " " + estudiante.getLast_name() + " (Puntaje: " + estudiante.getMath_score() + ")");
                } else {
                    System.out.println("Carrera: " + carrera + ", No hay estudiantes en esta carrera.");
                }
            });

            System.out.println("\nEstudiante con el Promedio mas Alto:");


            // 5. Estudiante con el puntaje más alto de todos
            Optional<Estudiante> estudianteConPuntajeMasAlto = lista_estudiantes.stream() // Obtener un Stream de los estudiantes
                    .max(Comparator.comparingDouble(Estudiante::getMath_score)); // Encontrar al estudiante con el puntaje más alto

            if (estudianteConPuntajeMasAlto.isPresent()) { // Verificar que el estudiante con el puntaje más alto exista
                Estudiante estudiante = estudianteConPuntajeMasAlto.get(); // Obtener el estudiante
                System.out.println("Estudiante con el puntaje más alto: " + estudiante.getFirst_name() + " " + estudiante.getLast_name() + " (Puntaje: " + estudiante.getMath_score() + ")");
            } else {
                System.out.println("No se encontró a ningún estudiante en la lista.");
            }


            // 6. Puntaje Promedio (math_score) por carrera
            Map<String, Double> puntajePromedioPorCarrera = lista_estudiantes.stream() // Obtener un Stream de los estudiantes
                    .collect(Collectors.groupingBy(
                            Estudiante::getCareer_aspiration, // Agrupar por la carrera de aspiración
                            Collectors.averagingDouble(Estudiante::getMath_score) // Calcular el promedio del puntaje en matemáticas
                    ));

            System.out.println("\nPuntajes Promedio de Matematicas por Carrera:");

            puntajePromedioPorCarrera.forEach((carrera, promedio) -> { // Iterar sobre cada grupo
                System.out.println("Carrera: " + carrera + ", Puntaje Promedio en Matemáticas: " + promedio); // Imprimir el nombre de la carrera y el promedio del puntaje en matemáticas
            });


        } catch (IOException e) { // Manejo de excepciones de lectura de archivos
            e.printStackTrace(); // Imprimir el error que ocurrió al leer el archivo csv
        }



    }
}