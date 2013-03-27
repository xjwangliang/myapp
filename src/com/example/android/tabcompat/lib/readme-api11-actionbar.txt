ActionBar
----------------------------------------------------------
[添加Action Bar]
----------------------------------------------------------
添加标签或是修改Action Bar风格，就需要将android:minSdkVersion设为“11”，这样才能使用ActionBar类。
<uses-sdk android:minSdkVersion="4"
              android:targetSdkVersion="11" />
              
如果希望从某个指定的活动中移除Action Bar，只需将该活动的主题设为Theme.Holo.NoActionBar。例如：
<activity android:theme="@android:style/Theme.Holo.NoActionBar">

提示：如果希望从一个自定义的主题中移除Action Bar，只需将android:windowActionBar样式属性设为false。关于更多Action Bar样式的内容请参见“对Action Bar使用样式”。

当Action Bar隐藏时，系统将调整活动的内容来填充可用的屏幕空间。

注意：如果移除了使用主题的Action Bar，那么该窗口将完全禁用Action Bar而无法再在运行时添加——调用getActionBar()将返回null

附录：android自动创建的风格：
<style name="AppBaseTheme" parent="android:Theme.Holo.Light">
        <!-- API 11 theme customizations can go here. -->
</style>
    
<style name="AppBaseTheme" parent="android:Theme.Holo.Light.DarkActionBar">
    <!-- API 14 theme customizations can go here. -->
</style>

例如：对于api-14（在values-14<bool name="atLeastIceCreamSandwich">true</bool>）
<activity android:name=".view.ContentBrowserActivity"
                android:label="Views/System UI Visibility/Content Browser"
                android:theme="@android:style/Theme.Holo.Light.DarkActionBar"
                android:uiOptions="splitActionBarWhenNarrow">
</activity>
splitActionBarWhenNarrow将android:showAsAction的menu显示在屏幕下方而不是上方
        
----------------------------------------------------------
[ 添加标签（tab）]
----------------------------------------------------------

首先，布局必须在每一个和显示的表现相关联的Fragment中包含一个View。要确保该视图有一个能在代码中引用的ID。

要添加标签至Action Bar：

1. 创建一个ActionBar.TabListener的实现来处理Action Bar标签的交互事件。比如实现所有的方法：onTabSelected()、onTabUnselected()和onTabReselected()。
每一个回馈方法都将传递收到了事件的ActionBar.Tab和一个FragmentTransaction用以执行片段事务（添加或是移除片段）。

 private class MyTabListener implements ActionBar.TabListener {
    private TabContentFragment mFragment;

    // Called to create an instance of the listener when adding a new tab
    public MyTabListener(TabContentFragment fragment) {
        mFragment = fragment;
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        ft.add(R.id.fragment_content, mFragment, null);
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        ft.remove(mFragment);
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // do nothing
    }

}
标签被选中时所有需要进行的行为都必须在ActionBar.TabListener回馈方法中被定义。当一个标签被选中，它将收到一个onTabSelected()的调用，应当在这里通过使用add()及所提供的FragmentTransaction来向布局中指定的视图添加正确的片段。类似的，当一个标签被取消选中时
（因为另一个标签被选中了），则应当使用remove()来从布局中移除该片段
注意：不能为这些事务调用commit()——系统会调用它，如果手动调用的话将会抛出一个例外。同时也不能将这些片段事务添加至返回栈。



2. 在onCreate()时通过在Activity中调用getActionBar()来获取活动的ActionBar（不过要注意需要在调用了setContentView()之后才这么做）。
3. 调用setNavigationMode(NAVIGATION_MODE_TABS)来开启ActionBar的标签模式。
4. 为Action Bar创建所有的标签：
	1. 通过对ActionBar调用newTab()来创建新的ActionBar.Tab。
	2. 通过调用setText()和/或setIcon()来为标签添加文字和/或图标。
	提示：这些方法返回相同的ActionBar.Tab实例，所以可以将这些调用一起使用。
	3. 声明ActionBar.TabListener，并通过向setTabListener()传递其实现的一个实例来使用它。
5. 通过对ActionBar调用addTab()来添加所有的ActionBar.Tab，并传递它们。
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    // setup Action Bar for tabs
    final ActionBar actionBar = getActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    // remove the activity title to make space for tabs
    actionBar.setDisplayShowTitleEnabled(false);

    // instantiate fragment for the tab
    Fragment artistsFragment = new ArtistsFragment();
    // add a new tab and set its title text and tab listener
    actionBar.addTab(actionBar.newTab().setText(R.string.tab_artists)
            .setTabListener(new TabListener(artistsFragment)));

    Fragment albumsFragment = new AlbumsFragment();
    actionBar.addTab(actionBar.newTab().setText(R.string.tab_albums)
            .setTabListener(new TabListener(albumsFragment)));
}

如果活动被中止，应当保存当前选中的标签，这样当用户返回应用程序时，可以打开该保存的标签。在保存状态时，可以通过getSelectedNavigationIndex()获取当前选中的标签。这将返回所选标签的索引位置。

注意：应该在必要时保存每一个片段的状态，这样用户在切换标签并返回前一个片段时能保持之前的样子。关于保存片段状态的更过信息，请参见“片段”开发者指南。

----------------------------------------------------------
[添加一个动作视图（Action View）]
----------------------------------------------------------
动作视图是在Action Bar上出现的一个控件，作为动作项目的一种替代。例如，如果在Option Menu中有一个项目是“搜索”，那么当该项目作为动作项目使用时可以在Action Bar中为该项目添加一个提供SearchView控件的项目。
为一个项目声明动作视图最好的方式是在菜单资源中使用android:actionLayout或是android:actionViewClass属性：

<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/menu_search"
        android:title="Search"
        android:icon="@drawable/ic_menu_search"
        android:showAsAction="ifRoom"
        android:actionLayout="@layout/searchview" />
</menu>

android:actionViewClass的值必须是一个所要使用的View的完整类名。例如：
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/menu_search"
        android:title="Search"
        android:icon="@drawable/ic_menu_search"
        android:showAsAction="ifRoom"
        android:actionViewClass="android.widget.SearchView" />
</menu>

必须包含android:showAsAction=”ifRoom”以使项目在空间足够时作为动作视图显示。不过，在必要时，可以通过设置android:showAsAction为“always”来强制该项目以动作视图显示。

现在，当菜单项目作为一个动作项目显示时，它将是一个动作视图而不是图标和/或标题文本。不过，如果在Action Bar中没有足够的空间的话，该项目将在浮动式菜单中以一个通常菜单项目的形式显示，必须在onOptionsItemSelected()回馈方法中响应该项目。

当活动首次启动时，系统通过调用onCreateOptionsMenu()生成Action Bar和浮动式菜单。当菜单在该方法中被展开之后，可以通过以菜单项目的ID调用findItem()来获取动作视图的元素（比如为了绑定监听器），而后对所返回的MenuItem调用getActionView()。例如，上面的例子里的搜索控件可以像这样获得：

@Override
public boolean onCreateOptionsMenu(Menu menu) {
  getMenuInflater().inflate(R.menu.options, menu);
  SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
  // Set appropriate listeners for searchView
  ...
  return super.onCreateOptionsMenu(menu);
}
关于使用搜索控件的更多信息，请参见“创建一个搜索接口”。

----------------------------------------------------------
【添加动作项目】
----------------------------------------------------------
动作项目仅仅是Option Menu中被声明要直接显示在Action Bar上的菜单项目。一个动作项目可以包括一个图标和/或文本。如果一个菜单项目不是动作项目，那么系统将把它放在浮动式菜单中，用户可以通过选择Action Bar右侧的菜单图标打开浮动式菜单。

当活动启动时，系统将通过调用onCreateOptionMenu()来为活动生成Action Bar和浮动式菜单。如同在“菜单”开发者指南中所描述的，这是为活动定义Option Menu的回馈方法。
可以指定某一菜单项目作为动作项目显示——如果有这样的空间的话——通过在菜单资源中为<item>元素声明android:showAsAction=”ifRoom”。这样，该菜单项目仅会在空间足够时显示在Action Bar中以供快速选择。如果空间不足，该项目将被置于浮动式菜单中（通过Action Bar右侧的菜单图标打开）。
可以在应用程序代码中声明一个菜单项目为动作项目，只需对MenuItem调用setShowAsAction()并传递SHOW_AS_ACTION_IF_ROOM。

如果菜单项目同时提供了标题和图标，那么动作项目默认只显示图标。如果希望让动作项目包含文本，需要在XML中添加”with text”旗标、对android:showAsAction属性添加withText，或是在程序代码中调用setShowAsAction()并使用SHOW_AS_ACTION_WITH_TEXT旗标。图2展示了有两个带有文字的动作项目以及浮动式菜单图标的Action Bar。


下面是一个在菜单资源文件中将菜单项目声明为动作项目的一个范例：

<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/menu_save"
          android:icon="@drawable/ic_menu_save"
          android:title="@string/menu_save"
    android:showAsAction="ifRoom|withText" />
</menu>
在这时，ifRoom和withText旗标都被设置了，所以当这个项目作为动作项目显示时，它包含了图标以及标题文本。

Action Bar中的菜单项目和Option Menu中的其他项目都会启发相同的回馈方法。当用户选择了一个动作项目时，活动将收到一个onOptionsItemSelected()的调用，并传递项目ID。

【注意】：如果用片段中添加菜单项目，那么该片段相应的onOptionsItemSelected()方法将被调用。然而活动可以在此之前先对其进行处理，也就是说系统对活动的onOptionsItemSelected()调用要先于对片段的。

可以声明一个项目总是作为动作项目出现，不过这并不被推荐，因为这会在有太多的动作项目时让UI变得混乱，Action Bar中的动作项目将会项目重叠。

关于菜单的更多信息，请参见“菜单”开发者指南。

将应用图标用作菜单项目

应用程序图标默认出现在Action Bar的左侧。它也将响应用户交互操作（当用户点击它时，它会有和其他动作项目一样的视觉响应），但需要手动指定用户点击时的行为。


通常的行为应当是在用户单击图标时让应用程序回到“主活动”或是（比如，在活动没有发生变化而片段变化了的时候）回到初始状态。如果用户已经处于主活动或是初始状态，则不必进行任何处理。

当用户单击图标时，系统以android.R.id.home的ID调用该活动的onOptionsItemSelected()方法。因此，需要在onOptionsItemSelected()方法中添加一个条件判断来侦听android.R.id.home并执行正确的行为，例如启动主活动或是将最近的片段事务出栈。

如果通过返回主活动来响应应用图标的点击，那么需要在Intent内包含FLAG_ACTIVITY_CLEAR_TOP旗标。这样，如果要启动的活动已经存在于当前任务的话，所有在其上的活动将被销毁，该活动将回到最上层。这种方式是令人满意的，因为回到“主活动”和“返回”是等价的，不应该为主活动创建新的实例。否则，最终在当前任务中会有一个很长的活动栈。

例如，下面是一个onOptionsItemSelected()的实现，它将返回应用程序的“主活动”：

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case android.R.id.home:
            // app icon in Action Bar clicked; go home
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
}

使用应用图标来进行”向上一级“导航

还可以使用应用程序的图标来为用户提供“向上一级”的导航。这在程序中的活动总是以某种固定的顺序出现并期望用户能方便地返回上一级活动的情况下特别有用（不过无所谓用户是怎样进入当前的活动的）。

响应这一事件的方式和返回主活动的方式是相同的（和上文所说的类似，只不过现在是根据当前的活动是哪一个来启动另一个不同的活动）。为了告诉用户这时的情况将有所不同，唯一需要做的就是把Action Bar设为“show home as up”。这通过对活动的ActionBar调用setDisplayHomeAsUpEnabled(true)即可完成。这时，系统将为应用程序的图标增加一个表示向上一层动作的箭头，就像图4这样。

例如，下面是将应用图标表示为“向上一层”动作的方法：

@Override
protected void onStart() {
    super.onStart();
    ActionBar actionBar = this.getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
}
之后，活动应当在用户单击图标时进行响应，在onOptionsItemSelected()中，通过监听ID android.R.id.home（如上所示）。在这种情况下，当向上导航时，在Intent中使用FLAG_ACTIVITY_CLEAR_TOP旗标是更为重要的，这样才能不在上级活动的实例已经存在时还再次创建一个新的。