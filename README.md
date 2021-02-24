# 0-我做的是一个网抑云APP。
主要的框架是准备使用mvvp架构。（对于架构方面的知识，也是第一次正式尝试，不是很清楚） 但是前期准备不充足，报错太多，只是使用了mvvp中的一部分内容DataBinding，。
MutableLiveData这些只有定义并未使用，也未添加观察者，数据互相绑定这些内容。DataBinding的报错内容奇奇怪怪，解决之后
也没再次使用其它内容了，加上前期对app的全局观不够全面。后期的各种数据处理成为难题。

1.网抑云登陆设计
登录只有一个活动，并且一个帧布局，在这一个登录活动界面切换各个碎片。自己包装了一个工具类便于碎片在活动的帧布局中切换。自己感觉相当于Navigation的功能，但我直接使用它的时候也是报错奇奇怪怪，
经验不足，未敢使用。

主要实现的是登陆，注册，修改密码，本地保存账号信息一键登录，还有一个好看的水波纹特效！接口有登陆，注册（修改密码），
发送验证码，验证验证码，验证手机号是否已经注册，退出登录共六个接口。并且实现本地保存，用户再次点击app时可一键登录，
跳过登录界面，进入主界面。（设计方法就不说了，比较简单) 
下来是登陆界面的水波纹效果，一个自定义view只实现了两个参数的构造方法（只在xml中使用嘛）。继承自RelativeLayout（因为可以与其它view覆盖,在其上面显示）
在view里面定义一个wave类  有坐标，画笔，半径属性。定义一个wave类的集合，利用画笔依次在需要的地方绘制。在handler里面发消息不断循环使用，每一次进行数据的
刷新（改变wave的半径，透明度），然后再次invalidate();就会重新绘制，每一次添加wave的时候，随机给画笔赋上颜色。当半径够大，透明度为负数的时候，就将它移除集合，
这样就实现了水波纹的动感效果，音乐的震动和快感。将它放在了登录网抑云的log上面。主要时运用了自定义view的 onDraw方法，画笔还有动画的知识。

2.主要歌曲信息界面，主界面一。
三个板块也是由三个碎片来做的。（一个vp，一个rv，一个播放器）
使用了推荐歌单的接口，用户自己歌单的接口。歌曲详细信息的的接口，歌单详情的接口。将接口请求回来的数据应用到rv和vp两个view上。主要用于展示信息。使用androidx.drawerlayout.widget.DrawerLayout和
com.google.android.material.navigation.NavigationView设置点击事件实现抽屉栏的效果。最上面有一个搜索框，前期是准备自定义一个搜索框的，但是后来没有时间了，搜索框成为了装饰品。
最下面有一个音乐播放器的相关按钮和ui。可以实时展示当前播放的歌曲信息，上面的信息与服务所播放的歌曲一一对应。切歌，暂停等按钮。还设有当前歌曲播放队列的信息按钮（整体是一个活动，那中上拉覆盖的view，
没有时间去做了）显示播放队列的这个活动相当于附属界面的感觉了。界面里面的所有rv和vp均可点击。各个相应的界面都会响应并展示一 一对应的信息。


3.主界面二，播放界面。使用了歌词的url接口，还有获得歌词的接口。喜欢音乐的的接口
是最后一个主界面也就是歌曲播放的界面 整体活动由帧布局拼凑而成。中间是展示界面，下方是各种功能。 实现了 切歌 暂停 循环播放 随机播放等功能。中间是一个自定义的帧布局，涉及事件的分发，点击可以
进行光碟界面和歌词界面的互换。而滑动可以滑动歌词（关于点击和滑动的事件分发具体方法，此处主要是由向哲哥请教）光碟可以随着音乐的播放而转动，停止而停止。即使界面重新加载，光碟的状态始终和是否
正在播放音乐对应。在这个界面的下面有一个SeekBar，它实现了歌曲的时间与进度一一对应。并且将时间变化，歌曲进度变化与最终时间都展示在下面。可以拖动它来实时调整歌曲的播放进度。而中间的歌词界面，
当前正在播放的歌词会高亮显示，正在播放的那句歌词在不滑动的情况下会一直显示在中间，并且变色，变大。如果滑动不会显示在中间，但是会变大，变色。本来是准备再写一个歌词离当前播放歌词距离越远，画笔
的透明度越高，那种淡入浅出的效果。（但是今天写了一个歌词展示的算法，头发用光了，就没有实现这个效果）
大体app的介绍就已经介绍完了。

关于app各种难题的思路：
 1.接口回调 可以降低代码的耦合性，并且传递相关的参数和数据。
 （但是好像没办法在服务当中使用接口回调传递数据）（因为我拿不到已经启动了的那个服务的实例） 我不记得当时有没有试过静态属性来实现这个接口回调
 Binder可以进行服务于与启动类的通信  但是没办法实现实时通信。
 后来的服务与歌词view 播放器ui的通信使用的是广播  （使用的是本地广播）
 用一个任务器不断实时的向外发送当前歌曲的播放信息，进度等。其它相关类实时监听 并作出相应的改变。
 2.歌词的jason数据解析    
 使用字符串的相关方法。
 统一时间的单位。
 最后将歌词一句一句的解析在集合中并解析一个一一对应的时间集合，利用当前播放器不断向外发送当前时间进度，就在歌词，进度条作出相应的改变。
 3.自定义view没办法很好的显示歌词
 当某一句歌词太长时就会超出界面，显示不全。
 所以我设计了一个算法，当一句歌词太长了，就会被截取下来。分段显示一起高亮。（这个算法较为复杂，好家伙我头皮发麻）（因为又要当前播放的歌词显示在中间，这里就产生了一个相对位置）高亮歌词的上边
 和下边的绘制是不一样的（因为高亮的那一句也可能被分成几段），此处就不在赘述，直接贴算法代码
 private int drwaLirc(String data,Canvas canvas){    // 分割高亮显示长句歌词
           NUMBER++;
        Log.i("流弊","高亮多行多行"+NUMBER);
        if(data.length()>=30){
            canvas.drawText(data.substring(0,30),getWidth()/2,getHeight()/2+NUMBER*100,mTextPaint);
            return drwaLirc(data.substring(30,data.length()),canvas);
        }else {
            canvas.drawText(data.substring(0,data.length()),getWidth()/2,getHeight()/2+NUMBER*100,mTextPaint);
                return NUMBER;
        }
    }

//    private int drwaComLircup(String data,Canvas canvas,int i){
//        NUMBER++;
//        String[] temp=null;
//        while (data.length()>30){
//            temp[NUMBER] = data.substring(0,30);
//            data=data.substring(30,data.length());
//            NUMBER++;
//        }
//        for(int j=0;j<temp.length;j++){
//
//        }
//    }
    private int drwaComLirc(String data,Canvas canvas,int i){   // 分割平民显示长句歌词
        NUMBER++;
        Log.i("流弊","普通多行"+NUMBER);
        if(data.length()>=30){
            canvas.drawText(data.substring(0,30),getWidth()/2,(getHeight()/2)+(i-currentlyric)*100,paint);
            i++;
            return drwaComLirc(data.substring(30,data.length()),canvas,i);
        }else {
            canvas.drawText(data.substring(0,data.length()),getWidth()/2,(getHeight()/2)+(i-currentlyric)*100,paint);
            return NUMBER;

        }


    }

    private void Drawlyrics(Canvas canvas){   //注册太多次广播接收器会造成卡顿且闪退 此处就是Ondraw方法
        rigsterbrodcaster();
            int postinup = 0;
            int postindowu=0;
            int postion=0;
            int uplirc =0;;
       // Log.i(TAG,"广播接收器已经注册"+"歌词的大小"+lyrics.lyric_line.size());
        for (int i=0;i<lyrics.lyric_line.size();i++){
            if(i==currentlyric){    //高亮显示歌词画法
                if(lyrics.lyric_line.get(i).length()>30){
                 postion=drwaLirc(lyrics.lyric_line.get(i),canvas);
                    Log.i("流弊","高亮postion位置"+postion);
                    NUMBER=-1;
                }else {
                    canvas.drawText(lyrics.lyric_line.get(i),getWidth()/2,getHeight()/2,mTextPaint);

                }
            }else {           //平民歌词画法
                if(i-currentlyric<0){    //相对位置以上采用倒叙画法
                    uplirc = Math.abs(i-currentlyric);
                    if(Math.abs(i-postinup)>0){
                        postinup= drwaComLircup(lyrics.lyric_line.get(uplirc),canvas,i+postinup)+postinup;
                        NUMBER=-1;
                    }

//                    if(lyrics.lyric_line.get(i).length()>30){
//                        drwaComLirc(lyrics.lyric_line.get(i),canvas,i-postinup);
//                        NUMBER=-1;
//                    }else {
//                        canvas.drawText(lyrics.lyric_line.get(i),getWidth()/2,(getHeight()/2)+(i-currentlyric)*100,paint);
//                    }
                }else{
                        if(lyrics.lyric_line.get(i).length()>30){
                          postindowu= drwaComLirc(lyrics.lyric_line.get(i),canvas,i+postindowu+postion)+postindowu;
                            NUMBER=-1;
                        }else {
                            canvas.drawText(lyrics.lyric_line.get(i),getWidth()/2,(getHeight()/2)+(i-currentlyric+postindowu+postion)*100,paint);
                        }
                }


            }
                Log.i("流弊","一个循环加一次"+i);
            // Log.i(TAG,lyrics.lyric_line.get(i)+"有无歌词");
        }

    }

    private int drwaComLircup(String data, Canvas canvas, int i) {
        List<String> temp = new ArrayList<>();
        if(data.length()>30) {
            Log.i("最后的黄昏","上部普通多行测试");
            while (data.length() > 30) {
                temp.add(data.substring(0, 30));
                data = data.substring(30, data.length());
                NUMBER++;
            }
            temp.add(data.substring(0,data.length()));
            NUMBER++;
            for (int j = temp.size() - 1; j >= 0; j--) {
                canvas.drawText(temp.get(j), getWidth() / 2, (getHeight() / 2) + (-i ) * 100, paint);
                i++;
            }
            temp.clear();
        }else {
            canvas.drawText(data, getWidth() / 2, (getHeight() / 2) + (-i ) * 100, paint);
            NUMBER++;
        }
        return NUMBER;
    }

4.服务里边播放歌曲
MediaPlayer这个类底层好像是用C++写的
使用起来有很多不明白的报错信息，看了很多博客和日志才解决。
5.封装了网络请求的工具类，还有碎片转换的工具类，实现沉浸式的工具类。还有一个静态保存url类（各种接口）
各个类之间的数据交流最后有的使用的是静态属性来交流的。


心得体会：
就是再写一个app的时候 先不要急着动手。要先构思它的架构。比如各个界面的设计以及各个界面的关系，数据交流（比如可以建一个能够被各个类共享的数据类，避免繁多类之间的数据直接交流，性能也会提升）
各种设计模式的使用（我只是知道单例模式和工厂模式）比如单列模式就可以避免拿到是不同的对象。使某些对象唯一性，避免拿到的不是同一个对象。（这个很烦的，很多时候就是没有拿到同一个对象，最后没办法硬
生生的拿出了静态属性去解决问题）
自定义view里边很多东西还不清楚，后边必须要多学习。要多打日志，非常好的查错方式。一个好的app一个人太不容易写出来了，这里我觉得合作变得很重要。还有要多些代码，如果一段时间不去写，就会变得很生疏。红岩的学长们都是很好的学长，耐心的为我们解决了问题。
还有就是要多掉头发才可以变得更强。一学期的红岩就让我有了如此大的变化，非常喜欢和大佬们在一起的日子，自己收获的成长也很多。真的很感谢我们红岩的学长们。这头发掉的不亏的哦！


待提升的地方：
整个项目比较耗费资源，网络请求的数据没有实现本地缓存。 自定义view的绘制速度耗费资源。服务与歌词view的数据实时交流，使用的是广播，我觉得应该还有更好的方法。而且容易造成内存泄漏。
项目中有线程安全的问题，但是我并没有解决。（因为被try catch掉了）（没有影响到app的正常运行）但是这种错误还是有概率发生的。还有歌词界面的算法肯定可以优化。
没有屏蔽频繁的网络请求，如果狂点切歌app会卡。非常期待下学期红岩的课程。希望我我可以是最后能留下来的人。
![1](https://github.com/ZXM250250/0-/blob/master/1613900997192.gif)
![2](https://github.com/ZXM250250/0-/blob/master/1613901029437.gif)
![3](https://github.com/ZXM250250/0-/blob/master/1613901200014.gif)
![4](https://github.com/ZXM250250/0-/blob/master/1613901316060.gif)
![5](https://github.com/ZXM250250/0-/blob/master/1613901432195.gif)
![6](https://github.com/ZXM250250/0-/blob/master/1613901965397.gif)
![7](https://github.com/ZXM250250/0-/blob/master/1613902004372.gif)
![8](https://github.com/ZXM250250/0-/blob/master/1613902026244.gif)
![9](https://github.com/ZXM250250/0-/blob/master/1613902088987.gif)
![10](https://github.com/ZXM250250/0-/blob/master/1613902373356.gif)
