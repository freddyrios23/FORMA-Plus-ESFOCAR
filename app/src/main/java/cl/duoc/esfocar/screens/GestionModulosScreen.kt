package cl.duoc.esfocar.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores de ESFOCAR
private val Verde = Color(0xFF17452F)
private val Fondo = Color(0xFFF6F7F3)

@Composable
fun GestionModulosScreen(
    onVolver: () -> Unit = {}
) {

    // Lista inicial de módulos de ejemplo
    val modulos = remember {
        mutableStateListOf(
            "Módulo 1: Introducción",
            "Módulo 2: Contenidos de estudio"
        )
    }

    // Guarda el texto que escribe el profesor
    var nombreModulo by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .padding(20.dp)
    ) {

        Text(
            text = "Gestionar módulos",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Verde
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo para escribir el nombre del módulo
        OutlinedTextField(
            value = nombreModulo,
            onValueChange = { nombreModulo = it },
            label = { Text("Nombre del módulo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Agrega el módulo a la lista
        Button(
            onClick = {
                if (nombreModulo.isNotBlank()) {
                    modulos.add(nombreModulo.trim())
                    nombreModulo = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde
            )
        ) {
            Text("Agregar módulo")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Módulos existentes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Verde
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Muestra los módulos de la lista
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(modulos) { indice, modulo ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {

                        Text(
                            text = modulo,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Elimina el módulo seleccionado
                        Button(
                            onClick = {
                                modulos.removeAt(indice)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF9B2424)
                            )
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Regresa al menú del profesor
        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Verde
            )
        ) {
            Text("Volver")
        }
    }
}
