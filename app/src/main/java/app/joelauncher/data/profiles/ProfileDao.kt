package app.joelauncher.data.profiles

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    // --- Profile Operations ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: Profile): Long // Returns the new profileId

    @Update
    suspend fun updateProfile(profile: Profile)

    @Delete
    suspend fun deleteProfile(profile: Profile)

    @Query("SELECT * FROM profiles WHERE profileId = :profileId")
    suspend fun getProfileById(profileId: Long): Profile?

    @Query("SELECT * FROM profiles ORDER BY displayName ASC")
    fun getAllProfiles(): Flow<List<Profile>> // Observe changes with Flow

    // --- AllowedApp Operations ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllowedApp(allowedApp: AllowedApp)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllowedApps(allowedApps: List<AllowedApp>)

    @Delete
    suspend fun deleteAllowedApp(allowedApp: AllowedApp)

    @Query("DELETE FROM allowed_apps WHERE profileOwnerId = :profileId AND packageName = :packageName")
    suspend fun deleteSpecificAllowedApp(profileId: Long, packageName: String)

    @Query("DELETE FROM allowed_apps WHERE profileOwnerId = :profileId")
    suspend fun clearAllowedAppsForProfile(profileId: Long)

    @Query("SELECT * FROM allowed_apps WHERE profileOwnerId = :profileId")
    suspend fun getAllowedAppsForProfile(profileId: Long): List<AllowedApp>

    // --- Profile Override Operations ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileOverride(profileOverride: ProfileOverrideEvent)

    @Query("SELECT * FROM profile_override_events WHERE expirationTime > :currentTime")
    suspend fun loadProfileOverridesThatApply(currentTime: Long): List<ProfileOverrideEvent>

    // --- Relationship Query (Profile with its Allowed Apps) ---

    @Transaction // Ensures the operation is performed atomically
    @Query("SELECT * FROM profiles WHERE profileId = :profileId")
    suspend fun getProfileWithAllowedApps(profileId: Long): ProfileWithAllowedApps?

    @Transaction
    @Query("SELECT * FROM profiles")
    fun getAllProfilesWithAllowedApps(): Flow<List<ProfileWithAllowedApps>>


}