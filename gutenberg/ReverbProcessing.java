package gutenberg;

import java.io.IOException;
import java.util.LinkedList;

/* For representing a sentence that is annotated with pos tags and np chunks.*/
import edu.washington.cs.knowitall.nlp.ChunkedSentence;

/* String -> ChunkedSentence */
import edu.washington.cs.knowitall.nlp.OpenNlpSentenceChunker;

/* The class that is responsible for extraction. */
import edu.washington.cs.knowitall.extractor.ReVerbExtractor;

/* The class that is responsible for assigning a confidence score to an
 * extraction.
 */
import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunction;
import edu.washington.cs.knowitall.extractor.conf.ReVerbOpenNlpConfFunction;

/* A class for holding a (arg1, rel, arg2) triple. */
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;

public class ReverbProcessing {

	ReVerbExtractor reverb = new ReVerbExtractor();
	ConfidenceFunction confFunc = new ReVerbOpenNlpConfFunction();
	OpenNlpSentenceChunker chunker = new OpenNlpSentenceChunker();

	ReverbProcessing() throws Exception {

	}

	ChunkedSentence ReverbChunk(String sentStr) throws IOException {

		ChunkedSentence sent = chunker.chunkSentence(sentStr);
		// Looks on the classpath for the default model files.

		// Prints out the (token, tag, chunk-tag) for the sentence
		System.out.println(sentStr);

		for (int i = 0; i < sent.getLength(); i++) {
			String token = sent.getToken(i);
			String posTag = sent.getPosTag(i);
			String chunkTag = sent.getChunkTag(i);
			System.out.println(token + " " + posTag + " " + chunkTag);
		}
		return sent;
	}

	// Prints out extractions from the sentence.
	LinkedList<ChunkedBinaryExtraction> ReverbChunk(ChunkedSentence sent) {
		LinkedList<ChunkedBinaryExtraction> extractions = new LinkedList<ChunkedBinaryExtraction>();

		for (ChunkedBinaryExtraction extr : reverb.extract(sent)) {
			extractions.add(extr);
			double conf = confFunc.getConf(extr);
			System.out.println("Arg1=" + extr.getArgument1());
			System.out.println("Rel=" + extr.getRelation());
			System.out.println("Arg2=" + extr.getArgument2());
			System.out.println("Conf=" + conf);
		}
		return extractions;
	}

}
