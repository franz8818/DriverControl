package com.example.drivercontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.drivercontrol.ui.theme.DriverControlTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            DriverControlTheme {
                MainScreen()
            }
        }
    }
}

data class Travel(
    val id: Int,
    val origin: String,
    val destination: String,
    val date: String,
    val value: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    var travels by remember {
        mutableStateOf(
            listOf(
                Travel(1, "Bogotá", "Chía", "18/05", "$25.000"),
                Travel(2, "Centro", "Usaquén", "18/05", "$18.000")
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = "Mis Viajes",
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Control diario de conductor",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Button(
                onClick = {
                    val newId =
                        (travels.maxOfOrNull { it.id } ?: 0) + 1

                    travels = travels + Travel(
                        newId,
                        "Nuevo origen",
                        "Nuevo destino",
                        "18/05",
                        "$0"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("➕ Crear viaje")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(travels, key = { it.id }) { travel ->

                    TravelCard(
                        travel = travel,
                        onDelete = {
                            travels = travels.filter {
                                it.id != travel.id
                            }
                        },
                        onEdit = {
                            travels = travels.map {
                                if (it.id == travel.id)
                                    it.copy(destination = "Editado")
                                else it
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TravelCard(
    travel: Travel,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                text = "${travel.origin} → ${travel.destination}",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text("Fecha: ${travel.date}")
            Text("Valor: ${travel.value}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween
            ) {

                TextButton(
                    onClick = onEdit
                ) {
                    Text("Editar")
                }

                TextButton(
                    onClick = onDelete
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    DriverControlTheme {
        MainScreen()
    }
}