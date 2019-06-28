package com.deep.pyrun.core;

import android.content.Intent;

import com.deep.pyrun.util.RecordService;
import com.intelligence.dpwork.DpWorkApplication;
import com.intelligence.dpwork.annotation.DpBugly;
import com.intelligence.dpwork.annotation.DpCrash;
import com.intelligence.dpwork.annotation.DpLang;
import com.intelligence.dpwork.lang.LanguageType;

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
