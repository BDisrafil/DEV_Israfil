package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DevFooter
import com.example.ui.components.DevLogo
import com.example.ui.components.GradientButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.DevIsrafilViewModel
import com.example.ui.viewmodel.Screen

@Composable
fun AboutScreen(
    viewModel: DevIsrafilViewModel,
    onNavigate: (Screen) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(28.dp)
    ) {
        // Hero Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0F172A), Color(0xFF080C14))
                    )
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                DevLogo()

                Text(
                    text = "About DEV_Israfil",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Text(
                    text = "DEV_Israfil is a coding resource platform where developers and learners can discover, explore and download useful source-code projects and web resources.",
                    fontSize = 14.sp,
                    color = DevTextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
        }

        // Mission & Values
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Our Mission & Vision",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = DevCardSurface,
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, DevBorderGlass)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FeatureRow(
                        icon = Icons.Default.Bolt,
                        title = "Production-Ready Source Code",
                        desc = "Every uploaded project includes authentic file structures, clean syntax, and verified configurations."
                    )
                    FeatureRow(
                        icon = Icons.Default.FolderZip,
                        title = "Instant Automatic ZIP Packaging",
                        desc = "Effortlessly package and download complete projects with accurate folder structures and README guidelines."
                    )
                    FeatureRow(
                        icon = Icons.Default.PlayCircle,
                        title = "Video Tutorials & Live Sandboxes",
                        desc = "Watch step-by-step developer tutorials and test live web demos before downloading."
                    )
                    FeatureRow(
                        icon = Icons.Default.Shield,
                        title = "Safe & Secure Developer Ecosystem",
                        desc = "Built with secure sandboxing, input sanitation, and zero-compromise file validation."
                    )
                }
            }
        }

        // Israfil Islam Author Section
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Created by Israfil Islam",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF0F172A),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, DevBorderGlass)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(DevGradientHero),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "DI",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = Color(0xFF030712)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Israfil Islam (DEV_Israfil)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Full-Stack Software Engineer & Content Creator dedicated to open-source developer tooling.",
                            fontSize = 12.sp,
                            color = DevTextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Call to action
        Box(
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            GradientButton(
                text = "Contact the Team / Suggest a Project",
                icon = Icons.Default.Email,
                onClick = { onNavigate(Screen.CONTACT) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Footer
        DevFooter(onNavigate = onNavigate)
    }
}

@Composable
fun FeatureRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF030712))
                .border(1.dp, DevCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = DevCyan, modifier = Modifier.size(18.dp))
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = desc, fontSize = 12.sp, color = DevTextSecondary, lineHeight = 16.sp)
        }
    }
}
