package io.github.u2ware.xd.data.support;

import org.joda.time.DateTime;

public class DateTimeUtils {

    //protected static Log logger = LogFactory.getLog(MongodbRestControllerSupport.class);
	public static DateTime maximumMinuteOfHour(DateTime d){
		return d.minuteOfHour().withMaximumValue()
				.secondOfMinute().withMaximumValue()
				.millisOfSecond().withMaximumValue();
	}
	public static DateTime maximumHourOfDay(DateTime d){
		return maximumMinuteOfHour(d.hourOfDay().withMaximumValue());
	}
	public static DateTime maximumDayOfMonth(DateTime d){
		return maximumHourOfDay(d.dayOfMonth().withMaximumValue());
	}
	public static DateTime maximumMonthOfYear(DateTime d){
		return maximumDayOfMonth(d.monthOfYear().withMinimumValue());
	}


	public static DateTime minimumMinuteOfHour(DateTime d){
		return d.minuteOfHour().withMinimumValue()
				.secondOfMinute().withMinimumValue()
				.millisOfSecond().withMinimumValue();
	}
	
	public static DateTime minimumHourOfDay(DateTime d){
		return minimumMinuteOfHour(d.hourOfDay().withMinimumValue());
	}
	public static DateTime minimumDayOfMonth(DateTime d){
		return minimumHourOfDay(d.dayOfMonth().withMinimumValue());
	}
	public static DateTime minimumMonthOfYear(DateTime d){
		return minimumDayOfMonth(d.monthOfYear().withMinimumValue());
	}
	
	
	public interface IntervalHandler {
    	void interval(int index, DateTime min, DateTime max);
    }
	
	public static void months(DateTime d, IntervalHandler handler){

    	DateTime x = d.monthOfYear().withMinimumValue();
    	DateTime min , max = null;
    	int index = 0;

    	while(x.getYear() == d.getYear()){

    		min = minimumDayOfMonth(x);
    		max = maximumDayOfMonth(x);
  
    		//logger.debug("months: "+index+": "+min+"~~~"+max);
    		handler.interval(index, min, max);
    		
    		x = x.plusMonths(1);
    		index++;
    	}
	}
	
	public static void days(DateTime d, IntervalHandler handler){

		DateTime x = d.dayOfMonth().withMinimumValue();
    	DateTime min , max = null;
    	int index = 0;

    	while(x.getMonthOfYear() == d.getMonthOfYear()){

    		min = minimumHourOfDay(x);
    		max = maximumHourOfDay(x);

    		//logger.debug("days: "+index+": "+min+"~~~"+max);
    		handler.interval(index, min, max);
    		x = x.plusDays(1);
    		index++;
    	}
	}

	public static void hours(DateTime d, IntervalHandler handler){
    	DateTime x = d.hourOfDay().withMinimumValue();
    	DateTime min , max = null;
    	int index = 0;
    	
    	while(x.getDayOfMonth() == d.getDayOfMonth()){
    		min = minimumMinuteOfHour(x);
    		max = maximumMinuteOfHour(x);

    		//logger.debug("hours: "+index+": "+min+"~~~"+max);
    		handler.interval(index, min, max);
    		
    		x = x.plusHours(1);
    		index++;
    	}
	}
}
