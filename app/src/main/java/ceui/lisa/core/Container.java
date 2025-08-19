package ceui.lisa.core;

import android.text.TextUtils;

import java.util.HashMap;

import ceui.lisa.utils.Common;

public class Container {

    private HashMap<String, PageData> pages = new HashMap<>();
    private boolean isNetworking = false;

    /**
     * 用 HashMap 存储，app杀掉之后就没有了
     *
     * @param pageData 一个插画列表
     */
    public void addPageToMap(PageData pageData) {
        if (pageData == null) {
            return;
        }

        if (TextUtils.isEmpty(pageData.getUUID())) {
            return;
        }

        if (pages == null) {
            pages = new HashMap<>();
        }

        pages.put(pageData.getUUID(), pageData);
        Common.showLog("Container addPage " + pageData.getUUID());
    }

    public PageData getPage(String uuid) {
        Common.showLog("Container getPage " + uuid);
        if (TextUtils.isEmpty(uuid)) {
            return null;
        }

        if (pages == null || pages.size() == 0) {
            return null;
        }

        return pages.get(uuid);
    }

    public void clear() {
        Common.showLog("Container clear ");
        if (pages == null) {
            pages = new HashMap<>();
            return;
        }
        if (pages.size() != 0) {
            pages.clear();
        }
    }

    public boolean isNetworking() {
        return isNetworking;
    }

    public void setNetworking(boolean networking) {
        isNetworking = networking;
    }

    private Container() {
    }

    private static class SingleTonHolder {
        private static final Container INSTANCE = new Container();
    }

    public static Container get() {
        return SingleTonHolder.INSTANCE;
    }
}
