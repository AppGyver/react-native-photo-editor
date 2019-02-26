package com.ahmedadeltito.photoeditor;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.provider.MediaStore;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.content.ContentUris;

public class UtilFunctions {

    // private static final String pattern =
    // "\\s*([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4})\\s*";
    // private static final String pattern =
    // "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";

    private static final AlphaAnimation enableAnim = new AlphaAnimation(0.6f,
            1.0f);

    private static final AlphaAnimation disableAnim = new AlphaAnimation(1.0f,
            0.6f);

    private static final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;


    public static boolean stringIsEmpty(String string) {

        if (string != null) {
            if (!string.trim().equals("")) {
                return false;
            }
        }
        return true;
    }

    public static boolean stringIsNotEmpty(String string) {
        if (string != null && !string.equals("null")) {
            if (!string.trim().equals("")) {
                return true;
            }
        }
        return false;
    }

    public static String[] stringTokenizer(String string) {

        StringTokenizer tokens = new StringTokenizer(string, ",");
        String[] result = new String[tokens.countTokens()];

        for (int i = 0; i < tokens.countTokens(); i++) {
            result[i] = tokens.nextToken();
        }

        return result;
    }

    public static String ArrayToString(ArrayList<String> string) {

        String result = "";
        StringBuilder builder = new StringBuilder();
        int len = string.size() - 1;
        if (string.size() > 0) {
            for (int i = 0; i < len; i++) {
                result += string.get(i) + ", ";
                builder.append(string.get(i));
                builder.append(", ");
            }
            result += string.get(len);
            builder.append(string.get(len));
        }

        return builder.toString();
    }

    public static boolean isValidEmail(String s) {

        // return Pattern.matches(pattern, s);
        try {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches();
        } catch (NullPointerException exception) {
            return false;
        }
    }

    public static void enableView(View v) {
        if (Build.VERSION.SDK_INT >= 11) {
            try {
                v.setAlpha(1.0f);
                return;
            } catch (Exception e) {
            }
        } else {
            enableAnim.setDuration(100);
            enableAnim.setFillAfter(true);
            v.startAnimation(enableAnim);
        }
    }

    public static void disableView(View v) {
        if (Build.VERSION.SDK_INT >= 11) {
            try {
                v.setAlpha(0.6f);
                return;
            } catch (Exception e) {
            }
        } else {
            disableAnim.setDuration(100);
            disableAnim.setFillAfter(true);
            v.startAnimation(disableAnim);
        }

    }

    public static String stripHtml(String html) {
        String s = Html.fromHtml(html).toString();
        s = s.replaceAll("[\n\r\t]", " ");
        return s;
    }

    public static boolean containSpace(String s) {
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(s);
        return matcher.find();
    }

    public static String getYoutubeVideoId(String youtubeUrl) {
        String video_id = "";
        if (youtubeUrl != null && youtubeUrl.trim().length() > 0
                && youtubeUrl.startsWith("http")) {
            String expression = "^.*((youtu.be"
                    + "\\/)"
                    + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
            CharSequence input = youtubeUrl;
            Pattern pattern = Pattern.compile(expression,
                    Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    video_id = groupIndex1;
            }
        }
        return video_id;
    }

    public static String encodeImage(Bitmap bitmap) {
        String encodedImage = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            baos.close();
            baos = null;
            encodedImage = Base64.encodeToString(b,
                    Base64.NO_WRAP);
        } catch (Exception e) {

        }
        return encodedImage;
    }

    public static void hideKeyboardFrom(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void showKeyboardAt(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (GalleryUtils.isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (GalleryUtils.isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return GalleryUtils.getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (GalleryUtils.isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return GalleryUtils.getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (GalleryUtils.isFileProviderUri(context, uri)) {
                return GalleryUtils.getFileProviderPath(context, uri);
            }

            return GalleryUtils.getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
}
