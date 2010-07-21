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
 * $Id: PersonParamValues.java,v 1.2 2009/06/01 17:19:26 tschindl Exp $
 */
package com.iks.hto.karteikastensystem.simple.rcp.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.IParameterValues;

/**
 * Possible parameter values
 */
public class BenutzerBearbeitenParamValues implements IParameterValues {
	/**
	 * Parameter value to create a new Benutzer
	 */
	public static final String ACTION_PARAM_VALUE_NEW = "neu";
	/**
	 * Parameter value to edit a Benutzer
	 */
	public static final String ACTION_PARAM_VALUE_EDIT = "bearbeiten";

	public Map<?, ?> getParameterValues() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("Neu", ACTION_PARAM_VALUE_NEW);
		map.put("Bearbeiten", ACTION_PARAM_VALUE_EDIT);
		return map;
	}

}
