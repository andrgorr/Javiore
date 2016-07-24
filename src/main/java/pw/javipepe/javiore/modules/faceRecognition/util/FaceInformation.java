package pw.javipepe.javiore.modules.faceRecognition.util;

import com.facepp.error.FaceppParseException;
import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author: Javi
 * <p>
 * Class created on 28/05/16 as part of project Javiore
 **/
public class FaceInformation {

    @Getter JSONObject rawinfo;
    @Getter int age;
    @Getter int ageoffset;
    @Getter String gender;
    //@Getter String glass;
    @Getter String race;
    @Getter Double smile;

    public FaceInformation (JSONObject rawinfo) {
        this.rawinfo = rawinfo;
    }

    public FaceInformation fetch() throws FaceppParseException, JSONException {

        JSONObject base = this.rawinfo.getJSONArray("face").getJSONObject(0).getJSONObject("attribute");

        //age 10
        this.age = base.getJSONObject("age").getInt("value");
        //ageoffset 3
        this.ageoffset = base.getJSONObject("age").getInt("range");
        //gender female
        this.gender = base.getJSONObject("gender").getString("value");
        //glasses y/n
        //this.glass = base.getJSONObject("glass").getString("value");
        //race white
        this.race = base.getJSONObject("race").getString("value");
        //smile
        this.smile = base.getJSONObject("smiling").getDouble("value");

        return this;
    }

    public String generateParagraph() {
        return "&7My magical computer eyes can see a &c"
                + this.race.toLowerCase()
                + (this.gender.equals("Female") ? " woman" : " man")
                + "&r&7"/* + (this.glass.equals("Normal") ? "wears &cglasses&7" : "doesn't wear glasses")*/ + "."
                + " I estimate " + (this.gender.equals("Female") ? "s" : "") + "he is about &c" + this.age + " years old&r&7," +
                " I could be off by approx. &c" + this.ageoffset + " &r&7years, though." +
                (this.gender.equals("Female") ? "s" : "") + " He is also &c" + this.mood() + "&7 in this picture.";
    }

    private String mood() {
        if (smile <= 1)
            return "a bit sad";
        if (smile > 1 && smile <= 10)
            return "quite serious";
        if (smile > 10 && smile <= 40)
            return "slightly smiling";
        if (smile > 40 && smile <= 80)
            return "very happily smiling";
        return "happy as hell";
    }
}
