package pw.javipepe.javiore.modules.faceRecognition.util;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import lombok.Getter;

/**
 * @author: Javi
 * <p>
 * Class created on 28/05/16 as part of project Javiore
 **/
public class Face {

    @Getter String url;

    public Face (String url) {
        this.url = url;
    }

    public FaceInformation scan() throws FaceppParseException {
        HttpRequests httpRequests = new HttpRequests("94ad7644df7b077293bee69b1d8749d3", "vL6Sr1eXofGMJQ_UPnKkCqxFjWAWeFbx", false, true);
        PostParameters postParameters =
                new PostParameters()
                        .setUrl(this.url)
                        .setAttribute("all");
        return new FaceInformation(httpRequests.detectionDetect(postParameters));
    }
}
