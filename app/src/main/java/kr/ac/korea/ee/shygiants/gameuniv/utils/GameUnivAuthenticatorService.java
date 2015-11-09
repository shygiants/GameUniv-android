package kr.ac.korea.ee.shygiants.gameuniv.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by SHYBook_Air on 15. 11. 8..
 */
public class GameUnivAuthenticatorService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        GameUnivAuthenticator authenticator = new GameUnivAuthenticator(this);
        return authenticator.getIBinder();
    }
}
