package com.ffzxnet.developutil.ui.skin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.ffzxnet.developutil.BuildConfig;
import com.ffzxnet.developutil.R;
import com.ffzxnet.developutil.base.ui.BaseActivity;
import com.ffzxnet.developutil.utils.tools.FileUtil;
import com.wind.me.xskinloader.SkinManager;
import com.wind.me.xskinloader.util.AssetFileUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 更换皮肤
 *  皮肤制作参考app_skin
 *  设置对应的资源文件，文件路径与名称要和主App一样
 *  点击Build 获取到apk文件放入主App项目下面的assets/skins/下面
 *  后续远程升级可以将皮肤apk放到服务，app下载到本地，文件后缀改成skin即可
 *  具体参考onViewClicked()
 *  更多使用详情参考：https://github.com/WindySha/XSkinLoader
 */
public class SkinDemoActivity extends BaseActivity {
    @BindView(R.id.skin_image)
    ImageView skinImage;
    @BindView(R.id.skin_button)
    Button skinButton;

    private boolean changeSkin = false;

    @Override
    public int getContentViewByBase(Bundle savedInstanceState) {
        return R.layout.activity_skin;
    }

    @Override
    public void createdViewByBase(Bundle savedInstanceState) {
        initToolBar("","更换皮肤");
        //每个需要更换皮肤的Activity都要在onCreate()设置 ,这里我统一放到了BaseActivity,看个人需求
        //        SkinInflaterFactory.setFactory(this);
        //        super.onCreate(savedInstanceState);
        //某些view的资源是在代码中动态设置的，使用以下方式来设置资源，才能实现换肤效果：
        //注意: 图片只支持drawable文件下的 ,如果可以，建议需要更换的控件都用代码设置，这样好维护
        //设置imageView的src资源
        SkinManager.get().setImageDrawable(skinImage, R.drawable.icon_logo1);
        //设置imageView的backgroud资源
        SkinManager.get().setViewBackground(skinButton, R.drawable.btn_skin_btn);
    }

    @Override
    protected void onClickTitleBack() {
        SkinManager.get().restoreToDefaultSkin();
        goBackByQuick();
    }


    @OnClick(R.id.skin_button)
    public void onViewClicked() {
        if (changeSkin) {
            //使用默认皮肤
            SkinManager.get().restoreToDefaultSkin();
        } else {
            String saveDir = FileUtil.Skin;
            String savefileName = "light.skin";
            String asset_dir = "skins/app_skin-debug.apk";
            File file = new File(saveDir + File.separator + savefileName);
            if (!file.exists() || BuildConfig.DEBUG) {
                //将皮肤apk拷贝到本地文件夹里面
                AssetFileUtils.copyAssetFile(this, asset_dir, saveDir, savefileName);
            }
            //加载指定路径皮肤
            SkinManager.get().loadSkin(file.getAbsolutePath());
        }
        changeSkin = !changeSkin;
    }
}
