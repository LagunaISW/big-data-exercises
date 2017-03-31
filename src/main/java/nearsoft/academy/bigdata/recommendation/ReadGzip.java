package nearsoft.academy.bigdata.recommendation;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by laguna on 30/03/2017.
 */
public class ReadGzip {

    public static BufferedReader readGzip(String filePath) throws IOException {
        InputStream fileStream = new FileInputStream(filePath);
        InputStream gzipStream = new GZIPInputStream(fileStream);
        Reader decoder = new InputStreamReader(gzipStream, "UTF-8");

        return new BufferedReader(decoder);
    }

}
