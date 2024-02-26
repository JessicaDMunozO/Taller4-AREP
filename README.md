# Taller 4 - AREP
Para este taller se continuó con la construcción de un servidor web en Java. Dicho servidor es capaz de entregar páginas web e imágenes. Además se incluye el manejo de reflexión con Java
y permite que se cargue un POJO. Para esto se trabajó con anotaciones como `@Component` que aplica en clases y `@GetMapping` que aplica sobre métodos. Por último, el framework explora sobre
el *classpath* en búsqueda de aquellas clases que tienen la anotación `@Component` y carga dicho componente para simplificar el proceso de configuración manual.

## Empezando
Las siguientes instrucciones permiten que obtenga una copia del proyecto en funcionamiento.

### Prerrequisitos
1. Debe tener Maven y JDK para compilar y ejecutar el proyecto.
2. Verificar que el puerto 35000 esté disponible para que el servidor web lo pueda usar sin inconvenientes.
3. Tener conexión a internet.
   
### Instalación
Ahora bien, para obtener el proyecto y ejecutarlo, debe ser descargado en formato zip o puede ser clonado desde el repositorio de GitHub. Con el proyecto en su máquina, debe acceder al 
directorio que contiene el proyecto. Luego, debe descargar las dependencias del proyecto, para esto ejecute el comando `mvn install`. Después, para compilar ejecute el comando `mvn package`
y por último ejecute el comando `java -cp .\target\classes\ edu.escuelaing.arem.ASE.app.MyWebServices`.

## Despliegue
Con el proyecto corriendo debe abrir en un navegador la siguiente dirección: http://localhost:35000/movies.html allí podrá observar el html completo trabajado en talleres anteriores,
que fue traido desde el disco local.

## Diseño
Se continúa con el servidor HTTP que escucha por el puerto 35000 del taller anterior, pero se realizaron diferentes extensiones para ampliar su funcionalidad.

Ahora se trabaja con dos tipos de anotaciones, una que se aplica sobre clases `@Component` y otra que se aplica sobre métodos `@GetMapping`. Las cuales se probaron sobre
la clase *HelloController*, en donde, cada método es asociado a una ruta en particular. Algunos de estos métodos pueden recibir un parámetro. 

Para esto el servidor almacenará los componentes en un HashMap que asocia rutas con métodos particulares. Sin embargo, para cargar estos componentes, se buscan aquellas clases
que tengan la anotación de `@Component` en el *classpath* y se almacenan los métodos que tengan la anotación de `@GetMapping` para su posterior invocación. Y sobre el path se
añadió una verificación de si es un componente; en cuyo caso si tiene la ruta registrada en el HashMap, obtiene el método y dependiendo si tiene parámetros o no lo invoca. 

## Evaluación
Si ingresa la siguiente dirección http://localhost:35000/component/ se observa

![image](https://github.com/JessicaDMunozO/Taller4-AREP/assets/123814482/9c405ccd-7fd6-4ba1-9678-dc3b44d88ee6)

Si ingresa una dirección con un parámetro http://localhost:35000/component/helloname?name=Jessica

![image](https://github.com/JessicaDMunozO/Taller4-AREP/assets/123814482/8aaa3d43-5746-4067-a8fe-e6b44a3d8337)

O también se puede ingresar http://localhost:35000/component/square?val=4

![image](https://github.com/JessicaDMunozO/Taller4-AREP/assets/123814482/cc59ee2a-6b75-4760-967d-4d7b1debd3dc)

Así mismo, se mantienen las funcionalidades desarrolladas en los talleres anteriores.
Para ver el servicio web asociado a la ruta `/arep` con una consulta http://localhost:35000/action/arep?con=param

![image](https://github.com/JessicaDMunozO/Taller4-AREP/assets/123814482/f7b72049-7191-4bf0-afc1-2e1a52f4d5bc)

Para ver los archivos estáticos traidos del directorio `public` con la búsqueda de una película http://localhost:35000/movies.html 

![image](https://github.com/JessicaDMunozO/Taller4-AREP/assets/123814482/74f7b180-24b7-4424-8410-09a33cd5542a)

Para solicitar una imagen del directorio creado `files` se ingresa la dirección http://localhost:35000/files/voleibol.png

![image](https://github.com/JessicaDMunozO/Taller4-AREP/assets/123814482/22ff215f-1d64-495a-af7a-2c0886b88616)

Y al ingresar una dirección inválida

![image](https://github.com/JessicaDMunozO/Taller4-AREP/assets/123814482/b44b4015-85ef-4375-8f69-f260736c0b1f)

## Construido con
Maven - Gestión de dependencias

## Versiones
Git - Control de versiones

## Autor
Jessica Daniela Muñoz Ossa
