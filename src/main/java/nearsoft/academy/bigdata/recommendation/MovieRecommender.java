package nearsoft.academy.bigdata.recommendation;

import com.google.common.collect.HashBiMap;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Created by laguna on 29/03/2017.
 */
public class MovieRecommender {

    private int totalReviews;
    private HashMap<String, Integer> totalUsers;
    private HashBiMap<String, Integer> totalProducts;

    public MovieRecommender(String pathToFile) {
        this.totalProducts = HashBiMap.create();
        this.totalUsers = new HashMap<String, Integer>();
        setValues(pathToFile);
    }

    private void setValues(String path){
        try {
            InputStream fileStream = new FileInputStream(path);
            InputStream gzipStream = new GZIPInputStream(fileStream);
            Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
            BufferedReader reader = new BufferedReader(decoder);

            String line;

            while((line = reader.readLine()) != null) {
                if (line.startsWith("product/productId: ")) {
                    String productId = line.substring(19);
                    if (!this.totalProducts.containsKey(productId))
                        this.totalProducts.put(productId, this.totalProducts.size());

                }

                else if (line.startsWith("review/userId: ")) {
                    String userId = line.substring(15);
                    if (!this.totalUsers.containsKey(userId))
                        this.totalUsers.put(userId, totalUsers.size());
                    this.totalReviews++;
                }
            }

            reader.close();
            decoder.close();
            gzipStream.close();
            fileStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    public int getTotalProducts() {
        return totalProducts.size();
    }

    public int getTotalUsers() {
        return totalUsers.size();
    }

    public List<String> getRecommendationsForUser(String id) {
        return null;
    }
}
