package cl.duoc.esfocar.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores de ESFOCAR
private val Verde = Color(0xFF17452F)
private val Dorado = Color(0xFFC6A34A)
private val Fondo = Color(0xFFF6F7F3)

@Composable
fun TeacherHomeScreen(
    onModulos: () -> Unit = {},
    onCuestionarios: () -> Unit = {},
    onResultados: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Título de la aplicación
        Text(
            text = "ESFOCAR",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Verde
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Menú del profesor",
            fontSize = 22.sp,
            color = Verde
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Opción para administrar módulos
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onModulos() },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Gestionar módulos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Verde
                )

                Text(
                    text = "Crear y administrar módulos de estudio"
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Opción para crear cuestionarios
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCuestionarios() },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Crear cuestionarios",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Verde
                )

                Text(
                    text = "Agregar preguntas y alternativas"
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Opción para revisar resultados
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onResultados() },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Ver resultados",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Verde
                )

                Text(
                    text = "Revisar los puntajes de los estudiantes"
                )
            }
        }
    }
}
