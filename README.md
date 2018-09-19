# Demo_FragmentTabhost
FragmentTabHost实现中间按钮凸出效果https://www.jianshu.com/p/302bde10349f
目前很多app主页都是由几个tab页组成，所以我们开发app的时候一般都会涉及到主页tab的切换实现。常用的主页tab切换实现可以用viewpage和FragmentActivity组合，用普通Button/TextView/RadioButton等等和FragmentTransaction的add、replace、remove、hide和show方法组合，以及Android官方框架FragmentTabHost。前面两种实现起来会比较容易一点，但是，当你的底部菜单凹凸不规则或者 是要嵌入其它标签（比如消息提示功能）的时候，那 FragmentTabHost 实现起来就更容易了。

FragmentTabHost 作为 Android 4.0 版本的控件当然优点多多，已被项目广泛使用，Android 5.0版本又推出TabLayout+ViewPager实现多页切换的效果。此处主要讲使用FragmentTabHost 实现中间按钮凸出效果，说起FragmentTabHost，相信小伙伴们用得比较多也比较熟悉的是用其来实现类似如下图所示的tab效果吧！
![image](http://upload-images.jianshu.io/upload_images/3989735-c2739911c403af62?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

上图效果实现起来也是比较简单的，今天我要记录的是使用FragmentTabHost 实现如下图所示的效果：
![image](http://upload-images.jianshu.io/upload_images/3989735-45209bbf89853907?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

可能会有很多大神看完效果图 噗呲 一笑，觉得小菜一碟。那么这些大神可以绕道通行啦！

下面直接进入正题，上代码干货：

1.首先是activity_main.xml布局文件引用v4包的FragmentTabHost控件，FragmentTabHost 要想让中间按钮凸出需根据需求再拉控件覆盖原本中间按钮，其他按钮凸出同理

```
<?xml version="1.0" encoding="utf-8"?><!--
/* //device/apps/common/assets/res/layout/tab_content.xml
**
** Copyright 2011, The Android Open Source Project
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
**
**     http://www.apache.org/licenses/LICENSE-2.0
**
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
*/
android:clipToPadding="true"
android:fitsSystemWindows="true"
-->

<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.v4.app.FragmentTabHost
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@android:id/tabhost"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical">

            <FrameLayout
                android:id="@android:id/tabcontent"
                android:layout_width="0dp"
                android:layout_height="0dp"
                android:layout_weight="0" />

            <FrameLayout
                android:id="@+id/realtabcontent"
                android:layout_width="match_parent"
                android:layout_height="0dp"
                android:layout_weight="1" />

            <TabWidget
                android:id="@android:id/tabs"
                android:layout_width="match_parent"
                android:layout_height="45dp"                 //tab的高度
                android:layout_weight="0"
                android:background="#F5F5F5"
                android:divider="#00000000"
                android:gravity="center"
                android:orientation="horizontal" />
        </LinearLayout>

    </android.support.v4.app.FragmentTabHost>

    <ImageView
        android:id="@+id/main_image_center"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_centerInParent="true"
        android:layout_marginBottom="17dp"
        android:src="@drawable/circle" />

    <TextView
        android:id="@+id/main_tv_final"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_centerHorizontal="true"
        android:layout_centerInParent="true"
        android:layout_marginBottom="2dp"
        android:text="金融圈"
        android:textColor="@color/main_tabtextcolor"
        android:textSize="12sp" />

</RelativeLayout>
```
如果tab在顶部，只需要把xml中的TabWidget放在@android:id/tabcontent上即可

2.搞定了xml接下来就是代码部分了，先新建片段（根据自己需求确定新建几个片段）此处我每个片段区别比较大，复用性差，所以5个tab新建了5个片段
HomeFragment.class, MessageFragment.class, CenterFragment.class, FinancialFragment.class,MineFragment.class

3.接下来最重要的代码类MainActivity.java：

```
public class MainActivity extends FragmentActivity implements OnClickListener  
{  
  
    private FragmentTabHost mTabHost;  //mTabHost控件
    private Class[] clas = new Class[] { HomeFragment.class, MessageFragment.class, CenterFragment.class, FinancialFragment.class,  
            MineFragment.class };  //片段
    private int images[] = new int[] {R.drawable.tab_1_selector, R.drawable.tab_2_selector,1, R.drawable.tab_4_selector,  
            R.drawable.tab_5_selector };  //tab按钮  选中和不选中状态
    private TextView mBottom_center;  //中间按钮TextView 
    private ImageView main_image_center;  //中间按钮ImageView 
  
    @Override  
    protected void onCreate(Bundle savedInstanceState)  
    {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        initUI();  
    }  
  
    public void initUI()  
    {  
        //底部中间按钮控件  
        main_image_center = (ImageView) findViewById(R.id.main_image_center);  
           main_image_center.setImageResource(R.drawable.nav_button_finance_default);  
        //底部中间按钮TextView
        mBottom_center = (TextView) findViewById(R.id.main_tv_final);  
        //设置监听
        main_image_center.setOnClickListener(this);  
        mBottom_center.setOnClickListener(this); 
     
        String[] tabIndicatorArray = getResources().getStringArray(R.array.arr_tab_indicator);  
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);  
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);  
        LayoutInflater inflater = getLayoutInflater();  
        for (int i = 0; i < images.length; i++) {  
            View indicatorView = inflater.inflate(R.layout.g_list_item_viewpagerindicator, null);  
            TextView tvIndicator = (TextView) indicatorView.findViewById(R.id.tv_title_indicator);  
            tvIndicator.setText(tabIndicatorArray[i]);  
            ImageView imageView = (ImageView) indicatorView.findViewById(R.id.ima_indicator);  
            imageView.setImageResource(images[i]);  
            //tabhost添加tab切换事件 tab0-tab4 
            mTabHost.addTab(mTabHost.newTabSpec("tab"+i).setIndicator(indicatorView), clas[i], null);  
            mTabHost.setOnTabChangedListener(new OnTabChangeListener() {  
                  
                @Override  
                public void onTabChanged(String tabId) {  
                    switch (tabId)  
                    {  
                    case "tab2":  //获取中间按钮点击事件  设置覆盖控件
                        main_image_center.setImageResource(R.drawable.nav_button_finance_selected);  
                        mBottom_center.setTextColor(Color.parseColor("#ffd38a"));  
                        break;  
                    default:  //其他四个按钮
                        main_image_center.setImageResource(R.drawable.nav_button_finance_default);  
                        mBottom_center.setTextColor(Color.parseColor("#b2b2b2"));  
                        break;  
                    }  
  
                }  
            });  
          
        }                                             
    }  
  
    @Override  
    public boolean onCreateOptionsMenu(Menu menu)  
    {  
        getMenuInflater().inflate(R.menu.main, menu);  
        return true;  
    }  
  
    @Override  
    public boolean onOptionsItemSelected(MenuItem item)  
    {  
        int id = item.getItemId();  
        if (id == R.id.action_settings)  
        {  
            return true;  
        }  
        return super.onOptionsItemSelected(item);  
    }  
  
    @Override  
    public void onClick(View v)  
    {  
        switch (v.getId())  
        {  
        case R.id.main_image_center:  
            mTabHost.setCurrentTab(2);  
            break;  
        default:  
            break;  
        }  
    }  
}  
```

3.res文件夹下新建一个color文件夹，main_tabtextcolor.xml的代码为：

```
<?xml version="1.0" encoding="utf-8"?>  
<selector xmlns:android="http://schemas.android.com/apk/res/android">  
    <item android:state_selected="true" android:color="#ffd38a"/>  
    <item android:color="#b2b2b2"/>  
</selector>
```

4.drawable文件夹下的tab_1_selector.xml、tab_2_selector.xml、tab_3_selector.xml、tab_4_selector.xml、tab_5_selector.xml代码

```
<?xml version="1.0" encoding="utf-8"?>  
<selector  
  xmlns:android="http://schemas.android.com/apk/res/android">  
    <item android:state_selected="true" android:drawable="@drawable/nav_button_home_selected" />  
    <item android:state_pressed="true" android:drawable="@drawable/nav_button_home_selected" />  
    <item android:state_selected="false" android:drawable="@drawable/nav_button_home_default" />  
</selector>  
```

效果就这样实现了，如若有不到位之处还望不吝指点，非常感谢~
