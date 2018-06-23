package gutenberg;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.FloatArrayFS;
import org.apache.uima.cas.IntArrayFS;
import org.apache.uima.cas.StringArrayFS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.examples.RunAE;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.util.FileUtils;
import org.apache.uima.util.XMLInputSource;

public class WardAE {
	public WardAE() {

		// TODO Auto-generated constructor stub
	}

	void runmain() {

	}

	/**
	 * Main program.
	 * 
	 * @param args
	 *            Command-line arguments - see class description
	 */
	public static void main(String[] args) {
		try {
			File taeDescriptor = null;
			File inputDir = null;

			// Read and validate command line arguments
			boolean validArgs = false;
			if (args.length == 2) {
				taeDescriptor = new File(args[0]);
				inputDir = new File(args[1]);

				validArgs = taeDescriptor.exists() && !taeDescriptor.isDirectory() && inputDir.isDirectory();
			}
			if (!validArgs) {
				printUsageMessage();
			} else {
				// get Resource Specifier from XML file
				XMLInputSource in = new XMLInputSource(taeDescriptor);
				ResourceSpecifier specifier = UIMAFramework.getXMLParser().parseResourceSpecifier(in);

				// for debugging, output the Resource Specifier
				// System.out.println(specifier);

				// create Analysis Engine
				AnalysisEngine ae = UIMAFramework.produceAnalysisEngine(specifier);
				// create a CAS
				CAS cas = ae.newCAS();

				// get all files in the input directory
				File[] files = inputDir.listFiles();
				if (files == null) {
					System.out.println("No files to process");
				} else {
					// process documents
					for (int i = 0; i < files.length; i++) {
						if (!files[i].isDirectory()) {
							processFile(files[i], ae, cas);
						}
					}
				}
				ae.destroy();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints usage message.
	 */
	public static void printUsageMessage() {
		System.err.println("Usage: java org.apache.uima.example.ExampleApplication "
				+ "<Analysis Engine descriptor or PEAR file name> <input dir>");
	}

	/**
	 * Processes a single XML file and prints annotations to System.out
	 * 
	 * @param aFile
	 *            file to process
	 * @param aAE
	 *            Analysis Engine that will process the file
	 * @param aCAS
	 *            CAS that will be used to hold analysis results
	 */
	public static void processFile(File aFile, AnalysisEngine aAE, CAS aCAS)
			throws IOException, AnalysisEngineProcessException {
		System.out.println("Processing file " + aFile.getName());

		String document = FileUtils.file2String(aFile);
		document = document.trim();

		// put document text in CAS
		aCAS.setDocumentText(document);

		// process
		aAE.process(aCAS);

		// print annotations to System.out
		printAnnotations(aCAS, System.out);

		// reset the CAS to prepare it for processing the next document
		aCAS.reset();
	}

	public static void printAnnotations(CAS aCAS, PrintStream aOut) {

		// Version 3 using select with Stream support
		// aCAS.select(Annotation.class).forEach(fs -> printFS(fs, aCAS, 0, aOut));

		// Version 3 using select with extended for
		for (Annotation fs : aCAS.getAnnotationIndex().select(Annotation.class)) {
			printFS(fs, aCAS, 0, aOut);
		}
		//
		// // version 2 style using iterators
		// FSIterator<AnnotationFS> iter = aCAS.getAnnotationIndex().iterator();
		//
		// // iterate
		// while (iter.isValid()) {
		// FeatureStructure fs = iter.get();
		// printFS(fs, aCAS, 0, aOut);
		// iter.moveToNext();
		// }

	}

	/**
	 * Prints all Annotations of a specified Type to a PrintStream.
	 * 
	 * @param aCAS
	 *            the CAS containing the FeatureStructures to print
	 * @param aAnnotType
	 *            the Type of Annotation to be printed
	 * @param aOut
	 *            the PrintStream to which output will be written
	 */
	public static void printAnnotations(CAS aCAS, Type aAnnotType, PrintStream aOut) {
		// get iterator over annotations
		FSIterator iter = aCAS.getAnnotationIndex(aAnnotType).iterator();

		// iterate
		while (iter.isValid()) {
			FeatureStructure fs = iter.get();
			printFS(fs, aCAS, 0, aOut);
			iter.moveToNext();
		}
	}

	/**
	 * Prints a FeatureStructure to a PrintStream.
	 * 
	 * @param aFS
	 *            the FeatureStructure to print
	 * @param aCAS
	 *            the CAS containing the FeatureStructure
	 * @param aNestingLevel
	 *            number of tabs to print before each line
	 * @param aOut
	 *            the PrintStream to which output will be written
	 */
	public static void printFS(FeatureStructure aFS, CAS aCAS, int aNestingLevel, PrintStream aOut) {
		Type stringType = aCAS.getTypeSystem().getType(CAS.TYPE_NAME_STRING);

		printTabs(aNestingLevel, aOut);
		aOut.println(aFS.getType().getName());

		// if it's an annotation, print the first 64 chars of its covered text
		if (aFS instanceof AnnotationFS) {
			AnnotationFS annot = (AnnotationFS) aFS;
			String coveredText = annot.getCoveredText();
			printTabs(aNestingLevel + 1, aOut);
			aOut.print("\"");
			if (coveredText.length() <= 64) {
				aOut.print(coveredText);
			} else {
				aOut.println(coveredText.substring(0, 64) + "...");
			}
			aOut.println("\"");
		}

		// print all features
		List aFeatures = aFS.getType().getFeatures();
		Iterator iter = aFeatures.iterator();
		while (iter.hasNext()) {
			Feature feat = (Feature) iter.next();
			printTabs(aNestingLevel + 1, aOut);
			// print feature name
			aOut.print(feat.getShortName());
			aOut.print(" = ");
			// prnt feature value (how we get this depends on feature's range type)
			String rangeTypeName = feat.getRange().getName();
			if (aCAS.getTypeSystem().subsumes(stringType, feat.getRange())) // must check for subtypes of
																			// string
			{
				String str = aFS.getStringValue(feat);
				if (str == null) {
					aOut.println("null");
				} else {
					aOut.print("\"");
					if (str.length() > 64) {
						str = str.substring(0, 64) + "...";
					}
					aOut.print(str);
					aOut.println("\"");
				}
			} else if (CAS.TYPE_NAME_INTEGER.equals(rangeTypeName)) {
				aOut.println(aFS.getIntValue(feat));
			} else if (CAS.TYPE_NAME_FLOAT.equals(rangeTypeName)) {
				aOut.println(aFS.getFloatValue(feat));
			} else if (CAS.TYPE_NAME_STRING_ARRAY.equals(rangeTypeName)) {
				StringArrayFS arrayFS = (StringArrayFS) aFS.getFeatureValue(feat);
				if (arrayFS == null) {
					aOut.println("null");
				} else {
					String[] vals = arrayFS.toArray();
					aOut.print("[");
					for (int i = 0; i < vals.length - 1; i++) {
						aOut.print(vals[i]);
						aOut.print(',');
					}
					if (vals.length > 0) {
						aOut.print(vals[vals.length - 1]);
					}
					aOut.println("]\"");
				}
			} else if (CAS.TYPE_NAME_INTEGER_ARRAY.equals(rangeTypeName)) {
				IntArrayFS arrayFS = (IntArrayFS) aFS.getFeatureValue(feat);
				if (arrayFS == null) {
					aOut.println("null");
				} else {
					int[] vals = arrayFS.toArray();
					aOut.print("[");
					for (int i = 0; i < vals.length - 1; i++) {
						aOut.print(vals[i]);
						aOut.print(',');
					}
					if (vals.length > 0) {
						aOut.print(vals[vals.length - 1]);
					}
					aOut.println("]\"");
				}
			} else if (CAS.TYPE_NAME_FLOAT_ARRAY.equals(rangeTypeName)) {
				FloatArrayFS arrayFS = (FloatArrayFS) aFS.getFeatureValue(feat);
				if (arrayFS == null) {
					aOut.println("null");
				} else {
					float[] vals = arrayFS.toArray();
					aOut.print("[");
					for (int i = 0; i < vals.length - 1; i++) {
						aOut.print(vals[i]);
						aOut.print(',');
					}
					if (vals.length > 0) {
						aOut.print(vals[vals.length - 1]);
					}
					aOut.println("]\"");
				}
			} else // non-primitive type
			{
				FeatureStructure val = aFS.getFeatureValue(feat);
				if (val == null) {
					aOut.println("null");
				} else {
					printFS(val, aCAS, aNestingLevel + 1, aOut);
				}
			}
		}
	}

	/**
	 * Prints tabs to a PrintStream.
	 * 
	 * @param aNumTabs
	 *            number of tabs to print
	 * @param aOut
	 *            the PrintStream to which output will be written
	 */
	private static void printTabs(int aNumTabs, PrintStream aOut) {
		for (int i = 0; i < aNumTabs; i++) {
			aOut.print("\t");
		}
	}

}
