package gutenberg;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Getvideo {

	Getvideo() {

		int size = 4096;
		byte[] buf = new byte[size];

	}

	boolean readvideo(String destinationDir, String fAddress, String localFileName) {
		URL url;
		byte[] buf = null;
		int byteRead, byteWritten = 0;
		FilterOutputStream outStream = null;
		InputStream is = null;
		try {

			url = getFinalLocation(fAddress);
			outStream = new BufferedOutputStream(new FileOutputStream(destinationDir + "\\" + localFileName));
			URLConnection conn = url.openConnection();

			conn = url.openConnection();
			is = conn.getInputStream();

			while ((byteRead = is.read(buf)) != -1) {
				outStream.write(buf, 0, byteRead);
				byteWritten += byteRead;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				is.close();

				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;

	}

	URL getFinalLocation(String address) throws IOException {
		URL url = new URL(address);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		int status = conn.getResponseCode();
		if (status != HttpURLConnection.HTTP_OK) {
			if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM
					|| status == HttpURLConnection.HTTP_SEE_OTHER) {
				String newLocation = conn.getHeaderField("Location");
				return getFinalLocation(newLocation);
			}
		}
		return url;
	}
}
