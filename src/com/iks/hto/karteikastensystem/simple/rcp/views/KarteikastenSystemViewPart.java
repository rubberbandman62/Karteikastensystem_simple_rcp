package com.iks.hto.karteikastensystem.simple.rcp.views;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.ISourceProviderService;

import com.iks.hto.karteikastensystem.core.IKarteikastenSystemResource;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastenSystem;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.provider.KarteikastensystemItemProviderAdapterFactory;
import com.iks.hto.karteikastensystem.simple.rcp.Activator;
import com.iks.hto.karteikastensystem.simple.rcp.ResourceProvider;
import com.iks.hto.karteikastensystem.simple.rcp.databinding.EMFObservablesManager;

public class KarteikastenSystemViewPart extends ViewPart implements
		ISaveablePart2 {
	public static final String ID = "com.iks.hto.karteikastensystem.simple.rcp.view";

	private IPartListener2 listener;
	private IKarteikastenSystemResource.Listener modelListener;

	private float divider = 0.2f;
	private static final String DIVIDER_KEY = Activator.PLUGIN_ID + ".divider";

	private FormToolkit toolkit;
	private SashForm sashForm;
	private TreeViewer treeViewer;

	private KarteikastenSystemExplorerPart karteikastenSystemExplorer;
	private ObservablesManager mgr;
	private ObservablesManager defaultMgr;
	private IKarteikastenSystemResource resource;

	private String path;

	private class PartListenerImpl implements IPartListener2 {
		private KarteikastenSystemViewPart viewPart;

		public PartListenerImpl(KarteikastenSystemViewPart viewPart) {
			this.viewPart = viewPart;
		}

		public void partVisible(IWorkbenchPartReference partRef) {
		}

		public void partOpened(IWorkbenchPartReference partRef) {
		}

		public void partInputChanged(IWorkbenchPartReference partRef) {
		}

		public void partHidden(IWorkbenchPartReference partRef) {
		}

		public void partDeactivated(IWorkbenchPartReference partRef) {
		}

		public void partClosed(IWorkbenchPartReference partRef) {
		}

		public void partBroughtToTop(IWorkbenchPartReference partRef) {
		}

		public void partActivated(IWorkbenchPartReference partRef) {
			if (partRef.getPart(false) == viewPart) {
				ISourceProviderService s = (ISourceProviderService) viewPart
						.getSite().getService(ISourceProviderService.class);
				ResourceProvider p = (ResourceProvider) s
						.getSourceProvider(ResourceProvider.MODEL_RESOURCE_NAME);
				p.setModelResource(resource);
			}
		}
	}

	@Override
	public void init(final IViewSite site, IMemento memento)
			throws PartInitException {
		super.init(site, memento);
		if (memento != null && memento.getFloat(DIVIDER_KEY) != null) {
			divider = memento.getFloat(DIVIDER_KEY);
		}

		listener = new PartListenerImpl(this);
		site.getPage().addPartListener(listener);
	}

	@Override
	public void saveState(IMemento memento) {
		super.saveState(memento);
		if (sashForm.getWeights().length > 2) {
			int total = sashForm.getWeights()[0] + sashForm.getWeights()[1];
			memento.putFloat(DIVIDER_KEY, sashForm.getWeights()[0] * 1.f
					/ total);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
//		IViewSite vs = getViewSite();
//		String secId = vs.getSecondaryId();
//		path = secId.replaceAll("#_#", ":");
		
		File f = new File("C:/SoftwareDevelopment/Databinding Workspace/com.iks.hto.karteikastensystem.simple.rcp/init/initialData.txt");
		try {
			BufferedReader initStream = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			path = initStream.readLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("createPartControl: path = " + path);

		resource = Activator.getDefault().loadResource(path);
		if (resource == null) {
			throw new RuntimeException("Could not load resource!");
		}

		this.setPartName(path.substring(path.lastIndexOf('/') + 1));

		modelListener = new IKarteikastenSystemResource.Listener() {

			public void dirtyStateChanged() {
				firePropertyChange(PROP_DIRTY);
			}

			public void commandStackChanged() {
			}

		};

		resource.addListener(modelListener);

		toolkit = new FormToolkit(parent.getDisplay());
		sashForm = new SashForm(parent, SWT.HORIZONTAL);

		/*
		 * Track the creation of observables so that we don't leak listeners
		 * when the view part is closed
		 */
		mgr = new EMFObservablesManager();
		defaultMgr = new ObservablesManager();
		mgr.runAndCollect(new Runnable() {

			public void run() {
				karteikastenSystemExplorer = new KarteikastenSystemExplorerPart(
						getViewSite(), sashForm, toolkit, resource
								.getKarteikastenSystem(), defaultMgr);
			}
		});

		int left = (int) (100 * divider);
	}

	@Override
	public void dispose() {
		if (modelListener != null && resource != null)
			resource.removeListener(modelListener);

		ISourceProviderService s = (ISourceProviderService) getSite()
				.getService(ISourceProviderService.class);
		ResourceProvider p = (ResourceProvider) s
				.getSourceProvider(ResourceProvider.MODEL_RESOURCE_NAME);
		p.setModelResource(null);
		getSite().getPage().removePartListener(listener);

		karteikastenSystemExplorer.dispose();
		
		if (toolkit != null) {
			toolkit.dispose();
		}

		if (defaultMgr != null) {
			defaultMgr.dispose();
		}

		if (mgr != null) {
			mgr.dispose();
		}

		super.dispose();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		karteikastenSystemExplorer.setFocus();
	}

	@Override
	public int promptToSaveOnClose() {
		return ISaveablePart2.DEFAULT;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if (resource != null) {
			IStatus s = resource.save();
			if (!s.isOK()) {
				Activator.getDefault().getLog().log(s);
				throw new RuntimeException();
			} else {
				firePropertyChange(PROP_DIRTY);
			}
		}
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isDirty() {
		return resource.isDirty();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public boolean isSaveOnCloseNeeded() {
		return true;
	}
}