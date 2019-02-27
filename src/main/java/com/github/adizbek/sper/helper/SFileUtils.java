package com.github.adizbek.sper.helper;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import com.blankj.utilcode.util.EncodeUtils;
import com.github.adizbek.sper.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SFileUtils {
    private static final String TAG = SFileUtils.class.getName();

    public static Info read(Uri uri) {
        ContentResolver contentResolver = BaseApplication.c.getContentResolver();

        Cursor cursor = contentResolver
                .query(uri, null, null, null, null, null);

        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                String displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                Log.i(TAG, "Display Name: " + displayName);
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                // If the size is unknown, the value stored is null.  But since an
                // int can't be null in Java, the behavior is implementation-specific,
                // which is just a fancy term for "unpredictable".  So as
                // a rule, check if it's null before assigning to an int.  This will
                // happen often:  The storage API allows for remote files, whose
                // size might not be locally known.
                int size = 0;

                try {
                    if (!cursor.isNull(sizeIndex)) {
                        // Technically the column stores an int, but cursor.getString()
                        // will do the conversion automatically.
                        size = Integer.parseInt(cursor.getString(sizeIndex));
                    }
                } catch (Exception ignored) {
                }

                Log.i(TAG, "Size: " + size);

                return new Info(uri, displayName, size, contentResolver.getType(uri));
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return null;
    }

    public static class Info {
        public String name, type;
        public int size;
        public Uri uri;

        public Info(Uri uri, String name, int size, String type) {
            this.name = name;
            this.size = size;
            this.uri = uri;
            this.type = type;
        }

        @SuppressLint("DefaultLocale")
        public String getSize() {
            int v = 0;
            int b = size;

            while (b >= 1024) {
                b /= 1024;
                v++;
            }

            if (v == 0) {
                return String.format("%d byte", b);
            } else if (v == 1) {
                return String.format("%d Kb", b);
            } else if (v == 2) {
                return String.format("%d Mb", b);
            } else if (v == 3) {
                return String.format("%d Gb", b);
            } else if (v == 4) {
                return String.format("%d Tb", b);
            } else {
                return String.format("%d byte", size);
            }
        }

        public InputStream getInput() throws IOException {
            return BaseApplication.c.getContentResolver().openInputStream(uri);
        }

        public byte[] getBytes() throws IOException {
            InputStream inputStream = getInput();

            StringBuilder stringBuilder = new StringBuilder();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int read;
            byte[] buff = new byte[4096];

            while ((read = inputStream.read(buff, 0, 4096)) != -1) {
                out.write(buff, 0, read);
            }

            inputStream.close();

            return out.toByteArray();
        }

        public String getBase64() {
            try {
                return EncodeUtils.base64Encode2String(getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "";
        }
    }
}
