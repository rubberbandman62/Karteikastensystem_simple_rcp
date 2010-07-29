package com.iks.hto.karteikastensystem.simple.rcp.views;

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
import com.iks.hto.karteikastensystem.simple.rcp.Activator;

public class KarteifachDetailFormPart {

	private Image folderImage;
	private Form form;
	private Label nameValue1;
	private Label nameValue2;
	private Label neuValue;
	private Label gelerntValue;
	private Label bekanntValue;
	private Label gesichertValue;
	private Label archiviertValue;
	private Label anzahlGesamtValue;
	private Label anzahl0Value;
	private Label anzahl1Value;
	private Label anzahl2Value;
	private Label anzahl3Value;

	public KarteifachDetailFormPart() {
		ImageDescriptor desc = Activator.imageDescriptorFromPlugin(
				"org.eclipse.ui", "icons/silk/folder.png");
		if (desc != null) {
			folderImage = desc.createImage();
		} else {
			folderImage = null;
		}
	}

	public KarteifachDetailFormPart(IWorkbenchPartSite site,
			final Composite parent, FormToolkit toolkit, final Fach fach) {
		this();
		displayForm(site, parent, toolkit, fach);
	}

	public void displayForm(IWorkbenchPartSite site, final Composite parent,
			FormToolkit toolkit, final Fach fach) {

		form = toolkit.createForm(parent);
		toolkit.decorateFormHeading(form);
		form.setText("Karteifach-Details");
		form.setImage(folderImage);

		Composite body = form.getBody();
		body.setLayout(new GridLayout(2, false));

		nameValue1 = toolkit.createLabel(body, (fach == null ? "" : fach
				.getKarteikasten().getVonSprache().getName()
				+ " - " + fach.getKarteikasten().getNachSprache().getName()));
		nameValue1.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true,
				false, 2, 1));

		nameValue2 = toolkit.createLabel(body, (fach == null ? "" : fach
				.getTyp().getName()
				+ "e Karten"));
		nameValue2.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true,
				false, 2, 1));

		toolkit.createLabel(body, "Anzahl Karten gesamt:");
		anzahlGesamtValue = toolkit.createLabel(body, (fach == null ? "" : ""
				+ fach.getAnzahlKarten()));

		toolkit.createLabel(body, "Anzahl Karten nicht gewusst:");
		anzahl0Value = toolkit.createLabel(body, (fach == null ? "" : ""
				+ fach.getAnzahlKartenInFolgeGewusst(0)));

		toolkit.createLabel(body, "Anzahl Karten einmal gewusst:");
		anzahl1Value = toolkit.createLabel(body, (fach == null ? "" : ""
				+ fach.getAnzahlKartenInFolgeGewusst(1)));

		toolkit.createLabel(body, "Anzahl Karten zweimal in Folge gewusst:");
		anzahl2Value = toolkit.createLabel(body, (fach == null ? "" : ""
				+ fach.getAnzahlKartenInFolgeGewusst(2)));

		toolkit.createLabel(body, "Anzahl Karten dreimal in Folge gewusst:");
		anzahl3Value = toolkit.createLabel(body, (fach == null ? "" : ""
				+ fach.getAnzahlKartenInFolgeGewusst(3)));

	}

	/**
	 * release all sources allocated
	 */
	public void dispose() {
		if (folderImage != null) {
			folderImage.dispose();
		}

		if (form != null) {
			form.dispose();
		}
	}

	public void setKarteifach(Fach fach) {
		if (fach != null) {
			nameValue1
					.setText((fach.getKarteikasten().getVonSprache() == null
							|| fach.getKarteikasten().getNachSprache() == null ? ""
							: fach.getKarteikasten().getVonSprache().getName()
									+ " - "
									+ fach.getKarteikasten().getNachSprache()
											.getName()));
			nameValue2.setText(fach.getTyp().getName() + "e Karten");
			anzahlGesamtValue.setText("" + fach.getAnzahlKarten());
			anzahl0Value.setText("" + fach.getAnzahlKartenInFolgeGewusst(0));
			anzahl1Value.setText("" + fach.getAnzahlKartenInFolgeGewusst(1));
			anzahl2Value.setText("" + fach.getAnzahlKartenInFolgeGewusst(2));
			anzahl3Value.setText("" + fach.getAnzahlKartenInFolgeGewusst(3));
		}
	}

}
