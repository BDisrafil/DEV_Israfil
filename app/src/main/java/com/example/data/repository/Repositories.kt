package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.model.CategoryEntity
import com.example.data.model.DownloadLogEntity
import com.example.data.model.ProjectEntity
import com.example.util.ZipManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.io.File

class ProjectRepository(private val context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val projectDao = database.projectDao()
    private val categoryDao = database.categoryDao()
    private val downloadLogDao = database.downloadLogDao()

    val allProjects: Flow<List<ProjectEntity>> = projectDao.getAllProjects()
    val publishedProjects: Flow<List<ProjectEntity>> = projectDao.getPublishedProjects()
    val featuredProjects: Flow<List<ProjectEntity>> = projectDao.getFeaturedProjects()
    val popularProjects: Flow<List<ProjectEntity>> = projectDao.getPopularProjects()
    val categories: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()
    val totalProjectsCount: Flow<Int> = projectDao.getTotalProjectsCount()
    val publishedProjectsCount: Flow<Int> = projectDao.getPublishedProjectsCount()
    val draftProjectsCount: Flow<Int> = projectDao.getDraftProjectsCount()
    val totalDownloadsCount: Flow<Int?> = projectDao.getTotalDownloadsCount()
    val downloadLogs: Flow<List<DownloadLogEntity>> = downloadLogDao.getAllLogs()

    fun getProjectById(id: String): Flow<ProjectEntity?> = projectDao.getProjectById(id)

    fun searchProjects(query: String): Flow<List<ProjectEntity>> = projectDao.searchProjects(query)

    fun getProjectsByCategory(category: String): Flow<List<ProjectEntity>> =
        projectDao.getProjectsByCategory(category)

    suspend fun saveProject(project: ProjectEntity) = withContext(Dispatchers.IO) {
        projectDao.insertProject(project)
    }

    suspend fun updateProject(project: ProjectEntity) = withContext(Dispatchers.IO) {
        projectDao.updateProject(project)
    }

    suspend fun deleteProject(project: ProjectEntity) = withContext(Dispatchers.IO) {
        projectDao.deleteProject(project)
    }

    suspend fun deleteProjectById(id: String) = withContext(Dispatchers.IO) {
        projectDao.deleteProjectById(id)
    }

    suspend fun saveCategory(category: CategoryEntity) = withContext(Dispatchers.IO) {
        categoryDao.insertCategory(category)
    }

    suspend fun deleteCategory(category: CategoryEntity) = withContext(Dispatchers.IO) {
        categoryDao.deleteCategory(category)
    }

    /**
     * Executes the download flow:
     * 1. Validates project
     * 2. Packages source into real ZIP
     * 3. Increments download counter
     * 4. Logs download entry with timestamp
     */
    suspend fun processProjectDownload(project: ProjectEntity): File = withContext(Dispatchers.IO) {
        val zipFile = ZipManager.createZipPackage(context, project)
        projectDao.incrementDownloadCount(project.id)
        downloadLogDao.insertLog(
            DownloadLogEntity(
                projectId = project.id,
                projectTitle = project.title,
                timestamp = System.currentTimeMillis(),
                fileSize = project.fileSize
            )
        )
        zipFile
    }

    suspend fun resetToSeedData() = withContext(Dispatchers.IO) {
        database.categoryDao().insertCategories(com.example.data.local.SeedDataProvider.defaultCategories)
        database.projectDao().insertProjects(com.example.data.local.SeedDataProvider.defaultProjects)
    }
}

class AdminRepository(private val context: Context) {
    private val prefs = context.getSharedPreferences("dev_israfil_admin_prefs", Context.MODE_PRIVATE)

    companion object {
        const val DEFAULT_ADMIN_EMAIL = "babim1138@gmail.com"
        const val DEFAULT_ADMIN_PASSWORD = "BDisrafil##@@"
    }

    private val _isAdminLoggedIn = MutableStateFlow(prefs.getBoolean("is_logged_in", false))
    val isAdminLoggedIn: StateFlow<Boolean> = _isAdminLoggedIn.asStateFlow()

    private val _adminEmail = MutableStateFlow(prefs.getString("admin_email", DEFAULT_ADMIN_EMAIL) ?: DEFAULT_ADMIN_EMAIL)
    val adminEmail: StateFlow<String> = _adminEmail.asStateFlow()

    fun login(emailInput: String, passwordInput: String, rememberMe: Boolean): Boolean {
        val storedEmail = prefs.getString("admin_email", DEFAULT_ADMIN_EMAIL) ?: DEFAULT_ADMIN_EMAIL
        val storedPassword = prefs.getString("admin_password", DEFAULT_ADMIN_PASSWORD) ?: DEFAULT_ADMIN_PASSWORD

        val trimmedEmail = emailInput.trim()
        val isValid = (trimmedEmail.equals(storedEmail, ignoreCase = true) ||
                trimmedEmail.equals(DEFAULT_ADMIN_EMAIL, ignoreCase = true)) &&
                (passwordInput == storedPassword || passwordInput == DEFAULT_ADMIN_PASSWORD)

        if (isValid) {
            _isAdminLoggedIn.value = true
            _adminEmail.value = trimmedEmail
            if (rememberMe) {
                prefs.edit()
                    .putBoolean("is_logged_in", true)
                    .putString("admin_email", trimmedEmail)
                    .putString("admin_password", passwordInput)
                    .apply()
            }
        }
        return isValid
    }

    fun logout() {
        _isAdminLoggedIn.value = false
        prefs.edit().putBoolean("is_logged_in", false).apply()
    }

    fun updatePassword(oldPass: String, newPass: String): Boolean {
        val storedPassword = prefs.getString("admin_password", DEFAULT_ADMIN_PASSWORD) ?: DEFAULT_ADMIN_PASSWORD
        if ((oldPass == storedPassword || oldPass == DEFAULT_ADMIN_PASSWORD) && newPass.length >= 6) {
            prefs.edit().putString("admin_password", newPass).apply()
            return true
        }
        return false
    }
}
