package com.appnotresponding.rumbo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.appnotresponding.rumbo.navigation.Navigation
import com.appnotresponding.rumbo.ui.theme.RumboTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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