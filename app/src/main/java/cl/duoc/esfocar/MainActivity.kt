package cl.duoc.esfocar

// Herramientas básicas de Android.
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge


// Importa la pantalla para administrar cuentas de profesores.
import cl.duoc.esfocar.screens.AdminScreen
import cl.duoc.esfocar.screens.TeacherHomeScreen
import cl.duoc.esfocar.screens.GestionModulosScreen
import cl.duoc.esfocar.screens.CrearCuestionarioScreen
import cl.duoc.esfocar.screens.ResultadosScreen
import cl.duoc.esfocar.screens.ListaCuestionariosScreen

// Elementos de diseño de Jetpack Compose.
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState

// Estados que permiten actualizar la interfaz.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue

// Herramientas de formato y apariencia.
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Pantallas creadas en archivos separados.
import cl.duoc.esfocar.screens.AuthScreen
import cl.duoc.esfocar.screens.HomeScreen
import cl.duoc.esfocar.ui.theme.EsfocarTheme
import cl.duoc.esfocar.screens.ResponderCuestionarioScreen

// Permite acceder al contexto de la aplicación Android.
import androidx.compose.ui.platform.LocalContext

// Permite utilizar la base de datos y el modelo de usuario.
import cl.duoc.esfocar.data.local.DatabaseProvider
import cl.duoc.esfocar.data.model.Usuario

// Permite abrir y cerrar el menú lateral de forma asíncrona.
import kotlinx.coroutines.launch

// Permite ejecutar una tarea cuando se inicia la pantalla.
import androidx.compose.runtime.LaunchedEffect

// ----------------------------------------------------
// 1. COLORES DE FORMA+
// Centralizamos los colores para reutilizarlos.
// ----------------------------------------------------
private val VerdeForma = Color(0xFF17452F)
private val VerdeClaro = Color(0xFFE7F0E9)
private val DoradoForma = Color(0xFFC6A34A)
private val FondoForma = Color(0xFFF6F7F3)

// ----------------------------------------------------
// 2. PANTALLAS DISPONIBLES
// Identifica las pantallas disponibles en la aplicación.
private enum class Pantalla {
    Inicio,
    Estudio,
    Cuestionario,
    ResponderCuestionario,
    AdminProfesores,
    Profesor,
    GestionModulos,
    CrearCuestionario,
    Resultados
}

// ----------------------------------------------------
// 3. ACTIVIDAD PRINCIPAL DE ANDROID
// Android inicia la aplicación desde onCreate().
// ----------------------------------------------------
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que la interfaz aproveche el espacio de la pantalla.
        enableEdgeToEdge()

        // Inicia la interfaz usando el tema configurado del proyecto.
        setContent {
            EsfocarTheme {
                FormaApp()
            }
        }
    }
}

// ----------------------------------------------------
// 4. COMPONENTE PRINCIPAL DE LA APLICACIÓN
// Gestiona autenticación, navegación y menú lateral.
// ----------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormaApp() {

    // -----------------------------------------------
    // 1. CONEXIÓN CON LA BASE DE DATOS
    // -----------------------------------------------

    // Obtiene el contexto de la aplicación Android.
    val contexto = LocalContext.current

    // Crea la instancia de Room y la conserva durante
    // las recomposiciones de esta pantalla.
    val db = remember {
        DatabaseProvider.obtenerDatabase(contexto)
    }

    // Obtiene el DAO que permite insertar y buscar usuarios.
    val usuarioDAO = remember {
        db.usuarioDAO()
    }


// Indica si terminó la preparación inicial de la aplicación.
    var inicializacionLista by remember {
        mutableStateOf(false)
    }

// Comprueba si existe un administrador al iniciar ESFOCAR.
    LaunchedEffect(usuarioDAO) {

        // Si todavía no existe ningún administrador, crea la cuenta de prueba.
        if (!usuarioDAO.existeAdministrador()) {

            val administrador = Usuario(
                nombre = "Administrador",
                correo = "admin@gmail.com",
                password = "1234",
                rol = "ADMIN"
            )

            // Guarda la cuenta inicial en la base de datos.
            usuarioDAO.insertar(administrador)
        }

        // Permite mostrar la pantalla de inicio de sesión.
        inicializacionLista = true
    }


    // -----------------------------------------------
    // 2. ESTADOS DE AUTENTICACIÓN
    // -----------------------------------------------

    // Indica si existe una sesión iniciada.
    var sesionIniciada by remember {
        mutableStateOf(false)
    }

    // false = inicio de sesión; true = registro.
    var esRegistroAuth by remember {
        mutableStateOf(false)
    }

    // Nombre que se mostrará en el saludo del menú.
    var nombreUsuario by remember {
        mutableStateOf("")
    }

    // Guarda el rol del usuario que inició sesión.
    // Puede ser ESTUDIANTE o PROFESOR.
    var rolUsuario by remember {
        mutableStateOf("")
    }

    // Mensaje para errores de inicio de sesión o registro.
    var errorAuth by remember {
        mutableStateOf<String?>(null)
    }

    // -----------------------------------------------
    // 3. ESTADO DE NAVEGACIÓN
    // -----------------------------------------------

    // Define qué sección de la aplicación está abierta.
    var pantalla by remember {
        mutableStateOf(Pantalla.Inicio)
    }

    // Guarda el ID del cuestionario seleccionado.
    var cuestionarioSeleccionadoId by remember {
        mutableStateOf<Int?>(null)
    }

    // Controla si el menú lateral está abierto o cerrado.
    val estadoMenu = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    // Permite ejecutar operaciones asíncronas.
    val alcance = rememberCoroutineScope()

    // -----------------------------------------------
    // 4. MOSTRAR INICIO DE SESIÓN O REGISTRO
    // -----------------------------------------------

    // Espera a que termine la preparación inicial.
    if (!inicializacionLista) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Preparando ESFOCAR...")
        }

        return
    }

    // Si no hay sesión, mostramos el formulario de autenticación.
    if (!sesionIniciada) {

        AuthScreen(
            // Indica qué formulario debe aparecer.
            esRegistro = esRegistroAuth,

            // Envía los mensajes de error al formulario.
            mensajeError = errorAuth,

            // ---------------------------------------
            // INICIAR SESIÓN
            // ---------------------------------------
            onIniciarSesion = { correo, clave ->

                // Ejecuta la consulta sin bloquear la interfaz.
                alcance.launch {

                    // Busca una cuenta con el correo y la contraseña.
                    val usuario = usuarioDAO.buscarUsuario(
                        correo,
                        clave
                    )

                    // Comprueba si se encontró el usuario.
                    if (usuario != null) {

                        // Guarda el nombre para mostrarlo en el menú.
                        nombreUsuario = usuario.nombre

                        // Guarda el rol para mostrar las opciones correspondientes.
                        rolUsuario = usuario.rol

                        // Permite acceder a las pantallas principales.
                        sesionIniciada = true

                        // Limpia cualquier error anterior.
                        errorAuth = null

                        // Al entrar, mostramos Inicio.
                        pantalla = Pantalla.Inicio

                    } else {

                        // Informa si las credenciales no coinciden.
                        errorAuth = "Correo o contraseña incorrectos."
                    }
                }
            },

            // ---------------------------------------
            // REGISTRARSE
            // ---------------------------------------
            onRegistrarse = { nombre, correo, clave ->

                // Ejecuta las consultas y la inserción en segundo plano.
                alcance.launch {

                    // Comprueba si ya existe una cuenta con ese correo.
                    val usuarioExistente =
                        usuarioDAO.buscarPorCorreo(correo)

                    if (usuarioExistente != null) {

                        // Evita registrar dos veces el mismo correo.
                        errorAuth =
                            "Ya existe una cuenta con ese correo."

                    } else {

                        // Crea el objeto con los datos del nuevo usuario.
                        val nuevoUsuario = Usuario(
                            nombre = nombre,
                            correo = correo,
                            password = clave,
                            // El registro público siempre crea estudiantes.
                            rol = "ESTUDIANTE"
                        )

                        // Guarda el usuario en la base de datos SQLite.
                        usuarioDAO.insertar(nuevoUsuario)

                        // Guarda el nombre para mostrarlo en el menú.
                        nombreUsuario = nombre

                        // Guarda el rol de la cuenta recién creada.
                        rolUsuario = nuevoUsuario.rol

                        // Inicia la sesión después del registro.
                        sesionIniciada = true

                        // Limpia errores y vuelve a Inicio.
                        errorAuth = null
                        pantalla = Pantalla.Inicio
                    }
                }
            },

            // ---------------------------------------
            // CAMBIAR ENTRE LOS FORMULARIOS
            // ---------------------------------------
            onCambiarModo = {
                esRegistroAuth = !esRegistroAuth
                errorAuth = null
            }
        )

        // Evita mostrar el resto de la aplicación sin sesión.
        return
    }

    // -----------------------------------------------
    // 5. MENÚ LATERAL
    // Contiene los accesos a las distintas secciones.
    // -----------------------------------------------
    ModalNavigationDrawer(
        drawerState = estadoMenu,

        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    // Nombre de la aplicación.
                    Text(
                        text = "ESFOCAR",
                        color = DoradoForma,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Frase descriptiva.
                    Text(
                        text = "Tu aprendizaje, a tu ritmo",
                        color = VerdeForma
                    )

                    // Saludo personalizado para la sesión actual.
                    Text(
                        text = "Hola, $nombreUsuario",
                        color = VerdeForma,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Opción para regresar a la pantalla principal.
                    NavigationDrawerItem(
                        label = { Text("Inicio") },
                        selected = pantalla == Pantalla.Inicio,
                        onClick = {
                            pantalla = Pantalla.Inicio

                            // Cierra el menú después de elegir.
                            alcance.launch {
                                estadoMenu.close()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Esta opción solo aparece cuando inició sesión un administrador.
                    if (rolUsuario == "ADMIN") {

                        NavigationDrawerItem(
                            label = {
                                Text("Administrar profesores")
                            },
                            selected = pantalla == Pantalla.AdminProfesores,
                            onClick = {
                                // Abre la pantalla de administración.
                                pantalla = Pantalla.AdminProfesores

                                // Cierra el menú lateral.
                                alcance.launch {
                                    estadoMenu.close()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Las opciones de estudio solo aparecen para estudiantes.
                    if (rolUsuario == "ESTUDIANTE") {
                        // Acceso a los módulos de estudio.
                        NavigationDrawerItem(
                            label = { Text("Módulos de estudio") },
                            selected = pantalla == Pantalla.Estudio,
                            onClick = {
                                pantalla = Pantalla.Estudio

                                alcance.launch {
                                    estadoMenu.close()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Acceso a los cuestionarios.
                        NavigationDrawerItem(
                            label = { Text("Cuestionarios") },
                            selected = pantalla == Pantalla.Cuestionario,
                            onClick = {
                                pantalla = Pantalla.Cuestionario

                                alcance.launch {
                                    estadoMenu.close()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Opciones disponibles para el profesor.
                    if (rolUsuario == "PROFESOR") {

                        NavigationDrawerItem(
                            label = { Text("Menú del profesor") },
                            selected = pantalla == Pantalla.Profesor,
                            onClick = {
                                pantalla = Pantalla.Profesor

                                alcance.launch {
                                    estadoMenu.close()
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Cierra la sesión y vuelve al inicio de sesión.
                    NavigationDrawerItem(
                        label = { Text("Cerrar sesión") },
                        selected = false,
                        onClick = {
                            sesionIniciada = false
                            esRegistroAuth = false
                            nombreUsuario = ""
                            rolUsuario = ""
                            errorAuth = null

                            // Cierra el menú lateral.
                            alcance.launch {
                                estadoMenu.close()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) {

        // -------------------------------------------
        // 6. ESTRUCTURA PRINCIPAL DE LAS PANTALLAS
        // Scaffold organiza la barra superior y el contenido.
        // -------------------------------------------
        Scaffold(
            containerColor = FondoForma,

            // Barra superior con el botón del menú.
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 8.dp,
                            vertical = 8.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Botón para abrir o cerrar el menú lateral.
                    IconButton(
                        onClick = {
                            alcance.launch {
                                if (estadoMenu.isClosed) {
                                    estadoMenu.open()
                                } else {
                                    estadoMenu.close()
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "☰",
                            fontSize = 28.sp,
                            color = VerdeForma
                        )
                    }

                    // Título de la barra superior.
                    Text(
                        text = "ESFOCAR",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeForma
                    )
                }
            }
        ) { padding ->

            // Box ocupa el área disponible debajo de la barra.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                // Muestra una pantalla según el estado actual.
                when (pantalla) {

                    // Pantalla de inicio.
                    Pantalla.Inicio -> HomeScreen(
                        onEstudio = {
                            pantalla = Pantalla.Estudio
                        },
                        onCuestionario = {
                            pantalla = Pantalla.Cuestionario
                        }
                    )

                    // Pantalla de módulos de estudio.
                    Pantalla.Estudio -> EstudioScreen(
                        onVolver = {
                            pantalla = Pantalla.Inicio
                        }
                    )

                    Pantalla.Cuestionario -> ListaCuestionariosScreen(
                        onVolver = {
                            pantalla = Pantalla.Inicio
                        },
                        onSeleccionarCuestionario = { cuestionarioId ->
                            cuestionarioSeleccionadoId = cuestionarioId
                            pantalla = Pantalla.ResponderCuestionario
                        }
                    )

                    Pantalla.ResponderCuestionario -> {
                        val id = cuestionarioSeleccionadoId

                        if (id != null) {
                            ResponderCuestionarioScreen(
                                cuestionarioId = id,
                                onVolver = {
                                    pantalla = Pantalla.Cuestionario
                                }
                            )
                        } else {
                            ListaCuestionariosScreen(
                                onVolver = {
                                    pantalla = Pantalla.Inicio
                                },
                                onSeleccionarCuestionario = { cuestionarioId ->
                                    cuestionarioSeleccionadoId = cuestionarioId
                                    pantalla = Pantalla.ResponderCuestionario
                                }
                            )
                        }
                    }

                    // Pantalla para que el administrador registre profesores.
                    Pantalla.AdminProfesores -> {

                        // Verifica nuevamente el rol antes de mostrar la pantalla.
                        if (rolUsuario == "ADMIN") {

                            AdminScreen(
                                usuarioDAO = usuarioDAO,
                                onVolver = {
                                    // Regresa a Inicio.
                                    pantalla = Pantalla.Inicio
                                }
                            )

                        } else {

                            // Si no tiene permiso, no permite permanecer en esa pantalla.
                            pantalla = Pantalla.Inicio
                        }
                    }




                        // Pantalla principal del profesor.
                    Pantalla.Profesor -> {

                        // Comprueba que el usuario tenga el rol de profesor.
                        if (rolUsuario == "PROFESOR") {

                            // Muestra el menú del profesor y conecta sus botones.
                            TeacherHomeScreen(

                                // Abre la pantalla para gestionar módulos.
                                onModulos = {
                                    pantalla = Pantalla.GestionModulos
                                },

                                // Abre la pantalla para crear cuestionarios.
                                onCuestionarios = {
                                    pantalla = Pantalla.CrearCuestionario
                                },

                                // Abre la pantalla de resultados.
                                onResultados = {
                                    pantalla = Pantalla.Resultados
                                }
                            )

                        } else {

                            // Si no es profesor, regresa a la pantalla de inicio.
                            pantalla = Pantalla.Inicio
                        }
                    }

                            // Pantalla para gestionar los módulos de estudio.
                    Pantalla.GestionModulos -> {

                        // Muestra los módulos y permite volver al menú del profesor.
                        GestionModulosScreen(
                            onVolver = {

                                // Regresa al menú principal del profesor.
                                pantalla = Pantalla.Profesor
                            }
                        )
                    }

                        // Pantalla para crear cuestionarios.
                    Pantalla.CrearCuestionario -> {

                        // Muestra el formulario para crear preguntas.
                        CrearCuestionarioScreen(
                            onVolver = {

                                // Regresa al menú principal del profesor.
                                pantalla = Pantalla.Profesor
                            }
                        )
                    }

                        // Pantalla para consultar los resultados.
                    Pantalla.Resultados -> {

                        // Muestra los resultados de ejemplo.
                        ResultadosScreen(
                            onVolver = {

                                // Regresa al menú principal del profesor.
                                pantalla = Pantalla.Profesor
                            }
                        )
                    }

                }
            }
        }
    }
}


// ----------------------------------------------------
// 7. PANTALLA DE ESTUDIO
// Por ahora muestra un módulo de ejemplo.
// ----------------------------------------------------
@Composable
fun EstudioScreen(onVolver: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Módulos de estudio",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeForma
        )

        Text(
            text = "Selecciona un contenido para comenzar a repasar."
        )

        // Tarjeta que representa el primer módulo.
        Card(
            colors = CardDefaults.cardColors(
                containerColor = VerdeClaro
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Text(
                    text = "Módulo 1: Repaso de contenidos",
                    fontWeight = FontWeight.Bold,
                    color = VerdeForma
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Aquí incorporaremos material de estudio " +
                            "y contenidos educativos."
                )
            }
        }

        // Regresa a la pantalla de inicio.
        Button(
            onClick = onVolver,
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeForma,
                contentColor = Color.White
            )
        ) {
            Text("Volver al inicio")
        }
    }
}

// ----------------------------------------------------
// 8. PANTALLA DE CUESTIONARIO
// Permite seleccionar una respuesta y comprobarla.
// ----------------------------------------------------
@Composable
fun CuestionarioScreen(onVolver: () -> Unit) {

    // -1 indica que todavía no se eligió una alternativa.
    var respuesta by remember {
        mutableStateOf(-1)
    }

    // Indica si el usuario ya comprobó su respuesta.
    var corregido by remember {
        mutableStateOf(false)
    }

    // Alternativas de la pregunta de demostración.
    val alternativas = listOf(
        "Memorizar sin comprender.",
        "Estudiar, practicar y revisar los errores.",
        "Responder sin leer las preguntas."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Cuestionario de práctica",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = VerdeForma
        )

        Text(text = "Pregunta 1 de práctica")

        Text(
            text = "¿Qué estrategia ayuda a reforzar el aprendizaje?",
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold
        )

        // Crea una tarjeta por cada alternativa de la lista.
        alternativas.forEachIndexed { index, alternativa ->

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    // Resalta la alternativa seleccionada.
                    containerColor = if (respuesta == index) {
                        VerdeClaro
                    } else {
                        Color.White
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Permite seleccionar una alternativa.
                    RadioButton(
                        selected = respuesta == index,
                        onClick = {
                            respuesta = index

                            // Oculta el resultado anterior al cambiar.
                            corregido = false
                        }
                    )

                    Text(
                        text = alternativa,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }

        // Comprueba la respuesta elegida.
        Button(
            onClick = {
                corregido = true
            },
            // No permite comprobar si no se ha seleccionado nada.
            enabled = respuesta != -1,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeForma,
                contentColor = Color.White
            )
        ) {
            Text("Comprobar respuesta")
        }

        // Muestra la retroalimentación después de comprobar.
        if (corregido) {

            // La alternativa con índice 1 es la correcta.
            val correcta = respuesta == 1

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (correcta) {
                        VerdeClaro
                    } else {
                        Color(0xFFFFE8E6)
                    }
                )
            ) {
                Text(
                    text = if (correcta) {
                        "¡Correcto! Practicar y revisar los errores " +
                                "ayuda a reforzar el aprendizaje."
                    } else {
                        "Respuesta incorrecta. Revisa las alternativas " +
                                "e inténtalo nuevamente."
                    },
                    modifier = Modifier.padding(16.dp),
                    color = if (correcta) {
                        VerdeForma
                    } else {
                        Color(0xFF9B2424)
                    }
                )
            }
        }

        // Regresa a la pantalla de inicio.
        Button(
            onClick = onVolver,
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeForma,
                contentColor = Color.White
            )
        ) {
            Text("Volver al inicio")
        }
    }
}
