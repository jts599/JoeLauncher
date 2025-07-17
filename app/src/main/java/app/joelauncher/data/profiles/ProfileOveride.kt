package app.joelauncher.data.profiles

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Time

@Entity(
    tableName = "profile_override_events",
    foreignKeys = [
        ForeignKey(
            entity = Profile::class,
            parentColumns = ["profileId"],
            childColumns = ["profileOwnerId"],
            onDelete = ForeignKey.CASCADE // If a profile is deleted, its allowed apps are also deleted
        )
    ],
    indices = [Index(value = ["profileOwnerId"]),Index(value = ["expirationTime"]) ] // Index for faster queries on profileOwnerId, and expiration time
)
data class ProfileOverrideEvent(
    @PrimaryKey(autoGenerate = true)
    val eventNumber: Long = 0, // Auto-generated primary key for each allowed app entry
    val profileOwnerId: Long, // Foreign key linking to Profile's profileId
    val createdAT: Time, // When was this override created?
    val expirationTime: Time, // When is this override valid until?
    val comment: String // What is this override for?
)