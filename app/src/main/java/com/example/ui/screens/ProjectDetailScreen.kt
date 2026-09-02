package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ProjectEntity
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.DevIsrafilViewModel
import com.example.ui.viewmodel.Screen
import com.example.util.ZipManager
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProjectDetailScreen(
    viewModel: DevIsrafilViewModel,
    onNavigate: (Screen) -> Unit
) {
    val project by viewModel.selectedProject.collectAsState()
    val allProjects by viewModel.displayedProjects.collectAsState()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val dateFormat = remember { SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()) }

    if (project == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Project Not Found", fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
                Button(onClick = { onNavigate(Screen.PROJECTS) }) {
                    Text("Return to Projects")
                }
            }
        }
        return
    }

    val currentProj = project!!
    val files = remember(currentProj) { ZipManager.parseProjectFiles(currentProj.sourceFilesJson) }
    val primaryCodeFile = files.firstOrNull()

    val relatedProjects = remember(allProjects, currentProj) {
        allProjects.filter { it.id != currentProj.id && it.category == currentProj.category }.take(3)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Top Bar Back Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F172A))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.clickable { onNavigate(Screen.PROJECTS) }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = DevCyan
                )
                Text(
                    text = "Back to Projects",
                    color = DevCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Surface(
                color = DevCyan.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, DevCyan)
            ) {
                Text(
                    text = currentProj.category,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DevCyan,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        // Hero Thumbnail Banner
        Box(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, DevBorderGlass, RoundedCornerShape(24.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.hero_coding_banner),
                contentDescription = currentProj.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient tint
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0x990A0E17), Color(0xF00A0E17))
                        )
                    )
            )

            // Title inside Hero
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = currentProj.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Published on ${dateFormat.format(Date(currentProj.createdAt))}",
                    fontSize = 12.sp,
                    color = DevTextSecondary
                )
            }
        }

        // Metadata Spec Cards Grid
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SpecBadge(
                icon = Icons.Default.CloudDownload,
                label = "Downloads",
                value = "${currentProj.downloadCount}",
                color = DevCyan,
                modifier = Modifier.weight(1f)
            )
            SpecBadge(
                icon = Icons.Default.FolderZip,
                label = "Package Size",
                value = currentProj.fileSize,
                color = DevPurple,
                modifier = Modifier.weight(1f)
            )
            SpecBadge(
                icon = Icons.Default.Numbers,
                label = "Version",
                value = "v${currentProj.version}",
                color = DevEmerald,
                modifier = Modifier.weight(1f)
            )
        }

        // Major Action Buttons Grid
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Primary Download Button
            GradientButton(
                text = "📦 Download Source Code ZIP",
                icon = Icons.Default.Download,
                onClick = { viewModel.startProjectDownload(currentProj) },
                modifier = Modifier.fillMaxWidth()
            )

            // Secondary Actions Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Live Preview Button
                OutlinedButton(
                    onClick = { viewModel.openLivePreview(currentProj) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, DevCyan),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DevCyan)
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("🌐 Live Demo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Watch Tutorial Button
                OutlinedButton(
                    onClick = {
                        val url = currentProj.youtubeUrl.ifBlank { "https://youtube.com/@DEV_Israfil" }
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            viewModel.showToast("Opening tutorial: $url")
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFEF4444)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444))
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("🎥 Tutorial", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Copy Code & Source Explorer Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        if (primaryCodeFile != null) {
                            clipboardManager.setText(AnnotatedString(primaryCodeFile.content))
                            viewModel.showToast("Copied ${primaryCodeFile.name} code to clipboard!")
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = DevCyan, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("📋 Copy Code", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = { viewModel.openCodeExplorer(currentProj) },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E293B))
                ) {
                    Icon(imageVector = Icons.Default.FolderOpen, contentDescription = null, tint = DevPurple, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("📁 View All Files", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Project Description Section
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Project Description",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFF0F172A),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DevBorderGlass)
            ) {
                Text(
                    text = currentProj.description,
                    fontSize = 14.sp,
                    color = Color(0xFFCBD5E1),
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(18.dp)
                )
            }
        }

        // Technologies Used Section
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Technologies & Dependencies",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                currentProj.techList.forEach { tech ->
                    Surface(
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color(0xFF334155))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Terminal,
                                contentDescription = null,
                                tint = DevCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = tech,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Source Code Preview
        if (primaryCodeFile != null) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Source Code Preview",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Open Explorer (${files.size} files) →",
                        fontSize = 12.sp,
                        color = DevCyan,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { viewModel.openCodeExplorer(currentProj) }
                    )
                }

                CodeSnippetViewer(
                    filename = primaryCodeFile.name,
                    code = primaryCodeFile.content.take(800) + if (primaryCodeFile.content.length > 800) "\n\n// ... click 'View All Files' to view complete source repository" else "",
                    language = primaryCodeFile.language
                )
            }
        }

        // Related Projects Section
        if (relatedProjects.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Related Projects",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                relatedProjects.forEach { rel ->
                    ProjectCard(
                        project = rel,
                        onViewProject = { viewModel.openProjectDetails(rel) },
                        onQuickDownload = { viewModel.startProjectDownload(rel) }
                    )
                }
            }
        }

        // Footer
        DevFooter(onNavigate = onNavigate)
    }
}

@Composable
fun SpecBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xFF0F172A),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Color(0xFF1E293B))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = label, fontSize = 10.sp, color = DevTextMuted)
        }
    }
}
