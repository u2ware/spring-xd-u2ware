package io.github.u2ware.xd.modbus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.xd.dirt.module.SynchronizingModuleRegistry;
import org.springframework.xd.dirt.module.UploadedModuleDefinition;
import org.springframework.xd.dirt.module.WritableResourceModuleRegistry;
import org.springframework.xd.dirt.server.singlenode.SingleNodeApplication;
import org.springframework.xd.dirt.test.SingleNodeIntegrationTestSupport;
import org.springframework.xd.module.ModuleType;


public class ModbusSourceModuleUploadMain {
	
    protected static Log logger = LogFactory.getLog(ModbusSourceModuleUploadMain.class);

    private static final String moduleName     = "u2ware-modbus-source";
    private static final String moduleType     = "source";
    private static final String moduleResource = "target/spring-xd-u2ware-modbus-source-1.0.0.jar";

    private static final String sourceResource     = "../../xd/custom-modules/";
    private static final String targetResource     = "../../xd/custom-modules/";
    
    public static void main(String[] args) throws Exception{
    	logger.info("ModuleUploadPlugin starting.....");
		SingleNodeApplication application = new SingleNodeApplication().run();
		
		logger.info("ModuleUploadPlugin sourceUri: "+sourceResource);
		logger.info("ModuleUploadPlugin targetUri: "+targetResource);
		logger.info("ModuleUploadPlugin moduleName: "+moduleName);
		logger.info("ModuleUploadPlugin moduleType: "+moduleType);
		logger.info("ModuleUploadPlugin moduleResource: "+moduleResource);

		String sourceUri = new FileSystemResource(sourceResource).getURI().toString();
		String targetUri = new FileSystemResource(targetResource).getURI().toString();
		Resource resource = new FileSystemResource(moduleResource);

		UploadedModuleDefinition md = new UploadedModuleDefinition(moduleName, ModuleType.valueOf(moduleType), resource.getInputStream());
		
		WritableResourceModuleRegistry sourceRegistry = new WritableResourceModuleRegistry(sourceUri);
		WritableResourceModuleRegistry targetRegistry = new WritableResourceModuleRegistry(targetUri);
		sourceRegistry.afterPropertiesSet();		
		targetRegistry.afterPropertiesSet();		
		
		SynchronizingModuleRegistry synchRegistry = new SynchronizingModuleRegistry(sourceRegistry, targetRegistry);
		synchRegistry.delete(md);
		synchRegistry.registerNew(md);
		
		SingleNodeIntegrationTestSupport singleNodeIntegrationTestSupport 
			= new SingleNodeIntegrationTestSupport(application);
		singleNodeIntegrationTestSupport.addModuleRegistry(synchRegistry);

		logger.info("ModuleUploadPlugin module uploaded!!! ");
		System.exit(0);
    }
}



