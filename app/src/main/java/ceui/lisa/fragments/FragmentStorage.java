package ceui.lisa.fragments;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ceui.lisa.R;
import ceui.lisa.activities.Shaft;
import ceui.lisa.database.AppDatabase;
import ceui.lisa.database.IllustRecmdEntity;
import ceui.lisa.databinding.FragmentStorageBinding;
import ceui.lisa.models.IllustsBean;

public class FragmentStorage extends BaseFragment<FragmentStorageBinding> {

    private List<IllustRecmdEntity> localData;

    @Override
    protected void initLayout() {
        mLayoutID = R.layout.fragment_storage;
    }

    private static final int WRITE_REQUEST_CODE = 43;

    @Override
    protected void initView() {
        localData = AppDatabase.getAppDatabase(mContext).recmdDao().getAll();
        List<IllustsBean> temp = new ArrayList<>();
        for (int i = 0; i < localData.size(); i++) {
            IllustsBean illustsBean = Shaft.sGson.fromJson(
                    localData.get(i).getIllustJson(), IllustsBean.class);
            temp.add(illustsBean);
        }
        baseBind.store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        baseBind.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });
    }
}
