/**
 * <copyright>
 *
 * Copyright (c) 2009 Bestsolution.at and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: 
 *   Tom Schindl<tom.schindl@bestsolution.at> - Initial API and implementation
 *
 * </copyright>
 *
 * $Id: ProjectExplorerPart.java,v 1.6 2009/06/09 07:36:38 tschindl Exp $
 */
package com.iks.hto.karteikastensystem.simple.rcp.views;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.services.ISourceProviderService;

import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Fach;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karteifachtyp;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karteikasten;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastenSystem;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Person;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.provider.KarteikastensystemItemProviderAdapterFactory;
import com.iks.hto.karteikastensystem.simple.rcp.Activator;
import com.iks.hto.karteikastensystem.simple.rcp.ResourceProvider;

/**
 * Part responsible for rendering the project tree
 */
public class KarteikastenSystemExplorerPart {
	private final TreeViewer viewer;
	private FormToolkit toolkit;

	private final IViewSite site;

	/**
	 * Create a new project explorer
	 * 
	 * @param site
	 *            the view site
	 * @param parent
	 *            the parent control to renderer on
	 * @param toolkit
	 *            the toolkit
	 * @param foundation
	 *            the foundation instance
	 * @param manager
	 *            manager for observables to avoid leaks
	 */
	public KarteikastenSystemExplorerPart(IViewSite site, Composite parent,
			FormToolkit toolkit, KarteikastenSystem karteikastensystem,
			ObservablesManager manager) {
		this.toolkit = toolkit;
		this.site = site;

		viewer = init(parent, karteikastensystem);
		
		site.setSelectionProvider(viewer);
	}

	private TreeViewer init(Composite parent,
			KarteikastenSystem karteikastensystem) {

		KarteikastensystemItemProviderAdapterFactory adapterFactory = new KarteikastensystemItemProviderAdapterFactory();

		TreeViewer viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new AdapterFactoryContentProvider(
				adapterFactory));
		viewer
				.setLabelProvider(new AdapterFactoryLabelProvider(
						adapterFactory));

		viewer.setInput(karteikastensystem);

		MenuManager mgr = new MenuManager();
		mgr.add(new GroupMarker("additions"));
		viewer.getControl().setMenu(mgr.createContextMenu(viewer.getControl()));

		site.registerContextMenu(Activator.PLUGIN_ID
				+ ".karteikastenSystemAdditions", mgr, viewer);

		IObservableValue treeObs = ViewerProperties.singleSelection().observe(
				viewer);
		treeObs.addValueChangeListener(new IValueChangeListener() {

			public void handleValueChange(ValueChangeEvent event) {
				ISourceProviderService s = (ISourceProviderService) site
						.getService(ISourceProviderService.class);
				ResourceProvider p = (ResourceProvider) s
						.getSourceProvider(ResourceProvider.MODEL_RESOURCE_NAME);

				if (event.diff.getNewValue() instanceof Person) {
					Person person = (Person) event.diff.getNewValue();
					p.setBenutzer(person);
					p.setKarteikasten(null);
					p.setFach(null);
				} else if (event.diff.getNewValue() instanceof Karteikasten) {
					Karteikasten kk = (Karteikasten) event.diff.getNewValue();
					p.setBenutzer(kk.getPerson());
					p.setKarteikasten(kk);
					p.setFach(null);
				} else if (event.diff.getNewValue() instanceof Fach) {
					Fach f = (Fach) event.diff.getNewValue();
					p.setBenutzer(f.getKarteikasten().getPerson());
					p.setKarteikasten(f.getKarteikasten());
					p.setFach(f);
				} else {
					p.setBenutzer(null);
					p.setKarteikasten(null);
					p.setFach(null);
				}
			}
		});

		return viewer;
	}


	/**
	 * Set the focus
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
		if (viewer.getTree().getItemCount() > 0) {
			viewer.setSelection(new StructuredSelection(viewer.getTree()
					.getItem(0).getData()));
		}
	}

	/**
	 * Dispose all resources and remove all listeners
	 */
	public void dispose() {
	}

}
