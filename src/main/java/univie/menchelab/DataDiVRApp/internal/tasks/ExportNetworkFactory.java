package univie.menchelab.DataDiVRApp.internal.tasks;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.task.AbstractNetworkTaskFactory;
import org.cytoscape.work.TaskIterator;

public class ExportNetworkFactory extends AbstractNetworkTaskFactory {

	final CyServiceRegistrar registrar;


	public ExportNetworkFactory(final CyServiceRegistrar registrar) {
		this.registrar = registrar;
	}

	public TaskIterator createTaskIterator(CyNetwork net) {
		return new TaskIterator(new ExportNetworkTask(registrar, net));
	}


};
