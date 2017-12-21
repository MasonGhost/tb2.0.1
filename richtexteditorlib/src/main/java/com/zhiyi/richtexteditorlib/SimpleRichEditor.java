package com.zhiyi.richtexteditorlib;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.inputmethod.InputMethodManager;

import com.zhiyi.richtexteditorlib.base.RichEditor;
import com.zhiyi.richtexteditorlib.constant.ItemIndex;
import com.zhiyi.richtexteditorlib.factories.BaseItemFactory;
import com.zhiyi.richtexteditorlib.factories.DefaultItemFactory;
import com.zhiyi.richtexteditorlib.utils.SelectController;
import com.zhiyi.richtexteditorlib.view.BottomMenu;
import com.zhiyi.richtexteditorlib.view.api.ITheme;
import com.zhiyi.richtexteditorlib.view.logiclist.MenuItem;
import com.zhiyi.richtexteditorlib.view.menuitem.AbstractBottomMenuItem;
import com.zhiyi.richtexteditorlib.view.theme.AbstractTheme;
import com.zhiyi.richtexteditorlib.view.theme.DarkTheme;
import com.zhiyi.richtexteditorlib.view.theme.LightTheme;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.log.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class SimpleRichEditor extends RichEditor {

    @SuppressWarnings("unused")
    public void setOnStateChangeListener(OnStateChangeListener mOnStateChangeListener) {
        this.mOnStateChangeListener = mOnStateChangeListener;
    }

    public BaseItemFactory getBaseItemFactory() {
        if (mBaseItemFactory == null) {
            mBaseItemFactory = createDefaultFactory();
        }
        return mBaseItemFactory;
    }

    private DefaultItemFactory createDefaultFactory() {
        return new DefaultItemFactory();
    }


    /**
     * @param baseItemFactory the bottomItem factory that will override the default factory
     *                        设置新的工厂方法生产自定义的底栏 Item 项
     */
    public void setBaseItemFactory(BaseItemFactory baseItemFactory) {
        this.mBaseItemFactory = baseItemFactory;
    }


    public interface OnEditorClickListener {
        void onLinkButtonClick();

        void onInsertImageButtonClick();

        void onLinkClick(String name, String url);

        void onImageClick(Long id);

        void onTextStypeClick(boolean isSelect);

        void onInputListener(int length);
    }

    @SuppressWarnings("unused")
    public abstract static class OnEditorClickListenerImp implements OnEditorClickListener {
        @Override
        public void onImageClick(Long id) {

        }

        @Override
        public void onInsertImageButtonClick() {

        }

        @Override
        public void onLinkButtonClick() {

        }

        @Override
        public void onLinkClick(String name, String url) {

        }
    }

    private BottomMenu mBottomMenu;
    private SelectController mSelectController;
    private OnEditorClickListener mOnEditorClickListener;
    private ArrayList<Long> mFreeItems;//不受其他items点击事件影响的items
    private ItemIndex.Register mRegister;
    private OnStateChangeListener mOnStateChangeListener;
    private BaseItemFactory mBaseItemFactory;

    public SimpleRichEditor(Context context) {
        super(context);
    }

    public SimpleRichEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleRichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBottomMenu(@NonNull BottomMenu mBottomMenu) {
        this.mBottomMenu = mBottomMenu;
        init();
        initRichTextViewListeners();
    }

    public void setOnEditorClickListener(OnEditorClickListener mOnEditorClickListener) {
        this.mOnEditorClickListener = mOnEditorClickListener;
    }

    private void init() {
        mSelectController = SelectController.createController();
        mRegister = ItemIndex.getInstance().getRegister();
        mFreeItems = new ArrayList<>();

        addArrow();
        addLink();
        addHalvingLine();
        addTypefaceBranch(true, true, true, true, true);
//        addMoreBranch(true, true);
        addUndo();
        addRedo();
        addImageInsert();

//        等效与以下
//       mLuBottomMenu.
//                addRootItem(MenuItemFactory.generateImageItem(getContext(), 0x01, R.drawable
// .insert_image, false)).//
//                addRootItem(MenuItemFactory.generateImageItem(getContext(), 0x02, R.drawable.a)
// ).//
//                addRootItem(MenuItemFactory.generateImageItem(getContext(), 0x03, R.drawable
// .more)).//
//                addRootItem(MenuItemFactory.generateImageItem(getContext(), 0x04, R.drawable
// .undo, false)).
//                addRootItem(MenuItemFactory.generateImageItem(getContext(), 0x05, R.drawable
// .redo, false)).
//
//                addItem(0x02, MenuItemFactory.generateImageItem(getContext(), 0x06, R.drawable
// .bold)).
//                addItem(0x02, MenuItemFactory.generateImageItem(getContext(), 0x07, R.drawable
// .italic)).
//                addItem(0x02, MenuItemFactory.generateImageItem(getContext(), 0x08, R.drawable
// .strikethrough)).
//                addItem(0x02, MenuItemFactory.generateImageItem(getContext(), 0x09, R.drawable
// .blockquote)).
//                addItem(0x02, MenuItemFactory.generateImageItem(getContext(), 0x0a, R.drawable
// .h1)).
//                addItem(0x02, MenuItemFactory.generateImageItem(getContext(), 0x0b, R.drawable
// .h2)).
//                addItem(0x02, MenuItemFactory.generateImageItem(getContext(), 0x0c, R.drawable
// .h3)).
//                addItem(0x02, MenuItemFactory.generateImageItem(getContext(), 0x0d, R.drawable
// .h4)).
//                addItem(0x03, MenuItemFactory.generateImageItem(getContext(), 0x0e, R.drawable
// .halving_line, false)).
//                addItem(0x03, MenuItemFactory.generateImageItem(getContext(), 0x0f, R.drawable
// .link, false));
        //mLuBottomMenu.setOnItemClickListener(this);

        //mSelectController.addAll(0x09L, 0x0aL, 0x0bL, 0x0cL, 0x0dL);


        mSelectController.setHandler(new SelectController.StatesTransHandler() {
            @Override
            public void handleA2B(long id) {
                if (id > 0) {
                    mBottomMenu.setItemSelected(id, true);
                }
            }

            @Override
            public void handleB2A(long id) {
                if (id > 0) {
                    mBottomMenu.setItemSelected(id, false);
                }
            }
        });
    }

    private void initRichTextViewListeners() {

        setOnDecorationChangeListener((text, types) -> {
            onStateChange(text, types);

            for (long id : mFreeItems) {
                mBottomMenu.setItemSelected(id, false);
            }
            mSelectController.reset();
            for (Type t :
                    types) {
                if (!mSelectController.contain(t.getTypeCode())) {
                    mBottomMenu.setItemSelected(t.getTypeCode(), true);
                } else {
                    mSelectController.changeState(t.getTypeCode());
                }
            }

        });
        setOnTextChangeListener(text -> mOnEditorClickListener.onInputListener(text));
        setOnFocusChangeListener(isFocus -> {
            if (!isFocus) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mBottomMenu.show(200);
                }
            } else {
                mBottomMenu.hide(200);
            }

        });
        setOnLinkClickListener((linkName, url) -> showChangeLinkDialog(linkName, url));
        setOnImageClickListener(id -> showImageClick(id));

        setOnInitialLoadListener(isReady -> {
            if (isReady) {
                focusEditor();
            }
        });

        mBottomMenu.setOnItemClickListener(new AbstractBottomMenuItem.OnItemClickListener() {
            @Override
            public void onItemClick(MenuItem item) {
                if (item.getId() == ItemIndex.A) {
                    mOnEditorClickListener.onTextStypeClick(item.getSelected());
                }
            }
        });
    }

    /**
     * @param text  传入的字段
     * @param types 含有的类型
     *              自定义时添加监听以实现自定义按钮的逻辑
     */
    private void onStateChange(String text, List<Type> types) {
        if (mOnStateChangeListener != null) {
            mOnStateChangeListener.onStateChangeListener(text, types);
        }
    }

    private void showLinkDialog() {
        if (mOnEditorClickListener != null) {
            mOnEditorClickListener.onLinkButtonClick();
        }
    }

    private void showImagePicker() {
        if (mOnEditorClickListener != null) {
            mOnEditorClickListener.onInsertImageButtonClick();
        }
    }

    private void onTextStypeClick(boolean isSelect) {
        if (mOnEditorClickListener != null) {
            mOnEditorClickListener.onTextStypeClick(isSelect);
        }
    }

    private void showImageClick(Long id) {
        if (mOnEditorClickListener != null) {
            mOnEditorClickListener.onImageClick(id);
        }
    }

    private void showChangeLinkDialog(String linkName, String url) {
        if (mOnEditorClickListener != null) {
            mOnEditorClickListener.onLinkClick(linkName, url);
        }
    }

    private boolean isInSelectController(long id) {
        if (mSelectController.contain(id)) {
            mSelectController.changeState(id);
            return true;
        }
        return false;
    }

    public void setTheme(int theme) {
        if (theme == AbstractTheme.DARK_THEME) {
            mBottomMenu.setTheme(new DarkTheme());
            //do something
        } else if (theme == AbstractTheme.LIGHT_THEME) {
            mBottomMenu.setTheme(new LightTheme());
            //do something
        }
    }

    public void setTheme(final ITheme theme) {
        mBottomMenu.setTheme(theme);

        post(() -> {
            String backgroundColor = ConvertUtils.converInt2HexColor(theme.getBackGroundColors()[0]);
            //从高亮色和基础色中找出和背景明度差异大的作为字体颜色
            double backgroundLum = ColorUtils.calculateLuminance(theme.getBackGroundColors()
                    [0]);
            double normalLum = ColorUtils.calculateLuminance(theme.getNormalColor());
            double accentLum = ColorUtils.calculateLuminance(theme.getAccentColor());

            int fontColorInt;
            if (Math.abs(normalLum - backgroundLum) > Math.abs(accentLum - backgroundLum)) {
                fontColorInt = theme.getNormalColor();
            } else {
                fontColorInt = theme.getAccentColor();
            }

            String fontColor = ConvertUtils.converInt2HexColor(fontColorInt);
            //找出背景色和字体色的中间色作为引用块底色
            //unused
            int color = ColorUtils.blendARGB(fontColorInt, theme.getBackGroundColors()[0],
                    0.5f);

            exec("javascript:RE.setBackgroundColor('" + backgroundColor + "');");
            exec("javascript:RE.setFontColor('" + fontColor + "');");
        });

        //do something
    }

    public SimpleRichEditor addTypefaceBranch(boolean needBold, boolean needItalic, boolean
            needStrikeThrough, boolean needBlockQuote, boolean needH) {
        checkNull(mBottomMenu);

        if (!(needBlockQuote || needBold || needH || needItalic || needStrikeThrough)) {
            return this;
        }
        if (needBlockQuote) {
            mSelectController.add(ItemIndex.BLOCK_QUOTE);
        }
        if (needH) {
            mSelectController.addAll(ItemIndex.H1, ItemIndex.H2, ItemIndex.H3, ItemIndex.H4);
        }

        if (needBold) {
            mFreeItems.add(ItemIndex.BOLD);
        }
        if (needItalic) {
            mFreeItems.add(ItemIndex.ITALIC);
        }
        if (needStrikeThrough) {
            mFreeItems.add(ItemIndex.STRIKE_THROUGH);
        }

        mBottomMenu.addRootItem(getBaseItemFactory().generateItem(getContext(), ItemIndex.A))
                .addItem(ItemIndex.A, needBold ? getBaseItemFactory().generateItem(
                        getContext(),
                        ItemIndex.BOLD,
                        (item, isSelected) -> {
                            setBold();
                            LogUtils.d("onItemClick", item.getId() + "");

                            //不拦截不在选择控制器中的元素让Menu自己控制选择显示效果
                            return isInSelectController(item.getId());
                        }) : null)
                .addItem(ItemIndex.A, needItalic ? getBaseItemFactory().generateItem(
                        getContext(),
                        ItemIndex.ITALIC,
                        (item, isSelected) -> {
                            setItalic();
                            LogUtils.d("onItemClick", item.getId() + "");

                            return isInSelectController(item.getId());
                        }) : null)
                .addItem(ItemIndex.A, needStrikeThrough ? getBaseItemFactory().generateItem(
                        getContext(),
                        ItemIndex.STRIKE_THROUGH,
                        (item, isSelected) -> {
                            setStrikeThrough();
                            LogUtils.d("onItemClick", item.getId() + "");

                            return isInSelectController(item.getId());
                        }) : null)
                .addItem(ItemIndex.A, needBlockQuote ? getBaseItemFactory().generateItem(
                        getContext(),
                        ItemIndex.BLOCK_QUOTE,
                        (item, isSelected) -> {
                            setBlockquote(!isSelected);
                            LogUtils.d("onItemClick", item.getId() + "");

                            //mSelectController.changeState(ItemIndex.BLOCK_QUOTE);
                            return isInSelectController(item.getId());
                        }) : null)

                .addItem(ItemIndex.A, needH ? getBaseItemFactory().generateItem(
                        getContext(),
                        ItemIndex.H1,
                        (item, isSelected) -> {
                            setHeading(1, !isSelected);
                            LogUtils.d("onItemClick", item.getId() + "");

                            //mSelectController.changeState(ItemIndex.H1);
                            return isInSelectController(item.getId());
                        }) : null)
                .addItem(ItemIndex.A, needH ? getBaseItemFactory().generateItem(
                        getContext(),
                        ItemIndex.H2,
                        (item, isSelected) -> {
                            setHeading(2, !isSelected);
                            //mSelectController.changeState(ItemIndex.H2);
                            return isInSelectController(item.getId());
                        }) : null)
                .addItem(ItemIndex.A, needH ? getBaseItemFactory().generateItem(
                        getContext(),
                        ItemIndex.H3,
                        (item, isSelected) -> {
                            setHeading(3, !isSelected);
                            //mSelectController.changeState(ItemIndex.H3);
                            return isInSelectController(item.getId());
                        }) : null)
                .addItem(ItemIndex.A, needH ? getBaseItemFactory().generateItem(
                        getContext(),
                        ItemIndex.H4,
                        (item, isSelected) -> {
                            setHeading(4, !isSelected);
                            //mSelectController.changeState(ItemIndex.H4);
                            return isInSelectController(item.getId());
                        }) : null);
        return this;
    }

    /**
     * 自定义的 收起 软键盘按钮
     *
     * @return
     */
    public SimpleRichEditor addArrow() {
        addRootCustomItem(ItemIndex.ARROW, getBaseItemFactory().generateItem(
                getContext(),
                ItemIndex.ARROW,
                (item, isSelected) -> {
                    InputMethodManager imm = (InputMethodManager) getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(SimpleRichEditor.this.getWindowToken(), 0);
                    return true;
                }));
        return this;
    }

    public SimpleRichEditor addImageInsert() {
        checkNull(mBottomMenu);

        mBottomMenu.addRootItem(getBaseItemFactory().generateItem(
                getContext(),
                ItemIndex.INSERT_IMAGE,
                (item, isSelected) -> {
                    showImagePicker();
                    return true;
                }));
        return this;
    }

    public SimpleRichEditor addLink() {
        checkNull(mBottomMenu);

        mBottomMenu.addRootItem(getBaseItemFactory().generateItem(
                getContext(),
                ItemIndex.LINK,
                (item, isSelected) -> {
                    showLinkDialog();
                    return false;
                }));
        return this;
    }

    public SimpleRichEditor addHalvingLine() {
        checkNull(mBottomMenu);

        mBottomMenu.addRootItem(getBaseItemFactory().generateItem(
                getContext(),
                ItemIndex.HALVING_LINE,
                (item, isSelected) -> {
                    insertHr();
                    InputMethodManager imm = (InputMethodManager) getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(SimpleRichEditor.this.getWindowToken(), 0);
                    return false;
                }));
        return this;
    }

    public SimpleRichEditor addMoreBranch(boolean needHalvingLine, boolean needLink) {
        checkNull(mBottomMenu);

        if (!needHalvingLine && !needLink) {
            return this;
        }
        mBottomMenu.addRootItem(getBaseItemFactory().generateItem(getContext(), ItemIndex.MORE))
                .addItem(ItemIndex.MORE, needHalvingLine ? getBaseItemFactory().generateItem(
                        getContext(),
                        ItemIndex.HALVING_LINE,
                        (item, isSelected) -> {
                            insertHr();
                            LogUtils.d("onItemClick", item.getId() + "");
                            return false;
                        }
                ) : null)
                .addItem(ItemIndex.MORE, needLink ? getBaseItemFactory().generateItem(
                        getContext(),
                        ItemIndex.LINK,
                        (item, isSelected) -> {
                            showLinkDialog();
                            LogUtils.d("onItemClick", item.getId() + "");

                            return false;
                        }
                ) : null);
        return this;
    }

    public SimpleRichEditor addUndo() {
        checkNull(mBottomMenu);

        mBottomMenu.addRootItem(getBaseItemFactory().generateItem(
                getContext(),
                ItemIndex.UNDO,
                (item, isSelected) -> {
                    undo();
                    return false;
                }));
        return this;
    }

    public SimpleRichEditor addRedo() {
        checkNull(mBottomMenu);

        mBottomMenu.addRootItem(getBaseItemFactory().generateItem(getContext(),
                ItemIndex.REDO,
                (item, isSelected) -> {
                    redo();
                    return false;
                }));
        return this;
    }

    @SuppressWarnings("unused")
    public SimpleRichEditor addCustomItem(long parentId, long id, AbstractBottomMenuItem item) {
        checkNull(mBottomMenu);

        if (!mRegister.hasRegister(parentId)) {
            throw new RuntimeException(parentId + ":" + ItemIndex.NO_REGISTER_EXCEPTION);
        }
        if (mRegister.isDefaultId(id)) {
            throw new RuntimeException(id + ":" + ItemIndex.HAS_REGISTER_EXCEPTION);
        }

        if (!mRegister.hasRegister(id)) {
            mRegister.register(id);
        }

        item.getMenuItem().setId(id);
        mBottomMenu.addItem(parentId, item);
        return this;
    }

    public SimpleRichEditor addRootCustomItem(long id, AbstractBottomMenuItem item) {
        checkNull(mBottomMenu);

        if (mRegister.isDefaultId(id)) {
            throw new RuntimeException(id + ":" + ItemIndex.HAS_REGISTER_EXCEPTION);
        }
        if (!mRegister.hasRegister(id)) {
            mRegister.register(id);
        }
        item.getMenuItem().setId(id);
        mBottomMenu.addRootItem(item);
        return this;
    }

    private void checkNull(Object obj) {
        if (obj == null) {
            throw new RuntimeException("object can't be null");
        }
    }
}
