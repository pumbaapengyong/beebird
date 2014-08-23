package am.hour.unknown.listView;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    //下载的内容是测试用的，没什么用了
    public static String[] mStrings={
    		"http://img.huxiu.com/portal/201408/09/072936yvygvymxgvglxjx1.jpg",
    		"http://pica.nipic.com/2007-07-09/20077916924121_2.jpg",
    		"http://img4.imgtn.bdimg.com/it/u=2355553967,262785485&fm=23&gp=0.jpg",
    		"http://img1.imgtn.bdimg.com/it/u=4258304963,100592550&fm=23&gp=0.jpg",
    		"http://p6.zbjimg.com/task/2011-08/10/965635/4e429be8dfea4.jpg",
    		"http://p6.zbjimg.com/task/2011-10/17/1087732/4e9bb0794d2ef.jpg",
    		"http://pic1.ooopic.com/00/91/86/10b1OOOPIC89.jpg",
    		"http://pic1.ooopic.com/uploadfilepic/shiliang/2009-08-03/OOOPIC_gaofeng504_20090803e19e4857b204d342.jpg",
    		"http://img03.b2b.hc360.com/pic-3/company-pic-14/3-14-1504314.jpg",
    		"http://img2.imgtn.bdimg.com/it/u=2445059343,3525496233&fm=23&gp=0.jpg",
    		"http://pic2.ooopic.com/01/16/18/88bOOOPIC6f.jpg",
    		"http://img20.vikecn.com/Task/2010-11/18/11337363_1912709.jpg",
    		"http://img3.imgtn.bdimg.com/it/u=1484971559,788636950&fm=23&gp=0.jpg",
    		"http://p5.zbjimg.com/task/2011-04/25/710357/4db505faa3f0c.png",
    		"http://p2010a.zbjimg.com/task/2010-05/27/314173/middlen7lgyc97.jpg",
    		"http://pic3.nipic.com/20090629/66582_223011057_2.jpg",
    		
    		"http://p2008.zbjimg.com/task/2009-07/16/112264/910widbf.jpg",
    		"http://img14.vikecn.com/Task/2010-3/12/141853635_1520644.jpg",
    		"http://www.hoteljob.cn/upPIC/FIRM/2010/07/20103231536573798.jpg",
    		"http://p2008.zbjimg.com/task/2008-08/15/57332/p6hdgmu1.jpg",
    		"http://p2010a.zbjimg.com/task/2010-05/29/315707/nvegzkzu.jpg",
    		"http://p2008.zbjimg.com/task/2009-02/23/71236/7o5uso0j.jpg",
    		"http://mbr.qqdcw.com/corp/mbr1009/MBR100906155811588237/PicNatural/IMG101113113244765784.jpg",
    		"http://mbr.qqdcw.com/corp/mbr1009/MBR100906155811588237/PicNatural/IMG101113113244765784.jpg",
    		"http://www.omyy.com/user_sub/Upload/20091311516.jpg",
    		"http://p2008.zbjimg.com/task/2008-07/10/55580/3pxhqip2.jpg",
    		"http://p5.zbjimg.com/task/2010-11/25/493720/4cee0652aa084.jpg",
    		"http://p2008.zbjimg.com/task/2009-01/05/67269/middlehhw84auk.jpg",
    		"http://img2.zhubajie.com/task/2007-10/42773/z50jp5at.jpg",
    		"http://img4.imgtn.bdimg.com/it/u=3731997611,1132267851&fm=23&gp=0.jpg",
    		"http://p4.zbjimg.com/task/2011-11/24/1265689/4ece2838d3cc2.png",
    		"http://p2008.zbjimg.com/task/2009-06/12/100655/mzez3xof.jpg",
    		"http://www.51ps.com/upfile/2009/08/2009827104734376915.jpg",
    		"http://www.omyy.com/user_sub/Upload/20113511380.jpg",
    		"http://img9.vikecn.com/Task/2009-10/18/12473568_926565.jpg"
    };
    
    public static int findURL(String url){
    	for(int i=0;i<mStrings.length;i++){
    		if(url.equals(mStrings[i])){
    			return i;
    		}
    	}
		return -1;
    }
}