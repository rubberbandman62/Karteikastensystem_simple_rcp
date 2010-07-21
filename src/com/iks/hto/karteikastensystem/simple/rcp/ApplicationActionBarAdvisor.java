package com.iks.hto.karteikastensystem.simple.rcp;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.
	private IWorkbenchAction exit;
	private IWorkbenchAction save;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(final IWorkbenchWindow window) {
		// Creates the actions and registers them.
		// Registering is needed to ensure that key bindings work.
		// The corresponding commands keybindings are defined in the plugin.xml
		// file.
		// Registering also provides automatic disposal of the actions when
		// the window is closed.

		save = ActionFactory.SAVE.create(window);
		register(save);
		
		exit = ActionFactory.QUIT.create(window);
		register(exit);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
	    MenuManager file = new MenuManager("&Datei", "datei");

	    MenuManager subnew = new MenuManager("&Neu", "neu");
	    subnew.add(new Separator("after_resource"));

	    file.add(subnew);
	    file.add(new Separator());
	    file.add(exit);
	    file.add(new Separator());
	    file.add(save);

	    menuBar.add(file);
	}

}
