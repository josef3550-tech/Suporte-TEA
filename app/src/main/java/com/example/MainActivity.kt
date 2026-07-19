package com.example

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ui.screens.MainScreen
import com.example.ui.theme.MyApplicationTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Text-To-Speech
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale("pt", "BR"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Fallback to device's default locale if pt-BR is missing
                    tts?.setLanguage(Locale.getDefault())
                    Toast.makeText(
                        this,
                        "Idioma Português não disponível na síntese de voz. Usando padrão.",
                        Toast.LENGTH_LONG
                    ).show()
                }
                isTtsInitialized = true
            } else {
                Toast.makeText(
                    this,
                    "Erro ao inicializar síntese de voz (TTS).",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // MainScreen handles all state and tab options
                    MainScreen(
                        onSpeak = { text -> speak(text) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    private fun speak(text: String) {
        if (isTtsInitialized && tts != null) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "SuporteTeaUtteranceId")
        } else {
            Toast.makeText(this, "Aguardando síntese de voz...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}

