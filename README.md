# Bono Parcial: Aplicación Web con Sockets y Reflexión

Este proyecto consiste en una aplicación web distribuida que utiliza sockets para recibir comandos matemáticos y listas de parámetros separados por comas. El comando puede corresponder a cualquier función de la clase `Math` en Java (con double), o el comando especial `bbl`, que utiliza el algoritmo de ordenamiento Bubble Sort para ordenar una lista de números.

## Requerimientos

- **No puede revisar código en internet ni utilizar código antiguo**.
- **No puede usar frameworks como Spark o Spring**.
- Solo debe utilizar **sockets** para la comunicación entre los componentes.
- La aplicación debe funcionar bajo los principios de una arquitectura distribuida.
  
## Arquitectura

La aplicación está compuesta por tres componentes principales, distribuidos en diferentes máquinas virtuales de Java:

1. **Fachada de Servicios (ServiceFacade)**: Este componente actúa como intermediario entre el cliente web y el servicio de cálculo. Recibe las solicitudes del cliente y las delega al servicio de cálculo.
   
2. **Servicio de Cálculo (ReflexCalculator)**: Este servicio realiza los cálculos matemáticos utilizando reflexión para invocar métodos de la clase `Math`. Además, implementa el comando especial `bbl` para ordenar números mediante el algoritmo Bubble Sort.

3. **Cliente Web (HTML + JS)**: Un cliente simple que se descarga desde la fachada de servicios. El cliente permite al usuario enviar comandos a través de un formulario HTML y recibir los resultados en formato JSON sin recargar la página.

## API

### API de la Fachada de Servicios

1. **Entrega del Cliente Web**: 
   ```
   http://localhost:35000/
   ```
   Este servicio entrega la página HTML y el JavaScript.

   ![image](https://github.com/user-attachments/assets/a6333fce-cba7-42f1-b895-86a414f80a42)


3. **Computar Operaciones**:
   ```
   http://localhost:35000/computar?comando=[comando con parámetros separados por coma entre paréntesis]
   ```
   Este endpoint recibe el comando y lo delega al servicio de cálculo, devolviendo el resultado en formato JSON.

   ![image](https://github.com/user-attachments/assets/1639de6a-7392-4551-9cd5-13b60589ba84)


### API del Servicio de Cálculo

1. **Cálculo mediante Reflexión**:
   ```
   http://localhost:36000//compreflex?comando=[comando con parámetros separados por coma entre paréntesis]
   ```
   Este servicio recibe el comando y devuelve el resultado en formato JSON.

   ![image](https://github.com/user-attachments/assets/baafdce0-14d9-424c-afb4-ce5c1c06ce04)


## Ejemplo de Comandos

- **Función matemática**:
  ```
  /computar?comando=max(3.0,4.0)
  ```
  Devuelve el máximo entre 3.0 y 4.0.

  ![image](https://github.com/user-attachments/assets/5c47dc76-7a90-4d0d-97f4-b89d8f0a9006)

- **Ordenamiento con Bubble Sort**:
  ```
  /computar?comando=bbl(4,2,9,1)
  ```
  Ordena los números 4, 2, 9, 1 usando Bubble Sort y devuelve: `[1, 2, 4, 9]`.

  ![image](https://github.com/user-attachments/assets/a1fee014-3e7d-41ed-8b53-b2fed93695a2)


## Instalación y Ejecución

### 1. Servidor de la Fachada (ServiceFacade)

1. Clonar el repositorio.
2. Compilar usando maven
  ```
  mvn clean install
  ```
4. Ejecutar la clase `CalcreflexFacade`.

  ```
  java -cp target/AREP-parcial-bono-1.0-SNAPSHOT.jar edu.escuelaing.arem.ASE.app.CalcreflexFacade
  ```

### 2. Servidor de Cálculo (ReflexCalculator)

1. En otra máquina virtual o puerto diferente, ejecutar la clase `CalcreflexBEServer`.
  ```
  java -cp target/AREP-parcial-bono-1.0-SNAPSHOT.jar edu.escuelaing.arem.ASE.app.CalcreflexBEServer
  ```

### 3. Cliente Web

1. El cliente web se puede acceder desde un navegador al hacer una solicitud GET a:
   
   ```
   http://localhost:35000/
   ```

3. Ingresar un comando en el formulario y presionar **Submit** para obtener el resultado.

## Notas Importantes

- Los números que se envían a la calculadora se asumen que siempre son de tipo `Double` para las funciones matemáticas, y de tipo `Integer` para el comando `bbl`.
- Se asegura de manejar solicitudes HTTP inesperadas devolviendo mensajes válidos de error o respuestas adecuadas.
- La comunicación entre el cliente y la fachada es asíncrona, usando JavaScript para actualizar los resultados sin recargar la página.

## Buenas Prácticas de Diseño

- La aplicación sigue los principios de la Programación Orientada a Objetos (POO) y el uso de reflexión para asegurar flexibilidad en la ejecución de los comandos.
- Los servicios están distribuidos en diferentes máquinas virtuales, lo que favorece la escalabilidad y separación de responsabilidades.
