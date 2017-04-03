package nearsoft.academy.bigdata.recommendation;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by laguna on 29/03/2017.
 */
public class MovieRecommender {

    /* CONFIGURATION */
        public static final int PRODUCT_BEGIN_INDEX = 19;
        public static final int USER_BEGIN_INDEX = 15;
        public static final int SCORE_BEGIN_INDEX = 14;
        public static final String PRODUCT_PATTERN = "product/productId: ";
        public static final String USER_PATTERN = "review/userId: ";
        public static final String SCORE_PATTERN = "review/score: ";
        public static final String DATABASE = "database.txt";
    /* CONFIGURATION */

    private int totalReviews;
    private HashMap<String, Integer> totalUsers;
    private HashBiMap<String, Integer> totalProducts;
    private Recommender recommender;

    public MovieRecommender(String pathToFile) {
        this.totalProducts = HashBiMap.create();
        this.totalUsers = new HashMap<String, Integer>();
        File database = setValues(pathToFile);
        buildRecommender(database);
    }

    private File setValues(String filePath) {
        try {
            BufferedReader reader = GzipReader.readGzip(filePath);
            FileWriter writer = new FileWriter(DATABASE);

            String lineToRead;
            String lineToWrite = "";
            int selectedProduct = 0;

            while((lineToRead = reader.readLine()) != null) {

                if(lineToRead.startsWith(PRODUCT_PATTERN)) {
                    String productId = lineToRead.substring(PRODUCT_BEGIN_INDEX);
                    if (!this.totalProducts.containsKey(productId)){
                        this.totalProducts.put(productId, this.totalProducts.size());
                        selectedProduct = this.totalProducts.get(productId);
                    }
                } else if (lineToRead.startsWith(USER_PATTERN)) {
                    String userId = lineToRead.substring(USER_BEGIN_INDEX);

                    if (!this.totalUsers.containsKey(userId)){
                        this.totalUsers.put(userId, totalUsers.size());
                    }
                    this.totalReviews++;

                    lineToWrite = this.totalUsers.get(userId) + "," + selectedProduct + ",";
                } else if (lineToRead.startsWith(SCORE_PATTERN)) {
                    double score = Double.parseDouble(lineToRead.substring(SCORE_BEGIN_INDEX));
                    lineToWrite += score + "\n";
                    writer.write(lineToWrite);
                    writer.flush();
                }
            }

            reader.close();
            writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new File(DATABASE);
    }

    public void buildRecommender(File database) {
        try {
            DataModel model = new FileDataModel(database);
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
            UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
            this.recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

        } catch (Exception ex) {
            ex.printStackTrace();
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

    public List<String> getRecommendationsForUser(String id)  {
        try {
            return getRecommendations(this.recommender.recommend(this.totalUsers.get(id), 3));
        } catch (TasteException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    private List<String> getRecommendations(List<RecommendedItem> recommendations) {
        ArrayList<String> products = new ArrayList<String>();

        for (RecommendedItem r : recommendations) {
            products.add(this.totalProducts.inverse().get((int) r.getItemID()));
        }

        return products;
    }
}
