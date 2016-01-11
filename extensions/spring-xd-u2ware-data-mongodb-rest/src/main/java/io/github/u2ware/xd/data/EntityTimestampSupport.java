package io.github.u2ware.xd.data;

import java.util.List;

import org.joda.time.DateTime;

import com.google.common.collect.Lists;

public class EntityTimestampSupport {

    public static enum Interval{
    	REALTIME, //  
    	HOUR, //24
    	DAY, //total day of month
    	MONTH //12
    }
	
	
	public static List<DateTime> getPartical(DateTime datetime, Interval interval) {
		final List<DateTime> partical = Lists.newArrayList();

		DateTime criterion = datetime == null ? DateTime.now() : datetime;
		
		if(Interval.HOUR.equals(interval)){
			hour(criterion, new DatetimeIntervalHandler(){
				public void interval(int index, DateTime min, DateTime max) {
					partical.add(max);
				}
			});
		
		}else if(Interval.DAY.equals(interval)){
			day(criterion, new DatetimeIntervalHandler(){
				public void interval(int index, DateTime min, DateTime max) {
					partical.add(max);
				}
			});

		}else if(Interval.MONTH.equals(interval)){
			month(criterion, new DatetimeIntervalHandler(){
				public void interval(int index, DateTime min, DateTime max) {
					partical.add(max);
				}
			});
			
		}else if(Interval.REALTIME.equals(interval)){
			realtime(DateTime.now(), new DatetimeIntervalHandler(){
				public void interval(int index, DateTime min, DateTime max) {
					partical.add(max);
				}
			});

		}
		
		return partical;
	}

	
	
	private interface DatetimeIntervalHandler {
    	void interval(int index, DateTime min, DateTime max);
    }
	
	private static void hour(DateTime d, DatetimeIntervalHandler handler){
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
	private static void day(DateTime d, DatetimeIntervalHandler handler){

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
	
	private static void month(DateTime d, DatetimeIntervalHandler handler){

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

	private static void realtime(DateTime d, DatetimeIntervalHandler handler){

		int c = 60;
		int i = 3;
		
    	DateTime x = d.minusSeconds(c*i - i);
    	for(int index = 0 ; index < c; index++){
    		handler.interval(index, x, x);
    		x = x.plusSeconds(i);
    		//index++;
    	}
	}
}
