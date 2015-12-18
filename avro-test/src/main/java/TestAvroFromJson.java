import org.apache.avro.Schema;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.JsonDecoder;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by kkim on 8/3/15.
 */
public class TestAvroFromJson {

    public static void main(String[] args) {

        try {
            FileInputStream fis = new FileInputStream("/Users/kkim/tmp/Twitter1_000000.gz");
            GZIPInputStream gis = new GZIPInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(gis));

            String line=null;
            while((line=br.readLine())!=null) {
                System.out.println(line);

                Schema schema = new Schema.Parser().parse(new File("schema"));

                JsonDecoder decoder = DecoderFactory.get().jsonDecoder();
            }

            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
