package com.tkpphr.android.medianode.demo;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class UriResolver {
    private UriResolver(){}

    public static String getFilePath(Context context, Uri uri){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(DocumentsContract.isDocumentUri(context, uri)){
                String documentId=DocumentsContract.getDocumentId(uri);
                if("com.android.externalstorage.documents".equals(uri.getAuthority())){
                    return getDocumentExternalStorage(documentId);
                }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                    return getDocumentDownload(context,documentId);
                }else if("com.android.providers.media.documents".equals(uri.getAuthority())){
                    return getDocumentMedia(context,documentId);
                }
            }
        }
        if("content".equals(uri.getScheme())){
            return getDataColumn(context,uri,null,null);
        }else if("file".equals(uri.getScheme())){
            return uri.getPath();
        }
        return null;
    }

    private static String getDocumentExternalStorage(String id){
        String[] type=id.split(":");
        if("primary".equalsIgnoreCase(type[0])){
            return Environment.getExternalStorageDirectory()+"/"+type[1];
        }else{
            return null;
        }
    }

    private static String getDocumentDownload(Context context,String id){
        Uri uri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

        return getDataColumn(context,uri,null,null);
    }

    private static String getDocumentMedia(Context context,String id){
        Uri contentUri=null;
        String[] type=id.split(":");
        if("image".equals(type[0])){
            contentUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }else if("audio".equals(type[0])){
            contentUri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        return getDataColumn(context,contentUri,"_id=?",new String[]{type[1]});
    }

    private static String getDataColumn(Context context,Uri uri,String selection,String[] selectionArgs) {
        String[] columns = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, columns, selection, selectionArgs, null);
        if(cursor==null){
            return "";
        }
        String data;
        try {
            int index=cursor.getColumnIndexOrThrow(columns[0]);
            cursor.moveToFirst();
            data=cursor.getString(index);
        } catch (Exception e){
            e.printStackTrace();
            data="";
        }finally {
            cursor.close();
        }

        return data;
    }
}
