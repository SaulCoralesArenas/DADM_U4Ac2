package edu.itvo.pets.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.itvo.pets.R
import edu.itvo.pets.presentation.composables.Pet
import edu.itvo.pets.presentation.screens.ListPetScreen
import edu.itvo.pets.presentation.ui.theme.PetsTheme
import kotlinx.coroutines.delay
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import edu.itvo.pets.presentation.composables.PetUpdateScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetsTheme {
                val navController = rememberNavController()
                val currentBackStackEntry = navController.currentBackStackEntryFlow.collectAsState(initial = null)
                val currentRoute = currentBackStackEntry.value?.destination?.route
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (currentRoute != "main_screen") { // Oculta el TopAppBar en "main_screen"
                            TopAppBar(
                                title = { Text("Pets") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        navController.navigate("main_screen")
                                    }) {
                                        Icon(
                                            imageVector = Icons.Filled.Pets,
                                            contentDescription = "Navegar a Lista"
                                        )
                                    }
                                },
                                actions = {
                                    IconButton(onClick = {
                                        navController.navigate("new_pet")
                                    },
                                        modifier = Modifier
                                            .padding(end = 2.dp)
                                            .width(120.dp)
                                    ) {
                                        Text("Nueva Mascota")
                                    }
                                    IconButton(onClick = {
                                        navController.navigate("list_pet")
                                    },
                                        modifier = Modifier
                                            .padding(end = 4.dp)
                                            .width(100.dp)
                                    ) {
                                        Text("Ver Lista")
                                    }
                                }
                            )
                        }
                    },
                    content = { innerPadding ->
                        NavHost(navController = navController, startDestination = "main_screen") {
                            composable("main_screen") {
                                MainScreen(navController)
                            }
                            composable("list_pet") {
                                ListPetScreen(
                                    modifier = Modifier.padding(innerPadding),
                                    navController = navController
                                )
                            }
                            composable("pet_update/{petId}") { backStackEntry ->
                                val petId = backStackEntry.arguments?.getString("petId")?.toInt() ?: 0
                                PetUpdateScreen(petId, navController, context = this@MainActivity)
                            }
                            composable("new_pet") {
                                Pet(
                                    modifier = Modifier.padding(innerPadding),
                                    context = this@MainActivity
                                )
                            }
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun MainScreen(navController: NavHostController) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.po),
                contentDescription = "Fondo Principal",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 16.dp, top = 32.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                Button(
                    onClick = { navController.navigate("new_pet") },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(4.dp)
                ) {
                    Text("Agregar Mascota")
                }
                Button(
                    onClick = { navController.navigate("list_pet") },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(4.dp)
                ) {
                    Text("Ver Lista")
                }
            }
        }
    }
}