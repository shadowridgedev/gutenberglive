package gutenberg;
/*
import edu.illinois.cs.cogcomp.core.datastructures.textannotation.TextAnnotation;
import edu.illinois.cs.cogcomp.core.utilities.configuration.ResourceManager;
import edu.illinois.cs.cogcomp.nlp.utility.TokenizerTextAnnotationBuilder;
import edu.illinois.cs.cogcomp.annotation.AnnotatorException;
import edu.illinois.cs.cogcomp.annotation.TextAnnotationBuilder;
import edu.illinois.cs.cogcomp.core.datastructures.ViewNames;
import edu.illinois.cs.cogcomp.ner.NERAnnotator;
import edu.illinois.cs.cogcomp.nlp.tokenizer.StatefulTokenizer;
import edu.illinois.cs.cogcomp.ner.LbjTagger;
import java.io.IOException;

import java.util.Properties;

public class Process {

	public NERAnnotator DoIt(String text1, String corpus, String textId) throws IOException, AnnotatorException {
		text1 = "Good afternoon, gentlemen. I am a HAL-9000 " + "computer. I was born in Urbana, Il. in 1992";

		corpus = "2001_ODYSSEY";
		textId = "001";

		// Create a TextAnnotation using the LBJ sentence splitter // and tokenizers.
		TextAnnotationBuilder tab; // don't split on hyphens, as NER models are trained this way

		boolean splitOnHyphens = false;
		tab = new TokenizerTextAnnotationBuilder(new StatefulTokenizer(splitOnHyphens));

		TextAnnotation ta = tab.createTextAnnotation(corpus, textId, text1);

		NERAnnotator co = new NERAnnotator(ViewNames.NER_CONLL);

		co.getView(ta);

		System.out.println(ta.getView(ViewNames.NER_CONLL));

		return co;
	}
}
*/