
package cl.duoc.esfocar.screens

// Elementos que organizan el contenido de la pantalla.
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

// Componentes visuales de Material Design 3.
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

// Herramientas para crear funciones visuales y manejar estados.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

// Herramientas para configurar la apariencia de los componentes.
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores utilizados en la pantalla de autenticación.
private val VerdeAuth = Color(0xFF17452F)
private val DoradoAuth = Color(0xFFC6A34A)

// Construye la pantalla de inicio de sesión y registro.
// Recibe los datos de configuración y las acciones desde MainActivity.
@Composable
fun AuthScreen(
    esRegistro: Boolean, // true muestra el registro; false muestra el inicio de sesión.
    mensajeError: String?, // Error que puede enviar MainActivity.

    // Función que recibe el correo y la contraseña al iniciar sesión.
    onIniciarSesion: (String, String) -> Unit,

    // Función que recibe nombre, correo y contraseña al registrarse.
    onRegistrarse: (String, String, String) -> Unit,

    // Función que cambia entre los dos formularios.
    onCambiarModo: () -> Unit
) {
    // Estados que guardan lo escrito en los campos.
    // rememberSaveable ayuda a conservarlos ante cambios de configuración.
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var confirmarClave by rememberSaveable { mutableStateOf("") }

    // Guarda los errores generados por las validaciones locales.
    var errorLocal by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    // Organiza los componentes verticalmente y permite desplazarse.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Nombre y descripción de la aplicación.
        Text(
            text = "FORMA+",
            color = DoradoAuth,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tu aprendizaje, a tu ritmo",
            color = VerdeAuth,
            fontSize = 17.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Tarjeta que agrupa los campos y botones del formulario.
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // El título cambia según el formulario seleccionado.
                Text(
                    text = if (esRegistro) {
                        "Crear una cuenta"
                    } else {
                        "Iniciar sesión"
                    },
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeAuth
                )

                // El nombre solo se solicita al crear una cuenta.
                if (esRegistro) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = {
                            nombre = it
                            errorLocal = null
                        },
                        label = { Text("Nombre completo") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // Campo para escribir el correo electrónico.
                OutlinedTextField(
                    value = correo,
                    onValueChange = {
                        correo = it
                        errorLocal = null
                    },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Campo de contraseña: oculta los caracteres escritos.
                OutlinedTextField(
                    value = clave,
                    onValueChange = {
                        clave = it
                        errorLocal = null
                    },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // La confirmación de contraseña solo aparece al registrarse.
                if (esRegistro) {
                    OutlinedTextField(
                        value = confirmarClave,
                        onValueChange = {
                            confirmarClave = it
                            errorLocal = null
                        },
                        label = { Text("Confirmar contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                // Prioriza el error local; si no existe, muestra el de MainActivity.
                val mensaje = errorLocal ?: mensajeError

                if (mensaje != null) {
                    Text(
                        text = mensaje,
                        color = Color(0xFFB3261E)
                    )
                }

                // Botón principal para registrarse o iniciar sesión.
                Button(
                    onClick = {
                        // Elimina espacios del correo y lo convierte a minúsculas.
                        val email = correo.trim().lowercase()

                        // Valida los datos antes de enviarlos a MainActivity.
                        errorLocal = when {
                            esRegistro && nombre.isBlank() ->
                                "Ingresa tu nombre completo."

                            email.isBlank() ||
                                    !android.util.Patterns.EMAIL_ADDRESS
                                        .matcher(email).matches() ->
                                "Ingresa un correo electrónico válido."

                            clave.isBlank() ->
                                "Ingresa tu contraseña."

                            esRegistro && clave.length < 8 ->
                                "La contraseña debe tener al menos 8 caracteres."

                            esRegistro && clave != confirmarClave ->
                                "Las contraseñas no coinciden."

                            // Si no se cumple ninguna condición, no hay error.
                            else -> null
                        }

                        // Solo continúa cuando todas las validaciones son correctas.
                        if (errorLocal == null) {
                            if (esRegistro) {
                                // Envía los tres datos para registrar al usuario.
                                onRegistrarse(nombre.trim(), email, clave)
                            } else {
                                // Envía el correo y la contraseña para iniciar sesión.
                                onIniciarSesion(email, clave)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VerdeAuth,
                        contentColor = Color.White
                    )
                ) {
                    // Cambia el texto según el modo del formulario.
                    Text(if (esRegistro) "Registrarme" else "Entrar")
                }

                // Botón secundario para alternar entre registro e inicio de sesión.
                TextButton(
                    onClick = {
                        errorLocal = null
                        onCambiarModo()
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = if (esRegistro) {
                            "¿Ya tienes cuenta? Inicia sesión"
                        } else {
                            "¿No tienes cuenta? Regístrate"
                        },
                        color = VerdeAuth
                    )
                }
            }
        }
    }
}
