package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DevFooter
import com.example.ui.components.GradientButton
import com.example.ui.components.SocialIconPill
import com.example.ui.theme.*
import com.example.ui.viewmodel.DevIsrafilViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun ContactScreen(
    viewModel: DevIsrafilViewModel,
    onNavigate: (Screen) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        // Banner
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Contact & Community",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = "Have questions, feature requests, or custom project inquiries? Reach out directly to the DEV_Israfil engineering team.",
                fontSize = 13.sp,
                color = DevTextSecondary
            )
        }

        // Contact Form
        Surface(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            color = DevCardSurface,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, DevBorderGlass)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Send a Message",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Full Name") },
                    placeholder = { Text("Israfil Islam") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevCyan,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    placeholder = { Text("dev@example.com") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevCyan,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                // Message
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("Project Inquiry or Message") },
                    placeholder = { Text("Describe what you would like to ask or suggest...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DevCyan,
                        unfocusedBorderColor = Color(0xFF334155),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    maxLines = 6
                )

                GradientButton(
                    text = "Send Message",
                    icon = Icons.Default.Send,
                    onClick = {
                        if (name.isBlank() || email.isBlank() || message.isBlank()) {
                            viewModel.showToast("Please fill in all fields")
                        } else {
                            viewModel.submitContactMessage(name, email, message)
                            name = ""
                            email = ""
                            message = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Direct Social Channels
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Official Social & Support Channels",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            ChannelCard(
                icon = Icons.Default.PlayCircle,
                title = "YouTube Channel",
                handle = "@DEV_Israfil",
                desc = "Full project build tutorials, modern web design workflows, and coding breakdowns.",
                color = Color(0xFFFF0000),
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        viewModel.showToast("Opening YouTube...")
                    }
                }
            )

            ChannelCard(
                icon = Icons.Default.Send,
                title = "Telegram Community",
                handle = "t.me/DEV_Israfil",
                desc = "Instant source code drop alerts, community discussions, and direct developer Q&A.",
                color = Color(0xFF229ED9),
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.org"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        viewModel.showToast("Opening Telegram...")
                    }
                }
            )

            ChannelCard(
                icon = Icons.Default.Share,
                title = "Facebook Page",
                handle = "DEV_Israfil Official",
                desc = "Coding resource announcements, tech news, and live Q&A sessions.",
                color = Color(0xFF1877F2),
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com"))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        viewModel.showToast("Opening Facebook...")
                    }
                }
            )
        }

        // Footer
        DevFooter(onNavigate = onNavigate)
    }
}

@Composable
fun ChannelCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    handle: String,
    desc: String,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        color = Color(0xFF0F172A),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = handle, fontSize = 12.sp, color = color, fontWeight = FontWeight.SemiBold)
                }
                Text(text = desc, fontSize = 11.sp, color = DevTextSecondary)
            }

            Icon(imageVector = Icons.Default.OpenInNew, contentDescription = null, tint = DevTextMuted, modifier = Modifier.size(16.dp))
        }
    }
}
