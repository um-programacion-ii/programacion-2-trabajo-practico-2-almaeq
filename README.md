[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/tc38IXJF)
# 📚 Trabajo Práctico: Sistema de Gestión de Biblioteca Digital (Java 21+)

## 📌 Objetivo General

Desarrollar un sistema de gestión de biblioteca digital que implemente los cinco principios SOLID, programación orientada a objetos, y conceptos avanzados de Java. El sistema deberá manejar diferentes tipos de recursos digitales, préstamos, reservas, y notificaciones en tiempo real.

## 👨‍🎓 Información del Alumno
- **Nombre y Apellido**: Alma Quinteros
---
# 📚 Sistema de Gestión de Recursos Digitales

Este sistema permite gestionar usuarios, recursos digitales, préstamos y reservas en una biblioteca digital. Implementa funcionalidades como renovaciones, alertas de vencimiento, recordatorios automáticos, notificaciones personalizadas y generación de reportes en segundo plano.

---

## 🚀 Cómo funciona el sistema

### 🧱 Arquitectura General

El sistema se basa en una arquitectura modular orientada a objetos. Cada componente tiene responsabilidades claras:

- **Usuario**: representa a la persona que utiliza el sistema.
- **RecursoDigital**: representa un recurso como libro, revista o audiolibro.
- **Prestamo**: gestiona el ciclo de vida de un recurso prestado.
- **Reserva**: permite reservar recursos no disponibles.
- **Gestores**: encapsulan la lógica de negocio (usuarios, recursos, préstamos, reservas, reportes, notificaciones).
- **Alertas y Recordatorios**: notifican vencimientos y disponibilidades.
- **CLI**: interfaz de línea de comandos interactiva.

## 📦 Organización del proyecto

```
src/
├── alertas/
├── gestores/
├── modelos/
├── utils/
└── CLI/
```

📁 src
 ┣ 📁 modelos
 ┃ ┣ 📄 Usuario.java              → Representa a un usuario del sistema
 ┃ ┣ 📄 RecursoDigital.java       → Clase abstracta base para recursos
 ┃ ┣ 📄 Libro.java                → Recurso de tipo libro
 ┃ ┣ 📄 Revista.java              → Recurso de tipo revista
 ┃ ┣ 📄 Audiolibro.java           → Recurso de tipo audiolibro
 ┃ ┣ 📄 Prestamo.java             → Registro de préstamos con su estado
 ┃ ┣ 📄 Reserva.java              → Registro de reservas en cola
 ┃ ┗ 📄 EstadoRecurso.java        → Enum de estados posibles de recursos

 ┣ 📁 gestores
 ┃ ┣ 📄 GestorUsuario.java        → Controla creación y búsqueda de usuarios
 ┃ ┣ 📄 GestorRecursos.java       → Controla la administración de recursos
 ┃ ┣ 📄 GestorPrestamo.java       → Maneja préstamos y devoluciones
 ┃ ┣ 📄 GestorReserva.java        → Administra la cola de reservas y prioridades
 ┃ ┣ 📄 GestorNotificaciones.java → Envia notificaciones (email, SMS)
 ┃ ┗ 📄 GestorReportes.java       → Genera reportes estadísticos del sistema

 ┣ 📁 alertas
 ┃ ┣ 📄 AlertaVencimiento.java    → Verifica vencimientos y permite renovaciones
 ┃ ┣ 📄 AlertaDisponibilidad.java → Notifica disponibilidad de recursos reservados
 ┃ ┣ 📄 RecordatorioPeriodico.java→ Runnable que envía recordatorios automáticos
 ┃ ┗ 📄 HistorialAlertas.java     → Almacena y muestra las alertas generadas

 ┣ 📁 servicios
 ┃ ┣ 📄 ServicioNotificaciones.java        → Interfaz base
 ┃ ┣ 📄 ServicioNotificacionesEmail.java   → Notificador vía email
 ┃ ┗ 📄 ServicioNotificacionesSMS.java     → Notificador vía SMS

 ┣ 📁 utils
 ┃ ┣ 📄 SimuladorPrestamos.java            → Simulación de múltiples préstamos concurrentes
 ┃ ┣ 📄 SimuladorAlertaVencimiento.java    → Simula alertas en modo aislado
 ┃ ┗ 📄 SimuladorRecordatorios.java        → Simula recordatorios periódicos

 ┣ 📁 enums
 ┃ ┣ 📄 EstadoPrestamo.java     → Enum para estado del préstamo
 ┃ ┣ 📄 EstadoReserva.java      → Enum para estado de la reserva
 ┃ ┣ 📄 CanalNotificacion.java  → Enum para preferencias (EMAIL, SMS)
 ┃ ┣ 📄 PrioridadReserva.java   → Enum de prioridades (ALTA, MEDIA, BAJA)
 ┃ ┗ 📄 NivelUrgencia.java      → Enum para clasificar severidad de alertas

 ┣ 📁 interfaces
 ┃ ┣ 📄 Prestable.java          → Interfaz para recursos que pueden prestarse
 ┃ ┣ 📄 Renovable.java          → Interfaz para recursos renovables
 ┃ ┗ 📄 Notificable.java        → Interfaz para recursos con notificación

 ┗ 📄 CLI.java                  → Interfaz de consola principal (menú interactivo)

---

### 🔄 Flujo de trabajo

1. El usuario se registra y crea recursos digitales.
2. Puede prestar un recurso, devolverlo o renovarlo si es renovable.
3. Si un recurso no está disponible, el usuario puede reservarlo.
4. Al devolverse un recurso, se notifica al siguiente en la cola de reserva.
5. Alertas y recordatorios se ejecutan automáticamente o se simulan.
6. Se generan reportes asincrónicamente para evaluar el uso del sistema.

---

## 🛠️ Cómo ponerlo en funcionamiento

### ✅ Requisitos previos

- Java 17 o superior
- IDE como IntelliJ IDEA o Eclipse
- Maven o Gradle (opcional)
- Terminal (CLI) con soporte para entrada y salida

### 🧪 Compilación

Si estás en un entorno con `javac`:
```bash
javac */*.java Main.java
```

### ▶️ Ejecución

Desde tu IDE, ejecutar la clase principal:

```java
CLI.CLI.main(String[] args)
```

O desde terminal:
```bash
java Main  
```

---

## 🧪 Cómo probar cada aspecto desarrollado

### 1. 👤 Gestión de Usuarios
- Crear usuarios desde el menú principal.
- Buscar por nombre, fragmento o apellido.
- Cambiar preferencias de notificación (email, SMS, ambos).

### 2. 📚 Gestión de Recursos
- Crear libros, revistas y audiolibros.
- Buscar por título, estado, categoría o palabra clave.

### 3. 🔄 Gestión de Préstamos
- Registrar un préstamo con fecha de devolución.
- Devolver el recurso y ver si se notifica al siguiente usuario.
- Renovar préstamo (se permite ingresar nueva fecha).
- Ver lista de todos los préstamos registrados.

### 4. 📅 Gestión de Reservas
- Reservar recurso si no está disponible.
- Cancelar o completar reservas.
- Simular disponibilidad inmediata desde la notificación.

### 5. 🛎️ Alertas y Recordatorios
- Simular alertas de vencimiento y renovar desde consola.
- Ejecutar recordatorios automáticos cada 24h (por `ScheduledExecutorService`).
- Historial de alertas visible desde consola.

### 6. ⚙️ Notificaciones y Preferencias
- Las notificaciones se envían solo si el canal está activado.
- Se utiliza un `ExecutorService` para simular envío asíncrono.
- Soporte para `EMAIL` y `SMS`.

### 7. 📈 Reportes Asíncronos
- Recursos más prestados
- Usuarios más activos
- Uso por categoría
- Los reportes se generan en segundo plano, mostrando progreso en consola.

---

## ✅ Ejemplos prácticos

- **Renovación desde alerta**:
  ```
  ⚠ [HOY] El préstamo del recurso 'Recurso 1' vence hoy.
  🔁 ¿Deseás renovar el préstamo #1? (s/n): s
  📅 Ingresá la nueva fecha de devolución (YYYY-MM-DD): 2025-04-26
  ✅ Préstamo renovado correctamente.
  ```

- **Notificación inmediata**:
  ```
  📧 Email a usuario@example.com: 📘 Se devolvió el recurso: Python Avanzado
  📢 El recurso 'Python Avanzado' está disponible para tu reserva.
  👤 Juan, ¿deseás tomar el recurso ahora? (s/n): s
  ```

- **Simulador de recordatorios**:
  ```
  🧪 Simulando recordatorios de vencimiento... (modo aislado)
  🕒 Hora de ejecución: 2025-04-22 20:45:01
  📢 Alerta a usuario1@example.com: ⚠️ Hoy vence tu préstamo del recurso 'Libro 2'.
  ```

---

## 🧪 Simuladores del Sistema

El sistema incluye una serie de simuladores que permiten probar de forma aislada y controlada algunas de las funcionalidades más relevantes del sistema. Están diseñados para **evaluar el comportamiento sin afectar los datos reales**.

### 📂 Ubicación

Todos los simuladores se encuentran en el paquete:

```plaintext
src/utils/
```

### 🔧 Acceso desde la Consola (CLI)

Podés ejecutar los simuladores desde el menú principal del sistema:

```
8. Verificar alertas
  ├─ 2. Simular vencimientos (con datos propios y aislados)
  └─ 3. Simular recordatorios periódicos (modo aislado)

5. Gestionar préstamos
  └─ 6. Simular operaciones concurrentes (con datos propios y aislados)
```

### 📄 Descripción de los Simuladores

#### ✅ SimuladorPrestamos.java
- **Propósito**: Simula múltiples usuarios intentando tomar prestado el mismo recurso al mismo tiempo.
- **Objetivo**: Verificar la correcta sincronización del sistema y prevenir condiciones de carrera.
- **Resultado esperado**: Solo un usuario logra tomar el préstamo, los demás reciben un mensaje de recurso no disponible.

---

#### 📅 SimuladorAlertaVencimiento.java
- **Propósito**: Simula tres escenarios diferentes de vencimiento de préstamos:
  - Un préstamo que vence mañana
  - Un préstamo que vence hoy
  - Un préstamo que ya está vencido
- **Objetivo**: Probar el sistema de alertas de vencimiento y permitir renovaciones inmediatas.
- **Resultado esperado**: Se imprime una alerta con el nivel de urgencia correspondiente y se ofrece renovar el préstamo.

---

#### 🔁 SimuladorRecordatorios.java
- **Propósito**: Ejecuta un ciclo completo de recordatorios como lo haría el sistema en segundo plano.
- **Objetivo**: Verificar que se envían mensajes correctamente con base en la fecha de devolución.
- **Resultado esperado**: Aparecen mensajes `ℹ`, `⚠` o `❗` dependiendo de cuántos días falten o hayan pasado del vencimiento.


## Esto permite probar funcionalidades críticas sin necesidad de modificar los datos reales ni interactuar con un flujo completo del sistema.
---

# 🧪 Pruebas por Módulo

Este documento detalla cómo probar cada aspecto del sistema utilizando la interfaz de consola.

---

## 👤 Gestión de Usuarios (Menú: 1)

### ➕ Añadir Usuarios
1. Seleccionar la opción `1. Crear Usuario`.
2. Ingresar los datos solicitados: nombre, apellido y correo electrónico.
3. Verificar el mensaje de confirmación: `✅ Usuario creado con éxito`.

### 🔍 Buscar Usuarios (Menú: 2)
- **Listar todos**: opción `1` muestra todos los usuarios registrados.
- **Buscar por nombre exacto**: opción `2`, ingresar nombre completo.
- **Buscar por fragmento**: opción `3`, ingresar parte del nombre o apellido.
- **Ordenar por apellido o nombre**: opciones `4` y `5`.

---

## 📚 Gestión de Recursos Digitales (Menú: 3 y 4)

### ➕ Crear Recursos
1. Seleccionar `3. Crear Recurso Digital`.
2. Elegir la categoría (Libro, Revista, etc).
3. Ingresar título y atributos requeridos.
4. Confirmar con `✅ Recurso agregado con éxito`.

### 🔍 Buscar Recursos (Menú: 4)
- Búsquedas por título exacto o fragmento.
- Filtros por categoría, estado o renovabilidad.

---

## 🔄 Gestión de Préstamos (Menú: 5)

### ➕ Registrar Préstamo
1. Opción `1. Registrar Préstamo`.
2. Seleccionar usuario y recurso disponible.
3. Ingresar fecha de devolución.
4. Confirmar el mensaje de éxito.

### 🔁 Renovar Préstamo
- Opción `3. Renovar Préstamo`.
- Ingresar ID del préstamo.
- Ingresar nueva fecha de devolución si corresponde.

### ↩️ Devolver Préstamo
- Opción `2. Devolver Préstamo`.
- Ingresar ID del préstamo.
- Se actualiza el estado y puede activar notificación de reserva.

### 📋 Listar Préstamos
- Opción `4`, muestra todos los préstamos y su estado.

---

## 📅 Gestión de Reservas (Menú: 6)

- **Registrar reserva**: se valida si el recurso está prestado.
- **Cancelar o completar** reservas desde su respectiva opción.
- **Mostrar próxima reserva** y ver prioridades dinámicas.

---

## 📢 Alertas y Recordatorios (Menú: 8)

### 📆 Verificar Vencimientos
- Opción `1`, verifica y pregunta si desea renovar.
- Aplica lógica de urgencia (⚠️ Hoy, ℹ Mañana, ❗ Vencido).

### 🧪 Simular Vencimientos
- Opción `2`, ejecuta una simulación con datos ficticios.

### 🧪 Simular Recordatorios
- Opción `3`, simula recordatorios periódicos para usuarios ficticios.

---

## 📈 Reportes (Menú: 7)

- Generación en segundo plano (asincrónica).
- Recursos más prestados, usuarios más activos, estadísticas por categoría.

---

## ⚙️ Configuración de Preferencias (Desde gestión de usuarios)
- Al seleccionar un usuario por ID, se puede configurar:
  - 📧 Email
  - 📱 SMS
  - Ambos
- Se aplican a futuras notificaciones.

---

## 🗃️ Historial de Alertas
- Se mantiene un registro visible desde consola.
- Ayuda a auditar vencimientos y recordatorios previos.

## 🧼 Mantenimiento y mejoras

- [x] ExecutorService usado para tareas asincrónicas
- [x] Uso de `synchronized` para evitar problemas de concurrencia
- [x] Modularidad en clases y responsabilidades
- [x] Pruebas en consola para cada funcionalidad

---

## 📋 Requisitos Adicionales

### Documentación del Sistema
Como parte del trabajo práctico, deberás incluir en este README una guía de uso que explique:

1. **Cómo funciona el sistema**:
   - Descripción general de la arquitectura
   - Explicación de los componentes principales
   - Flujo de trabajo del sistema

2. **Cómo ponerlo en funcionamiento**:
   - Deberás incluir las instrucciones detalladas de puesta en marcha
   - Explicar los requisitos previos necesarios
   - Describir el proceso de compilación
   - Detallar cómo ejecutar la aplicación

3. **Cómo probar cada aspecto desarrollado**:
   - Deberás proporcionar ejemplos de uso para cada funcionalidad implementada
   - Incluir casos de prueba que demuestren el funcionamiento del sistema
   - Describir flujos de trabajo completos que muestren la interacción entre diferentes componentes

La guía debe ser clara, concisa y permitir a cualquier usuario entender y probar el sistema. Se valorará especialmente:
- La claridad de las instrucciones
- La completitud de la documentación
- La organización de la información
- La inclusión de ejemplos prácticos

### Prueba de Funcionalidades

#### 1. Gestión de Recursos
- **Agregar Libro**: 
  - Proceso para agregar un nuevo libro al sistema
  - Verificación de que el libro se agregó correctamente
  - Validación de los datos ingresados

- **Buscar Recurso**:
  - Proceso de búsqueda de recursos
  - Verificación de resultados de búsqueda
  - Manejo de casos donde no se encuentran resultados

- **Listar Recursos**:
  - Visualización de todos los recursos
  - Filtrado por diferentes criterios
  - Ordenamiento de resultados

#### 2. Gestión de Usuarios
- **Registrar Usuario**:
  - Proceso de registro de nuevos usuarios
  - Validación de datos del usuario
  - Verificación del registro exitoso

- **Buscar Usuario**:
  - Proceso de búsqueda de usuarios
  - Visualización de información del usuario
  - Manejo de usuarios no encontrados

#### 3. Préstamos
- **Realizar Préstamo**:
  - Proceso completo de préstamo
  - Verificación de disponibilidad
  - Actualización de estados

- **Devolver Recurso**:
  - Proceso de devolución
  - Actualización de estados
  - Liberación del recurso

#### 4. Reservas
- **Realizar Reserva**:
  - Proceso de reserva de recursos
  - Gestión de cola de reservas
  - Notificación de disponibilidad

#### 5. Reportes
- **Ver Reportes**:
  - Generación de diferentes tipos de reportes
  - Visualización de estadísticas
  - Exportación de datos

#### 6. Alertas
- **Verificar Alertas**:
  - Sistema de notificaciones
  - Diferentes tipos de alertas
  - Gestión de recordatorios

### Ejemplos de Prueba
1. **Flujo Completo de Préstamo**:
   - Registrar un usuario
   - Agregar un libro
   - Realizar un préstamo
   - Verificar el estado del recurso
   - Devolver el recurso
   - Verificar la actualización del estado

2. **Sistema de Reservas**:
   - Registrar dos usuarios
   - Agregar un libro
   - Realizar una reserva con cada usuario
   - Verificar la cola de reservas
   - Procesar las reservas

3. **Alertas y Notificaciones**:
   - Realizar un préstamo
   - Esperar a que se acerque la fecha de vencimiento
   - Verificar las alertas generadas
   - Probar la renovación del préstamo

## 🧩 Tecnologías y Herramientas

- Java 21+ (LTS)
- Git y GitHub
- GitHub Projects
- GitHub Issues
- GitHub Pull Requests

## 📘 Etapas del Trabajo

### Etapa 1: Diseño Base y Principios SOLID
- **SRP**: 
  - Crear clase `Usuario` con atributos básicos (nombre, ID, email)
  - Crear clase `RecursoDigital` como clase base abstracta
  - Implementar clase `GestorUsuarios` separada de `GestorRecursos`
  - Cada clase debe tener una única responsabilidad clara
  - Implementar clase `Consola` para manejar la interacción con el usuario

- **OCP**: 
  - Diseñar interfaz `RecursoDigital` con métodos comunes
  - Implementar clases concretas `Libro`, `Revista`, `Audiolibro`
  - Usar herencia para extender funcionalidad sin modificar código existente
  - Ejemplo: agregar nuevo tipo de recurso sin cambiar clases existentes
  - Implementar menú de consola extensible para nuevos tipos de recursos

- **LSP**: 
  - Asegurar que todas las subclases de `RecursoDigital` puedan usarse donde se espera `RecursoDigital`
  - Implementar métodos comunes en la clase base
  - Validar que el comportamiento sea consistente en todas las subclases
  - Crear métodos de visualización en consola para todos los tipos de recursos

- **ISP**: 
  - Crear interfaz `Prestable` para recursos que se pueden prestar
  - Crear interfaz `Renovable` para recursos que permiten renovación
  - Implementar solo las interfaces necesarias en cada clase
  - Diseñar menús de consola específicos para cada tipo de operación

- **DIP**: 
  - Crear interfaz `ServicioNotificaciones`
  - Implementar `ServicioNotificacionesEmail` y `ServicioNotificacionesSMS`
  - Usar inyección de dependencias en las clases que necesitan notificaciones
  - Implementar visualización de notificaciones en consola

### Etapa 2: Gestión de Recursos y Colecciones
- Implementar colecciones:
  - Usar `ArrayList<RecursoDigital>` para almacenar recursos
  - Usar `Map<String, Usuario>` para gestionar usuarios
  - Implementar métodos de búsqueda básicos
  - Crear menú de consola para gestión de recursos

- Crear servicios de búsqueda:
  - Implementar búsqueda por título usando Streams
  - Implementar filtrado por categoría
  - Crear comparadores personalizados para ordenamiento
  - Diseñar interfaz de consola para búsquedas con filtros

- Sistema de categorización:
  - Crear enum `CategoriaRecurso`
  - Implementar método de asignación de categorías
  - Crear búsqueda por categoría
  - Mostrar categorías disponibles en consola

- Manejo de excepciones:
  - Crear `RecursoNoDisponibleException`
  - Crear `UsuarioNoEncontradoException`
  - Implementar manejo adecuado de excepciones en los servicios
  - Mostrar mensajes de error amigables en consola

### Etapa 3: Sistema de Préstamos y Reservas
- Implementar sistema de préstamos:
  - Crear clase `Prestamo` con atributos básicos
  - Implementar lógica de préstamo y devolución
  - Manejar estados de los recursos (disponible, prestado, reservado)
  - Diseñar menú de consola para préstamos

- Sistema de reservas:
  - Crear clase `Reserva` con atributos necesarios
  - Implementar cola de reservas usando `BlockingQueue`
  - Manejar prioridad de reservas
  - Mostrar estado de reservas en consola

- Notificaciones:
  - Implementar sistema básico de notificaciones
  - Crear diferentes tipos de notificaciones
  - Usar `ExecutorService` para enviar notificaciones
  - Mostrar notificaciones en consola

- Concurrencia:
  - Implementar sincronización en operaciones de préstamo
  - Usar `synchronized` donde sea necesario
  - Manejar condiciones de carrera
  - Mostrar estado de operaciones concurrentes en consola

### Etapa 4: Reportes y Análisis
- Generar reportes básicos:
  - Implementar reporte de recursos más prestados
  - Crear reporte de usuarios más activos
  - Generar estadísticas de uso por categoría
  - Diseñar visualización de reportes en consola

- Sistema de alertas:
  - Implementar alertas por vencimiento de préstamos:
    - Crear clase `AlertaVencimiento` que monitorea fechas de devolución
    - Implementar lógica de recordatorios (1 día antes, día del vencimiento)
    - Mostrar alertas en consola con formato destacado
    - Permitir renovación desde la alerta
  
  - Crear notificaciones de disponibilidad:
    - Implementar `AlertaDisponibilidad` para recursos reservados
    - Notificar cuando un recurso reservado está disponible
    - Mostrar lista de recursos disponibles en consola
    - Permitir préstamo inmediato desde la notificación
  
  - Manejar recordatorios automáticos:
    - Implementar sistema de recordatorios periódicos
    - Crear diferentes niveles de urgencia (info, warning, error)
    - Mostrar historial de alertas en consola
    - Permitir configuración de preferencias de notificación

- Concurrencia en reportes:
  - Implementar generación de reportes en segundo plano
  - Usar `ExecutorService` para tareas asíncronas
  - Manejar concurrencia en acceso a datos
  - Mostrar progreso de generación de reportes en consola

## 📋 Detalle de Implementación

### 1. Estructura Base
```java
// Interfaces principales
public interface RecursoDigital {
    String getIdentificador();
    EstadoRecurso getEstado();
    void actualizarEstado(EstadoRecurso estado);
}

public interface Prestable {
    boolean estaDisponible();
    LocalDateTime getFechaDevolucion();
    void prestar(Usuario usuario);
}

public interface Notificable {
    void enviarNotificacion(String mensaje);
    List<Notificacion> getNotificacionesPendientes();
}

// Clase base abstracta
public abstract class RecursoBase implements RecursoDigital, Prestable {
    // Implementación común
}
```

### 2. Gestión de Biblioteca
```java
public class GestorBiblioteca {
    private final Map<String, RecursoDigital> recursos;
    private final List<Prestamo> prestamos;
    private final ExecutorService notificador;
    // Implementación de gestión
}
```

### 3. Sistema de Préstamos
```java
public class SistemaPrestamos {
    private final BlockingQueue<SolicitudPrestamo> colaSolicitudes;
    private final ExecutorService procesadorPrestamos;
    // Implementación de préstamos
}
```

## ✅ Entrega y Flujo de Trabajo con GitHub

1. **Configuración del Repositorio**
   - Proteger la rama `main`
   - Crear template de Issues y Pull Requests

2. **Project Kanban**
   - `To Do`
   - `In Progress`
   - `Code Review`
   - `Done`

3. **Milestones**
   - Etapa 1: Diseño Base
   - Etapa 2: Gestión de Recursos
   - Etapa 3: Sistema de Préstamos
   - Etapa 4: Reportes

4. **Issues y Pull Requests**
   - Crear Issues detallados para cada funcionalidad
   - Asociar cada Issue a un Milestone
   - Implementar en ramas feature
   - Revisar código antes de merge

## 📝 Ejemplo de Issue

### Título
Implementar sistema de préstamos concurrente

### Descripción
Crear el sistema de préstamos que utilice hilos y el patrón productor-consumidor para procesar solicitudes de préstamo en tiempo real.

#### Requisitos
- Implementar `BlockingQueue` para solicitudes de préstamo
- Crear procesador de solicitudes usando `ExecutorService`
- Implementar sistema de notificaciones
- Asegurar thread-safety en operaciones de préstamo

#### Criterios de Aceptación
- [ ] Sistema procesa préstamos concurrentemente
- [ ] Manejo adecuado de excepciones
- [ ] Documentación de diseño

### Labels
- `enhancement`
- `concurrency`

## ✅ Requisitos para la Entrega

- ✅ Implementación completa de todas las etapas
- ✅ Código bien documentado
- ✅ Todos los Issues cerrados
- ✅ Todos los Milestones completados
- ✅ Pull Requests revisados y aprobados
- ✅ Project actualizado

> ⏰ **Fecha de vencimiento**: 23/04/2025 a las 13:00 hs

## 📚 Recursos Adicionales

- Documentación oficial de Java 21
- Guías de estilo de código
- Ejemplos de implementación concurrente
- Patrones de diseño aplicados

## 📝 Consideraciones Éticas

### Uso de Inteligencia Artificial
El uso de herramientas de IA en este trabajo práctico debe seguir las siguientes pautas:

1. **Transparencia**
   - Documentar claramente qué partes del código fueron generadas con IA
   - Explicar las modificaciones realizadas al código generado
   - Mantener un registro de las herramientas utilizadas

2. **Aprendizaje**
   - La IA debe usarse como herramienta de aprendizaje, no como reemplazo
   - Comprender y ser capaz de explicar el código generado
   - Utilizar la IA para mejorar la comprensión de conceptos

3. **Integridad Académica**
   - El trabajo final debe reflejar tu aprendizaje y comprensión personal
   - No se permite la presentación de código generado sin comprensión
   - Debes poder explicar y defender cualquier parte del código

4. **Responsabilidad**
   - Verificar la corrección y seguridad del código generado
   - Asegurar que el código cumple con los requisitos del proyecto
   - Mantener la calidad y estándares de código establecidos

5. **Desarrollo Individual**
   - La IA puede usarse para facilitar tu proceso de aprendizaje
   - Documentar tu proceso de desarrollo y decisiones tomadas
   - Mantener un registro de tu progreso y aprendizaje

### Consecuencias del Uso Inadecuado
El uso inadecuado de IA puede resultar en:
- Calificación reducida o nula
- Sanciones académicas
- Pérdida de oportunidades de aprendizaje
- Impacto negativo en tu desarrollo profesional

## 📝 Licencia

Este trabajo es parte del curso de Programación Avanzada de Ingeniería en Informática. Uso educativo únicamente.