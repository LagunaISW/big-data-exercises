package nearsoft.academy.bigdata.recommendation;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by laguna on 29/03/2017.
 */
public class MovieRecommender {

    private int totalReviews;
    private HashMap<String, Integer> totalUsers;
    private HashMap<String, Integer> totalProducts;

    public MovieRecommender(String pathToFile) {
        this.totalProducts = new HashMap<String, Integer>();
        this.totalUsers = new HashMap<String, Integer>();
        setValues(pathToFile);
    }

    private void setValues(String path){
        try {
            BufferedReader reader = GzipReader.readGzip(path);

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
