package com.zhiyicx.thinksnsplus.comment;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringDef;
import android.support.v4.util.ArrayMap;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

import static android.support.annotation.RestrictTo.Scope.GROUP_ID;

/**
 * @Author Jliuer
 * @Date 2017/04/26/11:08
 * @Email Jliuer@aliyun.com
 * @Description 评论操作的公共类
 */
public class CommonMetadata extends BaseListBean implements Parcelable {
    private static final String TAG = "CommonMetadata";

    public static final int SEND_ING = 0;
    public static final int SEND_SUCCESS = 1;
    public static final int SEND_ERROR = 2;

    public static final String METADATA_KEY_COMMENT_ID = "zhiyi.common.metadata.comment_id";
    public static final String METADATA_KEY_MAX_ID = "zhiyi.common.metadata.max_id";
    public static final String METADATA_KEY_COMMENT_TYPE = "zhiyi.common.metadata.comment_type";
    public static final String METADATA_KEY_SOURCE_ID = "zhiyi.common.metadata.source_id";
    public static final String METADATA_KEY_TARGET_ID = "zhiyi.common.metadata.target_id";
    public static final String METADATA_KEY_COMMENT_STATE = "zhiyi.common.metadata.comment_state";
    public static final String METADATA_KEY_COMMENT_URL = "zhiyi.common.metadata.comment_url";
    public static final String METADATA_KEY_DELETE_URL = "zhiyi.common.metadata.delete_url";
    public static final String METADATA_KEY_COMMENT_CONTENT = "zhiyi.common.metadata.comment_content";
    public static final String METADATA_KEY_COMMENT_MARK = "zhiyi.common.metadata.comment_mark";
    public static final String METADATA_KEY_TO_USER = "zhiyi.common.metadata.to_user";
    public static final String METADATA_KEY_TO_USER_ID = "zhiyi.common.metadata.to_user_id";
    public static final String METADATA_KEY_FROM_USER = "zhiyi.common.metadata.from_user";
    public static final String METADATA_KEY_AUTHOR_USER = "zhiyi.common.metadata.author_user";
    public static final String METADATA_KEY_CREATED_DATE = "zhiyi.common.metadata.created_at";
    public static final String METADATA_KEY_UPDATED_DATE = "zhiyi.common.metadata.updated_at";


    private static final int METADATA_TYPE_LONG = 0;
    private static final int METADATA_TYPE_STRING = 1;
    private static final int METADATA_TYPE_INTEGER = 2;
    private static final int METADATA_TYPE_OBJECT = 4;

    private static final ArrayMap<String, Integer> METADATA_KEYS_TYPE;

    private final Bundle mBundle;

    private CommonMetadata(Bundle bundle) {
        mBundle = bundle;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    static {
        METADATA_KEYS_TYPE = new ArrayMap<>();
        METADATA_KEYS_TYPE.put(METADATA_KEY_COMMENT_ID, METADATA_TYPE_INTEGER);
        METADATA_KEYS_TYPE.put(METADATA_KEY_MAX_ID, METADATA_TYPE_INTEGER);
        METADATA_KEYS_TYPE.put(METADATA_KEY_COMMENT_TYPE, METADATA_TYPE_INTEGER);
        METADATA_KEYS_TYPE.put(METADATA_KEY_COMMENT_STATE, METADATA_TYPE_INTEGER);
        METADATA_KEYS_TYPE.put(METADATA_KEY_SOURCE_ID, METADATA_TYPE_INTEGER);
        METADATA_KEYS_TYPE.put(METADATA_KEY_TARGET_ID, METADATA_TYPE_INTEGER);
        METADATA_KEYS_TYPE.put(METADATA_KEY_TO_USER_ID, METADATA_TYPE_INTEGER);

        METADATA_KEYS_TYPE.put(METADATA_KEY_COMMENT_URL, METADATA_TYPE_STRING);
        METADATA_KEYS_TYPE.put(METADATA_KEY_DELETE_URL, METADATA_TYPE_STRING);
        METADATA_KEYS_TYPE.put(METADATA_KEY_COMMENT_CONTENT, METADATA_TYPE_STRING);
        METADATA_KEYS_TYPE.put(METADATA_KEY_CREATED_DATE, METADATA_TYPE_STRING);
        METADATA_KEYS_TYPE.put(METADATA_KEY_UPDATED_DATE, METADATA_TYPE_STRING);

        METADATA_KEYS_TYPE.put(METADATA_KEY_COMMENT_MARK, METADATA_TYPE_LONG);

        METADATA_KEYS_TYPE.put(METADATA_KEY_TO_USER, METADATA_TYPE_OBJECT);
        METADATA_KEYS_TYPE.put(METADATA_KEY_FROM_USER, METADATA_TYPE_OBJECT);
        METADATA_KEYS_TYPE.put(METADATA_KEY_AUTHOR_USER, METADATA_TYPE_OBJECT);
    }

    /**
     * @hide
     */
    @RestrictTo(GROUP_ID)
    @StringDef({METADATA_KEY_COMMENT_URL, METADATA_KEY_COMMENT_CONTENT, METADATA_KEY_CREATED_DATE,
            METADATA_KEY_UPDATED_DATE, METADATA_KEY_DELETE_URL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextKey {
    }

    @RestrictTo(GROUP_ID)
    @StringDef({METADATA_KEY_COMMENT_MARK})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LongKey {
    }

    @RestrictTo(GROUP_ID)
    @StringDef({METADATA_KEY_COMMENT_ID, METADATA_KEY_COMMENT_STATE, METADATA_KEY_SOURCE_ID
            , METADATA_KEY_TARGET_ID, METADATA_KEY_TO_USER_ID, METADATA_KEY_COMMENT_TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IntegerKey {
    }

    @RestrictTo(GROUP_ID)
    @StringDef({METADATA_KEY_TO_USER, METADATA_KEY_FROM_USER, METADATA_KEY_AUTHOR_USER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ObjectKey {
    }

    @RestrictTo(GROUP_ID)
    @IntDef({SEND_SUCCESS, SEND_ING, SEND_ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface StateValue {
    }

    public int size() {
        return mBundle.size();
    }

    public Set<String> keySet() {
        return mBundle.keySet();
    }

    public static final class Builder {

        private final Bundle mBundle;

        public Builder() {
            mBundle = new Bundle();
        }

        public Builder(CommonMetadata source) {
            mBundle = new Bundle(source.mBundle);
        }

        public Builder putString(@TextKey String key, String value) {
            if (METADATA_KEYS_TYPE.containsKey(key)) {
                if (METADATA_KEYS_TYPE.get(key) != METADATA_TYPE_STRING) {
                    throw new IllegalArgumentException("The " + key
                            + " key cannot be used to put a CharSequence");
                }
            }
            mBundle.putString(key, value);
            return this;
        }


        public Builder putInteger(@IntegerKey String key, Integer value) {
            if (METADATA_KEYS_TYPE.containsKey(key)) {
                if (METADATA_KEYS_TYPE.get(key) != METADATA_TYPE_INTEGER) {
                    throw new IllegalArgumentException("The " + key
                            + " key cannot be used to put a Integer");
                }
            }
            mBundle.putInt(key, value);
            return this;
        }

        public Builder putState(@IntegerKey String key, @StateValue Integer value) {
            if (METADATA_KEYS_TYPE.containsKey(key)) {
                if (METADATA_KEYS_TYPE.get(key) != METADATA_TYPE_INTEGER) {
                    throw new IllegalArgumentException("The " + key
                            + " key cannot be used to put a Integer");
                }
            }
            mBundle.putInt(key, value);
            return this;
        }

        public Builder putLong(@LongKey String key, Long value) {
            if (METADATA_KEYS_TYPE.containsKey(key)) {
                if (METADATA_KEYS_TYPE.get(key) != METADATA_TYPE_LONG) {
                    throw new IllegalArgumentException("The " + key
                            + " key cannot be used to put a Long");
                }
            }
            mBundle.putLong(key, value);
            return this;
        }

        public Builder putObj(@ObjectKey String key, Parcelable value) {
            if (METADATA_KEYS_TYPE.containsKey(key)) {
                if (METADATA_KEYS_TYPE.get(key) != METADATA_TYPE_OBJECT) {
                    throw new IllegalArgumentException("The " + key
                            + " key cannot be used to put a Parcelable");
                }
            }
            mBundle.putParcelable(key, value);
            return this;
        }

        public CommonMetadata build() {
            return new CommonMetadata(mBundle);
        }
    }

    public String getString(@TextKey String key) {
        return mBundle.getString(key);
    }

    public long getLong(@LongKey String key) {
        return mBundle.getLong(key, -1);
    }

    public int getInteger(@IntegerKey String key) {
        return mBundle.getInt(key, -1);
    }

    public Parcelable getParcelable(@ObjectKey String key) {
        return mBundle.getParcelable(key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.mBundle);
    }

    protected CommonMetadata(Parcel in) {
        this.mBundle = in.readBundle();
    }

    public static final Creator<CommonMetadata> CREATOR = new Creator<CommonMetadata>() {
        @Override
        public CommonMetadata createFromParcel(Parcel source) {
            return new CommonMetadata(source);
        }

        @Override
        public CommonMetadata[] newArray(int size) {
            return new CommonMetadata[size];
        }
    };
}
