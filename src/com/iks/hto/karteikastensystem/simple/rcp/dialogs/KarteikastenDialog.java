package com.iks.hto.karteikastensystem.simple.rcp.dialogs;

import org.eclipse.core.databinding.DataBindingContext;
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

import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karteikasten;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastensystemPackage;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Person;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Sprache;
import com.iks.hto.karteikastensystem.simple.rcp.databinding.EMFObservablesManager;
import com.iks.hto.karteikastensystem.simple.rcp.databinding.FormBuilder;

public class KarteikastenDialog extends TitleAreaDialog {

	private final Karteikasten kasten;
	private final EditingDomain editingDomain;
	private final DataBindingContext ctx;

	public KarteikastenDialog(Shell parentShell, EditingDomain editingDomain,
			Karteikasten kasten) {
		super(parentShell);
		this.kasten = kasten;
		this.editingDomain = editingDomain;
		this.ctx = new EMFDataBindingContext();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.getShell().setText("Karteikasten anlegen");
		this.setTitle("Karteikasten anlegen");
		this.setMessage("Einen neuen Karteikasten für einen Benutzer anlegen");
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

		int l = Sprache.values().length;
		String[] sprachen = new String[l];
		Sprache[] s = Sprache.values();
		for (int i = 0; i < l; i++) {
			sprachen[i] = s[i].getLiteral();
		}

		FormBuilder<IEMFEditValueProperty> builder = new FormBuilder<IEMFEditValueProperty>();
		builder.addComboEntry("Ausgangssprache: ", EMFEditProperties.value(
				editingDomain,
				KarteikastensystemPackage.Literals.KARTEIKASTEN__VON_SPRACHE),
				sprachen, "Die Ausgangssprache darf nicht leer sein!",
				this.kasten);

		builder.addComboEntry("Zielsprache: ", EMFEditProperties.value(
				editingDomain,
				KarteikastensystemPackage.Literals.KARTEIKASTEN__NACH_SPRACHE),
				sprachen, "Die Zielsprache darf nicht leer sein!", this.kasten);

		Composite formContainer = builder.build(ctx, container);
		formContainer.setLayoutData(new GridData(GridData.FILL_BOTH));

		TitleAreaDialogSupport.create(this, ctx);

		return container;
	}
}
