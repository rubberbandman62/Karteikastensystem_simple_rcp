package com.iks.hto.karteikastensystem.simple.rcp.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPersistable;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.services.ISourceProviderService;

import com.iks.hto.karteikastensystem.core.IKarteikastenSystemResource;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastenSystem;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastensystemFactory;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastensystemPackage;
import com.iks.hto.karteikastensystem.simple.rcp.Activator;
import com.iks.hto.karteikastensystem.simple.rcp.Perspective;
import com.iks.hto.karteikastensystem.simple.rcp.ResourceProvider;
import com.iks.hto.karteikastensystem.simple.rcp.views.KarteikastenSystemViewPart;

public class NeuesKarteikastensystemHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEvaluationContext ctx = (IEvaluationContext) event
				.getApplicationContext();
		Shell shell = (Shell) ctx
				.getVariable(ISources.ACTIVE_WORKBENCH_WINDOW_SHELL_NAME);

		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		String name = dialog.open();

		if (name != null) {
			File f = new File(name);

			if (!f.exists()) {
				ResourceSet resourceSet = new ResourceSetImpl();
				resourceSet.getResourceFactoryRegistry()
						.getExtensionToFactoryMap().put(
								Resource.Factory.Registry.DEFAULT_EXTENSION,
								new XMIResourceFactoryImpl());
				resourceSet.getPackageRegistry().put(
						KarteikastensystemPackage.eNS_URI,
						KarteikastensystemPackage.eINSTANCE);

				Resource resource = resourceSet.createResource(URI
						.createURI("http:///karteikastensystem.iks-gmbh.com"));
				KarteikastenSystem root = KarteikastensystemFactory.eINSTANCE
						.createKarteikastenSystem();
				resource.getContents().add(root);

				try {
					FileOutputStream out = new FileOutputStream(f);
					resource.save(out, null);
					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				IWorkbenchWindow w = (IWorkbenchWindow) ctx
						.getVariable(ISources.ACTIVE_WORKBENCH_WINDOW_NAME);
				String path = f.toURI().toURL().toString();
				path = path.replaceAll(":", "#_#");
				w.getActivePage().showView(KarteikastenSystemViewPart.ID, path,
						IWorkbenchPage.VIEW_ACTIVATE);
			} catch (PartInitException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

}
