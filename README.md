# Proyecto gestionNovelas_SoniaTejeroRecio

## Comenzamos 🚀

Este proyecto está desarrollado completamente en Kotlin. La aplicación se centra en la gestión de novelas utilizando Firebase Firestore para la sincronización de datos en tiempo real y Firebase Cloud Messaging (FCM) para el envío de notificaciones push.

## ¿Cómo he estructurado el proyecto?

- **MainActivity**: Es el corazón de la aplicación. Aquí se cargan y sincronizan los datos de novelas con Firestore, se maneja la recepción de notificaciones a través de Firebase Cloud Messaging y se controla la navegación entre las diferentes pantallas.
  
- **MyFirebaseMessagingService**: Este servicio gestiona la recepción de notificaciones push a través de FCM. Cuando llega una notificación, la aplicación genera y muestra un mensaje al usuario.
  
- **NetworkChangeReceiver**: Un `BroadcastReceiver` que detecta eventos de red, como la conexión a una red Wi-Fi. Cuando el dispositivo está conectado a Wi-Fi, la aplicación activa la sincronización de datos con Firestore automáticamente.

## Pantallas

### MainActivity

- **Descripción**: Pantalla principal de la aplicación que actúa como punto de entrada para la gestión de novelas.
- **Funcionalidades**:
  - Sincronización de novelas desde Firebase Firestore.
  - Sincronización automática cuando el dispositivo se conecta a Wi-Fi (usando `NetworkChangeReceiver`).
  - Gestión de notificaciones push mediante Firebase Cloud Messaging.
  - Navegación entre las pantallas de la aplicación.

### MyFirebaseMessagingService

- **Descripción**: Servicio de Firebase Cloud Messaging para recibir notificaciones push.
- **Funcionalidades**:
  - Recibe mensajes push y genera notificaciones para el usuario.
  - Verifica si el permiso de notificaciones está concedido antes de mostrar las alertas.

### NetworkChangeReceiver

- **Descripción**: Un `BroadcastReceiver` que detecta cambios en la conectividad del dispositivo.
- **Funcionalidades**:
  - Detecta cuando el dispositivo se conecta a una red Wi-Fi.
  - Sincroniza los datos automáticamente con Firebase Firestore cuando hay conexión a Wi-Fi.

### WelcomeScreen

- **Descripción**: Pantalla de bienvenida donde el usuario puede comenzar a navegar por la aplicación.
- **Funcionalidades**:
  - Botón para navegar a la `SecondScreen`.

### SecondScreen

- **Descripción**: Pantalla de navegación hacia la pantalla de agregación de novelas y visualización de detalles.
- **Funcionalidades**:
  - Navegación hacia la pantalla de agregar novelas.
  - Navegación hacia la pantalla de detalles de novelas.

### AddNovelaScreen

- **Descripción**: Pantalla donde el usuario puede agregar una nueva novela a la colección de Firebase Firestore.
- **Funcionalidades**:
  - Permite introducir y guardar el título, año de publicación, nota media, editorial y temas de la novela.
  - Sincronización automática de los datos con Firestore.

### DetallesNovelasScreen

- **Descripción**: Pantalla que muestra la lista de novelas almacenadas en Firestore.
- **Funcionalidades**:
  - Listado de novelas.
  - Posibilidad de marcar novelas como favoritas.
  - Botones para eliminar novelas o ver las reseñas.

### ResenasScreen

- **Descripción**: Pantalla donde el usuario puede ver las reseñas de una novela seleccionada y añadir nuevas reseñas.
- **Funcionalidades**:
  - Mostrar la lista de reseñas existentes.
  - Añadir nuevas reseñas y sincronizarlas con Firestore.

## Corrección 🖇️

**Link a codespace:** [codespace_de_Sonia](https://codespaces.new/SoniaTejeroRecio/gestionNovelas_SoniaTejeroRecio)

**Repositorio de GitHub:** [Repositorio](https://github.com/SoniaTejeroRecio/gestionNovelas_SoniaTejeroRecio.git)
