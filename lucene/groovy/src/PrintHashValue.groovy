
import org.apache.solr.common.util.Hash

/**
 * Created by kkim on 1/28/16.
 */


class PrintHashValue {
    static def main(args) {
        String id="TT1893369060"
        println Integer.toHexString(Hash.murmurhash3_x86_32(id,0,id.length(),0))
    }
}
