package lib.kalu.skin.model;

import android.support.annotation.Keep;

import java.io.Serializable;

/**
 * description: 样式
 * created by kalu on 2020-11-10
 */
@Keep
public final class AttributeModel implements Serializable {
    /***
     * 对应View的属性
     */
    public String attrName;

    /***
     * 属性值对应的reference id值，类似R.color.XX
     */
    public int attrValueRefId;

    /***
     * 属性值refrence id对应的名称，如R.color.XX，则此值为"XX"
     */
    public String attrValueRefName;

    /***
     * 属性值refrence id对应的类型，如R.color.XX，则此值为color
     */
    public String attrValueTypeName;

    public AttributeModel(String attrName, int attrValueRefId, String attrValueRefName, String attrValueTypeName) {
        this.attrName = attrName;
        this.attrValueRefId = attrValueRefId;
        this.attrValueRefName = attrValueRefName;
        this.attrValueTypeName = attrValueTypeName;
    }

    @Override
    public String toString() {
        return "AttributeModel{" +
                "attrName='" + attrName + '\'' +
                ", attrValueRefId=" + attrValueRefId +
                ", attrValueRefName='" + attrValueRefName + '\'' +
                ", attrValueTypeName='" + attrValueTypeName + '\'' +
                '}';
    }
}
