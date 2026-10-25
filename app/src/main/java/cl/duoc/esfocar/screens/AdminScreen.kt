
package cl.duoc.esfocar.screens

// Componentes para organizar los elementos de la pantalla.
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

// Componentes visuales de Material Design.
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme

// Herramientas de Jetpack Compose para mantener el estado.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

// Herramientas de diseño.
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Base de datos y modelo de usuario.
import cl.duoc.esfocar.data.UsuarioDAO
import cl.duoc.esfocar.data.model.Usuario

// Permite ejecutar consultas sin bloquear la interfaz.
import kotlinx.coroutines.launch

// Pantalla donde el administrador registra profesores.
@Composable
fun AdminScreen(
    usuarioDAO: UsuarioDAO,
    onVolver: () -> Unit
) {
    // Guarda lo que se escribe en cada campo.
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    // Guarda los mensajes que se mostrarán al usuario.
    var mensaje by remember { mutableStateOf("") }

    // Permite ejecutar operaciones de base de datos.
    val alcance = rememberCoroutineScope()

    // Organiza los campos y botones verticalmente.
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

        // Explica la función de esta sección.
        Text(
            text = "Registra las cuentas de los profesores autorizados."
        )

        // Campo para el nombre del profesor.
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del profesor") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Campo para el correo de inicio de sesión.
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Campo para la contraseña de la cuenta.
        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Botón que registra una cuenta con el rol PROFESOR.
        Button(
            onClick = {
                // Limpia los espacios sobrantes de los datos.
                val nombreLimpio = nombre.trim()
                val correoLimpio = correo.trim()

                // Comprueba que no haya campos vacíos.
                if (
                    nombreLimpio.isBlank() ||
                    correoLimpio.isBlank() ||
                    clave.isBlank()
                ) {
                    mensaje = "Completa todos los campos."
                } else if (
                    !correoLimpio.contains("@") ||
                    !correoLimpio.substringAfter("@").contains(".")
                ) {
                    // Comprueba de forma básica el formato del correo.
                    mensaje = "Ingresa un correo electrónico válido."
                } else {
                    // Ejecuta las consultas de Room en una corrutina.
                    alcance.launch {
                        // Evita registrar un correo que ya existe.
                        val existente =
                            usuarioDAO.buscarPorCorreo(correoLimpio)

                        if (existente != null) {
                            mensaje = "Ya existe una cuenta con ese correo."
                        } else {
                            // Crea la cuenta con permisos de profesor.
                            val profesor = Usuario(
                                nombre = nombreLimpio,
                                correo = correoLimpio,
                                password = clave,
                                rol = "PROFESOR"
                            )

                            // Guarda el profesor en la base de datos.
                            usuarioDAO.insertar(profesor)

                            // Informa que el registro terminó correctamente.
                            mensaje = "Profesor registrado correctamente."

                            // Limpia los campos para el siguiente registro.
                            nombre = ""
                            correo = ""
                            clave = ""
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar profesor")
        }

        // Muestra los mensajes de validación o confirmación.
        if (mensaje.isNotBlank()) {
            Text(text = mensaje)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Permite regresar a la pantalla principal.
        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al inicio")
        }
    }
}
