
package cl.duoc.esfocar.screens

// Componentes para organizar la pantalla.
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

// Componentes visuales de Material Design.
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

// Estados y efectos de Jetpack Compose.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

// Herramientas visuales.
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Acceso a la base de datos y al modelo.
import cl.duoc.esfocar.data.UsuarioDAO
import cl.duoc.esfocar.data.model.Usuario

// Ejecuta operaciones sin bloquear la interfaz.
import kotlinx.coroutines.launch

// Pantalla CRUD para administrar profesores.
@Composable
fun AdminScreen(
    usuarioDAO: UsuarioDAO,
    onVolver: () -> Unit
) {
    // Datos escritos en el formulario.
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    // Profesor que se está editando.
    // Si es null, el formulario crea una cuenta nueva.
    var profesorEditando by remember {
        mutableStateOf<Usuario?>(null)
    }

    // Lista de profesores cargada desde Room.
    var profesores by remember {
        mutableStateOf<List<Usuario>>(emptyList())
    }

    // Mensajes de confirmación o error.
    var mensaje by remember { mutableStateOf("") }

    // Controla el diálogo de confirmación de eliminación.
    var profesorEliminar by remember {
        mutableStateOf<Usuario?>(null)
    }

    // Permite ejecutar consultas de manera asíncrona.
    val alcance = rememberCoroutineScope()

    // Carga la lista de profesores desde la base de datos.
    fun cargarProfesores() {
        alcance.launch {
            profesores = usuarioDAO.obtenerProfesores()
        }
    }

    // Carga los profesores cuando se abre la pantalla.
    LaunchedEffect(usuarioDAO) {
        profesores = usuarioDAO.obtenerProfesores()
    }

    // Limpia el formulario y sale del modo edición.
    fun limpiarFormulario() {
        nombre = ""
        correo = ""
        clave = ""
        profesorEditando = null
        mensaje = ""
    }

    // Diálogo para confirmar antes de borrar una cuenta.
    if (profesorEliminar != null) {
        AlertDialog(
            onDismissRequest = {
                profesorEliminar = null
            },
            title = {
                Text("Eliminar profesor")
            },
            text = {
                Text(
                    "¿Seguro que deseas eliminar la cuenta de " +
                            "${profesorEliminar?.nombre}? Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Guarda el profesor seleccionado.
                        val seleccionado = profesorEliminar

                        // Cierra el diálogo.
                        profesorEliminar = null

                        // Elimina solamente la cuenta seleccionada.
                        if (seleccionado != null) {
                            alcance.launch {
                                usuarioDAO.eliminar(seleccionado)

                                // Actualiza la lista después de eliminar.
                                profesores = usuarioDAO.obtenerProfesores()

                                // Si se estaba editando esa cuenta,
                                // limpia el formulario.
                                if (
                                    profesorEditando?.id ==
                                    seleccionado.id
                                ) {
                                    limpiarFormulario()
                                }

                                mensaje = "Profesor eliminado correctamente."
                            }
                        }
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        profesorEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Contenido principal desplazable.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Título de la pantalla.
        Text(
            text = "Administrar profesores",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Registra, consulta, edita o elimina cuentas de profesores."
        )

        // Cambia el título según la operación seleccionada.
        Text(
            text = if (profesorEditando == null) {
                "Registrar nuevo profesor"
            } else {
                "Editar profesor"
            },
            style = MaterialTheme.typography.titleMedium
        )

        // Campo para el nombre.
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del profesor") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Campo para el correo.
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // En edición, la contraseña puede dejarse vacía
        // para conservar la contraseña que ya tenía la cuenta.
        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = {
                Text(
                    if (profesorEditando == null) {
                        "Contraseña"
                    } else {
                        "Nueva contraseña (opcional)"
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Guarda una cuenta nueva o actualiza la existente.
        Button(
            onClick = {
                // Normaliza los datos escritos.
                val nombreLimpio = nombre.trim()
                val correoLimpio = correo.trim()
                val profesorActual = profesorEditando

                // Valida los campos obligatorios.
                if (
                    nombreLimpio.isBlank() ||
                    correoLimpio.isBlank()
                ) {
                    mensaje = "Completa el nombre y el correo."
                } else if (
                    !correoLimpio.contains("@") ||
                    !correoLimpio.substringAfter("@").contains(".")
                ) {
                    mensaje = "Ingresa un correo válido."
                } else if (
                    profesorActual == null && clave.isBlank()
                ) {
                    // Para crear una cuenta, la contraseña es obligatoria.
                    mensaje = "Ingresa una contraseña."
                } else {
                    // Ejecuta las consultas y cambios en Room.
                    alcance.launch {
                        // Impide usar un correo de otra cuenta.
                        val correoDuplicado =
                            if (profesorActual == null) {
                                usuarioDAO.buscarPorCorreo(correoLimpio) != null
                            } else {
                                usuarioDAO.correoExisteEnOtroUsuario(
                                    correo = correoLimpio,
                                    id = profesorActual.id
                                )
                            }

                        if (correoDuplicado) {
                            mensaje = "Ese correo ya está registrado."
                        } else {
                            if (profesorActual == null) {
                                // CREATE: crea un profesor nuevo.
                                val nuevoProfesor = Usuario(
                                    nombre = nombreLimpio,
                                    correo = correoLimpio,
                                    password = clave,
                                    rol = "PROFESOR"
                                )

                                usuarioDAO.insertar(nuevoProfesor)

                                mensaje = "Profesor registrado correctamente."
                            } else {
                                // UPDATE: conserva el ID y el rol.
                                // Si la contraseña queda vacía,
                                // mantiene la contraseña anterior.
                                val profesorActualizado = profesorActual.copy(
                                    nombre = nombreLimpio,
                                    correo = correoLimpio,
                                    password = if (clave.isBlank()) {
                                        profesorActual.password
                                    } else {
                                        clave
                                    }
                                )

                                usuarioDAO.actualizar(profesorActualizado)

                                mensaje = "Profesor actualizado correctamente."
                            }

                            // Recarga la lista con los datos actuales.
                            profesores = usuarioDAO.obtenerProfesores()

                            // Limpia el formulario tras guardar.
                            limpiarFormulario()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (profesorEditando == null) {
                    "Registrar profesor"
                } else {
                    "Guardar cambios"
                }
            )
        }

        // Permite cancelar una edición.
        if (profesorEditando != null) {
            OutlinedButton(
                onClick = {
                    limpiarFormulario()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar edición")
            }
        }

        // Informa el resultado de las operaciones.
        if (mensaje.isNotBlank()) {
            Text(text = mensaje)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // READ: lista de profesores registrados.
        Text(
            text = "Profesores registrados (${profesores.size})",
            style = MaterialTheme.typography.titleMedium
        )

        if (profesores.isEmpty()) {
            Text("Todavía no hay profesores registrados.")
        }

        // Crea una tarjeta por cada profesor.
        profesores.forEach { profesor ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Muestra los datos de la cuenta.
                    Text(
                        text = profesor.nombre,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(text = profesor.correo)
                    Text(text = "Rol: ${profesor.rol}")

                    // Acciones para editar o eliminar la cuenta.
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                // Carga los datos en el formulario.
                                profesorEditando = profesor
                                nombre = profesor.nombre
                                correo = profesor.correo

                                // No mostramos la contraseña actual.
                                clave = ""
                                mensaje = ""
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Editar")
                        }

                        OutlinedButton(
                            onClick = {
                                // Solicita confirmación antes de eliminar.
                                profesorEliminar = profesor
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Regresa a la pantalla principal.
        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al inicio")
        }
    }
}
