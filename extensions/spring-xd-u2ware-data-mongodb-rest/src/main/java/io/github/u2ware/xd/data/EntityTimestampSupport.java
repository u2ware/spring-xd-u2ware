package io.github.u2ware.xd.data;

import org.joda.time.DateTime;

public class EntityTimestampSupport {

    public static enum Calculation{
    	MIN, MAX, AVG;
    }
    public static enum Interval{
    	REALTIME, //60  
    	MINUTE, //60
    	HOUR, //24
    	DAY, //total day of month
    	MONTH //12
    }
	
	public interface IntervalHandler {
    	void interval(int index, DateTime min, DateTime max);
    }
	
	public static void handle(DateTime datetime, Interval interval, IntervalHandler handler) {

		DateTime criterion = datetime == null ? DateTime.now() : datetime;
		
		if(Interval.MINUTE.equals(interval)){
			minute(criterion, handler);
		
		}else if(Interval.HOUR.equals(interval)){
			hour(criterion, handler);
		
		}else if(Interval.DAY.equals(interval)){
			day(criterion, handler);

		}else if(Interval.MONTH.equals(interval)){
			month(criterion, handler);
			
		}else if(Interval.REALTIME.equals(interval)){
			realtime(criterion, handler);

		}
	}

	private static void minute(DateTime d, IntervalHandler handler){
		
    	DateTime x = d.minuteOfHour().withMinimumValue();
    	DateTime min , max = null;
    	int index = 0;
    	
    	while(x.getHourOfDay() == d.getHourOfDay()){
    		min = x.secondOfMinute().withMinimumValue()
					.millisOfSecond().withMinimumValue();
    		
    		max = x.secondOfMinute().withMaximumValue()
					.millisOfSecond().withMaximumValue();

    		//logger.debug(count+": "+min+"~~~"+max);
    		handler.interval(index, min, max);
    		
    		x = x.plusMinutes(1);
    		index++;
    	}
	}
	
	
	
	private static void hour(DateTime d, IntervalHandler handler){
    	DateTime x = d.hourOfDay().withMinimumValue();
    	DateTime min , max = null;
    	int index = 0;
    	
    	while(x.getDayOfMonth() == d.getDayOfMonth()){
    		min = x.minuteOfHour().withMinimumValue()
					.secondOfMinute().withMinimumValue()
					.millisOfSecond().withMinimumValue();
    		
    		max = x.minuteOfHour().withMaximumValue()
					.secondOfMinute().withMaximumValue()
					.millisOfSecond().withMaximumValue();

    		//logger.debug(count+": "+min+"~~~"+max);
    		handler.interval(index, min, max);
    		
    		x = x.plusHours(1);
    		index++;
    	}
	}
	private static void day(DateTime d, IntervalHandler handler){

		DateTime x = d.dayOfMonth().withMinimumValue();
    	DateTime min , max = null;
    	int index = 0;

    	while(x.getMonthOfYear() == d.getMonthOfYear()){

    		min = x.hourOfDay().withMinimumValue()
    				.minuteOfHour().withMinimumValue()
					.secondOfMinute().withMinimumValue()
					.millisOfSecond().withMinimumValue();
    		
    		max = x.hourOfDay().withMaximumValue()
    				.minuteOfHour().withMaximumValue()
					.secondOfMinute().withMaximumValue()
					.millisOfSecond().withMaximumValue();

    		//logger.debug(count+": "+min+"~~~"+max);

    		handler.interval(index, min, max);
    		
    		x = x.plusDays(1);
    		index++;
    	}
	}
	
	private static void month(DateTime d, IntervalHandler handler){

    	DateTime x = d.monthOfYear().withMinimumValue();
    	DateTime min , max = null;
    	int index = 0;

    	while(x.getYear() == d.getYear()){

    		min = x.dayOfMonth().withMinimumValue()
    				.hourOfDay().withMinimumValue()
    				.minuteOfHour().withMinimumValue()
					.secondOfMinute().withMinimumValue()
					.millisOfSecond().withMinimumValue();
    		
    		max = x.dayOfMonth().withMaximumValue()
    				.hourOfDay().withMaximumValue()
    				.minuteOfHour().withMaximumValue()
					.secondOfMinute().withMaximumValue()
					.millisOfSecond().withMaximumValue();

    		//logger.debug(count+": "+min+"~~~"+max);
    		handler.interval(index, min, max);
    		
    		x = x.plusMonths(1);
    		index++;
    	}
	}

	private static void realtime(DateTime d, IntervalHandler handler){

		int c = 60;
		int i = 3;

    	DateTime min = d.millisOfSecond().withMinimumValue().minusSeconds(c*i - i);
    	DateTime max;
    	for(int index = 0 ; index < c; index++){

    		max = min.plusSeconds(2).millisOfSecond().withMaximumValue();
    		
    		handler.interval(index, min, max);

    		min = min.plusSeconds(3);
    	}
	}
}
