package com.iks.hto.karteikastensystem.simple.rcp.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISources;

import com.iks.hto.karteikastensystem.core.IKarteikastenSystemResource;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Fach;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karteifachtyp;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karteikasten;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastensystemFactory;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastensystemPackage;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Person;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.impl.KarteikastensystemFactoryImpl;
import com.iks.hto.karteikastensystem.simple.rcp.ResourceProvider;
import com.iks.hto.karteikastensystem.simple.rcp.dialogs.BenutzerDialog;
import com.iks.hto.karteikastensystem.simple.rcp.dialogs.KarteikastenDialog;

public class NeuerKarteikastenHandler extends AbstractHandler implements
		IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEvaluationContext ctx = (IEvaluationContext) event
				.getApplicationContext();
		IKarteikastenSystemResource resource = (IKarteikastenSystemResource) ctx
				.getVariable(ResourceProvider.MODEL_RESOURCE_NAME);
		Person benutzer = (Person) ctx
				.getVariable(ResourceProvider.BENUTZER_NAME);
		Shell shell = (Shell) ctx.getVariable(ISources.ACTIVE_SHELL_NAME);

		Karteikasten k = KarteikastensystemFactory.eINSTANCE
				.createKarteikasten();
		Command cmd = AddCommand.create(resource.getEditingDomain(), benutzer,
				KarteikastensystemPackage.Literals.PERSON__KARTEIKAESTEN, k);
		if (cmd.canExecute()) {
			resource.executeCmd(cmd);
		} else {
			resource.rollback();
			throw new ExecutionException(
					"Could not execute add command to add a new karteikasten.");
		}

		if (k != null) {
			KarteikastenDialog dialog = new KarteikastenDialog(shell, resource
					.getEditingDomain(), k);
			if (dialog.open() == IDialogConstants.OK_ID) {
				resource.commit();
				k.addFach(Karteifachtyp.NEU);
				k.addFach(Karteifachtyp.GELERNT);
				k.addFach(Karteifachtyp.BEKANNT);
				k.addFach(Karteifachtyp.GESICHERT);
				k.addFach(Karteifachtyp.ARCHIVIERT);
			} else {
				resource.rollback();
			}
		}

		return null;
	}

}
