### 前言
> 一般的悬浮窗实现方式，需要申请权限，并还是要对部分机型进行适配才能正常显示。那么这里，我们换一种思路，实现一个不一样的悬浮窗。

### 一、应用内悬浮窗实现思路

通常的悬浮窗是通过`WindowManager`直接添加的，在不同的Android系统上需要做不同的适配，在`Android`6.0以上的机型上，还需要引导用户跳转到设置界面手动开启悬浮窗权限。虽然这样实现悬浮窗有完整的解决方案，但是开启悬浮窗过程对用户并不是很友好。下面，我们换一种思路，去使用一个应用内悬浮窗，避免机型适配和权限申请的坑，让悬浮窗像普通的`View`一样显示在界面上。

一般悬浮窗的实现方案是向系统`window`添加`type`为`TYPE_PHONE`或者`TYPE_TOAST`的`View`，从而使悬浮窗可以作为一个独立的`View`进行展示。`Android`对这一行为作了限制，那我们可以考虑从比较常规的途径添加`View`：向每一个展示界面，即`Activity`，添加一个`View`作为悬浮窗。这样，我们使用悬浮窗时就可以避免适配和权限问题。那么，怎么样实现这样的悬浮窗更好呢？

要实现这样一个悬浮窗，相当于我们要在`Activity`加载完后将悬浮窗的`View`添加的`Activity`上，我们不想在原有的Activity上插入这段代码，这时就可以利用`ActivityLifecycleCallbacks`和`fragment`的加载特性来完成一个无侵入式的悬浮窗的显示。

![demo](https://github.com/windinwork/floatingwindowdemo/blob/master/art/gif_floating_window_demo.gif)

### 二、应用内悬浮窗的实现

1. 首先，我们先自定义一个`View`用于显示悬浮窗界面，就叫它`FloatingWindow`。至于怎么实现，这个各位可以自由发挥。
2. 接下来，我们要把`FloatingWindow`添加到每一个`Activity`上，这时就利用`ActivityLifecycleCallbacks`。`Activity`的每个生命周期都能回调到`ActivityLifecycleCallbacks`，这时我们只要在`onActivityCreated(Activity activity, Bundle savedInstanceState)`中加上悬浮窗`View`。但是，`onActivityCreated(Activity activity, Bundle savedInstanceState)`方法是在`onCreate(Bundle savedInstanceState)`时被调用的，我们需要保证在`setContentView()`之后才添加悬浮窗，让悬浮窗处于上层，所以我们插入一个空Fragment，利用`Fragment`的`onActivityCreated(Bundle savedInstanceState)`是在`Activity`的`onCreate(Bundle savedInstanceState)`之后的特性来加入悬浮窗。
```
registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        super.onActivityCreated(activity, savedInstanceState);

        if (activity instanceof FragmentActivity) {
            FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
            fm.beginTransaction().add(new SupportFragment(), FRAGMENT_TAG).commitAllowingStateLoss();
        }
    }
    ...
});

public static class SupportFragment extends Fragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        if (activity != null) {
            FloatingWindow fw = new FloatingWindow(activity);
            activity.addContentView(fw, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }
}
```
通过这几行核心代码，我们便完成了一个不需要权限申请的悬浮窗。细节一点的实现可以参考我的demo：https://github.com/windinwork/floatingwindowdemo

### 三、总结
像我们这样的悬浮窗，有优点也有缺点。优点显而易见，它不需要向系统申请特殊的权限即可正常显示；缺点的话即是每一个`Activity`都有一个悬浮窗，相互独立存在，当然这个是可以优化一下实现方式解决的，这里不细讲，另一个缺点即是这样的悬浮窗无法在应用退到后台的时候存在，当然在在合适的应用场景这也不是问题。以上便是一个无侵入式无权限的悬浮窗实现方式，希望能为小伙伴提供不同的悬浮窗实现思路。