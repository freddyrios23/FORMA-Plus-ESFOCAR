package cl.duoc.esfocar.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultadosScreen(onVolver: () -> Unit = {}) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text(
            text = "Resultados de estudiantes",
            fontSize = 24.sp,
            color = Color(0xFF17452F)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Estudiante: Juan")
        Text("Cuestionario: Introducción")
        Text("Puntaje: 80%")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onVolver) {
            Text("Volver")
        }
    }
}


