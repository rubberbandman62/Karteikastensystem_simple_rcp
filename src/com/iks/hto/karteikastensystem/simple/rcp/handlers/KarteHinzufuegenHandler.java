package com.iks.hto.karteikastensystem.simple.rcp.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISources;

import com.iks.hto.karteikastensystem.core.IKarteikastenSystemResource;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karte;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karteifachtyp;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karteikasten;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastenSystem;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastensystemFactory;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastensystemPackage;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Sprache;
import com.iks.hto.karteikastensystem.simple.rcp.ResourceProvider;
import com.iks.hto.karteikastensystem.simple.rcp.dialogs.CreateKarteDialog;

public class KarteHinzufuegenHandler extends AbstractHandler implements
		IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEvaluationContext ctx = (IEvaluationContext) event
				.getApplicationContext();
		IKarteikastenSystemResource resource = (IKarteikastenSystemResource) ctx
				.getVariable(ResourceProvider.MODEL_RESOURCE_NAME);
		Karteikasten kk = (Karteikasten) ctx
				.getVariable(ResourceProvider.KARTEIKASTEN_NAME);

		KarteikastenSystem kks = resource.getKarteikastenSystem();

		Shell shell = (Shell) ctx.getVariable(ISources.ACTIVE_SHELL_NAME);

		Karte k = this.createKarte("neue Karte", kk.getVonSprache(), kk
				.getNachSprache());

		Command cmd = AddCommand.create(resource.getEditingDomain(), kks,
				KarteikastensystemPackage.Literals.KARTEIKASTEN_SYSTEM__KARTEN,
				k);

		if (cmd.canExecute()) {
			resource.executeCmd(cmd);
		} else {
			resource.rollback();
			throw new ExecutionException(
					"Could not execute add command to add new karte!");
		}

		if (k != null) {
			CreateKarteDialog dialog = new CreateKarteDialog(shell, resource
					.getEditingDomain(), k, kk.getVonSprache(), kk
					.getNachSprache());
			if (dialog.open() == IDialogConstants.OK_ID) {
				k.setId(k.getSeite(kk.getVonSprache()).getVokabel());
				kk.getFach(Karteifachtyp.NEU).addKarte(k);
				resource.commit();
			} else {
				resource.rollback();
			}
		}

		return null;
	}

	public Karte createKarte(String id, Sprache von, Sprache nach) {
		Karte k = KarteikastensystemFactory.eINSTANCE.createKarte();
		k.setId(id);

		k.addSeite(von);
		k.addSeite(nach);

		return k;
	}

}
