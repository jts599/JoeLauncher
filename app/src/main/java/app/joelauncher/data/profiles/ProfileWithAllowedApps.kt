package app.joelauncher.data.profiles

import androidx.room.Embedded
import androidx.room.Relation

data class ProfileWithAllowedApps(
    @Embedded val profile: Profile,
    @Relation(
        parentColumn = "profileId", // From Profile entity
        entityColumn = "profileOwnerId" // From AllowedApp entity
    )
    val allowedApps: List<AllowedApp>
)