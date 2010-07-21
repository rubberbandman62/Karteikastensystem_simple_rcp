package com.iks.hto.karteikastensystem.simple.rcp.views;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastenSystem;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Person;
import com.iks.hto.karteikastensystem.simple.rcp.Activator;

public class PersonDetailFormPart {

	private Image personImage;
	private Form form;

	public PersonDetailFormPart() {
		ImageDescriptor desc = Activator.imageDescriptorFromPlugin(
				"org.eclipse.ui", "icons/silk/user.png");
		if (desc != null) {
			personImage = desc.createImage();
		} else {
			personImage = null;
		}
	}

	public void displayForm(IViewSite site, final Composite parent,
			FormToolkit toolkit, final Person benutzer) {

		form = toolkit.createForm(parent);
		toolkit.decorateFormHeading(form);
		form.setText("Benutzer-Details");
		form.setImage(personImage);

		Composite body = form.getBody();
		body.setLayout(new GridLayout(2, false));

		toolkit.createLabel(body, "Name: ");
		toolkit.createLabel(body, benutzer.getName());

		toolkit.createLabel(body, "Nickname: ");
		toolkit.createLabel(body, benutzer.getNickName());

	}

	public void hideForm() {
		form.setVisible(false);
	}
	
	public void showForm() {
		form.setVisible(true);
	}
	
	/**
	 * release all sources allocated
	 */
	public void dispose() {
		if (personImage != null) {
			personImage.dispose();
		}

		if (form != null) {
			form.dispose();
		}
	}

}
