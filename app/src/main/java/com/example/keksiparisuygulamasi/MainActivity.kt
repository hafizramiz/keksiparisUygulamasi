package com.example.keksiparisuygulamasi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.example.keksiparisuygulamasi.data.DataSource
import com.example.keksiparisuygulamasi.ui.theme.KekSiparisUygulamasiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            println("Data sour ce dan aldigim flavours: ${DataSource.flavors}")
            val dataSourceFlavours=DataSource.flavors.map { id-> LocalContext.current.resources.getString(id) }
            println("Eslesmis Data: ${dataSourceFlavours}")

            DataSource.quantityOptions.forEach{item-> println("icindeki itemlar:${item.first}") }
            KekSiparisUygulamasiTheme {
                CupcakeApp()
            }
        }
    }
}


