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
 * $Id: ResourceProvider.java,v 1.2 2009/06/01 17:19:26 tschindl Exp $
 */
package com.iks.hto.karteikastensystem.simple.rcp;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

import com.iks.hto.karteikastensystem.core.IKarteikastenSystemResource;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Fach;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karte;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Karteikasten;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Kartenseite;
import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Person;

/**
 * Adds special variables to the application context
 */
public class ResourceProvider extends AbstractSourceProvider {
	/**
	 * The model resource variable
	 */
	public static final String MODEL_RESOURCE_NAME = "modelresource";
	/**
	 * The benutzer variable
	 */
	public static final String BENUTZER_NAME = "benutzer";
	/**
	 * The karteikasten variable
	 */
	public static final String KARTEIKASTEN_NAME = "karteikasten";
	/**
	 * The karte variable
	 */
	public static final String KARTEN_NAME = "karte";
	/**
	 * The fach variable
	 */
	public static final String FACH_NAME = "fach";
	/**
	 * The kartenseite variable
	 */
	public static final String KARTENSEITE_NAME = "kartenseite";

	private HashMap<String, Object> map = new HashMap<String, Object>();

	public void dispose() {
		map = null;
	}

	public Map<?, ?> getCurrentState() {
		return map;
	}

	public String[] getProvidedSourceNames() {
		return new String[] { MODEL_RESOURCE_NAME, BENUTZER_NAME,
				KARTEIKASTEN_NAME, FACH_NAME, KARTEN_NAME, KARTENSEITE_NAME };
	}

	/**
	 * Set the current model resource
	 * 
	 * @param resource
	 *            the resource
	 */
	public void setModelResource(IKarteikastenSystemResource resource) {
		map.put(MODEL_RESOURCE_NAME, resource);
		fireSourceChanged(ISources.ACTIVE_PART, MODEL_RESOURCE_NAME, resource);
	}

	/**
	 * Set the current Benutzer
	 * 
	 * @param benutzer
	 *            the Benutzer
	 */
	public void setBenutzer(Person benutzer) {
		map.put(BENUTZER_NAME, benutzer);
		fireSourceChanged(ISources.ACTIVE_PART, BENUTZER_NAME, benutzer);
	}

	/**
	 * Set the current karteikasten
	 * 
	 * @param karteikasten
	 *            the karteikasten
	 */
	public void setKarteikasten(Karteikasten karteikasten) {
		map.put(KARTEIKASTEN_NAME, karteikasten);
		fireSourceChanged(ISources.ACTIVE_PART, KARTEIKASTEN_NAME, karteikasten);
	}

	/**
	 * Set the current fach
	 * 
	 * @param fach
	 *            the fach
	 */
	public void setFach(Fach fach) {
		map.put(FACH_NAME, fach);
		fireSourceChanged(ISources.ACTIVE_PART, FACH_NAME, fach);
	}

	/**
	 * Set the current karte
	 * 
	 * @param karte
	 *            the karte
	 */
	public void setKarte(Karte karte) {
		map.put(KARTEN_NAME, karte);
		fireSourceChanged(ISources.ACTIVE_PART, KARTEN_NAME, karte);
	}

	/**
	 * Set the current kartenseite
	 * 
	 * @param karte
	 *            the kartenseite
	 */
	public void setKartenseite(Kartenseite kartenseite) {
		map.put(KARTENSEITE_NAME, kartenseite);
		fireSourceChanged(ISources.ACTIVE_PART, KARTENSEITE_NAME, kartenseite);
	}
	
	public Object getResource(String resource_name) {
		return map.get(resource_name);
	}
}