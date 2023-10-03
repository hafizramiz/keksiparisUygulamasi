package com.example.keksiparisuygulamasi

import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.keksiparisuygulamasi.data.DataSource
import com.example.keksiparisuygulamasi.ui.OrderSummaryPreview
import com.example.keksiparisuygulamasi.ui.OrderSummaryScreen
import com.example.keksiparisuygulamasi.ui.OrderViewModel
import com.example.keksiparisuygulamasi.ui.SelectOptionScreen
import com.example.keksiparisuygulamasi.ui.StartOrderScreen


enum class CupcakeScreen {
    Start,
    Flavor,
    Pickup,
    Summary
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CupcakeAppBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Back Button")
                }
                println("Navigate bback")
            }

        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CupcakeApp(
    myViewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()

) {
    // Bu metot arka tarafda bir onceki ekranda yani backstack te bir Composable var mi yok mu
//    onu gosteriyor.
    val backStackEntery by navController.currentBackStackEntryAsState()
    CupcakeScreen.valueOf(backStackEntery?.destination?.route?: CupcakeScreen.Start.name)
    Scaffold(topBar = {
        CupcakeAppBar(canNavigateBack = false, navigateUp = {
        })
    }) {
        println(it)
        val uiState by myViewModel.uiState.collectAsState()
        NavHost(
            navController = navController,
            startDestination = CupcakeScreen.Start.name,
            modifier = Modifier.padding(it)
        ) {
            composable(route = CupcakeScreen.Start.name) {
                StartOrderScreen(
                    onNextButtonClicked = {
                        myViewModel.setQuantity(it)
                        navController.navigate(CupcakeScreen.Flavor.name)
                        println("on Next Button Clicked call backten gelen veri: $it")
                    },
                    quantityOptions = DataSource.quantityOptions,
                    navController = navController
                )
            }

            composable(route = CupcakeScreen.Flavor.name) {
                val context = LocalContext.current
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = DataSource.flavors.map { id -> context.resources.getString(id) },
                    onSelectionChanged = { myViewModel.setFlavor(it) },
                    onNextButtonClicked = { navController.navigate(CupcakeScreen.Pickup.name) },
                    onCancelButtonClicked = {
                        // Cancel yaparsa bunu kullanacagiz. Bunu sonra yazcam.
                    }
                )
            }
            composable(route = CupcakeScreen.Pickup.name) {
                SelectOptionScreen(
                    subtotal = uiState.price,
                    options = uiState.pickupOptions,
                    onNextButtonClicked = {
                        navController.navigate(CupcakeScreen.Summary.name)
                    },
                    onSelectionChanged = { myViewModel.setDate(it) },
                    modifier = Modifier.fillMaxHeight(),
                    onCancelButtonClicked = {
                        // Bunu da sonra yazcam
                        println("Cancel calisti")
                        myViewModel.resetOrder()
                        navController.popBackStack(CupcakeScreen.Start.name, false)

                    }
                )
            }
            composable(route = CupcakeScreen.Summary.name) {
                val context = LocalContext.current

                OrderSummaryScreen(
                    orderUiState = uiState,
                    onCancelButtonClicked = {
                        println("Cancel butonu calisti")
                        // Geri gelmek icin kullanilan navigasyon yontemi navController.popBackStack()
                        // dir. Bu iki parametre alir:
                        // 1. Route gidecegi rotayi yazariz.
                        // 2. Bir boolean deger inclusive: False or True
                        // Eger true ise hangi ekrandan bu ekrana geldiysek o ekrani da stack'den
                        // kaldirir.Eger false ise bu ekrani cagiran ekrani stack'te tutar geri kalani
                        // stack'ten siler
                        navController.popBackStack(CupcakeScreen.Start.name, false)
                    },
                    onSendButtonClicked = { subject: String, summary: String ->
                        // Intent ile baska uygulama icin veri paylaasimi yapabiliriz.
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, subject)
                            putExtra(Intent.EXTRA_SUBJECT, summary)
                        }
                        context.startActivity(
                            Intent.createChooser(
                                intent,
                                context.getString(R.string.new_cupcake_order)
                            )
                        )
                    }
                )
            }
        }
    }

}


private fun cancelOrderAndNavigateToStart(
    orderViewModel: OrderViewModel,
    navController: NavHostController
) {
    orderViewModel.resetOrder()
    navController.popBackStack(CupcakeScreen.Start.name, false)
}

@Preview
@Composable
fun KekAppPreview() {
    CupcakeApp()
}