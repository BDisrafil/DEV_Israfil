package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CategoryEntity
import com.example.data.model.DownloadLogEntity
import com.example.data.model.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE status = 'PUBLISHED' ORDER BY createdAt DESC")
    fun getPublishedProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE status = 'PUBLISHED' AND isFeatured = 1 ORDER BY createdAt DESC")
    fun getFeaturedProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE status = 'PUBLISHED' ORDER BY downloadCount DESC")
    fun getPopularProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    fun getProjectById(id: String): Flow<ProjectEntity?>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    suspend fun getProjectByIdDirect(id: String): ProjectEntity?

    @Query("SELECT * FROM projects WHERE category = :category AND status = 'PUBLISHED' ORDER BY createdAt DESC")
    fun getProjectsByCategory(category: String): Flow<List<ProjectEntity>>

    @Query("""
        SELECT * FROM projects 
        WHERE status = 'PUBLISHED' AND (
            title LIKE '%' || :query || '%' OR 
            description LIKE '%' || :query || '%' OR 
            technologies LIKE '%' || :query || '%' OR 
            category LIKE '%' || :query || '%'
        )
        ORDER BY createdAt DESC
    """)
    fun searchProjects(query: String): Flow<List<ProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<ProjectEntity>)

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Query("UPDATE projects SET downloadCount = downloadCount + 1 WHERE id = :id")
    suspend fun incrementDownloadCount(id: String)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: String)

    @Query("SELECT COUNT(*) FROM projects")
    fun getTotalProjectsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM projects WHERE status = 'PUBLISHED'")
    fun getPublishedProjectsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM projects WHERE status = 'DRAFT'")
    fun getDraftProjectsCount(): Flow<Int>

    @Query("SELECT SUM(downloadCount) FROM projects")
    fun getTotalDownloadsCount(): Flow<Int?>
}

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryEntity>)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Query("DELETE FROM categories WHERE id = :id")
    suspend fun deleteCategoryById(id: String)

    @Query("SELECT COUNT(*) FROM categories")
    fun getCategoriesCount(): Flow<Int>
}

@Dao
interface DownloadLogDao {
    @Query("SELECT * FROM download_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<DownloadLogEntity>>

    @Query("SELECT * FROM download_logs WHERE timestamp >= :sinceTimestamp ORDER BY timestamp DESC")
    fun getLogsSince(sinceTimestamp: Long): Flow<List<DownloadLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: DownloadLogEntity)

    @Query("SELECT COUNT(*) FROM download_logs WHERE timestamp >= :sinceTimestamp")
    suspend fun getDownloadCountSince(sinceTimestamp: Long): Int
}
