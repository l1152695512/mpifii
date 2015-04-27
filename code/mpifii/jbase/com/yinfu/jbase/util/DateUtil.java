 package com.yinfu.jbase.util;
 
 import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

 
 public class DateUtil
 {
   public static final String HH_MM_SS = "HH:mm:ss";
   public static final String YYYY_MM_DD = "yyyy-MM-dd";
   public static final String MM_DD = "MM-dd";
   public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
   public static final String YY_MM_DD_HH_MM_SS = "yy-MM-dd HH:mm:ss";
 
   public static String addDay(String data)
   {
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     Calendar cd = Calendar.getInstance();
     try
     {
       cd.setTime(sdf.parse(data));
       cd.add(5, 1);
     }
     catch (ParseException e)
     {
       e.printStackTrace();
     }
 
     return sdf.format(cd.getTime());
   }
 
   public static String addDay(int add)
   {
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     Calendar cd = Calendar.getInstance();
     cd.setTime(new Date());
     cd.add(5, add);
     return sdf.format(cd.getTime());
   }
 
   public static String addDay(String data, int add)
   {
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     Calendar cd = Calendar.getInstance();
     try
     {
       cd.setTime(sdf.parse(data));
       cd.add(5, add);
     }
     catch (ParseException e)
     {
       e.printStackTrace();
     }
 
     return sdf.format(cd.getTime());
   }
 
   public static Date getAfterDate(Date date, int amount)
   {
     Calendar cal = Calendar.getInstance();
     cal.setTime(date);
     cal.add(5, amount);
     return cal.getTime();
   }
 
   public static int getMonthDay(int year, int month)
   {
     return getMonthDayByYear(year)[(--month)];
   }
 
   public static int[] getMonthDayByYear(int year)
   {
     int[] monthDay = 
       { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
     if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) monthDay[1] += 1;
     return monthDay;
   }
 
   public static long[] dateDiff(String startTime, String endTime)
   {
     return dateDiff(startTime, endTime, "yyyy-MM-dd");
   }
 
   public static long[] dateDiff(String startTime, String endTime, String format)
   {
     SimpleDateFormat sd = new SimpleDateFormat(format);
 
     long nd = 86400000L;
 
     long nh = 3600000L;
 
     long nm = 60000L;
 
     long ns = 1000L;
 
     long[] date = new long[4];
     try
     {
       long diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
 
       long day = diff / nd;
       long hour = diff % nd / nh;
       long min = diff % nd % nh / nm;
       long sec = diff % nd % nh % nm / ns;
 
       date[0] = day;
       date[1] = hour;
       date[2] = min;
       date[3] = sec;
     }
     catch (ParseException e)
     {
       e.printStackTrace();
     }
 
     return date;
   }
 
   public static Date parseDate(String dateStr, String format)
   {
     Date date = null;
     try
     {
       SimpleDateFormat dateFormat = new SimpleDateFormat(format);
 
       date = dateFormat.parse(dateStr);
     }
     catch (Exception localException)
     {
     }
     return date;
   }
 
   public static String formatDate(String dateStr, String format, String toFormat)
   {
     return format(parseDate(dateStr, format), toFormat);
   }
 
   public static Date parseDate(String dateStr)
   {
     return parseDate(dateStr, "yyyy-MM-dd");
   }
 
   public static String format(Date date, String format)
   {
     String result = "";
     try
     {
       if (date != null)
       {
         DateFormat dateFormat = new SimpleDateFormat(format);
         result = dateFormat.format(date);
       }
     }
     catch (Exception localException) {
     }
     return result;
   }
 
   public static String format(Date date)
   {
     return format(date, "yyyy-MM-dd");
   }
 
   public static int getYear(Date date)
   {
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(date);
     return calendar.get(1);
   }
 
   public static int getMonth(Date date)
   {
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(date);
     return calendar.get(2) + 1;
   }
 
   public static int getDay(Date date)
   {
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(date);
     return calendar.get(5);
   }
 
   public static int getHour(Date date)
   {
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(date);
     return calendar.get(11);
   }
 
   public static int getMinute(Date date)
   {
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(date);
     return calendar.get(12);
   }
 
   public static int getSecond(Date date)
   {
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(date);
     return calendar.get(13);
   }
 
   public static long getMillis(Date date)
   {
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(date);
     return calendar.getTimeInMillis();
   }
   
   public static String getNow(){
	   
	   return getDate(new Date());
   }
 
   public static String getDate(Date date)
   {
     return format(date, "yyyy-MM-dd");
   }
 
   public static String getTime(Date date)
   {
     return format(date, "HH:mm:ss");
   }
 
   public static String getDateTime(Date date)
   {
     return format(date, "yyyy-MM-dd HH:mm:ss");
   }
 
   public static Date addDate(Date date, int day)
   {
     Calendar calendar = Calendar.getInstance();
     long millis = getMillis(date) + day * 24L * 3600L * 1000L;
     calendar.setTimeInMillis(millis);
     return calendar.getTime();
   }
 
   public static long diffDate(Date date, Date date1)
   {
     return (getMillis(date) - getMillis(date1)) / 86400000L;
   }
 
   public static String getMonthBegin(String strdate)
   {
     Date date = parseDate(strdate);
     return format(addDate(null, 0), "yyyy-MM") + "-01";
   }
 
   public static String getMonthEnd(String strdate)
   {
     Date date = parseDate(getMonthBegin(strdate));
     Calendar calendar = Calendar.getInstance();
     calendar.setTime(date);
     calendar.add(2, 1);
     calendar.add(6, -1);
     return formatDate(calendar.getTime());
   }
 
   public static String formatDate(Date date)
   {
     return formatDateByFormat(date, "yyyy-MM-dd");
   }
 
   public static String formatDateByFormat(Date date, String format)
   {
     String result = "";
     if (date != null)
     {
       try
       {
         SimpleDateFormat sdf = new SimpleDateFormat(format);
         result = sdf.format(date);
       }
       catch (Exception ex)
       {
         ex.printStackTrace();
       }
     }
     return result;
   }
 
   public static final int getCurrentDayOfWeek()
   {
     return Calendar.getInstance().get(7) - 1;
   }
 //@formatter:off 
 	/**
 	 * Title: getQuot
// 	 * Description:计算日期天数差
 	 * Created On: 2014年8月12日 上午11:26:19
 	 * @author JiaYongChao
 	 * <p>
 	 * @param stratDate开始时间
 	 * @param endDate结束时间
 	 * @return 
 	 */
 	//@formatter:on
 	public static long getQuot(String stratDate, String endDate){
 		  long quot = 0;
 		  SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
 		  try {
 		   Date date1 = ft.parse( endDate );
 		   Date date2 = ft.parse( stratDate );
 		   quot = date1.getTime() - date2.getTime();
 		   quot = quot / 1000 / 60 / 60 / 24;
 		  } catch (ParseException e) {
 		   e.printStackTrace();
 		  }
 		  return quot;
 		 }
 	
 	//@formatter:off 
 	/**
 	 * Title: scopeTime
 	 * Description:获得时间范围内的所有天数数组(不带年份)
 	 * Created On: 2014年8月12日 下午2:49:35
 	 * @author JiaYongChao
 	 * <p>
 	 * @param startDate
 	 * @param endDate
 	 * @return 
 	 */
 	//@formatter:on
 	public static String[] scopeTime(String startDate,String endDate){
 		String date ="";
 		String[] dates = new String []{};
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 		try {
			Date begin = sdf.parse(startDate);
			Date end = sdf.parse(endDate);
			double between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
			double day = between / (24 * 3600);
			for (int i = 0; i <= day; i++) {
				Calendar cd = Calendar.getInstance();
				cd.setTime(sdf.parse(startDate));
				cd.add(Calendar.DATE, i);// 增加一天
				// cd.add(Calendar.MONTH, n);//增加一个月
				int month = (cd.get(Calendar.MONTH)) + 1;  
			    // 当前月的第几天：即当前日  
			    int day_of_month = cd.get(Calendar.DAY_OF_MONTH);
			    String info =month+"-"+day_of_month;
			    date+=info+";";
			}
			dates = date.split(";");
			return dates;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
 		
 	}
 	//@formatter:off 
 	/**
 	 * Title: scopeTimes
 	 * Description:获得时间范围内的所有天数数组(带年份)
 	 * Created On: 2014年8月28日 下午6:22:03
 	 * @author JiaYongChao
 	 * <p>
 	 * @param startDate
 	 * @param endDate
 	 * @return 
 	 */
 	//@formatter:on
 	public static String[] scopeTimes(String startDate,String endDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date ="";
 		String[] dates = new String []{};
		try {
			Date begin = sdf.parse(startDate);
			Date end = sdf.parse(endDate);
			double between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒
			double day = between / (24 * 3600);
			for (int i = 0; i <= day; i++) {
				Calendar cd = Calendar.getInstance();
				cd.setTime(sdf.parse(startDate));
				cd.add(Calendar.DATE, i);// 增加一天
				// cd.add(Calendar.MONTH, n);//增加一个月
				 String info =sdf.format(cd.getTime());
				  date+=info+";";
			}
			dates = date.split(";");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dates;
 		
 	}
 	/**
     * 获得指定日期的前一天
     * 
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
                .getTime());
        return dayBefore;
    }
    
    //@formatter:off 
    /**
     * Title: getSevenDayBefore
     * Description:获得当前日期前7天的天数
     * Created On: 2014年12月29日 下午4:17:14
     * @author JiaYongChao
     * <p>
     * @param dates
     * @return 
     * @throws ParseException 
     */
    //@formatter:on
    public static String getSevenDayBefore(String date){
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		Date inputDate = null;
		try {
			inputDate = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		Calendar cal = Calendar.getInstance();  cal.setTime(inputDate);  
		int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);  
		cal.set(Calendar.DAY_OF_YEAR , inputDayOfYear-7);   
		String afterDate = dateFormat.format(cal.getTime());
		return afterDate;
    }
    //@formatter:off 
    /**
     * Title: getSevenDayBefore
     * Description:获得当前日期前7天的天数
     * Created On: 2014年12月29日 下午4:17:14
     * @author JiaYongChao
     * <p>
     * @param dates
     * @return 
     * @throws ParseException 
     */
    //@formatter:on
    public static String getOneMonth(String date){
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		Date inputDate = null;
		try {
			inputDate = dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		Calendar cal = Calendar.getInstance();  cal.setTime(inputDate);  
		int inputDayOfYear = cal.get(Calendar.DAY_OF_YEAR);  
		cal.set(Calendar.DAY_OF_YEAR , inputDayOfYear-11);   
		String afterDate = dateFormat.format(cal.getTime());
		return afterDate;
    }

    private static     Calendar cal=Calendar.getInstance();
    public static int getNowYear(){
    	return cal.get(Calendar.YEAR);
    }
    public static int getNowMonty(){
    	return cal.get(Calendar.MARCH);
    }
    
    /**
     * 获得当前日期 前或者后n个月
     * n=-1 n=2
     * @return
     * @param dateStr=2015-01 
     * @throws ParseException 
     */
    public static String getBeforOrAfterNMonth(String dateStr,int n) throws ParseException{
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
    	cal.setTime(dateFormat.parse(dateStr));
    	cal.add(Calendar.MONTH, n);
    	return dateFormat.format(cal.getTime());
    }
    
 }

