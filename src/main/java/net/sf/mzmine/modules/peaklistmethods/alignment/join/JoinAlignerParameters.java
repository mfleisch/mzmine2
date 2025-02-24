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

package net.sf.mzmine.modules.peaklistmethods.alignment.join;

import net.sf.mzmine.modules.peaklistmethods.isotopes.isotopepatternscore.IsotopePatternScoreParameters;
import net.sf.mzmine.parameters.Parameter;
import net.sf.mzmine.parameters.impl.SimpleParameterSet;
import net.sf.mzmine.parameters.parametertypes.BooleanParameter;
import net.sf.mzmine.parameters.parametertypes.DoubleParameter;
import net.sf.mzmine.parameters.parametertypes.StringParameter;
import net.sf.mzmine.parameters.parametertypes.selectors.PeakListsParameter;
import net.sf.mzmine.parameters.parametertypes.submodules.OptionalModuleParameter;
import net.sf.mzmine.parameters.parametertypes.tolerances.MZToleranceParameter;
import net.sf.mzmine.parameters.parametertypes.tolerances.RTToleranceParameter;

public class JoinAlignerParameters extends SimpleParameterSet {

  public static final PeakListsParameter peakLists = new PeakListsParameter();

  public static final StringParameter peakListName =
      new StringParameter("Feature list name", "Feature list name", "Aligned feature list");

  public static final MZToleranceParameter MZTolerance = new MZToleranceParameter();

  public static final DoubleParameter MZWeight =
      new DoubleParameter("Weight for m/z", "Score for perfectly matching m/z values");

  public static final RTToleranceParameter RTTolerance = new RTToleranceParameter();

  public static final DoubleParameter RTWeight =
      new DoubleParameter("Weight for RT", "Score for perfectly matching RT values");

  public static final BooleanParameter SameChargeRequired = new BooleanParameter(
      "Require same charge state", "If checked, only rows having same charge state can be aligned");

  public static final BooleanParameter SameIDRequired = new BooleanParameter("Require same ID",
      "If checked, only rows having same compound identities (or no identities) can be aligned");

  public static final OptionalModuleParameter compareIsotopePattern =
      new OptionalModuleParameter("Compare isotope pattern",
          "If both peaks represent an isotope pattern, add isotope pattern score to match score",
          new IsotopePatternScoreParameters());

  public JoinAlignerParameters() {
    super(new Parameter[] {peakLists, peakListName, MZTolerance, MZWeight, RTTolerance, RTWeight,
        SameChargeRequired, SameIDRequired, compareIsotopePattern});
  }

}
