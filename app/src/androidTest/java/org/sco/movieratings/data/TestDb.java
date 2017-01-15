package org.sco.movieratings.data;

import java.util.HashSet;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase() { mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME); }

    public void setUp() { deleteTheDatabase(); }

    public void testCreateDb() throws Throwable {

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDBHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: DB not created",
                c.moveToFirst());

        // Verify tables were created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext() );

        assertTrue("Error: Tables were not created",
                tableNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")", null);

        assertTrue("Error: Could not query database for table info.",
                c.moveToFirst());

        final HashSet<String> movieColumnHashSet = new HashSet<String>();
        movieColumnHashSet.add(MovieContract.MovieEntry._ID);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_IS_FAVORITE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_OVERVIEW);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_RATING);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        assertTrue("Error: The database doesn't contain all required columns",
                movieColumnHashSet.isEmpty());
        db.close();
    }

    public void testMovieTable() {
        long movieRowId = insertMovie();
    }

    public long insertMovie() {
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createMovieValues();

        long movieRowId;
        movieRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        assertTrue(movieRowId != -1);

        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null  // sort order
        );

        assertTrue("Error: Nothing returned from movie query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Movie query validation failed.", cursor, testValues);

        assertFalse("Error: More than one record returned", cursor.moveToNext());

        cursor.close();
        db.close();
        return movieRowId;
    }

}
