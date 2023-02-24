package com.roshanadke.permissionhandlingjc

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.roshanadke.permissionhandlingjc.ui.theme.PermissionHandlingJCTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionHandlingJCTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    val permissionState = rememberMultiplePermissionsState(
                        permissions =
                        listOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                        )
                    )

                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(
                        key1 = lifecycleOwner,
                        effect = {
                            val observer = LifecycleEventObserver { _, event ->
                                if(event == Lifecycle.Event.ON_START) {
                                    permissionState.launchMultiplePermissionRequest()
                                }
                            }
                            lifecycleOwner.lifecycle.addObserver(observer)
                            onDispose {
                                lifecycleOwner.lifecycle.removeObserver(observer)
                            }
                        }
                    )

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        permissionState.permissions.forEach { permState ->

                            when (permState.permission) {
                                Manifest.permission.CAMERA -> {
                                    when {
                                        permState.hasPermission -> {
                                            Text(text = "Camera Permission Granted")
                                        }

                                        permState.shouldShowRationale -> {
                                            Text(text = "Please grant camera permission. It is Required to access camera.")
                                        }

                                        permState.isPermissionPermanentlyDeclined() -> {
                                            Text(text = "Camera permission permanently denied. Please go into settings and enable it.")

                                        }
                                    }
                                }

                                Manifest.permission.RECORD_AUDIO -> {
                                    when {
                                        permState.hasPermission -> {
                                            Text(text = "Record Audio Permission Granted")
                                        }

                                        permState.shouldShowRationale -> {
                                            Text(text = "Please grant record audio permission. It is Required to record audio.")
                                        }

                                        permState.isPermissionPermanentlyDeclined() -> {
                                            Text(text = "Record audio permission permanently denied. Please go into settings and enable it.")
                                        }
                                    }
                                }
                            }

                        }


                    }


                }
            }
        }
    }
}