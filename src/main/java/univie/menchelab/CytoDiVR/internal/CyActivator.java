package univie.menchelab.CytoDiVR.internal;


import static org.cytoscape.work.ServiceProperties.COMMAND;
import static org.cytoscape.work.ServiceProperties.COMMAND_DESCRIPTION;
import static org.cytoscape.work.ServiceProperties.COMMAND_LONG_DESCRIPTION;
import static org.cytoscape.work.ServiceProperties.COMMAND_NAMESPACE;
import static org.cytoscape.work.ServiceProperties.MENU_GRAVITY;
import static org.cytoscape.work.ServiceProperties.PREFERRED_MENU;
import static org.cytoscape.work.ServiceProperties.TITLE;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.cytoscape.application.CyUserLog;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.task.NetworkTaskFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;
import univie.menchelab.CytoDiVR.internal.tasks.ExportNetworkFactory;
import univie.menchelab.CytoDiVR.internal.tasks.SendNetworkFactory;
import univie.menchelab.CytoDiVR.internal.test.TestSendFactory;

public class CyActivator extends AbstractCyActivator {

	public CyActivator() {
		super();
	}


	public void start(BundleContext bc) {
		final Logger logger = Logger.getLogger(CyUserLog.NAME);

		Version v = bc.getBundle().getVersion();
		String version = v.toString(); // The full version
		CyServiceRegistrar registrar = getService(bc, CyServiceRegistrar.class);

		ExportNetworkFactory exportNetwork = new ExportNetworkFactory(registrar);
		SendNetworkFactory sendNetwork = new SendNetworkFactory(registrar);
		TestSendFactory testSendFactory = new TestSendFactory(registrar);

		Properties props = new Properties();

		// // Create button on File -> Export -> Export network as VRNetz
		props.setProperty(PREFERRED_MENU, "File.Export");
		props.setProperty(TITLE, "Network as VRNetz...");
		props.setProperty(MENU_GRAVITY, "4.0");

		registerService(bc, exportNetwork, NetworkTaskFactory.class, props);

		// Create button on Apps -> DataDiVR -> Export as VRNetz
		props.setProperty(PREFERRED_MENU, "Apps.CytoDiVR");
		props.setProperty(TITLE, "Export network as VRNetz...");
		props.setProperty(MENU_GRAVITY, "1.0");

		props.setProperty(COMMAND_NAMESPACE, "datadivr");
		props.setProperty(COMMAND, "export");
		props.setProperty(COMMAND_DESCRIPTION, "Exports the currently selected network.");
		props.setProperty(COMMAND_LONG_DESCRIPTION,
				"<html>The currently selected network gets exported <br />"
						+ "as an VRNetz.<br /></html>");

		registerService(bc, exportNetwork, NetworkTaskFactory.class, props);

		props = new Properties();
		// Create button on Apps -> DataDiVR -> Send network to DataDiVR
		props.setProperty(PREFERRED_MENU, "Apps.CytoDiVR");
		props.setProperty(TITLE, "Send network to DataDiVR");
		props.setProperty(MENU_GRAVITY, "1.0");

		props.setProperty(COMMAND_NAMESPACE, "datadivr");
		props.setProperty(COMMAND, "send");
		props.setProperty(COMMAND_DESCRIPTION,
				"Send the currently selected network to a running DataDiVR server.");
		props.setProperty(COMMAND_LONG_DESCRIPTION,
				"<html>The currently selected network gets send <br />"
						+ "as to the DataDiVR.<br /></html>");

		registerService(bc, sendNetwork, NetworkTaskFactory.class, props);

		props = new Properties();
		// Init Test command
		props.setProperty(COMMAND_NAMESPACE, "datadivr");
		props.setProperty(COMMAND, "testsend");
		props.setProperty(COMMAND_DESCRIPTION,
				"Send the currently selected network to a running DataDiVR server.");
		props.setProperty(COMMAND_LONG_DESCRIPTION,
				"<html>The currently selected network gets send <br />"
						+ "as to the DataDiVR.<br /></html>");

		registerService(bc, testSendFactory, NetworkTaskFactory.class, props);



		logger.info("CytoDiVR " + version + " initialized.");
		System.out.println("CytoDiVR " + version + " initialized.");

	}

}
