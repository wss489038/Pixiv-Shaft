package ceui.lisa.utils;

import android.content.Context;
import android.content.Intent;

import ceui.lisa.R;
import ceui.lisa.interfaces.IExecutor;
import ceui.lisa.models.IllustsBean;

/**
 * 分享
 */
public abstract class ShareIllust implements IExecutor {

    public static final String URL_Head = "https://www.pixiv.net/artworks/";
    private final IllustsBean mIllustsBean;
    private final Context mContext;

    public ShareIllust(Context context, IllustsBean illustsBean) {
        mContext = context;
        mIllustsBean = illustsBean;
    }

    @Override
    public void execute() {
        onPrepare();
        share();
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,
                mContext.getString(R.string.share_illust,
                        mIllustsBean.getTitle(),
                        mIllustsBean.getUser().getName(),
                        URL_Head + mIllustsBean.getId()));
        mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.share)));
    }
}
