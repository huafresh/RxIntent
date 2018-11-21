readme
===
# 一、简介
项目里如果存在账户系统，一般都会有一个头像更换的功能。通常这个功能的业务逻辑是这样的：
1. 点击头像弹出一个对话框提示用户从相册或者相机中选择一张图片；
2. 启动系统应用（相机or相册）拿到图片；
3. 再启动系统应用裁剪图片；
4. 最后发请求上传这个图片。

这里面涉及多次启动系统应用，启动系统应用一般都是调用startActivityForResult，然后在onActivityResult中拿到结果，这里面的步骤还是比较繁琐的，需要定义requestCode、解析结果Intent，高版本还需要考虑运行时权限以及fileProvider文件共享等兼容问题。本人处理业务时比较喜欢用RxJava，因此，这一块能不能封装一下，使得结果返回的是一个可以被观察的Observable，其他的细节都屏蔽掉呢？

通过无UI的fragment来处理回调，然后结合RxJava封装一下，问题应该不大。细节不太好说，感兴趣可以直接看代码。这里直接介绍下如何使用。
# 二、简单使用
首先是最简单的使用（这里是打开系统相机）

```
RxIntent.openCamera(this)
            .subscribe2(new SimpleResultCallback<String>() {
                @Override
                public void onResult(@Nullable String path) {
                    //拿到的path就是图片存储的全路径
                }
            });
```
如果项目里图片选择是自定义实现的，可以添加一个Intent转换器转换一下Intent即可。


```
RxIntent.openCamera(this)
            .beforeStart(new IConverter<Intent, Intent>() {
                @Override
                public Intent convert(Intent intent) {
                    //do your convert
                    return null;
                }
            })
            .subscribe(new Consumer<Intent>() {
                @Override
                public void accept(Intent intent) throws Exception {

                }
            });
```

注意此时就不能使用subscribe2方法了，要使用subscribe，因为此时返回的结果Intent框架并不能解析。
subscribe方法回调的Intent是没有经过任何处理的。

# 三、后记
还有其他回调，比如权限被拒绝、出现异常等，就不贴代码了。  
目前RxIntent暂时只支持相机、相册、裁剪等系统应用的启动，未来还会逐步添加其他系统应用的支持。

