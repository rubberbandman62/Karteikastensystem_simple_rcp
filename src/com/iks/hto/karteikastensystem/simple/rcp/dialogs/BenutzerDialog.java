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

import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastensystemPackage;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Person;
import com.iks.hto.karteikastensystem.simple.rcp.databinding.EMFObservablesManager;
import com.iks.hto.karteikastensystem.simple.rcp.databinding.FormBuilder;

public class BenutzerDialog extends TitleAreaDialog {

	private final Person person;
	private final EditingDomain editingDomain;
	private final DataBindingContext ctx;

	public BenutzerDialog(Shell parentShell, EditingDomain editingDomain,
			Person person) {
		super(parentShell);
		this.person = person;
		this.editingDomain = editingDomain;
		this.ctx = new EMFDataBindingContext();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		this.getShell().setText("Benutzer bearbeiten");
		this.setTitle("Benutzer bearbeiten");
		this.setMessage("Bearbeite die Informationen eines Benutzers");
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
		builder.addTextEntry("Name", EMFEditProperties.value(editingDomain,
				KarteikastensystemPackage.Literals.PERSON__NAME),
				"Der Name darf nicht ller sein!", person);
		builder.addTextEntry("Spitzname", EMFEditProperties.value(
				editingDomain,
				KarteikastensystemPackage.Literals.PERSON__NICK_NAME),
				"Lastname must not be empty", person);

		Composite formContainer = builder.build(ctx, container);
		formContainer.setLayoutData(new GridData(GridData.FILL_BOTH));

		TitleAreaDialogSupport.create(this, ctx);

		return container;
	}
}
