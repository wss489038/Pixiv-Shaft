package ceui.lisa.helper;

import android.content.Context;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import androidx.documentfile.provider.DocumentFile;
import ceui.lisa.core.DownloadItem;
import ceui.lisa.file.SAFile;
import okhttp3.Response;
import rxhttp.wrapper.callback.UriFactory;

public class SAFactory extends UriFactory {

    private final DownloadItem mItem;
    private Uri mUri;

    public SAFactory(@NotNull Context context, DownloadItem item) {
        super(context);
        this.mItem = item;
        DocumentFile file = SAFile.getDocument(getContext(), mItem.getIllust(), mItem.getIndex(), mItem.shouldStartNewDownload());
        mUri = file.getUri();
    }

    @Nullable
    @Override
    public Uri query() {
        return mUri;
    }

    @NotNull
    @Override
    public Uri insert(@NotNull Response response) {
        return mUri;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }
}
