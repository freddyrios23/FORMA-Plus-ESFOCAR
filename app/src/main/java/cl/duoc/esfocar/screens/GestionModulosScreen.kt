// Indica el paquete donde se encuentra esta pantalla.
package cl.duoc.esfocar.screens

// Permite colocar un color de fondo en la pantalla.
import androidx.compose.foundation.background

// Importa herramientas para organizar elementos en filas, columnas,
// agregar espacios y definir tamaños.
import androidx.compose.foundation.layout.*

// Permite mostrar una lista que se puede desplazar verticalmente.
import androidx.compose.foundation.lazy.LazyColumn

// Permite mostrar los elementos de una lista en LazyColumn.
import androidx.compose.foundation.lazy.items

// Importa el botón que se utiliza para ejecutar acciones.
import androidx.compose.material3.Button

// Permite personalizar los colores de los botones.
import androidx.compose.material3.ButtonDefaults

// Permite mostrar cada módulo dentro de una tarjeta.
import androidx.compose.material3.Card

// Permite configurar la apariencia de las tarjetas.
import androidx.compose.material3.CardDefaults

// Importa el campo donde el profesor escribe el nombre del módulo.
import androidx.compose.material3.OutlinedTextField

// Permite mostrar textos en la pantalla.
import androidx.compose.material3.Text

// Importa herramientas de Compose para guardar y actualizar estados.
import androidx.compose.runtime.*

// Permite modificar el tamaño, la posición y la apariencia de los elementos.
import androidx.compose.ui.Modifier

// Permite utilizar colores.
import androidx.compose.ui.graphics.Color

// Permite obtener el contexto de la aplicación Android.
import androidx.compose.ui.platform.LocalContext

// Permite cambiar el grosor de las letras.
import androidx.compose.ui.text.font.FontWeight

// Permite definir tamaños en dp y sp.
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Importa el DAO que contiene las operaciones de los módulos.
import cl.duoc.esfocar.data.ModuloDAO

// Importa el proveedor que permite acceder a la base de datos Room.
import cl.duoc.esfocar.data.local.DatabaseProvider

// Importa el modelo que representa cada módulo guardado.
import cl.duoc.esfocar.data.model.Modulo

// Permite ejecutar operaciones de base de datos en una corrutina.
import kotlinx.coroutines.launch


// Define el color verde utilizado en los elementos de ESFOCAR.
private val Verde = Color(0xFF17452F)

// Define el color de fondo de la pantalla.
private val Fondo = Color(0xFFF6F7F3)


// Define la pantalla para que el profesor pueda gestionar los módulos.
@Composable
fun GestionModulosScreen(

    // Función que se ejecuta cuando el profesor pulsa el botón Volver.
    // Se deja vacía por defecto si no se entrega ninguna acción.
    onVolver: () -> Unit = {}
) {

    // Obtiene el contexto de la aplicación Android.
    // Se necesita para acceder a la base de datos.
    val contexto = LocalContext.current


    // Obtiene el DAO de módulos desde la base de datos.
    // remember evita crear nuevamente esta referencia en cada recomposición.
    val moduloDAO = remember {
        DatabaseProvider.obtenerDatabase(contexto).moduloDAO()
    }


    // Crea un alcance de corrutinas.
    // Permite ejecutar operaciones de Room sin bloquear la pantalla.
    val scope = rememberCoroutineScope()


    // Guarda la lista de módulos que se mostrarán en pantalla.
    // Inicialmente la lista está vacía porque los datos se cargarán desde Room.
    var modulos by remember {
        mutableStateOf<List<Modulo>>(emptyList())
    }


    // Guarda el texto que el profesor escribe en el campo de entrada.
    // Al principio el campo está vacío.
    var nombreModulo by remember {
        mutableStateOf("")
    }


    // Ejecuta este bloque cuando se inicia la pantalla o cambia moduloDAO.
    // Consulta los módulos almacenados en la base de datos.
    LaunchedEffect(moduloDAO) {

        // Obtiene todos los módulos guardados y actualiza la lista.
        modulos = moduloDAO.obtenerModulos()
    }


    // Organiza verticalmente todos los elementos de la pantalla.
    Column(
        modifier = Modifier

            // Hace que la columna ocupe todo el espacio disponible.
            .fillMaxSize()

            // Establece el color de fondo.
            .background(Fondo)

            // Agrega un margen interior de 20 dp.
            .padding(20.dp)
    ) {

        // Muestra el título principal de la pantalla.
        Text(
            text = "Gestionar módulos",

            // Define el tamaño del texto.
            fontSize = 26.sp,

            // Coloca el texto en negrita.
            fontWeight = FontWeight.Bold,

            // Utiliza el color verde definido anteriormente.
            color = Verde
        )


        // Agrega un espacio vertical de 16 dp debajo del título.
        Spacer(modifier = Modifier.height(16.dp))


        // Crea el campo donde el profesor escribe el nombre del módulo.
        OutlinedTextField(

            // Muestra el texto guardado en la variable nombreModulo.
            value = nombreModulo,

            // Actualiza la variable cada vez que el profesor escribe.
            onValueChange = { nombreModulo = it },

            // Muestra una etiqueta dentro del campo.
            label = { Text("Nombre del módulo") },

            // Hace que el campo ocupe todo el ancho disponible.
            modifier = Modifier.fillMaxWidth()
        )


        // Agrega un espacio vertical de 10 dp.
        Spacer(modifier = Modifier.height(10.dp))


        // Crea el botón para agregar un módulo.
        Button(
            onClick = {

                // Comprueba que el profesor haya escrito algún texto.
                // isNotBlank también rechaza un texto compuesto solo por espacios.
                if (nombreModulo.isNotBlank()) {

                    // Crea un objeto Modulo con el nombre escrito.
                    // El ID se genera automáticamente en Room.
                    val nuevoModulo = Modulo(
                        nombre = nombreModulo.trim()
                    )

                    // Inicia una corrutina para trabajar con la base de datos.
                    scope.launch {

                        // Guarda el nuevo módulo en la tabla modulos.
                        moduloDAO.insertar(nuevoModulo)

                        // Vuelve a consultar la base de datos.
                        // Así la lista incluye el módulo recién agregado.
                        modulos = moduloDAO.obtenerModulos()
                    }

                    // Limpia el campo de texto después de iniciar el guardado.
                    nombreModulo = ""
                }
            },

            // Hace que el botón ocupe todo el ancho disponible.
            modifier = Modifier.fillMaxWidth(),

            // Define el color de fondo del botón.
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde
            )
        ) {

            // Texto que aparece dentro del botón.
            Text("Agregar módulo")
        }


        // Agrega un espacio vertical de 20 dp.
        Spacer(modifier = Modifier.height(20.dp))


        // Muestra el título de la lista de módulos.
        Text(
            text = "Módulos existentes",

            // Define el tamaño del texto.
            fontSize = 20.sp,

            // Coloca el texto en negrita.
            fontWeight = FontWeight.Bold,

            // Utiliza el color verde.
            color = Verde
        )


        // Agrega un espacio vertical de 10 dp.
        Spacer(modifier = Modifier.height(10.dp))


        // Crea una lista desplazable para mostrar los módulos.
        LazyColumn(

            // La lista ocupa el espacio disponible restante de la columna.
            // Esto permite mantener el botón Volver debajo de la lista.
            modifier = Modifier.weight(1f),

            // Deja 10 dp de separación entre cada módulo.
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // Recorre la lista de módulos y muestra cada elemento.
            // La clave utiliza el ID único de cada módulo.
            items(
                items = modulos,
                key = { modulo -> modulo.id }
            ) { modulo ->

                // Crea una tarjeta para mostrar un módulo.
                Card(
                    // Hace que la tarjeta ocupe todo el ancho disponible.
                    modifier = Modifier.fillMaxWidth(),

                    // Define el color de fondo de la tarjeta.
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    // Organiza verticalmente el nombre y el botón Eliminar.
                    Column(
                        // Agrega 15 dp de espacio interior en la tarjeta.
                        modifier = Modifier.padding(15.dp)
                    ) {

                        // Muestra el nombre del módulo.
                        Text(
                            text = modulo.nombre,

                            // Muestra el nombre en negrita.
                            fontWeight = FontWeight.Bold
                        )


                        // Agrega un espacio de 8 dp debajo del nombre.
                        Spacer(modifier = Modifier.height(8.dp))


                        // Crea el botón para eliminar este módulo.
                        Button(
                            onClick = {

                                // Inicia una corrutina para ejecutar la eliminación.
                                scope.launch {

                                    // Elimina de Room el módulo seleccionado.
                                    moduloDAO.eliminar(modulo)

                                    // Consulta nuevamente la base de datos.
                                    // El módulo eliminado ya no aparecerá en la lista.
                                    modulos = moduloDAO.obtenerModulos()
                                }
                            },

                            // Define el color rojo del botón Eliminar.
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF9B2424)
                            )
                        ) {

                            // Texto que aparece dentro del botón.
                            Text("Eliminar")
                        }
                    }
                }
            }
        }


        // Agrega un espacio vertical de 10 dp antes del botón Volver.
        Spacer(modifier = Modifier.height(10.dp))


        // Crea el botón que permite regresar al menú del profesor.
        Button(
            // Ejecuta la función recibida como parámetro.
            onClick = onVolver,

            // Hace que el botón ocupe todo el ancho disponible.
            modifier = Modifier.fillMaxWidth(),

            // Establece el color verde del botón.
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde
            )
        ) {

            // Texto que aparece dentro del botón.
            Text("Volver")
        }
    }
}
