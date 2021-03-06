package net.myexperiments.gutenberg;

import java.io.File;
import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.common.params.ExpandParams;
import org.apache.solr.common.util.NamedList;

public class SolrCellRequestDemo {
	public NamedList<Object> test(String host, String collection, String filepath, String filetype)
			throws IOException, SolrServerException {
		String path = "http://" + host + ":8983/solr/" + collection;
		SolrClient client = new HttpSolrClient.Builder(path).build();
		ContentStreamUpdateRequest req = new ContentStreamUpdateRequest("/update/extract");
		req.addFile(new File(filepath), filetype);
		req.setParam(ExpandParams.EXPAND, "true");
		NamedList<Object> result = client.request(req);
		System.out.println("Result: " + result);
		return result;

	}
}
