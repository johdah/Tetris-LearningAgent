/*
 * @(#)MainBeanInfo.java
 *
 * This work is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This work is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * Copyright (c) 2003 Per Cederberg. All rights reserved.
 */

package tetris;

import java.beans.SimpleBeanInfo;

/**
 * A simple bean info class. This class has been created only to avoid
 * the erroneous HTTP lookup for it when the Tetris game is run as an
 * applet in some browsers.
 * 
 * @version  1.2
 * @author   Per Cederberg, per@percederberg.net
 */
public class MainBeanInfo extends SimpleBeanInfo {

}
