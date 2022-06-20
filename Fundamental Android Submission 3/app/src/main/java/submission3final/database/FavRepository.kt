package submission3final.database

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavRepository(application: Application) {
    private val mFavDao: FavDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavRoomDatabase.getDatabase(application)
        mFavDao = db.favDao()
    }

    fun getAllFav(): LiveData<List<FavoriteDb>> = mFavDao.getAllFav()

    fun insert(fav: FavoriteDb) {
        executorService.execute { mFavDao.insert(fav) }
    }

    fun delete(fav: FavoriteDb) {
        executorService.execute { mFavDao.delete(fav) }
    }

    fun update(fav: FavoriteDb) {
        executorService.execute { mFavDao.update(fav) }
    }
}