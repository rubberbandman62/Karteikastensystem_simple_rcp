package com.iks.hto.karteikastensystem.simple.rcp.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Fach;

public class FachEditorInput implements IEditorInput {

	private final Fach fach;

	public FachEditorInput(Fach fach) {
		super();
		this.fach = fach;
	}

	public Fach getFach() {
		return fach;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return fach.getTyp().getName();
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return fach.getKarteikasten().getVonSprache().getName() + " - "
				+ fach.getKarteikasten().getNachSprache().getName() + ": "
				+ fach.getTyp().getName();
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			return true;
		}
		if (obj instanceof FachEditorInput) {
			return fach.equals(((FachEditorInput) obj).getFach());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return fach.hashCode();
	}

	
}
