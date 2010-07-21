package com.iks.hto.karteikastensystem.simple.rcp.views;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;

import com.iks.hto.karteikastensystem.core.IKarteikastenSystemResource;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Fach;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Person;
import com.iks.hto.karteikastensystem.simple.rcp.Activator;
import com.iks.hto.karteikastensystem.simple.rcp.ResourceProvider;

public class DetailsViewPart extends ViewPart implements ISelectionListener {

	public static final String ID = "com.iks.hto.karteikastensystem.simple.rcp.detailsView";

	private PersonDetailFormPart personForm;
	private Image detailsImage;
	private FormToolkit toolkit;
	private Composite parent;

	@Override
	public void createPartControl(Composite parent) {

		this.parent = parent;
		
		ImageDescriptor desc = Activator.imageDescriptorFromPlugin(
				"org.eclipse.ui", "icons/full/obj16/generic_elements.gif");
		if (desc != null) {
			detailsImage = desc.createImage();
		} else {
			detailsImage = null;
		}

		toolkit = new FormToolkit(parent.getDisplay());
		
		personForm = new PersonDetailFormPart(); 
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			if (obj instanceof Person) {
				Person p = (Person) obj;
				personForm.displayForm(this.getViewSite(), this.parent, toolkit, p);
			}
		}

	}

}
