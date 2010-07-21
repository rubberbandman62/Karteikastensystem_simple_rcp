package com.iks.hto.karteikastensystem.simple.rcp.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Fach;

public class FachEditor extends EditorPart {

	public static final String ID = "com.iks.hto.karteikastensystem.simple.rcp.editor.fach";

	private Fach fach;

	public FachEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(input);
		fach = ((FachEditorInput) input).getFach();
		setPartName(input.getToolTipText());

	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);

		Label label1 = new Label(parent, SWT.BORDER);
		label1.setText("Karteikasten: ");
		Label karteikastenName = new Label(parent, SWT.BORDER);
		karteikastenName.setText(fach.getKarteikasten().getVonSprache()
				.getName()
				+ " - " + fach.getKarteikasten().getNachSprache().getName());

		Label label2 = new Label(parent, SWT.BORDER);
		label2.setText("Karteikastenfachtyp: ");
		Label karteikastenfachType = new Label(parent, SWT.BORDER);
		karteikastenfachType.setText(fach.getTyp().getName());

		Label label3 = new Label(parent, SWT.BORDER);
		label3.setText("Anzahl Karten: ");
		Label anzahlKarten = new Label(parent, SWT.BORDER);
		anzahlKarten.setText("" + fach.getKarten().size());

	}

	@Override
	public void setFocus() {
	}

}
