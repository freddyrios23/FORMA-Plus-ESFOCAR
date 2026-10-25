package cl.duoc.esfocar.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.esfocar.data.local.DatabaseProvider
import cl.duoc.esfocar.data.model.Cuestionario
import kotlinx.coroutines.launch

private val VerdeCuestionario = Color(0xFF17452F)

@Composable
fun ListaCuestionariosScreen(
    onVolver: () -> Unit,
    onSeleccionarCuestionario: (Int) -> Unit
) {
    val contexto = LocalContext.current
    val alcance = rememberCoroutineScope()

    val cuestionarioDAO = remember {
        DatabaseProvider.obtenerDatabase(contexto).cuestionarioDAO()
    }

    var cuestionarios by remember {
        mutableStateOf<List<Cuestionario>>(emptyList())
    }

    var cargando by remember {
        mutableStateOf(true)
    }

    // Carga los cuestionarios guardados en Room.
    androidx.compose.runtime.LaunchedEffect(Unit) {
        alcance.launch {
            try {
                cuestionarios = cuestionarioDAO.obtenerCuestionarios()
            } catch (e: Exception) {
                cuestionarios = emptyList()
            } finally {
                cargando = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Cuestionarios disponibles",
            fontSize = 26.sp,
            color = VerdeCuestionario
        )

        when {
            cargando -> {
                Text("Cargando cuestionarios...")
            }

            cuestionarios.isEmpty() -> {
                Text("Todavía no hay cuestionarios guardados.")
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(cuestionarios) { cuestionario ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = cuestionario.titulo,
                                    fontSize = 19.sp,
                                    color = VerdeCuestionario
                                )

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Button(
                                    onClick = {
                                        onSeleccionarCuestionario(
                                            cuestionario.id
                                        )
                                    }
                                ) {
                                    Text("Comenzar cuestionario")
                                }
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al inicio")
        }
    }
}
