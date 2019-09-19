package com.yans.source.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

public class Function {

	public static Log log = LogFactory.getLog("dataLog");
	
	private static Gson json = new Gson();
	/**
	 * Date转换成String 格式yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String DateFomartString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(date);
		return dateStr;
	}
	
	/**
	 * String转换成Date 格式yyyy-MM-dd
	 * @param dateStr
	 * @return
	 */
	public static Date StringToDate(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			log.error("String parse Date error:",e);
		}
		return date;
	}
	
	/**
	 * String yyyyMMdd 转换成String 格式yyyy-MM-dd
	 * @param dateStr
	 * @return
	 */
	public static String StringToString(String dateStr){
		String result = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = sdf.parse(dateStr);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			result = sdf1.format(date);
		} catch (ParseException e) {
			log.error("String parse Date error:",e);
		}
		return result;
	}
	
	/**
	 * String yyyyMM 转换成String 格式yyyy-MM
	 * @param dateStr
	 * @return
	 */
	public static String StringToStringMonth(String dateStr){
		String result = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			Date date = sdf.parse(dateStr);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
			result = sdf1.format(date);
		} catch (ParseException e) {
			log.error("String parse Date error:",e);
		}
		return result;
	}
	
	
	/**
	 * Date转换成String 格式yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String yesterdayString(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(new Date(cal.getTimeInMillis()));
		return dateStr;
	}
	
	public static String yesterdayStringFromDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = "";
		try{
			
			Date now = new Date();
			now = sdf.parse(date);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			cal.add(Calendar.DATE, -1);
			
			dateStr = sdf.format(new Date(cal.getTimeInMillis()));
		}catch (Exception e) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			
		    dateStr = sdf.format(new Date(cal.getTimeInMillis()));
		}
		return dateStr;
	}
	
	
	public static String todayString(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(new Date(cal.getTimeInMillis()));
		return dateStr;
	}
	
	public static String todayStringFromDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = "";
		try{
			Date now = sdf.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			cal.add(Calendar.DATE,0);
			dateStr = sdf.format(new Date(cal.getTimeInMillis()));
		}catch (Exception e) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE,0);
			dateStr = sdf.format(new Date(cal.getTimeInMillis()));
		}
		return dateStr;
	}
	
	
	
	public static String todayStringFromNow(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		String now = sdf.format(new Date());
		return now;
	}
	
	/**
	 * 最近8天的日期
	 * @return
	 */
	public static String beforeDateString(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -8);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(new Date(cal.getTimeInMillis()));
		return dateStr;
	}
	
	/**
	 * 最近12月的日期
	 * @return
	 */
	public static String beforeOneYearString(){
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.MONTH, -12);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String dateStr = sdf.format(new Date(cal.getTimeInMillis()));
	    return dateStr;
	}
	
	/**
	 * 最近2月的日期
	 * @return
	 */
	public static String beforeTwoMonthString(){
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.MONTH, -2);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String dateStr = sdf.format(new Date(cal.getTimeInMillis()));
	    return dateStr;
	}
	
	/**
	 * Date转换成String 格式yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String beforeMonthString(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String dateStr = sdf.format(new Date(cal.getTimeInMillis()));
		return dateStr;
	}
	
	/**
	 * 该方法只计算天
	 * + 当前时间之后几天
	 * - 当前时间前几天
	 * @return
	 */
	public static String dateOperation(int i){
		Date d=new Date();
		return DateFomartString(new Date(d.getTime() - i * 24 * 60 * 60 * 1000));
	}
	
	public static String dateOperation(Date date){
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.setTime(date);
		cal.add(Calendar.MONDAY, -1);
		return DateFomartString(cal.getTime());
	}
	
	public static List<String> process(String date1, String date2) {
		List<String> list = new ArrayList<String>();
		String dateFormat = "yyyy-MM-dd";
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		String tmp = format.format(strToDate(format,date1).getTime());
		while (tmp.compareTo(date2) <= 0) {
			list.add(tmp);
			tmp = format.format(strToDate(format,tmp).getTime() + 3600 * 24 * 1000);
		}
		
		return list;
	}
	
	private static Date strToDate(SimpleDateFormat format,String str) {
		if (str == null)
			return null;
		try {
			return format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 根据指定日期计算所在周的周一和周日
	 *注意：返回的日期用#拼接
	 * @param args
	 */
	public static String dayInWeek(String date){
        Calendar cal = Calendar.getInstance();  
        Date time=StringToDate(date);
        cal.setTime(time);  
        
        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了  
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
        if(1 == dayWeek) {  
           cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
        
       cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
       
       int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天  
       cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值   
       String One=DateFomartString(cal.getTime());
       cal.add(Calendar.DATE, 6);
       String SIX = DateFomartString(cal.getTime());
       return One+"#"+SIX;
	}
	
	/**
	 * 根据指定日期计算所在月的起始日期和结束日期
	 *注意：返回的日期用#拼接
	 * @param args
	 */
	public static String dayInMath(String date){
		 Calendar cal = Calendar.getInstance();  
		 String[] ds = date.split("-");
		 cal.set(Calendar.YEAR, Integer.parseInt(ds[0]));
		 cal.set(Calendar.MONTH, Integer.parseInt(ds[1]));
		 cal.set(Calendar.DAY_OF_MONTH, 1);
	//	 System.out.println(cal.getTime());
		 cal.add(Calendar.DAY_OF_MONTH, -1);//得到这个月的最后一天
		 String endMonth = DateFomartString(cal.getTime());
		 cal.set(Calendar.DAY_OF_MONTH, 1);
		 String startMonth = DateFomartString(cal.getTime());
		 return startMonth+"#"+endMonth;
	}
	
	/**
	 * 根据指定日期计算所在年的起始日期和结束日期(lijinhu)
	 * @param date
	 * @return
	 */
	public static String dayInYear(String date){
		 Calendar cal = Calendar.getInstance();  
		 String[] ds = date.split("-");
		 cal.set(Calendar.YEAR, Integer.parseInt(ds[0]));
		 cal.set(Calendar.DAY_OF_YEAR, 1);
		 String startYear = DateFomartString(cal.getTime());
		 
		 cal.roll(Calendar.DAY_OF_YEAR, -1);
		  String endYear = DateFomartString(cal.getTime());
//		  String yesterdayString = Function.yesterdayString();
		/* if(endYear.compareTo(yesterdayString)>0){
			 endYear = yesterdayString;
		 }*/
		 return startYear+"#"+endYear;
	}
	
	
	/**
	 * 计算日期是一年中的第几周
	 * @param args
	 * @throws Exception
	 */
	public static int weekInYear(String date){
		  Date toDate = StringToDate(date);
		  Calendar calendar = Calendar.getInstance();
		  calendar.setFirstDayOfWeek(Calendar.MONDAY);
		  calendar.setMinimalDaysInFirstWeek(7);
		  calendar.setTime(toDate);
		  int i = calendar.get(Calendar.WEEK_OF_YEAR);
		  return i;
	}
	
	/**
	 * 传入“yyyy-MM-dd”格式的日期字符串，得出是星期几
	 * @param dateStr
	 * @return
	 */
	public static String transfStrToDateTwo(String dateStr) {  
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	    SimpleDateFormat format = new SimpleDateFormat("E");  
	    Date date = null;  
	    try {  
	        date = sdf.parse(dateStr);  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	     String time = format.format(date);
	    return time;  
	}  
	
	
	public static ArrayList<String> intAarray(String a,String b){
		ArrayList<String> result = new ArrayList<String>();
		if((a.substring(0,4)).equals(b.substring(0,4))){
			int weekInYears = weekInYear(a);
			int weekInYeare = weekInYear(b);
			if(weekInYears>weekInYeare){
				weekInYears=0;
			}
			if(weekInYears==weekInYeare){
				result.add(a+"-"+a.substring(0,4));
				return result;
			}
			for(;weekInYears<=weekInYeare;weekInYears++){
				result.add(weekInYears+"-"+b.substring(0,4));
			}
		}else{
			int weekInYears = Function.weekInYear(a);
			int maxyear=getMaxWeekNumOfYear(Integer.parseInt(a.substring(0,4)));
			if((a.substring(5,7)).equals("01")&&weekInYears>50){
				weekInYears=0;
			}
			if(weekInYears==maxyear){
				result.add(weekInYears+"-"+a.substring(0,4));
			}
			for(;weekInYears<=maxyear;weekInYears++){
				result.add(weekInYears+"-"+a.substring(0,4));
			}
			
			int weekInYeare = weekInYear(b);
			if((b.substring(5,7)).equals("01")&&weekInYeare>50){
				weekInYeare=0;
			}
			for(int i=0;i<=weekInYeare;i++){
				result.add(i+"-"+b.substring(0,4));
			}
		}
		return result;
	}
	
	public static ArrayList<String> getMonthBetween(String minDate, String maxDate){
		ArrayList<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月
		Calendar min = Calendar.getInstance();
		Calendar max = Calendar.getInstance();
		try {
			min.setTime(sdf.parse(minDate));
			min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);
			max.setTime(sdf.parse(maxDate));
			max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar curr = min;
		while (curr.before(max)) {
		 result.add(sdf.format(curr.getTime()));
		 curr.add(Calendar.MONTH, 1);
		}
		return result;
	}
	
	public static String getLastMonth(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		 Date date=null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	        Calendar c = Calendar.getInstance();  
	        c.setTime(date);
	        String format = sdf.format(getDateOfLastMonth(c).getTime());
	        return format+"-01";
	}
	
	public static Calendar getDateOfLastMonth(Calendar date) {  
	    Calendar lastDate = (Calendar) date.clone();  
	    lastDate.add(Calendar.MONTH, -1);  
	    return lastDate;  
	}  
	
	
	//月份的加减
	public static String decrDate(Date date,int num){
		Calendar last=Calendar.getInstance();
		last.setTime(new Date());
		last.add(Calendar.MONTH, num);
		Date time = last.getTime();
		return Function.DateFomartString(time);
	}
	
	 // 获取某年的第几周的开始日期
    public static String getFirstDayOfWeek(int year, int week) {
        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, 1);
 
        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, week * 7);
 
        return DateFomartString(getFirstDayOfWeek(cal.getTime()));
    }
    
 // 获取当前时间所在周的开始日期
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        return c.getTime();
    }
    
 // 获取当前时间所在年的周数
    public static int getWeekOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);
 
        return c.get(Calendar.WEEK_OF_YEAR);
    }
 
    // 获取当前时间所在年的最大周数
    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
 
        return getWeekOfYear(c.getTime());
    }
	/**
	 * 今年1月1日的日期
	 * @return
	 */
	public static String yearString(){
	    Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.MONTH, 0);
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String dateStr = sdf.format(new Date(cal.getTimeInMillis()));
	    return dateStr;
	}
	public static String dateGetLastMonthDay(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = "";
		try{
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
			Date now = sdf1.parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);
			int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			cal.add(Calendar.DATE,days-1);
			dateStr = sdf.format(new Date(cal.getTimeInMillis()));
		}catch (Exception e) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE,0);
			dateStr = sdf.format(new Date(cal.getTimeInMillis()));
		}
		return dateStr;
	}
	
	public static List<String> getAllMonth(){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			List<String> time = new ArrayList<String>();
			Date d1 = sdf.parse("2017-01");//定义起始日期
			Date d2 = sdf.parse(sdf.format(new Date()));//定义结束日期
			Calendar dd = Calendar.getInstance();//定义日期实例
			dd.setTime(d2);//设置日期起始时间
			
			while(dd.getTime().after(d1)){//判断是否到结束日期
				
				String str = sdf.format(dd.getTime());
//				System.out.println(str);//输出日期结果
				dd.add(Calendar.MONTH, -1);//进行当前日期月份加1
				time.add(str);
			}
			time.add(sdf.format(d1));
			return time;
		}catch (Exception e) {
			log.error(e);
			return null;
		}
	}
	
	
	public static String ObjectToString(Object t){
		return json.toJson(t);
	}
	
	public static Object StringToObject(String s){
		return json.fromJson(s, Object.class);
	}
	
	
	public synchronized static String getOrderNo(){
		return new Date().getTime() + "" + new Random().nextInt(1000000);
	}
	
	
	public static String wxPayTimeStart(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date().getTime());
	} 
	
	public static String wxPayTimeEnd(){
		
		Date afterDate = new Date(new Date().getTime() + 310000);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(afterDate);
		
	}
	

	public static void main(String[] args) throws Exception {
//		String s="diansga";
//		String[] split = s.split(",");
//		System.out.println(split[0]);
		
//		List<String> s = getAllMonth();
//		for (String string : s) {
//			System.out.println(string);
//		}
		System.out.println(new Date().getTime());
		System.out.println(wxPayTimeStart());
		System.out.println(wxPayTimeEnd());
		
	}
}
