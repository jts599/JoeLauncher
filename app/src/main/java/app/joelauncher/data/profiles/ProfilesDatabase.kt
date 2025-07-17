package app.joelauncher.data.profiles


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Profile::class, AllowedApp::class], version = 1, exportSchema = false)
abstract class ProfilesDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var INSTANCE: ProfilesDatabase? = null

        fun getDatabase(context: Context): ProfilesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProfilesDatabase::class.java,
                    "profile_database" // Name of your database file
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // .fallbackToDestructiveMigration() // Use with caution during development
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}