package com.iks.hto.karteikastensystem.simple.rcp.views;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.services.ISourceProviderService;

import com.iks.hto.karteikastensystem.core.IKarteikastenSystemResource;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Fach;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karteikasten;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Person;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.provider.KarteikastensystemItemProviderAdapterFactory;
import com.iks.hto.karteikastensystem.simple.rcp.Activator;
import com.iks.hto.karteikastensystem.simple.rcp.ResourceProvider;

public class KarteikastenSystemViewPart extends ViewPart implements
		ISaveablePart2, ISelectionListener {
	public static final String ID = "com.iks.hto.karteikastensystem.simple.rcp.view";

	private IPartListener2 listener;
	private IKarteikastenSystemResource.Listener modelListener;

	private FormToolkit toolkit;
	private TreeViewer treeViewer;

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
		// TODO Wird der Partlistener noch gebraucht?
		listener = new PartListenerImpl(this);
		site.getPage().addPartListener(listener);
	}

	@Override
	public void saveState(IMemento memento) {
		super.saveState(memento);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		// IViewSite vs = getViewSite();
		// String secId = vs.getSecondaryId();
		// path = secId.replaceAll("#_#", ":");

		File f = new File(
				"C:/SoftwareDevelopment/Databinding Workspace/com.iks.hto.karteikastensystem.simple.rcp/init/initialData.txt");
		try {
			BufferedReader initStream = new BufferedReader(
					new InputStreamReader(new FileInputStream(f)));
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
		KarteikastensystemItemProviderAdapterFactory adapterFactory = new KarteikastensystemItemProviderAdapterFactory();

		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		treeViewer.setContentProvider(new AdapterFactoryContentProvider(
				adapterFactory));
		treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(
				adapterFactory));

		treeViewer.setInput(resource.getKarteikastenSystem());

		MenuManager mgr = new MenuManager();
		mgr.add(new GroupMarker("additions"));
		treeViewer.getControl().setMenu(
				mgr.createContextMenu(treeViewer.getControl()));

		getSite().registerContextMenu(
				Activator.PLUGIN_ID + ".karteikastenSystemAdditions", mgr,
				treeViewer);

		// register this as a selection listener
		ISelectionService selectionService = (ISelectionService) getSite()
				.getService(ISelectionService.class);
		selectionService.addSelectionListener(this);
		getSite().setSelectionProvider(treeViewer);

//		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
//			
//			@Override
//			public void doubleClick(DoubleClickEvent event) {
//				try {
//					getSite().getPage().showView(DetailsViewPart.ID);
//				} catch (PartInitException e) {
//					e.printStackTrace();
//				}
//				
//			}
//		});
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

		if (toolkit != null) {
			toolkit.dispose();
		}

		super.dispose();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
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

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		ISourceProviderService s = (ISourceProviderService) getSite()
				.getService(ISourceProviderService.class);
		ResourceProvider p = (ResourceProvider) s
				.getSourceProvider(ResourceProvider.MODEL_RESOURCE_NAME);

		if (selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			if (obj instanceof Person) {
				Person person = (Person) obj;
				p.setBenutzer(person);
				p.setKarteikasten(null);
				p.setFach(null);
			} else if (obj instanceof Karteikasten) {
				Karteikasten karteikasten = (Karteikasten) obj;
				p.setBenutzer(karteikasten.getPerson());
				p.setKarteikasten(karteikasten);
				p.setFach(null);
			} else if (obj instanceof Fach) {
				Fach fach = (Fach) obj;
				p.setBenutzer(fach.getKarteikasten().getPerson());
				p.setKarteikasten(fach.getKarteikasten());
				p.setFach(fach);
			} else {
				p.setBenutzer(null);
				p.setKarteikasten(null);
				p.setFach(null);
			}
		}
	}
}