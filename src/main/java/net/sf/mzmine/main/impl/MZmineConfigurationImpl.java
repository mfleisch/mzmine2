/*
 * Copyright 2006-2018 The MZmine 2 Development Team
 * 
 * This file is part of MZmine 2.
 * 
 * MZmine 2 is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * MZmine 2 is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with MZmine 2; if not,
 * write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301
 * USA
 */

package net.sf.mzmine.main.impl;

import net.sf.mzmine.desktop.preferences.MZminePreferences;
import net.sf.mzmine.main.MZmineConfiguration;
import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.modules.MZmineModule;
import net.sf.mzmine.parameters.Parameter;
import net.sf.mzmine.parameters.ParameterSet;
import net.sf.mzmine.parameters.impl.SimpleParameterSet;
import net.sf.mzmine.parameters.parametertypes.EncryptionKeyParameter;
import net.sf.mzmine.parameters.parametertypes.filenames.FileNameListSilentParameter;
import net.sf.mzmine.util.ColorPalettes;
import net.sf.mzmine.util.StringCrypter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * MZmine configuration class
 */
public class MZmineConfigurationImpl implements MZmineConfiguration {

  private final Logger logger = Logger.getLogger(this.getClass().getName());

  private final MZminePreferences preferences;

  // list of last used projects
  private final @Nonnull FileNameListSilentParameter lastProjects;

  private final EncryptionKeyParameter globalEncrypter;

  private final Map<Class<? extends MZmineModule>, ParameterSet> moduleParameters;

  public MZmineConfigurationImpl() {
    moduleParameters = new Hashtable<Class<? extends MZmineModule>, ParameterSet>();
    preferences = new MZminePreferences();
    lastProjects = new FileNameListSilentParameter("Last projets");
    globalEncrypter = new EncryptionKeyParameter();
  }

  @Override
  public StringCrypter getEncrypter() {
    if (globalEncrypter.getValue() == null)
      globalEncrypter.setValue(new StringCrypter());
    return globalEncrypter.getValue();
  }

  @Override
  public ParameterSet getModuleParameters(Class<? extends MZmineModule> moduleClass) {
    ParameterSet parameters = moduleParameters.get(moduleClass);
    if (parameters == null) {
      throw new IllegalArgumentException(
          "Module " + moduleClass + " does not have any parameter set instance");
    }
    return parameters;
  }

  @Override
  public void setModuleParameters(Class<? extends MZmineModule> moduleClass,
      ParameterSet parameters) {
    assert moduleClass != null;
    assert parameters != null;
    MZmineModule moduleInstance = MZmineCore.getModuleInstance(moduleClass);
    Class<? extends ParameterSet> parametersClass = moduleInstance.getParameterSetClass();
    if (!parametersClass.isInstance(parameters)) {
      throw new IllegalArgumentException("Given parameter set is an instance of "
          + parameters.getClass() + " instead of " + parametersClass);
    }
    moduleParameters.put(moduleClass, parameters);

  }

  // color palettes
  @Override
  public ColorPalettes.Vision getColorVision() {
    return preferences.getParameter(MZminePreferences.colorPalettes).getValue();
  }

  // Number formatting functions
  @Override
  public NumberFormat getIntensityFormat() {
    return preferences.getParameter(MZminePreferences.intensityFormat).getValue();
  }

  @Override
  public NumberFormat getMZFormat() {
    return preferences.getParameter(MZminePreferences.mzFormat).getValue();
  }

  @Override
  public NumberFormat getRTFormat() {
    return preferences.getParameter(MZminePreferences.rtFormat).getValue();
  }

  @Override
  public String getRexecPath() {
    File f = preferences.getParameter(MZminePreferences.rExecPath).getValue();
    if (f == null)
      return null;
    else
      return f.getPath();
  }

  @Override
  public Boolean getSendStatistics() {
    return preferences.getParameter(MZminePreferences.sendStatistics).getValue();
  }

  @Override
  public void loadConfiguration(File file) throws IOException {

    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document configuration = dBuilder.parse(file);

      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();

      logger.finest("Loading desktop configuration");

      XPathExpression expr = xpath.compile("//configuration/preferences");
      NodeList nodes = (NodeList) expr.evaluate(configuration, XPathConstants.NODESET);
      if (nodes.getLength() == 1) {
        Element preferencesElement = (Element) nodes.item(0);
        //loading encryption key
        //this has to be read first because following parameters may already contain encrypted data
        //that needs this key for encryption
        if (file.equals(MZmineConfiguration.CONFIG_FILE))
          new SimpleParameterSet(new Parameter[]{globalEncrypter}).loadValuesFromXML(preferencesElement);
        preferences.loadValuesFromXML(preferencesElement);
      }

      logger.finest("Loading last projects");
      expr = xpath.compile("//configuration/lastprojects");
      nodes = (NodeList) expr.evaluate(configuration, XPathConstants.NODESET);
      if (nodes.getLength() == 1) {
        Element lastProjectsElement = (Element) nodes.item(0);
        lastProjects.loadValueFromXML(lastProjectsElement);
      }

      logger.finest("Loading modules configuration");

      for (MZmineModule module : MZmineCore.getAllModules()) {

        String className = module.getClass().getName();
        expr =
            xpath.compile("//configuration/modules/module[@class='" + className + "']/parameters");
        nodes = (NodeList) expr.evaluate(configuration, XPathConstants.NODESET);
        if (nodes.getLength() != 1)
          continue;

        Element moduleElement = (Element) nodes.item(0);

        ParameterSet moduleParameters = getModuleParameters(module.getClass());
        moduleParameters.loadValuesFromXML(moduleElement);
      }

      logger.info("Loaded configuration from file " + file);
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  @Override
  public void saveConfiguration(File file) throws IOException {
    try {
      // write sensitive parameters only to the local config file
      final boolean skipSensitive = !file.equals(MZmineConfiguration.CONFIG_FILE);

      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

      Document configuration = dBuilder.newDocument();
      Element configRoot = configuration.createElement("configuration");
      configuration.appendChild(configRoot);

      Element prefElement = configuration.createElement("preferences");
      configRoot.appendChild(prefElement);
      preferences.setSkipSensitiveParameters(skipSensitive);
      preferences.saveValuesToXML(prefElement);

      Element lastFilesElement = configuration.createElement("lastprojects");
      configRoot.appendChild(lastFilesElement);
      lastProjects.saveValueToXML(lastFilesElement);

      Element modulesElement = configuration.createElement("modules");
      configRoot.appendChild(modulesElement);

      // traverse modules
      for (MZmineModule module : MZmineCore.getAllModules()) {

        String className = module.getClass().getName();

        Element moduleElement = configuration.createElement("module");
        moduleElement.setAttribute("class", className);
        modulesElement.appendChild(moduleElement);

        Element paramElement = configuration.createElement("parameters");
        moduleElement.appendChild(paramElement);

        ParameterSet moduleParameters = getModuleParameters(module.getClass());
        moduleParameters.setSkipSensitiveParameters(skipSensitive);
        moduleParameters.saveValuesToXML(paramElement);
      }

      // save encryption key to local config only
      // ATTENTION: this should to be written after all other configs
      final SimpleParameterSet encSet = new SimpleParameterSet(new Parameter[]{globalEncrypter});
      encSet.setSkipSensitiveParameters(skipSensitive);
      encSet.saveValuesToXML(prefElement);

      TransformerFactory transfac = TransformerFactory.newInstance();
      Transformer transformer = transfac.newTransformer();
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

      // Create parent folder if it does not exist
      File confParent = file.getParentFile();
      if ((confParent != null) && (!confParent.exists())) {
        confParent.mkdirs();
      }

      StreamResult result = new StreamResult(new FileOutputStream(file));
      DOMSource source = new DOMSource(configuration);
      transformer.transform(source, result);

      // make user home config file invisible on windows
      if (!skipSensitive)
        Files.setAttribute(file.toPath(), "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);

      logger.info("Saved configuration to file " + file);
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  @Override
  public MZminePreferences getPreferences() {
    return preferences;
  }

  @Override
  @Nonnull
  public List<File> getLastProjects() {
    return lastProjects.getValue();
  }

  @Override
  @Nonnull
  public FileNameListSilentParameter getLastProjectsParameter() {
    return lastProjects;
  }

}
