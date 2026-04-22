package com.appnotresponding.rumbo.ui.screens.map

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.appnotresponding.rumbo.models.sampleUser
import com.appnotresponding.rumbo.ui.components.organisms.chat.ChatPreviewData
import com.appnotresponding.rumbo.ui.templates.MapTemplate

@Composable
fun MapScreen(
    controller: NavHostController
){
    MapTemplate(sampleUser.copy(name = "Ana"), controller)
}