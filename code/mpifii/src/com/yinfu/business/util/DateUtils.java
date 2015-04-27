package com.yinfu.business.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 时间处理函数
 * 
 * @author 李听
 *
 */
public class DateUtils {

	private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	public static final String TIME_YEAR = "yyyy";
	
	public static final String TIME_MONEN = "MM";
	
	public static final String TIME_DAY = "dd";

	public static String getDate(String interval, Date starttime, String pattern) {
		Calendar temp = Calendar.getInstance(TimeZone.getDefault());
		temp.setTime(starttime);
		temp.add(temp.MONTH, Integer.parseInt(interval));
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(temp.getTime());
	}

	/**
	 * 将字符串类型转换为时间类型
	 * 
	 * @return
	 */
	
	public static Date str2Date(String str) {
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
		try {
			d = sdf.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}

	public static Date str2Date(String str, String pattern) {
		Date d = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			d = sdf.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * 将时间格式化
	 * 
	 * @return
	 */
	public static Date DatePattern(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
		try {
			String dd = sdf.format(date);
			date = str2Date(dd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将时间格式化
	 */
	public static Date DatePattern(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			String dd = sdf.format(date);
			date = str2Date(dd, pattern);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String date2Str(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
		return sdf.format(date);
	}

	public static String date2Str(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 获取昨天
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date getLastDate(Date date) {
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		calendar.setTime(date);

		calendar.add(calendar.DATE, -1);

		return str2Date(date2Str(calendar.getTime()));
	}
	/**
	 * 获取前几天
	 * @param date
	 * @return
	 */
	public static Date getBeforeDate(Date date,int dates) {
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		calendar.setTime(date);

		calendar.add(calendar.DATE, -dates);

		return str2Date(date2Str(calendar.getTime()));
	}

	/**
	 * 获取上周第一天（周一）
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date getLastWeekStart(Date date) {
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		calendar.setTime(date);
		int i = calendar.get(calendar.DAY_OF_WEEK) - 1;
		int startnum = 0;
		if (i == 0) {
			startnum = 7 + 6;
		} else {
			startnum = 7 + i - 1;
		}
		calendar.add(calendar.DATE, -startnum);

		return str2Date(date2Str(calendar.getTime()));
	}

	/**
	 * 获取上周最后一天（周末）
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static Date getLastWeekEnd(Date date) {
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		calendar.setTime(date);
		int i = calendar.get(calendar.DAY_OF_WEEK) - 1;
		int endnum = 0;
		if (i == 0) {
			endnum = 7;
		} else {
			endnum = i;
		}
		calendar.add(calendar.DATE, -(endnum - 1));

		return str2Date(date2Str(calendar.getTime()));
	}
	
	/**
	 * 根据年和月得到天数
	 * @param num 月
	 * @param year 年
	 * @return
	 */
	public static int getday(int num,int year){
		if(num==1 || num==3 || num==5 || num==7 || num==8 || num==10 || num==12){
			return 31;
		}else if(num==2){
			//判断是否为闰年
			if(year%400==0 || (year%4==0 && year%100!=0)){
				return 29;
			}else{
				return 28;
			}
			
		}else{
			return 30;
		}
	}
	/*
	 * 计算当前日期距离下个月还有多少天
	 */
	public static int getdaymis(Date time){
		int year = Integer.parseInt(
				new SimpleDateFormat(TIME_YEAR).format(time));//年
		
		int mm = Integer.parseInt(
				new SimpleDateFormat(TIME_MONEN).format(time));//月
		
		int dd = Integer.parseInt(
				new SimpleDateFormat(TIME_DAY).format(time));//日
		
		
		//获取当前年月的总天数
		int sdd = getday(mm,year);
		
		return sdd-dd;
		
		
	}
	/**
	 * 日期转秒数
	 * @param dateString
	 * @return
	 */
	public static long getTime(String dateString) {
	    long time = 0;
	    try {
		    Date ret = null;
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    ret = sdf.parse(dateString);
		    time = ret.getTime()/1000;
	    } catch (Exception e) {
		
	    }
	    return time;
    }
	
	
	/**
	 * 精确计算时间差，精确到日
	 * @param fistill 起始日期
	 * @param nowtime 结束日期
	 * @param type type为1返回年月日（如：2年3个月零5天） 否则返回总的天数
	 * @return
	 */
	public static String patienage(Date fistill,Date nowtime,Integer type){
		
		int fyear = Integer.parseInt(
				new SimpleDateFormat(TIME_YEAR).format(fistill));//起始年
		
		int fmm = Integer.parseInt(
				new SimpleDateFormat(TIME_MONEN).format(fistill));//起始月
		
		int fdd = Integer.parseInt(
				new SimpleDateFormat(TIME_DAY).format(fistill));//起始日
		
		
		int nyear = Integer.parseInt(
				new SimpleDateFormat(TIME_YEAR).format(nowtime));//结束年
		
		int nmm = Integer.parseInt(
				new SimpleDateFormat(TIME_MONEN).format(nowtime));//结束月
		
		int ndd = Integer.parseInt(
				new SimpleDateFormat(TIME_DAY).format(nowtime));//结束日
		
		int cyear = nyear - fyear;
		int cmm = nmm - fmm;
		int cdd = ndd - fdd;
		
		
		int zyear = 0;
		int zmm = 0;
		int zdd = 0;
		
		int countddd = 0;  //年月日累计天数
		
		if(cdd<0){
			if(cmm<0){
				zyear = cyear - 1;
				zmm = (cmm + 12)-1; 
				int dd = getday(zmm,nyear-1);
				zdd = dd + cdd;
				
				
				countddd = zyear*365+zmm*30+zdd;
				
			}else if(cmm==0){
				zyear = cyear - 1;
				zmm = 12-1; 
				int dd = getday(zmm,nyear-1);
				zdd = dd + cdd;
				
				countddd = zyear*365+zmm*30+zdd;
				
			}else{
				zyear = cyear;
				zmm = cmm - 1; 
				int dd = getday(zmm,nyear);
				zdd = dd + cdd;
				
				countddd = zyear*365+zmm*30+zdd;
				
			}
		}else if(cdd==0){
			if(cmm<0){
				zyear = cyear - 1;
				zmm = cmm + 12; 
				zdd = 0;
				
				countddd = zyear*365+zmm*30;
				
			}else if(cmm==0){
				zyear = cyear;
				zmm = 0; 
				zdd = 0;
				
				countddd = zyear*365+zmm*30;
				
			}else{
				zyear = cyear;
				zmm = cmm; 
				zdd = 0;
				
				countddd = zyear*365+zmm*30;
			}
		}else{
			if(cmm<0){
				zyear = cyear - 1;
				zmm = cmm + 12; 
				zdd = cdd;
				
				countddd = zyear*365+zmm*30+zdd;
			}else if(cmm==0){
				zyear = cyear;
				zmm = 0; 
				zdd = cdd;
				
				countddd = zyear*365+zmm*30+zdd;
			}else{
				zyear = cyear;
				zmm = cmm; 
				zdd = cdd;
				
				countddd = zyear*365+zmm*30+zdd;
			}
		}
		String ptime = null;
		
		if(zdd!=0){
			if(zmm!=0){
				if(zyear!=0){
					ptime = zyear+"年"+zmm+"个月"+"零"+zdd+"天";
				}else{
					ptime = zmm+"个月"+"零"+zdd+"天";
				}
			}else{
				if(zyear!=0){
					ptime = zyear+"年"+"零"+zdd+"天";
				}else{
					ptime = zdd+"天";
				}
			}
		}else{
			if(zmm!=0){
				if(zyear!=0){
					ptime = zyear+"年"+zmm+"个月";
				}else{
					ptime = zmm+"个月";
				}
			}else{
				if(zyear!=0){
					ptime = zyear+"年";
				}else{
					ptime = null;
				}
			}
		}
		if(type==1){
			return ptime;   //返回年月日（如：2年3个月零5天）
		}else{
			return String.valueOf(countddd);  //返回总天数
		}
		
		
	}
	/**
	 * 得到月数
	 * @param year 年数差
	 * @param month 月数差
	 * @return
	 */
	public static int getCmm(Integer year,Integer month){
		int zmm = 0;
		if(month < 0){
			zmm = (month + 12)+(year-1)*12;
		}else if(month == 0){
			zmm = year*12;
		}else{
			zmm = year*12+month;
		}
		return zmm;
	}
	
	

	/**
	 * 改更现在时间
	 */
	public static Date changeDate(String type, int value) {
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		if (type.equals("month")) {
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + value);
		} else if (type.equals("date")) {
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + value);
		}
		return calendar.getTime();
	}

	
	
	/**
	 * 更改时间
	 */
	public static Date changeDate(Date date, String type, int value) {
		if (date != null) {
			// Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			// Calendar calendar = Calendar.
			if (type.equals("month")) {
				calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + value);
			} else if (type.equals("date")) {
				calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + value);
			} else if (type.endsWith("year")) {
				calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + value);
			}
			return calendar.getTime();
		}
		return null;
	}

	/**
	 * haoxw 比较时间是否在这两个时间点之间
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean checkTime(String time1, String time2) {
		Calendar calendar = Calendar.getInstance();
		Date date1 = calendar.getTime();
		Date date11 = DateUtils.str2Date(DateUtils.date2Str(date1, "yyyy-MM-dd") + " " + time1);// 起始时间

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		Date date2 = c.getTime();
		Date date22 = DateUtils.str2Date(DateUtils.date2Str(date2, "yyyy-MM-dd") + " " + time2);// 终止时间

		Calendar scalendar = Calendar.getInstance();
		scalendar.setTime(date11);// 起始时间

		Calendar ecalendar = Calendar.getInstance();
		ecalendar.setTime(date22);// 终止时间

		Calendar calendarnow = Calendar.getInstance();

		if (calendarnow.after(scalendar) && calendarnow.before(ecalendar)) {
			return true;
		} else {
			return false;
		}

	}
	
	/**
	 * haoxw 比较时间是否在这两个时间点之间
	 * 
	 * @param date11
	 * @param date22
	 * @return
	 */
	public static boolean checkTime(Date date11, Date date22) {
		
		

		Calendar scalendar = Calendar.getInstance();
		scalendar.setTime(date11);// 起始时间

		Calendar ecalendar = Calendar.getInstance();
		ecalendar.setTime(date22);// 终止时间

		Calendar calendarnow = Calendar.getInstance();

		if (calendarnow.after(scalendar) && calendarnow.before(ecalendar)) {
			return true;
		} else {
			return false;
		}

	}

	
	public static boolean checkDate(String date1, String date2) {

		Date date11 = DateUtils.str2Date(date1, "yyyy-MM-dd HH:mm:ss");// 起始时间

		Date date22 = DateUtils.str2Date(date2, "yyyy-MM-dd HH:mm:ss");// 终止时间

		Calendar scalendar = Calendar.getInstance();
		scalendar.setTime(date11);// 起始时间

		Calendar ecalendar = Calendar.getInstance();
		ecalendar.setTime(date22);// 终止时间

		Calendar calendarnow = Calendar.getInstance();

		System.out.println(date11.toString());
		System.out.println(date22.toString());
		System.out.println(scalendar.toString());
		System.out.println(ecalendar.toString());
		System.out.println(calendarnow.toString());

		if (calendarnow.after(scalendar) && calendarnow.before(ecalendar)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取interval天之前的日期
	 * 
	 * @param interval
	 * @param starttime
	 * @param pattern
	 * @return
	 */
	public static Date getIntervalDate(String interval, Date starttime, String pattern) {
		Calendar temp = Calendar.getInstance();
		temp.setTime(starttime);
		temp.add(temp.DATE, Integer.parseInt(interval));
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String shijian = sdf.format(temp.getTime());
		return str2Date(shijian);
	}
	

    
    /**
     * 返回日期：yyyyMMddHHmmss格式的字符串
     */
    public static String getStrOfDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    
    /**
     * 获取当前日期时间 返回日期：yyyy-MM-dd HH:mm:ss
     * 
     * @author WikerYong
     * @version 2012-1-9 上午09:47:39
     * @return
     */
    public static String getStrOfDateTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    
    /**
     * 字符串转为日期类型，返回yyyy-MM-dd HH:mm:ss格式
     * 
     * @author WikerYong Email:<a href="#">yw_312@foxmail.com</a>
     * @version 2012-7-5 下午04:33:49
     * @param str
     * @return
     */
    public static Date getDateByStr(String str) {
        Date date = new Date();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = formatter.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    
    /**
     * 获取当前日期时间 返回日期：yyyy-MM-dd HH:mm
     * 
     * @author WikerYong Email:<a href="#">yw_312@foxmail.com</a>
     * @version 2012-1-31 下午02:57:30
     * @return
     */
    public static String getStrOfDateMinute() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    
    /**
     * 返回日期：yyyyMMddHHmmssSSS格式的字符串
     * 
     * @author WikerYong
     * @version 2011-11-25 下午07:18:44
     * @return
     */
    public static String getStrOfMs() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    
    /**
     * 返回日期：yyyyMM格式的字符串
     */
    public static String getMonthFolder() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    
    /**
     * 返回日期：yyyyMM格式的字符串
     */
    public static String getDateFolder() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    
    /**
     * 获取当前月份
     * 
     * @author WikerYong Email:<a href="#">yw_312@foxmail.com</a>
     * @version 2012-4-9 上午10:45:28
     * @return
     */
    public static String getMonth() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    
    /**
     * 获取当前年份
     * 
     * @author WikerYong Email:<a href="#">yw_312@foxmail.com</a>
     * @version 2012-7-5 下午04:31:07
     * @return
     */
    public static String getYear() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    
    /**
     * 返回日期：yyyyMMddHH格式的字符串
     * 
     * @author WikerYong
     * @version 2011-12-20 下午03:43:14
     * @return
     */
    public static String getDataOfHour() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHH");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    
    /**
     * 返回时间：yyyyMMddHHmm格式
     */
    public static String getStrOfTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    
    /**
     * 返回时间：yyyy-MM-dd格式
     */
    public static String getCurrentDay() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    
    /**
     * 返回时间：_yyyy_MM格式只有年月
     */
    public static String getCurrentYearMonth() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("_yyyy_MM");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
    
    /**
     * 返回时间：_yyyy_MM格式只有年月
     */
    public static String getCurrentYearMonth(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("_yyyy_MM");
        String dateString = formatter.format(date);
        return dateString;
    }
    
    /**
     *@return 当前日期的 length 月 的日子 格式是
     */
    public static Date getYearMonth(int length) {
    	Calendar cal=Calendar.getInstance();
    	cal.add(Calendar.MONTH, -length);
        return cal.getTime();
    }
/**
 * 根据传入的日子   求出当期日妻之间的 月份差   
 * @param date
 * @return
 */
    public static int getCalcMonth(Date date){
    	Calendar cal1= Calendar.getInstance();
    	Integer month1= cal1.get(Calendar.MONTH)+1;
    	Integer year =cal1.get(Calendar.YEAR);
    	Calendar cal2=Calendar.getInstance();
    	cal2.setTime(date);
    	Integer cal2month= cal2.get(Calendar.MONTH)+1;
    	Integer cal2year=cal2.get(Calendar.YEAR);
    	Integer resmonth=month1-cal2month;
    	Integer resyear=year-cal2year;
    	return DateUtils.getCmm(resyear, resmonth);
    }
    
    /**
     * 获取传入时间的年份
     * @param length
     * @return
     */
    public static int getYear(Date date) {
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        int year= cal.get(Calendar.YEAR);//传入日期年份
        return year ;
    }
    
    /**
     * 获取传入时间的年份
     * @param length
     * @return
     */
    public static int getMonth(Date date) {
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        int month= cal.get(Calendar.MONTH)+1;//传入日期月份
        return month;
    }
    public static void main(String[] args) {
    	System.out.println(DateUtils.getCalcMonth(DateUtils.getYearMonth(4)));
    }
    
    public static String getLastDay(int day) {
        java.util.Date yestoday = new java.util.Date(new java.util.Date().getTime() - 1000 * 60
                * 60 * 24 * day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(yestoday);
    }
    
    /**
     * 获取昨天、前天的日期
     * 
     * @param currentDate
     * @return
     */
    public static String[] getLastDates(String currentDate) {
        String currYear, currMonth, currDay;
        currYear = currentDate.substring(0, 4);
        currMonth = currentDate.substring(4, 6);
        currDay = currentDate.substring(6);
        
        // 月份或日期首位是0
        String tempMonth, tempDay;
        if (currMonth.substring(0, 1).equals("0")) {
            tempMonth = "0";
        } else {
            tempMonth = "";
        }
        if (currDay.substring(0, 1).equals("0") || currDay.equals("10")) {
            tempDay = "0";
        } else {
            tempDay = "";
        }
        
        String returnDays[] = new String[2];
        
        if (currMonth.equals("01") && currDay.equals("01")) {
            returnDays[0] = (Integer.parseInt(currYear) - 1) + "1231";
            returnDays[1] = (Integer.parseInt(currYear) - 1) + "1230";
        } else if (currMonth.equals("01") && currDay.equals("02")) {
            returnDays[0] = currYear + "0101";
            returnDays[1] = (Integer.parseInt(currYear) - 1) + "1231";
        } else if (Integer.parseInt(currMonth) >= 1 && Integer.parseInt(currDay) > 2) {
            returnDays[0] = currYear + currMonth + tempDay + (Integer.parseInt(currDay) - 1);
            if (currDay.equals("11")) {
                returnDays[1] = currYear + currMonth + "0" + (Integer.parseInt(currDay) - 2);
            } else {
                returnDays[1] = currYear + currMonth + tempDay + (Integer.parseInt(currDay) - 2);
            }
        } else if (Integer.parseInt(currMonth) > 1 && Integer.parseInt(currDay) == 2) {
            returnDays[0] = currYear + currMonth + "01";
            if (currMonth.equals("10")) {
                returnDays[1] = currYear
                        + "0"
                        + (Integer.parseInt(currMonth) - 1)
                        + (getLastDayOfUpMonth(Integer.parseInt(currYear),
                                Integer.parseInt(currMonth), Integer.parseInt(currDay)));
            } else {
                returnDays[1] = currYear
                        + tempMonth
                        + (Integer.parseInt(currMonth) - 1)
                        + (getLastDayOfUpMonth(Integer.parseInt(currYear),
                                Integer.parseInt(currMonth), Integer.parseInt(currDay)));
            }
        } else if (Integer.parseInt(currMonth) > 1 && Integer.parseInt(currDay) == 1) {
            if (currMonth.equals("10")) {
                returnDays[0] = currYear
                        + "0"
                        + (Integer.parseInt(currMonth) - 1)
                        + (getLastDayOfUpMonth(Integer.parseInt(currYear),
                                Integer.parseInt(currMonth), Integer.parseInt(currDay)));
                returnDays[1] = currYear
                        + "0"
                        + (Integer.parseInt(currMonth) - 1)
                        + (getLastDayOfUpMonth(Integer.parseInt(currYear),
                                Integer.parseInt(currMonth), Integer.parseInt(currDay)) - 1);
            } else {
                returnDays[0] = currYear
                        + tempMonth
                        + (Integer.parseInt(currMonth) - 1)
                        + (getLastDayOfUpMonth(Integer.parseInt(currYear),
                                Integer.parseInt(currMonth), Integer.parseInt(currDay)));
                returnDays[1] = currYear
                        + tempMonth
                        + (Integer.parseInt(currMonth) - 1)
                        + (getLastDayOfUpMonth(Integer.parseInt(currYear),
                                Integer.parseInt(currMonth), Integer.parseInt(currDay)) - 1);
            }
        } else {
            returnDays[0] = currYear + currMonth + tempDay + (Integer.parseInt(currDay) - 1);
            returnDays[1] = currYear + currMonth + tempDay + (Integer.parseInt(currDay) - 2);
        }
        
        return returnDays;
    }
    
    public static int getLastDayOfUpMonth(int year, int month, int date) {
        Calendar calendar = new GregorianCalendar(year, month, date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        calendar.add(Calendar.MONTH, -1);// 月增减1天
        calendar.add(Calendar.DAY_OF_MONTH, -1);// 日期倒数一日,既得到本月最后一天
        return calendar.get(Calendar.DATE);
    }
    
    /**
     * 获取当月第一天
     * 
     * @author WikerYong
     * @version 2011-11-21 下午04:45:06
     * @return
     */
    public static String getFirstDayOfMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }
    
    /**
     * 获取当月最后一天
     * 
     * @author WikerYong
     * @version 2011-11-21 下午04:46:06
     * @return
     */
    public static String getLastDayOfMonteh() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }
    
    /**
     * 获取去年的年份
     * 
     * @return
     */
    public static String getLastYear() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(currentTime);
        c.add(Calendar.YEAR, -1);
        String dateString = formatter.format(c.getTime());
        return dateString;
    }
    
    /**
     * 获取前年的年份
     * 
     * @return
     */
    public static String getBeforeLastYear() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(currentTime);
        c.add(Calendar.YEAR, -2);
        String dateString = formatter.format(c.getTime());
        return dateString;
    }
    
    /**
     * 获取某月最后一天
     * 
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }
    
    /**
     * 获取某月第一天
     *
     * @param year
     * @param month
     * @return
     */
    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
    }
    
    /**
     * 判断日期是否为同一天
     *
     * @param dateA
     * @param dateB
     * @return true 则为同一天，false则为不在同一天
     */
    public static boolean isSameDay(Date dateA,Date dateB) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                &&  calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
    }

	public static final String COMMON = "yyyy-MM-dd HH:mm:ss";
	
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(COMMON);
		
		return sdf.format(new Date());
	}
	
	public static String fromDate2String(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(COMMON);
		return sdf.format(date);
	}
	
	public static Date buildDate(int hour , int minute , int second) {
		Calendar c = Calendar.getInstance();
		c.set(2013,9, 26);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		
		return c.getTime(); 
	}
	
//	public static void main(String[] args) {
//		System.out.println(DateUtil.fromDate2String(DateUtil.buildDate(14, 7,11)));
//	}
    
}