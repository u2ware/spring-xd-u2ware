package io.github.u2ware.xd.data;

import org.joda.time.DateTime;

public class EntityQuerySupport {

    //protected Log logger = LogFactory.getLog(getClass());
	
    public static enum CalculationType{
    	MIN, MAX, AVG;
    }
    public static enum IntervalType{
    	//REALTIME, // 60 개 
    	HOUR, //24
    	DAY, //total day of month
    	MONTH //12
    }
    
    public interface DatetimeIntervalHandler {

    	void interval(int index, DateTime min, DateTime max);
    }
    
	public void handle(IntervalType criterionType, DateTime criterionDatetime, DatetimeIntervalHandler handler){
	
		DateTime datetime = criterionDatetime == null ? DateTime.now() : criterionDatetime;
		
		if(IntervalType.HOUR.equals(criterionType)){
			hour(datetime, handler);
		
		}else if(IntervalType.DAY.equals(criterionType)){
			day(datetime, handler);
		
		}else if(IntervalType.MONTH.equals(criterionType)){
			month(datetime, handler);

		}else{
			return;//realtime(DateTime.now(), handler);
		}
	}
    
    
    
    
    
	private void hour(DateTime d, DatetimeIntervalHandler handler){
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
	private void day(DateTime d, DatetimeIntervalHandler handler){

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
	
	private void month(DateTime d, DatetimeIntervalHandler handler){

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
	
}
