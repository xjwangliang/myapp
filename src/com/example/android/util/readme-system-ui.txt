void android.view.View.setSystemUiVisibility(int visibility)
	Added in API level 11
	Request that the visibility of the status bar or other screen/window decorations be changed. 
 Bitwise-or of flags 
	 SYSTEM_UI_FLAG_LOW_PROFILE, 
	 SYSTEM_UI_FLAG_HIDE_NAVIGATION, 
	 SYSTEM_UI_FLAG_FULLSCREEN, 
	 SYSTEM_UI_FLAG_LAYOUT_STABLE, 
	 SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION, 
	 SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.  
  
  
This method is used to put the over device UI into temporary modes where the user's attention is focused more on the application content, 
by dimming or hiding surrounding system affordances. This is typically used in conjunction with Window.FEATURE_ACTION_BAR_OVERLAY, allowing 
the applications content to be placed behind the action bar (and with these flags other system affordances) so that smooth transitions between 
hiding and showing them can be done. 

Two representative examples of the use of system UI visibility is implementing a content browsing application (like a magazine reader) and a 
video playing application. 

The first code shows a typical implementation of a View in a content browsing application. In this implementation, the application goes into a 
content-oriented mode by hiding the status bar and action bar, and putting the navigation elements into lights out mode. The user can then interact 
with content while in this mode. Such an application should provide an easy way for the user to toggle out of the mode (such as to check information 
in the status bar or access notifications). In the implementation here, this is done simply by tapping on the content. 


例子在：com.example.android.apis.view.ContentBrowserActivity(apidemos)

public static class Content extends ScrollView
        implements View.OnSystemUiVisibilityChangeListener, View.OnClickListener {
    TextView mText;
    TextView mTitleView;
    SeekBar mSeekView;
    boolean mNavVisible;
    int mBaseSystemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | SYSTEM_UI_FLAG_LAYOUT_STABLE;
    int mLastSystemUiVis;

    Runnable mNavHider = new Runnable() {
        @Override public void run() {
            setNavVisibility(false);
        }
    };

    public Content(Context context, AttributeSet attrs) {
        super(context, attrs);

        mText = new TextView(context);
        mText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mText.setText(context.getString(R.string.alert_dialog_two_buttons2ultra_msg));
        mText.setClickable(false);
        mText.setOnClickListener(this);
        mText.setTextIsSelectable(true);			//笔记:文字可选
        addView(mText, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setOnSystemUiVisibilityChangeListener(this);
    }

    public void init(TextView title, SeekBar seek) {
        // This called by the containing activity to supply the surrounding
        // state of the content browser that it will interact with.
        mTitleView = title;
        mSeekView = seek;
        setNavVisibility(true);
    }

    @Override public void onSystemUiVisibilityChange(int visibility) {
        // Detect when we go out of low-profile mode, to also go out
        // of full screen.  We only do this when the low profile mode
        // is changing from its last state, and turning off.
        int diff = mLastSystemUiVis ^ visibility;
        mLastSystemUiVis = visibility;
        if ((diff&SYSTEM_UI_FLAG_LOW_PROFILE) != 0
                && (visibility&SYSTEM_UI_FLAG_LOW_PROFILE) == 0) {
            setNavVisibility(true);
        }
    }

    @Override protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        // When we become visible, we show our navigation elements briefly
        // before hiding them.
        setNavVisibility(true);
        getHandler().postDelayed(mNavHider, 2000);
    }

    @Override protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        // When the user scrolls, we hide navigation elements.
        setNavVisibility(false);
    }

    @Override public void onClick(View v) {
        // When the user clicks, we toggle the visibility of navigation elements.
        int curVis = getSystemUiVisibility();
        setNavVisibility((curVis&SYSTEM_UI_FLAG_LOW_PROFILE) != 0);
    }

    void setBaseSystemUiVisibility(int visibility) {
        mBaseSystemUiVisibility = visibility;
    }

    void setNavVisibility(boolean visible) {
        int newVis = mBaseSystemUiVisibility;
        if (!visible) {
            newVis |= SYSTEM_UI_FLAG_LOW_PROFILE | SYSTEM_UI_FLAG_FULLSCREEN;//不可见时设置SYSTEM_UI_FLAG_LOW_PROFILE和SYSTEM_UI_FLAG_FULLSCREEN
        }
        final boolean changed = newVis == getSystemUiVisibility();

        // Unschedule any pending event to hide navigation if we are
        // changing the visibility, or making the UI visible.
        if (changed || visible) {
            Handler h = getHandler();
            if (h != null) {
                h.removeCallbacks(mNavHider);
            }
        }

        // Set the new desired visibility.
        setSystemUiVisibility(newVis);
        mTitleView.setVisibility(visible ? VISIBLE : INVISIBLE);
        mSeekView.setVisibility(visible ? VISIBLE : INVISIBLE);
    }
}


This second code sample shows a typical implementation of a View in a video playing application. In this situation, while the video is playing the application would like to go into a complete full-screen mode, to use as much of the display as possible for the video. When in this state the user can not interact with the application; the system intercepts touching on the screen to pop the UI out of full screen mode. See fitSystemWindows(Rect) for a sample layout that goes with this code. 

public static class Content extends ImageView implements
        View.OnSystemUiVisibilityChangeListener, View.OnClickListener,
        ActionBar.OnMenuVisibilityListener {
    Activity mActivity;
    TextView mTitleView;
    Button mPlayButton;
    SeekBar mSeekView;
    boolean mAddedMenuListener;
    boolean mMenusOpen;
    boolean mPaused;
    boolean mNavVisible;
    int mLastSystemUiVis;

    Runnable mNavHider = new Runnable() {
        @Override public void run() {
            setNavVisibility(false);
        }
    };

    public Content(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnSystemUiVisibilityChangeListener(this);
        setOnClickListener(this);
    }

    public void init(Activity activity, TextView title, Button playButton,
            SeekBar seek) {
        // This called by the containing activity to supply the surrounding
        // state of the video player that it will interact with.
        mActivity = activity;
        mTitleView = title;
        mPlayButton = playButton;
        mSeekView = seek;
        mPlayButton.setOnClickListener(this);
        setPlayPaused(true);
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mActivity != null) {
            mAddedMenuListener = true;
            mActivity.getActionBar().addOnMenuVisibilityListener(this);
        }
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAddedMenuListener) {
            mActivity.getActionBar().removeOnMenuVisibilityListener(this);
        }
    }

    @Override public void onSystemUiVisibilityChange(int visibility) {
        // Detect when we go out of nav-hidden mode, to clear our state
        // back to having the full UI chrome up.  Only do this when
        // the state is changing and nav is no longer hidden.
        int diff = mLastSystemUiVis ^ visibility;
        mLastSystemUiVis = visibility;
        if ((diff&SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0
                && (visibility&SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
            setNavVisibility(true);
        }
    }

    @Override protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        // When we become visible or invisible, play is paused.
        setPlayPaused(true);
    }

    @Override public void onClick(View v) {
        if (v == mPlayButton) {
            // Clicking on the play/pause button toggles its state.
            setPlayPaused(!mPaused);
        } else {
            // Clicking elsewhere makes the navigation visible.
            setNavVisibility(true);
        }
    }

    @Override public void onMenuVisibilityChanged(boolean isVisible) {
        mMenusOpen = isVisible;
        setNavVisibility(true);
    }

    void setPlayPaused(boolean paused) {
        mPaused = paused;
        mPlayButton.setText(paused ? R.string.play : R.string.pause);
        setKeepScreenOn(!paused);
        setNavVisibility(true);
    }

    void setNavVisibility(boolean visible) {
        int newVis = SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | SYSTEM_UI_FLAG_LAYOUT_STABLE;
        if (!visible) {
            newVis |= SYSTEM_UI_FLAG_LOW_PROFILE | SYSTEM_UI_FLAG_FULLSCREEN
                    | SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // If we are now visible, schedule a timer for us to go invisible.
        if (visible) {
            Handler h = getHandler();
            if (h != null) {
                h.removeCallbacks(mNavHider);
                if (!mMenusOpen && !mPaused) {
                    // If the menus are open or play is paused, we will not auto-hide.
                    h.postDelayed(mNavHider, 1500);
                }
            }
        }

        // Set the new desired visibility.
        setSystemUiVisibility(newVis);
        mTitleView.setVisibility(visible ? VISIBLE : INVISIBLE);
        mPlayButton.setVisibility(visible ? VISIBLE : INVISIBLE);
        mSeekView.setVisibility(visible ? VISIBLE : INVISIBLE);
    }
}


FEATURE_ACTION_BAR_OVERLAY字段
笔记：应该也可以这样设置<item name="android:windowActionBarOverlay">true</item>， getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
public static final int Window.FEATURE_ACTION_BAR_OVERLAY 
	Added in API level 11 
	Flag for requesting an Action Bar that overlays window content. Normally an Action Bar will sit in the space above window content, but if this 
	feature is requested along with FEATURE_ACTION_BAR it will be layered over the window content itself. This is useful if you would like your app 
	to have more control over how the Action Bar is displayed, such as letting application content scroll beneath an Action Bar with a transparent 
	background or otherwise displaying a transparent/translucent Action Bar over application content. 

This mode is especially useful with View.SYSTEM_UI_FLAG_FULLSCREEN, which allows you to seamlessly hide the action bar in conjunction with other 
screen decorations. 

As of JELLY_BEAN, when an ActionBar is in this mode it will adjust the insets provided to View.fitSystemWindows(Rect) to include the content covered 
by the action bar, so you can do layout within that space. 笔记问题：神马意思？

FEATURE_ACTION_BAR
FEATURE_ACTION_MODE_OVERLAY


