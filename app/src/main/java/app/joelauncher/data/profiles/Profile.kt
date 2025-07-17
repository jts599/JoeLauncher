package app.joelauncher.data.profiles


import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    val profileId: Long = 0, // Auto-generated primary key
    var displayName: String, //bitwise representation of days
    var repeatDays: Long?, //second of the day
    var startTime: Long?, //second of the day
    var endTime: Long?,
    var profileState: ProfileState
)

enum class ProfileState {
    On,
    Off,
    Scheduled
}

class ProfileHelpers {
    public fun getRepeatDaysFromListOfDays(days: List<DayOfWeek>): Long {
        var result: Long = 0;
        for (day in days) {
            result = result or (1L shl day.ordinal);
        }
        return result;
    }
}


