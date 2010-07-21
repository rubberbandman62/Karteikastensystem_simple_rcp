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
 * $Id: EmptyStringValidator.java,v 1.2 2009/06/01 17:19:25 tschindl Exp $
 */
package com.iks.hto.karteikastensystem.simple.rcp.databinding;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.iks.hto.karteikastensystem.simple.rcp.Activator;

/**
 * Validator which ensures that no empty string is set
 */
public class EmptyStringValidator implements IValidator
{
  private String message;

  /**
   * New validator
   * @param message message displayed when validation failed
   */
  public EmptyStringValidator(String message)
  {
    this.message = message;
  }

  public IStatus validate(Object value)
  {
    return value == null || value.toString().trim().length() == 0
      ? new Status(IStatus.ERROR, Activator.PLUGIN_ID, message) : Status.OK_STATUS;
  }

}
