package com.appnotresponding.rumbo.ui.templates

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.appnotresponding.rumbo.R
import com.appnotresponding.rumbo.models.User
import com.appnotresponding.rumbo.models.samplePlace
import com.appnotresponding.rumbo.models.sampleReview
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.components.molecules.map.LocateMe
import com.appnotresponding.rumbo.ui.components.molecules.map.WriteDropNote
import com.appnotresponding.rumbo.ui.components.organisms.common.MainTopBar
import com.appnotresponding.rumbo.ui.components.organisms.common.Nav
import com.appnotresponding.rumbo.ui.components.organisms.map.DropNoteComposer
import com.appnotresponding.rumbo.ui.components.organisms.map.PlacePreviewCard
import com.appnotresponding.rumbo.ui.theme.RumboTheme

@Composable
fun MapTemplate(user: User,
                controller: NavHostController) {

    var popupStateDNComposer by remember { mutableStateOf(false) }
    var popupStateReview by remember { mutableStateOf(false) }


    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = { MainTopBar(user) },
        floatingActionButton = {
            Column(
                modifier = Modifier
                    .height(120.dp)
                    .width(45.dp),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                WriteDropNote {
                    popupStateDNComposer = !popupStateDNComposer }
                LocateMe { }
            }
        },
        bottomBar = { Nav(controller) }) { paddingValues ->
        // Main content area with the map
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier.fillMaxSize().clickable(onClick = {popupStateReview = !popupStateReview}),
                contentScale = ContentScale.FillHeight,
                painter = painterResource(R.drawable.img_map),
                contentDescription = "Map"
            )

        }
    }
    if (popupStateDNComposer) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(0.75f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            DropNoteComposer()
        }
    }
    if (popupStateReview){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp).offset(y=-(90).dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            PlacePreviewCard(place = samplePlace, reviews = listOf(sampleReview))
        }
    }
}

@Preview(showBackground = true, name = "PlacePreviewCard - Light")
@Composable
private fun MapTemplateLightPreview() {
    RumboTheme(darkTheme = true) {
        MapTemplate(sampleUser,
            controller = rememberNavController())
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E1E1E, name = "PlacePreviewCard - Dark")
@Composable
private fun MapTemplateDarkPreview() {
    RumboTheme(darkTheme = false) {
        MapTemplate(sampleUser,
            controller = rememberNavController())
    }
}