package com.deep.pyrun.view;

import com.deep.pyrun.R;
import com.deep.pyrun.base.TBaseScreen;
import com.intelligence.dpwork.annotation.DpLayout;
import com.intelligence.dpwork.util.DTimeUtil;

/**
 * Logo
 * Created by Administrator on 2019/6/30 0030.
 */
@DpLayout(R.layout.logo_screen)
public class LogoScreen extends TBaseScreen {
    @Override
    public void init() {
        DTimeUtil.get().run(this, new DTimeUtil.DTimeRun() {
            @Override
            public void uiRun() {
                openAndClose(new MainScreen());
            }
        }, 500);
    }
}
