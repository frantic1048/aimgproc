package ssbit.glwzz.aimgproc;


import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;


public class Identifier implements Runnable {
    private Record[] records;
    private SQLMan mySQLMan;
    private ArrayList<OnePic> pics;
    private ArrayList<OnePic> highPic = new ArrayList<>();
    private ArrayList<OnePic> lowPic = new ArrayList<>();
    private Handler identifyDoneHandler;
    private boolean hasDiff;

    public Identifier(Context context, ArrayList<OnePic> pics, Handler identifyDoneHandler) {
        mySQLMan = new SQLMan(context);
        this.records = mySQLMan.readAllRecord();
        this.pics = pics;
        this.identifyDoneHandler = identifyDoneHandler;
    }

    private boolean haveRecord(OnePic pic) {
        for (Record record : records) {
            if (record.match(pic)) {
                pic.setHighQuality(record.getQuality());
                pic.setIdInDb(record.getId());
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        for (OnePic pic : pics) {
            if (!pic.isIdentified()) {
                if (!haveRecord(pic)) {
                    pic.setHighQuality(Algorithm.getQuality(pic));
                    pic.setIdInDb(mySQLMan.createRecord(
                            new Record(pic)));
                    //TODO 调用对应的算法,将没一个的判断结果存放到数据库中
                }
                pic.makeIdentified();
            }
        }
//        try {
//            Thread.sleep(8000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        identifyDoneHandler.sendEmptyMessage(Msg.IdentifyDone);
    }

    public void diff() {
        for (OnePic pic : pics) {
            if (pic.getHighQuality()) {
                highPic.add(pic);
            } else {
                lowPic.add(pic);
            }
        }
        this.hasDiff = true;
    }
    public boolean diffed(){
        return this.hasDiff;
    }

    public ArrayList<OnePic> getPics() {
        return pics;
    }

    public ArrayList<OnePic> getHighPic() {

        return highPic;
    }

    public ArrayList<OnePic> getLowPic() {
        return lowPic;
    }

    public boolean deal(OnePic pic) {
        for (OnePic aPic : pics) {
            if (aPic.equals(pic)) {

                //equal是不是可以通过自带的那个在数据库中的id来实现？
                System.out.println("###找到对应图片");
                if (aPic.getHighQuality() != pic.getHighQuality()) {
                    System.out.println("###发现图片质量不一样");
                    //如果质量属性被修改的话
                    if (hasDiff) {
                        System.out.println("###图片已经被diff");
                        if (aPic.getHighQuality()) {
                            //原来是高质量，移动到低质量
                            System.out.println("###原来是高质量");
                            highPic.remove(aPic);
                            lowPic.add(aPic);
                        } else {
                            System.out.println("###原来是低质量");
                            lowPic.remove(aPic);
                            highPic.add(aPic);
                        }
                    }
                    //不管是不是已经被判断过都设定为判断过
                    aPic.makeIdentified();
                    aPic.setHighQuality(pic.getHighQuality());
                    System.out.println("###对应修改id" + aPic.getIdInDb());
                    mySQLMan.updateOneRecord(
                            new Record(aPic.getIdInDb(), aPic));
                    return true;
                    //return true 表示确实发生了更改
                }

                break;
            }
        }
        return false;
    }
}
