/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Peter Smith
 *******************************************************************************/
package org.boris.winrun4j.winapi;

public interface WinError
{
    int ERROR_SUCCESS = 0;
    int NO_ERROR = 0;
    int ERROR_INSUFFICIENT_BUFFER = 122;
    int ERROR_SERVICE_EXISTS = 1073;
}
