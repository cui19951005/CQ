package com.cq.sdk.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 时间扩展类
 * Created by Administrator on 2016/7/13 0013.
 */
public final class Time extends java.util.Date{
    private long time;
    private static final int[] MONTHS =new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final long BASE_DATE= Time.convert(new Time.DateTime(1970,1,1,8,0,0));
    public Time(){
        this(System.currentTimeMillis()+ Time.BASE_DATE);
    }
    public Time(long time){
        this.setTime(time);
    }
    public Time(java.util.Date date){
        this(date.getTime()+ Time.BASE_DATE);
    }
    public Time(DateTime dateTime){
        this(convert(dateTime));
    }
    public Time(int year, int month, int day, int hour, int minutes, int second){
        this(new DateTime(year,month,day,hour,minutes,second));
    }
    public Time(int year, int month, int day){
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

    public void setTime(long time) {
        this.time=time;
        super.setTime(time- Time.BASE_DATE);
    }

    @Override
    public long getTime() {
        return this.time;
    }

    @Override
    public Object clone() {
        return new Time(Time.convert(this.getDateTime()));
    }
    public String toString(String format){
        return this.toString(format, Locale.getDefault());
    }
    public String toString(String format,Locale locale){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat(format,locale);
        return simpleDateFormat.format(new java.util.Date(this.getTime()-this.BASE_DATE));
    }
    public DateTime getDateTime(){
        return Time.convert(this.time);
    }
    public Time add(Time date){
        return Time.calculate(this.getDateTime(),date.getDateTime(),DateCalculateType.plus);
    }
    public Time minus(Time date){
        return Time.calculate(this.getDateTime(),date.getDateTime(),DateCalculateType.reduce);
    }
    /*静态*/
    public static int[] getMonths(){
        return MONTHS;
    }
    public static Time toDate(String dateStr){
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
            }catch (Exception ex){}
        }
        return null;
    }
    public static Time toDate(String date, String format) throws ParseException {
        return new Time(new SimpleDateFormat(format).parse(date).getTime()+ Time.BASE_DATE);
    }
    public static DateTime convert(long timeSpan){
        DateTime dateTime=new DateTime();
        long temp=timeSpan/1000+8*60*60;
        dateTime.setMillisecond((int) (timeSpan-temp*1000));
        timeSpan=temp;
        int i=0;
        long sum=0;
        boolean isBreak=false;
        do{
            for(int j = 0; j< MONTHS.length; j++){
                if(((i%4==0&&dateTime.getYear()%100!=0) || i%400==0)&&j==1) {
                    sum += (MONTHS[j]+1) * 24 * 60 * 60;
                }else{
                    sum += MONTHS[j] * 24 * 60 * 60;
                }
                if((sum+ MONTHS[j+1==12?0:j+1]* 24 * 60 * 60)>=timeSpan){
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
        long surplus=timeSpan-sum;
        dateTime.setDay((int)surplus/(24*60*60));
        surplus-=dateTime.getDay()*24*60*60;
        dateTime.setHour((int)surplus/(60*60));
        surplus-=dateTime.getHour()*60*60;
        dateTime.setMinutes((int)surplus/(60));
        surplus-=dateTime.getMinutes()*60;
        dateTime.setSecond((int)surplus);
        dateTime.setMonth(dateTime.getMonth()+1);
        dateTime.setDay(dateTime.getDay()+1);
        Time.setMonth(dateTime);
        Time.setDay(dateTime);
        return dateTime;
    }
    public static long convert(DateTime dateTime){
        long sum=-(8*60*60);//北京时间1月1日8点
        for(int i=0;i<dateTime.getYear();i++){
            for(int j = 0; j< Time.MONTHS.length; j++){
                if(((i%4==0&&i%100!=0) || i%400==0)&& j==1){
                    sum+=(Time.MONTHS[j]+1)*24*60*60;
                }else{
                    sum+= Time.MONTHS[j]*24*60*60;
                }
            }
        }
        for(int j=0;j<dateTime.getMonth()-1;j++){
            if(((dateTime.getYear()%4==0&&dateTime.getYear()%100!=0) || (dateTime.getYear()%400==0))&& j==1){
                sum+=(Time.MONTHS[j]+1)*24*60*60;
            }else {
                sum+= Time.MONTHS[j]*24*60*60;
            }
        }
        sum+=(dateTime.getDay()-1)*24*60*60;
        sum+=dateTime.getHour()*60*60;
        sum+=dateTime.getMinutes()*60;
        sum+=dateTime.getSecond();
        sum*=1000;
        sum+=dateTime.getMillisecond();
        return sum;
    }
    public static Time calculate(DateTime date1, DateTime date2, DateCalculateType dateCalculateType){
        return new Time(DateTime.calculate(date1,date2,dateCalculateType));
    }
    private static void setMonth(DateTime dateTime){
        if(dateTime.getMonth()<1){
            dateTime.setYear(dateTime.getYear()-1);
            dateTime.setMonth(Time.MONTHS.length+dateTime.getMonth());
            dateTime.setDay(Time.MONTHS[dateTime.getMonth()-1]+dateTime.getDay());
            setDay(dateTime);
        }else if(dateTime.getMonth()>12){
            dateTime.setYear(dateTime.getYear()+1);
            dateTime.setMonth(dateTime.getMonth()- Time.MONTHS.length);
        }
    }
    private static void setDay(DateTime dateTime){
        int day= Time.getMonths()[(dateTime.getMonth()<1? Time.MONTHS.length+dateTime.getMonth():(dateTime.getMonth()>12?dateTime.getMonth()- Time.MONTHS.length:dateTime.getMonth()))-1];
        if(dateTime.getDay()>day){
            dateTime.setMonth(dateTime.getMonth()+1);
            dateTime.setDay(dateTime.getDay()-day);
        }else if(dateTime.getDay()<1){
            dateTime.setMonth(dateTime.getMonth()-1);
            dateTime.setDay(dateTime.getDay()+day);
        }
        setMonth(dateTime);
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
        private int millisecond;
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
            setMonth(dateTime);
            setDay(dateTime);
            return dateTime;
        }

        public int getMillisecond() {
            return millisecond;
        }

        public void setMillisecond(int millisecond) {
            this.millisecond = millisecond;
        }

        @Override
        public String toString() {
            return this.year+"-"+this.month+"-"+this.day+" "+this.hour+":"+this.minutes+":"+this.second;
        }
    }

}
