package com.appnotresponding.rumbo

import android.location.Geocoder
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.appnotresponding.rumbo.navigation.Navigation
import com.appnotresponding.rumbo.ui.theme.RumboTheme
import com.google.firebase.auth.FirebaseAuth
import org.osmdroid.bonuspack.routing.OSRMRoadManager

lateinit var auth: FirebaseAuth
lateinit var geocoder: Geocoder
lateinit var roadManager: OSRMRoadManager
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        roadManager = OSRMRoadManager(this, "ANDROID")
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        enableEdgeToEdge()
        setContent {
            // Se usa la integración de coil para mejorar el rendimiento de carga de imágenes, especialmente para listas con muchas imágenes docs: https://coil-kt.github.io/coil/network/
            setSingletonImageLoaderFactory { context ->
                ImageLoader.Builder(context).components {
                    add(OkHttpNetworkFetcherFactory())
                }.build()
            }
            RumboTheme {
                Navigation()
            }
        }
    }
}