package app.joelauncher.data.profiles

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "allowed_apps",
    foreignKeys = [
        ForeignKey(
            entity = Profile::class,
            parentColumns = ["profileId"],
            childColumns = ["profileOwnerId"],
            onDelete = ForeignKey.CASCADE // If a profile is deleted, its allowed apps are also deleted
        )
    ],
    indices = [Index(value = ["profileOwnerId"])] // Index for faster queries on profileOwnerId
)
data class AllowedApp(
    @PrimaryKey(autoGenerate = true)
    val appId: Long = 0, // Auto-generated primary key for each allowed app entry
    val profileOwnerId: Long, // Foreign key linking to Profile's profileId
    val packageName: String // The package name of the allowed app, e.g., "com.example.app"
)