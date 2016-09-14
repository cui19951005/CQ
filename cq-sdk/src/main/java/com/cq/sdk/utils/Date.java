package com.cq.sdk.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 时间扩展类
 * Created by Administrator on 2016/7/13 0013.
 */
public class Date extends java.util.Date   {
    private DateTime dateTime=new DateTime();
    private static final int[] months=new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public Date(){
        this(System.currentTimeMillis());
    }
    public Date(long time){
        this.dateTime=convert(time);
        this.setTime(time);
    }
    public Date(java.util.Date date){
        this.dateTime=convert(date.getTime());
        this.setTime(date.getTime());
    }
    public Date(DateTime dateTime){
        this(convert(dateTime));
    }
    public Date(int year,int month,int day,int hour,int minutes,int second){
        this(new DateTime(year,month,day,hour,minutes,second));
    }
    public Date(int year,int month,int day){
        this(year,month,day,0,0,0);
    }

    @Override
    public String toString() {
        return this.toString("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public void setTime(long time) {
        super.setTime(time);
        this.dateTime=convert(time);
    }

    @Override
    public Object clone() {
        return super.clone();
    }
    public String toString(String format){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format);
        return simpleDateFormat.format(this);
    }
    public DateTime getDateTime(){
        return this.dateTime;
    }
    public Date calculate(DateTime dateTime,DateCalculateType dateCalculateType){
        return calculate(this.getDateTime(),dateTime,dateCalculateType);
    }
    /*静态*/
    public static int[] getMonths(){
        return months;
    }
    public static Date toDate(String dateStr){
        String[] dateFormats=new String[]{
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd",
                "yyyy/MM/dd HH:mm:ss",
                "yyyy/MM/dd",
                "yyyy年MM月dd日 HH时mm分ss秒",
                "yyyy年MM月dd日"
        };
        for(String dateFormat: dateFormats){
            try {
                return toDate(dateStr,dateFormat);
            }catch (Exception ex){
            }
        }
        return null;
    }
    public static Date toDate(String date,String format) throws ParseException {
        return new Date(new SimpleDateFormat(format).parse(date).getTime());
    }
    public static DateTime convert(long timespan){
        DateTime dateTime=new DateTime();
        timespan=timespan/1000+8*60*60+31*24*60*60+24*60*60;
        int i=1970;
        int sum=0;
        boolean isBreak=false;
        do{
            for(int j=0;j<months.length;j++){
                if(i%4==0&&j==1) {
                    sum += (months[j]+1) * 24 * 60 * 60;
                }else{
                    sum += months[j] * 24 * 60 * 60;
                }
                if((sum+months[j+1==12?0:j+1]* 24 * 60 * 60)>timespan){
                    dateTime.setMonth(j+1);
                    isBreak=true;
                    break;
                }
            }
            if(isBreak){
                break;
            }
            i++;
        }while (true);
        dateTime.setYear(i);
        long surplus=timespan-sum;
        dateTime.setDay((int)surplus/(24*60*60));
        surplus-=dateTime.getDay()*24*60*60;
        dateTime.setHour((int)surplus/(60*60));
        surplus-=dateTime.getHour()*60*60;
        dateTime.setMinutes((int)surplus/(60));
        surplus-=dateTime.getMinutes()*60;
        dateTime.setSecond((int)surplus);
        return dateTime;
    }
    public static long convert(DateTime dateTime){
        long sum=-(8*60*60+24*60*60);//北京时间1月1日8点
        for(int i=1970;i<dateTime.getYear();i++){
            for(int j=0;j<months.length;j++){
                if(i%4==0 && j==1){
                    sum+=(months[j]+1)*24*60*60;
                }else{
                    sum+=months[j]*24*60*60;
                }
            }
        }
        for(int j=0;j<dateTime.getMonth()-1;j++){
            if(dateTime.getYear() % 4 ==0 && j==1){
                sum+=(months[j]+1)*24*60*60;
            }else {
                sum+=months[j]*24*60*60;
            }
        }
        sum+=dateTime.getDay()*24*60*60;
        sum+=dateTime.getHour()*60*60;
        sum+=dateTime.getMinutes()*60;
        sum+=dateTime.getSecond();
        sum=sum*1000;
        return sum;
    }
    public static Date calculate(DateTime date1,DateTime date2,DateCalculateType dateCalculateType){
        return new Date(DateTime.calculate(date1,date2,dateCalculateType));
    }
    public enum DateCalculateType{
        plus(1),reduce(2);
        private int type;
        DateCalculateType(int type){
            this.type=type;
        }
        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
    public static class DateTime{
        private int year;
        private int month;
        private int day;
        private int hour;
        private int minutes;
        private int second;

        public DateTime() {
        }

        public DateTime(int year) {
            this(year,0);
        }

        public DateTime(int year, int month) {
            this(year,month,0);
        }

        public DateTime(int year, int month, int day) {
            this(year,month,day,0,0,0);
        }

        public DateTime(int year, int month, int day, int hour, int minutes, int second) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minutes = minutes;
            this.second = second;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinutes() {
            return minutes;
        }

        public void setMinutes(int minutes) {
            this.minutes = minutes;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }

        public static DateTime calculate(DateTime dateTime1,DateTime dateTime2,DateCalculateType dateCalculateType){
            DateTime dateTime=new DateTime();
            switch (dateCalculateType){
                case plus:
                    dateTime.setYear(dateTime1.getYear()+dateTime2.getYear());
                    dateTime.setMonth(dateTime1.getMonth()+dateTime2.getMonth());
                    dateTime.setDay(dateTime1.getDay()+dateTime2.getDay());
                    dateTime.setHour(dateTime1.getHour()+dateTime2.getHour());
                    dateTime.setMinutes(dateTime1.getMinutes()+dateTime2.getMinutes());
                    dateTime.setSecond(dateTime1.getSecond()+dateTime2.getSecond());
                    break;
                case reduce:
                    dateTime.setYear(dateTime1.getYear()-dateTime2.getYear());
                    dateTime.setMonth(dateTime1.getMonth()-dateTime2.getMonth());
                    dateTime.setDay(dateTime1.getDay()- dateTime2.getDay());
                    dateTime.setHour(dateTime1.getHour()- dateTime2.getHour());
                    dateTime.setMinutes(dateTime1.getMinutes()- dateTime2.getMinutes());
                    dateTime.setSecond(dateTime1.getSecond()- dateTime2.getSecond());
                    break;
            }
            if(dateTime.getSecond()>59){
                dateTime.setMinutes(dateTime.getMinutes()+1);
                dateTime.setSecond(dateTime.getSecond()-60);
            }else if(dateTime.getSecond()<0){
                dateTime.setMinutes(dateTime.getMinutes()-1);
                dateTime.setSecond(dateTime.getSecond()+60);
            }
            if(dateTime.getMinutes()>59){
                dateTime.setHour(dateTime.getHour()+1);
                dateTime.setMinutes(dateTime.getMinutes()-60);
            }else if(dateTime.getMinutes()<0){
                dateTime.setHour(dateTime.getHour()-1);
                dateTime.setMinutes(dateTime.getMinutes()+60);
            }
            if(dateTime.getHour()>23){
                dateTime.setDay(dateTime.getDay()+1);
                dateTime.setHour(dateTime.getHour()+24);
            }else if(dateTime.getHour()<0){
                dateTime.setDay(dateTime.getDay()-1);
                dateTime.setHour(dateTime.getHour()-24);
            }
            int day= Date.getMonths()[dateTime.getMonth()>12?dateTime.getMonth()%12:(dateTime.getMonth()<1?12+dateTime.getMonth():dateTime.getMonth())];
            if(dateTime.getDay()>day){
                dateTime.setMonth(dateTime.getMonth()+1);
                dateTime.setDay(dateTime.getDay()-day);
            }else if(dateTime.getDay()<1){
                dateTime.setMonth(dateTime.getMonth()-1);
                day=Date.getMonths()[dateTime.getMonth()>12?dateTime.getMonth()%12:(dateTime.getMonth()<1?12+dateTime.getMonth():dateTime.getMonth())];
                dateTime.setDay(dateTime.getDay()+day);
            }
            if(dateTime.getMonth()>12){
                dateTime.setYear(dateTime.getYear()+1);
                dateTime.setMonth(dateTime.getMonth()-12);
            }else if(dateTime.getMonth()<1){
                dateTime.setYear(dateTime.getYear()-1);
                dateTime.setMonth(dateTime.getMonth()+12);
            }
            return dateTime;
        }
    }

}
