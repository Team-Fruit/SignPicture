package net.teamfruit.signpic.information;

import net.teamfruit.signpic.Reference;
import net.teamfruit.signpic.util.DataUtils;

public class Endpoint {
    public static Info SIGNPIC_API = new Info();

    public static boolean loadGateway() {
        final Info data = DataUtils.loadUrl(Reference.SIGN_GATEWAY, Info.class, "SignPicture API");
        if (data != null) {
            SIGNPIC_API = data;
            return true;
        }
        return false;
    }
}
