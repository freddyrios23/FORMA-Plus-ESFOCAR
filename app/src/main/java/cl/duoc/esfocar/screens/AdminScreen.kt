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

// Componentes visuales.
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

// Estados de Jetpack Compose.
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

// Base de datos y modelo de usuario.
import cl.duoc.esfocar.data.UsuarioDAO
import cl.duoc.esfocar.data.model.Usuario

// Permite ejecutar operaciones sin bloquear la interfaz.
import kotlinx.coroutines.launch

// Pantalla para administrar profesores.
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
    var profesorEditando by remember {
        mutableStateOf<Usuario?>(null)
    }

    // Lista de profesores obtenida de la base de datos.
    var profesores by remember {
        mutableStateOf<List<Usuario>>(emptyList())
    }

    // Mensaje de confirmación o error.
    var mensaje by remember { mutableStateOf("") }

    // Profesor que se quiere eliminar.
    var profesorEliminar by remember {
        mutableStateOf<Usuario?>(null)
    }

    // Permite ejecutar operaciones de base de datos.
    val alcance = rememberCoroutineScope()

    // Carga la lista de profesores.
    fun cargarProfesores() {
        alcance.launch {
            profesores = usuarioDAO.obtenerProfesores()
        }
    }

    // Carga los profesores al abrir la pantalla.
    LaunchedEffect(usuarioDAO) {
        cargarProfesores()
    }

    // Limpia el formulario y cancela la edición.
    fun limpiarFormulario() {
        nombre = ""
        correo = ""
        clave = ""
        profesorEditando = null
        mensaje = ""
    }

    // Diálogo para confirmar la eliminación.
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
                        val seleccionado = profesorEliminar
                        profesorEliminar = null

                        if (seleccionado != null) {
                            alcance.launch {
                                usuarioDAO.eliminar(seleccionado)

                                // Actualiza la lista.
                                cargarProfesores()

                                // Limpia el formulario si se editaba esa cuenta.
                                if (profesorEditando?.id == seleccionado.id) {
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

    // Contenido principal de la pantalla.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Administrar profesores",
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Registra, consulta, edita o elimina cuentas de profesores."
        )

        // El título cambia según la operación.
        Text(
            text = if (profesorEditando == null) {
                "Registrar nuevo profesor"
            } else {
                "Editar profesor"
            },
            style = MaterialTheme.typography.titleMedium
        )

        // Campo del nombre.
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del profesor") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Campo del correo.
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // En edición, la contraseña es opcional.
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

        // Registra un profesor o guarda sus cambios.
        Button(
            onClick = {
                val nombreLimpio = nombre.trim()
                val correoLimpio = correo.trim()
                val profesorActual = profesorEditando

                // Comprueba los campos obligatorios.
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
                    mensaje = "Ingresa una contraseña."
                } else {
                    alcance.launch {
                        // Comprueba si el correo pertenece a otra cuenta.
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
                                // CREATE: crea un nuevo profesor.
                                val nuevoProfesor = Usuario(
                                    nombre = nombreLimpio,
                                    correo = correoLimpio,
                                    password = clave,
                                    rol = "PROFESOR"
                                )

                                usuarioDAO.insertar(nuevoProfesor)

                                mensaje = "Profesor registrado correctamente."
                            } else {
                                // UPDATE: modifica el profesor existente.
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

                            // Actualiza la lista y limpia el formulario.
                            cargarProfesores()
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

        // Permite cancelar la edición.
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

        // Muestra los mensajes.
        if (mensaje.isNotBlank()) {
            Text(text = mensaje)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de profesores registrados.
        Text(
            text = "Profesores registrados (${profesores.size})",
            style = MaterialTheme.typography.titleMedium
        )

        if (profesores.isEmpty()) {
            Text("Todavía no hay profesores registrados.")
        }

        // Muestra una tarjeta por cada profesor.
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
                    Text(
                        text = profesor.nombre,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(text = profesor.correo)
                    Text(text = "Rol: ${profesor.rol}")

                    // Botones para editar y eliminar.
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
                                clave = ""
                                mensaje = ""
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Editar")
                        }

                        OutlinedButton(
                            onClick = {
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

        // Regresa a la pantalla anterior.
        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al inicio")
        }
    }
}
