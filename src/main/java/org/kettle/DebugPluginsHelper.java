/*! ******************************************************************************
 *
 * Pentaho Data Integration
 *
 * Copyright (C) 2002-2017 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.kettle;

import org.apache.commons.lang.StringUtils;
import org.kettle.util.Defaults;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.gui.SpoonFactory;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.ui.core.dialog.EnterSelectionDialog;
import org.pentaho.di.ui.spoon.ISpoonMenuController;
import org.pentaho.di.ui.spoon.Spoon;
import org.pentaho.di.ui.spoon.trans.TransGraph;
import org.pentaho.ui.xul.dom.Document;
import org.pentaho.ui.xul.impl.AbstractXulEventHandler;

import java.util.HashMap;
import java.util.Map;

public class DebugPluginsHelper extends AbstractXulEventHandler implements ISpoonMenuController {
  protected static Class<?> PKG = DebugPluginsHelper.class; // for i18n

  private static DebugPluginsHelper instance = null;

  private DebugPluginsHelper() {
  }

  public static DebugPluginsHelper getInstance() {
    if ( instance == null ) {
      instance = new DebugPluginsHelper();
      Spoon spoon = ( (Spoon) SpoonFactory.getInstance() );
      spoon.addSpoonMenuController( instance );
    }
    return instance;
  }

  public String getName() {
    return "debugHelper";
  }

  public void updateMenu( Document doc ) {
    // Nothing so far.
  }

  public void setStepLoggingLevel() {
    setClearLogLevel(true);
  }

  public void clearStepLoggingLevel() {
    setClearLogLevel( false );
  }

  private void setClearLogLevel(boolean set) {
    Spoon spoon = ( (Spoon) SpoonFactory.getInstance() );
    TransGraph transGraph = spoon.getActiveTransGraph();
    TransMeta transMeta = spoon.getActiveTransformation();
    StepMeta stepMeta = transGraph.getCurrentStep();
    if ( transGraph == null || transMeta == null || stepMeta == null ) {
      return;
    }

    Map<String, Map<String, String>> attributesMap = transMeta.getAttributesMap();
    Map<String, String> debugGroupAttributesMap = attributesMap.get( Defaults.TRANSMETA_DEBUG_GROUP );

    if (debugGroupAttributesMap==null) {
      debugGroupAttributesMap = new HashMap<>();
      attributesMap.put(Defaults.TRANSMETA_DEBUG_GROUP, debugGroupAttributesMap);
    }

    if (!set) {
      debugGroupAttributesMap.remove( stepMeta.getName() );
      transMeta.setChanged();
      spoon.refreshGraph();
      return;
    }

    String previousLogLevelCode = debugGroupAttributesMap.get( stepMeta.getName() );
    LogLevel previousLogLevel = null;
    if ( StringUtils.isNotEmpty(previousLogLevelCode)) {
      previousLogLevel = LogLevel.getLogLevelForCode( previousLogLevelCode );
    }

    String[] descriptions = LogLevel.getLogLevelDescriptions();
    EnterSelectionDialog dialog = new EnterSelectionDialog( spoon.getShell(), descriptions, "Select logging level", "Select logging level" );
    String levelDescription;
    if ( previousLogLevel != null ) {
      levelDescription = dialog.open(  );
    } else {
      levelDescription = dialog.open();
    }
    if (levelDescription==null) {
      return;
    }
    LogLevel stepLogLevel = LogLevel.values()[ Const.indexOfString(levelDescription, LogLevel.getLogLevelDescriptions() ) ];
    debugGroupAttributesMap.put( stepMeta.getName(), stepLogLevel.getCode() );
    transMeta.setChanged();
    spoon.refreshGraph();
  }

  public void clearAllLogging() {
    Spoon spoon = ( (Spoon) SpoonFactory.getInstance() );
    TransGraph transGraph = spoon.getActiveTransGraph();
    TransMeta transMeta = spoon.getActiveTransformation();
    StepMeta stepMeta = transGraph.getCurrentStep();
    if ( transGraph == null || transMeta == null || stepMeta == null ) {
      return;
    }
    Map<String, Map<String, String>> attributesMap = transMeta.getAttributesMap();
    attributesMap.remove( Defaults.TRANSMETA_DEBUG_GROUP );
    transMeta.setChanged();
    spoon.refreshGraph();
  }
}