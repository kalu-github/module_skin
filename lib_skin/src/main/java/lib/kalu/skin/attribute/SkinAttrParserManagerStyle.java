package lib.kalu.skin.attribute;

import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lib.kalu.skin.model.AttributeModel;
import lib.kalu.skin.pstyle.SkinParserStyleProgressBarIndeterminateDrawable;
import lib.kalu.skin.pstyle.SkinParserStyleTextColor;
import lib.kalu.skin.base.BaseSkinParserStyle;

/**
 * description: style属性解析
 * created by kalu on 2020-11-10
 */
public final class SkinAttrParserManagerStyle {

    private static List<BaseSkinParserStyle> sStyleParserArray = new ArrayList<>();

    static {
        addStyleParser(new SkinParserStyleTextColor());
        addStyleParser(new SkinParserStyleProgressBarIndeterminateDrawable());
    }

    public static void addStyleParser(BaseSkinParserStyle parser) {
        if (!sStyleParserArray.contains(parser)) {
            sStyleParserArray.add(parser);
        }
    }

    public static void parseStyle(View view, AttributeSet attrs, Map<String, AttributeModel> viewAttrs, String[] specifiedAttrList) {
        for (BaseSkinParserStyle parser : sStyleParserArray) {
            parser.parseStyle(view, attrs, viewAttrs, specifiedAttrList);
        }
    }
}
