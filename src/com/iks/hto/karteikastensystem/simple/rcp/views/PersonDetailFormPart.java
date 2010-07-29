package com.iks.hto.karteikastensystem.simple.rcp.views;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.databinding.edit.IEMFEditValueProperty;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastensystemPackage;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Person;
import com.iks.hto.karteikastensystem.simple.rcp.Activator;

public class PersonDetailFormPart {

	private Image personImage;
	private Form form;
	private Label nameValue;
	private Label nicknameValue;
	private EMFDataBindingContext dbc;

	public PersonDetailFormPart() {
		ImageDescriptor desc = Activator.imageDescriptorFromPlugin(
				"org.eclipse.ui", "icons/silk/user.png");
		if (desc != null) {
			personImage = desc.createImage();
		} else {
			personImage = null;
		}
		dbc = new EMFDataBindingContext();
	}

	public PersonDetailFormPart(IWorkbenchPartSite site,
			final Composite parent, FormToolkit toolkit,
			EditingDomain editingDomain, final Person benutzer) {
		this();
		displayForm(site, parent, toolkit, editingDomain, benutzer);
	}

	public void displayForm(IWorkbenchPartSite site, final Composite parent,
			FormToolkit toolkit, EditingDomain editingDomain,
			final Person benutzer) {

		form = toolkit.createForm(parent);
		toolkit.decorateFormHeading(form);
		form.setText("Benutzer-Details");
		form.setImage(personImage);

		Composite body = form.getBody();
		body.setLayout(new GridLayout(2, false));

		IWidgetValueProperty textProp = WidgetProperties.text();

		toolkit.createLabel(body, "Name: ");
		if (benutzer == null) {
			nameValue = toolkit.createLabel(body, "");
		} else {
			nameValue = toolkit.createLabel(body, benutzer.getName());
			IObservableValue uiNameValueObs = textProp.observeDelayed(400,
					nameValue);
			IObservableValue mNameValueObs = EMFEditProperties.value(
					editingDomain,
					KarteikastensystemPackage.Literals.PERSON__NAME)
					.observe(benutzer);
			dbc.bindValue(uiNameValueObs, mNameValueObs);
		}

		toolkit.createLabel(body, "Nickname: ");
		if (benutzer == null) {
			nicknameValue = toolkit.createLabel(body, "");
		} else {
			nicknameValue = toolkit.createLabel(body, benutzer.getNickName());
			IObservableValue uiNickNameValueObs = textProp.observeDelayed(400,
					nicknameValue);
			IObservableValue mNickNameValueValueObs = EMFEditProperties.value(
					editingDomain,
					KarteikastensystemPackage.Literals.PERSON__NICK_NAME)
					.observe(benutzer);
			dbc.bindValue(uiNickNameValueObs, mNickNameValueValueObs);
		}

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

	public void setPerson(Person p, EditingDomain editingDomain) {
		if (p != null) {
			nameValue.setText((p.getName() == null ? "" : p.getName()));
			nicknameValue.setText((p.getNickName() == null ? "" : p
					.getNickName()));

			IWidgetValueProperty textProp = WidgetProperties.text();

			IObservableValue uiNameValueObs = textProp.observeDelayed(400,
					nameValue);
			IObservableValue mNameValueObs = EMFEditProperties.value(
					editingDomain,
					KarteikastensystemPackage.Literals.PERSON__NAME)
					.observe(p);
			dbc.bindValue(uiNameValueObs, mNameValueObs);

			IObservableValue uiNickNameValueObs = textProp.observeDelayed(400,
					nicknameValue);
			IObservableValue mNickNameValueValueObs = EMFEditProperties.value(
					editingDomain,
					KarteikastensystemPackage.Literals.PERSON__NICK_NAME)
					.observe(p);
			dbc.bindValue(uiNickNameValueObs, mNickNameValueValueObs);
		}
	}

}
