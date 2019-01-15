package com.ahmedadeltito.photoeditor;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class GalleryUtils {

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param context The Application context
	 * @param uri The Uri is checked by functions
	 * @return Whether the Uri authority is FileProvider
	 */
	public static boolean isFileProviderUri(final Context context, final Uri uri) {
		final String packageName = context.getPackageName();
		final String authority = new StringBuilder(packageName).append(".provider").toString();
		return authority.equals(uri.getAuthority());
	}

	/**
	 * @param context The Application context
	 * @param uri The Uri is checked by functions
	 * @return File path or null if file is missing
	 */
	public static String getFileProviderPath(final Context context, final Uri uri)
	{
		final File appDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		final File file = new File(appDir, uri.getLastPathSegment());
		return file.exists() ? file.toString() : Environment.DIRECTORY_PICTURES + uri.getLastPathSegment();
	}
}
