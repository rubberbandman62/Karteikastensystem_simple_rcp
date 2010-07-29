package com.iks.hto.karteikastensystem.simple.rcp.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.RemoveCommand;

import com.iks.hto.karteikastensystem.core.IKarteikastenSystemResource;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karteikasten;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastensystemPackage;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Person;
import com.iks.hto.karteikastensystem.simple.rcp.ResourceProvider;

public class KarteikastenLöschenHandler extends AbstractHandler implements
		IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEvaluationContext ctx = (IEvaluationContext) event
				.getApplicationContext();
		IKarteikastenSystemResource resource = (IKarteikastenSystemResource) ctx
				.getVariable(ResourceProvider.MODEL_RESOURCE_NAME);
		Person benutzer = (Person) ctx
				.getVariable(ResourceProvider.BENUTZER_NAME);
		Karteikasten kk = (Karteikasten) ctx
				.getVariable(ResourceProvider.KARTEIKASTEN_NAME);

		if (kk == null)
			return null;

		Command cmd = RemoveCommand.create(resource.getEditingDomain(),
				benutzer,
				KarteikastensystemPackage.Literals.PERSON__KARTEIKAESTEN, kk);

		if (cmd.canExecute()) {
			resource.executeCmd(cmd);
		} else {
			resource.rollback();
			throw new ExecutionException(
					"Could not execute remove command to remove a karteikasten.");
		}

		return null;
	}

}
