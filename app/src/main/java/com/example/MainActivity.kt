package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.*
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.DevIsrafilViewModel
import com.example.ui.viewmodel.Screen
import com.example.util.ZipManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: DevIsrafilViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DEV_IsrafilTheme {
                MainApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: DevIsrafilViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val appLanguage by viewModel.appLanguage.collectAsState()
    val showLanguageDialog by viewModel.showLanguageDialog.collectAsState()
    val isAdminLoggedIn by viewModel.isAdminLoggedIn.collectAsState()
    val downloadingProject by viewModel.downloadingProject.collectAsState()
    val downloadProgress by viewModel.downloadProgress.collectAsState()
    val downloadedZipFile by viewModel.downloadedZipFile.collectAsState()
    val livePreviewProject by viewModel.livePreviewProject.collectAsState()
    val codeExplorerProject by viewModel.codeExplorerProject.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Toast listener
    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = DevSurfaceDark,
                drawerContentColor = Color.White,
                drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
                modifier = Modifier
                    .width(310.dp)
                    .border(BorderStroke(1.dp, DevBorderGlass), RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    DevLogo(
                        onClick = {
                            viewModel.navigateTo(Screen.HOME)
                            coroutineScope.launch { drawerState.close() }
                        }
                    )

                    HorizontalDivider(color = DevBorderGlass)

                    // Language Selector in Drawer
                    Text(
                        text = com.example.util.Strings.languageNavHeader(appLanguage),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DevTextMuted,
                        letterSpacing = 1.sp
                    )

                    LanguageSegmentedControl(
                        selectedLanguage = appLanguage,
                        onLanguageSelected = { viewModel.setLanguage(it) }
                    )

                    HorizontalDivider(color = DevBorderGlass)

                    Text(
                        text = com.example.util.Strings.mainNavHeader(appLanguage),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DevTextMuted,
                        letterSpacing = 1.sp
                    )

                    DrawerItem(
                        title = com.example.util.Strings.navHome(appLanguage),
                        icon = Icons.Default.Home,
                        isSelected = currentScreen == Screen.HOME,
                        onClick = {
                            viewModel.navigateTo(Screen.HOME)
                            coroutineScope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        title = com.example.util.Strings.navProjects(appLanguage),
                        icon = Icons.Default.Code,
                        isSelected = currentScreen == Screen.PROJECTS,
                        onClick = {
                            viewModel.navigateTo(Screen.PROJECTS)
                            coroutineScope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        title = com.example.util.Strings.navCategories(appLanguage),
                        icon = Icons.Default.Category,
                        isSelected = currentScreen == Screen.CATEGORIES,
                        onClick = {
                            viewModel.navigateTo(Screen.CATEGORIES)
                            coroutineScope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        title = com.example.util.Strings.navAbout(appLanguage),
                        icon = Icons.Default.Info,
                        isSelected = currentScreen == Screen.ABOUT,
                        onClick = {
                            viewModel.navigateTo(Screen.ABOUT)
                            coroutineScope.launch { drawerState.close() }
                        }
                    )

                    DrawerItem(
                        title = com.example.util.Strings.navContact(appLanguage),
                        icon = Icons.Default.ContactMail,
                        isSelected = currentScreen == Screen.CONTACT,
                        onClick = {
                            viewModel.navigateTo(Screen.CONTACT)
                            coroutineScope.launch { drawerState.close() }
                        }
                    )

                    HorizontalDivider(color = DevBorderGlass)

                    Text(
                        text = com.example.util.Strings.adminNavHeader(appLanguage),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DevTextMuted,
                        letterSpacing = 1.sp
                    )

                    if (isAdminLoggedIn) {
                        DrawerItem(
                            title = com.example.util.Strings.adminDashboard(appLanguage),
                            icon = Icons.Default.Dashboard,
                            isSelected = currentScreen == Screen.ADMIN_DASHBOARD,
                            onClick = {
                                viewModel.navigateTo(Screen.ADMIN_DASHBOARD)
                                coroutineScope.launch { drawerState.close() }
                            }
                        )

                        DrawerItem(
                            title = com.example.util.Strings.uploadNewProject(appLanguage),
                            icon = Icons.Default.CloudUpload,
                            isSelected = currentScreen == Screen.ADMIN_UPLOAD,
                            onClick = {
                                viewModel.prepareNewProjectForm()
                                coroutineScope.launch { drawerState.close() }
                            }
                        )

                        DrawerItem(
                            title = com.example.util.Strings.adminLogout(appLanguage),
                            icon = Icons.Default.Logout,
                            isSelected = false,
                            onClick = {
                                viewModel.adminLogout()
                                coroutineScope.launch { drawerState.close() }
                            }
                        )
                    } else {
                        DrawerItem(
                            title = com.example.util.Strings.adminPortal(appLanguage),
                            icon = Icons.Default.AdminPanelSettings,
                            isSelected = currentScreen == Screen.ADMIN_LOGIN,
                            onClick = {
                                viewModel.navigateTo(Screen.ADMIN_LOGIN)
                                coroutineScope.launch { drawerState.close() }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "DEV_Israfil • SaaS Code Hub\n${com.example.util.Strings.tagline(appLanguage)}",
                        fontSize = 11.sp,
                        color = DevTextMuted
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            DevLogo(
                                onClick = { viewModel.navigateTo(Screen.HOME) }
                            )
                        },
                        navigationIcon = {
                            Box(
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(DevCardGlass)
                                    .border(1.dp, DevBorderGlass, CircleShape)
                                    .clickable { coroutineScope.launch { drawerState.open() } },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Navigation Menu",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        actions = {
                            // Quick Language Switcher Chip in TopAppBar
                            LanguageSwitcherChip(
                                currentLanguage = appLanguage,
                                onClick = { viewModel.openLanguageDialog() }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            // Search Action Button
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(DevCardGlass)
                                    .border(1.dp, DevBorderGlass, CircleShape)
                                    .clickable {
                                        viewModel.setSearchQuery("")
                                        viewModel.setSelectedCategory(null)
                                        viewModel.navigateTo(Screen.PROJECTS)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Projects",
                                    tint = DevTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Admin Portal Quick Button
                            Box(
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(DevCardGlass)
                                    .border(1.dp, if (isAdminLoggedIn) DevEmerald.copy(alpha = 0.5f) else DevBorderGlass, CircleShape)
                                    .clickable {
                                        if (isAdminLoggedIn) {
                                            viewModel.navigateTo(Screen.ADMIN_DASHBOARD)
                                        } else {
                                            viewModel.navigateTo(Screen.ADMIN_LOGIN)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Box(contentAlignment = Alignment.TopEnd) {
                                    Icon(
                                        imageVector = if (isAdminLoggedIn) Icons.Default.AdminPanelSettings else Icons.Outlined.AdminPanelSettings,
                                        contentDescription = "Admin Portal",
                                        tint = if (isAdminLoggedIn) DevEmerald else DevTextSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    if (isAdminLoggedIn) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(DevEmerald)
                                        )
                                    }
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = DevSurfaceDark,
                            titleContentColor = Color.White
                        )
                    )
                    HorizontalDivider(color = DevBorderGlass, thickness = 1.dp)
                }
            },
            bottomBar = {
                // Bottom navigation with Premium SaaS border & container
                Column {
                    HorizontalDivider(color = DevBorderGlass, thickness = 1.dp)
                    NavigationBar(
                        containerColor = DevSurfaceDark,
                        tonalElevation = 0.dp,
                        modifier = Modifier.height(72.dp)
                    ) {
                        NavigationBarItem(
                            selected = currentScreen == Screen.HOME,
                            onClick = { viewModel.navigateTo(Screen.HOME) },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                            label = { Text(com.example.util.Strings.navHome(appLanguage), fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DevCyan,
                                selectedTextColor = DevCyan,
                                indicatorColor = DevCyan.copy(alpha = 0.15f),
                                unselectedIconColor = DevTextMuted,
                                unselectedTextColor = DevTextMuted
                            )
                        )

                        NavigationBarItem(
                            selected = currentScreen == Screen.PROJECTS || currentScreen == Screen.PROJECT_DETAILS,
                            onClick = { viewModel.navigateTo(Screen.PROJECTS) },
                            icon = { Icon(Icons.Default.Code, contentDescription = "Browse") },
                            label = { Text(com.example.util.Strings.navBrowse(appLanguage), fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DevCyan,
                                selectedTextColor = DevCyan,
                                indicatorColor = DevCyan.copy(alpha = 0.15f),
                                unselectedIconColor = DevTextMuted,
                                unselectedTextColor = DevTextMuted
                            )
                        )

                        NavigationBarItem(
                            selected = currentScreen == Screen.CATEGORIES,
                            onClick = { viewModel.navigateTo(Screen.CATEGORIES) },
                            icon = { Icon(Icons.Default.Category, contentDescription = "Library") },
                            label = { Text(com.example.util.Strings.navCategories(appLanguage), fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DevCyan,
                                selectedTextColor = DevCyan,
                                indicatorColor = DevCyan.copy(alpha = 0.15f),
                                unselectedIconColor = DevTextMuted,
                                unselectedTextColor = DevTextMuted
                            )
                        )

                        NavigationBarItem(
                            selected = currentScreen == Screen.ABOUT,
                            onClick = { viewModel.navigateTo(Screen.ABOUT) },
                            icon = { Icon(Icons.Default.Info, contentDescription = "About") },
                            label = { Text(com.example.util.Strings.navAbout(appLanguage), fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DevCyan,
                                selectedTextColor = DevCyan,
                                indicatorColor = DevCyan.copy(alpha = 0.15f),
                                unselectedIconColor = DevTextMuted,
                                unselectedTextColor = DevTextMuted
                            )
                        )

                        NavigationBarItem(
                            selected = currentScreen in listOf(
                                Screen.ADMIN_LOGIN,
                                Screen.ADMIN_DASHBOARD,
                                Screen.ADMIN_UPLOAD,
                                Screen.ADMIN_ALL_PROJECTS,
                                Screen.ADMIN_CATEGORIES,
                                Screen.ADMIN_STATS,
                                Screen.ADMIN_SETTINGS
                            ),
                            onClick = {
                                if (isAdminLoggedIn) {
                                    viewModel.navigateTo(Screen.ADMIN_DASHBOARD)
                                } else {
                                    viewModel.navigateTo(Screen.ADMIN_LOGIN)
                                }
                            },
                            icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                            label = { Text(com.example.util.Strings.navAdmin(appLanguage), fontSize = 10.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = DevCyan,
                                selectedTextColor = DevCyan,
                                indicatorColor = DevCyan.copy(alpha = 0.15f),
                                unselectedIconColor = DevTextMuted,
                                unselectedTextColor = DevTextMuted
                            )
                        )
                    }
                }
            },
            containerColor = DevBgDark
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(DevBgDark)
            ) {
                // SaaS Ambient Glow Spheres in background
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .offset(x = 180.dp, y = (-50).dp)
                        .clip(CircleShape)
                        .background(DevAmbientCyanGlow)
                )
                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .align(Alignment.BottomStart)
                        .offset(x = (-80).dp, y = 80.dp)
                        .clip(CircleShape)
                        .background(DevAmbientPurpleGlow)
                )

                when (currentScreen) {
                    Screen.HOME -> HomeScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                    Screen.PROJECTS -> ProjectsScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                    Screen.PROJECT_DETAILS -> ProjectDetailScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                    Screen.CATEGORIES -> CategoriesScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                    Screen.ABOUT -> AboutScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                    Screen.CONTACT -> ContactScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                    Screen.ADMIN_LOGIN -> AdminLoginScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                    Screen.ADMIN_DASHBOARD -> AdminDashboardScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                    Screen.ADMIN_UPLOAD -> AdminUploadScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                    Screen.ADMIN_ALL_PROJECTS -> AdminAllProjectsScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                    Screen.ADMIN_CATEGORIES -> AdminCategoriesScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                    Screen.ADMIN_STATS -> AdminStatisticsScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                    Screen.ADMIN_SETTINGS -> AdminSettingsScreen(viewModel = viewModel, onNavigate = { viewModel.navigateTo(it) })
                }
            }
        }
    }

    // Modal Overlays
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = appLanguage,
            onLanguageSelected = { selectedLang ->
                viewModel.setLanguage(selectedLang)
            },
            onDismiss = {
                viewModel.dismissLanguageDialog()
            }
        )
    }

    if (downloadingProject != null) {
        DownloadProgressModal(
            project = downloadingProject!!,
            progress = downloadProgress,
            downloadedFile = downloadedZipFile,
            onDismiss = { viewModel.closeDownloadModal() },
            onShareFile = { file ->
                ZipManager.shareOrOpenZip(context, file)
            }
        )
    }

    if (livePreviewProject != null) {
        LivePreviewModal(
            project = livePreviewProject!!,
            onDismiss = { viewModel.closeLivePreview() }
        )
    }

    if (codeExplorerProject != null) {
        CodeExplorerModal(
            project = codeExplorerProject!!,
            onDismiss = { viewModel.closeCodeExplorer() }
        )
    }
}

@Composable
fun DrawerItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        color = if (isSelected) DevCyan.copy(alpha = 0.15f) else Color.Transparent,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, if (isSelected) DevCyan.copy(alpha = 0.5f) else Color.Transparent)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) DevCyan else DevTextSecondary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) DevCyan else Color.White
            )
        }
    }
}
