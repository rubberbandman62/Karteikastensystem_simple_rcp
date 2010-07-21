package com.iks.hto.karteikastensystem.simple.rcp;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.iks.hto.karteikastensystem.simple.rcp.views.DetailsViewPart;
import com.iks.hto.karteikastensystem.simple.rcp.views.KarteikastenSystemViewPart;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

//		layout.setFixed(true);
		System.out.println("in createInitialLayout");
		layout.addStandaloneView(KarteikastenSystemViewPart.ID,  false, IPageLayout.LEFT, 0.5f, editorArea);
		layout.getViewLayout(KarteikastenSystemViewPart.ID).setCloseable(false);
		
		IFolderLayout folder = layout.createFolder("details", IPageLayout.RIGHT, 0.5f, KarteikastenSystemViewPart.ID);
		folder.addView(DetailsViewPart.ID);

	}

}
