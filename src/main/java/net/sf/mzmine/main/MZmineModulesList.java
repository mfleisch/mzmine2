/*
 * Copyright 2006-2015 The MZmine 2 Development Team
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

package net.sf.mzmine.main;

import net.sf.mzmine.modules.batchmode.BatchModeModule;
import net.sf.mzmine.modules.masslistmethods.ADAPchromatogrambuilder.ADAPChromatogramBuilderModule;
import net.sf.mzmine.modules.masslistmethods.chromatogrambuilder.ChromatogramBuilderModule;
import net.sf.mzmine.modules.masslistmethods.shoulderpeaksfilter.ShoulderPeaksFilterModule;
import net.sf.mzmine.modules.peaklistmethods.alignment.adap3.ADAP3AlignerModule;
import net.sf.mzmine.modules.peaklistmethods.alignment.hierarchical.HierarAlignerGcModule;
import net.sf.mzmine.modules.peaklistmethods.alignment.join.JoinAlignerModule;
import net.sf.mzmine.modules.peaklistmethods.alignment.ransac.RansacAlignerModule;
import net.sf.mzmine.modules.peaklistmethods.dataanalysis.clustering.ClusteringModule;
import net.sf.mzmine.modules.peaklistmethods.dataanalysis.heatmaps.HeatMapModule;
import net.sf.mzmine.modules.peaklistmethods.dataanalysis.projectionplots.CDAPlotModule;
import net.sf.mzmine.modules.peaklistmethods.dataanalysis.projectionplots.PCAPlotModule;
import net.sf.mzmine.modules.peaklistmethods.dataanalysis.projectionplots.SammonsPlotModule;
import net.sf.mzmine.modules.peaklistmethods.dataanalysis.rtmzplots.cvplot.CVPlotModule;
import net.sf.mzmine.modules.peaklistmethods.dataanalysis.rtmzplots.logratioplot.LogratioPlotModule;
import net.sf.mzmine.modules.peaklistmethods.dataanalysis.significance.SignificanceModule;
import net.sf.mzmine.modules.peaklistmethods.filtering.clearannotations.PeaklistClearAnnotationsModule;
import net.sf.mzmine.modules.peaklistmethods.filtering.duplicatefilter.DuplicateFilterModule;
import net.sf.mzmine.modules.peaklistmethods.filtering.groupms2.GroupMS2Module;
import net.sf.mzmine.modules.peaklistmethods.filtering.neutralloss.NeutralLossFilterModule;
import net.sf.mzmine.modules.peaklistmethods.filtering.peakcomparisonrowfilter.PeakComparisonRowFilterModule;
import net.sf.mzmine.modules.peaklistmethods.filtering.peakfilter.PeakFilterModule;
import net.sf.mzmine.modules.peaklistmethods.filtering.rowsfilter.RowsFilterModule;
import net.sf.mzmine.modules.peaklistmethods.gapfilling.peakfinder.PeakFinderModule;
import net.sf.mzmine.modules.peaklistmethods.gapfilling.peakfinder.multithreaded.MultiThreadPeakFinderModule;
import net.sf.mzmine.modules.peaklistmethods.gapfilling.samerange.SameRangeGapFillerModule;
import net.sf.mzmine.modules.peaklistmethods.identification.adductsearch.AdductSearchModule;
import net.sf.mzmine.modules.peaklistmethods.identification.camera.CameraSearchModule;
import net.sf.mzmine.modules.peaklistmethods.identification.complexsearch.ComplexSearchModule;
import net.sf.mzmine.modules.peaklistmethods.identification.customdbsearch.CustomDBSearchModule;
import net.sf.mzmine.modules.peaklistmethods.identification.formulaprediction.FormulaPredictionModule;
import net.sf.mzmine.modules.peaklistmethods.identification.formulapredictionpeaklist.FormulaPredictionPeakListModule;
import net.sf.mzmine.modules.peaklistmethods.identification.fragmentsearch.FragmentSearchModule;
import net.sf.mzmine.modules.peaklistmethods.identification.gnpsresultsimport.GNPSResultsImportModule;
import net.sf.mzmine.modules.peaklistmethods.identification.lipididentification.LipidSearchModule;
import net.sf.mzmine.modules.peaklistmethods.identification.ms2search.Ms2SearchModule;
import net.sf.mzmine.modules.peaklistmethods.identification.nist.NistMsSearchModule;
import net.sf.mzmine.modules.peaklistmethods.identification.onlinedbsearch.OnlineDBSearchModule;
import net.sf.mzmine.modules.peaklistmethods.identification.precursordbsearch.PrecursorDBSearchModule;
import net.sf.mzmine.modules.peaklistmethods.identification.sirius.SiriusProcessingModule;
import net.sf.mzmine.modules.peaklistmethods.identification.spectraldbsearch.LocalSpectralDBSearchModule;
import net.sf.mzmine.modules.peaklistmethods.identification.spectraldbsearch.sort.SortSpectralDBIdentitiesModule;
import net.sf.mzmine.modules.peaklistmethods.io.csvexport.CSVExportModule;
import net.sf.mzmine.modules.peaklistmethods.io.gnpsexport.GNPSExportAndSubmitModule;
import net.sf.mzmine.modules.peaklistmethods.io.metaboanalystexport.MetaboAnalystExportModule;
import net.sf.mzmine.modules.peaklistmethods.io.mgfexport.MGFExportModule;
import net.sf.mzmine.modules.peaklistmethods.io.mspexport.MSPExportModule;
import net.sf.mzmine.modules.peaklistmethods.io.mztabexport.MzTabExportModule;
import net.sf.mzmine.modules.peaklistmethods.io.mztabimport.MzTabImportModule;
import net.sf.mzmine.modules.peaklistmethods.io.siriusexport.SiriusExportModule;
import net.sf.mzmine.modules.peaklistmethods.io.spectraldbsubmit.LibrarySubmitModule;
import net.sf.mzmine.modules.peaklistmethods.io.sqlexport.SQLExportModule;
import net.sf.mzmine.modules.peaklistmethods.io.xmlexport.XMLExportModule;
import net.sf.mzmine.modules.peaklistmethods.io.xmlimport.XMLImportModule;
import net.sf.mzmine.modules.peaklistmethods.isotopes.deisotoper.IsotopeGrouperModule;
import net.sf.mzmine.modules.peaklistmethods.isotopes.isotopepeakscanner.IsotopePeakScannerModule;
import net.sf.mzmine.modules.peaklistmethods.isotopes.isotopeprediction.IsotopePatternCalculator;
import net.sf.mzmine.modules.peaklistmethods.normalization.linear.LinearNormalizerModule;
import net.sf.mzmine.modules.peaklistmethods.normalization.rtcalibration.RTCalibrationModule;
import net.sf.mzmine.modules.peaklistmethods.normalization.standardcompound.StandardCompoundNormalizerModule;
import net.sf.mzmine.modules.peaklistmethods.peakpicking.adap3decompositionV1_5.ADAP3DecompositionV1_5Module;
import net.sf.mzmine.modules.peaklistmethods.peakpicking.adap3decompositionV2.ADAP3DecompositionV2Module;
import net.sf.mzmine.modules.peaklistmethods.peakpicking.deconvolution.DeconvolutionModule;
import net.sf.mzmine.modules.peaklistmethods.peakpicking.peakextender.PeakExtenderModule;
import net.sf.mzmine.modules.peaklistmethods.peakpicking.shapemodeler.ShapeModelerModule;
import net.sf.mzmine.modules.peaklistmethods.peakpicking.smoothing.SmoothingModule;
import net.sf.mzmine.modules.peaklistmethods.sortpeaklists.SortPeakListsModule;
import net.sf.mzmine.modules.projectmethods.projectclose.ProjectCloseModule;
import net.sf.mzmine.modules.projectmethods.projectload.ProjectLoadModule;
import net.sf.mzmine.modules.projectmethods.projectsave.ProjectSaveAsModule;
import net.sf.mzmine.modules.projectmethods.projectsave.ProjectSaveModule;
import net.sf.mzmine.modules.rawdatamethods.exportscans.ExportScansFromRawFilesModule;
import net.sf.mzmine.modules.rawdatamethods.exportscans.ExportScansModule;
import net.sf.mzmine.modules.rawdatamethods.extractscans.ExtractScansModule;
import net.sf.mzmine.modules.rawdatamethods.filtering.alignscans.AlignScansModule;
import net.sf.mzmine.modules.rawdatamethods.filtering.baselinecorrection.BaselineCorrectionModule;
import net.sf.mzmine.modules.rawdatamethods.filtering.cropper.CropFilterModule;
import net.sf.mzmine.modules.rawdatamethods.filtering.scanfilters.ScanFiltersModule;
import net.sf.mzmine.modules.rawdatamethods.filtering.scansmoothing.ScanSmoothingModule;
import net.sf.mzmine.modules.rawdatamethods.merge.RawFileMergeModule;
import net.sf.mzmine.modules.rawdatamethods.peakpicking.gridmass.GridMassModule;
import net.sf.mzmine.modules.rawdatamethods.peakpicking.manual.ManualPeakPickerModule;
import net.sf.mzmine.modules.rawdatamethods.peakpicking.massdetection.MassDetectionModule;
import net.sf.mzmine.modules.rawdatamethods.peakpicking.msms.MsMsPeakPickerModule;
import net.sf.mzmine.modules.rawdatamethods.peakpicking.targetedpeakdetection.TargetedPeakDetectionModule;
import net.sf.mzmine.modules.rawdatamethods.rawdataexport.RawDataExportModule;
import net.sf.mzmine.modules.rawdatamethods.rawdataimport.RawDataImportModule;
import net.sf.mzmine.modules.rawdatamethods.sortdatafiles.SortDataFilesModule;
import net.sf.mzmine.modules.tools.isotopepatternpreview.IsotopePatternPreviewModule;
import net.sf.mzmine.modules.tools.msmsspectramerge.MsMsSpectraMergeModule;
import net.sf.mzmine.modules.tools.mzrangecalculator.MzRangeFormulaCalculatorModule;
import net.sf.mzmine.modules.tools.mzrangecalculator.MzRangeMassCalculatorModule;
import net.sf.mzmine.modules.visualization.fx3d.Fx3DVisualizerModule;
import net.sf.mzmine.modules.visualization.histogram.HistogramVisualizerModule;
import net.sf.mzmine.modules.visualization.infovisualizer.InfoVisualizerModule;
import net.sf.mzmine.modules.visualization.intensityplot.IntensityPlotModule;
import net.sf.mzmine.modules.visualization.kendrickmassplot.KendrickMassPlotModule;
import net.sf.mzmine.modules.visualization.mzhistogram.MZDistributionHistoModule;
import net.sf.mzmine.modules.visualization.neutralloss.NeutralLossVisualizerModule;
import net.sf.mzmine.modules.visualization.peaklisttable.PeakListTableModule;
import net.sf.mzmine.modules.visualization.peaklisttable.export.IsotopePatternExportModule;
import net.sf.mzmine.modules.visualization.peaklisttable.export.MSMSExportModule;
import net.sf.mzmine.modules.visualization.productionfilter.ProductIonFilterVisualizerModule;
import net.sf.mzmine.modules.visualization.scatterplot.ScatterPlotVisualizerModule;
import net.sf.mzmine.modules.visualization.spectra.msms.MsMsVisualizerModule;
import net.sf.mzmine.modules.visualization.spectra.simplespectra.SpectraVisualizerModule;
import net.sf.mzmine.modules.visualization.spectra.simplespectra.datapointprocessing.DataPointProcessingManager;
import net.sf.mzmine.modules.visualization.spectra.simplespectra.datapointprocessing.identification.sumformulaprediction.DPPSumFormulaPredictionModule;
import net.sf.mzmine.modules.visualization.spectra.simplespectra.datapointprocessing.isotopes.deisotoper.DPPIsotopeGrouperModule;
import net.sf.mzmine.modules.visualization.spectra.simplespectra.datapointprocessing.massdetection.DPPMassDetectionModule;
import net.sf.mzmine.modules.visualization.spectra.simplespectra.spectraidentification.customdatabase.CustomDBSpectraSearchModule;
import net.sf.mzmine.modules.visualization.spectra.simplespectra.spectraidentification.lipidsearch.LipidSpectraSearchModule;
import net.sf.mzmine.modules.visualization.spectra.simplespectra.spectraidentification.onlinedatabase.OnlineDBSpectraSearchModule;
import net.sf.mzmine.modules.visualization.spectra.simplespectra.spectraidentification.spectraldatabase.SpectraIdentificationSpectralDatabaseModule;
import net.sf.mzmine.modules.visualization.spectra.simplespectra.spectraidentification.sumformula.SumFormulaSpectraSearchModule;
import net.sf.mzmine.modules.visualization.spectra.spectralmatchresults.SpectraIdentificationResultsModule;
import net.sf.mzmine.modules.visualization.tic.TICVisualizerModule;
import net.sf.mzmine.modules.visualization.twod.TwoDVisualizerModule;
import net.sf.mzmine.modules.visualization.vankrevelendiagram.VanKrevelenDiagramModule;

/**
 * List of modules included in MZmine 2
 */
public class MZmineModulesList {

  public static final Class<?> MODULES[] = new Class<?>[] {

      // Project methods
      ProjectLoadModule.class, ProjectSaveModule.class, ProjectSaveAsModule.class,
      ProjectCloseModule.class,

      // Batch mode
      BatchModeModule.class,

      // Raw data methods
      RawDataImportModule.class, RawDataExportModule.class, ExportScansFromRawFilesModule.class,
      RawFileMergeModule.class, ExtractScansModule.class, MassDetectionModule.class,
      ShoulderPeaksFilterModule.class, ChromatogramBuilderModule.class,
      ADAPChromatogramBuilderModule.class,
      // Not ready for prime time: ADAP3DModule.class,
      GridMassModule.class, ManualPeakPickerModule.class, MsMsPeakPickerModule.class,
      ScanFiltersModule.class, CropFilterModule.class, BaselineCorrectionModule.class,
      AlignScansModule.class, ScanSmoothingModule.class, SortDataFilesModule.class,

      // Alignment
      SortPeakListsModule.class, JoinAlignerModule.class, HierarAlignerGcModule.class,

      RansacAlignerModule.class, ADAP3AlignerModule.class,
      // PathAlignerModule.class,

      // I/O
      CSVExportModule.class, MetaboAnalystExportModule.class, MzTabExportModule.class,
      SQLExportModule.class, XMLExportModule.class, MzTabImportModule.class, XMLImportModule.class,
      MSPExportModule.class, MGFExportModule.class, GNPSExportAndSubmitModule.class,
      SiriusExportModule.class,

      // Gap filling
      PeakFinderModule.class, MultiThreadPeakFinderModule.class, SameRangeGapFillerModule.class,

      // Isotopes
      IsotopeGrouperModule.class, IsotopePatternCalculator.class, IsotopePeakScannerModule.class,

      // Feature detection
      SmoothingModule.class, DeconvolutionModule.class, ShapeModelerModule.class,
      PeakExtenderModule.class, TargetedPeakDetectionModule.class,
      ADAP3DecompositionV1_5Module.class, ADAP3DecompositionV2Module.class,

      // Feature list filtering
      GroupMS2Module.class, DuplicateFilterModule.class, RowsFilterModule.class,
      PeakComparisonRowFilterModule.class, PeakFilterModule.class,
      PeaklistClearAnnotationsModule.class, NeutralLossFilterModule.class,

      // Normalization
      RTCalibrationModule.class, LinearNormalizerModule.class,
      StandardCompoundNormalizerModule.class,

      // Data analysis
      CVPlotModule.class, LogratioPlotModule.class, PCAPlotModule.class, CDAPlotModule.class,
      SammonsPlotModule.class, ClusteringModule.class, HeatMapModule.class,
      SignificanceModule.class,

      // Identification
      LocalSpectralDBSearchModule.class, PrecursorDBSearchModule.class,
      SortSpectralDBIdentitiesModule.class, CustomDBSearchModule.class,
      FormulaPredictionModule.class, FragmentSearchModule.class, AdductSearchModule.class,
      ComplexSearchModule.class, OnlineDBSearchModule.class, LipidSearchModule.class,
      CameraSearchModule.class, NistMsSearchModule.class, FormulaPredictionPeakListModule.class,
      Ms2SearchModule.class, SiriusProcessingModule.class, GNPSResultsImportModule.class,

      // Visualizers
      TICVisualizerModule.class, SpectraVisualizerModule.class, TwoDVisualizerModule.class,
      Fx3DVisualizerModule.class, MsMsVisualizerModule.class, NeutralLossVisualizerModule.class,
      MZDistributionHistoModule.class, PeakListTableModule.class, IsotopePatternExportModule.class,
      MSMSExportModule.class, ScatterPlotVisualizerModule.class, HistogramVisualizerModule.class,
      InfoVisualizerModule.class, IntensityPlotModule.class, KendrickMassPlotModule.class,
      VanKrevelenDiagramModule.class, ProductIonFilterVisualizerModule.class,

      // Tools
      MzRangeMassCalculatorModule.class, MzRangeFormulaCalculatorModule.class,
      IsotopePatternPreviewModule.class, MsMsSpectraMergeModule.class,

      // all other regular MZmineModule (not MZmineRunnableModule) NOT LISTED IN MENU
      SpectraIdentificationSpectralDatabaseModule.class, LibrarySubmitModule.class,
      CustomDBSpectraSearchModule.class, LipidSpectraSearchModule.class,
      OnlineDBSpectraSearchModule.class, SumFormulaSpectraSearchModule.class,
      ExportScansModule.class, SpectraIdentificationResultsModule.class,

      // Data point processing, implement DataPointProcessingModule
      DataPointProcessingManager.class, DPPMassDetectionModule.class,
      DPPSumFormulaPredictionModule.class, DPPIsotopeGrouperModule.class};
}
