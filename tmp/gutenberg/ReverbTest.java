package gutenberg;

import java.io.IOException;
import java.util.LinkedList;

/* For representing a sentence that is annotated with pos tags and np chunks.*/
import edu.washington.cs.knowitall.nlp.ChunkedSentence;

import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;

public class ReverbTest {

	ReverbProcessing processor;

	ReverbTest() throws Exception {
		processor = new ReverbProcessing();

	}

	public LinkedList<ChunkedBinaryExtraction> chunksentence(String sentStr) throws IOException {
		ChunkedSentence chunked = processor.ReverbChunk(sentStr);
		return processor.ReverbChunk(chunked);
	}
}
