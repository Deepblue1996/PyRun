package com.deep.pyrun.view;

import android.widget.ImageView;

import com.deep.dpwork.annotation.DpLayout;
import com.deep.dpwork.annotation.DpMainScreen;
import com.deep.dpwork.util.DTimeUtil;
import com.deep.pyrun.R;
import com.deep.pyrun.base.TBaseScreen;

import butterknife.BindView;

/**
 * Logo
 * Created by Administrator on 2019/6/30 0030.
 */
@DpMainScreen
@DpLayout(R.layout.logo_screen)
public class LogoScreen extends TBaseScreen {

    @BindView(R.id.logoImg)
    ImageView logoImg;

    @Override
    public void init() {
        DTimeUtil.run(500, new DTimeUtil.DTimeUtilListener() {
            @Override
            public void run() {
                openAndClose(MainScreen.class);
            }
        });
    }
}
