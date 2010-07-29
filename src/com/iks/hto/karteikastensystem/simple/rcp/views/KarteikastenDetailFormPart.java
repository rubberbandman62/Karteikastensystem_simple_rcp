package com.iks.hto.karteikastensystem.simple.rcp.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Fach;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karteifachtyp;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karteikasten;
import com.iks.hto.karteikastensystem.simple.rcp.Activator;

public class KarteikastenDetailFormPart {

	private Image packageImage;
	private Form form;
	private Label nameValue;
	private Label neuValue;
	private Label gelerntValue;
	private Label bekanntValue;
	private Label gesichertValue;
	private Label archiviertValue;

	public KarteikastenDetailFormPart() {
		ImageDescriptor desc = Activator.imageDescriptorFromPlugin(
				"org.eclipse.ui", "icons/silk/package.png");
		if (desc != null) {
			packageImage = desc.createImage();
		} else {
			packageImage = null;
		}
	}

	public KarteikastenDetailFormPart(IWorkbenchPartSite site,
			final Composite parent, FormToolkit toolkit,
			final Karteikasten kasten) {
		this();
		displayForm(site, parent, toolkit, kasten);
	}

	public void displayForm(IWorkbenchPartSite site, final Composite parent,
			FormToolkit toolkit, final Karteikasten kasten) {

		form = toolkit.createForm(parent);
		toolkit.decorateFormHeading(form);
		form.setText("Karteikasten-Details");
		form.setImage(packageImage);

		Composite body = form.getBody();
		body.setLayout(new GridLayout(2, false));

		nameValue = toolkit.createLabel(body, (kasten == null ? "" : kasten
				.getVonSprache().getName()
				+ " - " + kasten.getNachSprache().getName()));
		nameValue.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true,
				false, 2, 1));

		toolkit.createLabel(body, Karteifachtyp.NEU.getName() + ":");
		neuValue = toolkit.createLabel(body, (kasten == null ? "" : ""
				+ kasten.getFach(Karteifachtyp.NEU).getAnzahlKarten()));
		toolkit.createLabel(body, Karteifachtyp.GELERNT.getName() + ":");
		gelerntValue = toolkit.createLabel(body, (kasten == null ? "" : ""
				+ kasten.getFach(Karteifachtyp.GELERNT).getAnzahlKarten()));
		toolkit.createLabel(body, Karteifachtyp.BEKANNT.getName() + ":");
		bekanntValue = toolkit.createLabel(body, (kasten == null ? "" : ""
				+ kasten.getFach(Karteifachtyp.BEKANNT).getAnzahlKarten()));
		toolkit.createLabel(body, Karteifachtyp.GESICHERT.getName() + ":");
		gesichertValue = toolkit.createLabel(body, (kasten == null ? "" : ""
				+ kasten.getFach(Karteifachtyp.GESICHERT).getAnzahlKarten()));
		toolkit.createLabel(body, Karteifachtyp.ARCHIVIERT.getName() + ":");
		archiviertValue = toolkit.createLabel(body, (kasten == null ? "" : ""
				+ kasten.getFach(Karteifachtyp.ARCHIVIERT).getAnzahlKarten()));

	}

	/**
	 * release all sources allocated
	 */
	public void dispose() {
		if (packageImage != null) {
			packageImage.dispose();
		}

		if (form != null) {
			form.dispose();
		}
	}

	public void setKarteikasten(Karteikasten kk) {
		if (kk != null) {
			nameValue.setText((kk.getVonSprache() == null
					|| kk.getNachSprache() == null ? "" : kk.getVonSprache()
					.getName()
					+ " - " + kk.getNachSprache().getName()));
			neuValue.setText("" + kk.getFach(Karteifachtyp.NEU).getAnzahlKarten());
			gelerntValue.setText("" + kk.getFach(Karteifachtyp.GELERNT).getAnzahlKarten());
			bekanntValue.setText("" + kk.getFach(Karteifachtyp.BEKANNT).getAnzahlKarten());
			gesichertValue.setText("" + kk.getFach(Karteifachtyp.GESICHERT).getAnzahlKarten());
			archiviertValue.setText("" + kk.getFach(Karteifachtyp.ARCHIVIERT).getAnzahlKarten());
		}
	}

}
