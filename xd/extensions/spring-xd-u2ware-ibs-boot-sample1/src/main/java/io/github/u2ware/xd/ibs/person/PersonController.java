package io.github.u2ware.xd.ibs.person;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.Mongo;

@RestController
public class PersonController {

    protected Log logger = LogFactory.getLog(getClass());

    @Autowired
    private Mongo mongo;
    
    @Autowired
    private PersonRepository personRepository;
    
    
    @RequestMapping(value="/person")
    public Person person(Pageable pageable) {
    	
    	logger.info("mongo "+mongo);
    	logger.info("mongo "+mongo);
    	logger.info("personRepository "+personRepository);
    	logger.info("personRepository "+personRepository);
    	try{
        	Object save = personRepository.save(new Person(1L,"a","b"));
        	logger.info("save "+save);
        	
            Page<Person> list= personRepository.findAll(pageable);
        	logger.info("list "+list);
    	}catch(Exception e){
    		logger.info("ERROR", e);
    	}
        return new Person(1L,"a","b");
    }
}