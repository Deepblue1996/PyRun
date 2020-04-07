package com.deep.pyrun.core;

import android.content.Intent;

import com.deep.dpwork.DpWorkApplication;
import com.deep.dpwork.annotation.DpBugly;
import com.deep.dpwork.annotation.DpCrash;
import com.deep.dpwork.annotation.DpLang;
import com.deep.dpwork.lang.LanguageType;
import com.deep.pyrun.service.RecordService;

/**
 * Class - 应用入口
 * <p>
 * Created by Deepblue on 2018/9/29 0029.
 */
@DpCrash
@DpLang(LanguageType.LANGUAGE_CHINESE_SIMPLIFIED)
@DpBugly("801275d37c")
public class CoreApp extends DpWorkApplication {

    /**
     * 初始化函数
     * Bugly ID
     */
    @Override
    protected void initApplication() {

        // 启动 Marvel service
        startService(new Intent(this, RecordService.class));
    }

}
