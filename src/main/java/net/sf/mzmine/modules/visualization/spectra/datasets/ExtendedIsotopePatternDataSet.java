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

package net.sf.mzmine.modules.visualization.spectra.datasets;

import java.util.ArrayList;
import java.util.List;
import org.jfree.data.xy.IntervalXYDelegate;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import io.github.msdk.MSDKRuntimeException;
import net.sf.mzmine.datamodel.DataPoint;
import net.sf.mzmine.datamodel.IsotopePattern;
import net.sf.mzmine.datamodel.impl.SimpleIsotopePattern;
import net.sf.mzmine.modules.peaklistmethods.isotopes.isotopepeakscanner.ExtendedIsotopePattern;

/**
 * 
 * Dataset that can contain an ExtendedIsotopePattern and convert it so it can be accessed by an bar
 * renderer. This also contains the description of the datapoints so they can be displayed as a
 * tooltip in the preview chart. The tooltips can be generated by SpectraToolTipGenerator.
 * Furthermore, a minimum intensity is specified and the peaks are sorted into two different
 * XYSeries to allow a color differentiation if the relative intensities are above or below the
 * given threshold.
 * 
 * @author Steffen Heuckeroth steffen.heuckeroth@gmx.de / s_heuc03@uni-muenster.de
 *
 */
public class ExtendedIsotopePatternDataSet extends XYSeriesCollection {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private SimpleIsotopePattern pattern;
  private double minIntensity;
  private DataPoint[] dp;
  private XYSeries above;
  private XYSeries below;
  private double width;
  private List<String> descrBelow, descrAbove;
  private IntervalXYDelegate intervalDelegate;

  private enum AB {
    ABOVE, BELOW
  };

  Assignment assignment[];

  private class Assignment {
    AB ab;
    private int id;
  }

  /**
   * 
   * @param pattern ExtendedIsotopePattern to generate the dataset from
   * @param minIntensity minimum intensity (0.0-1.0) threshold for color differentiation
   * @param width Width of the datapoints to be displayed. Used for merging the peaks.
   */
  public ExtendedIsotopePatternDataSet(SimpleIsotopePattern pattern, double minIntensity,
      double width) {
    // super(pattern.getDescription(), pattern.getDataPoints());
    this.pattern = pattern;
    this.setMinIntensity(minIntensity);
    above = new XYSeries("Above minimum intensity");
    below = new XYSeries("Below minimum intensity");
    descrBelow = new ArrayList<String>();
    descrAbove = new ArrayList<String>();

    dp = pattern.getDataPoints();
    assignment = new Assignment[dp.length];
    for (int i = 0; i < assignment.length; i++)
      assignment[i] = new Assignment();

    for (int i = 0; i < dp.length; i++) {
      if (dp[i].getIntensity() < minIntensity) {
        assignment[i].ab = AB.BELOW;
        assignment[i].id = i;
        below.add(dp[i].getMZ(), dp[i].getIntensity());
        descrBelow.add(pattern.getIsotopeComposition(i));
      } else {
        assignment[i].ab = AB.ABOVE;
        assignment[i].id = i;
        above.add(dp[i].getMZ(), dp[i].getIntensity());
        descrAbove.add(pattern.getIsotopeComposition(i));
      }
    }

    this.intervalDelegate = new IntervalXYDelegate(this);
    this.intervalDelegate.setFixedIntervalWidth(width);
    super.addSeries(above);
    super.addSeries(below);
  }

  public String getItemDescription(int series, int item) {
    if (series == 0 && item < descrAbove.size())
      return descrAbove.get(item);
    if (series == 1 && item < descrBelow.size())
      return descrBelow.get(item);

    return "Invalid series/index";

  }

  // the next two methods are not needed yet but might come in handy in the future
  public int getSeriesDpIndex(int peakIndex) throws MSDKRuntimeException {
    if (peakIndex > dp.length)
      throw new MSDKRuntimeException("Index out of bounds.");
    return assignment[peakIndex].id;
  }

  public AB getSeriesAB(int peakIndex) throws MSDKRuntimeException {
    if (peakIndex > dp.length)
      throw new MSDKRuntimeException("Index out of bounds.");
    return assignment[peakIndex].ab;
  }

  public IsotopePattern getIsotopePattern() {
    return pattern;
  }

  public XYBarDataset getBarDataset(double mergeWidth) {

    return new XYBarDataset((XYSeriesCollection) this, mergeWidth);
  }

  public double getWidth() {
    return width;
  }

  public void setWidth(double width) {
    this.width = width;
    this.intervalDelegate.setFixedIntervalWidth(width);
  }

  /**
   * By redefining this, the pattern is displayed as an actual bar instead of a line
   */
  @Override
  public double getStartXValue(int series, int item) {
    return intervalDelegate.getStartXValue(series, item);
  }

  /**
   * By redefining this, the pattern is displayed as an actual bar instead of a line
   */
  @Override
  public double getEndXValue(int series, int item) {
    return intervalDelegate.getEndXValue(series, item);
  }

  public double getMinIntensity() {
    return minIntensity;
  }

  public void setMinIntensity(double minIntensity) {
    this.minIntensity = minIntensity;
  }
}
