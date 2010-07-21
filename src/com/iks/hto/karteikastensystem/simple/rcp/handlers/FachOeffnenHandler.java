package com.iks.hto.karteikastensystem.simple.rcp.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.ui.ISources;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.iks.hto.karteikastensystem.core.IKarteikastenSystemResource;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Fach;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karteikasten;
import com.iks.hto.karteikastensystem.simple.rcp.ResourceProvider;
import com.iks.hto.karteikastensystem.simple.rcp.editors.FachEditor;
import com.iks.hto.karteikastensystem.simple.rcp.editors.FachEditorInput;

public class FachOeffnenHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEvaluationContext ctx = (IEvaluationContext) event
				.getApplicationContext();
		IKarteikastenSystemResource resource = (IKarteikastenSystemResource) ctx
				.getVariable(ResourceProvider.MODEL_RESOURCE_NAME);
		Fach f = (Fach) ctx
				.getVariable(ResourceProvider.FACH_NAME);

		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		
		
		// Get the view
		if (f != null) {
				FachEditorInput input = new FachEditorInput(f);
				try {
					page.openEditor(input, FachEditor.ID);

				} catch (PartInitException e) {
					System.out.println(e.getStackTrace());
				}
		}
		return null;
	}

}
