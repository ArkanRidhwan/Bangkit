package submission3final.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import submission3final.database.FavRepository
import submission3final.database.FavoriteDb

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavRepository: FavRepository = FavRepository(application)

    fun getAllFavs(): LiveData<List<FavoriteDb>> = mFavRepository.getAllFav()
}