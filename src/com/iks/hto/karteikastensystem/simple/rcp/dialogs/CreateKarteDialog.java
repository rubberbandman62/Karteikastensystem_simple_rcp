package com.iks.hto.karteikastensystem.simple.rcp.dialogs;

import org.eclipse.core.databinding.ObservablesManager;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.databinding.edit.IEMFEditValueProperty;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.databinding.dialog.TitleAreaDialogSupport;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karte;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastensystemPackage;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Kartenseite;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Sprache;
import com.iks.hto.karteikastensystem.simple.rcp.databinding.EMFObservablesManager;
import com.iks.hto.karteikastensystem.simple.rcp.databinding.FormBuilder;

public class CreateKarteDialog extends TitleAreaDialog {

	private Karte karte;
	private EditingDomain editingDomain;
	private EMFDataBindingContext ctx;
	private Sprache von;
	private Sprache nach;
	private Kartenseite vonSeite;
	private Kartenseite nachSeite;

	public CreateKarteDialog(Shell parentShell, EditingDomain editingDomain, Karte k,
			Sprache vonSprache, Sprache nachSprache) {
		super(parentShell);
		this.karte = k;
		this.vonSeite = k.getSeite(vonSprache);
		this.nachSeite = k.getSeite(nachSprache);
		this.editingDomain = editingDomain;
		this.ctx = new EMFDataBindingContext();
		this.von = vonSprache;
		this.nach = nachSprache;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.getShell().setText("Karte beschriften");
		this.setTitle("Karte beschriften (" + von.getName() + " - " + nach.getName() + ")");
		this.setMessage("Beschriftung einer neuen Karte. von " + von.getName() + " nach " + nach.getName() + ".");
		final Composite comp = (Composite) super.createDialogArea(parent);

		ObservablesManager mgr = new EMFObservablesManager();
		mgr.runAndCollect(new Runnable() {

			public void run() {
				createForm(comp)
						.setLayoutData(new GridData(GridData.FILL_BOTH));
			}
		});

		return comp;
	}

	private Composite createForm(Composite parent) {
		final Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));

		FormBuilder<IEMFEditValueProperty> builder = new FormBuilder<IEMFEditValueProperty>();
		builder.addLabel(this.von.getName());
		builder.addTextEntry("Vokabel", EMFEditProperties.value(
				editingDomain, KarteikastensystemPackage.Literals.KARTENSEITE__VOKABEL),
				"Die Vokabel der Ausgangssprache muss zumindest eingegeben werden!", this.vonSeite);
		builder.addTextEntry("Beispiel", EMFEditProperties.value(editingDomain,
				KarteikastensystemPackage.Literals.KARTENSEITE__BEISPIEL),
				"Das Anwendungsbeispiel in der Ausgangssprache darf nich leer sein!", this.vonSeite);

		builder.addLabel(this.nach.getName());
		builder.addTextEntry("Vokabel", EMFEditProperties.value(
				editingDomain, KarteikastensystemPackage.Literals.KARTENSEITE__VOKABEL),
				"Die Vokabel der Zielsprache muss zumindest eingegeben werden!", this.nachSeite);
		builder.addTextEntry("Beispiel", EMFEditProperties.value(editingDomain,
				KarteikastensystemPackage.Literals.KARTENSEITE__BEISPIEL),
				"Das Anwendungsbeispiel in der Zielsprache darf nich leer sein!", this.nachSeite);

		Composite formContainer = builder.build(ctx, container);
		formContainer.setLayoutData(new GridData(GridData.FILL_BOTH));

		TitleAreaDialogSupport.create(this, ctx);

		return container;
	}

}
