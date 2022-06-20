package submission3final.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(fav: FavoriteDb)

    @Update
    fun update(fav: FavoriteDb)

    @Delete
    fun delete(fav: FavoriteDb)

    @Query("SELECT * from favoritedb ORDER BY id ASC")
    fun getAllFav(): LiveData<List<FavoriteDb>>
}