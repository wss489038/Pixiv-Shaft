package ceui.lisa.file;

import android.content.Context;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.text.TextUtils;

import androidx.documentfile.provider.DocumentFile;

import ceui.lisa.activities.Shaft;
import ceui.lisa.download.FileCreator;
import ceui.lisa.helper.FileStorageHelper;
import ceui.lisa.models.IllustsBean;
import ceui.lisa.utils.Common;

public class SAFile {

    public static DocumentFile getDocument(Context context, IllustsBean illust, int index, boolean deleteOld) {
        DocumentFile root = rootFolder(context);
        String displayName = FileCreator.customFileName(illust, index);
        String id = DocumentsContract.getTreeDocumentId(root.getUri());
        String subDirectoryName = FileStorageHelper.getShaftIllustR18DirNameWithInnerR18Folder(illust);
        String authorDirectoryName = FileStorageHelper.getAuthorDirectoryName(illust.getUser());
        if(!TextUtils.isEmpty(subDirectoryName)){
            id += "/" + subDirectoryName;
        }
        boolean saveForSeparateAuthor = authorDirectoryName.length() > 0;
        if(saveForSeparateAuthor){
            id += "/" + authorDirectoryName;
        }
        id += "/" + displayName;
        Uri childrenUri = DocumentsContract.buildDocumentUriUsingTree(root.getUri(), id);
        DocumentFile realFile = DocumentFile.fromSingleUri(context, childrenUri);
        if (realFile != null && realFile.exists()) {
            if (deleteOld) {
                try {
                    DocumentsContract.deleteDocument(context.getContentResolver(), realFile.getUri());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return realFile;
            }
        }

        DocumentFile finalDirectory;
        DocumentFile subDirectory = root;
        if (!TextUtils.isEmpty(subDirectoryName)) {
            subDirectory = root.findFile(subDirectoryName);
            if (subDirectory == null) {
                subDirectory = root.createDirectory(subDirectoryName);
            }
        }
        finalDirectory = subDirectory;

        if (saveForSeparateAuthor) {
            DocumentFile authorDirectory = subDirectory.findFile(authorDirectoryName);
            if (authorDirectory == null) {
                authorDirectory = subDirectory.createDirectory(authorDirectoryName);
            }
            finalDirectory = authorDirectory;
        }
        return finalDirectory.createFile(getMimeTypeFromIllust(illust, index), displayName);
    }

    public static DocumentFile rootFolder(Context context) {
        String rootUriString = Shaft.sSettings.getRootPathUri();
        Uri uri = Uri.parse(rootUriString);
        try {
            return DocumentFile.fromTreeUri(context, uri);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isFileExists(Context context, IllustsBean illust) {
        return isFileExists(context, illust, 0);
    }

    public static boolean isFileExists(Context context, IllustsBean illust, int index) {
        DocumentFile root = rootFolder(context);
        if (root != null) {
            String id = DocumentsContract.getTreeDocumentId(root.getUri());
            String displayName = illust.isGif() ? new FileName().gifName(illust) : FileCreator.customFileName(illust, index);
            id = FileStorageHelper.getIllustFileSAFFullName(id, illust, displayName);
            Uri childrenUri = DocumentsContract.buildDocumentUriUsingTree(root.getUri(), id);
            DocumentFile realFile = DocumentFile.fromSingleUri(context, childrenUri);
            return realFile != null && realFile.exists();
        } else {
            return false;
        }
    }

    public static String getMimeTypeFromIllust(IllustsBean illust, int index) {
        String url;
        if (illust.getPage_count() == 1) {
            url = illust.getMeta_single_page().getOriginal_image_url();
        } else {
            url = illust.getMeta_pages().get(index).getImage_urls().getOriginal();
        }

        String result = "png";
        if (url.contains(".")) {
            result = url.substring(url.lastIndexOf(".") + 1);
        }
        Common.showLog("getMimeTypeFromIllust fileUrl: " + url + ", fileType: " + result);
        return "image/" + result;
    }
}
