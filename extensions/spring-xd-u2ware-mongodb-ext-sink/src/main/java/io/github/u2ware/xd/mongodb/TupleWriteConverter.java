package io.github.u2ware.xd.mongodb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.xd.tuple.Tuple;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class TupleWriteConverter implements Converter<Tuple, DBObject> {

	private Log logger = LogFactory.getLog(getClass());

	@Override
	public DBObject convert(Tuple source) {

		logger.debug("TupleWriteConverter");
		logger.debug("TupleWriteConverter");
		logger.debug("TupleWriteConverter");
		logger.debug("TupleWriteConverter");
		logger.debug("TupleWriteConverter");
		logger.debug("TupleWriteConverter");
		logger.debug("TupleWriteConverter");
		
		BasicDBObject result = new BasicDBObject();
		for(String name : source.getFieldNames()){
			result.put(name, source.getValue(name));
		}
		return result;
	}
}
