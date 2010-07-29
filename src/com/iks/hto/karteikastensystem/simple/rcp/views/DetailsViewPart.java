package com.iks.hto.karteikastensystem.simple.rcp.views;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Fach;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karteikasten;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Person;
import com.iks.hto.karteikastensystem.simple.rcp.Activator;

public class DetailsViewPart extends ViewPart implements ISelectionListener {

	public static final String ID = "com.iks.hto.karteikastensystem.simple.rcp.detailsView";

	private PersonDetailFormPart personForm;
	private KarteikastenDetailFormPart karteikastenForm;
	private KarteifachDetailFormPart karteifachForm;
	private Image detailsImage;
	private FormToolkit toolkit;
	private StackLayout stackLayout;
	private Composite personComposite;
	private Composite karteikastenComposite;
	private Composite emptyComposite;
	private Composite stackComposite;

	private Composite karteifachComposite;

	@Override
	public void createPartControl(Composite parent) {
		System.out.println("DetailsViewPart: createPartControl");
		stackComposite = new Composite(parent, SWT.NONE);
		stackLayout = new StackLayout();
		stackComposite.setLayout(stackLayout);
		personComposite = new Composite(stackComposite, SWT.NONE);
		karteikastenComposite = new Composite(stackComposite, SWT.NONE);
		karteifachComposite = new Composite(stackComposite, SWT.NONE);

		emptyComposite = new Composite(stackComposite, SWT.NONE);
		emptyComposite.setLayout(new FillLayout());
		new Label(emptyComposite, SWT.NONE).setText("No person selected");

		stackLayout.topControl = emptyComposite;

		GridLayout layout = new GridLayout();
		personComposite.setLayout(layout);
		karteikastenComposite.setLayout(layout);
		karteifachComposite.setLayout(layout);

		ImageDescriptor desc = Activator.imageDescriptorFromPlugin(
				"org.eclipse.ui", "icons/full/obj16/generic_elements.gif");
		if (desc != null) {
			detailsImage = desc.createImage();
		} else {
			detailsImage = null;
		}

		// register this as a selection listener
		ISelectionService selectionService = (ISelectionService) getSite()
				.getService(ISelectionService.class);
		selectionService.addSelectionListener(this);

		toolkit = new FormToolkit(parent.getDisplay());

		
		personForm = new PersonDetailFormPart(getSite(), personComposite,
				toolkit, Activator.getDefault().getLastLoadedResource().getEditingDomain(), null);
		karteikastenForm = new KarteikastenDetailFormPart(getSite(),
				karteikastenComposite, toolkit, null);
		karteifachForm = new KarteifachDetailFormPart(getSite(),
				karteifachComposite, toolkit, null);
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			if (obj instanceof Person) {
				Person p = (Person) obj;
				personForm.setPerson(p, Activator.getDefault().getLastLoadedResource().getEditingDomain());
				stackLayout.topControl = personComposite;
			} else if (obj instanceof Karteikasten) {
				Karteikasten kk = (Karteikasten) obj;
				karteikastenForm.setKarteikasten(kk);
				stackLayout.topControl = karteikastenComposite;
			} else if (obj instanceof Fach) {
				Fach f = (Fach) obj;
				karteifachForm.setKarteifach(f);
				stackLayout.topControl = karteifachComposite;
			} else {
				stackLayout.topControl = emptyComposite;
			}
			stackComposite.layout(true, true);
		}

	}

}
