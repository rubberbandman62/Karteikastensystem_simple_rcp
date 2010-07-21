package com.iks.hto.karteikastensystem.simple.rcp;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import com.iks.hto.karteikastensystem.core.IKarteikastenSystemLoadingService;
import com.iks.hto.karteikastensystem.core.IKarteikastenSystemResource;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements ServiceListener {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.iks.hto.karteikastensystem.simple.rcp";

	// The shared instance
	private static Activator plugin;

	private ServiceTracker tracker;

	private IKarteikastenSystemLoadingService loadingService;

	private BundleContext context;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		this.context = context;
		plugin = this;

		tracker = new ServiceTracker(context, IKarteikastenSystemLoadingService.class
				.getName(), null);
		tracker.open();

		loadingService = (IKarteikastenSystemLoadingService) tracker.getService();

		context.addServiceListener(this, "(objectclass="
				+ IKarteikastenSystemLoadingService.class.getName() + ")");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		tracker.close();
		tracker = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * Load the model resource
	 * 
	 * @param uri
	 *            the uri
	 * @return the resource
	 */
	public IKarteikastenSystemResource loadResource(String uri) {
		if (loadingService != null) {
			return loadingService.findAndLoadResource(uri);
		} else {
			getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID,
							"Model loading service not available"));
		}

		return null;
	}

	@Override
	public void serviceChanged(ServiceEvent event) {
		ServiceReference sr = event.getServiceReference();
		switch (event.getType()) {
		case ServiceEvent.REGISTERED: {
			loadingService = (IKarteikastenSystemLoadingService) context.getService(sr);
		}
			break;
		case ServiceEvent.UNREGISTERING: {
			loadingService = null;
		}
			break;
		}
	}
}
