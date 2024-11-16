# Novelas con widgets y fragments - Sonia Tejero Recio

## Comenzamos 🚀

Este proyecto está desarrollado en **Kotlin** utilizando **Android Studio** para crear una aplicación móvil que permite a los usuarios gestionar una lista de novelas. A través de fragmentos y widgets, se ofrece una interfaz modular y adaptable para mostrar tanto la lista de novelas como los detalles de cada una. Además, un widget permite ver la lista resumida de novelas favoritas directamente desde la pantalla de inicio.

## ¿Cómo he estructurado el proyecto?

### Fragmentos

- **NovelListFragment**: Fragmento que muestra una lista de novelas mediante un `RecyclerView`. Este fragmento se encarga de observar cambios en la base de datos mediante `LiveData` y actualiza la lista en tiempo real.
- **NovelDetailFragment**: Fragmento que muestra los detalles de una novela seleccionada, incluyendo el título, autor, año y sinopsis. La información se pasa a este fragmento mediante un `Bundle`.

### Widgets

- **NewAppWidget**: Widget de la aplicación que muestra un resumen de las novelas favoritas del usuario en la pantalla de inicio. Los usuarios pueden hacer clic en el widget para abrir la aplicación y ver más detalles.

### AndroidManifest.xml

- Configuración del widget y las actividades de la aplicación. Se declaran los fragmentos y el widget para asegurar su correcto funcionamiento en el ciclo de vida de la aplicación.

## Pantallas

### NovelListFragment

- **Descripción**: Muestra una lista de todas las novelas.
- **Funcionalidades**:
  - Carga de datos desde `Room` usando `LiveData` para actualizar dinámicamente la lista en el `RecyclerView`.
  - Permite seleccionar una novela para mostrar sus detalles en `NovelDetailFragment`.

### NovelDetailFragment

- **Descripción**: Pantalla de detalle de cada novela seleccionada.
- **Funcionalidades**:
  - Muestra información detallada de la novela (título, autor, año, sinopsis).
  - Recibe datos a través de argumentos usando `Bundle`.

### Widget de Novelas Favoritas

- **Descripción**: Widget de acceso rápido desde la pantalla de inicio.
- **Funcionalidades**:
  - Muestra un resumen de las novelas favoritas.
  - Permite acceder rápidamente a la aplicación mediante un `PendingIntent` que abre la actividad principal.

## Arquitectura y Configuración del Proyecto

### Implementación de Fragmentos

Los fragmentos permiten una interfaz de usuario modular:
- **NovelListFragment** observa cambios en la base de datos y actualiza la lista automáticamente.
- **NovelDetailFragment** muestra los detalles de una novela seleccionada y se comunica con `NovelListFragment` para una navegación fluida.

### Implementación de Widgets

- **NewAppWidget** se configura para mostrar un resumen de las novelas favoritas y se actualiza periódicamente para reflejar los cambios en la lista de favoritos.

### AndroidManifest.xml

- El widget se registra en el manifiesto para que esté disponible en la pantalla de inicio. Se especifica el layout y el tamaño mínimo en `appwidget_provider_info.xml`.

## Flujo de la Aplicación

1. El usuario abre la aplicación y ve la lista de novelas en `NovelListFragment`.
2. Al seleccionar una novela, `NovelDetailFragment` muestra los detalles específicos de la novela seleccionada.
3. El widget muestra un resumen de las novelas favoritas y permite al usuario acceder rápidamente a la aplicación.
4. Los datos de las novelas se gestionan mediante `Room` y `LiveData`, permitiendo que las listas y detalles se actualicen en tiempo real.


## Corrección 🖇️

**Repositorio de GitHub:** [Repositorio](https://github.com/SoniaTejeroRecio/novelaConFragments.git)

