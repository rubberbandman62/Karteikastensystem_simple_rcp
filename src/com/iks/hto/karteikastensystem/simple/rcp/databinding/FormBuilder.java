/**
 * <copyright>
 *
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id: FormBuilder.java,v 1.1 2009/06/06 16:04:12 tschindl Exp $
 */
package com.iks.hto.karteikastensystem.simple.rcp.databinding;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.jface.databinding.swt.IWidgetValueProperty;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.internal.databinding.swt.ComboTextProperty;
import org.eclipse.jface.internal.databinding.swt.WidgetTextProperty;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.iks.hto.karteikastensystem.core.model.karteikastensystem.Kartenseite;

/**
 * Helper class which builds a two column form with labels and text-fields
 * 
 * @param <P>
 *            the value property type
 */
public class FormBuilder<P extends IValueProperty> {
	public static String TEXT_TYPE = "text";
	public static String COMBO_TYPE = "combo";
	public static String ONLY_LABEL_TYPE = "label";

	private class Entry {
		private String type;
		private String label;
		private P property;
		private String[] options;
		private String nullMessage;
		private Object bindTo;

		private Entry(String label, P property, String type,
				String nullMessage, Object bindTo) {
			this.label = label;
			this.property = property;
			this.type = type;
			this.options = null;
			this.nullMessage = nullMessage;
			this.bindTo = bindTo;
		}

		public Entry(String label, P property, String type, String[] options,
				String nullMessage, Object bindTo) {
			this.label = label;
			this.property = property;
			this.type = type;
			this.options = options;
			this.nullMessage = nullMessage;
			this.bindTo = bindTo;
		}

		public Entry(String label) {
			this.label = label;
			this.property = null;
			this.type = ONLY_LABEL_TYPE;
			this.options = null;
			this.nullMessage = null;
			this.bindTo = null;
		}
	}

	private List<Entry> entries = new ArrayList<Entry>();

	/**
	 * Add a text entry
	 * 
	 * @param label
	 *            the label to display
	 * @param property
	 *            the property to bind
	 * @param nullMessage
	 *            the message shown when the property gets set to null
	 */
	public void addTextEntry(String label, P property, String nullMessage,
			Object bindTo) {
		entries.add(new Entry(label, property, TEXT_TYPE, nullMessage, bindTo));
	}

	/**
	 * Add a combo entry
	 * 
	 * @param label
	 *            the label to display
	 * @param property
	 *            the property to bind
	 * @param options
	 *            the valid options for this combo box
	 * @param nullMessage
	 *            the message shown when the property gets set to null
	 */
	public void addComboEntry(String label, P property, String[] options,
			String nullMessage, Object bindTo) {
		entries.add(new Entry(label, property, COMBO_TYPE, options,
				nullMessage, bindTo));
	}

	/**
	 * Add a label entry
	 * 
	 * @param label
	 *            the label to display
	 */
	public void addLabel(String label) {
		entries.add(new Entry(label));
	}

	/**
	 * Build a two column form with the elements added
	 * 
	 * @param dbc
	 *            the databinding context
	 * @param parent
	 *            the parent the form is created on
	 * @param object
	 *            the object to bind
	 * @param nachSeite
	 * @return the form container
	 */
	public Composite build(DataBindingContext dbc, Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));

		IWidgetValueProperty textProp = WidgetProperties.text(SWT.Modify);
		IWidgetValueProperty comboProp = new ComboTextProperty();

		for (Entry e : entries) {

			Label l = new Label(container, SWT.NONE);
			l.setText(e.label);

			IObservableValue uiObs = null;
			if (e.type.equals(TEXT_TYPE)) {
				Text t = new Text(container, SWT.BORDER);
				t.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

				uiObs = textProp.observeDelayed(400, t);

			} else if (e.type.equals(COMBO_TYPE)) {
				Combo cb = new Combo(container, SWT.DROP_DOWN);
				cb.setItems(e.options);
				cb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

				uiObs = comboProp.observeDelayed(400, cb);
			} else if (e.type.equals(ONLY_LABEL_TYPE)) {
				Label dummy = new Label(container, SWT.NONE);
				dummy.setText("");
			}

			IObservableValue mObs;

			if (e.bindTo != null) {
				if (e.bindTo instanceof IObservableValue) {
					mObs = e.property
							.observeDetail((IObservableValue) e.bindTo);
				} else {
					mObs = e.property.observe(e.bindTo);
				}

				// TODO: Hier findet das Binding statt!!! Inkl. Update-strategie
				// und
				// Validation
				dbc.bindValue(uiObs, mObs, new EMFUpdateValueStrategy()
						.setBeforeSetValidator(new EmptyStringValidator(
								e.nullMessage)), null);
			}
		}

		return container;
	}
}