/**
 * <copyright>
 *
 * Copyright (c) 2009 Bestsolution.at and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: 
 *   Tom Schindl<tom.schindl@bestsolution.at> - Initial API and implementation
 *
 * </copyright>
 *
 * $Id: PersonEditingHandler.java,v 1.2 2009/06/01 17:19:26 tschindl Exp $
 */
package com.iks.hto.karteikastensystem.simple.rcp.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISources;

import com.iks.hto.karteikastensystem.core.IKarteikastenSystemResource;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastensystemFactory;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.KarteikastensystemPackage;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Person;
import com.iks.hto.karteikastensystem.simple.rcp.Activator;
import com.iks.hto.karteikastensystem.simple.rcp.ResourceProvider;
import com.iks.hto.karteikastensystem.simple.rcp.dialogs.BenutzerDialog;

/**
 * Handles the editing/creation of persons
 */
public class BenutzerBearbeitenHandler extends AbstractHandler {
	/**
	 * Parameter passed to switch between creation/editing
	 */
	public static final String ACTION_PARAM = Activator.PLUGIN_ID
			+ ".benutzer.action";

	public Object execute(ExecutionEvent event) throws ExecutionException {

		IEvaluationContext ctx = (IEvaluationContext) event
				.getApplicationContext();
		IKarteikastenSystemResource resource = (IKarteikastenSystemResource) ctx
				.getVariable(ResourceProvider.MODEL_RESOURCE_NAME);
		Shell shell = (Shell) ctx
				.getVariable(ISources.ACTIVE_WORKBENCH_WINDOW_SHELL_NAME);

		String action = event.getParameter(ACTION_PARAM);

		if (action != null) {
			if (action.equals(BenutzerBearbeitenParamValues.ACTION_PARAM_VALUE_EDIT)
					|| action.equals(BenutzerBearbeitenParamValues.ACTION_PARAM_VALUE_NEW)) {
				IStatus s = resource.setSavePoint();
				if (s.isOK()) {
					Person p;

					if (action.equals(BenutzerBearbeitenParamValues.ACTION_PARAM_VALUE_NEW)) {
						System.out.println("add a user to: " + resource.toString());
						p = KarteikastensystemFactory.eINSTANCE.createPerson();
						Command cmd = AddCommand
								.create(
										resource.getEditingDomain(),
										resource.getKarteikastenSystem(),
										KarteikastensystemPackage.Literals.KARTEIKASTEN_SYSTEM__BENUTZER,
										p);
						if (cmd.canExecute()) {
							resource.executeCmd(cmd);
						} else {
							resource.rollback();
							throw new ExecutionException(
									"Could not execute add command");
						}
					} else {
						System.out.println("change a user of: " + resource.toString());
						p = (Person) ctx
								.getVariable(ResourceProvider.BENUTZER_NAME);
					}

					if (p != null) {
						BenutzerDialog dialog = new BenutzerDialog(shell,
								resource.getEditingDomain(), p);
						if (dialog.open() == IDialogConstants.OK_ID) {
							resource.commit();
						} else {
							resource.rollback();
						}
					}
				} else {
					throw new ExecutionException("Could not set a save point");
				}
			}
		}

		return null;
	}
}