package com.catalyst.android.birdapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.catalyst.android.birdapp.BirdSighting;

public class DatabaseHandler extends SQLiteOpenHelper {

	private Cursor cursor;
	
	private static DatabaseHandler INSTANCE;
	private static final String DATABASE_NAME = "BADB";
	private static final int DATABASE_VERSION = 1;
	private static String newActivityName = "";
	private static final String BIRD_SIGHTING = "birdSighting";
	private static final String BIRD_SIGHTING_ID = "birdSightingId";
	private static final String BIRD_COMMON_NAME = "birdCommonName";
	private static final String BIRD_SCIENTIFIC_NAME = "birdScientificName";
	private static final String SIGHTING_NOTES = "sightingNotes";
	private static final String LATITUDE = "latitude";
	private static final String LONGITUDE = "longitude";
	private static final String DATE_TIME = "dateTime";

	private static final String SIGHTING_PICTURE_MAP = "sightingPictureMap";
	private static final String SIGHTING_PICTURE_ID = "sightingPictureId";
	private static final String IS_DEFAULT_PICTURE = "isDefaultPicture";

	private static final String SIGHTING_AUDIO_MAP = "sightingAudioMap";
	private static final String SIGHTING_AUDIO_ID = "sightingAudioId";

	private static final String BIRD_SIGHTINGS_CATEGORY = "birdSightingsCategory";
	private static final String SIGHTING_CATEGORY_ID = "sightingCategoryId";
	private static final String SIGHTING_CATEGORY = "sightingCategory";

	private static final String BIRD_PICTURE = "birdPicture";
	private static final String PICTURE_ID = "pictureId";
	private static final String PICTURE_PATH = "picturePath";

	private static final String BIRD_AUDIO = "birdAudio";
	private static final String AUDIO_ID = "audioId";
	private static final String AUDIO_PATH = "audioPath";

	private static final String BIRD_ACTIVITIES = "birdActivities";
	private static final String BIRD_ACTIVITY_ID = "birdActivityId";
	private static final String BIRD_ACTIVITY = "birdActivity";

	private static final String BIRD_SIGHTING_CREATE = "create table birdSighting (birdSightingId integer primary key autoincrement, birdCommonName text,"
			+ " birdScientificName text, sightingNotes text, latitude real, longitude real, dateTime integer, birdActivityId integer, sightingCategoryId integer,"
			+ " foreign key (birdActivityId) references birdActivities (birdActivityId), foreign key (sightingCategoryId) references birdSightingsCategory (sightingCategoryId))";

	private static final String SIGHTING_PICTURE_MAP_CREATE = "create table sightingPictureMap (sightingPictureId integer primary key autoincrement, birdSightingId integer, pictureId integer,"
			+ " isDefaultPicture integer, foreign key (birdSightingId) references birdSighting (birdSightingId), foreign key (pictureId) references birdPicture (pictureId))";

	private static final String SIGHTING_AUDIO_MAP_CREATE = "create table sightingAudioMap (sightingAudioId integer primary key autoincrement,"
			+ " birdSightingId integer, audioId integer, foreign key (birdSightingId) references birdSighting (birdSightingId),"
			+ " foreign key (audioId) references birdAudio (audioId))";

	private static final String BIRD_SIGHTINGS_CATEGORY_CREATE = "create table birdSightingsCategory (sightingCategoryId integer primary key autoincrement,"
			+ " sightingCategory text)";

	private static final String BIRD_PICTURE_CREATE = "create table birdPicture (pictureId integer primary key autoincrement, picturePath text)";

	private static final String BIRD_AUDIO_CREATE = "create table birdAudio (audioId integer primary key autoincrement, audioPath text)";

	private static final String BIRD_ACTIVITIES_CREATE = "create table birdActivities (birdActivityId integer primary key autoincrement, birdActivity text)";

	// private Cursor cursor;

	private DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static DatabaseHandler getInstance(Context context) {
		if (INSTANCE == null) {
			INSTANCE = new DatabaseHandler(context);
		}
		return INSTANCE;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(BIRD_SIGHTING_CREATE);
			db.execSQL(SIGHTING_PICTURE_MAP_CREATE);
			db.execSQL(SIGHTING_AUDIO_MAP_CREATE);
			db.execSQL(BIRD_SIGHTINGS_CATEGORY_CREATE);
			db.execSQL(BIRD_PICTURE_CREATE);
			db.execSQL(BIRD_AUDIO_CREATE);
			db.execSQL(BIRD_ACTIVITIES_CREATE);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// log the version upgrade
		Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		// Drop older tables if existing
		db.execSQL("DROP TABLE IF EXISTS " + BIRD_SIGHTING);
		db.execSQL("DROP TABLE IF EXISTS " + SIGHTING_PICTURE_MAP);
		db.execSQL("DROP TABLE IF EXISTS " + SIGHTING_AUDIO_MAP);
		db.execSQL("DROP TABLE IF EXISTS " + BIRD_SIGHTINGS_CATEGORY);
		db.execSQL("DROP TABLE IF EXISTS " + BIRD_PICTURE);
		db.execSQL("DROP TABLE IF EXISTS " + BIRD_AUDIO);
		db.execSQL("DROP TABLE IF EXISTS " + BIRD_ACTIVITIES);

		// Create tables again
		onCreate(db);
	}
	
	public int getAcivityByActivityName(String activity) {
		int birdId;
		SQLiteDatabase db = this.getReadableDatabase();
		cursor = db.query(BIRD_ACTIVITIES, new String[] { BIRD_ACTIVITY_ID }, BIRD_ACTIVITY
				+ " = ?", new String[] { activity }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			birdId = cursor.getInt(0);
		} else {
			birdId = 0;
		}
		db.close();
		return birdId;
	}
	
	public int getCategoryByCategoryName(String category) {
		int birdId;
		SQLiteDatabase db = this.getReadableDatabase();
		cursor = db.query(BIRD_SIGHTINGS_CATEGORY, new String[] { SIGHTING_CATEGORY_ID }, SIGHTING_CATEGORY
				+ " = ?", new String[] { category }, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			birdId = cursor.getInt(0);
		} else {
			birdId = 0;
		}
		db.close();
		return birdId;
	}
	
	public long insertBirdSighting(BirdSighting birdSighting) {
		
		// Perform Readable tasks before trying to write to DB (get foreign Key Ids)
		int activityId = getAcivityByActivityName(birdSighting.getActivity());
		int categoryId = getCategoryByCategoryName(birdSighting.getCategory());
		
		SQLiteDatabase database = getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(DatabaseHandler.BIRD_COMMON_NAME, birdSighting.getCommonName());
		contentValues.put(DatabaseHandler.BIRD_SCIENTIFIC_NAME, birdSighting.getScientificName());
		contentValues.put(DatabaseHandler.SIGHTING_NOTES, birdSighting.getNotes());
		contentValues.put(DatabaseHandler.LATITUDE, birdSighting.getLatitude());
		contentValues.put(DatabaseHandler.LONGITUDE, birdSighting.getLongitude());
		contentValues.put(DatabaseHandler.SIGHTING_CATEGORY_ID, categoryId);
		contentValues.put(DatabaseHandler.BIRD_ACTIVITY_ID, activityId);
		if (birdSighting.getDateTime() != null) {
			contentValues.put(DatabaseHandler.DATE_TIME, birdSighting.getDateTime().toString());
		}
		
		long affectedColumnId = database.insert(DatabaseHandler.BIRD_SIGHTING, null, contentValues);
		
		database.close();
		
		return affectedColumnId;
	}
	
	public void saveActivity(String activityName){
		SQLiteDatabase database = this.getWritableDatabase();
		newActivityName = activityName;
		String query = "INSERT INTO birdactivities (birdActivity) VALUES ('"+newActivityName+"')";
		database.execSQL(query);
	}

}
