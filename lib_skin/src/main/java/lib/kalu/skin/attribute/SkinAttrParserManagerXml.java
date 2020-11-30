package lib.kalu.skin.attribute;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import lib.kalu.skin.model.AttributeModel;
import lib.kalu.skin.pxml.SkinParserXmlActivityStatusBarColor;
import lib.kalu.skin.pxml.SkinParserXmlBackground;
import lib.kalu.skin.pxml.SkinParserXmlImageDrawable;
import lib.kalu.skin.pxml.SkinParserXmlListViewDivider;
import lib.kalu.skin.pxml.SkinParserXmlListViewSelector;
import lib.kalu.skin.pxml.SkinParserXmlProgressBarIndeterminateDrawable;
import lib.kalu.skin.pxml.SkinParserXmlTextColorHint;
import lib.kalu.skin.pxml.SkinParserXmlTextColor;
import lib.kalu.skin.base.BaseSkinParserXml;

/**
 * description: xml属性解析
 * created by kalu on 2020-11-10
 */
public final class SkinAttrParserManagerXml {

    public static final String BACKGROUND = "background";
    public static final String IMAGE_SRC = "src";
    public static final String TEXT_COLOR = "textColor";
    public static final String TEXT_COLOR_HINT = "textColorHint";
    public static final String LIST_SELECTOR = "listSelector";
    public static final String DIVIDER = "divider";

    public static final String ACTIVITY_STATUS_BAR_COLOR = "statusBarColor";
    public static final String PROGRESSBAR_INDETERMINATE_DRAWABLE = "indeterminateDrawable";


    //存放支持的换肤属性和对应的处理器
    private static Map<String, BaseSkinParserXml> sSupportedSkinDeployerMap = new HashMap<String, BaseSkinParserXml>();

    //静态注册支持的属性和处理器
    static {
        registerDeployer(BACKGROUND, new SkinParserXmlBackground());
        registerDeployer(IMAGE_SRC, new SkinParserXmlImageDrawable());
        registerDeployer(TEXT_COLOR, new SkinParserXmlTextColor());
        registerDeployer(TEXT_COLOR_HINT, new SkinParserXmlTextColorHint());
        registerDeployer(LIST_SELECTOR, new SkinParserXmlListViewSelector());
        registerDeployer(DIVIDER, new SkinParserXmlListViewDivider());
        registerDeployer(ACTIVITY_STATUS_BAR_COLOR, new SkinParserXmlActivityStatusBarColor());
        registerDeployer(PROGRESSBAR_INDETERMINATE_DRAWABLE, new SkinParserXmlProgressBarIndeterminateDrawable());
    }

    public static void registerDeployer(String attrName, BaseSkinParserXml skinResDeployer) {
        if (TextUtils.isEmpty(attrName) || null == skinResDeployer) {
            return;
        }
        if (sSupportedSkinDeployerMap.containsKey(attrName)) {
            throw new IllegalArgumentException("The attrName has been registed, please rename it");
        }
        sSupportedSkinDeployerMap.put(attrName, skinResDeployer);
    }

    public static BaseSkinParserXml of(AttributeModel attr) {
        if (attr == null) {
            return null;
        }
        return of(attr.attrName);
    }

    public static BaseSkinParserXml of(String attrName) {
        if (TextUtils.isEmpty(attrName)) {
            return null;
        }
        return sSupportedSkinDeployerMap.get(attrName);
    }

    public static boolean isSupportedAttr(String attrName) {
        return of(attrName) != null;
    }

    public static boolean isSupportedAttr(AttributeModel attr) {
        return of(attr) != null;
    }

}
