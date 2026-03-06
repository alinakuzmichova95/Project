package com.example.playlistmarket.presentation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.playlistmarket.R
import com.example.playlistmarket.ui.theme.PlaylistMarketTheme

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Добавляем отступ сверху через Spacer
        Spacer(modifier = Modifier.height(48.dp))

        // Кнопка "Назад"
        Button(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text("← Назад")
        }

        // Заголовок
        Text(
            text = stringResource(R.string.settings_title),
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Поделиться приложением
        Button(
            onClick = {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_text))
                    type = "text/plain"
                }
                context.startActivity(
                    Intent.createChooser(shareIntent, context.getString(R.string.settings_share_button))
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(stringResource(R.string.settings_share_button))
        }

        // Написать разработчикам
        Button(
            onClick = {
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:ryabuhin_av@itmoscow.pro".toUri()
                    putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject))
                    putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_body))
                }
                context.startActivity(emailIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(stringResource(R.string.settings_email_button))
        }

        // Пользовательское соглашение
        Button(
            onClick = {
                val url = context.getString(R.string.terms_url)
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    url.toUri()
                )
                context.startActivity(browserIntent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.settings_terms_button))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    PlaylistMarketTheme {
        SettingsScreen(
            onBackClick = {}
        )
    }
}