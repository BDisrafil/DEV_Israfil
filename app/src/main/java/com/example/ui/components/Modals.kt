package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ProjectEntity
import com.example.data.model.ProjectFile
import com.example.ui.theme.*
import com.example.util.ZipManager
import java.io.File

@Composable
fun DownloadProgressModal(
    project: ProjectEntity,
    progress: Float,
    downloadedFile: File?,
    onDismiss: () -> Unit,
    onShareFile: (File) -> Unit
) {
    val context = LocalContext.current
    val files = remember(project) { ZipManager.parseProjectFiles(project.sourceFilesJson) }

    Dialog(
        onDismissRequest = { if (progress >= 1f) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .background(DevCardSurface)
                .border(1.dp, DevBorderGlass, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Icon
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(if (progress >= 1f) DevEmerald.copy(alpha = 0.2f) else DevCyan.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (progress >= 1f) Icons.Default.CheckCircle else Icons.Default.CloudDownload,
                        contentDescription = null,
                        tint = if (progress >= 1f) DevEmerald else DevCyan,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = if (progress >= 1f) "Download Complete!" else "Packaging & Downloading...",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "DEV_Israfil-${project.slug}.zip (${project.fileSize})",
                    fontSize = 13.sp,
                    color = DevTextSecondary,
                    fontFamily = FontFamily.Monospace
                )

                // Progress Bar
                Column(modifier = Modifier.fillMaxWidth()) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (progress >= 1f) DevEmerald else DevCyan,
                        trackColor = Color(0xFF1E293B)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (progress >= 1f) "Verified ZIP integrity" else "Creating archive structure...",
                            fontSize = 11.sp,
                            color = DevTextMuted
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DevCyan
                        )
                    }
                }

                // Extracted Files Preview List
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 140.dp),
                    color = Color(0xFF090D16),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF1E293B))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Archive Contents (${files.size} files):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DevTextSecondary
                        )
                        files.forEach { file ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.InsertDriveFile,
                                    contentDescription = null,
                                    tint = DevCyan,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = file.path,
                                    fontSize = 11.sp,
                                    color = Color(0xFFCBD5E1),
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }

                // Actions
                if (progress >= 1f && downloadedFile != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = DevTextSecondary)
                        ) {
                            Text("Close")
                        }

                        Button(
                            onClick = { onShareFile(downloadedFile) },
                            modifier = Modifier.weight(1.3f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DevCyan)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = Color(0xFF030712),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Open / Share",
                                color = Color(0xFF030712),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LivePreviewModal(
    project: ProjectEntity,
    onDismiss: () -> Unit
) {
    val files = remember(project) { ZipManager.parseProjectFiles(project.sourceFilesJson) }
    val htmlFile = files.find { it.name.endsWith(".html") } ?: files.firstOrNull()
    val jsFile = files.find { it.name.endsWith(".js") }
    var activeTab by remember { mutableStateOf("preview") } // "preview" or "console"
    var consoleLogs by remember { mutableStateOf(listOf("[DEV_Israfil Simulator] Initializing runtime sandbox...", "[DEV_Israfil] Loaded resources for '${project.title}'", "[DEV_Israfil] DOM Ready: Simulation active.")) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF080C14))
                .border(1.dp, DevBorderGlass, RoundedCornerShape(24.dp))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Browser Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F172A))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF10B981)))
                        }
                        // Address Bar
                        Surface(
                            modifier = Modifier
                                .widthIn(min = 160.dp, max = 220.dp)
                                .height(28.dp),
                            color = Color(0xFF030712),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = DevEmerald,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = "https://preview.devisrafil.com/${project.slug}",
                                    fontSize = 10.sp,
                                    color = Color(0xFF94A3B8),
                                    maxLines = 1,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = DevTextSecondary)
                    }
                }

                // Sub-nav for preview tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF111827))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChip(
                            selected = activeTab == "preview",
                            onClick = { activeTab = "preview" },
                            label = { Text("App Sandbox Preview", fontSize = 11.sp) }
                        )
                        FilterChip(
                            selected = activeTab == "console",
                            onClick = { activeTab = "console" },
                            label = { Text("DevTools Console", fontSize = 11.sp) }
                        )
                    }

                    IconButton(
                        onClick = {
                            consoleLogs = consoleLogs + "[DEV_Israfil] Sandbox reload triggered at ${System.currentTimeMillis() % 10000}ms"
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reload", tint = DevCyan, modifier = Modifier.size(16.dp))
                    }
                }

                // Content
                if (activeTab == "preview") {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(Color(0xFF030712))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "🌐 Interactive Live Sandbox",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DevCyan
                            )
                            Text(
                                text = project.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )

                            // Interactive Demo widget
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                color = Color(0xFF020617),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, DevBorderGlass)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Code,
                                        contentDescription = null,
                                        tint = DevCyan,
                                        modifier = Modifier.size(36.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "${project.category} Project Environment",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Powered by DEV_Israfil Live Engine",
                                        fontSize = 11.sp,
                                        color = DevTextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = {
                                            consoleLogs = consoleLogs + "[Action Triggered] User clicked sandbox primary interaction!"
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = DevCyan)
                                    ) {
                                        Text("Execute Action", color = Color(0xFF030712), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Text(
                                text = "Live preview rendering: ${htmlFile?.name ?: "source"}",
                                fontSize = 11.sp,
                                color = DevTextMuted
                            )
                        }
                    }
                } else {
                    // Console Tab
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(Color(0xFF020617))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(consoleLogs) { log ->
                            Text(
                                text = "> $log",
                                fontSize = 11.sp,
                                color = if (log.contains("Action")) DevEmerald else DevCyan,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CodeExplorerModal(
    project: ProjectEntity,
    onDismiss: () -> Unit
) {
    val files = remember(project) { ZipManager.parseProjectFiles(project.sourceFilesJson) }
    var selectedFileIndex by remember { mutableStateOf(0) }
    val selectedFile = files.getOrNull(selectedFileIndex) ?: files.firstOrNull()
    val clipboardManager = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.90f)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF080C14))
                .border(1.dp, DevBorderGlass, RoundedCornerShape(24.dp))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0F172A))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderZip,
                            contentDescription = null,
                            tint = DevCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "${project.title} — Source Explorer",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = DevTextSecondary)
                    }
                }

                // File Selector Horizontal Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0B1120))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    files.forEachIndexed { index, file ->
                        val isSelected = index == selectedFileIndex
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    selectedFileIndex = index
                                    copied = false
                                },
                            color = if (isSelected) DevCyan.copy(alpha = 0.2f) else Color(0xFF1E293B),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (isSelected) DevCyan else Color.Transparent)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = if (isSelected) DevCyan else DevTextSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = file.name,
                                    fontSize = 11.sp,
                                    color = if (isSelected) DevCyan else Color.White,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }

                // Code Display with Copy Button
                if (selectedFile != null) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(Color(0xFF030712))
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Sub-toolbar
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF090E1A))
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedFile.path,
                                    fontSize = 11.sp,
                                    color = DevTextSecondary,
                                    fontFamily = FontFamily.Monospace
                                )

                                Button(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(selectedFile.content))
                                        copied = true
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (copied) DevEmerald else DevCyan
                                    ),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Icon(
                                        imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                                        contentDescription = null,
                                        tint = Color(0xFF030712),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (copied) "Copied!" else "Copy Code",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF030712)
                                    )
                                }
                            }

                            // Code text view with line numbers
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .padding(12.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                val lines = selectedFile.content.lines()
                                // Line Numbers
                                Column(
                                    modifier = Modifier.padding(end = 12.dp),
                                    horizontalAlignment = Alignment.End
                                ) {
                                    lines.forEachIndexed { i, _ ->
                                        Text(
                                            text = "${i + 1}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF475569),
                                            fontFamily = FontFamily.Monospace,
                                            lineHeight = 17.sp
                                        )
                                    }
                                }

                                // Code Lines
                                Column {
                                    lines.forEach { line ->
                                        Text(
                                            text = line.ifEmpty { " " },
                                            fontSize = 11.sp,
                                            color = Color(0xFF38BDF8),
                                            fontFamily = FontFamily.Monospace,
                                            lineHeight = 17.sp
                                        )
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

@Composable
fun AdminDeleteConfirmDialog(
    projectTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Delete Project",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete '$projectTitle'? This will permanently remove its metadata, source files, and download statistics.",
                color = DevTextSecondary
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = DevRose)
            ) {
                Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel", color = DevTextSecondary)
            }
        },
        containerColor = DevCardSurface,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun LanguageSelectionModal(
    currentLanguage: com.example.util.AppLanguage,
    onLanguageSelected: (com.example.util.AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    var tempSelected by remember { mutableStateOf(currentLanguage) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .background(DevSurfaceDark)
                .border(1.dp, DevBorderGlass, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Icon
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(DevCyan.copy(alpha = 0.2f), DevPurple.copy(alpha = 0.2f))
                            )
                        )
                        .border(1.dp, DevBorderGlass, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Language",
                        tint = DevCyan,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Title & Subtitle
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "ভাষা নির্বাচন করুন\nChoose Your Language",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 24.sp
                    )
                    Text(
                        text = "আপনার পছন্দের ভাষায় কোড ও প্রজেক্ট ডাউনলোড করুন",
                        fontSize = 12.sp,
                        color = DevTextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }

                // Language Option Cards
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Option 1: বাংলা
                    val isBnSelected = tempSelected == com.example.util.AppLanguage.BANGLA
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { tempSelected = com.example.util.AppLanguage.BANGLA }
                            .border(
                                width = if (isBnSelected) 2.dp else 1.dp,
                                color = if (isBnSelected) DevCyan else DevBorderGlass,
                                shape = RoundedCornerShape(18.dp)
                            ),
                        color = if (isBnSelected) DevCyan.copy(alpha = 0.12f) else DevCardGlass
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "🇧🇩",
                                fontSize = 28.sp
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "বাংলা (Bengali)",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isBnSelected) DevCyan else Color.White
                                    )
                                    if (isBnSelected) {
                                        Surface(
                                            color = DevCyan,
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "ডিফল্ট",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "বাংলায় প্রজেক্ট ও সোর্স কোড ডাউনলোড করুন",
                                    fontSize = 11.sp,
                                    color = DevTextSecondary
                                )
                            }

                            RadioButton(
                                selected = isBnSelected,
                                onClick = { tempSelected = com.example.util.AppLanguage.BANGLA },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = DevCyan,
                                    unselectedColor = DevTextMuted
                                )
                            )
                        }
                    }

                    // Option 2: English
                    val isEnSelected = tempSelected == com.example.util.AppLanguage.ENGLISH
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { tempSelected = com.example.util.AppLanguage.ENGLISH }
                            .border(
                                width = if (isEnSelected) 2.dp else 1.dp,
                                color = if (isEnSelected) DevPurple else DevBorderGlass,
                                shape = RoundedCornerShape(18.dp)
                            ),
                        color = if (isEnSelected) DevPurple.copy(alpha = 0.12f) else DevCardGlass
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "🌐",
                                fontSize = 28.sp
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "English",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isEnSelected) DevPurple else Color.White
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Explore & download source code in English",
                                    fontSize = 11.sp,
                                    color = DevTextSecondary
                                )
                            }

                            RadioButton(
                                selected = isEnSelected,
                                onClick = { tempSelected = com.example.util.AppLanguage.ENGLISH },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = DevPurple,
                                    unselectedColor = DevTextMuted
                                )
                            )
                        }
                    }
                }

                // Action Button
                Button(
                    onClick = {
                        onLanguageSelected(tempSelected)
                        onDismiss()
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = if (tempSelected == com.example.util.AppLanguage.BANGLA) "এগিয়ে যান (Continue)" else "Continue (এগিয়ে যান)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

