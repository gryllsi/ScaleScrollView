# Demo
![image](/screenshot/screenshot1.gif)
# Usage
## Step 1
Add dependencies in build.gradle:
```
 dependencies {
       compile 'compile 'com.totcy.scale:scalelibrary:1.0.0''
    }
```
Or Maven:
```
<dependency>
  <groupId>com.totcy.scale</groupId>
  <artifactId>scalelibrary</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
## Step2
Add Layout:
```
    <com.totcy.salelibrary.HorizontalScaleScrollView
        android:id="@+id/hsScale"
        xmlns:scale="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_below="@id/tv_scale"
        scale:MaxScale="200"
        scale:MinScale="20"
        scale:ScaleTextSize="14sp"
        />
```
## Step 3
activity:
```
    {
        //整型数据测试
        hsScale.setScrollListener(this);
        //hsScale.setMinAndMaxScale(10,500);

        //浮点型测试
        hsScale2.setScrollListener(this);
        //hsScale2.setIsIntegarl(false);
       // hsScale2.setMinAndMaxScale(1.1f,33.3f);

        //上下组合测试
        mulScale.setScrollListener(this);
        mulScale.setTopMinAndMaxScale(30,299);
        mulScale.setBottomMinAndMaxScale(30,299);
    }

    @Override
    public void onScaleScroll(float scale) {
        tv_scale2.setText("进度:" + scale);
    }

    @Override
    public void onScaleScroll(int scale) {
        tv_scale.setText("进度:" + scale);
    }

    @Override
    public void onScaleScrollBottom(int scale) {
        tv_scale4.setText("下面进度:" + scale);
    }

    @Override
    public void onScaleScrollTop(int scale) {
        tv_scale3.setText("上面进度:" + scale);
    }
```
# attr
attr     | describe
-------- | ---
0 | 0
