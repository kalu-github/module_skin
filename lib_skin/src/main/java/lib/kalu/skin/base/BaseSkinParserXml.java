package lib.kalu.skin.base;

import android.support.annotation.NonNull;
import android.view.View;

import lib.kalu.skin.model.AttributeModel;


/**
 * description: 解析xml
 * created by kalu on 2020-11-11
 */
public interface BaseSkinParserXml {

    /**
     * 将属性AttrModel通过resource设置到当前view上
     *
     * @param view      当前view
     * @param attrModel 属性
     * @param resource  设置的资源工具
     */
    void parserXml(@NonNull View view, @NonNull AttributeModel attrModel, @NonNull BaseSkinResource resource);
}
