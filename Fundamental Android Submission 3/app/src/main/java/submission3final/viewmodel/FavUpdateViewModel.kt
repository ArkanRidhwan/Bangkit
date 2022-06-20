package submission3final.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import submission3final.database.FavRepository
import submission3final.database.FavoriteDb

class FavUpdateViewModel(application: Application) : ViewModel() {


    private val mFavRepository: FavRepository = FavRepository(application)
    fun insert(fav: FavoriteDb) {
        mFavRepository.insert(fav)
    }

    fun delete(fav: FavoriteDb) {
        mFavRepository.delete(fav)
    }
}