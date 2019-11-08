
# Room

[Room](https://developer.android.com/jetpack/androidx/releases/room)  

### Add dependency
```
 def room_version = "2.2.1"

 implementation "androidx.room:room-runtime:$room_version"
 annotationProcessor "androidx.room:room-compiler:$room_version" // For Kotlin use kapt instead of annotationProcessor
```

### Entity
```
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

@Entity
// @Entity(tableName = "table_name")  // Table 명을 따로 지정해 줄때
public class DataModel {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @Override
    public String toString() {
        String s = "";

	... toString 재정의

        return s;
    }
}

```

### Dao
```
package com.metarobotics.playvandi.ROOM.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.metarobotics.playvandi.Models.FlightDataModel;

import java.util.List;

@Dao
public interface IDataDao {

    // INSERT
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFlightData(DataModel fdm);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertFlightDatas(List<DataModel> dataList);


    // UPDATE
    @Update
    void updateData(DataModel fdm);


    // DELETE
    @Delete
    void deleteOne(DataModel fdm);

    @Query("DELETE FROM DataModel WHERE id = :id")
    void deleteOneById(int id);

    @Query("DELETE FROM DataModel WHERE uid = :uid")
    void deleteOneByUid(int uid);

    @Query("DELETE FROM DataModel")
    void deleteAll();


    // GET
    @Query("SELECT * FROM DataModel ORDER BY id DESC")
    List<DataModel> getAll();

    @Query("SELECT * FROM DataModel WHERE id = :id")
    DataModel getOneById(int id);

    @Query("SELECT * FROM DataModel WHERE uid = :uid ORDER BY id DESC LIMIT 1")
    DataModel getOneByUid(int uid);

    @Query("SELECT * FROM DataModel ORDER BY id DESC LIMIT 1")
    int getLastInsertedId();
}

```

### AppDatabase
```
package com.metarobotics.playvandi.ROOM;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {DataModel.class, DataModel2.class}, version = 1)
//@Database(entities = arrayOf(DataModel.class), version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract IDataDao dataDao();
    ...
}

```

### Database client
```
package com.metarobotics.playvandi.ROOM;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseClient {
    private Context mCtx;
    private static DatabaseClient mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "appdata")
		// .allowMainThreadQueries()  // Main thread 에서도 db 조작 가능
		.build();
    }

    public static DatabaseClient getInstance(Context mCtx) {
    // public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
```

### Use
```
DatabaseClient.getInstance(context).getAppDatabase().dataDao()...
```